import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.Timer;


/** Board class to represent the tetris board. All the board logic happpens here 
 * 
 * last updated: 2017-04-11
 * 
 * @author Martin Bergström
 *
 */
public class Board extends Observable implements ActionListener{
	public static final int hBOARD = 22;
	public static final int wBOARD = 10;

	private Timer timer;
	private int timerInterval = 400;
	private int currentX = 0;
	private int currentY = 0;
	private int[][] coords;
	private boolean rotatable;
	private int clearedRows; 
	private Tshape activeTetro;
	private Tshape nextTetro;
	private GridTile[][] grid;
	private boolean paused;
	private boolean tShapeActive;
	private boolean gameOver;

	/**
	 * Creates the Board and initialized the grid
	 */
	public Board(){
		paused = false;
		tShapeActive = false;
		clearedRows = 0;
		gameOver = false;
		grid = new GridTile[hBOARD][wBOARD];
		for(int i = 0; i<hBOARD; i++){
			for(int j = 0; j<wBOARD; j++){
				grid[i][j] = new GridTile();
				grid[i][j].setFalse();			
			}
		}
		timer = new Timer(timerInterval,this); 
		timer.start(); 
		activeTetro = new Tshape();
		activeTetro.randomize(); 
		nextTetro = new Tshape();
		nextTetro.randomize();
	}

	/**
	 * Move tetrominoes each tick/gamestep as long as it's not game over
	 */
	public void gameStep(){
		if(!gameOver){
			if(!tShapeActive){
				generateTetrominoe();
			}else{
				moveTetrominoe();
				clearFilledRow();	
			}
		}else{ 
			setChanged();
			notifyObservers(gameOver);
			timer.stop();
		}
	}

	/**
	 * Creates a new random tetrominoe and places it on the grid
	 */
	public void generateTetrominoe(){
		activeTetro = nextTetro;
		nextTetro = new Tshape();
		nextTetro.randomize();
		gridUpdateNextPiece();
		coords = activeTetro.getCoords();
		currentY = coords.length-1;
		currentX = 3;
		boolean isBlocked = checkCollisionsUnder(currentY);
		if(isBlocked){
			gameOver = true;
			System.out.println("!!GAME OVER!!!");
			notifyObservers(gameOver);
		}
		gridUpdateActive();
		tShapeActive = true; 
	}

	/**
	 * Tells the observer that a new piece is placed -> update the grid 
	 */
	private void gridUpdateNextPiece(){
		setChanged();
		notifyObservers(new String("NEXT"));
	}

	/**
	 * Tells the observer to update the static grid
	 */
	private void gridUpdateStatic() {
		setChanged();
		notifyObservers(new String("STATIC"));
	}

	/**
	 * Tells the observer to update the active tetrominoe
	 */
	private void gridUpdateActive() {
		setChanged();
		notifyObservers(new String("ACTIVE"));
	}

	/**
	 * Moves the active tetrominoe one step down the grid at each game tick as long as it's
	 * not blocked and hasn't reached the bottom of the grid.
	 */
	public void moveTetrominoe(){
		if(currentY < 21){
			coords = activeTetro.getCoords(); 
			boolean isBlocked = checkCollisionsUnder(currentY+1);
			currentY++; 
			if(!isBlocked){	
				rotatable = true;
				gridUpdateActive();
			}else{
				rotatable = false;
				setActiveStatic();
			}
		}else{
			currentY++;
			setActiveStatic();
			rotatable = false;
		}
	}

	/**
	 * Sets the active tetrominoe to become static when collision or reached bottom occured
	 */
	private void setActiveStatic(){
		for(int i = 0; i<coords.length; i++){
			for(int j = 0; j<coords[i].length; j++){
				if(coords[i][j] == 1){
					grid[(currentY-coords.length)+i][currentX+j].setTrue();
					grid[(currentY-coords.length)+i][currentX+j].setColorGridTile(activeTetro.getColor());
				}
			}
		}
		rotatable = false;
		activeTetro = null;
		tShapeActive = false;
		gridUpdateStatic(); 
	}

