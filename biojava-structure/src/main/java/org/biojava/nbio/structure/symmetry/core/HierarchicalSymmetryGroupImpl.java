package org.biojava.nbio.structure.symmetry.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a HierarchicalSymmetryGroup.
 * 
 * @author Aleix Lafita
 * @since 4.1.1
 *
 */
public class HierarchicalSymmetryGroupImpl implements HierarchicalSymmetryGroup {

	private HierarchicalSymmetryGroup parent;
	private List<HierarchicalSymmetryGroup> children;
	private SymmetryGroup group;

	public HierarchicalSymmetryGroupImpl(SymmetryGroup group,
			HierarchicalSymmetryGroup parent) {
		this.group = group;
		this.parent = parent;
		this.children = new ArrayList<HierarchicalSymmetryGroup>();
	}

	@Override
	public SymmetryGroup getSymmetryGroup() {
		return group;
	}

	@Override
	public List<HierarchicalSymmetryGroup> getChildren() {
		return children;
	}

	@Override
	public void addChild(HierarchicalSymmetryGroup child) {
		children.add(child);
	}

	@Override
	public HierarchicalSymmetryGroup getParent() {
		return parent;
	}

	@Override
	public int size() {
		return children.size();
	}

}
