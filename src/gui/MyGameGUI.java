package gui;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.GameClient;
import utils.Point3D;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener {
	private GameClient gc;
	private int mc;
	private Graph_Algo ga = new Graph_Algo();

	private static final String STAGE = "Stage";
	private final int WIDTH = 1000;
	private final int HEIGHT = 1000;
	private final int X_SCALE_TMIN = 15;
	private final int Y_SCALE_TMIN = 200;
	private final int Y_SCALE_TMAX = 50;

	/**
	 * Empty constructor
	 */
	public MyGameGUI() {
		gc = new GameClient();
		initGUI(WIDTH, HEIGHT);
	}

	private void initStage(int stage) {
		gc.initGame(stage);
		this.mc = this.getG().getMC();
	}

	/**
	 * Getter for the graph
	 * 
	 * @return
	 */
	public graph getG() {
		if (this.gc == null)
			return null;
		return this.gc.getGraph();
	}

	/**
	 * Getter for mc
	 * 
	 * @return
	 */
	public int getMc() {
		return this.mc;
	}

	/**
	 * Function to init the gui object
	 * 
	 * @param width
	 * @param height
	 */
	private void initGUI(int width, int height) {
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.initMenuBar();
		this.initMcThread();
	}

	private void initMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu load = new Menu("Load");
		menuBar.add(load);
		this.setMenuBar(menuBar);

		MenuItem stage = new MenuItem(STAGE);
		stage.addActionListener(this);

		load.add(stage);

		this.addMouseListener(this);
	}

	/**
	 * Function to draw the graph on the JFrame
	 */
	public void paint(Graphics g) {
		// super.paint(g);
		try {
			if (this.getG() != null) {
				BufferedImage bufferedImage = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = bufferedImage.createGraphics();
				g2d.setBackground(new Color(240, 240, 240));
				g2d.clearRect(0, 0, this.WIDTH, this.HEIGHT);
				// paint using g2d ...
				this.drawGraph(g2d);
				Graphics2D g2dComponent = (Graphics2D) g;
				g2dComponent.drawImage(bufferedImage, null, 0, 0);
			}
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Function to init mc thread This thread is listening to changes on the graph
	 * (by mc value)
	 */
	private void initMcThread() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (gc) {
						if (gc.getGraph() != null && gc.getGraph().getMC() != mc) {
							mc = gc.getGraph().getMC();
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							repaint();
						}
					}
				}
			}
		});
		t.start();
	}

	private void setGuiLocations() {
		graph g = this.gc.getGraph();
		double x_scale[] = xAxis_Min_Max(g);
		double y_scale[] = yAxis_Min_Max(g);

		// node set guid location
		for (node_data n : g.getV()) {
			double x = scale(n.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN, this.getWidth() - Y_SCALE_TMAX);
			double y = scale(n.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN,
					this.getHeight() - Y_SCALE_TMAX);
			Point3D p = new Point3D(x, y);
			n.setGuiLocation(p);
		}

		// fruit set gui location
		for (Fruit f : gc.fruitList()) {
			double xF = scale(f.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN,
					this.getWidth() - Y_SCALE_TMAX);
			double yF = scale(f.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN,
					this.getHeight() - Y_SCALE_TMAX);
			Point3D pF = new Point3D(xF, yF);
			f.setGuiLocation(pF);
		}

		// robot set gui location
		for (Robot r : gc.robotList()) {
			double xR = scale(r.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN,
					this.getWidth() - Y_SCALE_TMAX);
			double yR = scale(r.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN,
					this.getHeight() - Y_SCALE_TMAX);
			Point3D pR = new Point3D(xR, yR);
			r.setGuiLocation(pR);
		}
	}

	/**
	 * Function that draw the graph with his nodes and edges
	 * 
	 * @param g1
	 */
	private void drawGraph(Graphics2D g) {
		setGuiLocations();

		g.setColor(Color.RED);
		g.setFont(new Font("Arial", 1, 15));
		List<String> rob = gc.getGameRobots();
		for (int i = 1; i <= rob.size(); i++) {
			g.drawString(rob.get(i - 1), 100, 70 + (20 * i));
		}
		g.drawString("Time left: " + gc.getGameClock(), 900, 100);
		for (node_data src : this.getG().getV()) {
			Point3D pSrc = src.getGuiLocation();
			g.setColor(Color.BLUE);
			Shape circle = new Arc2D.Double(pSrc.x() - 5, pSrc.y() - 5, 12, 12, 0, 360, Arc2D.CHORD);
			g.fill(circle);
			g.drawString("" + src.getKey(), pSrc.ix(), pSrc.iy() - 5);

			Collection<edge_data> e = this.getG().getE(src.getKey());
			if (e != null) {
				for (edge_data edge : e) {
					node_data dest = this.getG().getNode(edge.getDest());
					if (dest != null) {
						Point3D pDest = dest.getGuiLocation();
						g.setColor(Color.DARK_GRAY);
						g.drawLine(pSrc.ix(), pSrc.iy(), pDest.ix(), pDest.iy());

						int centerX = (pSrc.ix() + pDest.ix()) / 2;
						int centerY = (pSrc.iy() + pDest.iy()) / 2;
						for (int i = 0; i < 2; i++) {
							centerX = (centerX + pSrc.ix()) / 2;
							centerY = (centerY + pSrc.iy()) / 2;
						}
						g.setColor(Color.RED);
						g.fillOval(centerX - 3, centerY, 5, 5);
					}
				}
			}
		}
		for (Fruit f : gc.fruitList()) {
			Point3D pFruit = f.getGuiLocation();
			if (pFruit != null) {// need to remove , prevent null pointer exception (from thread synchronized...)
				if (f.getType() == -1)
					g.setColor(new Color(255, 196, 30)); // banana
				if (f.getType() == 1)
					g.setColor(Color.GREEN); // apple
				Shape fruitCircle = new Arc2D.Double(pFruit.x() - 9, pFruit.y() - 5, 20, 20, 0, 360, Arc2D.CHORD);
				g.fill(fruitCircle);
				g.setColor(Color.DARK_GRAY);
				g.drawString("" + f.getValue(), pFruit.ix(), pFruit.iy());
			}

		}
		for (Robot r : gc.robotList()) {
			BufferedImage img;
			try {
				img = ImageIO.read(new File("./images/robot.png"));
				Point3D pRobot = r.getGuiLocation();
				if (pRobot != null) // need to remove , prevent null pointer exception (from thread synchronized...)
					g.drawImage(img, pRobot.ix() - 15, pRobot.iy() - 15, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Ellipse2D.Double robotCircle = new Ellipse2D.Double(pRobot.x() - 9, pRobot.y() - 5, 17, 17);
//			g.draw(robotCircle);
//			g.drawOval(pRobot.ix(), pRobot.iy(), 20, 20);

		}

	}

	private double[] xAxis_Min_Max(graph g) {
		double arr[] = { Double.MAX_VALUE, Double.MIN_VALUE }; // min [0] max [1]
		for (node_data n : g.getV()) {
			Point3D p = n.getLocation();
			if (p.x() < arr[0])
				arr[0] = p.x();
			if (p.x() > arr[1])
				arr[1] = p.x();

		}
		return arr;
	}

	private double[] yAxis_Min_Max(graph g) {
		double arr[] = { Double.MAX_VALUE, Double.MIN_VALUE }; // min [0] max [1]
		for (node_data n : g.getV()) {
			Point3D p = n.getLocation();
			if (p.y() < arr[0])
				arr[0] = p.y();
			if (p.y() > arr[1])
				arr[1] = p.y();

		}
		return arr;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Override action listener function , to listen the menu clicks
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		switch (str) {
		case STAGE: {
			String s = JOptionPane.showInputDialog("Please insert number of stage between 0-23");
			int stage;
			try {
				stage = Integer.parseInt(s);
				this.initStage(stage);
				repaint();
				Thread t = new Thread(gc);
				t.start();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			break;
		}

		}
	}

	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
		return res;
	}
}
