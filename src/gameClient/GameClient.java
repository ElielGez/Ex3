package gameClient;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.graph;

public class GameClient implements Runnable {
	private boolean isManual;
	private graph g;
	private game_service game;
	private GameAlgo game_algo;
	private KML_Logger log;
	private boolean exportKMLOnEnd;
	private int stage;

	public static final double EPS = 0.000001;
	private static final int[] moves = { 290/* 0 */, 580/* 1 */, Integer.MAX_VALUE/* 2 */, 580/* 3 */,
			Integer.MAX_VALUE/* 4 */, 500/* 5 */, Integer.MAX_VALUE/* 6 */, Integer.MAX_VALUE/* 7 */,
			Integer.MAX_VALUE/* 8 */, 580/* 9 */, Integer.MAX_VALUE/* 10 */, 580/* 11 */, Integer.MAX_VALUE/* 12 */,
			580/* 13 */, Integer.MAX_VALUE/* 14 */, Integer.MAX_VALUE/* 15 */, 290/* 16 */, Integer.MAX_VALUE/* 17 */,
			Integer.MAX_VALUE/* 18 */, 580/* 19 */, 290/* 20 */, Integer.MAX_VALUE/* 21 */, Integer.MAX_VALUE/* 22 */,
			1140/* 23 */ };
	private static final int[] grade = { 145/* 0 */, 450/* 1 */, Integer.MAX_VALUE/* 2 */, 720/* 3 */,
			Integer.MAX_VALUE/* 4 */, 570/* 5 */, Integer.MAX_VALUE/* 6 */, Integer.MAX_VALUE/* 7 */,
			Integer.MAX_VALUE/* 8 */, 510/* 9 */, Integer.MAX_VALUE/* 10 */, 1050/* 11 */, Integer.MAX_VALUE/* 12 */,
			310/* 13 */, Integer.MAX_VALUE/* 14 */, Integer.MAX_VALUE/* 15 */, 235/* 16 */, Integer.MAX_VALUE/* 17 */,
			Integer.MAX_VALUE/* 18 */, 250/* 19 */, 200/* 20 */, Integer.MAX_VALUE/* 21 */, Integer.MAX_VALUE/* 22 */,
			1000/* 23 */ };
	private static final int[] sleep = { 10/* 0 */, 10/* 1 */, 50/* 2 */, 10/* 3 */, 50/* 4 */, 10/* 5 */, 50/* 6 */,
			50/* 7 */, 50/* 8 */, 10/* 9 */, 50/* 10 */, 50/* 11 */, 50/* 12 */, 52/* 13 */, 50/* 14 */, 50/* 15 */,
			50/* 16 */, 50/* 17 */, 50/* 18 */, 50/* 19 */, 20/* 20 */, 50/* 21 */, 50/* 22 */, 10/* 23 */ };

	private static final int[] mod = { 10/* 0 */, 10/* 1 */, 2/* 2 */, 6/* 3 */, 2/* 4 */, 6/* 5 */, 2/* 6 */, 2/* 7 */,
			2/* 8 */, 6/* 9 */, 2/* 10 */, 2/* 11 */, 2/* 12 */, 2/* 13 */, 2/* 14 */, 2/* 15 */, 2/* 16 */, 2/* 17 */,
			2/* 18 */, 2/* 19 */, 4/* 20 */, 2/* 21 */, 2/* 22 */, 6/* 23 */ };

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
		this();
		this.stage = stage;
		this.exportKMLOnEnd = false;
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
	
	public int getStage() {
		return this.stage;
	}

	/**
	 * Override function of runnable , this function run the game on thread and
	 * moving every even number the robots
	 */
	@Override
	public void run() { // stages that working : 0,1,3,5,11,13,19,23
		try {
			Game_Server.login(316519966);
			game.startGame();
			int ind = 0;
			long dt = sleep[stage];
			while (game.isRunning()) {
				if (!isManual) {
					game_algo.moveRobotsAuto(game, g);
				}
				game_algo.updateRobots(game, log);
				game_algo.initFruitsOnEdges(g, game, log);

				if (ind % mod[stage] == 0 && this.getGameMoves() < moves[stage]) {
					game.move();
					g.upgradeMC();
				}
				ind++;

				Thread.sleep(dt);
			}
			g.upgradeMC();
			if (this.exportKMLOnEnd)
				log.closeDocument();
//			System.out.println("stage: " + this.stage + " " + game.toString());
			calculateResults();
			game.sendKML(log.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculateResults() {

		int currentMoves = this.getGameMoves();
		int currentGrade = this.getGameGrade();

		boolean failed = false;
		if (currentMoves > moves[this.stage] || currentGrade < grade[this.stage]) {
			failed = true;
		}
		if (!failed) {
			System.out.println("Stage: " + this.stage + ", Moves: " + this.getGameMoves() + ", Grade: "
					+ this.getGameGrade() + " SUCCESS");
		} else {
			System.out.println("Stage: " + this.stage + ", Moves: " + this.getGameMoves() + ", Grade: "
					+ this.getGameGrade() + " FAILED");
			System.out.println("Expected: Moves: " + moves[this.stage] + ", Grade: " + grade[this.stage]);
		}
	}
	
}
