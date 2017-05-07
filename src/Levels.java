import java.util.HashMap;

import javax.swing.Timer;

/**
 * Class to bind levels to drop speed
 * 
 * DEFAULT SPEED (index = 1) - 400ms
 * Level number: 2 has speed of : 363
 * Level number: 3 has speed of : 326
 * Level number: 4 has speed of : 289
 * Level number: 5 has speed of : 252
 * Level number: 6 has speed of : 215
 * Level number: 7 has speed of : 178
 * Level number: 8 has speed of : 141
 * Level number: 9 has speed of : 104
 * Level number: 10 has speed of : 67
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