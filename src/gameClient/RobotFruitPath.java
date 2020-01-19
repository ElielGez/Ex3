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

	public RobotFruitPath(double distance, Fruit f, Robot r, List<node_data> path) {
		this.distance = distance;
		this.f = f;
		this.r = r;
		this.path = path;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Fruit getF() {
		return f;
	}

	public void setF(Fruit f) {
		this.f = f;
	}

	public Robot getR() {
		return r;
	}

	public void setR(Robot r) {
		this.r = r;
	}

	public List<node_data> getPath() {
		return path;
	}

	public void setPath(List<node_data> path) {
		this.path = path;
	}

}