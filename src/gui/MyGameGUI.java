package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dataStructure.Fruit;
import dataStructure.GameAlgo;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.GameClient;
import utils.Point3D;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener {
	private GameClient gc;
	private GameAlgo ga;
	private int mc;

	private int MOVE_ROBOT_ID = -1;
	private int MOVE_TO_DEST = -1;

	private static final String GAME = "Game";
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
		gc = new GameClient(stage);

		this.ga = gc.getGameAlgo();
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

		chooseStagePopup();
	}

	private void initMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu menu1 = new Menu("New");
		menuBar.add(menu1);
		this.setMenuBar(menuBar);

		MenuItem game = new MenuItem(GAME);
		game.addActionListener(this);

		menu1.add(game);

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
				this.drawGameInfo(g2d);
				this.drawGraph(g2d);
				this.drawFruits(g2d);
				this.drawRobots(g2d);
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

		synchronized (ga.fruitList()) {
			// fruit set gui location
			for (Fruit f : ga.fruitList()) {
				double xF = scale(f.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN,
						this.getWidth() - Y_SCALE_TMAX);
				double yF = scale(f.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN,
						this.getHeight() - Y_SCALE_TMAX);
				Point3D pF = new Point3D(xF, yF);
				f.setGuiLocation(pF);
			}
		}

		synchronized (ga.robotList()) {
			// robot set gui location
			for (Robot r : ga.robotList()) {
				double xR = scale(r.getLocation().x(), x_scale[0], x_scale[1], X_SCALE_TMIN,
						this.getWidth() - Y_SCALE_TMAX);
				double yR = scale(r.getLocation().y(), y_scale[1], y_scale[0], Y_SCALE_TMIN,
						this.getHeight() - Y_SCALE_TMAX);
				Point3D pR = new Point3D(xR, yR);
				r.setGuiLocation(pR);
			}
		}
	}

	/**
	 * Function that draw the graph with his nodes and edges
	 * 
	 * @param g1
	 */
	private void drawGraph(Graphics2D g) {
		setGuiLocations();

		g.setFont(new Font("Arial", 1, 15));
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

	}

	private void drawFruits(Graphics2D g) {
		synchronized (ga.fruitList()) {
			for (Fruit f : ga.fruitList()) {
				Point3D pFruit = f.getGuiLocation();
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
	}

	private void drawRobots(Graphics2D g) {
		synchronized (ga.robotList()) {
			for (Robot r : ga.robotList()) {
				BufferedImage img;
				try {
					img = ImageIO.read(new File("./images/robot.png"));
					Point3D pRobot = r.getGuiLocation();
					g.drawImage(img, pRobot.ix() - 15, pRobot.iy() - 15, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private void drawGameInfo(Graphics2D g) {

		g.setColor(Color.RED);
		g.setFont(new Font("Arial", 1, 15));
		List<String> rob = gc.getGameRobots();
		for (int i = 0; i < rob.size(); i++) {
			g.drawString(rob.get(i), 130, 100 + (20 * i));
		}

		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial", 1, 17));
		if (gc.getGame().isRunning())
			g.drawString("Time left: " + gc.getGameClock(), 30, 70);
		g.drawString("Score: " + gc.getGameGrade(), 900, 70);
		if (!gc.getGame().isRunning()) {
			g.setColor(Color.ORANGE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Game Over!", 300, 500);
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
		if (gc.IsManual()) {
			int x = e.getX();
			int y = e.getY();
			Point3D p = new Point3D(x, y);
			if (e.getClickCount() == 1) {
				node_data n = findNodeByLocation(p);
				if (n != null) {
					MOVE_TO_DEST = n.getKey();
					try {
						ga.moveRobot(MOVE_ROBOT_ID, MOVE_TO_DEST, gc.getGame(), getG());
						MOVE_TO_DEST = -1;
					} catch (Exception ex) {
						System.out.println(ex);
//						JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR",
//								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			if (e.getClickCount() == 2) {
				Robot r = findRobotByLocation(p);
				if (r != null) {
					MOVE_ROBOT_ID = r.getId();
				}
			}
		}
	}

	private node_data findNodeByLocation(Point3D p) {
		for (node_data node : gc.getGraph().getV()) {
			double distance = node.getGuiLocation().distance2D(p);
			if (distance >= 0 && distance <= 15)
				return node;
		}
		return null;
	}

	private Robot findRobotByLocation(Point3D p) {
		for (Robot robot : ga.robotList()) {
			double distance = robot.getGuiLocation().distance2D(p);
			if (distance >= 0 && distance <= 25)
				return robot;
		}
		return null;

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
		case GAME: {
			chooseStagePopup();
			break;
		}

		}
	}

	private void chooseStagePopup() {
		String s = JOptionPane.showInputDialog("Please insert number of stage between 0-23");
		int stage;
		try {
			stage = Integer.parseInt(s);
			this.initStage(stage);
			repaint();
			int reply = JOptionPane.showConfirmDialog(null, "You want to run manually?", "Choose mode",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				gc.setIsManual(true);
			} else {
				gc.setIsManual(false);
			}
			Thread t = new Thread(gc);
			t.start();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
		return res;
	}
}
