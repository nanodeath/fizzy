package org.newdawn.fizzy;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;

/**
 * A shape implementation defining a rectangle for collision 
 * 
 * @author kevin
 */
public class Rectangle extends PrimitiveShape<PolygonDef> {
	/** The width of the rectangle */
	private float width;
	/** The height of the rectangle */
	private float height;
	/** The horinzontal axis offset */
	private float xoffset;
	/** The vertical axis offset */
	private float yoffset;
	/** The angle offset */
	private float angleOffset;
	
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
		super(new PolygonDef());
		
		def.setAsBox(width / 2, height / 2);
		def.density = density;
		def.restitution = restitution;
		def.friction = friction;
		
		this.width = width;
		this.height = height;
	}

	/**
	 * Move the rectangle away from the center of it's potential body. The rectangle will still 
	 * be positioned based on it's center but will be offset from the body's center by the given 
	 * value, and rotated by the angle given.
	 * 
	 * @param x The horizontal axis offset
	 * @param y The vertical axis offset
	 * @param angle
	 */
	public void setOffset(float x, float y, float angle) {
		xoffset = x;
		yoffset = y;
		angleOffset = angle;
		def.setAsBox(width / 2, height / 2, new Vec2(x,y), angle);
	}
	
	/**
	 * Get the horizontal axis offset from the body's center
	 * 
	 * @return The horizontal axis offset
	 */
	public float getXOffset() {
		return xoffset;
	}

	/**
	 * Get the vertical axis offset from the body's center
	 * 
	 * @return The horizontal axis offset
	 */
	public float getYOffset() {
		return yoffset;
	}

	/**
	 * Get the angle offset from the body's angle
	 * 
	 * @return The angle offset
	 */
	public float getAngleOffset() {
		return angleOffset;
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
