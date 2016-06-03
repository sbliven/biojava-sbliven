package org.biojava.nbio.structure.symmetry.core;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.biojava.nbio.structure.symmetry.internal.CESymmParameters.SymmetryType;

/**
 * Encapsulates which repeats of a structure are aligned at a particular
 * level of the symmetry hierarchy. This can be thought of as an alignment
 * matrix, where each column of the matrix gives equivalent repeats.
 * 
 * Any type can be used for the repeats, with the most common being Integer
 * indices into some external array.
 * @author Spencer Bliven, Aleix Lafita
 *
 * @param <R> Type of the repeats, typically Integer
 */
public class RepeatAlignment<R> {
	private final List<List<R>> alignment;
	private final Set<R> elements;
	private final SymmetryType symmType;
	
	/**
	 * Construct a new repeat alignment. Elements should be unique.
	 * They are stored such that <tt>alignment.get(column)</tt> gives a list of
	 * <i>order</i> aligned repeats for all columns.
	 * @param alignment Alignment matrix. All elements should be unique.
	 * @param symmType Whether the alignment is cyclic or not
	 */
	public RepeatAlignment(List<List<R>> alignment, SymmetryType symmType) {
		super();
		this.alignment = alignment;
		this.symmType = symmType;
		this.elements = new TreeSet<>();
		if(alignment.size() < 1) {
			throw new IllegalArgumentException("Empty alignment");
		}
		Iterator<List<R>> it = alignment.iterator();
		List<R> column = it.next();
		int order = column.size();
		elements.addAll(column);
		while(it.hasNext()) {
			column = it.next();
			if( order != column.size() ) {
				throw new IllegalArgumentException("Non-rectangular alignment");
			}
			elements.addAll(column);
		}
		if(elements.size() != order*alignment.size()) {
			throw new IllegalArgumentException("Non-unique elements");
		}
	}
	/**
	 * Number of rows in the alignment. Equivalently, the number of times the
	 * alignment operation can be applied before running out of repeats (open symm)
	 * or wrapping back to the first repeat (closed symm).
	 * @return The order of this group
	 */
	public int getOrder() {
		return alignment.get(0).size();
	}
	/**
	 * Number of columns in the alignment. Equivalently, the number of repeats
	 * per unit in this level of symmetry
	 * @return
	 */
	public int size() {
		return alignment.size();
	}
	/**
	 * @return the alignment
	 */
	public List<List<R>> getAlignment() {
		return alignment;
	}
	/**
	 * Indicates Whether the alignment is cyclic (CLOSED) or not (OPEN)
	 * @return the symmType
	 */
	public SymmetryType getSymmType() {
		return symmType;
	}
	
	/**
	 * Gives the row index of a particular repeat
	 * @param repeat Repeat to search for
	 * @return Row index, or -1 if this alignment doesn't contain the repeat
	 */
	public int rowIndexOf(R repeat) {
		for( List<R> column : alignment ) {
			int index = column.indexOf(repeat);
			if(index >= 0) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * Get the representative for a repeat; that is, the first repeat in the
	 * same column
	 * @param repeat Query repeat
	 * @return The repeat at the top of the same column, or null if this alignment doesn't contain the repeat.
	 */
	public R getRepresentative(R repeat) {
		for( List<R> column : alignment ) {
			if(column.contains(repeat)) {
				return column.get(0);
			}
		}
		return null;
	}
	
	/**
	 * Checks whether a particular element is included in this alignment
	 * @param repeat
	 * @return
	 */
	public boolean contains(R repeat) {
		return elements.contains(repeat);
	}
}
