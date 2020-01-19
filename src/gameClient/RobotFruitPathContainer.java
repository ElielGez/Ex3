package gameClient;

import java.util.ArrayList;

public class RobotFruitPathContainer {
	private ArrayList<RobotFruitPath> list;

	public RobotFruitPathContainer() {
		list = new ArrayList<RobotFruitPath>();
	}

	public void add(RobotFruitPath rfp) {
		this.list.add(rfp);
	}

	public void sort() {
		this.list.sort(new RobotFruitPathComparator());
	}

	public ArrayList<RobotFruitPath> getList() {
		return list;
	}
}
