package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.Edge;

public class EdgeTest {
	static Edge e;
	static int src = 1;
	static int dest = 2;
	static double weight = 10;

	@BeforeAll
	static void createEdge() {
		e = new Edge(src,dest,weight);
	}

	@Test
	void checkSrc() {
		assertEquals(src, e.getSrc());
	}

	@Test
	void checkDest() {
		assertEquals(dest, e.getDest());
	}

	@Test
	void checkWeight() {
		assertEquals(weight, e.getWeight());
	}

	@Test
	void checkInfo() {
		String info = "test info";
		e.setInfo(info);
		assertEquals(info, e.getInfo());
	}

	@Test
	void checkTag() {
		int t = 1;
		e.setTag(t);
		assertEquals(t, e.getTag());
	}

}
