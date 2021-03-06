import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sun.xml.internal.ws.transport.http.ResourceLoader;


/**
 * The main view class to represent the game grid graphically. 
 * Acts as an observer on the board model
 * 
 * @author Martin Bergstr�m
 *
 */
public class TetrisMain implements Observer, KeyListener {
	private Tile[][] gameGrid;
	private Tile[][] nextPieceGrid; 
	private JFrame frame;
	private Board board;
	private JLabel scoreLabel;
	private JLabel currentLvl;

	private static final int wBOARD = 10;
	private static final int hBOARD = 22;

	private ArrayList<Point> oldCoordinatesActive;

	public TetrisMain(){
		oldCoordinatesActive = new ArrayList<Point>();

		frame = new JFrame("Tetris");
		frame.setLayout(new BorderLayout());

		//SCOREPANEL
		JPanel scorePanel = new JPanel(new GridLayout(3,1));
		//NEXTPIECE
		JPanel nextPiecePanel = new JPanel(new GridLayout(2,1));
		JLabel topLabelNextP = new JLabel();
		JPanel nextPieceGridPanel = new JPanel(new GridLayout(4,3,1,1));
		nextPieceGridPanel.setPreferredSize(new Dimension(80,80));
		topLabelNextP.setText("<html><body>Next piece: </body></html>");

		nextPieceGrid = new Tile[4][3];
		for(int i = 0; i<4; i++){
			for(int j = 0; j<3; j++){
				nextPieceGrid[i][j]= new Tile();
				nextPieceGrid[i][j].setOpaque(true);
				nextPieceGridPanel.add(nextPieceGrid[i][j]);
			}
		}

		nextPiecePanel.add(topLabelNextP);
		nextPiecePanel.add(nextPieceGridPanel);
		//MIDPANEL
		JPanel midScorePanel = new JPanel();
		JLabel nbrLinesLabel = new JLabel();
		nbrLinesLabel.setText("<html><body>Number of lines cleared:</body></html>");
		nbrLinesLabel.setPreferredSize(new Dimension(60,200));
		scoreLabel = new JLabel();
		midScorePanel.add(nbrLinesLabel);
		midScorePanel.add(scoreLabel);
		//BOTPANEL
		JPanel botScorePanel = new JPanel();
		JLabel levelLabel = new JLabel();
		currentLvl = new JLabel();
		currentLvl.setText("1");
		levelLabel.setPreferredSize(new Dimension(60,200));
		levelLabel.setText("<html><body>LEVEL</body></html>");
		botScorePanel.add(levelLabel);
		botScorePanel.add(currentLvl);
		//ADD IN SCOREPANEL
		scorePanel.setBorder(new EmptyBorder(10,10,10,10));
		scorePanel.add(nextPiecePanel);
		scorePanel.add(midScorePanel);
		scorePanel.add(botScorePanel);

		//GAMEPANEL
		JPanel gamePanel = new JPanel(new GridLayout(22,10,1,1));
		gamePanel.setFocusable(true);
		gamePanel.addKeyListener(this);
		gamePanel.setPreferredSize(new Dimension(380,650));

		//ADD PANELS
		frame.getContentPane().add(scorePanel, BorderLayout.EAST);
		frame.getContentPane().add(gamePanel, BorderLayout.CENTER);


		gameGrid = new Tile[22][10];
		for(int i = 0; i<22; i++){
			for(int j = 0; j<10; j++){
				gameGrid[i][j]= new Tile();
				gameGrid[i][j].setOpaque(true);
				gamePanel.add(gameGrid[i][j]);
			}
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.validate();
		frame.repaint();
		frame.pack();
		frame.setVisible(true);
	}


	/* 
	 * Updates the GUI depending on what kind of Update enum sent from the model
	 * 
	 * This is to avoid having to repaint the entire grid when active is falling and so on
	 * 
	 */
	@Override
	public void update(Observable obs, Object obj) {
		if(obj instanceof Update){
			if(obj.equals(Update.GAMEOVER)){
				JOptionPane.showMessageDialog(frame, "GAME OVER");
				System.out.println("GAME OVER GAME OVER GAME OVER GAME OVER");
			}
			if(obj.equals(Update.NEXT)){
				paintNewNextPiece();
			}
			if(obj.equals(Update.STATIC)){
				oldCoordinatesActive.clear();
				rePaintStatic();
				paintNewNextPiece();
			}
			if(obj.equals(Update.ACTIVE)){
				removeOldActive(); 
				int[][] activeCoords = board.getActive();
				Color activeColor = board.getCurrentTetroColor();
				int x = board.getCurrentX();
				int y = board.getCurrentY()-(activeCoords.length-1); //f�r att hamna i top v�nster ist f�r top h�ger
				for(int i = 0; i<activeCoords.length; i++){
					for(int j = 0; j<activeCoords[0].length; j++){
						if(activeCoords[i][j] == 1){
							gameGrid[y+i][x+j].setShapeColor(activeColor);
							oldCoordinatesActive.add(new Point(y+i,x+j));
						}
					}
				}		
			}
			if(obj.equals(Update.LEVELUP)){
				currentLvl.setText(""+board.getCurrentLevel());
			}
		}
		scoreLabel.setText(Integer.toString(board.getClearedRows()));
		frame.repaint();
	}

	/**
	 * Removes the old painting of the active tetrominoe
	 */
	public void removeOldActive(){
		for(int i = 0; i<oldCoordinatesActive.size(); i++){
			int y =oldCoordinatesActive.get(i).x;
			int x = oldCoordinatesActive.get(i).y;
			gameGrid[y][x].setShapeColor(Color.GRAY);
		}
		oldCoordinatesActive.clear();
	}

	/**
	 * Paints the new piece generated from the model
	 */
	public void paintNewNextPiece(){
		for(int i = 0; i<nextPieceGrid.length; i++){
			for(int j = 0; j<nextPieceGrid[0].length; j++){
				nextPieceGrid[i][j].setShapeColor(Color.GRAY);
			}
		}

		int[][] next = board.getNextPieceCoords();
		for(int i = 0; i<next.length; i++){
			for(int j = 0; j<next[0].length; j++){
				if(next[i][j] == 1){
					nextPieceGrid[i][j].setShapeColor(board.getNextPieceColor());
				}
			}
		}
	}

	public void getBoard(Board board){
		this.board=board;
	}

	/**
	 * Repaints the whole static grid
	 */
	public void rePaintStatic(){
		GridTile[][] grid = board.getGrid();

		for(int i = 0; i<hBOARD; i++){
			for(int j = 0; j<wBOARD; j++){	
				if(grid[i][j].getBool() == true){
					gameGrid[i][j].setShapeColor(grid[i][j].getColor());
				}else{
					gameGrid[i][j].setShapeColor(Color.GRAY); 
				}
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_LEFT:
			board.stepLeft();
			break;
		case KeyEvent.VK_RIGHT:
			board.stepRight();
			break;
		case KeyEvent.VK_UP:
			board.rotateRight();
			break;
		case KeyEvent.VK_DOWN:
			board.rotateLeft();
			break;
		case KeyEvent.VK_SPACE:
			board.activateFastDrop();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e1) {
		int keycode = e1.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_SPACE:
			board.deActivateFastDrop();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
}
