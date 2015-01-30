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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.biojava3.core.sequence.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava3.core.sequence.compound.DNACompoundSet;
import org.biojava3.core.sequence.compound.NucleotideCompound;
import org.biojava3.core.sequence.loader.GenbankProxySequenceReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Scooter Willis <willishf at gmail dot com>
 */
public class GenbankCookbookTest {

	private final static Logger logger = LoggerFactory.getLogger(GenbankCookbookTest.class);

    public GenbankCookbookTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class GenbankReader.
     */
    @Test
    public void testProcess() throws Throwable {
        /*
         * Method 1: With the GenbankProxySequenceReader
         */
        //Try with the GenbankProxySequenceReader
        GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader
                = new GenbankProxySequenceReader<AminoAcidCompound>(System.getProperty("java.io.tmpdir"), "NP_000257", AminoAcidCompoundSet.getAminoAcidCompoundSet());
        ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
        genbankProteinReader.getHeaderParser().parseHeader(genbankProteinReader.getHeader(), proteinSequence);
        logger.info("Sequence({},{}) = {}...", proteinSequence.getAccession(), proteinSequence.getLength(), proteinSequence.getSequenceAsString().substring(0, 10));

	GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader 
	= new GenbankProxySequenceReader<NucleotideCompound>(System.getProperty("java.io.tmpdir"), "NM_001126", DNACompoundSet.getDNACompoundSet());
	DNASequence dnaSequence = new DNASequence(genbankDNAReader);
	genbankDNAReader.getHeaderParser().parseHeader(genbankDNAReader.getHeader(), dnaSequence);
	logger.info("Sequence({},{}) = {}...", dnaSequence.getAccession(), dnaSequence.getLength(), dnaSequence.getSequenceAsString().substring(0, 10));
        /*
         * Method 2: With the GenbankReaderHelper
         */
        //Try with the GenbankReaderHelper
        File dnaFile = new File("src/test/resources/NM_000266.gb");
        File protFile = new File("src/test/resources/BondFeature.gb");

        LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(dnaFile);
        for (DNASequence sequence : dnaSequences.values()) {
            logger.info("DNA Sequence: {}", sequence.getSequenceAsString());
        }

        LinkedHashMap<String, ProteinSequence> protSequences = GenbankReaderHelper.readGenbankProteinSequence(protFile);
        for (ProteinSequence sequence : protSequences.values()) {
            logger.info("Protein Sequence: {}", sequence.getSequenceAsString());
        }
        /*
         * Method 3: With the GenbankReader Object 
         */
        //Try reading with the GanbankReader
        FileInputStream is = new FileInputStream(dnaFile);
        GenbankReader<DNASequence, NucleotideCompound> dnaReader = new GenbankReader<DNASequence, NucleotideCompound>(
                is,
                new GenericGenbankHeaderParser<DNASequence, NucleotideCompound>(),
                new DNASequenceCreator(DNACompoundSet.getDNACompoundSet())
        );
        dnaSequences = dnaReader.process();
        is.close();
        logger.info("DNA Sequence: {}", dnaSequences);

        is = new FileInputStream(protFile);
        GenbankReader<ProteinSequence, AminoAcidCompound> protReader = new GenbankReader<ProteinSequence, AminoAcidCompound>(
                is,
                new GenericGenbankHeaderParser<ProteinSequence, AminoAcidCompound>(),
                new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet())
        );
        protSequences = protReader.process();
        is.close();
        logger.info("Protein Sequence: {}", protSequences);

    }

}
