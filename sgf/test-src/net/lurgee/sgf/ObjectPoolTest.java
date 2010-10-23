/*
 * @(#)ObjectPoolTest.java		2005/10/31
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import junit.framework.TestCase;

/**
 * Unit tests for {@link ObjectPool}.
 * @author mpatric
 */
public class ObjectPoolTest extends TestCase {

	private static final int MAX_OBJECTS = 5;
	private static final int INITIAL_CAPACITY = 2;
	
	private ObjectPool pool = new ObjectPool(TestPoolable.class, MAX_OBJECTS, INITIAL_CAPACITY);
	
	public void testShouldCreateAnObjectWhenCheckedOutForTheFirstTime() throws Exception {
		assertEquals(0, pool.countCheckedOutObjects());
		TestPoolable testPoolable = (TestPoolable) pool.checkOut();
		assertEquals(1, pool.countCheckedOutObjects());
		assertEquals(1, testPoolable.getInitCount());
		assertEquals(0, testPoolable.getDoneCount());
		assertEquals(0, testPoolable.getRecycleCount());
	}
	
	public void testShouldReuseAnObjectWhichIsAvailableInThePool() throws Exception {
		TestPoolable testPoolable = (TestPoolable) pool.checkOut();
		pool.checkIn(testPoolable);
		assertEquals(0, pool.countCheckedOutObjects());
		TestPoolable testPoolable2 = (TestPoolable) pool.checkOut();
		assertEquals(1, pool.countCheckedOutObjects());
		assertEquals(1, testPoolable2.getInitCount());
		assertEquals(1, testPoolable.getDoneCount());
		assertEquals(1, testPoolable.getRecycleCount());
	}
	
	public void testShouldThrowObjectPoolFullExceptionIfNoMoreCapacity() throws Exception {
		for (int i = 0; i < MAX_OBJECTS; i++) {
			pool.checkOut();
			assertEquals(i + 1, pool.countCheckedOutObjects());
		}
		try {
			pool.checkOut();
			fail("Expected exception not thrown");
		} catch (ObjectPoolFullException e) {
			assertEquals("Maximum objects in pool already", e.getMessage());
		}
	}
	
	public void testShouldCheckinAllCheckedOutObjects() throws Exception {
		Poolable objects[] = new Poolable[MAX_OBJECTS];
		for (int i = 0; i < MAX_OBJECTS; i++) {
			objects[i] = pool.checkOut();
			assertEquals(i + 1, pool.countCheckedOutObjects());
		}
		assertEquals(MAX_OBJECTS, pool.checkedOutObjects.size());
		for (int i = 0; i < MAX_OBJECTS; i++) {
			assertTrue(pool.checkIn(objects[i]));
			assertEquals(MAX_OBJECTS - i - 1, pool.countCheckedOutObjects());
		}
		assertEquals(0, pool.countCheckedOutObjects());
		assertEquals(MAX_OBJECTS, pool.availableObjects.size());
	}
	
	public void testShouldPassParametersToPoolableOnCheckoutIfProvided() throws Exception {
		String param = "Hello there!";
		TestPoolable testPoolable = (TestPoolable) pool.checkOut(param);
		assertEquals(param, testPoolable.getLastParams());
	}
	
	public void testShouldReturnFalseOnCheckinIfObjectNotFromPool() throws Exception {
		TestPoolable fromPool = (TestPoolable) pool.checkOut();
		TestPoolable notFromPool = new TestPoolable();
		assertTrue(pool.checkIn(fromPool));
		assertFalse(pool.checkIn(notFromPool));
	}
}
