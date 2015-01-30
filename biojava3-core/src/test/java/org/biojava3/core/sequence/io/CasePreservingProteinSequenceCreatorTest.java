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
package org.biojava3.core.sequence.io;

import java.util.Collection;

import org.biojava3.core.exceptions.CompoundNotFoundException;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava3.core.sequence.io.CasePreservingProteinSequenceCreator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CasePreservingProteinSequenceCreatorTest {

	@Test
	public void testConstructor() throws CompoundNotFoundException {
		CasePreservingProteinSequenceCreator creator = new CasePreservingProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet());
		
		String seq = "aCDEfgHI-Jkl";
		ProteinSequence prot = (ProteinSequence) creator.getSequence(seq, 0);
		Collection<Object> uppercase = prot.getUserCollection();
		
		//test some assumptions. Hopefully work on non-english locals too?
		assertFalse(Character.isUpperCase('-'));
		assertFalse(Character.isUpperCase('.'));
		
		assertEquals("Lengths differ",seq.length(),uppercase.size());
		
		int i=0;
		for(Object obj : uppercase) {
			assertTrue("Not a Boolean",obj instanceof Boolean);
			Boolean bool = (Boolean)obj;
			assertEquals("Doesn't match case of "+seq.charAt(i),(Boolean)Character.isUpperCase(seq.charAt(i)),bool);
			i++;
		}
	}
}
