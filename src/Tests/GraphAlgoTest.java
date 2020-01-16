package Tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.graph;
import utils.Point3D;

public class GraphAlgoTest {
	Graph_Algo ga;
	graph g;

	@BeforeEach
	void createGraph() {
		g = new DGraph();
		ga = new Graph_Algo();
		g.addNode(new Node(1,new Point3D(30, 500)));
		g.addNode(new Node(2,new Point3D(270, 80)));
		g.addNode(new Node(3,new Point3D(50, 100)));
		g.addNode(new Node(4,new Point3D(250, 250)));
		g.addNode(new Node(5,new Point3D(500, 250)));
		g.addNode(new Node(6,new Point3D(450, 550)));
		g.connect(1, 3, 14);
		g.connect(1, 4, 9);
		g.connect(1, 6, 7);
		g.connect(3, 2, 9);
		g.connect(3, 4, 2);
		g.connect(4, 1, 2);
		g.connect(4, 3, 2);
		g.connect(4, 5, 11);
		g.connect(4, 6, 10);
		g.connect(5, 2, 6);
		g.connect(6, 5, 15);
		ga.init(g);
	}

	@Test
	void copyGraph() {
		graph currGraph = ga.copy();
		currGraph.removeNode(1);
		assertTrue(currGraph.nodeSize() != g.nodeSize());
		assertTrue(currGraph.edgeSize() != g.edgeSize());
		assertTrue(currGraph != g);
	}

	@Test
	void saveInitFile() {
		g.removeEdge(6, 5);
		ga.save("junit_test1.txt");
		g.connect(6, 5, 15);
		ga.init("junit_test1.txt");

		graph currGraph = ga.copy();
		assertTrue(currGraph.edgeSize() != g.edgeSize());
		assertTrue(currGraph.getEdge(6, 5) == null);
	}

	@Test
	void shortestPathDist() {
		double spd = 20; // shortest path distance between 1 and 2
		assertEquals(spd, ga.shortestPathDist(1, 2));
		spd = Double.MAX_VALUE; // shortest path distance between 5 and 3 (No path)
		assertEquals(spd, ga.shortestPathDist(5, 3));
		spd = 11; // shortest path distance between 4 and 2
		assertEquals(spd, ga.shortestPathDist(4, 2));
	}

	@Test
	void shortestPath() {
		assertEquals(null, ga.shortestPath(5, 3));
		assertNotEquals(null, ga.shortestPath(1, 2));
	}

	@Test
	void isConnected() {
		assertFalse(ga.isConnected());
		g.connect(2, 3, 8);
		g.connect(2, 5, 2);
		assertTrue(ga.isConnected());
	}

	@Test
	void TSP() {
		LinkedList<Integer> targets = new LinkedList<Integer>();
		targets.add(1);
		targets.add(4);
		targets.add(3);
		targets.add(2);
		assertNotEquals(null, ga.TSP(targets));

		targets = new LinkedList<>();
		targets.add(5);
		targets.add(3);
		targets.add(6);
		assertEquals(null, ga.TSP(targets));
	}

}
