package fr.dauphine.javaavance.phineloops.solver.snail;

import java.util.ArrayDeque;
import java.util.Deque;

import fr.dauphine.javaavance.phineloops.controller.RenderManager;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.solver.Solver;


public class SolverSnail implements Solver{
	private Game originalGame ;
	private int height;
	private int width;
	private Deque<StateSnail> stack = new ArrayDeque<>();
	private int nbIterationInStack=0;
	
	
	public SolverSnail(Game game) {
		this.originalGame = game;
		this.height = game.getHeight();
		this.width = game.getWidth();
	}


	public Game solve(int threads) {
		Game testGame  = new Game(originalGame);
		Shape[][] testBoard = testGame.getBoard();
		int maxStackSize = height*width;
		int i = 0;
		int j = 0;
		int nb = 1;
		
		//If in GUI, link the frame with the game
		boolean guiInit = RenderManager.getIntance().isInit();
		if(guiInit) {
			RenderManager.getIntance().updateGame(testGame);
		}
		
		int shapeType;
		StateSnail initialState = new StateSnail(Direction.EAST,0,i,j,0,1);
		stack.push(initialState);
		while(!stack.isEmpty()) {
			nb++;
			StateSnail iteration = stack.peek();
			i = iteration.getI();
			j = iteration.getJ();
			nbIterationInStack = iteration.getNb();
			int level = iteration.getLevel();
			Shape shape = testBoard[i][j];
			Direction currentDirection = iteration.getDir();
			StateSnail nextIteration = null;
			



			//Print 
			if(nb%100000000==0) {
				System.out.println("itération :"+nb+"  stack :"+stack.size()+"\n"+iteration);
				//System.out.println(testGame);
			}

			switch(currentDirection) {
			/////////////////////////////////////////////////////////////////////////////////DIRECTION IS EAST
			case EAST : 
				shapeType =shape.getType();
				//Case XShape or EmptyShape (do not rotate)
				if(shapeType == 0 || shapeType == 4) {
					stack.pop();
					if(!testGame.isShapeWellConnectedWithNorthAndWest(shape)) {continue;}
				}
				//Can rotate ?		
				else if(iteration.canRotate(shape)) {	
					boolean isWellPlaced = false;
					do{
						iteration.rotate(shape, guiInit);
						
							if(testGame.isShapeWellConnectedWithNorthAndWest(shape)) {isWellPlaced = true;}
						
					}while(!isWellPlaced && iteration.canRotate(shape));
					//if shape has no possible good rotation -> backtrack
					if(!isWellPlaced) {
						stack.pop();
						continue;
					}
				}
				else {
					//Case shape already test all rotation
					stack.pop();
					continue;
				}
				//When the shape is well placed, we prepare next iteration
				//Case last shape of the board
				if(nbIterationInStack==maxStackSize) {
					while(iteration.canRotate(shape)) {
						if(testGame.isShapeFullyConnected(shape)) {
							return testGame;
						}
						iteration.rotate(shape, guiInit);	
					}
					if(testGame.isShapeFullyConnected(shape)) {
						return testGame;
					}
					stack.pop();
					continue;
				}
				if(j<width-1-level) {
					nextIteration = new StateSnail(currentDirection,level, i,j+1,0,nbIterationInStack+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithEast(shape)) {
						nextIteration = new StateSnail(Direction.SOUTH,level,i+1,j,0,nbIterationInStack+1);
					}
					else{
						continue;
					}
				}
				//Add next iteration to stack
				stack.push(nextIteration);
				break;
				/////////////////////////////////////////////////////////////////////////////////DIRECTION IS SOUTH
			case SOUTH : 
				shapeType =shape.getType();
				//Case XShape or EmptyShape (do not rotate)
				if(shapeType == 0 || shapeType == 4) {
					stack.pop();
					if(!testGame.isShapeWellConnectedWithNorthAndEast(shape)) {continue;}
				}
				//Can rotate ?		
				else if(iteration.canRotate(shape)) {	
					boolean isWellPlaced = false;
					do{
						iteration.rotate(shape, guiInit);
						
							if(testGame.isShapeWellConnectedWithNorthAndEast(shape)) {isWellPlaced = true;}
						
					}while(isWellPlaced == false && iteration.canRotate(shape));
					//if shape has no possible good rotation -> backtrack
					if(!isWellPlaced) {
						stack.pop();
						continue;
					}
				}
				else {				
					//Case shape already test all rotation					
						stack.pop();
						continue;	
				}
				//When the shape is well placed, we prepare next iteration

				//Case last shape of the board
				if(nbIterationInStack==maxStackSize) {
					while(iteration.canRotate(shape)) {
						if(testGame.isShapeFullyConnected(shape)) {
							return testGame;
						}
						iteration.rotate(shape, guiInit);		
					}
					if(testGame.isShapeFullyConnected(shape)) {
						return testGame;
					}
					stack.pop();
					continue;
				}
				if(i<height-1-level) {
					nextIteration = new StateSnail(currentDirection,level,i+1,j,0,nbIterationInStack+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithSouth(shape)) {
						nextIteration = new StateSnail(Direction.WEST,level,i,j-1,0,nbIterationInStack+1);
					}
					else{
						continue;
					}
				}
				//Add next iteration to stack
				stack.push(nextIteration);
				break;
				////////////////////////////////////////////////////////////////////////////////////DIRECTION IS WEST
			case WEST : 
				shapeType =shape.getType();
				//Case XShape or EmptyShape (do not rotate)
				if(shapeType == 0 || shapeType == 4) {
					stack.pop();
					if(!testGame.isShapeWellConnectedWithSouthAndEast(shape)) {continue;}
				}
				//Can rotate ?		
				else if(iteration.canRotate(shape)) {	
					boolean isWellPlaced = false;
					do{
						iteration.rotate(shape, guiInit);
						
							if(testGame.isShapeWellConnectedWithSouthAndEast(shape)) {isWellPlaced = true;}
						
					}while(isWellPlaced == false && iteration.canRotate(shape));
					//if shape has no possible good rotation -> backtrack
					if(!isWellPlaced) {
						stack.pop();
						continue;
					}
				}
				else {
					
					//Case shape already test all rotation			
						stack.pop();
						continue;
				}
				//When the shape is well placed, we prepare next iteration

				//Case last shape of the board
				if(nbIterationInStack==maxStackSize) {
					while(iteration.canRotate(shape)) {
						if(testGame.isShapeFullyConnected(shape)) {
							return testGame;
						}
						iteration.rotate(shape, guiInit);	
					}
					if(testGame.isShapeFullyConnected(shape)) {
						return testGame;
					}
					stack.pop();
					continue;
				}
				if(j>0+level) {
					nextIteration = new StateSnail(currentDirection,level,i,j-1,0,nbIterationInStack+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithWest(shape)) {
						nextIteration = new StateSnail(Direction.NORTH,level,i-1,j,0,nbIterationInStack+1);
					}
					else{
						continue;
					}
				}
				//Add next iteration to stack
				stack.push(nextIteration);
				break;

				/////////////////////////////////////////////////////////////////////////////////DIRECTION IS NORTH
			case NORTH : 
				shapeType =shape.getType();
				//Case XShape or EmptyShape (do not rotate)
				if(shapeType == 0 || shapeType == 4) {
					stack.pop();
					if(!testGame.isShapeWellConnectedWithSouthAndWest(shape)) {continue;}
				}
				//Can rotate ?		
				else if(iteration.canRotate(shape)) {	
					boolean isWellPlaced = false;
					do{
						iteration.rotate(shape, guiInit);
						
							if(testGame.isShapeWellConnectedWithSouthAndWest(shape)) {isWellPlaced = true;}
						
					}while(isWellPlaced == false && iteration.canRotate(shape));
					//if shape has no possible good rotation -> backtrack
					if(!isWellPlaced) {
						stack.pop();
						continue;
					}
				}
				else {
					
					//Case shape already test all rotation				
						stack.pop();
						continue;				
				}
				//When the shape is well placed, we prepare next iteration

				//Case last shape of the board
				if(nbIterationInStack==maxStackSize) {
					while(iteration.canRotate(shape)) {
						if(testGame.isShapeFullyConnected(shape)) {
							return testGame;
						}
						iteration.rotate(shape, guiInit);	
					}
					if(testGame.isShapeFullyConnected(shape)) {
						return testGame;
					}
					stack.pop();
					continue;
				}
				if(i>1+level) {
					nextIteration = new StateSnail(currentDirection,level,i-1,j,0,nbIterationInStack+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithNorth(shape)) {
						nextIteration = new StateSnail(Direction.EAST,level+1,i,j+1,0,nbIterationInStack+1);
					}
					else{
						continue;
					}
				}
				//Add next iteration to stack
				stack.push(nextIteration);
				break;
			}
		}
		return null;
	}
}
