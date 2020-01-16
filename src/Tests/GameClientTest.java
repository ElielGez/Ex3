package Tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gameClient.GameClient;

public class GameClientTest {

	public static void main(String[] args) {
		runAllStages();
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

}
