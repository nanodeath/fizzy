package org.newdawn.fizzy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * The central object of the simulation. The world contains the bodies (and
 * joints) which model the world and react to the physics engine.
 * 
 * @author kevin
 */
public class World {
	public enum OutOfBoundsBehavior {
		/**
		 * Do nothing to objects that go out of bounds.
		 */
		NONE,
		/**
		 * Deactivate objects that go out of bounds (mark them as not active)
		 */
		DEACTIVATE,
		/**
		 * Remove objects from the world completely that go out of bounds.
		 */
		DESTROY
	}

	/**
	 * The amount by which to scale this engine so that it can work reasonably
	 * in pixels
	 */
	protected static float PIXELS_PER_METER = 10f;
	/**
	 * The amount by which to scale this engine so that it can work reasonably
	 * in pixels
	 */
	protected static float METERS_PER_PIXEL = 1f / PIXELS_PER_METER;

	/** The default gravity applied if none is specified (-10) */
	public static final float DEFAULT_GRAVITY = -10f;
	/**
	 * The default number of iteration used in the integration if none specified
	 * (10)
	 */
	public static final int DEFAULT_ITERATIONS = 10;

	/** The JBox2D world this World object is wrapping */
	private org.jbox2d.dynamics.World jboxWorld;
	/** The list of bodies added to the world */
	private List<Body<?>> bodies = new ArrayList<Body<?>>();
	/**
	 * A map from shapes that will be reported from collision to the bodies that
	 * own them
	 */
	private Map<org.jbox2d.collision.shapes.Shape, Body<?>> shapeMap = new HashMap<org.jbox2d.collision.shapes.Shape, Body<?>>();
	/** The list of listeners to be notified of collision events */
	private List<WorldListener> listeners = new ArrayList<WorldListener>();

	/** List of listeners that are associated with particular bodies */
	private Map<Body<?>, Set<WorldListener>> bodyListeners = new HashMap<Body<?>, Set<WorldListener>>();

	private AABB worldAABB;
	private AABB[] outOfBoundsRegions;
	private OutOfBoundsCallback outOfBoundsCallback;
	private OutOfBoundsBehavior outOfBoundsBehavior = OutOfBoundsBehavior.DEACTIVATE;

	/** The number of iterations to integrate over */
	private int velocityIterations;
	private int positionIterations;

	public World() {
		this(DEFAULT_GRAVITY);
	}

	public World(float gravity) {
		this(new Vec2(0f, gravity));
	}

	public World(Vec2 gravity) {
		setIterations(DEFAULT_ITERATIONS);
		boolean doSleep = true;
		jboxWorld = new org.jbox2d.dynamics.World(gravity, doSleep);
		jboxWorld.setContactListener(new ProxyContactListener());
	}

	/**
	 * Get the JBox2D world that is being wrapped
	 * 
	 * @return The JBox2D world that is being wrapped
	 */
	org.jbox2d.dynamics.World getJBoxWorld() {
		return jboxWorld;
	}

	/**
	 * Add a body to the world
	 * 
	 * @param body
	 *            The body to be added to the world
	 */
	public void add(Body<?> body) {
		body.addToWorld(this);
		List<org.jbox2d.collision.shapes.Shape> shapes = body.getShape()
				.getJBoxShapes();

		for (int i = 0; i < shapes.size(); i++) {
			shapeMap.put(shapes.get(i), body);
		}
		bodies.add(body);
	}

	/**
	 * Remove a body from the world
	 * 
	 * @param body
	 *            The body to be removed from the world
	 */
	public void remove(Body<?> body) {
		List<org.jbox2d.collision.shapes.Shape> shapes = body.getShape()
				.getJBoxShapes();

		for (int i = 0; i < shapes.size(); i++) {
			shapeMap.remove(shapes.get(i));
		}
		body.removeFromWorld(this);
		bodies.remove(body);
		bodyListeners.remove(body);
	}

	/**
	 * Get the number of bodies in the world
	 * 
	 * @return The number of bodies in the world
	 */
	public int getBodyCount() {
		return bodies.size();
	}

