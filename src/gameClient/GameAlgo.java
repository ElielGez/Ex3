package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class GameAlgo {
	private ArrayList<Fruit> fruitList;
	private ArrayList<Robot> robotList;
	private graph_algorithms graph_algo = new Graph_Algo();
	private RobotFruitPathContainer rfpArray;
	private double x_scale[];
	private double y_scale[];
	private long dt = 50;

	public long getDt() {
		return dt;
	}

	public void setDt(long dt) {
		this.dt = dt;
	}

	private final int WIDTH = 1000;
	private final int HEIGHT = 1000;
	private final int X_SCALE_TMIN = 15;
	private final int Y_SCALE_TMIN = 200;
	private final int Y_SCALE_TMAX = 50;
	public static final double EPS = 0.000001;

	/**
	 * Empty constructor , init lists
	 */
	public GameAlgo() {
		fruitList = new ArrayList<>();
		robotList = new ArrayList<>();
		rfpArray = new RobotFruitPathContainer();
	}

	/**
	 * Function that scale the locations of nodes,robots and fruits
	 */
	public void initNodes(graph g, KML_Logger log) {
		this.x_scale = xAxis_Min_Max(g);
		this.y_scale = yAxis_Min_Max(g);

		for (node_data n : g.getV()) {
			double x = scale(n.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN, WIDTH - Y_SCALE_TMAX);
			double y = scale(n.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN, HEIGHT - Y_SCALE_TMAX);
			Point3D p = new Point3D(x, y);
			n.setGuiLocation(p);
			log.addNodePlaceMark("" + n.getLocation());
			for (edge_data edge : g.getE(n.getKey())) {
				node_data dest = g.getNode(edge.getDest());
				node_data src = g.getNode(edge.getSrc());
				log.addEdgePlacemark("" + src.getLocation(), "" + dest.getLocation());
			}
		}
	}

	/**
	 * Function to get min and max of xAxis , used on scale function
	 * 
	 * @param g
	 * @return
	 */
	private double[] xAxis_Min_Max(graph g) {
		double arr[] = { Double.MAX_VALUE, Double.MIN_VALUE }; // min [0] max [1]
		for (node_data n : g.getV()) {
			Point3D p = n.getLocation();
			if (p.x() < arr[0])
				arr[0] = p.x();
			if (p.x() > arr[1])
				arr[1] = p.x();

		}
		return arr;
	}

	/**
	 * Function to get min and max of yAxis , used on scale function
	 * 
	 * @param g
	 * @return
	 */
	private double[] yAxis_Min_Max(graph g) {
		double arr[] = { Double.MAX_VALUE, Double.MIN_VALUE }; // min [0] max [1]
		for (node_data n : g.getV()) {
			Point3D p = n.getLocation();
			if (p.y() < arr[0])
				arr[0] = p.y();
			if (p.y() > arr[1])
				arr[1] = p.y();

		}
		return arr;
	}

	/**
	 * Function scale some point
	 * 
	 * @param data
	 * @param r_min
	 * @param r_max
	 * @param t_min
	 * @param t_max
	 * @return
	 */
	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	/**
	 * Function to init robots on the nodes of the graph The robots come from game
	 * json
	 * 
	 * @param g
	 * @param game
	 */
	public void initRobotsOnNodes(graph g, game_service game, KML_Logger log) {
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
			this.updateRobots(game, log);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

	}

	/**
	 * Function to init fruits on edges in the graph by position The fruits come
	 * from game json
	 * 
	 * @param g
	 * @param game
	 */
	public void initFruitsOnEdges(graph g, game_service game, KML_Logger log) {
		if (g == null)
			throw new RuntimeException("Please fill graph first");
		fruitList = new ArrayList<>();
		synchronized (fruitList) {
			Iterator<String> f_iter = game.getFruits().iterator();
			while (f_iter.hasNext()) {
				String next = f_iter.next();
				Fruit f = new Fruit(next);
				double xF = scale(f.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN, WIDTH - Y_SCALE_TMAX);
				double yF = scale(f.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN, HEIGHT - Y_SCALE_TMAX);
				Point3D pF = new Point3D(xF, yF);
				f.setGuiLocation(pF);
				log.addFruitPlaceMark(f.getType() == -1 ? "banana" : "apple", "" + f.getLocation());
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
	public void updateRobots(game_service game, KML_Logger log) {
		robotList = new ArrayList<>();
		synchronized (robotList) {
			List<String> robots = game.getRobots();
			for (String string : robots) {
				Robot r = new Robot(string);
				double xR = scale(r.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN, WIDTH - Y_SCALE_TMAX);
				double yR = scale(r.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN, HEIGHT - Y_SCALE_TMAX);
				Point3D pR = new Point3D(xR, yR);
				r.setGuiLocation(pR);
				log.addRobotPlaceMark("" + r.getLocation());
				this.addRobot(r);
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
	 * Function to move robots by auto
	 * 
	 * @param game
	 * @param g
	 */
	public void moveRobotsAuto(game_service game, graph g) {
		synchronized (game) {
			graph_algo.init(g);
			rfpArray = new RobotFruitPathContainer();
			synchronized (robotList) {
				for (Robot r : robotList) {
					synchronized (fruitList) {
						for (Fruit f : fruitList) {
							List<node_data> path = findPathsRobotToFruit(r, f);
							if (path == null) { // no paths for this robot to fruit , go next fruit
								continue;
							} else {// there is path , find the distance
								double distance = Math.abs(f.getGuiLocation().distance2D(r.getGuiLocation()));
								rfpArray.add(new RobotFruitPath(distance, f, r, path));
							}
						}
					}
				}
			}
			rfpArray.sort();
			for (RobotFruitPath rfp : rfpArray.getList()) {
				if (!rfp.getF().isOnTarget()) {
					moveByPath(rfp.getR(), rfp.getPath(), game);
					rfp.getF().setOnTarget(true);
				}

			}
		}
	}

	/**
	 * Function to find path by dikstra between robot and fruit
	 * 
	 * @param r
	 * @param f
	 * @return
	 */
	private List<node_data> findPathsRobotToFruit(Robot r, Fruit f) {
		edge_data e = f.getEdge();
		int src = r.getSrc();
		int dest = -1;
		int last = -1;
		if (f.getType() == -1) {
			dest = e.getDest() > e.getSrc() ? e.getDest() : e.getSrc();
			last = e.getDest() > e.getSrc() ? e.getSrc() : e.getDest();
		}
		if (f.getType() == 1) {
			dest = e.getDest() < e.getSrc() ? e.getDest() : e.getSrc();
			last = e.getDest() < e.getSrc() ? e.getSrc() : e.getDest();
		}
		List<node_data> path = graph_algo.shortestPath(src, dest);
		path.add(new Node(last));
		if (path != null && path.size() > 1)
			return path;
		else
			return null;
	}

	/**
	 * Function to choose next edge by game server function
	 * 
	 * @param rid
	 * @param path
	 * @param game
	 */
	public void moveByPath(Robot r, List<node_data> path, game_service game) {
		for (int i = 1; i < path.size(); i++) {
			node_data n = path.get(i);
			game.chooseNextEdge(r.getId(), n.getKey());
		}
	}

	private long calculateDt(graph g, Fruit f, Robot r) {
		long dt;
		Point3D pSrc = g.getNode(f.getEdge().getSrc()).getGuiLocation();
		Point3D pDest = g.getNode(f.getEdge().getDest()).getGuiLocation();
		double n = (f.getGuiLocation().distance2D(pDest) / pSrc.distance2D(pDest));
		dt = (long) ((n * f.getEdge().getWeight()) / r.getSpeed());
//		System.out.println(dt);
		return dt;
	}
}
