package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.Node;
import utils.Point3D;

public class NodeTest {
	static Node n;
	@BeforeAll
	static void createNode() {
		n = new Node(1);
	}
	@Test
	void checkKey() {
		assertEquals(1, n.getKey());
	}
	@Test
	void checkLocation(){
		Point3D p = new Point3D(5,100);
		n.setLocation(p);
		assertEquals(p,n.getLocation());
	}
	@Test
	void checkWeight() {
		double w = 5.0;
		n.setWeight(w);
		assertEquals(w,n.getWeight());
	}
	@Test
	void checkInfo() {
		String info = "test info";
		n.setInfo(info);
		assertEquals(info,n.getInfo());
	}
	@Test
	void checkTag() {
		int t = 1;
		n.setTag(t);
		assertEquals(t,n.getTag());
	}
	
}
