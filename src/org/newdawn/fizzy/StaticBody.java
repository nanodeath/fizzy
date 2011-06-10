package org.newdawn.fizzy;

import org.jbox2d.dynamics.BodyType;

public class StaticBody<T> extends Body<T> {

	public StaticBody(Shape shape, float x, float y) {
		super(shape, x, y);
		jboxBodyDef.type = BodyType.STATIC;
	}

	@Override
	public boolean isStatic() {
		return true;
	}
}
