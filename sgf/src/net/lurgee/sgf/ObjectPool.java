/*
 * @(#)ObjectPool.java		2005/10/31
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.ArrayList;

/**
 * Class for pooling homogeneous objects that implement {@link Poolable}. This allows objects to be reused during
 * recursive processes rather than having to create new ones (which often results in many short-lived objects being
 * created). This is of particular advantage when the cost of creating the objects is much higher than the cost
 * of reuse. 
 * @author mpatric
 */
public class ObjectPool {

	private static final String MSG_MAX_OBJECTS_IN_POOL = "Maximum objects in pool already";
	private static final int DEFAULT_MAX_OBJECTS = -1;
	private static final int DEFAULT_INITIAL_CAPACITY = 4;

	protected final ArrayList<Poolable> availableObjects;
	protected final ArrayList<Poolable> checkedOutObjects;
	protected final Class<?> itemClass;
	protected final int maxObjects;

	/**
	 * Default constructor; create a pool with the default max objects, initial capacity.
	 * @param itemClass The class of the items being stored in this pool.
	 */
	public ObjectPool(Class<?> itemClass) {
		this(itemClass, DEFAULT_MAX_OBJECTS, DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * Constructor to create a pool with the specified max objects and the default initial capacity.
	 * @param itemClass The class of the items being stored in this pool.
	 * @param maxObjects Maximum number of objects in the pool (or -1 for no maximum).
	 */
	public ObjectPool(Class<?> itemClass, int maxObjects) {
		this(itemClass, maxObjects, DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * Constructor to create a pool with the specified max objects and initial capacity.
	 * @param itemClass The class of the items being stored in this pool.
	 * @param maxObjects Maximum number of objects in the pool (or -1 for no maximum).
	 * @param initialCapacity Initial capacity for the pool.
	 */
	public ObjectPool(Class<?> itemClass, int maxObjects, int initialCapacity) {
		this.itemClass = itemClass;
		this.maxObjects = maxObjects;
		availableObjects = new ArrayList<Poolable>(initialCapacity);
		checkedOutObjects = new ArrayList<Poolable>(initialCapacity);
	}

	protected Poolable createPoolable() throws ObjectPoolFullException, ObjectPoolException {
		synchronized (this) {
			Poolable poolable = null;
			if (maxObjects > 0 && checkedOutObjects.size() + availableObjects.size() >= maxObjects) {
				throw new ObjectPoolFullException(MSG_MAX_OBJECTS_IN_POOL);
			}
			try {
				poolable = (Poolable) itemClass.newInstance();
			} catch (InstantiationException ie) {
				throw new ObjectPoolException(ie.getMessage());
			} catch (IllegalAccessException iae) {
				throw new ObjectPoolException(iae.getMessage());
			}
			availableObjects.add(poolable);
			return poolable;
		}
	}

	/**
	 * Check out an object of type {@link #itemClass}, which is either obtained from the pool (if there is one
	 * available) or created (and added to the pool).
	 * @return An initialised object of type {@link #itemClass}.
	 * @throws ObjectPoolFullException The maximum number of objects in the pool is reached.
	 * @throws ObjectPoolException Another error occurred.
	 */
	public Poolable checkOut() throws ObjectPoolFullException, ObjectPoolException {
		return checkOut(null);
	}

	/**
	 * Check out an object of type {@link #itemClass}, which is either obtained from the pool (if there is one
	 * available) or created (and added to the pool).
	 * @param params Parameters to pass to the object via its init() method after creation - may be null.
	 * @return An initialised object of type {@link #itemClass}.
	 * @throws ObjectPoolFullException The maximum number of objects in the pool is reached.
	 * @throws ObjectPoolException Another error occurred.
	 */
	public Poolable checkOut(Object params) throws ObjectPoolFullException, ObjectPoolException {
		synchronized (this) {
			boolean isNew = false;
			Poolable poolable = null;
			if (availableObjects.size() == 0) {
				// create a new object for the pool
				poolable = createPoolable();
				isNew = true;
			}
			// use an existing object from the pool
			poolable = availableObjects.get(0);
			if (isNew) {
				poolable.poolableInit(params);
			} else {
				poolable.poolableRecycle(params);
			}
			availableObjects.remove(poolable);
			checkedOutObjects.add(poolable);
			return poolable;
		}
	}

	/**
	 * Check in an object which was previously checked out of the pool.
	 * @param poolable An object which was previously checked out of the pool.
	 * @return
	 * 	<ul>
	 * 		<li>true if the object was made available again;</li>
	 * 		<li>false if the object is not in the pool.</li>
	 * 	</ul>
	 */
	public boolean checkIn(Poolable poolable) {
		synchronized (this) {
			if (checkedOutObjects.remove(poolable)) {
				poolable.poolableDone();
				availableObjects.add(poolable);
				return true;
			}
			return false;
		}
	}
	
	public int countCheckedOutObjects() {
		return checkedOutObjects.size();
	}
}
