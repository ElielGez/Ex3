package Tests;

import Server.Game_Server;
import Server.game_service;
import gameClient.GameClient;
import gui.MyGameGUI;

public class MyGameGUITest {

	public static void main(String[] args) {
//		game_service game = Game_Server.getServer(22); // you have [0,23] games
//		String g = game.getGraph();
//		GameClient gc = new GameClient(g);
		new MyGameGUI();

	}

}
