package gameClient;

import java.util.HashMap;
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
	private boolean exportKMLOnEnd;
	private int stage;

	private static final HashMap<Integer, Integer> moves = new HashMap<>();
	private static final HashMap<Integer, Integer> grade = new HashMap<>();

	public static final double EPS = 0.000001;

	/**
	 * Empty constructor
	 */
	public GameClient() {
		prepareHashMapMoves();
		prepareHashMapGrade();
	}

	/**
	 * Constructor to create game client with stage
	 * 
	 * @param stage
	 */
	public GameClient(int stage) {
		this();
		this.stage = stage;
		this.exportKMLOnEnd = true;
		log = new KML_Logger("" + stage);
		game = Game_Server.getServer(stage);
		g = new DGraph();

		g.initFromJson(game.getGraph());

		game_algo = new GameAlgo();
		game_algo.initNodes(g, log);
		game_algo.initFruitsOnEdges(g, game, log);
		game_algo.initRobotsOnNodes(g, game, log);
	}

	/**
	 * Setter for export kml on end
	 * 
	 * @param exportKMLOnEnd
	 */
	public void setExportKMLOnEnd(boolean exportKMLOnEnd) {
		this.exportKMLOnEnd = exportKMLOnEnd;
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
	 * 
	 * @return
	 */
	public KML_Logger getKMLog() {
		return this.log;
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
	public void run() { // this run works with stages : 3,5,11,13,19,20,23
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

				if (ind % 2 == 0 && this.getGameMoves() < moves.get(stage)) {
					game.move();
					g.upgradeMC();
				}
				ind++;

				Thread.sleep(dt);
			}
			g.upgradeMC();
			if (this.exportKMLOnEnd)
				log.closeDocument();
			calculateResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareHashMapMoves() {
		moves.put(0, 290);
		moves.put(1, 580);
		moves.put(2, Integer.MAX_VALUE);
		moves.put(3, 580);
		moves.put(4, Integer.MAX_VALUE);
		moves.put(5, 500);
		moves.put(6, Integer.MAX_VALUE);
		moves.put(7, Integer.MAX_VALUE);
		moves.put(8, Integer.MAX_VALUE);
		moves.put(9, 580);
		moves.put(10, Integer.MAX_VALUE);
		moves.put(11, 580);
		moves.put(12, Integer.MAX_VALUE);
		moves.put(13, 580);
		moves.put(14, Integer.MAX_VALUE);
		moves.put(15, Integer.MAX_VALUE);
		moves.put(16, 290);
		moves.put(17, Integer.MAX_VALUE);
		moves.put(18, Integer.MAX_VALUE);
		moves.put(19, 580);
		moves.put(20, 290);
		moves.put(21, Integer.MAX_VALUE);
		moves.put(22, Integer.MAX_VALUE);
		moves.put(23, 1140);
	}

	private void prepareHashMapGrade() {
		grade.put(0, 145);
		grade.put(1, 450);
		grade.put(3, 720);
		grade.put(5, 570);
		grade.put(9, 510);
		grade.put(11, 1050);
		grade.put(13, 310);
		grade.put(16, 235);
		grade.put(19, 250);
		grade.put(20, 200);
		grade.put(23, 1000);
	}

	private void calculateResults() {

		int currentMoves = this.getGameMoves();
		int currentGrade = this.getGameGrade();

		boolean failed = false;
		if (currentMoves > moves.get(this.stage) || currentGrade < grade.get(this.stage)) {
			failed = true;
		}
		if (!failed) {
			System.out.println("Stage: " + this.stage + ", Moves: " + this.getGameMoves() + ", Grade: "
					+ this.getGameGrade() + " SUCCESS");
		} else {
			System.out.println("Stage: " + this.stage + ", Moves: " + this.getGameMoves() + ", Grade: "
					+ this.getGameGrade() + " FAILED");
			System.out.println("Expected: Moves: " + moves.get(this.stage) + ", Grade: " + grade.get(this.stage));
		}
	}
}
