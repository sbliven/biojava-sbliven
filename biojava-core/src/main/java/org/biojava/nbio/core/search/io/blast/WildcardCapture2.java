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
 * Created on Nov 30, 2016
 * Author: blivens 
 *
 */
 
package org.biojava.nbio.core.search.io.blast;

import java.util.function.Function;

import org.biojava.nbio.core.sequence.template.Compound;
import org.biojava.nbio.core.sequence.template.Sequence;
 
public class WildcardCapture2 {
	public static Sequence<?> getSequenceFromString(String gappedSequenceString){
		return null;
	}
	/**
	 * Test of createObjects method, of class BlastXMLParser.
	 */
	public void testCreateObjectsUntyped() throws Exception {
		
		// Because types need to be consistent, use a helper method for wildcard capture
		testCreateObjectsUntypedHelper(WildcardCapture2::getSequenceFromString);
	}
	public <S extends Sequence<C>,C extends Compound> void testCreateObjectsUntypedHelper(Function<String, S> buildSeq) throws Exception {
	}
}
