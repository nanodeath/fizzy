package org.newdawn.fizzy.examples;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.World;

public class SimpleTest {

	public static void main(String[] argv) {
		World world = new World();
		
		Body body = new Body(new Circle(10.0f), 0, 0);
		world.add(body);
		Body floor = new Body(new Rectangle(200.0f, 10.0f), 0, -50.0f, true);
		world.add(floor);
		
		for (int i=0;i<1000;i++) {
			world.update(0.01f);
			System.out.println(body.getX()+","+body.getY());
		}
	}
}
