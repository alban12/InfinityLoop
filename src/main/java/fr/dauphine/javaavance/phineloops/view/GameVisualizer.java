package fr.dauphine.javaavance.phineloops.view;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import fr.dauphine.javaavance.phineloops.checker.Checker;
import fr.dauphine.javaavance.phineloops.controller.RenderManager;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.solver.line.SolverLineByLine;
import fr.dauphine.javaavance.phineloops.solver.snail.SolverSnail;

public class GameVisualizer extends JFrame implements ActionListener{
	

	private static final long serialVersionUID = 1L;
	public static final int WIDTH_SIZE = 1000;
	public static final int HEIGHT_SIZE = 1000;
	public static final int MATRIX_SIZE = 6;

	private Game game; 
	private int height;
	private int width;
	private HashMap<String, ImageIcon> map = new HashMap<String, ImageIcon>();
	private int buttonSize;


	//public static GameDrawer game;

	private ShapeButton[][] buttons;




	public GameVisualizer(Game game) {
		super();
		this.game = game;
		init();
		this.setVisible(true);
		RenderManager.getIntance().init(this);
		addMenuBar();
		drawGame();
	}



	private void init() {
		map.put("0 0",  new ImageIcon(getClass().getResource("/images/00.png")));
		map.put("1 0",  new ImageIcon(getClass().getResource("/images/10.png")));
		map.put("1 1",  new ImageIcon(getClass().getResource("/images/11.png")));
		map.put("1 2", new ImageIcon( getClass().getResource("/images/12.png")));
		map.put("1 3",  new ImageIcon(getClass().getResource("/images/13.png")));
		map.put("2 0",  new ImageIcon(getClass().getResource("/images/20.png")));
		map.put("2 1",  new ImageIcon(getClass().getResource("/images/21.png")));
		map.put("3 0",  new ImageIcon(getClass().getResource("/images/30.png")));
		map.put("3 1",  new ImageIcon(getClass().getResource("/images/31.png")));
		map.put("3 2",  new ImageIcon(getClass().getResource("/images/32.png")));
		map.put("3 3",  new ImageIcon(getClass().getResource("/images/33.png")));
		map.put("4 0",  new ImageIcon(getClass().getResource("/images/40.png")));
		map.put("5 0",  new ImageIcon(getClass().getResource("/images/50.png")));
		map.put("5 1",  new ImageIcon(getClass().getResource("/images/51.png")));
		map.put("5 2",  new ImageIcon(getClass().getResource("/images/52.png")));
		map.put("5 3",  new ImageIcon(getClass().getResource("/images/53.png")));

		this.height = game.getHeight();
		this.width = game.getWidth();
		buttonSize = HEIGHT_SIZE/Math.max(height,width);
		this.setSize(WIDTH_SIZE,HEIGHT_SIZE);
		this.setTitle("INFINITY LOOP GAME");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		initButtons();
	}



	/**
	 * Adapt the layout to the game width and height. Link the buttons to the shapes.
	 */
	private void initButtons() {
		int max = Math.max(height,width); 
		setLayout(new GridLayout(max, max));
		buttons = new ShapeButton[height][width];
		Shape[][] board = game.getBoard();
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				ShapeButton button = new ShapeButton(board[i][j]);
				buttons[i][j] = button;
				add(button);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						button.getShape().rotate();
						refreshButton(button);
						if(Checker.check(game)) {
							displayDialog();
						}
					}
					
					private void displayDialog() {
						JOptionPane.showMessageDialog(GameVisualizer.this, "Well done ! If you want to replay click on generate in the menu bar");		
					}
				});
			}
		}
	}

	/**
	 * Add menu bar with menu "Solve" and "Generate"
	 */
	private void addMenuBar() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("Solve");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);

		//Solver line item
		menuItem = new JMenuItem("Solve with SolverLineByLine",
				KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SolverLineByLine solver = new SolverLineByLine(GameVisualizer.this.game);
				GameVisualizer.this.game = solver.solve(4);
				drawGame();
				updateButtons(game);
				
			}
		});
		menu.add(menuItem);
		
		//Solver snail button
		menuItem = new JMenuItem("Solve with SolverSnail",
				KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SolverSnail solver = new SolverSnail(GameVisualizer.this.game);
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						GameVisualizer.this.game = solver.solve(4);
						
					}
				});
				t.start();
			}
		});
		menu.add(menuItem);



		//Build second menu in the menu bar.
		menu = new JMenu("Generate");
		menu.setMnemonic(KeyEvent.VK_N);
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Generate new grid",
				KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_8, ActionEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String resp = JOptionPane.showInputDialog(GameVisualizer.this, "Enter width, height with this format :\"wxh\"");
				try {
					String[] dims = resp.split("x");
					width = Integer.parseInt(dims[0]);
					height = Integer.parseInt(dims[1]);
					//int nbcc = Integer.parseInt(dims[1].split(" ")[1]);
					GameVisualizer.this.height = height;
					GameVisualizer.this.width = width;
					if(height>1&&width>1) {
						GameVisualizer.this.game = new Game(height, width);
						GameVisualizer.this.game.generate();
						resetLayout();
					}
					
				}
				catch(NumberFormatException exp){
					JOptionPane.showMessageDialog(GameVisualizer.this, "Warning : Negative dims");
				}
				
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	} 
	
	/**
	 * Detroy the current frame and start a new one with a game's size layout
	 */
	private void resetLayout() {
		setLayout(null);
		this.dispose();
		new GameVisualizer(game);
	}

/**
 * Refresh the image of a button
 * @param button
 */
	private void refreshButton(ShapeButton button) {
		String  shapeId = button.getShape().toString();
		ImageIcon icon = new ImageIcon(map.get(shapeId).getImage().getScaledInstance(buttonSize, buttonSize, java.awt.Image.SCALE_SMOOTH));
		button.setIcon(icon);	
	}

	/**
	 * Draw the entire baord
	 */
	public void drawGame() {
		Shape[][] board = game.getBoard();
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				String shapeId = board[i][j].toString();
				ImageIcon icon = new ImageIcon(map.get(shapeId).getImage().getScaledInstance(buttonSize, buttonSize, java.awt.Image.SCALE_SMOOTH));
				buttons[i][j].setIcon(icon);
			}
		}
	}

	/**
	 * Link the grid layout buttons to a specific game with same height and width
	 * @param game : the game to link
	 */
	public void updateButtons(Game game) {
		Shape[][] board= game.getBoard();
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				buttons[i][j].setShape(board[i][j]);
			}
		}
	}



	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	/**
	 * Refresh the image of a specific shape's button
	 * @param shape : the shape
	 */
	public void updateShape(Shape shape) {
		int i = shape.getI();
		int j = shape.getJ();
		ShapeButton button = buttons[i][j];
		refreshButton(button);			
	}

}
