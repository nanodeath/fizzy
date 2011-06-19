package org.newdawn.fizzy;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/**
 * A primitive shape handling adding to the world and configuration of physics 
 * properties.
 * 
 * @author kevin
 */
public abstract class PrimitiveShape implements Shape {
	/** The array list of bodies that contains this shape */
	protected List<org.jbox2d.collision.shapes.Shape> jbox2DShapes = new ArrayList<org.jbox2d.collision.shapes.Shape>();
	/** The primitive shape that represent this fizzy fixture */
	protected Fixture jbox2DFixture;
	/** The definition of the core shape */
	protected FixtureDef def;
	/** The body this shape is being used in if any */
	protected Body<?> body;
	
	/**
	 * The body this shape is being used in if any
	 * 
	 * @return The body this shape is being used in if any
	 */
	public Body<?> getBody() {
		return body;
	}
	
	/**
	 * Create a new primitive shape
	 */
	protected PrimitiveShape() {
		this.def = new FixtureDef();
	}
	
	@Override
	public void createInBody(Body<?> body) {
		this.body = body;
		jbox2DFixture = body.getJBoxBody().createFixture(def);
		jbox2DShapes.add(jbox2DFixture.getShape());
	}

	@Override
	public List<org.jbox2d.collision.shapes.Shape> getJBoxShapes() {
		return jbox2DShapes;
	}

	@Override
	public void setDensity(float density) {
		if (jbox2DFixture == null) {
			def.density = density;
		} else {
			jbox2DFixture.m_density = density;
		}
	}

	@Override
	public void setFriction(float friction) {
		if (jbox2DFixture == null) {
			def.friction = friction;
		} else {
			jbox2DFixture.setFriction(friction);
		}
	}

	@Override
	public void setRestitution(float rest) {
		if (jbox2DFixture == null) {
			def.restitution = rest;
		} else {
			jbox2DFixture.setRestitution(rest);
		}
	}

}
