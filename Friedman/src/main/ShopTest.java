package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class ShopTest {

	@Test
	public final void testWantedProfitPerUnit() {
		double price = Shop.wantedProfitPerUnit(-1.2f);
		System.out.println(price);
		fail("Not yet implemented"); // TODO
	}

}
