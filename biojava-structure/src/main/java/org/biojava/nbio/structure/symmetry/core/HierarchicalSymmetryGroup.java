package org.biojava.nbio.structure.symmetry.core;

import java.util.List;

/**
 * Represents a hierarchical tree of symmetry groups applied to the same set of
 * subunits.
 * <p>
 * The structure forms a tree, where each level of the tree stores the point
 * group symmetry of its children. Each child has an additional point group
 * relating its children, down to the leaves with C1 symmetry.
 * <p>
 * The point groups of the children should generally be consistent: all children
 * should have the same size and symmetry, and the operators should be related
 * by the operators of this point group. Specifically, if this node's point
 * group has operators I (identity), A and B, and child 0 has operators I,1,2,
 * then child 2 should have operators A,A*1,A*2 and child 3 should have
 * B,B*1,B*2.
 * 
 * @author Spencer Bliven
 * @author Aleix Lafita
 * @since 4.1.1
 *
 */
public interface HierarchicalSymmetryGroup {

	/**
	 * Get the SymmetryGroup associated with this level of symmetry
	 * 
	 * @return the Group relating the subunits aligned here
	 */
	public SymmetryGroup getSymmetryGroup();

	/**
	 * Get a List of all sub-groups below this one
	 * 
	 * @return
	 */
	public List<HierarchicalSymmetryGroup> getChildren();

	/**
	 * Add a new SymmetryGroup as a child of this node
	 * 
	 * @return
	 */
	public void addChild(HierarchicalSymmetryGroup child);

	/**
	 * Get the Parent SymmetryGroup of this node
	 * 
	 * @return The parent node, or null if this is the root of the tree
	 */
	public HierarchicalSymmetryGroup getParent();

	/**
	 * Equivalent to {@link #getChildren()}<tt>.size()</tt>
	 * 
	 * @return The number of children
	 */
	public int size();

}
