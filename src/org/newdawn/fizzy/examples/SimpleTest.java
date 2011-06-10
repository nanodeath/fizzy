package org.newdawn.fizzy.examples;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;
import org.newdawn.fizzy.WorldListener;

/** 
 * A simple sanity test. Circle falls, hits rectangle, collision events are fired
 * 
 * @author kevin
 */
public class SimpleTest {
	/**
	 * Set up the test and run the simulation
	 * 
	 * @param argv The arguments passed to the program
	 */
	public static void main(String[] argv) {
		World world = new World();
		
		Body body = new DynamicBody(new Circle(10.0f), 0, 0);
		world.add(body);
		Body floor = new StaticBody(new Rectangle(200.0f, 10.0f), 0, -50.0f);
		world.add(floor);
		
		world.addListener(new WorldListener() {
			@Override
			public void collided(CollisionEvent event) {
				System.out.println("Collision");
			}

			@Override
			public void separated(CollisionEvent event) {
				System.out.println("Separate");
			}
			
		});
		for (int i=0;i<4000;i++) {
			world.update(0.01f);
		}
	}
}
