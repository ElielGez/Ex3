package gameClient;

import java.util.List;

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

	public static final double EPS = 0.000001;

	public GameClient() {
	}

	public void initGame(int stage) {
		game = Game_Server.getServer(stage);
		g = new DGraph();
		g.initFromJson(game.getGraph());
		game_algo = new GameAlgo();

		game_algo.initFruitsOnEdges(g, game);
		game_algo.initRobotsOnNodes(g, game);
	}

	public GameAlgo getGameAlgo() {
		return this.game_algo;
	}

	public long getGameClock() {
		return (game.timeToEnd() / 1000);
	}

	public graph getGraph() {
		return this.g;
	}
	
	public game_service getGame() {
		return this.game;
	}

	public List<String> getGameRobots() {
		return game.getRobots();
	}

	@Override
	public void run() {
		game.startGame();
		int ind = 0;
		long dt = 50;
		while (game.isRunning()) {
			if (!isManual) {
				game_algo.moveRobots(g, game);
			}
			game_algo.updateRobots(game);
			game_algo.initFruitsOnEdges(g, game);
			try {
				if (ind % 2 == 0) {
					game.move();
					g.upgradeMC();
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
