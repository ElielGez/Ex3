package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

public class DGraph implements graph, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LinkedHashMap<Integer, node_data> vertices;
	private LinkedHashMap<Integer, LinkedHashMap<Integer, edge_data>> edges;
	private int mc;
	private int edgesSize;

	/**
	 * Empty constructor - init hashmaps
	 */
	public DGraph() {
		this.vertices = new LinkedHashMap<Integer, node_data>();
		this.edges = new LinkedHashMap<Integer, LinkedHashMap<Integer, edge_data>>();
		this.mc = 0;
		this.edgesSize = 0;
	}

	/**
	 * Deep copy constructor
	 * 
	 * @param g
	 */
	public DGraph(DGraph g) {
		this.vertices = (LinkedHashMap<Integer, node_data>) g.vertices.clone();
		this.edges = (LinkedHashMap<Integer, LinkedHashMap<Integer, edge_data>>) g.edges.clone();
		this.mc = g.getMC();
		this.edgesSize = g.edgeSize();
	}

	/**
	 * Constructor to create fast nodes
	 * 
	 * @param vertices
	 */
	public DGraph(int vertices) {
		this();
		for (int i = 1; i <= vertices; i++) {
			node_data n = new Node(i);
			this.vertices.put(n.getKey(), n);
		}
	}

	/**
	 * Getter for node in vertices hashmap
	 */
	@Override
	public node_data getNode(int key) {
		return this.vertices.get(key);
	}

	/**
	 * Getter for edge in edges hashmap
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		if (this.edges.get(src) == null || this.edges.get(src).get(dest) == null)
			return null;
		return this.edges.get(src).get(dest);
	}

	/**
	 * Add node function , add to hashmap
	 */
	@Override
	public void addNode(node_data n) {
		if (this.getNode(n.getKey()) != null) {
			throw new ArithmeticException("This node already exists");
		}
		this.vertices.put(n.getKey(), n);
		this.mc++;
	}

	/**
	 * Function to connect between 2 nodes by edge
	 */
	@Override
	public void connect(int src, int dest, double w) {
		node_data s = this.vertices.get(src);
		node_data d = this.vertices.get(dest);
		if (s == null || d == null) {
			System.out.println("Cannot connect between this vertices , some of them or maybe both , are null");
			return;
		}
		LinkedHashMap<Integer, edge_data> hp = this.edges.get(src);
		if (hp == null) {
			// new src , create new linked list of edge values.
			hp = new LinkedHashMap<Integer, edge_data>();
		}
		edge_data exist = this.getEdge(src, dest);
		edge_data e = new Edge(src, dest, w);
		hp.put(dest, e);
		this.edges.put(src, hp);
		if (exist == null)
			this.edgesSize++;
		this.mc++;
	}

	/**
	 * Function to get collection of nodes from hashmap
	 */
	@Override
	public Collection<node_data> getV() {
		return this.vertices.values();
	}

	/**
	 * Function to get collection of edges from hashmap
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		LinkedHashMap<Integer, edge_data> edgesSrc = this.edges.get(node_id);
		if (edgesSrc == null)
			return null;
		return edgesSrc.values();
	}

	@Override
	public node_data removeNode(int key) {
		LinkedHashMap<Integer, edge_data> e = this.edges.remove(key);
		node_data n = this.vertices.remove(key);

		if ((e != null && e.values() != null) && n != null)
			this.mc++;

		if (e != null && e.values() != null)
			this.edgesSize -= e.size();

		return n;

	}

	/**
	 * Function to remove edge from the hashmap
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		LinkedHashMap<Integer, edge_data> hp = this.edges.get(src);
		if (hp == null) {
			System.out.println("No edges from this source : " + src);
			return null;
		}

		edge_data e = hp.remove(dest);
		if (e != null) {
			this.edgesSize--;
			this.mc++;
		}

		return e;
	}

	/**
	 * Function to get nodes size
	 */
	@Override
	public int nodeSize() {
		return this.vertices.size();
	}

	/**
	 * Function to get edges size
	 */
	@Override
	public int edgeSize() {
		return this.edgesSize;
	}

	/**
	 * Getter for mc Mc is the param to count how much changes made on this graph
	 */
	@Override
	public int getMC() {
		return this.mc;
	}

	/**
	 * Function to print vertices of the graph only
	 * 
	 * @return
	 */
	public String verticesString() {
		String s = "";
		for (int key : this.vertices.keySet()) {
			s += this.vertices.get(key) + "\n";
		}
		return s;
	}

	/**
	 * Override toString function
	 */
	public String toString() {
		String s = "";
		for (int src : this.edges.keySet()) {
			LinkedHashMap<Integer, edge_data> edgeSrc = this.edges.get(src);
			if (edgeSrc != null) {
				for (int dest : edgeSrc.keySet()) {
					s += "HashMap data - src: " + src + " dest: " + dest + "\n" + edgeSrc.get(dest) + "\n\n";
				}
			}
		}
		return s;
	}

	/**
	 * Deep copy function
	 */
	public DGraph copy() {
		return new DGraph(this);
	}

}
