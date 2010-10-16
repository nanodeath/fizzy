package org.newdawn.fizzy;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.ShapeDef;

public class Rectangle implements Shape {
	private PolygonDef def;
	private float width;
	private float height;
	
	public Rectangle(float width, float height) {
		this(width, height, DEFAULT_DENSITY, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	public Rectangle(float width, float height, float density) {
		this(width, height, density, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	public Rectangle(float width, float height, float densitiy, float resitution) {
		this(width, height, densitiy, resitution, DEFAULT_FRICTION);
	}
	
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
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
