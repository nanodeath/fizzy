package org.newdawn.fizzy;

import java.util.ArrayList;

import org.jbox2d.collision.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

/**
 * A single body in the world. A body holds a shape which collide with
 * the rest of the world. It also holds properties about the shapes once
 * they have been created
 * 
 * @author kglass
 */
public class Body {
	/** The shape held by JBox2D */
	private org.jbox2d.collision.Shape jboxShape;
	/** The definition of the shape held by JBox2D */
	private ShapeDef jboxShapeDef;	
	/** The body held by JBox2D */
	private org.jbox2d.dynamics.Body jboxBody;
	/** The body definition held by JBox2D */
	private BodyDef jboxBodyDef;
	/** True if this should be a static body */
	private boolean staticBody;
	/** True if the body is added to the world */
	private boolean addedToWorld;
	/** The list of bodies this body is touching */
	private ArrayList<Body> touching = new ArrayList<Body>();
	
	public Body(Shape shape, float x, float y) {
		this(shape,x,y,false);
	}
	
	public Body(Shape shape, float x, float y, boolean staticBody) {
		jboxBodyDef = new BodyDef();
		jboxBodyDef.position = new Vec2(x,y);
		jboxShapeDef = shape.getJBoxShape();
		this.staticBody = staticBody;
	}
	
	public boolean isTouching(Body other) {
		return touching.contains(other);
	}
	
	public int touchCount(Body other) {
		int count = 0;
		
		for (int i=0;i<touching.size();i++) {
			if (touching.get(i) == other) {
				count++;
			}
		}
		
		return count;
	}
	
	void touch(Body other) {
		touching.add(other);
	}
	
	void untouch(Body other) {
		touching.remove(other);
	}
	
	public float getX() {
		return jboxBody.getPosition().x;		
	}
	
	public float getY() {
		return jboxBody.getPosition().y;		
	}
	
	public void setRestitution(float rest) {
		if (!addedToWorld) {
			jboxShapeDef.restitution = rest;
		} else {
			jboxShape.m_restitution = rest;
		}
	}

	public void setFriction(float f) {
		if (!addedToWorld) {
			jboxShapeDef.friction = f;
		} else {
			jboxShape.m_friction = f;
		}
	}
	
	public void setDensity(float den) {
		if (!addedToWorld) {
			jboxShapeDef.density = den;
		} else {
			jboxShape.m_density = den;
		}
	}
	
	void addToWorld(World world) {
		addedToWorld = true;
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
				
		jboxBody = jboxWorld.createBody(jboxBodyDef);
		jboxShape = jboxBody.createShape(jboxShapeDef);
		
		if (!staticBody) {
			jboxBody.setMassFromShapes();
		}
	}
	
	void removeFromWorld(World world) {
		addedToWorld = false;
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
		jboxBody.destroyShape(jboxShape);
		jboxWorld.destroyBody(jboxBody);
	}

	public org.jbox2d.collision.Shape getJBoxShape() {
		return jboxShape;
	}
}
