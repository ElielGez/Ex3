package gameClient;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.GameAlgo;
import dataStructure.graph;

public class GameClient implements Runnable {
	private boolean isManual;
	private graph g;
	private game_service game;
	private GameAlgo game_algo;
	private KML_Logger log;

	public static final double EPS = 0.000001;

	public GameClient() {
	}

	/**
	 * Constructor to create game client with stage
	 * 
	 * @param stage
	 */
	public GameClient(int stage) {
		log = new KML_Logger(stage, "moshe");
		game = Game_Server.getServer(stage);
		g = new DGraph();
		g.initFromJson(game.getGraph());
		game_algo = new GameAlgo();

		game_algo.initFruitsOnEdges(g, game);
		game_algo.initRobotsOnNodes(g, game);
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
	 * Getter for game grade from json
	 * 
	 * @return
	 */
	public String getGameGrade() {
		String results = game.toString();
		JSONObject line;
		String grade = "0";
		try {
			line = new JSONObject(results);
			JSONObject gs = line.getJSONObject("GameServer");
			grade = "" + gs.getInt("grade");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return grade;
	}

	/**
	 * Getter for game robots
	 * 
	 * @return
	 */
	public List<String> getGameRobots() {
		return game.getRobots();
	}
	
	public KML_Logger getKMLog() {
		return this.log;
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
				game_algo.updateRobots(game);
				game_algo.initFruitsOnEdges(g, game);

				if (ind % 2 == 0) {
					game.move();
					g.upgradeMC();
				}
				Thread.sleep(dt);
				ind++;
			}
			g.upgradeMC();
			System.out.println(game.toString());
//			log.closeDocument();
		} catch (Exception e) {
			e.printStackTrace();
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
}
