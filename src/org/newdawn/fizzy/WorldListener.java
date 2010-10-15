package org.newdawn.fizzy;

public interface WorldListener {

	public void collided(CollisionEvent event);
	
	public void separated(CollisionEvent event);
}
