package Tests;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import gameClient.KML_Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KML_LoggerTest {
	static KML_Logger log;
	static final double EPS = 0.00001;
	static final String FILE_NAME = "JUNIT_TEST";

	@BeforeAll
	static void createKMLFile() {
		log = new KML_Logger(FILE_NAME);
	}

	@Test
	@Order(1)
	void addNodePlaceMark() {
		double lang = 35.19797411945117;
		double lat = 32.102764564705886;
		for (int i = 1; i <= 25; i++) {
			lang = lang + EPS;
			lat = lat + EPS * EPS;
			log.addNodePlaceMark(lang + "," + lat + ",0.0");
		}
	}

	@Test
	@Order(2)
	void addRobotPlaceMark() {
		double lang = 35.19797411945117;
		double lat = 32.102764564705886;
		for (int i = 1; i <= 3; i++) {
			lang = lang + EPS;
			lat = lat + EPS * EPS;
			log.addRobotPlaceMark(lang + "," + lat + ",0.0");
		}
	}

	@Test
	@Order(3)
	void addFruitPlaceMark() {
		double lang = 35.19797411945117;
		double lat = 32.102764564705886;
		for (int i = 1; i <= 100; i++) {
			lang = lang + EPS;
			lat = lat + EPS * EPS;
			log.addFruitPlaceMark(i % 2 == 0 ? "apple" : "banana", lang + "," + lat + ",0.0");
		}
	}

	@Test
	@Order(4)
	void closeAndSaveDocument() {
		log.closeDocument();
	}

	@Test
	@Order(5)
	void readKmlLog() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/" + FILE_NAME + ".kml"));
			assertNotEquals(null, br.readLine());
		} catch (IOException e) {
			fail();
		}
	}

}