	/**
	 * Get a body at a particular index in the list of bodies
	 * 
	 * @param index
	 *            The index of the body to retrieve
	 * @return The body at the given index
	 */
	public Body<?> getBody(int index) {
		return bodies.get(index);
	}

	/**
	 * Update the world
	 * 
	 * @param timeStep
	 *            The amount of time to simulate
	 */
	public void update(float timeStep) {
		jboxWorld.step(timeStep, velocityIterations, positionIterations);
		resolveOutOfBounds();
	}

	private class BodyQueryCallbackHelper implements QueryCallback {
		private Collection<Body<?>> bodies;

		public BodyQueryCallbackHelper(Collection<Body<?>> bodies) {
			this.bodies = bodies;
		}

		@Override
		public boolean reportFixture(Fixture fixture) {
			Body<?> body = shapeMap.get(fixture.getShape());
			if (body != null) {
				bodies.add(body);
			}
			return true;
		}
	};

	private void resolveOutOfBounds() {
		/*
		 * Only continue if: a) out of bounds is defined, and b) either a
		 * callback or a non-NONE out of bounds behavior is set
		 */
		if ((outOfBoundsRegions != null && (outOfBoundsBehavior != OutOfBoundsBehavior.NONE || outOfBoundsCallback != null))) {
			Collection<Body<?>> bodies = new LinkedList<Body<?>>();
			QueryCallback queryCallback = new BodyQueryCallbackHelper(bodies);
			for (AABB region : outOfBoundsRegions) {
				jboxWorld.queryAABB(queryCallback, region);
			}

			if (outOfBoundsCallback != null) {
				boolean continueProcessing = outOfBoundsCallback
						.reportBodies(bodies);
				if (!continueProcessing) {
					return;
				}
			}
			if (outOfBoundsBehavior != OutOfBoundsBehavior.NONE) {
				for (Body<?> body : bodies) {
					switch (outOfBoundsBehavior) {
					case DEACTIVATE:
						body.jboxBody.setActive(false);
						break;
					case DESTROY:
						remove(body);
						break;
					}
				}
			}
		}
	}

	/**
	 * Add a listener to be notified of collisions
	 * 
	 * @param listener
	 *            The listener to be notified of collisions
	 */
	public void addListener(WorldListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listener that will no longer receive events
	 * 
	 * @param listener
	 *            The listener to be removed
	 */
	public void removeListener(WorldListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Add a listener that listens for collisions on a particular body.
	 * 
	 * @param body
	 *            body on which collisions should be detected
	 * @param listener
	 *            the listener to call
	 */
	public void addBodyListener(Body<?> body, WorldListener listener) {
		Set<WorldListener> listeners = bodyListeners.get(body);
		if (listeners == null) {
			listeners = new HashSet<WorldListener>();
			bodyListeners.put(body, listeners);
		}
		listeners.add(listener);
	}

	/**
	 * Remove a listener that listens for collisions on a particular body
	 * 
	 * @see #addBodyListener(Body, WorldListener)
	 * @param body
	 * @param listener
	 */
	public void removeBodyListener(Body<?> body, WorldListener listener) {
		Set<WorldListener> listeners = bodyListeners.get(body);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.isEmpty()) {
				bodyListeners.remove(body);
			}
		}
	}

	/**
	 * Establish world boundaries centered at the origin and going in
	 * half-widths and half-heights in horizontal and vertical directions
	 * respectively.
	 * 
	 * @param width
	 *            total width of the world
	 * @param height
	 *            total height of the world
	 */
	public void setBounds(float width, float height) {
		setBounds(-width / 2, -height / 2, width / 2, height / 2);
	}

