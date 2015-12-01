package org.biojava.nbio.structure.align.multiple;

import java.util.List;

import org.biojava.nbio.structure.Chain;

public interface HeirarchicalAlignment {

	/**
	 * Get the point group associated with this level of symmetry
	 * @return the Point group relating the subunits aligned here
	 */
	public PointGroupAlignment getPointGroup();

	/**
	 * Get a list of all sub-alignments below this one
	 * @return
	 */
	public List<HeirarchicalAlignment> getChildren();

	/**
	 * Equivalent to {@link #getChildren()}<tt>.size()</tt>
	 * @return The number of children
	 */
	public int size();

	/**
	 * Subunits are defined as indices into an external object, such as rows of
	 * a {@link MultipleAlignment} or elements of an array of {@link Chain}s.
	 * 
	 * This method returns a 2D rectangular list, where the first index (rows)
	 * correspond to the children and the columns give equivalent subunits.
	 * 
	 * Each row should contain the same subunits as the child's subunits
	 * concatenated together, but their order may be changed based on this
	 * alignment's point group.
	 * 
	 * @return A list of length {@link #size()}, 
	 */
	public List<List<Integer>> getAlignedSubunits();
}
