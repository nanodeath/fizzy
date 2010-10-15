package org.newdawn.fizzy;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;

public class World {
	public static final float DEFAULT_GRAVITY = -10;
	public static final float DEFAULT_LEFT_BOUND = -200;
	public static final float DEFAULT_RIGHT_BOUND = 200;
	public static final float DEFAULT_TOP_BOUND = -200;
	public static final float DEFAULT_BOTTOM_BOUND = 200;
	public static final float DEFAULT_ITERATIONS = 10;
	
	private org.jbox2d.dynamics.World jboxWorld;
	private ArrayList<Body> bodies = new ArrayList<Body>();
	private int iterations;
	
	public World(int iterations) {
		this(-DEFAULT_LEFT_BOUND,DEFAULT_TOP_BOUND,DEFAULT_RIGHT_BOUND,DEFAULT_BOTTOM_BOUND
				 ,DEFAULT_GRAVITY, iterations);
	}
	
	public World() {
		this(-DEFAULT_LEFT_BOUND,DEFAULT_TOP_BOUND,DEFAULT_RIGHT_BOUND,DEFAULT_BOTTOM_BOUND
			 ,DEFAULT_GRAVITY, DEFAULT_ITERATIONS);
	}
	
	public World(float x1, float y1, float x2, float y2, float g, float iterations) {
		this.iterations = 10;
		
		AABB m_worldAABB = new AABB();
		m_worldAABB.lowerBound = new Vec2(-200.0f, -100.0f);
		m_worldAABB.upperBound = new Vec2(200.0f, 200.0f);
		Vec2 gravity = new Vec2(0.0f, g);
		boolean doSleep = true;
		jboxWorld = new org.jbox2d.dynamics.World(m_worldAABB, gravity, doSleep);
	}
	
	org.jbox2d.dynamics.World getJBoxWorld() {
		return jboxWorld;
	}
	
	public void add(Body body) {
		body.addToWorld(this);
		bodies.add(body);
	}
	
	public void remove(Body body) {
		body.removeFromWorld(this);
		bodies.remove(body);
	}
	
	public int getBodyCount() {
		return bodies.size();
	}
	
	public Body getBody(int index) {
		return bodies.get(index);
	}
	
	public void update(float timeStep) {
		jboxWorld.step(timeStep, iterations);
	}
}
