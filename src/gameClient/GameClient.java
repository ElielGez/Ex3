package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

public class GameClient implements Runnable {
	private boolean isManual;
	private DGraph dg;
	private game_service game;
	private ArrayList<Fruit> fruitList;
	private ArrayList<Robot> robotList;

	public static final double EPS = 0.000001;

	public GameClient() {
	}

	public void initGame(int stage) {
		game = Game_Server.getServer(stage);
		dg = new DGraph();
		dg.initFromJson(game.getGraph());
		fruitList = new ArrayList<Fruit>();

		initFruitsOnEdges();
		initRobotsOnNodes();
	}

	public void initRobotsOnNodes() {
		if (dg == null)
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
			this.updateRobots();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public long getGameClock() {
		return (game.timeToEnd() / 1000);
	}

	public void updateRobots() {
		robotList = new ArrayList<Robot>();
		synchronized (robotList) {
			List<String> robots = game.getRobots();
			for (String string : robots) {
				this.addRobot(new Robot(string));
			}
		}

	}

	public void updateFruits() {
		initFruitsOnEdges();

	}

	public void initFruitsOnEdges() {
		if (dg == null)
			throw new RuntimeException("Please fill graph first");
		fruitList = new ArrayList<Fruit>();
		synchronized (fruitList) {
			Iterator<String> f_iter = game.getFruits().iterator();
			while (f_iter.hasNext()) {
				String next = f_iter.next();
				Fruit f = new Fruit(next);
				for (node_data src : dg.getV()) {
					Collection<edge_data> e = dg.getE(src.getKey());
					if (e != null) {
						for (edge_data edge : e) {
							if (isFruitOnEdge(edge, f)) {
								f.setEdge(edge);
								this.addFruit(f);
								dg.upgradeMC();
							}
						}
					}
				}
			}
			this.sortFruits(Fruit.comp);
		}
	}

	public DGraph getGraph() {
		return this.dg;
	}

	public boolean isFruitOnEdge(edge_data e, Fruit f) {
		boolean ans = false;
		int src = e.getSrc();
		int dest = e.getDest();
		if (f.getType() < 0 && dest > src)
			return false;
		if (f.getType() > 0 && src > dest)
			return false;
		Point3D fruitLocation = f.getLocation();
		Point3D srcLocation = dg.getNode(src).getLocation();
		Point3D destLocation = dg.getNode(dest).getLocation();
		double dist = srcLocation.distance2D(destLocation);
		double dist2 = srcLocation.distance2D(fruitLocation) + fruitLocation.distance2D(destLocation);

		if (dist > dist2 - EPS)
			ans = true;

		return ans;
	}

	public List<String> getGameRobots() {
		return game.getRobots();
	}

	private void addFruit(Fruit f) {
		this.fruitList.add(f);
	}

//	private void removeFruit(int index) {
//		this.fruitList.remove(index);
//	}

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

	private Robot getRobotById(int id) {
		for (Robot robot : robotList) {
			if (robot.getId() == id)
				return robot;
		}
		return null;
	}

	public void moveRobot(int id, int dest) {
		Robot r = getRobotById(id);
		if (r == null)
			throw new RuntimeException("Robot with id: " + id + " doesn't exist");
		if (dg.getEdge(r.getSrc(), dest) == null)
			throw new RuntimeException("There is no edge between src: " + r.getSrc() + " dest: " + dest);
		game.chooseNextEdge(id, dest);
	}

	private void moveRobots() {
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
					dest = nextNode(dg, src);
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
	private static int nextNode(DGraph g, int src) {
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

	@Override
	public void run() {
		game.startGame();
		int ind = 0;
		long dt = 50;
		while (game.isRunning()) {
			if (!isManual) {
				moveRobots();
			}
			updateRobots();
			updateFruits();
			try {
				if (ind % 2 == 0) {
					game.move();
					dg.upgradeMC();
				}
				Thread.sleep(dt);
				ind++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setIsManual(boolean isManual) {
		this.isManual = isManual;
	}

	public boolean IsManual() {
		return this.isManual;
	}
}
