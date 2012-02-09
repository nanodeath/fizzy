package org.newdawn.fizzy;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import static org.newdawn.fizzy.World.METERS_PER_PIXEL;
import static org.newdawn.fizzy.World.PIXELS_PER_METER;

/**
 * A single body in the world. A body holds a shape which collide with
 * the rest of the world. It also holds properties about the shapes once
 * they have been created.
 * 
 * The generic T represents the type of the userData.
 * 
 * @author kglass
 */
abstract public class Body<T> {
	/** The body held by JBox2D */
	protected org.jbox2d.dynamics.Body jboxBody;
	/** The body definition held by JBox2D */
	protected BodyDef jboxBodyDef;
	/** The list of bodies this body is touching */
	private List<Body<?>> touching = new ArrayList<Body<?>>();
	/** The shape used to represent this body */
	private Shape shape;
	/** The userdata assigned to this body if any */
	private T userData;
	/** Whether the body has been attached to a world */
	private boolean attached;
	
	/**
	 * Create a new body
	 * 
	 * @param shape The shape the body should have
	 * @param x The x axis location of the body
	 * @param y The y axis location of the body
	 */
	public Body(Shape shape, float x, float y) {
		jboxBodyDef = new BodyDef();
		jboxBodyDef.position = new Vec2(x*METERS_PER_PIXEL,y*METERS_PER_PIXEL);
		this.shape = shape;
	}
	
	/**
	 * Check if this body was declared as static
	 * 
	 * @return True if this body was declared as static
	 */
	public boolean isStatic() {
		return false;
	}
	
	/**
	 * Get the user data assigned to this body if any
	 * 
	 * @return Get the user data assigned to this body (or null if none is defined);
	 */
	public T getUserData() {
		return userData;
	}
	
	/**
	 * Set the user data assigned to this body
	 * 
	 * @param object The user data to be assigned to this body
	 */
	public void setUserData(T object) {
		this.userData = object;
	}
	
	/**
	 * Check if this body is touching another
	 * 
	 * @param other The other body to check against 
	 * @return True if the bodies are touching
	 */
	public boolean isTouching(Body<?> other) {
		return touching.contains(other);
	}
	
