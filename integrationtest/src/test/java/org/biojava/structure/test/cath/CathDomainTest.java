package org.biojava.structure.test.cath;

import static org.junit.Assert.*;

import org.biojava.bio.structure.cath.CathDomain;
import org.biojava.bio.structure.cath.CathFactory;
import org.junit.Test;


/**
 * A test for {@link CathDomain}.
 * @author dmyersturnbull
 */
public class CathDomainTest {
	@Test
	public void test() {
		String id = "1qvrC03";
		CathDomain domain = CathFactory.getCathDatabase().getDomainByCathId(id);
		assertEquals("1qvr.C_332-400,C_514-540", domain.toCanonical().getIdentifier());
	}
}
