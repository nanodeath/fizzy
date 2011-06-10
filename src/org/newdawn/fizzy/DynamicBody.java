package org.newdawn.fizzy;

import org.jbox2d.dynamics.BodyType;

public class DynamicBody<T> extends Body<T> {

	public DynamicBody(Shape shape, float x, float y) {
		super(shape, x, y);
		jboxBodyDef.type = BodyType.DYNAMIC;
	}
}
