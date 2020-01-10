package dataStructure;

import utils.Point3D;

public class Node implements node_data {
	private int key;
	private Point3D location;
	private double weight;
	private String info;
	private int tag;

	/**
	 * Empty constructor
	 */
	public Node() {
		
	}
	/**
	 * Constructor that gets key
	 * @param key
	 */
	public Node(int key) {
		this.key = key;
		this.initNode();
		setLocation(getRandomLocation());
	}
	
	/**
	 * Deep copy constructor
	 * @param n - the node to copy from
	 */
	public Node(node_data n) {
		this.key = n.getKey();
		this.weight = n.getWeight();
		this.info = n.getInfo();
		this.location = n.getLocation();
		this.tag = n.getTag();
	}

	/**
	 * Constructor with location
	 * @param location - Point3D location
	 */
	public Node(int key,Point3D location) {
		this.key = key;
		this.initNode();
		this.location = new Point3D(location);
	}

	/**
	 * Getter for key field
	 */
	@Override
	public int getKey() {
		return this.key;
	}

	/**
	 * Getter for location
	 */
	@Override
	public Point3D getLocation() {
		return new Point3D(this.location);
	}

	/**
	 * Setter for location
	 */
	@Override
	public void setLocation(Point3D p) {
		this.location = new Point3D(p);
	}

	/**
	 * Getter for weight
	 */
	@Override
	public double getWeight() {
		return weight;
	}

	/**
	 * Setter for weight
	 */
	@Override
	public void setWeight(double w) {
		this.weight = w;
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
		return "Node data - " + " key: " + this.key + " location: " + this.location + " weight: " + this.weight + " info: "
				+ this.info + " tag: " + this.tag;
	}
	
	/**
	 * Gets random x and y and return new location with those positions
	 * @return
	 */
	private static Point3D getRandomLocation() {
		int randomX = (int) (Math.random() * (850 - 50)) + 50;
		int randomY = (int) (Math.random() * (750 - 70)) + 70;
		return new Point3D(randomX,randomY);
	}
	
	/**
	 * Function to reset node for the algorithms
	 */
	public void initNode() {
		setTag(0);
		setInfo("");
		setWeight(Double.MAX_VALUE);
	}

}
