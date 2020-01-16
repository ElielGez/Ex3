package Tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dataStructure.Fruit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FruitTest {
	static ArrayList<Fruit> fruitList;
	static final double EPS = 0.00001;
	static final int MAX_FRUITS = 50;

	@BeforeAll
	static void initFruitList() {
		fruitList = new ArrayList<>();
	}

	@Test
	@Order(1)
	void addFruits() {
		double lang = 35.19797411945117;
		double lat = 32.102764564705886;
		for (int i = 1; i <= MAX_FRUITS; i++) {
			lang = lang + EPS;
			lat = lat + EPS * EPS;
			String pos = lang + "," + lat + ",0.0";
			String value = "" + (Math.random() * 100);
			String type = i % 2 == 0 ? "-1" : "1";
			String json = createFruitJson(pos, value, type);
			fruitList.add(new Fruit(json));
		}
	}

	@Test
	@Order(2)
	void checkSize() {
		assertEquals(MAX_FRUITS, fruitList.size());
	}

	@Test
	@Order(3)
	void checkIsSorted() { // sorted by value , from higher to lower
		fruitList.sort(Fruit.comp);
		for (int i = 0; i < fruitList.size() - 1; i++) {
			Fruit curr = fruitList.get(i);
			Fruit next = fruitList.get(i + 1);
			assertTrue(curr.getValue() >= next.getValue());
		}
	}

	private String createFruitJson(String pos, String value, String type) {
		return "{\"Fruit\":{\"value\":" + value + ",\"type\":" + type + ",\"pos\":\"" + pos + "\"}}";
	}

}
