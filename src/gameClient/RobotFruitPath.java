package gameClient;

import java.util.List;

import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.node_data;

public class RobotFruitPath {
	private double distance;
	private Fruit f;
	private Robot r;
	private List<node_data> path;

	
	public RobotFruitPath() {

	}

	/**
	 * Constructor to create RobotFruit path with distance,fruit,robot and path
	 * @param distance
	 * @param f
	 * @param r
	 * @param path
	 */
	public RobotFruitPath(double distance, Fruit f, Robot r, List<node_data> path) {
		this.distance = distance;
		this.f = f;
		this.r = r;
		this.path = path;
	}

	/**
	 * Getter for distance
	 * @return
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Setter for distance
	 * @param distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Getter for fruit
	 * @return
	 */
	public Fruit getF() {
		return f;
	}

	/**
	 * Setter for fruit
	 * @param f
	 */
	public void setF(Fruit f) {
		this.f = f;
	}

	/**
	 * Getter for robot
	 * @return
	 */
	public Robot getR() {
		return r;
	}

	/**
	 * Setter for robot
	 * @param r
	 */
	public void setR(Robot r) {
		this.r = r;
	}

	/**
	 * Getter for path
	 * @return
	 */
	public List<node_data> getPath() {
		return path;
	}

	/**
	 * Setter for path
	 * @param path
	 */
	public void setPath(List<node_data> path) {
		this.path = path;
	}

}