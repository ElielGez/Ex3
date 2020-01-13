package dataStructure;

import java.util.Comparator;

public class Fruit_Comperator implements Comparator<Fruit> {

	public Fruit_Comperator() {;}
	public int compare(Fruit f1, Fruit f2) {
		if(f2.getValue() - f1.getValue() > 0) return 1;
		else if(f2.getValue() - f1.getValue() < 0) return -1;
		else return 0;
	}

	// ******** add your code below *********

}
