package org.newdawn.fizzy;

import java.util.ArrayList;
import java.util.TreeSet;

import org.jbox2d.collision.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

public class Body {
	private org.jbox2d.collision.Shape jboxShape;
	private ShapeDef jboxShapeDef;	
	private org.jbox2d.dynamics.Body jboxBody;
	private BodyDef jboxBodyDef;
	private boolean staticBody;
	private boolean addedToWorld;
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
