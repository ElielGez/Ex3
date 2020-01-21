package gameClient;

import java.util.ArrayList;

public class RobotFruitPathContainer {
	private ArrayList<RobotFruitPath> list;

	/**
	 * Constructor to init arraylist
	 */
	public RobotFruitPathContainer() {
		list = new ArrayList<RobotFruitPath>();
	}

	/**
	 * Function to add RobotFruitPath to the list
	 * @param rfp
	 */
	public void add(RobotFruitPath rfp) {
		this.list.add(rfp);
	}

	/**
	 * Function to sort list by RobotFruitPathComparator
	 */
	public void sort() {
		this.list.sort(new RobotFruitPathComparator());
	}

	/**
	 * Function to get list
	 * @return
	 */
	public ArrayList<RobotFruitPath> getList() {
		return list;
	}
}
