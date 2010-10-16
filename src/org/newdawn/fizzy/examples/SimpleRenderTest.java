package org.newdawn.fizzy.examples;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.World;
import org.newdawn.fizzy.WorldListener;
import org.newdawn.fizzy.render.WorldWindow;

public class SimpleRenderTest {

	public static void main(String[] argv) {
		World world = new World(800,800);
		
		for (int i=0;i<5;i++) {
			Body body = new Body(new Circle(10.0f), i, 20*i);
			world.add(body);
		}
		Body floor = new Body(new Rectangle(200.0f, 10.0f), 0, -50.0f, true);
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
		
		WorldWindow window = new WorldWindow(world);
		window.start();
	}
}
