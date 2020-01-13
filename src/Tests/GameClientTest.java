package Tests;

import Server.Game_Server;
import Server.game_service;
import gameClient.GameClient;

public class GameClientTest {

	public static void main(String[] args) {
		GameClient gc = new GameClient();
		gc.initGame(15);

	}

}
