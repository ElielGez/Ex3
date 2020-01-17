package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

public class Robot {
	private int id;
	private double value;
	private int src;
	private int dest;
	private double speed;
	private Point3D location;
	private Point3D guiLocation;

	/**
	 * Empty constructors
	 */
	public Robot() {

	}

	/**
	 * Constructor to create robot from json
	 * json format:
	 * 		"Robot":{
	 * 			"pos":"",
	 * 			"value":"",
	 * 			"id":"",
	 * 			"src":"",
	 * 			"dest":"",
	 * 			"speed":""
	 * 		}
	 * @param json
	 */
	public Robot(String json) {
		JSONObject line;
		try {
			line = new JSONObject(json);
			JSONObject robot = line.getJSONObject("Robot");
			String split[] = robot.getString("pos").split(",");
			Point3D p = new Point3D(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
			this.setLocation(p);
			this.setValue(robot.getDouble("value"));
			this.id = robot.getInt("id");
			this.setSrc(robot.getInt("src"));
			this.setDest(robot.getInt("dest"));
			this.setSpeed(robot.getDouble("speed"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter for value
	 * @return
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Setter for value
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Getter for location
	 * @return
	 */
	public Point3D getLocation() {
		return location;
	}

	/**
	 * Setter for location
	 * @param location
	 */
	public void setLocation(Point3D location) {
		this.location = new Point3D(location);
	}

	/**
	 * Getter for gui location
	 * @return
	 */
	public Point3D getGuiLocation() {
		return guiLocation;
	}

	/**
	 * Setter for gui location
	 * @param guiLocation
	 */
	public void setGuiLocation(Point3D guiLocation) {
		this.guiLocation = new Point3D(guiLocation);
	}

	/**
	 * Getter for src
	 * @return the src
	 */
	public int getSrc() {
		return src;
	}

	/**
	 * Setter for src
	 * @param src the src to set
	 */
	public void setSrc(int src) {
		this.src = src;
	}

	/**
	 * Getter for id
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter for dest
	 * @return the dest
	 */
	public int getDest() {
		return dest;
	}

	/**
	 * Setter for dest
	 * @param dest the dest to set
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}

	/**
	 * Getter for speed
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Setter for speed
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
