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
 * last updated: 2017-03-27
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

	
	public void gameStep(){
		if(!gameOver){
			if(!tShapeActive){
				generateTetrominoe();
			}else{
				moveTetrominoe();
				clearFilledRow();	
			}
		}else{ // game over
			setChanged();
			notifyObservers(gameOver);
			timer.stop();
		}
	}

	//skapa en ny shape och sätt ut den på spelplanen
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

	private void gridUpdateNextPiece(){
		setChanged();
		notifyObservers(new String("NEXT"));
	}

	private void gridUpdateStatic() {
		setChanged();
		notifyObservers(new String("STATIC"));
	}

	private void gridUpdateActive() {
		setChanged();
		notifyObservers(new String("ACTIVE"));
	}


	//flyttar den ett steg neråt om icke block, ska ske vid varje tick
	public void moveTetrominoe(){
		if(currentY < 21){
			coords = activeTetro.getCoords(); 
			boolean isBlocked = checkCollisionsUnder(currentY+1);
			currentY++; 
			if(!isBlocked){	
				rotatable = true;
				gridUpdateActive();
			}else{ //den blev blockad, ska bli static nu 
				rotatable = false;
				setActiveStatic();
			}
		}else{
			currentY++;
			setActiveStatic();
			rotatable = false;
		}
	}



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
		gridUpdateStatic(); // först här kallas updateStatic, alltså en efter updateActive
	}

	/*Returns true if there is an collision of the new potential location, otherwise false 
	 * and it's safe to keep going
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

	/*Returns true if there is an collision of the new potential location one step to the right/left, otherwise false 
	 * and it's safe to keep going
	 * 
	 * @param newX - The new potential startlocation for the x-coordinate, to check right
	 * it should be currentX + 1, and for left currentX - 1
	 * @param coords - The shape of the tetrominoe
	 * @return True if there is in fact a collision, false otherwise
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

	//Check for kollisions
	/* Move the active tetrominoe one step to the right*/
	public void stepRight(){
		if(currentX < (10-coords[0].length)){
			//searchEmptyCol(new String("RIGHT"));
			if(!checkCollisionsSide(currentX+1)){
				currentX++;
				gridUpdateActive();	
			}
		}
	}

	public void stepLeft(){
		if(currentX > 0){
			if(!checkCollisionsSide(currentX-1)){
				currentX--;
				gridUpdateActive();	
			}
		}
	}

	//bara om det går
	public void rotateRight(){
		if(rotatable){
			outOfBoundsRight();
			activeTetro.rotateRight();
			coords = activeTetro.getCoords();
			gridUpdateActive();
		}		
	}


	//bara om det går
	public void rotateLeft(){
		if(rotatable){
			outOfBoundsRight();
			activeTetro.rotateLeft();
			coords = activeTetro.getCoords();	
			gridUpdateActive();
		}
	}

	/* 
	 *  is it only out of bounds on the right side?
	 */
	private void outOfBoundsRight(){
		int newwidth = coords.length-1; 
		while( (currentX + newwidth) > 9){ //block höger
			currentX--;
		}
	}

	//kolla om en rad(eller alla?) är fylld och ska därför bort
	private boolean rowFilled(boolean[] row){
		int rowCount = 0;
		for(int i = 0; i<row.length; i++){
			if(row[i]== true){
				rowCount++;
			}
		}
		return rowCount == row.length? true: false;
	}

	private boolean[] getRow(int rowIndex){
		boolean[] newRow = new boolean[wBOARD];
		for(int i = 0; i<wBOARD; i++){
			newRow[i] = false;
			newRow[i] = grid[rowIndex][i].getBool();
		}
		return newRow;
	}

	private void clearRow(int index){
		for(int i = 0; i<wBOARD; i++){
			grid[index][i].setFalse();
			grid[index][i].setColorGridTile(Color.GRAY); //ta bort befintlig färg
		}
	}


	//everything above this index should be shifted down one step
	private void shiftDown(int index){
		for(int i = index; i>0; i--){
			for(int j = 0; j<wBOARD; j++){
				if(grid[i][j].getBool()== true){
					grid[i+1][j].setTrue(); //flytta ner true
					grid[i+1][j].setColorGridTile(grid[i][j].getColor()); //flytta ner färg
					grid[i][j].setColorGridTile(Color.GRAY); //ta bort gammal färg ovanför
				}else{
					grid[i+1][j].setFalse();
				}
			}
		}
		gridUpdateStatic();
	}

	/*
	 * 
	 */
	public void clearFilledRow(){
		//lopp through all row, check if filled
		for(int i = 0; i<hBOARD; i++){
			if( rowFilled(getRow(i)) ){ //denna raden är fylld
				//System.out.println("Raden som nu ska clearas och flytta ner är index" + i);
				clearRow(i);
				clearedRows++;
				shiftDown(i-1); //en ovanför raden som clearas ?
			}
		}	
	}

	//when the view needs to update
	public GridTile[][] getGrid(){
		return grid;
	}

	public void pauseGame(){
		paused = true;
	}

	public void unPauseGame(){
		paused = false;
	}

	//varje nytt tick
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!paused){
			gameStep();
		}	
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
	public void activateFastDrop() {
		timer.setDelay(90);
	}

	public void deActivateFastDrop() {
		timer.setDelay(timerInterval);
	}
}