	/**
	 * Establish world boundaries at the given coordinates.
	 * 
	 * @param x1
	 *            x-coordinate of lower-left coordinate
	 * @param y1
	 *            y-coordinate of lower-left coordinate
	 * @param x2
	 *            x-coordinate of upper-right coordinate
	 * @param y2
	 *            y-coordinate of upper-right coordinate
	 */
	public void setBounds(float x1, float y1, float x2, float y2) {
		worldAABB = new AABB(new Vec2(x1 * METERS_PER_PIXEL, y1
				* METERS_PER_PIXEL), new Vec2(x2 * METERS_PER_PIXEL, y2
				* METERS_PER_PIXEL));
		outOfBoundsRegions = new AABB[] {
				// everything below-left and directly left of worldAABB
				new AABB(new Vec2(Float.MIN_VALUE, Float.MIN_VALUE), new Vec2(
						x1, y2)),
				// everything above-left and directly above worldAABB
				new AABB(new Vec2(Float.MIN_VALUE, y2), new Vec2(x2,
						Float.MAX_VALUE)),
				// everything above-right and directly right of worldAABB
				new AABB(new Vec2(x2, y1), new Vec2(Float.MAX_VALUE,
						Float.MAX_VALUE)),
				// everything below-right and directly below worldAABB
				new AABB(new Vec2(x1, Float.MIN_VALUE), new Vec2(
						Float.MAX_VALUE, y1)) };
	}

	/**
	 * Return a list of all the bodies at the given position.
	 * 
	 * @param x
	 *            world x coordinate
	 * @param y
	 *            world y coordinate
	 * @return list of bodies at provided coordinate
	 */
	public List<Body<?>> bodiesAt(float x, float y) {
		return bodiesAt(x, y, x + 1f, y + 1f);
	}

	// public List<Body<?>> bodiesAt(float x, float y, float radius) {
	// List<Body<?>> bodies = bodiesAt(x - radius, y - radius, x + radius, y
	// + radius);
	// Iterator<Body<?>> it = bodies.iterator();
	// while (it.hasNext()) {
	// Body<?> body = it.next();
	// if (Math.hypot(x - body.getX(), y - body.getY()) > radius) {
	// it.remove();
	// }
	// }
	// return bodies;
	// }

	/**
	 * Return a list of all bodies in or near the given box.
	 */
	public List<Body<?>> bodiesAt(float x1, float y1, float x2, float y2) {
		float lowerX, upperX, lowerY, upperY;
		if (x1 < x2) {
			lowerX = x1 * METERS_PER_PIXEL;
			upperX = x2 * METERS_PER_PIXEL;
		} else {
			lowerX = x2 * METERS_PER_PIXEL;
			upperX = x1 * METERS_PER_PIXEL;
		}
		if (y1 < y2) {
			lowerY = y1 * METERS_PER_PIXEL;
			upperY = y2 * METERS_PER_PIXEL;
		} else {
			lowerY = y2 * METERS_PER_PIXEL;
			upperY = y1 * METERS_PER_PIXEL;
		}

		AABB aabb = new AABB(new Vec2(lowerX, lowerY), new Vec2(upperX, upperY));
		final List<Body<?>> bodies = new LinkedList<Body<?>>();
		jboxWorld.queryAABB(new BodyQueryCallbackHelper(bodies), aabb);
		return bodies;
	}

	/**
	 * Sets the "what happens" behavior when a body exits the world boundaries.
	 * Defaults to DEACTIVATE.
	 * 
	 * @param behavior
	 *            new behavior
	 */
	public void setOutOfBoundsBehavior(OutOfBoundsBehavior behavior) {
		setOutOfBoundsBehavior(behavior, null);
	}

	/**
	 * Same as {@link World#setOutOfBoundsBehavior(OutOfBoundsBehavior)}, but
	 * also configures a callback to run before the OutOfBoundsBehavior is
	 * enforced. The callback can modify its argument to change which bodies
	 * have the behavior applied, i.e. so that certain bodies aren't
	 * deactivated, destroyed, etc.
	 * 
	 * @param behavior
	 * @param callback
	 */
	public void setOutOfBoundsBehavior(OutOfBoundsBehavior behavior,
			OutOfBoundsCallback callback) {
		outOfBoundsBehavior = behavior;
		outOfBoundsCallback = callback;
	}

	public interface OutOfBoundsCallback {
		/**
		 * Bodies that are about to be processed -- you can modify this
		 * collection.
		 * 
		 * @param bodies
		 * @return false to abort regular OutOfBounds behavior
		 */
		public boolean reportBodies(Collection<Body<?>> bodies);
	}

	public void setIterations(int iterations) {
		velocityIterations = positionIterations = iterations;
	}

