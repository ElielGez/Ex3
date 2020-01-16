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
	private boolean onTarget;
	public static final Comparator<Fruit> comp = new Fruit_Comperator();

	public Fruit() {

	}

	/**
	 * Constructor for fruit from json. json format: { "Fruit":{ "pos":"",
	 * "value":"", "type":"", } }
	 * 
	 * @param json
	 */
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
			this.setOnTarget(false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter for value
	 * 
	 * @return
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Setter for value
	 * 
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Getter for type
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setter for type
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Getter for location
	 * 
	 * @return
	 */
	public Point3D getLocation() {
		return location;
	}

	/**
	 * Setter for location
	 * 
	 * @param location
	 */
	public void setLocation(Point3D location) {
		this.location = new Point3D(location);
	}

	/**
	 * Getter for gui location
	 * 
	 * @return
	 */
	public Point3D getGuiLocation() {
		return guiLocation;
	}

	/**
	 * Setter for gui location
	 * 
	 * @param guiLocation
	 */
	public void setGuiLocation(Point3D guiLocation) {
		this.guiLocation = new Point3D(guiLocation);
	}

	/**
	 * Getter for edge
	 * 
	 * @return the edge
	 */
	public edge_data getEdge() {
		return edge;
	}

	/**
	 * Setter for edge
	 * 
	 * @param edge the edge to set
	 */
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}

	/**
	 * Getter for onTarget
	 * 
	 * @return the onTarget
	 */
	public boolean isOnTarget() {
		return onTarget;
	}

	/**
	 * Setter for onTarget
	 * 
	 * @param onTarget the onTarget to set
	 */
	public void setOnTarget(boolean onTarget) {
		this.onTarget = onTarget;
	}

	public String toString() {
		return "value: " + value + ", type: " + type + ", location: " + location;
	}
}
