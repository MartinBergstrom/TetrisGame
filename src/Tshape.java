import java.awt.Color;
import java.util.Random;


/**
 * Class to generate tetrominoes and also rotate them.
 * 
 * The rotation is based on linear algebra where you can accomplish a rotation by trasposing the matrix 
 * and then reverse either the rows to rotate right or the columns to rotate left
 * 
 * @author Martin Bergstr�m
 *
 */
public class Tshape {
	private Tetrominoes tetro;
	private int[][] shapeCoords;

	public void randomize(){
		tetro= Tetrominoes.values()[new Random().nextInt(Tetrominoes.values().length)];

		shapeCoords=tetro.getcoords();
	}

	
	public static int[][] trasposeMatrix(int[][] matrix)
	{
	    int m = matrix.length;
	    int n = matrix[0].length;

	    int[][] trasposedMatrix = new int[n][m];

	    for(int x = 0; x < n; x++)
	    {
	        for(int y = 0; y < m; y++)
	        {
	            trasposedMatrix[x][y] = matrix[y][x];
	        }
	    }

	    return trasposedMatrix;
	}
	
	public void rotateLeft(){
		if(tetro!=Tetrominoes.SquareShape){
			shapeCoords= trasposeMatrix(shapeCoords);
			reverseColumnsInPlace(shapeCoords);
		}
	}
	
	public void rotateRight(){
		if(tetro!=Tetrominoes.SquareShape){
			shapeCoords = trasposeMatrix(shapeCoords);
			reverseRowsInPlace(shapeCoords);   	
		}
	}
	
	public static void reverseColumnsInPlace(int[][] matrix){
        for(int col = 0;col < matrix[0].length; col++){
            for(int row = 0; row < matrix.length/2; row++) {
                int temp = matrix[row][col];
                matrix[row][col] = matrix[matrix.length - row - 1][col];
                matrix[matrix.length - row - 1][col] = temp;
            }
    }
}
	public static void reverseRowsInPlace(int[][] matrix){
	    for(int row = 0; row < matrix.length; row++){
	        for(int col = 0; col < matrix[row].length / 2; col++) {
	            int temp = matrix[row][col];
	            matrix[row][col] = matrix[row][matrix[row].length - col - 1];
	            matrix[row][matrix[row].length - col - 1] = temp;
	        }
	    }
	}

	public int[][] getCoords(){
		return shapeCoords;
	}

	public Color getColor() {
		return tetro.getColor();
	}
	
	public String toString(){
		return tetro.name();
	}	
}
