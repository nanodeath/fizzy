package org.newdawn.fizzy;

/**
 * Decription of any class representing a shape for collision. The shape 
 * both defines the geometry and the collision properties. However, these
 * can be augmented at the Body level.
 * 
 * @author kevin
 */
public interface Shape {
	/** The default density applied to shapes if none is specified (25.0f) */
	public static final float DEFAULT_DENSITY = 25.0f;
	/** The default restitution applied to shapes if none is specified (0.9f) */
	public static final float DEFAULT_RESTIUTION = 0.9f;
	/** The default friction applied to shapes if none is specified (0.1f) */
	public static final float DEFAULT_FRICTION = 0.1f;
	
	/**
	 * Get the JBox2D Shape this Shape wraps
	 * 
	 * @return The JBox2D shape this shape wraps
	 */
	org.jbox2d.collision.ShapeDef getJBoxShape();
}
