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
 * Created on June 15, 2010
 * Author: Mark Chapman
 */

package org.biojava.nbio.core.alignment;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.AlignedSequence.Step;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.alignment.template.Profile.StringFormat;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.AccessionID;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class SimpleProfileTest {

	private ProteinSequence query, target;
	private Profile<ProteinSequence, AminoAcidCompound> global, local, single;

	@BeforeEach
	public void setup() throws CompoundNotFoundException {
		query = new ProteinSequence("ARND");
		target = new ProteinSequence("RDG");
		query.setAccession(new AccessionID("Query"));
		target.setAccession(new AccessionID("Target"));
		global = new SimpleProfile<ProteinSequence, AminoAcidCompound>(query, target, Arrays.asList(new Step[] {
				Step.COMPOUND, Step.COMPOUND, Step.COMPOUND, Step.COMPOUND, Step.GAP }), 0, 0, Arrays.asList(
						new Step[] { Step.GAP, Step.COMPOUND, Step.GAP, Step.COMPOUND, Step.COMPOUND }),
				0, 0);
		local = new SimpleProfile<ProteinSequence, AminoAcidCompound>(query, target, Arrays.asList(new Step[] {
				Step.COMPOUND, Step.COMPOUND, Step.COMPOUND }), 1, 0, Arrays.asList(
						new Step[] { Step.COMPOUND,
								Step.GAP, Step.COMPOUND }),
				0, 1);
		single = new SimpleProfile<ProteinSequence, AminoAcidCompound>(query);
	}

	@Test
	public void testSimpleProfile() {
		List<Step> steps1 = Arrays.asList(new Step[] {
				Step.COMPOUND, Step.COMPOUND, Step.COMPOUND, Step.COMPOUND, Step.GAP });
		List<Step> steps2 = Arrays.asList(new Step[] { Step.GAP, Step.COMPOUND, Step.GAP, Step.COMPOUND });
		assertThrows(
				IllegalArgumentException.class,
				() -> new SimpleProfile<ProteinSequence, AminoAcidCompound>(query, target, steps1, 0, 0, steps2, 0, 0));
	}

	@Test
	public void testGetAlignedSequenceInt() {
		assertEquals("ARND-", global.getAlignedSequence(1).toString());
		assertEquals("-R-DG", global.getAlignedSequence(2).toString());
		assertEquals("RND", local.getAlignedSequence(1).toString());
		assertEquals("R-D", local.getAlignedSequence(2).toString());
		assertEquals("ARND", single.getAlignedSequence(1).toString());
	}

	@Test
	public void testGetAlignedSequenceIntOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getAlignedSequence(0));
	}

	@Test
	public void testGetAlignedSequenceIntOutOfBounds2() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getAlignedSequence(3));
	}

	@Test
	public void testGetAlignedSequenceIntOutOfBounds3() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getAlignedSequence(0));
	}

	@Test
	public void testGetAlignedSequenceIntOutOfBounds4() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getAlignedSequence(3));
	}

	@Test
	public void testGetAlignedSequenceIntOutOfBounds5() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getAlignedSequence(0));
	}

	@Test
	public void testGetAlignedSequenceIntOutOfBounds6() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getAlignedSequence(2));
	}

	@Test
	public void testGetAlignedSequenceS() throws CompoundNotFoundException {
		assertEquals("ARND-", global.getAlignedSequence(query).toString());
		assertEquals("-R-DG", global.getAlignedSequence(target).toString());
		assertNull(global.getAlignedSequence(new ProteinSequence("AR")));
		assertEquals("RND", local.getAlignedSequence(query).toString());
		assertEquals("R-D", local.getAlignedSequence(target).toString());
		assertNull(local.getAlignedSequence(new ProteinSequence("AR")));
		assertEquals("ARND", single.getAlignedSequence(query).toString());
		assertNull(single.getAlignedSequence(target));
	}

	@Test
	public void testGetAlignedSequences() {
		List<AlignedSequence<ProteinSequence, AminoAcidCompound>> list = global.getAlignedSequences();
		assertEquals(2, list.size());
		assertEquals("ARND-", list.get(0).toString());
		assertEquals("-R-DG", list.get(1).toString());
		list = local.getAlignedSequences();
		assertEquals(2, list.size());
		assertEquals("RND", list.get(0).toString());
		assertEquals("R-D", list.get(1).toString());
		list = single.getAlignedSequences();
		assertEquals(1, list.size());
		assertEquals("ARND", list.get(0).toString());
	}

	@Test
	public void testGetAlignedSequencesIntArray() {
		List<AlignedSequence<ProteinSequence, AminoAcidCompound>> list = global.getAlignedSequences(2, 1, 2);
		assertEquals(3, list.size());
		assertEquals("-R-DG", list.get(0).toString());
		assertEquals("ARND-", list.get(1).toString());
		assertEquals("-R-DG", list.get(2).toString());
		list = local.getAlignedSequences(2, 2, 1);
		assertEquals(3, list.size());
		assertEquals("R-D", list.get(0).toString());
		assertEquals("R-D", list.get(1).toString());
		assertEquals("RND", list.get(2).toString());
		list = single.getAlignedSequences(1, 1);
		assertEquals(2, list.size());
		assertEquals("ARND", list.get(0).toString());
		assertEquals("ARND", list.get(1).toString());
	}

	@Test
	public void testGetAlignedSequencesSArray() {
		List<AlignedSequence<ProteinSequence, AminoAcidCompound>> list = global.getAlignedSequences(query, query,
				target);
		assertEquals(3, list.size());
		assertEquals("ARND-", list.get(0).toString());
		assertEquals("ARND-", list.get(1).toString());
		assertEquals("-R-DG", list.get(2).toString());
		list = local.getAlignedSequences(target, query, target);
		assertEquals(3, list.size());
		assertEquals("R-D", list.get(0).toString());
		assertEquals("RND", list.get(1).toString());
		assertEquals("R-D", list.get(2).toString());
		list = single.getAlignedSequences(query, query);
		assertEquals(2, list.size());
		assertEquals("ARND", list.get(0).toString());
		assertEquals("ARND", list.get(1).toString());
	}

	@Test
	public void testGetCompoundAtIntInt() {
		assertEquals("D", global.getCompoundAt(1, 4).getShortName());
		assertEquals("-", global.getCompoundAt(2, 3).getShortName());
		assertEquals("R", local.getCompoundAt(1, 1).getShortName());
		assertEquals("-", local.getCompoundAt(2, 2).getShortName());
		assertEquals("N", single.getCompoundAt(1, 3).getShortName());
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundAt(0, 4));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds2() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundAt(3, 4));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds3() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundAt(1, 0));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds4() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundAt(2, 6));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds5() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundAt(0, 2));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds6() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundAt(3, 2));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds7() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundAt(1, 0));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds8() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundAt(2, 4));
	}

	@Test
	public void testGetCompoundAtIntIntOutOfBounds9() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getCompoundAt(1, 0));
	}

	@Test
	public void testGetCompoundAtSInt() throws CompoundNotFoundException {
		assertEquals("R", global.getCompoundAt(query, 2).getShortName());
		assertEquals("G", global.getCompoundAt(target, 5).getShortName());
		assertNull(global.getCompoundAt(new ProteinSequence("AR"), 3));
		assertEquals("N", local.getCompoundAt(query, 2).getShortName());
		assertEquals("D", local.getCompoundAt(target, 3).getShortName());
		assertNull(local.getCompoundAt(new ProteinSequence("AR"), 3));
		assertEquals("R", single.getCompoundAt(query, 2).getShortName());
		assertNull(single.getCompoundAt(target, 3));
	}

	@Test
	public void testGetCompoundAtSIntOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundAt(query, 0));
	}

	@Test
	public void testGetCompoundAtSIntOutOfBounds2() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundAt(target, 6));
	}

	@Test
	public void testGetCompoundAtSIntOutOfBounds3() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundAt(target, 0));
	}

	@Test
	public void testGetCompoundAtSIntOutOfBounds4() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundAt(query, 4));
	}

	@Test
	public void testGetCompoundAtSIntOutOfBounds5() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getCompoundAt(query, 0));
	}

	@Test
	public void testGetCompoundSet() {
		assertEquals(AminoAcidCompoundSet.getAminoAcidCompoundSet(), global.getCompoundSet());
		assertEquals(AminoAcidCompoundSet.getAminoAcidCompoundSet(), local.getCompoundSet());
		assertEquals(AminoAcidCompoundSet.getAminoAcidCompoundSet(), single.getCompoundSet());
	}

	@Test
	public void testGetCompoundsAt() {
		List<AminoAcidCompound> column = global.getCompoundsAt(5);
		assertEquals(2, column.size());
		assertEquals("-", column.get(0).getShortName());
		assertEquals("G", column.get(1).getShortName());
		column = local.getCompoundsAt(2);
		assertEquals(2, column.size());
		assertEquals("N", column.get(0).getShortName());
		assertEquals("-", column.get(1).getShortName());
		column = single.getCompoundsAt(2);
		assertEquals(1, column.size());
		assertEquals("R", column.get(0).getShortName());
	}

	@Test
	public void testGetCompoundsAtOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundsAt(0));
	}

	@Test
	public void testGetCompoundsAtOutOfBounds2() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getCompoundsAt(6));
	}

	@Test
	public void testGetCompoundsAtOutOfBounds3() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundsAt(0));
	}

	@Test
	public void testGetCompoundsAtOutOfBounds4() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getCompoundsAt(4));
	}

	@Test
	public void testGetCompoundsAtOutOfBounds5() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getCompoundsAt(0));
	}

	@Test
	public void testGetCompoundsAtOutOfBounds6() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getCompoundsAt(5));
	}

	@Test
	public void testGetIndexOf() {
		AminoAcidCompoundSet cs = AminoAcidCompoundSet.getAminoAcidCompoundSet();
		assertEquals(1, global.getIndexOf(cs.getCompoundForString("A")));
		assertEquals(2, global.getIndexOf(cs.getCompoundForString("R")));
		assertEquals(3, global.getIndexOf(cs.getCompoundForString("N")));
		assertEquals(4, global.getIndexOf(cs.getCompoundForString("D")));
		assertEquals(5, global.getIndexOf(cs.getCompoundForString("G")));
		assertEquals(1, global.getIndexOf(cs.getCompoundForString("-")));
		assertEquals(-1, global.getIndexOf(cs.getCompoundForString("E")));
		assertEquals(1, local.getIndexOf(cs.getCompoundForString("R")));
		assertEquals(2, local.getIndexOf(cs.getCompoundForString("N")));
		assertEquals(3, local.getIndexOf(cs.getCompoundForString("D")));
		assertEquals(2, local.getIndexOf(cs.getCompoundForString("-")));
		assertEquals(-1, local.getIndexOf(cs.getCompoundForString("K")));
		assertEquals(1, single.getIndexOf(cs.getCompoundForString("A")));
		assertEquals(2, single.getIndexOf(cs.getCompoundForString("R")));
		assertEquals(3, single.getIndexOf(cs.getCompoundForString("N")));
		assertEquals(4, single.getIndexOf(cs.getCompoundForString("D")));
		assertEquals(-1, single.getIndexOf(cs.getCompoundForString("G")));
	}

	@Test
	public void testGetIndicesAt() {
		assertArrayEquals(new int[] { 1, 1 }, global.getIndicesAt(1));
		assertArrayEquals(new int[] { 2, 1 }, global.getIndicesAt(2));
		assertArrayEquals(new int[] { 3, 1 }, global.getIndicesAt(3));
		assertArrayEquals(new int[] { 4, 2 }, global.getIndicesAt(4));
		assertArrayEquals(new int[] { 4, 3 }, global.getIndicesAt(5));
		assertArrayEquals(new int[] { 2, 1 }, local.getIndicesAt(1));
		assertArrayEquals(new int[] { 3, 1 }, local.getIndicesAt(2));
		assertArrayEquals(new int[] { 4, 2 }, local.getIndicesAt(3));
		assertArrayEquals(new int[] { 1 }, single.getIndicesAt(1));
		assertArrayEquals(new int[] { 2 }, single.getIndicesAt(2));
		assertArrayEquals(new int[] { 3 }, single.getIndicesAt(3));
		assertArrayEquals(new int[] { 4 }, single.getIndicesAt(4));
	}

	@Test
	public void testGetIndicesAtOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getIndicesAt(0));
	}

	@Test
	public void testGetIndicesAtOutOfBounds2() {
		assertThrows(IndexOutOfBoundsException.class, () -> global.getIndicesAt(6));
	}

	@Test
	public void testGetIndicesAtOutOfBounds3() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getIndicesAt(0));
	}

	@Test
	public void testGetIndicesAtOutOfBounds4() {
		assertThrows(IndexOutOfBoundsException.class, () -> local.getIndicesAt(4));
	}

	@Test
	public void testGetIndicesAtOutOfBounds5() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getIndicesAt(0));
	}

	@Test
	public void testGetIndicesAtOutOfBounds6() {
		assertThrows(IndexOutOfBoundsException.class, () -> single.getIndicesAt(5));
	}

	@Test
	public void testGetLastIndexOf() {
		AminoAcidCompoundSet cs = AminoAcidCompoundSet.getAminoAcidCompoundSet();
		assertEquals(1, global.getLastIndexOf(cs.getCompoundForString("A")));
		assertEquals(2, global.getLastIndexOf(cs.getCompoundForString("R")));
		assertEquals(3, global.getLastIndexOf(cs.getCompoundForString("N")));
		assertEquals(4, global.getLastIndexOf(cs.getCompoundForString("D")));
		assertEquals(5, global.getLastIndexOf(cs.getCompoundForString("G")));
		assertEquals(5, global.getLastIndexOf(cs.getCompoundForString("-")));
		assertEquals(-1, global.getLastIndexOf(cs.getCompoundForString("E")));
		assertEquals(1, local.getLastIndexOf(cs.getCompoundForString("R")));
		assertEquals(2, local.getLastIndexOf(cs.getCompoundForString("N")));
		assertEquals(3, local.getLastIndexOf(cs.getCompoundForString("D")));
		assertEquals(2, local.getLastIndexOf(cs.getCompoundForString("-")));
		assertEquals(-1, local.getLastIndexOf(cs.getCompoundForString("K")));
		assertEquals(1, single.getLastIndexOf(cs.getCompoundForString("A")));
		assertEquals(2, single.getLastIndexOf(cs.getCompoundForString("R")));
		assertEquals(3, single.getLastIndexOf(cs.getCompoundForString("N")));
		assertEquals(4, single.getLastIndexOf(cs.getCompoundForString("D")));
		assertEquals(-1, single.getLastIndexOf(cs.getCompoundForString("G")));
	}

	@Test
	public void testGetLength() {
		assertEquals(5, global.getLength());
		assertEquals(3, local.getLength());
		assertEquals(4, single.getLength());
	}

	@Test
	public void testGetOriginalSequences() {
		List<ProteinSequence> list = global.getOriginalSequences();
		assertEquals(2, list.size());
		assertEquals(query, list.get(0));
		assertEquals(target, list.get(1));
		list = local.getOriginalSequences();
		assertEquals(2, list.size());
		assertEquals(query, list.get(0));
		assertEquals(target, list.get(1));
		list = single.getOriginalSequences();
		assertEquals(1, list.size());
		assertEquals(query, list.get(0));
	}

	@Test
	public void testGetSize() {
		assertEquals(2, global.getSize());
		assertEquals(2, local.getSize());
		assertEquals(1, single.getSize());
	}

	@Disabled // TODO SimpleProfile.getSubProfile(Location)
	@Test
	public void testGetSubProfile() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsCircular() {
		assertFalse(global.isCircular());
		assertFalse(local.isCircular());
		assertFalse(single.isCircular());
	}

	@Test
	public void testToStringInt() {

		assertEquals(global.toString(3), String.format(
				"          1 3%n" +
						"Query   1 ARN 3%n" +
						"           | %n" +
						"Target  1 -R- 1%n" +
						"%n" +
						"          4 5%n" +
						"Query   4 D- 4%n" +
						"          | %n" +
						"Target  2 DG 3%n"));
		assertEquals(local.toString(4), String.format(
				"          1 3%n" +
						"Query   2 RND 4%n" +
						"          | |%n" +
						"Target  1 R-D 2%n"));
		assertEquals(single.toString(4), String.format(
				"         1  4%n" +
						"Query  1 ARND 4%n"));
	}

	@Test
	public void testToStringFormatted() {

		assertEquals(global.toString(StringFormat.ALN), String.format(
				"CLUSTAL W MSA from BioJava%n%n" +
						"Query     ARND- 4%n" +
						"           | | %n" +
						"Target    -R-DG 3%n"));
		assertEquals(local.toString(StringFormat.FASTA), String.format(
				">Query%n" +
						"RND%n" +
						">Target%n" +
						"R-D%n"));
		assertEquals(single.toString(StringFormat.MSF), String.format(
				"MSA from BioJava%n%n" +
						" MSF: 4  Type: P  Check: 735 ..%n%n" +
						" Name: Query  Len: 4  Check:  735  Weight: 1.0%n" +
						"%n//%n%n" +
						"Query ARND%n"));
	}

	@ParameterizedTest
	@EnumSource(Profile.StringFormat.class)
	void testToStringFormattedFiles(Profile.StringFormat format) throws IOException {
		String formatted = global.toString(format);
		assertTrue(formatted.length() > 0);
		String expected = getExpectedMSA(format);
		assertNotNull(expected);
		assertEquals(expected, formatted);

	}

	private String getExpectedMSA(StringFormat format) throws IOException {
		String resource = String.format("SimpleProfileTest.%s", format);
		InputStream contents = SimpleProfileTest.class.getResourceAsStream(resource);
		if (contents == null) {
			return null;
		}
		return new String(contents.readAllBytes(), StandardCharsets.UTF_8);
	}

	@Test
	public void testToString() {
		assertEquals(String.format("ARND-%n-R-DG%n"), global.toString());
		assertEquals(String.format("RND%nR-D%n"), local.toString());
		assertEquals(String.format("ARND%n"), single.toString());
	}

	@Test
	public void testIterator() {
		for (AlignedSequence<ProteinSequence, AminoAcidCompound> s : global) {
			assertEquals(5, s.toString().length());
		}
		for (AlignedSequence<ProteinSequence, AminoAcidCompound> s : local) {
			assertEquals(3, s.toString().length());
		}
		for (AlignedSequence<ProteinSequence, AminoAcidCompound> s : single) {
			assertEquals(4, s.toString().length());
		}
	}

}
