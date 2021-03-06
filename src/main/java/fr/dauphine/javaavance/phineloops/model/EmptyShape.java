package fr.dauphine.javaavance.phineloops.model;

public class EmptyShape extends Shape {
	private int[] domain = {0};
	private int type = 0;
	private int nbConnection = 0;
	
	public EmptyShape(int orientation,int i,int j) {
		super(orientation,i,j);
		this.possibleOrientation = new boolean[]{true};
		this.domainSize = 1;
	}

	
	
	public int[] getDomainWithPruning(Game game) {
		return domain;
	}
	
	public String getSymbol() {
		return " ";
	}
	
	public int getMaxRotation() {
		return 0;
	}
	@Override
	public int getType() {
		return type;
	}
	
	@Override
	public void rotate() {
	}

	public int getNbConnection() {
		return nbConnection;
	}

	public void setNbConnection(int nbConnection) {
		this.nbConnection = nbConnection;
	}

	@Override
	public void rotateTo(int orientation) {
		
	}



	@Override
	public int[] getDomain() {
		// TODO Auto-generated method stub
		return null;
	}
}
