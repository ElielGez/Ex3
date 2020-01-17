package gameClient;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.node_data;

public class GameClient implements Runnable {
	private boolean isManual;
	private graph g;
	private game_service game;
	private GameAlgo game_algo;
	private KML_Logger log;

	public static final double EPS = 0.000001;

	/**
	 * Empty constructor
	 */
	public GameClient() {
	}

	/**
	 * Constructor to create game client with stage
	 * 
	 * @param stage
	 */
	public GameClient(int stage) {
		log = new KML_Logger("" + stage);
		game = Game_Server.getServer(stage);
		g = new DGraph();

		g.initFromJson(game.getGraph());
		initNodesLog();

		game_algo = new GameAlgo();
		game_algo.initFruitsOnEdges(g, game, log);
		game_algo.initRobotsOnNodes(g, game, log);
	}

	/**
	 * Getter for game_algo
	 * 
	 * @return
	 */
	public GameAlgo getGameAlgo() {
		return this.game_algo;
	}

	/**
	 * Getter for game clock
	 * 
	 * @return
	 */
	public long getGameClock() {
		return (game.timeToEnd() / 1000);
	}

	/**
	 * Getter for graph
	 * 
	 * @return
	 */
	public graph getGraph() {
		return this.g;
	}

	/**
	 * Getter for game
	 * 
	 * @return
	 */
	public game_service getGame() {
		return this.game;
	}

	/**
	 * Getter for game server json
	 * 
	 * @return
	 */
	private JSONObject getGameServerJson() {
		String results = game.toString();
		JSONObject gs = null;
		try {
			JSONObject line = new JSONObject(results);
			gs = line.getJSONObject("GameServer");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gs;
	}

	/**
	 * Getter for game grade from json
	 * 
	 * @return
	 */
	public int getGameGrade() {
		int grade = 0;
		JSONObject gs = getGameServerJson();
		try {
			grade = gs.getInt("grade");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grade;
	}

	/**
	 * Getter for game grade from json
	 * 
	 * @return
	 */
	public int getGameMoves() {
		int moves = 0;
		JSONObject gs = getGameServerJson();
		try {
			moves = gs.getInt("moves");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return moves;
	}

	/**
	 * Getter for game robots
	 * 
	 * @return
	 */
	public List<String> getGameRobots() {
		return game.getRobots();
	}

	/**
	 * Getter for kml log
	 * @return
	 */
	public KML_Logger getKMLog() {
		return this.log;
	}

	/**
	 * Function to init nodes on kml log
	 */
	private void initNodesLog() {
		for (node_data n : this.g.getV()) {
			this.log.addNodePlaceMark("" + n.getLocation());
		}
	}

	/**
	 * Setter for manual
	 * 
	 * @param isManual
	 */
	public void setIsManual(boolean isManual) {
		this.isManual = isManual;
	}

	/**
	 * Getter for manual
	 * 
	 * @return
	 */
	public boolean IsManual() {
		return this.isManual;
	}

	/**
	 * Override function of runnable , this function run the game on thread and
	 * moving every even number the robots
	 */
	@Override
	public void run() {
		try {
			game.startGame();
			int ind = 0;
			long dt = 50;
			while (game.isRunning()) {
				if (!isManual) {
					game_algo.moveRobotsAuto(game, g);
				}
				game_algo.updateRobots(game, log);
				game_algo.initFruitsOnEdges(g, game, log);

				if (ind % 2 == 0) {
					game.move();
					g.upgradeMC();
				}
				Thread.sleep(dt);
				ind++;
			}
			g.upgradeMC();
			log.closeDocument();
			System.out.println(game.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
