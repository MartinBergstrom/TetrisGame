
/**
 * Simple start-up class to connect the model to the view 
 * 
 * @author Martin Bergström
 *
 */
public class StartUp {
	
	public static void main(String[] args){
		Board model = new Board();
		TetrisMain view = new TetrisMain();
		view.getBoard(model);

		model.addObserver(view);
	}
}
