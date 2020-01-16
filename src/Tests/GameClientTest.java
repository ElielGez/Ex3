package Tests;

import gameClient.GameClient;

public class GameClientTest {

	public static void main(String[] args) {
		GameClient gc = new GameClient(23);
		Thread t = new Thread(gc);
		t.start();
	}

}