	/**
	 * Checks if there is a collision underneath
	 * 
	 * @param newY the new y-coordinate for this tetrominoe to check collision for
	 * @return true if there is a collision at the place one step under
	 */
	private boolean checkCollisionsUnder(int newY) {
		for(int i = 0; i<coords.length; i++){
			for(int j = 0; j<coords[0].length; j++){
				if( (coords[i][j] == 1)&&(grid[(newY-coords.length +1) +i][currentX+j].getBool()==true) ){ // vi har en krock?
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if there is a collision to either side of the active tetro
	 * 
	 * @param newX the new x-coordinate to check collisions for, 
	 * currentx+1 is one step to the right and currentx-1 is to the left
	 * 
	 * @return true if there is a collision at the newx-coordinate given
	 */
	private boolean checkCollisionsSide(int newX) {
		for(int i = 0; i<coords.length; i++){
			for(int j = 0; j<coords[0].length; j++){
				if( (coords[i][j] == 1) && (grid[(currentY-coords.length +1) +i][newX+j].getBool()==true) ){
					return true;
				}
			}
		}
		return false;
	}	

	/**
	 * Tries to move the active tetro one step to the right, 
	 * if it's possible (not blocked or out of bounds)
	 */
	public void stepRight(){
		if(currentX < (10-coords[0].length)){
			if(!checkCollisionsSide(currentX+1)){
				currentX++;
				gridUpdateActive();	
			}
		}
	}

	/**
	 * Tries to move the active tetro one step to the left, 
	 * if it's possible (not blocked or out of bounds)
	 */
	public void stepLeft(){
		if(currentX > 0){
			if(!checkCollisionsSide(currentX-1)){
				currentX--;
				gridUpdateActive();	
			}
		}
	}

	/**
	 * Tries to rotate the active tetro one step to the right, 
	 * if it's possible (not blocked or out of bounds)
	 */
	public void rotateRight(){
		if(rotatable){
			outOfBoundsRight();
			activeTetro.rotateRight();
			coords = activeTetro.getCoords();
			gridUpdateActive();
		}		
	}


	/**
	 * Tries to rotate the active tetro one step to the left, 
	 * if it's possible (not blocked or out of bounds)
	 */
	public void rotateLeft(){
		if(rotatable){
			outOfBoundsRight();
			activeTetro.rotateLeft();
			coords = activeTetro.getCoords();	
			gridUpdateActive();
		}
	}


	/**
	 * Checks if the currentX-coordinate of the active tetro is out of bounds and in that case adjusts it.
	 * This could happen after rotate
	 */
	private void outOfBoundsRight(){
		int newwidth = coords.length-1; 
		while( (currentX + newwidth) > 9){ //block höger
			currentX--;
		}
	}

	/**
	 * Checks if a row is filled
	 * 
	 * @param row the row to check
	 * @return true if the row is filled
	 */
	private boolean rowFilled(boolean[] row){
		int rowCount = 0;
		for(int i = 0; i<row.length; i++){
			if(row[i]== true){
				rowCount++;
			}
		}
		return rowCount == row.length? true: false;
	}

	/**
	 * Gets the grid row at the specified index
	 * 
	 * @param rowIndex the row to get
	 * @return the row at that index
	 */
	private boolean[] getRow(int rowIndex){
		boolean[] newRow = new boolean[wBOARD];
		for(int i = 0; i<wBOARD; i++){
			newRow[i] = false;
			newRow[i] = grid[rowIndex][i].getBool();
		}
		return newRow;
	}

	/**
	 * Clears the row
	 * 
	 * @param index the row to clear
	 */
	private void clearRow(int index){
		for(int i = 0; i<wBOARD; i++){
			grid[index][i].setFalse();
			grid[index][i].setColorGridTile(Color.GRAY);
		}
	}

	/**
	 * Shifts down all the rows filled with tetrominoes above an index
	 * 
	 * @param index the row index where everything above should be shifted down 
	 */
	private void shiftDown(int index){
		for(int i = index; i>0; i--){
			for(int j = 0; j<wBOARD; j++){
				if(grid[i][j].getBool()== true){
					grid[i+1][j].setTrue(); 
					grid[i+1][j].setColorGridTile(grid[i][j].getColor()); 
					grid[i][j].setColorGridTile(Color.GRAY); 
				}else{
					grid[i+1][j].setFalse();
				}
			}
		}
		gridUpdateStatic();
	}

	/**
	 * Checks the whole game grid for filled rows and calls clearRow and shiftDown on the filled rows
	 */
	public void clearFilledRow(){
		for(int i = 0; i<hBOARD; i++){
			if( rowFilled(getRow(i)) ){
				clearRow(i);
				clearedRows++;
				shiftDown(i-1); 
			}
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!paused){
			gameStep();
		}	
	}

	/**
	 * Actives the fast drop-mode where timer will delay 90ms
	 */
	public void activateFastDrop() {
		timer.setDelay(90);
	}
	
	/**
	 * De-activates the fast drop-mode and returns to normal timerInterval 
	 */
	public void deActivateFastDrop() {
		timer.setDelay(timerInterval);
	}
	
	// GETTER AND SETTERS ///

	public void pauseGame(){
		paused = true;
	}

	public void unPauseGame(){
		paused = false;
	}

	public GridTile[][] getGrid(){
		return grid;
	}

	public int getCurrentX() {
		return currentX;
	}
	public int getCurrentY(){
		return currentY;
	}

	public int getClearedRows(){
		return clearedRows;
	}

	public boolean getTshapeActive(){
		return tShapeActive;
	}

	public int[][] getActive(){
		return activeTetro.getCoords();
	}
	public int[][] getNextPieceCoords(){
		return nextTetro.getCoords();
	}
	public Color getCurrentTetroColor(){
		return activeTetro.getColor();	
	}
	public Color getNextPieceColor(){
		return nextTetro.getColor();
	}
}
