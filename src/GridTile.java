import java.awt.Color;

/* Can I change this class to a more OO-approach?
 * 
 */
public class GridTile {
	private boolean bool;
	private Color color;
	
	public GridTile(){
		bool = false;
		color = Color.GRAY;
	}
	
	public void setTrue(){
		bool = true;
	}
	
	public void setFalse(){
		bool = false;
	}
	
	public boolean getBool(){
		return bool;
	}
	
	public void setColorGridTile(Color c){
		this.color = c;
	}
	
	public Color getColor(){
		return color;
	}

}
