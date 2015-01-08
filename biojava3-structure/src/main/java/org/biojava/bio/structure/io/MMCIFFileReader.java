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
 * created at Oct 18, 2008
 */
package org.biojava.bio.structure.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.align.util.UserConfiguration;
import org.biojava.bio.structure.io.mmcif.MMcifParser;
import org.biojava.bio.structure.io.mmcif.SimpleMMcifConsumer;
import org.biojava.bio.structure.io.mmcif.SimpleMMcifParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** How to parse an mmCif file:
 * <pre>
  public static void main(String[] args) throws Exception {
        String filename =  "/path/to/something.cif.gz" ;

        StructureIOFile reader = new MMCIFFileReader();

        Structure struc = reader.getStructure(filename);
        System.out.println(struc);
    }
    </pre>
 *
 * @author Andreas Prlic
 * @since 1.7
 *
 */
public class MMCIFFileReader extends LocalPDBDirectory {

	private static final Logger logger = LoggerFactory.getLogger(MMCIFFileReader.class);

	public static final String[] MMCIF_SPLIT_DIR    = new String[]{"data","structures","divided" ,"mmCIF"};
	public static final String[] MMCIF_ALL_DIR      = new String[]{"data","structures","all"     ,"mmCIF"};
	public static final String[] MMCIF_OBSOLETE_DIR = new String[]{"data","structures","obsolete","mmCIF"};

	private SimpleMMcifConsumer consumer;

	public static void main(String[] args) throws Exception {

		MMCIFFileReader reader = new MMCIFFileReader();
		FileParsingParameters params = new FileParsingParameters();
		reader.setFileParsingParameters(params);


		Structure struc = reader.getStructureById("1m4x");
		System.out.println(struc);
		System.out.println(struc.toPDB());


	}

	/**
	 * Constructs a new MMCIFFileReader, initializing the extensions member variable.
	 * The path is initialized in the same way as {@link UserConfiguration}, 
	 * i.e. to system property/environment variable {@link UserConfiguration#PDB_DIR}.
	 * Both autoFetch and splitDir are initialized to false
	 */
	public MMCIFFileReader(){
		this(null);
	}

	/**
	 * Constructs a new PDBFileReader, initializing the extensions member variable.
	 * The path is initialized to the given path, both autoFetch and splitDir are initialized to false.
	 */
	public MMCIFFileReader(String path){
		super(path);
		addExtension(".cif");
		addExtension(".mmcif");
		addExtension(".cif.gz");
		addExtension(".mmcif.gz");
	}

	@Override
	public Structure getStructure(InputStream inStream) throws IOException{

		MMcifParser parser = new SimpleMMcifParser();

		consumer = new SimpleMMcifConsumer();

		consumer.setFileParsingParameters(getFileParsingParameters());


		// The Consumer builds up the BioJava - structure object.
		// you could also hook in your own and build up you own data model.
		parser.addMMcifConsumer(consumer);

		parser.parse(new BufferedReader(new InputStreamReader(inStream)));


		// now get the protein structure.
		Structure cifStructure = consumer.getStructure();

		return cifStructure;
	}

	public SimpleMMcifConsumer getMMcifConsumer(){
		return consumer;
	}

//	public void setMMCifConsumer(SimpleMMcifConsumer consumer){
//		this.consumer = consumer;
//	}

	@Override
	protected String getFilename(String pdbId) {
		return pdbId.toLowerCase()+".cif.gz";
	}

	@Override
	protected String[] getSplitDirPath() {
		return MMCIF_SPLIT_DIR;
	}

	@Override
	protected String[] getUnsplitDirPath() {
		return MMCIF_ALL_DIR;
	}

	@Override
	protected String[] getObsoleteDirPath() {
		return MMCIF_OBSOLETE_DIR;
	}

}
