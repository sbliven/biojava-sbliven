/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */

package org.biojava.nbio.structure;

import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Jules Jacobsen <jacobsen@ebi.ac.uk>
 */
public class ResidueNumberTest {

	/**
	 * Test of getChainName method, of class ResidueNumber.
	 */
	@Test
	public void testGetSetChainId() {
//        System.out.println("getChainName");
		ResidueNumber instance = new ResidueNumber("A", 42, ' ');
		String expResult = "A";
		String result = instance.getChainName();
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of getInsCode method, of class ResidueNumber.
	 */
	@Test
	public void testGetSetInsCode() {
//        System.out.println("getInsCode");
		ResidueNumber instance = new ResidueNumber("A", 42, ' ');
		Character expResult = ' ';
		Character result = instance.getInsCode();
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of getSeqNum method, of class ResidueNumber.
	 */
	@Test
	public void testGetSetResidueNumber() {
//        System.out.println("getSeqNum");
		ResidueNumber instance = new ResidueNumber("A", 42, ' ');
		Integer expResult = 42;
		Integer result = instance.getSeqNum();
		Assert.assertEquals(expResult, result);

	}


	/**
	 * Test of equals method, of class ResidueNumber.
	 */
	@Test
	public void testEquals() {
//        System.out.println("equals");
		ResidueNumber number1 = new ResidueNumber("A", 42, ' ');
		ResidueNumber number2 = new ResidueNumber("A", 42, ' ');
		boolean expResult = true;
		boolean result = number2.equals(number1);
		Assert.assertEquals(expResult, result);

		Set<ResidueNumber> numberSet= new HashSet<ResidueNumber>();
		numberSet.add(number1);
		numberSet.add(number2);
		Assert.assertEquals(1, numberSet.size());

	}

	/**
	 * Test of hashCode method, of class ResidueNumber.
	 */
	@Test
	public void testHashCode() {
//        System.out.println("hashCode");
		ResidueNumber instance = new ResidueNumber("A", 42, ' ');
		int expResult = 93290;
		int result = instance.hashCode();
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of toString method, of class ResidueNumber.
	 */
	@Test
	public void testToString() {
//        System.out.println("toString");
		ResidueNumber instance = new ResidueNumber("A", 42, ' ');
		String expResult = "42";
		String result = instance.toString();
		Assert.assertEquals(expResult, result);

	}

	/**
	 * Test of toPDB method, of class ResidueNumber.
	 */
	@Test
	public void testToPDB() {

		ResidueNumber instance  = new ResidueNumber("A", 42, ' ');
		ResidueNumber instance2 = new ResidueNumber("A", 42, null);

		String expResult = "A  42  ";
		String result1 = instance.toPDB();
		Assert.assertEquals(expResult, result1);

		String result2 = instance2.toPDB();
		Assert.assertEquals(expResult, result2);
	}
	
	/**
	 * Test parsing from strings
	 */
	@Test
	public void testFromString() {
		String range;
		ResidueNumber rs;
		
		range = "1";
		rs = ResidueNumber.fromString(range);
		assertEquals("Error parsing "+range, (Integer)1, rs.getSeqNum());
		assertNull("Error parsing "+range, rs.getChainName());
		assertNull("Error parsing "+range, rs.getInsCode());
		
		range = "1P";
		rs = ResidueNumber.fromString(range);
		assertEquals("Error parsing "+range, (Integer)1, rs.getSeqNum());
		assertNull("Error parsing "+range, rs.getChainName());
		assertEquals("Error parsing "+range, (Character)'P', rs.getInsCode());
		
		range = "-1";
		rs = ResidueNumber.fromString(range);
		assertEquals("Error parsing "+range, (Integer)(-1), rs.getSeqNum());
		assertNull("Error parsing "+range, rs.getChainName());
		assertNull("Error parsing "+range, rs.getInsCode());
		
		range = "-1P";
		rs = ResidueNumber.fromString(range);
		assertEquals("Error parsing "+range, (Integer)(-1), rs.getSeqNum());
		assertNull("Error parsing "+range, rs.getChainName());
		assertEquals("Error parsing "+range, (Character)'P', rs.getInsCode());
	}

	/**
	 * Test that some illegally formatted strings throw errors
	 */
	@Test
	public void testMisformatted(){
		String[] illegal = {  "P5", "--4", "1P2", "A  2B", };
		for(String rangeStr : illegal) {
			try {
				ResidueNumber.fromString(rangeStr);
				fail("Malformatted range passed: "+rangeStr);
			} catch(NumberFormatException e) {}
		}
	}

}
