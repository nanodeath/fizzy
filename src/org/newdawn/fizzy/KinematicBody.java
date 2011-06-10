package org.newdawn.fizzy;

import org.jbox2d.dynamics.BodyType;

public class KinematicBody<T> extends Body<T> {

	public KinematicBody(Shape shape, float x, float y) {
		super(shape, x, y);
		jboxBodyDef.type = BodyType.KINEMATIC;
	}
}
