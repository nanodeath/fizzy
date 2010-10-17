package org.newdawn.fizzy;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.ShapeDef;

/**
 * A shape implementation defining a rectangle for collision 
 * 
 * @author kevin
 */
public class Rectangle implements Shape {
	/** JBox2D's representation of the rectangle */
	private PolygonDef def;
	/** The width of the rectangle */
	private float width;
	/** The height of the rectangle */
	private float height;
	
	/**
	 * Create a new rectangle shape
	 * 
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public Rectangle(float width, float height) {
		this(width, height, DEFAULT_DENSITY, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	/**
	 * Create a new rectangle shape
	 * 
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param density The density of the rectangle
	 */
	public Rectangle(float width, float height, float density) {
		this(width, height, density, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	/**
	 * Create a new rectangle shape
	 * 
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param density The density of the rectangle
	 * @param resitution The resitution of the rectangle
	 */
	public Rectangle(float width, float height, float density, float resitution) {
		this(width, height, density, resitution, DEFAULT_FRICTION);
	}

	/**
	 * Create a new rectangle shape
	 * 
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param density The density of the rectangle
	 * @param restitution The resitution of the rectangle
	 * @param friction The friction of the rectangle
	 */
	public Rectangle(float width, float height, float density, float restitution, float friction) {
		def = new PolygonDef();
		def.setAsBox(width / 2, height / 2);
		def.density = density;
		def.restitution = restitution;
		def.friction = friction;
		
		this.width = width;
		this.height = height;
	}

	@Override
	public ShapeDef getJBoxShape() {
		return def;
	}
	
	/**
	 * Get the width of the rectangle
	 * 
	 * @return The width of the rectangle
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Get the height of the rectangle
	 * 
	 * @return The height of the rectangle
	 */
	public float getHeight() {
		return height;
	}
}
