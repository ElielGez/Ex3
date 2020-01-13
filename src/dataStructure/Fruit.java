package dataStructure;

import java.util.Comparator;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

public class Fruit {
	private double value;
	private int type;
	private Point3D location;
	private Point3D guiLocation;
	private edge_data edge;
	public static final Comparator<Fruit> comp = new Fruit_Comperator();

	public Fruit() {

	}

	public Fruit(String json) {
		JSONObject line;
		try {
			line = new JSONObject(json);
			JSONObject fruit = line.getJSONObject("Fruit");
			String split[] = fruit.getString("pos").split(",");
			Point3D p = new Point3D(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
			this.setLocation(p);
			this.setValue(fruit.getDouble("value"));
			this.setType(fruit.getInt("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Fruit(double value, int type, Point3D location) {
		this.value = value;
		this.type = type;
		this.location = location;
	}

	public Fruit(Fruit f) {
		this.value = f.getValue();
		this.type = f.getType();
		this.location = f.getLocation();
		this.guiLocation = f.getGuiLocation();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = new Point3D(location);
	}

	public Point3D getGuiLocation() {
		return guiLocation;
	}

	public void setGuiLocation(Point3D guiLocation) {
		this.guiLocation = new Point3D(guiLocation);
	}

	/**
	 * @return the edge
	 */
	public edge_data getEdge() {
		return edge;
	}

	/**
	 * @param edge the edge to set
	 */
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}
}
