package dataStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import utils.Point3D;

public class GameAlgo {
	private ArrayList<Fruit> fruitList;
	private ArrayList<Robot> robotList;
	private graph_algorithms graph_algo = new Graph_Algo();

	public static final double EPS = 0.000001;

	/**
	 * Empty constructor , init lists
	 */
	public GameAlgo() {
		fruitList = new ArrayList<>();
		robotList = new ArrayList<>();
	}

	/**
	 * Function to init robots on the nodes of the graph The robots come from game
	 * json
	 * 
	 * @param g
	 * @param game
	 */
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

	/**
	 * Function to init fruits on edges in the graph by position The fruits come
	 * from game json
	 * 
	 * @param g
	 * @param game
	 */
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

	/**
	 * Function to check if the fruit is on edge based on position and fruit type
	 * 
	 * @param e
	 * @param f
	 * @param g
	 * @return
	 */
	private boolean isFruitOnEdge(edge_data e, Fruit f, graph g) {
		boolean ans = false;
		int src = e.getSrc();
		int dest = e.getDest();
		if (fruitList.contains(f))
			return false;
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

	/**
	 * Function to update robots from the game
	 * 
	 * @param game
	 */
	public void updateRobots(game_service game) {
		robotList = new ArrayList<>();
		synchronized (robotList) {
			List<String> robots = game.getRobots();
			for (String string : robots) {
				this.addRobot(new Robot(string));
			}
		}

	}

	/**
	 * Function to add fruit to the list
	 * 
	 * @param f
	 */
	private void addFruit(Fruit f) {
		this.fruitList.add(f);
	}

	/**
	 * Function to get list of the fruits
	 * 
	 * @return
	 */
	public ArrayList<Fruit> fruitList() {
		return this.fruitList;
	}

	/**
	 * Function to sort fruitList with fruit comparator
	 * 
	 * @param comp
	 */
	private void sortFruits(Comparator<Fruit> comp) {
		this.fruitList.sort(comp);
	}

	/**
	 * Function to add robot to the list
	 * 
	 * @param r
	 */
	private void addRobot(Robot r) {
		this.robotList.add(r);
	}

	/**
	 * Function to get list of the robots
	 * 
	 * @return
	 */
	public ArrayList<Robot> robotList() {
		return this.robotList;
	}

	/**
	 * Function to get robot by id
	 * 
	 * @param id
	 * @return
	 */
	public Robot getRobotById(int id) {
		for (Robot robot : robotList) {
			if (robot.getId() == id)
				return robot;
		}
		return null;
	}

	/**
	 * Function to move robot (use on manually)
	 * 
	 * @param id
	 * @param dest
	 * @param game
	 * @param g
	 */
	public void moveRobot(int id, int dest, game_service game, graph g) {
		Robot r = getRobotById(id);
		if (r == null)
			throw new RuntimeException("Robot with id: " + id + " doesn't exist");
		if (g.getEdge(r.getSrc(), dest) == null)
			throw new RuntimeException("There is no edge between src: " + r.getSrc() + " dest: " + dest);
		game.chooseNextEdge(id, dest);
	}

	/**
	 * Function to move robots with auto algorithm
	 * 
	 * @param game
	 * @param g
	 */
	public void moveRobotsAuto(game_service game, graph g) {
		graph_algo.init(g);
		synchronized (robotList) {
			for (Robot r : robotList) {
				int i = 0;
				boolean found = false;
				while (!found) {
					Fruit f = fruitList.get(i);
					if (f.isOnTarget()) {
						i++;
						continue;
					}
					edge_data e = f.getEdge();
					int src = r.getSrc();
					int dest = -1;
					int last = -1;
					if (f.getType() == -1) {
						dest = e.getDest() > e.getSrc() ? e.getSrc() : e.getDest();
						last = e.getDest() > e.getSrc() ? e.getDest() : e.getSrc();
					}
					if (f.getType() == 1) {
						dest = e.getDest() > e.getSrc() ? e.getDest() : e.getSrc();
						last = e.getDest() > e.getSrc() ? e.getSrc() : e.getDest();
					}
					List<node_data> path = graph_algo.shortestPath(src, dest);
					path.add(new Node(last));
					if (path != null && path.size() > 1) {
						moveByPath(r.getId(), path, game);
						f.setOnTarget(true);
						found = true;
						continue;
					} else {
						i++;
					}
					if (i == fruitList.size()) {
						found = true;
						continue;
					}
				}
				i++;
			}
		}
	}

	/**
	 * Function to choose next edge by game server function
	 * 
	 * @param rid
	 * @param path
	 * @param game
	 */
	public void moveByPath(int rid, List<node_data> path, game_service game) {
		for (int i = 1; i < path.size(); i++) {
			node_data n = path.get(i);
			game.chooseNextEdge(rid, n.getKey());
		}
	}
}
