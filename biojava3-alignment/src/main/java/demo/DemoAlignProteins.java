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
package demo;

import java.net.URL;

import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.SubstitutionMatrixHelper;
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava3.alignment.template.GapPenalty;
import org.biojava3.alignment.template.PairwiseSequenceAligner;
import org.biojava3.alignment.template.SequencePair;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.io.FastaReaderHelper;

public class DemoAlignProteins {

	public static void main(String[] args) throws Exception {

		String uniprotID1 = "P69905";
		String uniprotID2 = "P68871";

		ProteinSequence s1 = getSequenceForId(uniprotID1);
		ProteinSequence s2 = getSequenceForId(uniprotID2);

		SubstitutionMatrix<AminoAcidCompound> matrix = SubstitutionMatrixHelper.getBlosum65();

		GapPenalty penalty = new SimpleGapPenalty();

		int gop = 8;
		int extend = 1;
		penalty.setOpenPenalty(gop);
		penalty.setExtensionPenalty(extend);


		PairwiseSequenceAligner<ProteinSequence, AminoAcidCompound> smithWaterman =
				Alignments.getPairwiseAligner(s1, s2, PairwiseSequenceAlignerType.LOCAL, penalty, matrix);

		SequencePair<ProteinSequence, AminoAcidCompound> pair = smithWaterman.getPair();


		System.out.println(pair.toString(60));

		
	}

	private static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
		URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
		ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
		System.out.printf("id : %s %s%s%s", uniProtId, seq, System.getProperty("line.separator"), seq.getOriginalHeader());
		System.out.println();
		
		return seq;
	}
}