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

import java.util.List;
import java.util.function.Function;
 
public class WildcardCapture2 {
//	@SuppressWarnings("serial")
//	static class NumberLists<L extends List<T>,T extends Number> extends ArrayList<L> {}
	public static List<? extends Number> getSequenceFromString(String gappedSequenceString){
		return null;
	}
	/**
	 * Test of createObjects method, of class BlastXMLParser.
	 */
	public void testCreateObjectsUntyped() throws Exception {
		
		// Because types need to be consistent, use a helper method for wildcard capture
		testCreateObjectsUntypedHelper(WildcardCapture2::getSequenceFromString);
	}
	public <S extends List<C>,C extends Number> void testCreateObjectsUntypedHelper(Function<String, S> buildSeq) throws Exception {
	}
}
