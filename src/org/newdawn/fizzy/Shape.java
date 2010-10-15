package org.newdawn.fizzy;

public interface Shape {
	public static final float DEFAULT_DENSITY = 25.0f;
	public static final float DEFAULT_RESTIUTION = 0.9f;
	public static final float DEFAULT_FRICTION = 0.1f;
	
	org.jbox2d.collision.ShapeDef getJBoxShape();
}
