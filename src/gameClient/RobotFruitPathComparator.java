package gameClient;

import java.util.Comparator;

class RobotFruitPathComparator implements Comparator<RobotFruitPath> {

	public RobotFruitPathComparator() {;}
	/**
	 * Compare between to fruits by their values , bigger to smaller.
	 */
	public int compare(RobotFruitPath rfp1, RobotFruitPath rfp2) {
		if(rfp1.getDistance() - rfp2.getDistance() > 0) return 1;
		else if(rfp1.getDistance() - rfp2.getDistance() < 0) return -1;
		else return 0;
	}

	// ******** add your code below *********

}
