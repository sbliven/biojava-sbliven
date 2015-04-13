package org.biojava.nbio.structure.xtal;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.contact.StructureInterfaceCluster;
import org.biojava.nbio.structure.contact.StructureInterfaceList;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.PDBFileParser;
import org.junit.Test;

public class TestInterfaceClustering {

	@Test
	public void test3DDO() throws IOException, StructureException {
		
		// 3DDO is special in that it contains 6 chains in 1 entity, all of them with different residue numbering
		
		AtomCache cache = new AtomCache();
		FileParsingParameters params = new FileParsingParameters();
		params.setAlignSeqRes(true);
		cache.setFileParsingParams(params);
		cache.setUseMmCif(true);
		
		StructureIO.setAtomCache(cache); 
				
		Structure s = StructureIO.getStructure("3DDO");
		
		CrystalBuilder cb = new CrystalBuilder(s);
		StructureInterfaceList interfaces = cb.getUniqueInterfaces(5.5);
		interfaces.calcAsas(100, 1, 0);
		interfaces.removeInterfacesBelowArea();
		
		List<StructureInterfaceCluster> clusters = interfaces.getClusters();
		
		// 22 if below 35A2 interfaces are filtered
		assertEquals(22,interfaces.size());
		
		// we simply want to test that some interfaces cluster together, for this entry 
		// it is problematic because of different residue numbering between different chains of same entity
		assertTrue("Expected fewer than 22 interfaces (some interfaces should cluster together)",clusters.size()<22);
		
		// first 2 clusters are of size 3
		assertEquals("Cluster 1 should have 3 members",3,clusters.get(0).getMembers().size());
		assertEquals("Cluster 2 should have 3 members",3,clusters.get(1).getMembers().size());
		
		// detection of isologous test: first 3 interfaces should be isologous
		
		assertTrue("Interface 1 should be isologous",interfaces.get(1).isIsologous());
		assertTrue("Interface 2 should be isologous",interfaces.get(2).isIsologous());
		assertTrue("Interface 3 should be isologous",interfaces.get(3).isIsologous());
		
		
		
	}

	
	@Test
	public void test3C5FWithSeqresPdb() throws IOException, StructureException {
		
		InputStream inStream = new GZIPInputStream(this.getClass().getResourceAsStream("/org/biojava/nbio/structure/io/3c5f_raw.pdb.gz"));
		assertNotNull(inStream);

		PDBFileParser pdbpars = new PDBFileParser();
		FileParsingParameters params = new FileParsingParameters();
		params.setAlignSeqRes(true);
		pdbpars.setFileParsingParameters(params);

		Structure s = pdbpars.parsePDBFile(inStream) ;

		assertNotNull(s);
		
		assertEquals(8, s.getChains().size());
		
		assertEquals(4, s.getCompounds().size());

		CrystalBuilder cb = new CrystalBuilder(s);
		StructureInterfaceList interfaces = cb.getUniqueInterfaces(5.5);
		interfaces.calcAsas(100, 1, 0);
		interfaces.removeInterfacesBelowArea();

		List<StructureInterfaceCluster> clusters = interfaces.getClusters();

		// 23 if below 35A2 interfaces are filtered
		assertEquals(23,interfaces.size());

		// we simply want to test that some interfaces cluster together
		assertTrue("Expected fewer than 23 interfaces (some interfaces should cluster together)",clusters.size()<23);

		// third cluster (index 2) is of size 2
		assertEquals("Cluster 3 should have 2 members",2,clusters.get(2).getMembers().size());

		assertTrue("Interface 3 should be isologous",interfaces.get(3).isIsologous());
		
		
	}
	
	// This doesn't work yet, since for raw files without a SEQRES, the seqres groups are not populated. Instead
	// in that case Compound.getAlignedResIndex() returns residue numbers as given (without insertion codes) and
	// thus in general residues will not be correctly aligned between different chains of same entity. This breaks 
	// cases like 3ddo (with no SEQRES records) where residue numbering is different in every chain of the one entity. 
	// Then contact overlap calculation will be wrong and interface clustering won't work.
	// see https://github.com/eppic-team/eppic/issues/39
	// See also TestCompoundResIndexMapping
	//@Test 
	public void test3DDONoSeqresPdb() throws IOException, StructureException {
		
		// 3ddo contains 6 chains in 1 entity, with residue numbering completely different in each of the chains
		
		InputStream inStream = new GZIPInputStream(this.getClass().getResourceAsStream("/org/biojava/nbio/structure/io/3ddo_raw_noseqres.pdb.gz"));
		assertNotNull(inStream);

		PDBFileParser pdbpars = new PDBFileParser();
		FileParsingParameters params = new FileParsingParameters();
		params.setAlignSeqRes(true);
		pdbpars.setFileParsingParameters(params);

		Structure s = pdbpars.parsePDBFile(inStream) ;

		assertNotNull(s);
		
		assertEquals(6, s.getChains().size());
		
		assertEquals(1, s.getCompounds().size());
		
		CrystalBuilder cb = new CrystalBuilder(s);
		StructureInterfaceList interfaces = cb.getUniqueInterfaces(5.5);
		interfaces.calcAsas(100, 1, 0);
		interfaces.removeInterfacesBelowArea();

		List<StructureInterfaceCluster> clusters = interfaces.getClusters();

		// 22 if below 35A2 interfaces are filtered
		assertEquals(22,interfaces.size());

		// we simply want to test that some interfaces cluster together, for this entry 
		// it is problematic because of different residue numbering between different chains of same entity
		assertTrue("Expected fewer than 22 interfaces (some interfaces should cluster together)",clusters.size()<22);

		// first 2 clusters are of size 3
		assertEquals("Cluster 1 should have 3 members",3,clusters.get(0).getMembers().size());
		assertEquals("Cluster 2 should have 3 members",3,clusters.get(1).getMembers().size());

		// detection of isologous test: first 3 interfaces should be isologous

		assertTrue("Interface 1 should be isologous",interfaces.get(1).isIsologous());
		assertTrue("Interface 2 should be isologous",interfaces.get(2).isIsologous());
		assertTrue("Interface 3 should be isologous",interfaces.get(3).isIsologous());



	}
}
