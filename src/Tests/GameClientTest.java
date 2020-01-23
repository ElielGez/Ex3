package Tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gameClient.GameClient;

public class GameClientTest {
	private static int stages[];

	public static void main(String[] args) {
//		runStage(0);
		//stages = new int[] { 0, 1, 3, 5, 9, 11, 13, 16, 19, 20, 23 };
//		stages = new int[] { 1 };
//		runAllStages();
	}

	/**
	 * Function that run all stages (auto mode) in thread pool
	 */
	private static void runAllStages() {
		ExecutorService pool = Executors.newFixedThreadPool(24);
		for (int i = 0; i < 24; i++) {
			Runnable r = new GameClient(i);
			pool.execute(r);
		}
		pool.shutdown();
	}
	
	private static void runStage(int stage) {
		GameClient gc = new GameClient(stage);
		Thread t = new Thread(gc);
		t.start();
	}

}
