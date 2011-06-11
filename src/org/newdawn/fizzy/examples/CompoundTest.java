package org.newdawn.fizzy.examples;

import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.CompoundShape;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Polygon;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;
import org.newdawn.fizzy.World.OutOfBoundsBehavior;

/**
 * Simple test for the debug rendering system
 * 
 * @author kevin
 */
public class CompoundTest extends AbstractTest {
	/**
	 * Create a simulation and run it through the renderer
	 * 
	 * @param argv Arguments passed to the test
	 */
	public static void main(String[] argv) {
		CompoundTest test = new CompoundTest();
		test.startInWindow();
	}

	@Override
	public World createWorld() {
		World world = new World();
		world.setBounds(800, 800);
		
		CompoundShape shape = new CompoundShape();
		shape.add(new Rectangle(20,10).setOffset(-5, 0, 0));
		shape.add(new Rectangle(10,10).setOffset(0, 10, 0));
		
		Body<?> compound = new DynamicBody(shape, 0, 0.0f);
		compound.setRestitution(0.1f);
		world.add(compound);
		compound.setRotation(0.5f);
		
		shape = new CompoundShape();
		shape.add(new Rectangle(10,10));
		shape.add(new Rectangle(10,10).setOffset(-10, 0, 0));
		shape.add(new Rectangle(10,10).setOffset(0, 10, 0));
		
		// get your winding right or get inverted shapes
		Polygon spike = new Polygon();
		Vec2[] points = new Vec2[]{
				new Vec2(10, 15),
				new Vec2(0, 30),
				new Vec2(-10, 15)
		};
		spike.setPoints(points);
		shape.add(spike);
		
		compound = new DynamicBody(shape, -10.0f, 80.0f);
		compound.setRestitution(0.1f);
		world.add(compound);
		compound.setRotation(0.5f);
		
		Body<?> floor = new StaticBody(new Rectangle(200.0f, 10.0f), 0, -50.0f);
		floor.setRestitution(0.1f);
		world.add(floor);
		floor = new StaticBody(new Rectangle(10.0f, 100.0f), -105.0f, 0);
		floor.setRestitution(0.1f);
		world.add(floor);
		floor = new StaticBody(new Rectangle(10.0f, 100.0f), 105.0f, 0);
		floor.setRestitution(0.1f);
		world.add(floor);
		
		return world;
	}
}
