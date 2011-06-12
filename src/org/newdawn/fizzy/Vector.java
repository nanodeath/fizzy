package org.newdawn.fizzy;

import org.jbox2d.common.Vec2;

public class Vector {
	public final float x, y;
	public Vector(final float x, final float y){
		this.x = x;
		this.y = y;
	}

	public static Vector fromVec2(final Vec2 vec2){
		return new Vector(vec2.x, vec2.y);
	}
}
