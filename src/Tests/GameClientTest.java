package Tests;

import gameClient.GameClient;

public class GameClientTest {

	public static void main(String[] args) {
		GameClient gc = new GameClient();
		gc.initGame(15);
		Thread t = new Thread(gc);
		t.start();

	}

}
