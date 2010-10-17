package org.newdawn.fizzy;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.ShapeDef;

/** 
 * A shape defining a circle to be used for a collision. 
 * 
 * @author kevin
 */
public class Circle implements Shape {
	/** The box 2D definition of the circle */
	private CircleDef def;
	
	/**
	 * Create a new circle shape
	 * 
	 * @param radius The radius of the circle
	 */
	public Circle(float radius) {
		this(radius, DEFAULT_DENSITY, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	/**
	 * Create a new circle shape
	 * 
	 * @param radius The radius of the circle
	 * @param density The density of the circle
	 */
	public Circle(float radius, float density) {
		this(radius, density, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	/**
	 * Create a new circle shape
	 * 
	 * @param radius The radius of the circle
	 * @param density The density of the circle
	 * @param resitution The resitution of the circle
	 */
	public Circle(float radius, float density, float resitution) {
		this(radius, density, resitution, DEFAULT_FRICTION);
	}

	/**
	 * Create a new circle shape
	 * 
	 * @param radius The radius of the circle
	 * @param density The density of the circle
	 * @param restitution The resitution of the circle
	 * @param friction The friction applied to the circle
	 */
	public Circle(float radius, float density, float restitution, float friction) {
		def = new CircleDef();
		def.radius = radius;
		def.density = density;
		def.restitution = restitution;
		def.friction = friction;
	}

	@Override
	public ShapeDef getJBoxShape() {
		return def;
	}
	
	/**
	 * Get the radius of the circle 
	 * 
	 * @return The radius of the circle
	 */
	public float getRadius() {
		return def.radius;
	}
}
