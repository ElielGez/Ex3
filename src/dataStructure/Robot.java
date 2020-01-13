package dataStructure;

import java.util.Comparator;

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

	public Robot() {

	}

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

	public Robot(Robot f) {
		this.value = f.getValue();
		this.location = f.getLocation();
		this.guiLocation = f.getGuiLocation();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
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
	 * @return the src
	 */
	public int getSrc() {
		return src;
	}

	/**
	 * @param src the src to set
	 */
	public void setSrc(int src) {
		this.src = src;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the dest
	 */
	public int getDest() {
		return dest;
	}

	/**
	 * @param dest the dest to set
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
