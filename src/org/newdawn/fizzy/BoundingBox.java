package org.newdawn.fizzy;

import static org.newdawn.fizzy.Vector.fromVec2;

import org.jbox2d.collision.AABB;

/**
 * Axis-aligned bounding box, mirroring JBox's AABB.
 */
public class BoundingBox {
	public final Vector lowerLeft;
	public final Vector upperRight;

	public BoundingBox(final Vector lowerLeft, final Vector upperRight){
		this.lowerLeft = lowerLeft;
		this.upperRight = upperRight;
	}
	public static BoundingBox fromAABB(final AABB aabb){
		return new BoundingBox(fromVec2(aabb.lowerBound), fromVec2(aabb.upperBound));
	}
}
