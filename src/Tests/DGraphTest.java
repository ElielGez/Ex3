package Tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.time.Duration;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructure.edge_data;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;

public class DGraphTest {
	DGraph d;
	int initNodeSize = 10;

	@BeforeEach
	void buildGraph() {
		d = new DGraph(initNodeSize);
	}

	@Test
	void checkNodesSize() {
		assertEquals(initNodeSize, d.nodeSize());
	}

	@Test
	void addNodes() {
		int add = 5;
		for (int i = initNodeSize + 1; i <= add + initNodeSize; i++) {
			node_data n = new Node(i);
			d.addNode(n);
		}
		assertEquals(initNodeSize + add, d.nodeSize());
	}

	@Test
	void removeNodes() {
		int remove = remove3Nodes();
		assertEquals(initNodeSize - remove, d.nodeSize());
	}

	@Test
	void getNodes() {
		remove3Nodes();
		int nodeKey = 1;
		assertTrue(d.getNode(nodeKey) == null);
		nodeKey = 3;
		assertTrue(d.getNode(nodeKey) == null);
		nodeKey = 5;
		assertTrue(d.getNode(nodeKey) != null);
	}

	@Test
	void connectEdges() {
		assertEquals(0, d.edgeSize());
		connectBetweenEdges();
		assertEquals(initNodeSize - 1, d.edgeSize());
	}

	@Test
	void removeEdges() {
		connectBetweenEdges();
		int remove = 3;
		for (int i = 1; i <= remove; i++) {
			d.removeEdge(i, i + 1);
		}
		assertEquals(initNodeSize - remove - 1, d.edgeSize());
	}

	@Test
	void getV() {
		Collection<node_data> c = d.getV();
		assertEquals(c.size(), d.nodeSize());
		int size = d.nodeSize();
		int remove = remove3Nodes();
		assertEquals(c.size(), size - remove);
	}

	@Test
	void getE() {
		Collection<edge_data> c = d.getE(1);
		assertTrue(c == null);
		connectBetweenEdges();
		c = d.getE(1);
		assertTrue(c != null);
		d.connect(1, 5, 10);
		assertEquals(2, c.size());
	}

	/** ------------- UTILS FUNCTIONS ------------ **/

	private int remove3Nodes() {
		int remove = 3;
		for (int i = 1; i <= remove; i++) {
			d.removeNode(i);
		}
		return remove;
	}

	private void connectBetweenEdges() {
		for (int i = 1; i <= initNodeSize - 1; i++) {
			d.connect(i, i + 1, i * 3);
		}
	}
}
