import java.awt.Color;
/*
 * NoShape(new int[][]{
			{0,0,0}, 
			{0,0,0}, 
			{0,0,0}
	}, new Color(0,0,0)),
	ZShape(new int[][]{
			{0,0,0},
			{1,1,0},
			{0,1,1}
	}, new Color(255,0,0)),
	SShape(new int[][]{
			{0,0,0},
			{0,1,1},
			{1,1,0}
	}, new Color(0,255,0)),
	LineShape(new int[][]{
			{0,0,0,0},
			{0,0,0,0},
			{1,1,1,1},
			{0,0,0,0}
	}, new Color(0,255,255)),
	TShape(new int[][]{
			{0,0,0},
			{0,1,0},
			{1,1,1}
	}, new Color(150,0,255)),
	SquareShape(new int[][]{
			{1,1},
			{1,1}
	}, new Color(255,255,0)),
	LShape(new int[][]{
			{1,0,0},
			{1,0,0},
			{1,1,0}
	}, new Color(0,0,255)),
	MirroredLShape(new int[][]{
			{0,0,1},
			{0,0,1},
			{0,1,1}
	}, new Color(255,150,0));
 */


public enum Tetrominoes {
//	NoShape(new int[][]{
//			{0,0,0}, 
//			{0,0,0}, 
//			{0,0,0}
//	}, new Color(0,0,0)),
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
