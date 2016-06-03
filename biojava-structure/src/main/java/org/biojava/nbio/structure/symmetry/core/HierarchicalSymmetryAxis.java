package org.biojava.nbio.structure.symmetry.core;

import java.util.List;

import javax.vecmath.Matrix4d;

public class HierarchicalSymmetryAxis<R> {

	private RepeatAlignment<R> alignment;
	private Matrix4d operator;
	private HierarchicalSymmetryAxis<R> parent;
	private List<HierarchicalSymmetryAxis<R>> children;
	
	/**
	 * Returns an operator which superimposes a repeat onto the representative position.
	 * For repeats in the first row of the RepeatAlignment, this will be the
	 * Identity matrix; repeats further down the RepeatAlignment will return
	 * powers of {@link #getOperator()}; repeats not directly specified in
	 * the RepeatAlignment will include a component from higher levels of
	 * axes.
	 * @param repeat
	 * @return
	 */
	public Matrix4d getSuperpositionOperator(R repeat) {
		int row = alignment.rowIndexOf(repeat);
		return null;//TODO
	}
	
	/**
	 * Get the representative for a repeat; that is, an equivalent repeat
	 * for whom {@link #getSuperpositionOperator(Object)} will return the
	 * identity operator.
	 * @param repeat
	 * @return
	 */
	public R getRepresentative(R repeat) {
		R repr = alignment.getRepresentative(repeat);
		if(repr != null) {
			return repr;
		}
		return null;//TODO
	}

	/**
	 * @return the alignment
	 */
	public RepeatAlignment<R> getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(RepeatAlignment<R> alignment) {
		this.alignment = alignment;
	}

	/**
	 * @return the operator
	 */
	public Matrix4d getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Matrix4d operator) {
		this.operator = operator;
	}

	/**
	 * @return the parent
	 */
	public HierarchicalSymmetryAxis<R> getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(HierarchicalSymmetryAxis<R> parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	public List<HierarchicalSymmetryAxis<R>> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<HierarchicalSymmetryAxis<R>> children) {
		this.children = children;
	}
	
	
}
