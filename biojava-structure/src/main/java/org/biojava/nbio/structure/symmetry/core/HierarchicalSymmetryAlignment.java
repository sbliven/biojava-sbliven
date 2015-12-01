package org.biojava.nbio.structure.symmetry.core;

import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.align.multiple.MultipleAlignment;

/**
 * Represents a hierarchical alignment of subunits.
 * 
 * The structure forms a tree, where each level of the tree stores the
 * point group symmetry of its children. Each child has an additional point
 * group relating its children, down to the leaves with C1 symmetry.
 * 
 * The point groups of the children should generally be consistent: all
 * children should have the same size and symmetry, and the operators should
 * be related by the operators of this point group. Specifically,
 * if this node's point group has operators I (identity), A and B, and child 0
 * has operators I,1,2, then child 2 should have operators A,A*1,A*2 and
 * child 3 should have B,B*1,B*2.
 * 
 * 
 * @author Spencer Bliven
 *
 */
public interface HierarchicalSymmetryAlignment {

	/**
	 * Get the point group associated with this level of symmetry
	 * @return the Point group relating the subunits aligned here
	 */
	public PointGroup getPointGroup();

	/**
	 * Get a list of all sub-alignments below this one
	 * @return
	 */
	public List<HierarchicalSymmetryAlignment> getChildren();
	
	/**
	 * 
	 * @return The parent node, or null if this is the root of the tree
	 */
	public HierarchicalSymmetryAlignment getParent();

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
