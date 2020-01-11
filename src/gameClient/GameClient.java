package gameClient;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;

public class GameClient {
	private DGraph dg;
	
	public GameClient() {
		
	}
	
	public void initGame(int stage) {
		game_service game = Game_Server.getServer(stage);
		dg = new DGraph();
		dg.initFromJson(game.getGraph());
	}
	
	public DGraph getGraph() {
		return this.dg;
	}
}
