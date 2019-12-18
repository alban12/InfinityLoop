package fr.dauphine.javaavance.phineloops.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Game {
	private static int NORTH = 0;
	private static int SOUTH = 1;
	private static int EAST = 2;
	private static int WEST = 3;
	private int width;
	private int height;
	private int maxcc;
	private Shape[][] board;

	public Game(int width, int height, int maxcc) {
		if(width<1) {
			throw new IllegalArgumentException("Width must be >= 1");
		}
		if(height<1) {
			throw new IllegalArgumentException("Height must be >= 1");
		}
		if(maxcc<0) {
			//|| maxcc<= (width*height)/2 / The smallest connected component you can put in a grid fill 2 cases => maxcc must be < numberOfCases/2
			throw new IllegalArgumentException("Maximum cc must be positive and you have to put at least less cc than the number of cases");
			//FIXME
		}
		this.width = width;
		this.height = height;
		this.maxcc = maxcc;
		board = new Shape[height][width];
	}


	public Game(File file) {
		load(file);
	}

	public Game(Game game) {
		this.height = game.height;
		this.width = game.width;
		board = new Shape[height][width];
		Shape[][] boardToClone = game.board;
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				Shape s = boardToClone[i][j];
				switch(s.getType()){
					case 0 : board[i][j] = new EmptyShape(s.getOrientation(), i, j);break;
					case 1 : board[i][j] = new QShape(s.getOrientation(), i, j);break;
					case 2 : board[i][j] = new IShape(s.getOrientation(), i, j);break;
					case 3 : board[i][j] = new TShape(s.getOrientation(), i, j);break;
					case 4 : board[i][j] = new XShape(s.getOrientation(), i, j);break;
					case 5 : board[i][j] = new LShape(s.getOrientation(), i, j);break;
				}
				//board[i][j] = s.getShapeType().buildShape(s.getOrientation(), i, j);
			}
		}
	}



	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Shape[][] getBoard() {
		return board;
	}

	public int getMaxCC()
	{
		return this.maxcc;
	}

	public void generate() {
		Generator generator = new Generator(this);
		generator.generate();
	}

	private void load(File file) {

	}

	public void solve() {

	}



	public void write(String outputFile) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(outputFile);
		PrintStream ps = new PrintStream(fos);
		ps.println(height);
		ps.println(width);
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				ps.println(board[i][j]);
			}
		}
	}

	public boolean isShapeFullyConnected(Shape shape) {
		return isShapeWellConnectedWithEast(shape)&&isShapeWellConnectedWithNorth(shape)&&isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithWest(shape);
		/*
		if(shape == null) {
			return false;
		}
		Shape[] neighbors = getNeighbors(shape); 
		List<Connection> connections = shape.getConnections();
		if(countNeighbors(neighbors)<connections.size()) {
			return false;
		}

		for(Connection connection : connections) {
			switch(connection) {
			case NORTH : if (neighbors[NORTH] == null || !neighbors[NORTH].hasConnection(Connection.SOUTH)) {return false;} break;
			case SOUTH : if (neighbors[SOUTH] == null || !neighbors[SOUTH].hasConnection(Connection.NORTH)) {return false;} break;
			case EAST :  if (neighbors[EAST] == null || !neighbors[EAST].hasConnection(Connection.WEST)) {return false;} break;
			case WEST :  if (neighbors[WEST] == null || !neighbors[WEST].hasConnection(Connection.EAST)) {return false;} break;
			default : break;
			}
		}
		return true;*/
	}

	private int countNeighbors(Shape[] neighbors) {
		int count = 0;
		for(int i=0;i<4;i++) {
			if(neighbors[i] != null) {
				count++;
			}
		}
		return count;
	}

	public boolean iShapeConnectedToBoardBorder(Shape shape) {

		int i = shape.getI();
		int j = shape.getJ();
		
		if(i>0 && j>0 && i<height-1 && j<width-1 ) {
			return false;
		}
		if(i == 0) {
			if(shape.hasConnection(Connection.NORTH)) {
				return true;
			}
		}
		else if(i == height-1) {
			if(shape.hasConnection(Connection.SOUTH)) {
				return true;
			}
		}
		if(j == 0) {
			if(shape.hasConnection(Connection.WEST)) {
				return true;
			}
		}
		else if(j == width-1) {
			if(shape.hasConnection(Connection.EAST)) {
				return true;
			}
		}
		return false;
		/*
		List<Connection> connections = shape.getConnections();
		Shape[] neighbors = getNeighbors(shape);
		for(Connection connection : connections) {
			switch(connection) {
			case NORTH : if (neighbors[NORTH] == null ) {return true;} break;
			case SOUTH : if (neighbors[SOUTH] == null) {return true;} break;
			case EAST :  if (neighbors[EAST] == null ) {return true;} break;
			case WEST :  if (neighbors[WEST] == null ) {return true;} break;
			}
		}
		return false;

		/* Other method. Same time cost
		int i = shape.getI();
		int j = shape.getJ();
		List<Connection> connections = shape.getConnections();
		for(Connection connection : connections) {
			switch(connection) {
			case NORTH : if (i==0) {return true;} break;
			case SOUTH : if (i==height-1) {return true;} break;
			case EAST :  if (j==width-1) {return true;} break;
			case WEST :  if (j==0) {return true;} break;
			}
		}
		return false;
		 */
	}



	public boolean isShapeWellConnectedWithNorthAndWest(Shape shape) {
		return isShapeWellConnectedWithNorth(shape)&&isShapeWellConnectedWithWest(shape);
	}

	public boolean isShapeWellConnectedWithNorthAndEast(Shape shape) {
		return isShapeWellConnectedWithNorth(shape)&&isShapeWellConnectedWithEast(shape);
	}

	public boolean isShapeWellConnectedWithSouthAndEast(Shape shape) {
		return isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithEast(shape);
	}

	public boolean isShapeWellConnectedWithSouthAndWest(Shape shape) {
		return isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithWest(shape);
	}

	public boolean isShapeWellConnectedWithSouth(Shape shape) {
		List<Connection> connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(i+1<height) {
			neighbor = board[i+1][j];
		}
		if (neighbor != null ) {
			if(neighbor.hasConnection(Connection.NORTH)) {
				if (!connections.contains(Connection.SOUTH)) {
					return false;
				}
			}
			else if (connections.contains(Connection.SOUTH)){
				return false;
			}
		}
		else if (connections.contains(Connection.SOUTH)) {
			return false;
		}
		return true;
	}

	public boolean isShapeWellConnectedWithNorth(Shape shape) {
		List<Connection> connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(i-1>=0) {
			neighbor = board[i-1][j];
		}
		if (neighbor != null ) {
			if(neighbor.hasConnection(Connection.SOUTH)) {
				if (!connections.contains(Connection.NORTH)) {
					return false;
				}
			}
			else if (connections.contains(Connection.NORTH)){
				return false;
			}
		}
		else if (connections.contains(Connection.NORTH)) {
			return false;
		}
		return true;
	}

	public boolean isShapeWellConnectedWithEast(Shape shape) {
		List<Connection> connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(j+1<width) {
			neighbor = board[i][j+1];
		}
		if (neighbor != null ) {
			if(neighbor.hasConnection(Connection.WEST)) {
				if (!connections.contains(Connection.EAST)) {
					return false;
				}
			}
			else if (connections.contains(Connection.EAST)){
				return false;
			}
		}
		else if (connections.contains(Connection.EAST)) {
			return false;
		}
		return true;
	}

	public boolean isShapeWellConnectedWithWest(Shape shape) {
		List<Connection> connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(j-1>=0) {
			neighbor = board[i][j-1];
		}
		if (neighbor != null ) {
			if(neighbor.hasConnection(Connection.EAST)) {
				if (!connections.contains(Connection.WEST)) {
					return false;
				}
			}
			else if (connections.contains(Connection.WEST)){
				return false;
			}
		}
		else if (connections.contains(Connection.WEST)) {
			return false;
		}
		return true;
	}
	/*
	List<Connection> connections = shape.getConnections();
	Shape[] neighbors = getNeighbors(shape);
	if (neighbors[WEST] != null ) {
		if(neighbors[WEST].hasConnection(Connection.EAST)) {
			if (!connections.contains(Connection.WEST)) {
				return false;
			}
		}
		else {
			if (connections.contains(Connection.WEST)) {
				return false;
			}
		}
	}
	return true;
}*/
	public Shape[] getNeighbors(Shape shape){
		int i = shape.getI();
		int j = shape.getJ();

		Shape[] neighbors = new Shape[4];

		//north neighbor
		if(i-1>=0) {
			neighbors[NORTH] = board[i-1][j];
		}
		//south neighbor
		if(i+1<height) {
			neighbors[SOUTH] = board[i+1][j];
		}
		//east neighbor
		if(j+1<width) {
			neighbors[EAST] = board[i][j+1];
		}
		//west neighbor
		if(j-1>=0) {
			neighbors[WEST] = board[i][j-1];
		}
		return neighbors;
	}

	public void addShape(Shape shape) throws Exception {
		if(shape == null) {
			return;
		}
		int shapeI =shape.getI();
		int shapeJ = shape.getJ();
		if(board[shapeI][shapeJ] == null) {
			board[shapeI][shapeJ] = shape;
			return;
		}
		throw new Exception("There is already a shape at "+shapeI+","+shapeJ);


	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				sb.append(board[i][j].getSymbol());
			}
			sb.append("\n");
		}
		return sb.toString();
	}









}
