package dataStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import utils.Point3D;

public class GameAlgo {
	private ArrayList<Fruit> fruitList;
	private ArrayList<Robot> robotList;

	public static final double EPS = 0.000001;

	public GameAlgo() {
		fruitList = new ArrayList<>();
		robotList = new ArrayList<>();
	}

	public void initRobotsOnNodes(graph g, game_service game) {
		if (g == null)
			throw new RuntimeException("Please fill graph first");
		
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject gs = line.getJSONObject("GameServer");
			int rs = gs.getInt("robots");
			for (int i = 0; i < rs; i++) {
				if (this.fruitList().size() > i) {
					game.addRobot(this.fruitList.get(i).getEdge().getSrc());
				} else {
					game.addRobot(i);
				}
			}
			this.updateRobots(game);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initFruitsOnEdges(graph g, game_service game) {
		if (g == null)
			throw new RuntimeException("Please fill graph first");
		fruitList = new ArrayList<>();
		synchronized (fruitList) {
			Iterator<String> f_iter = game.getFruits().iterator();
			while (f_iter.hasNext()) {
				String next = f_iter.next();
				Fruit f = new Fruit(next);
				for (node_data src : g.getV()) {
					Collection<edge_data> e = g.getE(src.getKey());
					if (e != null) {
						for (edge_data edge : e) {
							if (isFruitOnEdge(edge, f, g)) {
								f.setEdge(edge);
								this.addFruit(f);
								g.upgradeMC();
							}
						}
					}
				}
			}
			this.sortFruits(Fruit.comp);
		}
	}

	private boolean isFruitOnEdge(edge_data e, Fruit f, graph g) {
		boolean ans = false;
		int src = e.getSrc();
		int dest = e.getDest();
		if (f.getType() < 0 && dest > src)
			return false;
		if (f.getType() > 0 && src > dest)
			return false;
		Point3D fruitLocation = f.getLocation();
		Point3D srcLocation = g.getNode(src).getLocation();
		Point3D destLocation = g.getNode(dest).getLocation();
		double dist = srcLocation.distance2D(destLocation);
		double dist2 = srcLocation.distance2D(fruitLocation) + fruitLocation.distance2D(destLocation);

		if (dist > dist2 - EPS)
			ans = true;

		return ans;
	}

	public void updateRobots(game_service game) {
		robotList = new ArrayList<>();
		synchronized (robotList) {
			List<String> robots = game.getRobots();
			for (String string : robots) {
				this.addRobot(new Robot(string));
			}
		}

	}

	private void addFruit(Fruit f) {
		this.fruitList.add(f);
	}

	public ArrayList<Fruit> fruitList() {
		return this.fruitList;
	}

	private void sortFruits(Comparator<Fruit> comp) {
		this.fruitList.sort(comp);
	}

	private void addRobot(Robot r) {
		this.robotList.add(r);
	}

	public ArrayList<Robot> robotList() {
		return this.robotList;
	}

	public Robot getRobotById(int id) {
		for (Robot robot : robotList) {
			if (robot.getId() == id)
				return robot;
		}
		return null;
	}

	public void moveRobot(int id, int dest, game_service game, graph g) {
		Robot r = getRobotById(id);
		if (r == null)
			throw new RuntimeException("Robot with id: " + id + " doesn't exist");
		if (g.getEdge(r.getSrc(), dest) == null)
			throw new RuntimeException("There is no edge between src: " + r.getSrc() + " dest: " + dest);
		game.chooseNextEdge(id, dest);
	}

	public void moveRobots(graph g, game_service game) {
		long t = game.timeToEnd();
		List<String> robots = game.getRobots();
		for (String robot_json : robots) {
			try {
				JSONObject line = new JSONObject(robot_json);
				JSONObject ttt = line.getJSONObject("Robot");
				int rid = ttt.getInt("id");
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");

				if (dest == -1) {
					dest = nextNode(g, src);
					game.chooseNextEdge(rid, dest);
//						System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
//						System.out.println(ttt);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 * 
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int) (Math.random() * s);
		int i = 0;
		while (i < r) {
			itr.next();
			i++;
		}
		ans = itr.next().getDest();
		return ans;
	}
}
