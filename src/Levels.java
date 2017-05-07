import java.util.HashMap;

import javax.swing.Timer;

/**
 * Class to bind levels to drop speed
 * 
 * DEFAULT SPEED (index = 1) - 400ms
 * 
 * @author Martin
 *
 */
public class Levels {
	private HashMap<Integer,Integer> map; 
	private static final int maxLevels = 10;
	private static int currentLevel = 1;
	private Timer timer;

	public Levels(Timer timer){
		this.timer = timer;
		map = new HashMap<>();
		setUp();
		System.out.println(map.get(10));
	}
	
	private void setUp(){
		int speed = 400; //400ms
		map.put(1, speed);
		for(int i = 2; i<=maxLevels; i++){ //10 levels?
			speed-=37;
			map.put(i, speed);
		}
	}
	
	public void levelUp(){
		if(currentLevel != maxLevels){
			currentLevel++;
			timer.setDelay(map.get(currentLevel));
		}
	}
	
	public void restoreLevels(){
		currentLevel = 1;
		timer.setDelay(map.get(currentLevel));
	}
	
	public int getCurrentLevel(){
		return currentLevel;
	}
}