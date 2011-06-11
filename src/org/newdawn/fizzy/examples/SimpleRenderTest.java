package org.newdawn.fizzy.examples;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;
import org.newdawn.fizzy.World.OutOfBoundsBehavior;

/**
 * Simple test for the debug rendering system
 * 
 * @author kevin
 */
public class SimpleRenderTest extends AbstractTest {
	/**
	 * Create a simulation and run it through the renderer
	 * 
	 * @param argv Arguments passed to the test
	 */
	public static void main(String[] argv) {
		SimpleRenderTest test = new SimpleRenderTest();
		test.startInWindow();
	}

	@Override
	public World createWorld() {
		World world = new World();
		world.setBounds(800, 800);
		
		for (int i=0;i<5;i++) {
			Body<?> body = new DynamicBody(new Circle(10.0f), i, 20*i);
			world.add(body);
		}
		Body<?> floor = new StaticBody(new Rectangle(200.0f, 10.0f), 0, -50.0f);
		world.add(floor);
		
		return world;
	}
}
