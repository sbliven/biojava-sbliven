package org.biojava.nbio.alignment;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.AccessionID;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.template.Sequence;

public class ProfileFormatTest {

    private static boolean isSimilar(SubstitutionMatrix<AminoAcidCompound> matrix, AminoAcidCompound aa1,
            AminoAcidCompound aa2) {
        short val = matrix.getValue(aa1, aa2);
        return val > 0;
    }

    public static <S extends Sequence<C>, C extends AminoAcidCompound> String customFormat(
            SmithWaterman<S, C> alignment) {
        SubstitutionMatrix<AminoAcidCompound> blosum62 = SubstitutionMatrixHelper.getBlosum62();
        AlignedSequence<S, C> seq1 = alignment.getPair().getAlignedSequence(1);
        AlignedSequence<S, C> seq2 = alignment.getPair().getAlignedSequence(2);
        String s1 = seq1.toString();
        String s2 = seq2.toString();
        String start1 = seq1.getStart().getPosition().toString();
        String start2 = seq2.getStart().getPosition().toString();
        int startWidth = Math.max(start1.length(), start2.length());

        StringBuilder s = new StringBuilder();
        s.append(String.format("%" + startWidth + "d ", seq1.getStart().getPosition()));
        s.append(s1);
        s.append(String.format(" %d", seq1.getEnd().getPosition()));
        s.append(String.format("%n"));

        s.append(String.format("%" + startWidth + "s", " "));
        for (int i = 0; i < s1.toString().length(); i++) {
            if (i >= s2.length())
                break;

            C c1 = seq1.getCompoundAt(i + 1);
            C c2 = seq2.getCompoundAt(i + 1);
            s.append(isSimilar(blosum62, c1, c2) ? '|' : ' ');
        }
        s.append(String.format("%n"));
        s.append(String.format("%" + startWidth + "d ", seq2.getStart().getPosition()));
        s.append(s2);
        s.append(String.format(" %d", seq2.getEnd().getPosition()));

        return s.toString();
    }

    public static void main(String[] args) throws CompoundNotFoundException {
        // Test alignment
        ProteinSequence query = new ProteinSequence("AERNDKK");
        ProteinSequence target = new ProteinSequence("ERDNKGFPS");
        query.setAccession(new AccessionID("query"));
        target.setAccession(new AccessionID("target"));
        SimpleGapPenalty gaps = new SimpleGapPenalty((short) 2, (short) 1);
        SubstitutionMatrix<AminoAcidCompound> blosum62 = SubstitutionMatrixHelper.getBlosum62();
        SmithWaterman<ProteinSequence, AminoAcidCompound> alignment = new SmithWaterman<ProteinSequence, AminoAcidCompound>(
                query, target, gaps, blosum62);
        String s = customFormat(alignment);

        System.out.format("### custom:%n%s%n%n", s);
        System.out.format("### toString:%n%s%n%n", alignment.getPair());
 
        Map<Profile.StringFormat, String> fmts = Arrays.stream(Profile.StringFormat.values()).collect(
                Collectors.toMap(Function.identity(), f -> alignment.getPair().toString(f)));
        fmts.forEach((f, str) -> System.out.format("### %s:%n%s%n%n", f, str));

    }
}
