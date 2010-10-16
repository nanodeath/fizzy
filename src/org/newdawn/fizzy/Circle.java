package org.newdawn.fizzy;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.ShapeDef;

public class Circle implements Shape {
	private CircleDef def;
	
	public Circle(float radius) {
		this(radius, DEFAULT_DENSITY, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	public Circle(float radius, float density) {
		this(radius, density, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	public Circle(float radius, float densitiy, float resitution) {
		this(radius, densitiy, resitution, DEFAULT_FRICTION);
	}
	
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
	
	public float getRadius() {
		return def.radius;
	}
}
