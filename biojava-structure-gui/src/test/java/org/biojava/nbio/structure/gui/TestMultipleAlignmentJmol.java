package org.biojava.nbio.structure.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.EntityInfo;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.align.gui.jmol.MultipleAlignmentJmol;
import org.biojava.nbio.structure.align.multiple.MultipleAlignment;
import org.biojava.nbio.structure.symmetry.internal.CeSymm;
import org.biojava.nbio.structure.symmetry.internal.CeSymmResult;
import org.biojava.nbio.structure.symmetry.utils.SymmetryTools;
import org.junit.Ignore;
import org.junit.Test;

public class TestMultipleAlignmentJmol {

	/**
	 * Test for biojava#817
	 * 
	 * Originally failed with a NPE if the MultipleAlignmentJmol was created.
	 * This was fixed by biojava#817, but the underlying problem remains that
	 * creating a MultipleAlignmentJmol modifies the CeSymmResult.
	 * 
	 * @throws StructureException
	 * @throws IOException
	 */
	@Test
	public void testJmolModification() throws StructureException, IOException {
		String pdb = "4i4q";
		Structure s = StructureTools.getStructure(pdb);
		assertNotNull(s);
		
		Atom[] atoms = StructureTools.getRepresentativeAtomArray(s);
		assertNotNull(atoms);
		assertEquals(146, atoms.length);
		
		CeSymmResult result = CeSymm.analyze(atoms);

		MultipleAlignment msa = result.getMultipleAlignment();
		List<Atom[]> msaAtoms = msa.getAtomArrays();
		
		// Used to throw NPE before biojava#818
		List<Structure> divided = SymmetryTools.divideStructure(result);
		assertEquals(3, divided.size());
		
		Atom[] atms = result.getAtoms();
		Atom atm1 = atms[0];
		Chain chain = atm1.getGroup().getChain();
		EntityInfo info = chain.getEntityInfo();
		assertNotNull(info);
		
		// Creating Jmol loses EntityInfo
		new MultipleAlignmentJmol(msa, msaAtoms);
		
		atms = result.getAtoms();
		atm1 = atoms[0];
		chain = atm1.getGroup().getChain();
		info = chain.getEntityInfo();
		assertNotNull(info);
	}
}