	/**
	 * Fire a notification to all listeners that a collision has occured
	 * 
	 * @param bodyA
	 *            The first body in the collision
	 * @param bodyB
	 *            The second body in the collision
	 */
	private void fireCollision(Body<?> bodyA, Body<?> bodyB,
			FizzyContact contact) {
		CollisionEvent event = new CollisionEvent(bodyA, bodyB, contact);
		for (WorldListener listener : listeners) {
			listener.collided(event);
		}
		Collection<WorldListener> moreListeners = bodyListeners.get(bodyA);
		if (moreListeners != null) {
			for (WorldListener listener : moreListeners) {
				listener.collided(event);
			}
		}
		moreListeners = bodyListeners.get(bodyB);
		if (moreListeners != null) {
			for (WorldListener listener : moreListeners) {
				listener.collided(event);
			}
		}
	}

	/**
	 * Fire a notification to all listeners that a separation has occured
	 * 
	 * @param bodyA
	 *            The first body in the separation
	 * @param bodyB
	 *            The second body in the separation
	 */
	private void fireSeparated(Body<?> bodyA, Body<?> bodyB,
			FizzyContact contact) {
		CollisionEvent event = new CollisionEvent(bodyA, bodyB, contact);
		for (WorldListener listener : listeners) {
			listener.separated(event);
		}

		Collection<WorldListener> moreListeners = bodyListeners.get(bodyA);
		if (moreListeners != null) {
			for (WorldListener listener : moreListeners) {
				listener.separated(event);
			}
		}
		moreListeners = bodyListeners.get(bodyB);
		if (moreListeners != null) {
			for (WorldListener listener : moreListeners) {
				listener.separated(event);
			}
		}
	}

	/**
	 * A contact listener to collect effects and proxy them on to world
	 * listeners
	 * 
	 * @author max
	 */
	private class ProxyContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Body<?> bodyA = shapeMap.get(contact.getFixtureA().getShape());
			Body<?> bodyB = shapeMap.get(contact.getFixtureB().getShape());

			if ((bodyA != null) && (bodyB != null)) {
				bodyA.touch(bodyB);
				bodyB.touch(bodyA);
				if (bodyA.touchCount(bodyB) == 1) {
					fireCollision(bodyA, bodyB, new FizzyContact(contact));
				}
			}
		}

		@Override
		public void endContact(Contact contact) {
			Body<?> bodyA = shapeMap.get(contact.getFixtureA().getShape());
			Body<?> bodyB = shapeMap.get(contact.getFixtureB().getShape());

			if ((bodyA != null) && (bodyB != null)) {
				bodyA.untouch(bodyB);
				bodyB.untouch(bodyA);

				if (bodyA.touchCount(bodyB) == 0) {
					fireSeparated(bodyA, bodyB, new FizzyContact(contact));
				}
			}
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
		}

	}

	/**
	 * Sets the world gravity.
	 * 
	 * Negative is "down", so you'll usually want a negative value here. There
	 * won't be any pull in the x-axis.
	 * 
	 * @param gravity
	 *            new value for gravity (negative for down)
	 */
	public void setGravity(float gravity) {
		setGravity(0f, gravity);
	}

	/**
	 * Sets x- and y-axis gravity for the world. This gives you complete control
	 * over horizontal gravity in addition to vertical gravity.
	 * 
	 * @param xGravity
	 *            horizontal pull
	 * @param yGravity
	 *            vertical pull
	 */
	public void setGravity(float xGravity, float yGravity) {
		jboxWorld.setGravity(new Vec2(xGravity, yGravity));
	}

	/** returns the scaling between meters and pixels */
	public static float getPixelsPerMeter() {
		return PIXELS_PER_METER;
	}

	/** returns the scaling between pixels and meters */
	public static float getMetersPerPixel() {
		return METERS_PER_PIXEL;
	}

	/**
	 * Use with extreme caution! All of the scaling will be done by this new
	 * standard, but any measurements that have already been made will use the
	 * previous scaling between pixels and meters. Note also that this will
	 * affect all worlds. Any scaling that needs to vary between worlds should
	 * be implemented separately
	 */
	public static void changePixelsPerMeter(float PixelsPerMeter) {
		PIXELS_PER_METER = PixelsPerMeter;
		METERS_PER_PIXEL = 1f / PIXELS_PER_METER;
	}
}
