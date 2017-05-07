import java.awt.Color;

/**
 * Class to represent a single tetrominoe, 
 * the matrix represents the shape of the tetro
 * 
 * @author Martin Bergström
 *
 */
public enum Tetrominoes {
	ZShape(new int[][]{
			{1,1,0},
			{0,1,1}
	}, new Color(200,0,0)),
	SShape(new int[][]{
			{0,1,1},
			{1,1,0}
	}, new Color(0,200,0)),
	LineShape(new int[][]{
			{1},
			{1},
			{1},
			{1}
	}, new Color(0,200,200)),
	TShape(new int[][]{
			{0,1,0},
			{1,1,1}
	}, new Color(100,0,225)),
	SquareShape(new int[][]{
			{1,1},
			{1,1}
	}, new Color(225,225,0)),
	LShape(new int[][]{
			{1,0},
			{1,0},
			{1,1}
	}, new Color(0,0,200)),
	MirroredLShape(new int[][]{
			{0,1},
			{0,1},
			{1,1}
	}, new Color(255,150,0));

private final int[][] figures;
private Color color;

private Tetrominoes(int[][] figure, Color c){
	this.figures=figure;
	this.color= c;
}

public int[][] getcoords(){
	return figures;
}

public Color getColor(){
	return color;
}

}
