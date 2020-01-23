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

	@Test
	void initGraphFromJson() {
		String json = "{\"Edges\":[{\"src\":0,\"w\":1.4004465106761335,\"dest\":1},{\"src\":0,\"w\":1.4620268165085584,\"dest\":10},{\"src\":1,\"w\":1.8884659521433524,\"dest\":0},{\"src\":1,\"w\":1.7646903245689283,\"dest\":2},{\"src\":2,\"w\":1.7155926739282625,\"dest\":1},{\"src\":2,\"w\":1.1435447583365383,\"dest\":3},{\"src\":3,\"w\":1.0980094622804095,\"dest\":2},{\"src\":3,\"w\":1.4301580756736283,\"dest\":4},{\"src\":4,\"w\":1.4899867265011255,\"dest\":3},{\"src\":4,\"w\":1.9442789961315767,\"dest\":5},{\"src\":5,\"w\":1.4622464066335845,\"dest\":4},{\"src\":5,\"w\":1.160662656360925,\"dest\":6},{\"src\":6,\"w\":1.6677173820549975,\"dest\":5},{\"src\":6,\"w\":1.3968360163668776,\"dest\":7},{\"src\":7,\"w\":1.0176531013725074,\"dest\":6},{\"src\":7,\"w\":1.354895648936991,\"dest\":8},{\"src\":8,\"w\":1.6449953452844968,\"dest\":7},{\"src\":8,\"w\":1.8526880332753517,\"dest\":9},{\"src\":9,\"w\":1.4575484853801393,\"dest\":8},{\"src\":9,\"w\":1.022651770039933,\"dest\":10},{\"src\":10,\"w\":1.1761238717867548,\"dest\":0},{\"src\":10,\"w\":1.0887225789883779,\"dest\":9}],\"Nodes\":[{\"pos\":\"35.18753053591606,32.10378225882353,0.0\",\"id\":0},{\"pos\":\"35.18958953510896,32.10785303529412,0.0\",\"id\":1},{\"pos\":\"35.19341035835351,32.10610841680672,0.0\",\"id\":2},{\"pos\":\"35.197528356739305,32.1053088,0.0\",\"id\":3},{\"pos\":\"35.2016888087167,32.10601755126051,0.0\",\"id\":4},{\"pos\":\"35.20582803389831,32.10625380168067,0.0\",\"id\":5},{\"pos\":\"35.20792948668281,32.10470908739496,0.0\",\"id\":6},{\"pos\":\"35.20746249717514,32.10254648739496,0.0\",\"id\":7},{\"pos\":\"35.20319591121872,32.1031462,0.0\",\"id\":8},{\"pos\":\"35.19597880064568,32.10154696638656,0.0\",\"id\":9},{\"pos\":\"35.18910131880549,32.103618700840336,0.0\",\"id\":10}]}";
		DGraph graph = new DGraph();
		graph.initFromJson(json);
		assertEquals(22, graph.getEdgesSize());
		assertEquals(11, graph.getV().size());
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
