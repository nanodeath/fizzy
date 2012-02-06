package org.newdawn.fizzy;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class Polygon extends PolygonBasedShape {
	private PolygonShape shape;
	/**
	 * Create a new polygon shape
	 */
	public Polygon() {
		this(DEFAULT_DENSITY, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	/**
	 * Create a new polygon shape
	 * 
	 * @param density
	 *            The density of the polygon
	 */
	public Polygon(float density) {
		this(density, DEFAULT_RESTIUTION, DEFAULT_FRICTION);
	}

	/**
	 * Create a new polygon shape
	 * 
	 * @param density
	 *            The density of the polygon
	 * @param resitution
	 *            The resitution of the polygon
	 */
	public Polygon(float density, float resitution) {
		this(density, resitution, DEFAULT_FRICTION);
	}

	/**
	 * Create a new polygon shape
	 * 
	 * @param density
	 *            The density of the polygon
	 * @param restitution
	 *            The resitution of the polygon
	 * @param friction
	 *            The friction of the polygon
	 */
	public Polygon(float density, float restitution, float friction) {
		super();
		def.shape = shape = new PolygonShape();
		def.density = density;
		def.restitution = restitution;
		def.friction = friction;
	}

	/**
	 * Set all the points on the polygon.
	 * @param points points in CCW order (as usual)
	 */
	public void setPoints(Vec2[] points){
		Vec2[] scaledPoints=new Vec2[points.length];
		for (int i=0;i<points.length;i++){
			scaledPoints[i]=points[i].mul(World.METERS_PER_PIXEL);
		}
		shape.set(scaledPoints, points.length);
	}

	/**
	 * Get the number of points in the shape
	 * 
	 * @return The number of points in the shape
	 */
	public int getPointCount() {
		return shape.getVertexCount();
	}

	/**
	 * Get the x coordinate of the point at the given index
	 * 
	 * @param i
	 *            The index of the point to retrieve
	 * @return The x coordinate of the point
	 */
	public float getPointX(int i) {
		return shape.getVertex(i).x*World.PIXELS_PER_METER;
	}

	/**
	 * Get the y coordinate of the point at the given index
	 * 
	 * @param i
	 *            The index of the point to retrieve
	 * @return The y coordinate of the point
	 */
	public float getPointY(int i) {
		return shape.getVertex(i).y*World.PIXELS_PER_METER;
	}

	/**
	 * Move the rectangle away from the center of it's potential body. The rectangle will still 
	 * be positioned based on it's center but will be offset from the body's center by the given 
	 * value, and rotated by the angle given.
	 * 
	 * @param x The horizontal axis offset
	 * @param y The vertical axis offset
	 * @param angle
	 * @return This rectangle for chaining operations
	 */
	public PolygonBasedShape setOffset(float x, float y, float angle) {
		applyOffset(-xoffset,-yoffset,-angleOffset);
		xoffset = x;
		yoffset = y;
		angleOffset = angle;
		applyOffset(x,y,angle);
		return this;
	}
	
	@Override
	protected void applyOffset(float x, float y, float angle) {
		final Transform xf = new Transform();
		xf.position.set(new Vec2(x, y).mul(World.METERS_PER_PIXEL));
		xf.R.set(angle);
		// Transform vertices and normals.
		for (int i = 0; i < shape.getVertexCount(); ++i) {
			Vec2 vertex = shape.getVertex(i);
			// TODO Max: not sure this is right -- none of the examples test it.  Probably?
			Transform.mulTransToOut(xf, vertex, vertex);
		}
	}
}
