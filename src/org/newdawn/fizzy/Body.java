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
	/** The body held by JBox2D */
	private org.jbox2d.dynamics.Body jboxBody;
	/** The body definition held by JBox2D */
	private BodyDef jboxBodyDef;
	/** True if this should be a static body */
	private boolean staticBody;
	/** The list of bodies this body is touching */
	private ArrayList<Body> touching = new ArrayList<Body>();
	/** The shape used to represent this body */
	private Shape shape;
	
	/**
	 * Create a new body
	 * 
	 * @param shape The shape the body should have
	 * @param x The x axis location of the body
	 * @param y The y axis location of the body
	 */
	public Body(Shape shape, float x, float y) {
		this(shape,x,y,false);
	}

	/**
	 * Create a new body
	 * 
	 * @param shape The shape the body should have
	 * @param x The x axis location of the body
	 * @param y The y axis location of the body
	 * @param staticBody True if this body should be static
	 */
	public Body(Shape shape, float x, float y, boolean staticBody) {
		jboxBodyDef = new BodyDef();
		jboxBodyDef.position = new Vec2(x,y);
		this.staticBody = staticBody;
		this.shape = shape;
	}
	
	/**
	 * Check if this body is touching another
	 * 
	 * @param other The other body to check against 
	 * @return True if the bodies are touching
	 */
	public boolean isTouching(Body other) {
		return touching.contains(other);
	}
	
	/**
	 * Check how many contact points there are between two bodies
	 * 
	 * @param other The other body to check against
	 * @return The number of contact points
	 */
	public int touchCount(Body other) {
		int count = 0;
		
		for (int i=0;i<touching.size();i++) {
			if (touching.get(i) == other) {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Indicate that this body touches another at a *new* contact 
	 * point.
	 * 
	 * @param other The other body that is touched
	 */
	void touch(Body other) {
		touching.add(other);
	}
	
	/**
	 * Indicate that one contact point between the bodies has been removed
	 * 
	 * @param other The other body that is no longer touched by a particular 
	 * contact point.
	 */
	void untouch(Body other) {
		touching.remove(other);
	}
	
	public void applyForce(float x, float y) {
		jboxBody.applyForce(new Vec2(x,y), new Vec2(0,0));
	}
	
	public void setA(float v) {
		jboxBody.applyTorque(v);
	}
	
	/**
	 * Get the X position of the body
	 * 
	 * @return The x position of the body
	 */
	public float getX() {
		return jboxBody.getPosition().x;		
	}

	
	/**
	 * Get the Y position of the body
	 * 
	 * @return The y position of the body
	 */
	public float getY() {
		return jboxBody.getPosition().y;		
	}
	
	/**
	 * Get the rotation of the body
	 * 
	 * @return The rotation of the body
	 */
	public float getRotation() {
		return jboxBody.getAngle();
	}

	/**
	 * Get the X velocity of the body
	 * 
	 * @return The x velocity of the body
	 */
	public float getXVelocity() {
		return jboxBody.getLinearVelocity().x;		
	}

	
	/**
	 * Get the Y velocity of the body
	 * 
	 * @return The y velocity of the body
	 */
	public float getYVelocity() {
		return jboxBody.getLinearVelocity().y;		
	}
	
	/**
	 * Get the angular velocity of the body
	 * 
	 * @return The angular velocity of the body
	 */
	public float getAngularVelocity() {
		return jboxBody.getAngularVelocity();
	}
	
	/**
	 * Set the restitution applied when this body collides
	 * 
	 * @param rest The restitution applied when this body collides
	 */
	public void setRestitution(float rest) {
		shape.setRestitution(rest);
	}

	/**
	 * Set the friction applied when this body collides
	 * 
	 * @param f The friction applied when this body collides
	 */
	public void setFriction(float f) {
		shape.setFriction(f);
	}
	
	/**
	 * Set the density of this body
	 * 
	 * @param den The density of this body
	 */
	public void setDensity(float den) {
		shape.setDensity(den);
	}
	
	/**
	 * Notification that this body is being added to the world
	 * 
	 * @param world The world this body is being added to
	 */
	void addToWorld(World world) {
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
				
		jboxBody = jboxWorld.createBody(jboxBodyDef);
		shape.createInBody(this);
		
		if (!staticBody) {
			jboxBody.setMassFromShapes();
		}
	}

	/**
	 * Notification that this body is being removed from the world
	 * 
	 * @param world The world this body is being removed from
	 */
	void removeFromWorld(World world) {
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
		jboxWorld.destroyBody(jboxBody);
	}
	
	/**
	 * Get the JBox2D body that is wrapped by this class
	 * 
	 * @return The body that is wrapped by this proxy class
	 */
	org.jbox2d.dynamics.Body getJBoxBody() {
		return jboxBody;
	}
	
	/**
	 * Get the Fizzy shape representing this body
	 * 
	 * @return The fizzy shape representing this body
	 */
	public Shape getShape() {
		return shape;
	}
	
	public void setPosition(float x, float y) {
		jboxBody.setXForm(new Vec2(x,y), jboxBody.getAngle());
	}
	
	public void setRotation(float rotation) {
		jboxBody.setXForm(jboxBody.getPosition(), rotation);
	}
}
