package org.newdawn.fizzy;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

public class World {
	public static final float DEFAULT_GRAVITY = -10;
	public static final float DEFAULT_LEFT_BOUND = -200;
	public static final float DEFAULT_RIGHT_BOUND = 200;
	public static final float DEFAULT_TOP_BOUND = -200;
	public static final float DEFAULT_BOTTOM_BOUND = 200;
	public static final float DEFAULT_ITERATIONS = 10;
	
	private org.jbox2d.dynamics.World jboxWorld;
	private ArrayList<Body> bodies = new ArrayList<Body>();
	private HashMap<org.jbox2d.collision.Shape, Body> shapeMap = new HashMap<org.jbox2d.collision.Shape, Body>();
	private ArrayList<WorldListener> listeners = new ArrayList<WorldListener>();
	
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
		jboxWorld.setContactListener(new ProxyContactListener());
	}
	
	org.jbox2d.dynamics.World getJBoxWorld() {
		return jboxWorld;
	}
	
	public void add(Body body) {
		body.addToWorld(this);
		shapeMap.put(body.getJBoxShape(), body);
		bodies.add(body);
	}
	
	public void remove(Body body) {
		shapeMap.remove(body.getJBoxShape());
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
	
	public void addListener(WorldListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(WorldListener listener) {
		listeners.remove(listener);
	}
	
	private void fireCollision(Body bodyA, Body bodyB) {
		CollisionEvent event = new CollisionEvent(bodyA, bodyB);
		for (int i=0;i<listeners.size();i++) {
			listeners.get(i).collided(event);
		}
	}
	
	private void fireSeparated(Body bodyA, Body bodyB) {
		CollisionEvent event = new CollisionEvent(bodyA, bodyB);
		for (int i=0;i<listeners.size();i++) {
			listeners.get(i).separated(event);
		}	
	}
	
	private class ProxyContactListener implements ContactListener {

		@Override
		public void add(ContactPoint point) {
			Body bodyA = shapeMap.get(point.shape1);
			Body bodyB = shapeMap.get(point.shape2);
			
			if ((bodyA != null) && (bodyB != null)) {
				bodyA.touch(bodyB);
				bodyB.touch(bodyA);
				
				if (bodyA.touchCount(bodyA) == 1) {
					fireCollision(bodyA, bodyB);
				}
			}
		}

		@Override
		public void persist(ContactPoint point) {
		}

		@Override
		public void remove(ContactPoint point) {
			Body bodyA = shapeMap.get(point.shape1);
			Body bodyB = shapeMap.get(point.shape2);
			
			if ((bodyA != null) && (bodyB != null)) {
				bodyA.untouch(bodyB);
				bodyB.untouch(bodyA);
				
				if (bodyA.touchCount(bodyA) == 0) {
					fireSeparated(bodyA, bodyB);
				}
			}
		}

		@Override
		public void result(ContactResult point) {
		}
		
	}
}
