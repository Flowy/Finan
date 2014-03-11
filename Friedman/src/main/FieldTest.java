package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldTest {

	@Test
	public final void testDistanceFromIntIntField() {
		Field actual = new Shop(7, 8, Shop.Status.VOID);
		System.out.println(Field.distanceFrom(8, 7, actual));
		fail("Not yet implemented"); // TODO
	}

}
