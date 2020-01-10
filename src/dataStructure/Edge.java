package dataStructure;

public class Edge implements edge_data {
	private int src;
	private int dest;
	private double weight;
	private String info;
	private int tag;

	/**
	 * Empty constructor
	 */
	public Edge() {

	}

	/**
	 * Constructor with params
	 * 
	 * @param src
	 * @param dest
	 * @param weight
	 */
	public Edge(int src, int dest, double weight) {
		if (weight < 0)
			throw new ArithmeticException("Weight must be positive");
		this.src = src;
		this.dest = dest;
		this.weight = weight;
	}

	/**
	 * Deep copy constructor
	 * 
	 * @param e
	 */
	public Edge(edge_data e) {
		this.src = e.getSrc();
		this.dest = e.getDest();
		this.weight = e.getWeight();
		this.info = e.getInfo();
		this.tag = e.getTag();
	}

	/**
	 * Getter for src
	 */
	@Override
	public int getSrc() {
		return this.src;
	}

	/**
	 * Getter for dest
	 */
	@Override
	public int getDest() {
		return this.dest;
	}

	/**
	 * Getter for weight
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Getter for info
	 */
	@Override
	public String getInfo() {
		return this.info;
	}

	/**
	 * Setter for info
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	/**
	 * Getter for tag
	 */
	@Override
	public int getTag() {
		return this.tag;
	}

	/**
	 * Setter for tag
	 */
	@Override
	public void setTag(int t) {
		this.tag = t;
	}

	/**
	 * Override toString
	 */
	public String toString() {
		return "Edge data - " + " src: " + this.src + " dest: " + this.dest + " weight: " + this.weight + " info: "
				+ this.info + " tag: " + this.tag + " ";
	}

}