	/**
	 * Check how many contact points there are between two bodies
	 * 
	 * @param other The other body to check against
	 * @return The number of contact points
	 */
	public int touchCount(Body<?> other) {
		int count = 0;
		
		for(Body<?> touched : touching){
			if(touched == other){
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Indicate that this body touches another at a *new* contact 
	 * point.
	 * 
	 * @param other The other body that is touched
	 */
	void touch(Body<?> other) {
		touching.add(other);
	}
	
	/**
	 * Indicate that one contact point between the bodies has been removed
	 * 
	 * @param other The other body that is no longer touched by a particular 
	 * contact point.
	 */
	void untouch(Body<?> other) {
		touching.remove(other);
	}
	
	/**
	 * Reset all touch-related data -- disappear from other objects' touch information.
	 */
	void resetTouching(){
		for(Body<?> touched : touching){
			touched.untouch(this);
		}
		touching.clear();
	}
	
	/**
	 * Checks to see if this body is touching anything.
	 * @return true if something is touching
	 */
	public boolean isTouchingAnything(){
		return !touching.isEmpty();
	}
	
	/**
	 * Apply force to the body at its center.
	 * 
	 * @param x The amount of force on the X axis
	 * @param y The amount of force on the Y axis
	 */
	public void applyForce(float x, float y) {
		assertBodyAttached();
		jboxBody.applyForce(new Vec2(x,y), jboxBody.getWorldCenter());
	}
	
	/**
	 * Apple force to the body at an arbitrary location.
	 * 
	 * @param xMagnitude The amount of force on the X axis
	 * @param yMagnitude The amount of force on the Y axis
	 * @param xAt The x-coordinate at which to apply the force
	 * @param yAt The y-coordinate at which to apply the force
	 * @param isLocalPoint if true, assume the *At params are local to the body.  otherwise, they're world coordinates.
	 */
	public void applyForce(float xMagnitude, float yMagnitude, float xAt, float yAt, boolean isLocalPoint){
		assertBodyAttached();
		Vec2 position = new Vec2(xAt*METERS_PER_PIXEL, yAt*METERS_PER_PIXEL);
		if(isLocalPoint)
			position = jboxBody.getWorldPoint(position);
		jboxBody.applyForce(new Vec2(xMagnitude, yMagnitude), position);
	}

	/**
	 * (from JBox2d): Apply a torque. This affects the angular velocity without
	 * affecting the linear velocity of the center of mass. This wakes up the
	 * body.
	 * 
	 * @param torque about the z-axis (out of the screen), usually in N.
	 */
	public void applyTorque(float torque){
		assertBodyAttached();
		jboxBody.applyTorque(torque);
	}
	
	
	/**apply an impulse to the body at it's center*/
	public void applyImpulse(float xMagnitude, float yMagnitude){
		assertBodyAttached();
		jboxBody.applyLinearImpulse(new Vec2(xMagnitude,yMagnitude), jboxBody.getWorldCenter());
	}
	
	/**apply an impulse to the body at an arbitrary location 
	 * 
	 * @param xMagnitude impulse strength on the X axis
	 * @param yMagnitude impulse strength on the Y axis
	 * @param xAt The x-coordinate at which to apply the impulse
	 * @param yAt The y-coordinate at which to apply the impulse
	 * @param isLocalPoint if true, assume the *At params are local to the body.  otherwise, they're world coordinates.
	 */
	public void applyImpulse(float xMagnitude, float yMagnitude, float xAt, float yAt, boolean isLocalPoint){
		assertBodyAttached();
		Vec2 position = new Vec2(xAt*METERS_PER_PIXEL, yAt*METERS_PER_PIXEL);
		if(isLocalPoint)
			position = jboxBody.getWorldPoint(position);
		jboxBody.applyLinearImpulse(new Vec2(xMagnitude, yMagnitude), position);
	}
	
	/**
	 * (from JBox2d): Apply an impulse. This affects the angular velocity without
	 * affecting the linear velocity of the center of mass. This wakes up the
	 * body.
	 * 
	 * @param impulse about the z-axis (out of the screen), in newton*seconds.
	 */
	public void applyAngularImpulse(float angularImpulse){
		assertBodyAttached();
		jboxBody.applyAngularImpulse(angularImpulse);
	}
	
	/**
	 * Get the X position of the body
	 * 
	 * @return The x position of the body
	 */
	public float getX() {
		return attached ? jboxBody.getPosition().x*PIXELS_PER_METER : jboxBodyDef.position.x*PIXELS_PER_METER;
	}

	
	/**
	 * Get the Y position of the body
	 * 
	 * @return The y position of the body
	 */
	public float getY() {
		return attached ? jboxBody.getPosition().y*PIXELS_PER_METER : jboxBodyDef.position.y*PIXELS_PER_METER;
	}
	
	/**
	 * Get the X position of the body in local coordinates
	 * @return The x position of the body in local coordinates
	 */
	public float getLocalX(){
		assertBodyAttached();
		return jboxBody.getLocalCenter().x*PIXELS_PER_METER;
	}
	
	/**
	 * Get the Y position of the body in local coordinates
	 * @return The y position of the body in local coordinates
	 */
	public float getLocalY(){
		assertBodyAttached();
		return jboxBody.getLocalCenter().y*PIXELS_PER_METER;
	}
	
	/**
	 * Get the rotation of the body
	 * 
	 * @return The rotation of the body
	 */
	public float getRotation() {
		assertBodyAttached();
		return jboxBody.getAngle();
	}

	/**
	 * Get the X velocity of the body
	 * 
	 * @return The x velocity of the body
	 */
	public float getXVelocity() {
		assertBodyAttached();
		return jboxBody.getLinearVelocity().x;		
	}

	
	/**
	 * Get the Y velocity of the body
	 * 
	 * @return The y velocity of the body
	 */
	public float getYVelocity() {
		assertBodyAttached();
		return jboxBody.getLinearVelocity().y;		
	}
	
	/**
	 * Get the angular velocity of the body
	 * 
	 * @return The angular velocity of the body
	 */
	public float getAngularVelocity() {
		assertBodyAttached();
		return jboxBody.getAngularVelocity();
	}
	
	/**
	 * Set the restitution applied when this body collides
	 * 
	 * @param rest The restitution applied when this body collides
	 */
	public void setRestitution(float rest) {
		shape.setRestitution(rest);
	}

	/**
	 * Set the friction applied when this body collides
	 * 
	 * @param f The friction applied when this body collides
	 */
	public void setFriction(float f) {
		shape.setFriction(f);
	}
	
	/**
	 * Set the density of this body
	 * 
	 * @param den The density of this body
	 */
	public void setDensity(float den) {
		shape.setDensity(den);
	}
	
	/**
	 * @return true if the body is attached to a world and is marked "active"
	 */
	public boolean isActive() {
		return attached && jboxBody.isActive();
	}
	
	/**
	 * Tests whether this body is out of bounds.
	 * Since the world no longer tracks which objects is out of bounds,
	 * this just checks whether the body is still active...which will actually
	 * work as expected if the world's {@link World.OutOfBoundsBehavior} is set
	 * to DESTROY or DEACTIVATE.
	 * @deprecated use {@link #isActive()}, {@link #isAttached()}, and {@link World#setOutOfBoundsBehavior(org.newdawn.fizzy.World.OutOfBoundsBehavior)} instead.
	 * @return true if body is inactive
	 */
	@Deprecated
	public boolean isOutOfBounds() {
		return !isActive();
	}
	
	/**
	 * @return true if the body is currently attached to a world
	 */
	public boolean isAttached() {
		return attached;
	}
	
	/**
	 * Notification that this body is being added to the world
	 * 
	 * @param world The world this body is being added to
	 */
	void addToWorld(World world) {
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
				
		jboxBody = jboxWorld.createBody(jboxBodyDef);
		shape.createInBody(this);
		attached = true;
	}

	/**
	 * Notification that this body is being removed from the world
	 * 
	 * @param world The world this body is being removed from
	 */
	void removeFromWorld(World world) {
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
		jboxWorld.destroyBody(jboxBody);
		resetTouching();
		attached = false;
	}
	
	/**
	 * Get the JBox2D body that is wrapped by this class
	 * 
	 * @return The body that is wrapped by this proxy class
	 */
	org.jbox2d.dynamics.Body getJBoxBody() {
		return jboxBody;
	}
	
	/**
	 * Get the Fizzy shape representing this body
	 * 
	 * @return The fizzy shape representing this body
	 */
	public Shape getShape() {
		return shape;
	}
	
	/**
	 * Set the position of the body. This can only be called after the body has been added
	 * to the world.
	 * 
	 * @param x The new x coordinate of the body
	 * @param y The new y coordinate of the body
	 */
	public void setPosition(float x, float y) {
		assertBodyAttached();
		jboxBody.setTransform(new Vec2(x*METERS_PER_PIXEL,y*METERS_PER_PIXEL), jboxBody.getAngle());
	}
	
	/**
	 * Set the rotation of the body. This can only be called after the body has been added
	 * to the world.
	 * 
	 * @param rotation The new rotation of the body
	 */
	public void setRotation(float rotation) {
		assertBodyAttached();
		jboxBody.setTransform(jboxBody.getPosition(), rotation);
	}
	
	/**
	 * Check the body has been added to the world 
	 */
	private void assertBodyAttached() {
		if (!attached) {
			throw new NotAttachedToWorldException();
		}
	}

	/**
	 * Check if this body is "sleeping", i.e. its not moving any more
	 * 
	 * @return True if this body is sleeping
	 */
	public boolean isSleeping() {
		assertBodyAttached();
		return !jboxBody.isAwake();
	}

	/**
	 * Translate this body by the given amount
	 * 
	 * @param x The amount to move the body on the x axis
	 * @param y The amount to move the body on the y axis
	 */
	public void translate(float x, float y) {
		setPosition(getX()+x, getY()+y);
	}

	/**
	 * Set the linear damping to apply to this body. Higher 
	 * value slows the body acceleration. Maximum is 1.0.
	 * 
	 * @param damping The amount to dampen the movement by
	 * @deprecated use {@link #setLinearDamping(float)} instead
	 */
	public void setDamping(float damping) {
		setLinearDamping(damping);
	}
	
	/**
	 * Set the linear damping to apply to this body. Higher 
	 * value slows the body acceleration. Maximum is 1.0.
	 * 
	 * @param damping The amount to dampen the movement by
	 */
	public void setLinearDamping(float damping){
		if (jboxBody != null) throw new AlreadyAddedToWorldException();
		jboxBodyDef.linearDamping = damping;
	}
	
	/**
	 * Set the angular damping to apply to this body.  Higher
	 * value slows the angular velocity faster.  Maximum is 1.0.
	 * 
	 * @param damping The amount to dampen the rotation by
	 */
	public void setAngularDamping(float damping){
		if (jboxBody != null) throw new AlreadyAddedToWorldException();
		jboxBodyDef.angularDamping = damping;
	}

	/**
	 * Set the linear velocity of this body
	 * 
	 * @param xVelocity The x component of the velocity
	 * @param yVelocity The y component of the velocity
	 */
	public void setVelocity(float xVelocity, float yVelocity) {
		assertBodyAttached();
		Vec2 vel = jboxBody.getLinearVelocity();
		vel.x = xVelocity;
		vel.y = yVelocity;
		jboxBody.setLinearVelocity(vel);
	}

	/**
	 * Set the angular velocity (the speed at which it rotates)
	 * 
	 * @param vel The angular velocity to apply
	 */
	public void setAngularVelocity(float vel) {
		assertBodyAttached();
		jboxBody.setAngularVelocity(vel);
	}
	
	/**
	 * Sets whether this body has fixed rotation enabled.
	 * Bodies with Fixed Rotation can't rotate...in case that wasn't obvious.
	 * @param fixedRotation true if fixed rotation mode should be enabled.
	 */
	public void setFixedRotation(boolean fixedRotation){
		if(attached){
			jboxBody.setFixedRotation(fixedRotation);
		} else {
			jboxBodyDef.fixedRotation = fixedRotation;
		}
	}
	
	/**
	 * Get the bounding box that encloses this body and all of its constituent shapes
	 * @return bounding box of all shapes enclosed in this body
	 */
	public BoundingBox getBoundingBox(){
		AABB bodyAABB = new AABB(new AABB(new Vec2(Float.MAX_VALUE, Float.MAX_VALUE), new Vec2(Float.MIN_VALUE, Float.MIN_VALUE)));
		AABB shapeAABB = new AABB();
		for(org.jbox2d.collision.shapes.Shape jshape : shape.getJBoxShapes()){
			jshape.computeAABB(shapeAABB, jboxBody.m_xf);
			if(shapeAABB.lowerBound.x < bodyAABB.lowerBound.x){
				bodyAABB.lowerBound.x = shapeAABB.lowerBound.x;
			}
			if(shapeAABB.lowerBound.y < bodyAABB.lowerBound.y){
				bodyAABB.lowerBound.y = shapeAABB.lowerBound.y;
			}
			if(shapeAABB.upperBound.x > bodyAABB.upperBound.x){
				bodyAABB.upperBound.x = shapeAABB.upperBound.x;
			}
			if(shapeAABB.upperBound.y > bodyAABB.upperBound.y){
				bodyAABB.upperBound.y = shapeAABB.upperBound.y;
			}
		}
		bodyAABB.lowerBound.x=bodyAABB.lowerBound.x*PIXELS_PER_METER;
		bodyAABB.lowerBound.y=bodyAABB.lowerBound.y*PIXELS_PER_METER;
		bodyAABB.upperBound.x=bodyAABB.upperBound.x*PIXELS_PER_METER;
		bodyAABB.upperBound.y=bodyAABB.upperBound.y*PIXELS_PER_METER;
		return BoundingBox.fromAABB(bodyAABB);
	}
	
	private boolean bullet = false;
	/**
	 * Sets whether this body is to be considered a bullet.
	 * @see #getBullet()
	 * @param bullet
	 */
	public void setBullet(boolean bullet){
		this.bullet = bullet;
		if(attached){
			jboxBody.setBullet(bullet);
		} else {
			jboxBodyDef.bullet = bullet;
		}
	}
	/**
	 * Is the current body considered a "bullet" (at high risk of passing through
	 * other objects due to velocity).  This is on by default for Dynamic<->Static interactions
	 * but not Dynamic<->Dynamic interactions.
	 * @see http://www.box2d.org/manual.html
	 * @return true if the object is a "bullet", false otherwise.
	 */
	public boolean getBullet(){
		return bullet;
	}

	/**
	 * Sets the body to active (or inactive).  Inactive objects are essentially
	 * non-existent, as far as the physics world is concerned.
	 * @see http://www.box2d.org/manual.html
	 * @param flag true if body is to be active, false otherwise.
	 */
	public void setActive(boolean flag) {
		assertBodyAttached();
		jboxBody.setActive(flag);
	}
}
