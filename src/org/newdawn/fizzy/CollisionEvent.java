package org.newdawn.fizzy;

public class CollisionEvent {
	private Body bodyA;
	private Body bodyB;
	
	CollisionEvent(Body bodyA, Body bodyB) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
	}
	
	public Body getBodyA() {
		return bodyA;
	}

	public Body getBodyB() {
		return bodyB;
	}
}
