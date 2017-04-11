import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Tile extends JPanel{
	private Color color = Color.GRAY;
	private Tetrominoes tetros;

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(color);
		g.fillRect(0,0,100,100);
	}
	
	public void setShapeColor(Color c){
		this.color = c;	
	}
	
	public void setTetro(Tetrominoes t){
		this.tetros=t;
	}
	
	public Tetrominoes returnTetro(){
		return tetros;
	}
}
