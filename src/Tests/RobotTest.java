package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dataStructure.Robot;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RobotTest {
	static ArrayList<Robot> robotList;
	static final double EPS = 0.00001;
	static final int MAX_ROBOTS = 50;

	@BeforeAll
	static void initRobotList() {
		robotList = new ArrayList<>();
	}

	@Test
	@Order(1)
	void addRobots() {
		double lang = 35.19797411945117;
		double lat = 32.102764564705886;
		for (int i = 1; i <= MAX_ROBOTS; i++) {
			lang = lang + EPS;
			lat = lat + EPS * EPS;
			String id = "" + i;
			String value = "" + (Math.random() * 100);
			String src = "" + ((int) (Math.random() * 20));
			String dest = "" + ((int) (Math.random() * 20));
			String speed = "" + (Math.random() * 5);
			String pos = lang + "," + lat + ",0.0";
			String json = createRobotJson(id, value, src, dest, speed, pos);
			robotList.add(new Robot(json));
		}
	}

	@Test
	@Order(2)
	void checkSize() {
		assertEquals(MAX_ROBOTS, robotList.size());
	}

	private String createRobotJson(String id, String value, String src, String dest, String speed, String pos) {
		return "{\"Robot\":{\"id\":" + id + ",\"value\":" + value + ",\"src\":" + src + ",\"dest\":" + dest
				+ ",\"speed\":" + speed + ",\"pos\":\"" + pos + "\"}}";
	}

}
