package org.biojava.nbio.structure.symmetry.core;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Matrix4d;

/**
 * The OpenSymmetryGroup stores the SymmetryGroup information for non closed
 * symmetries (those not forming a point group). This includes helical and
 * translational symmetries.
 * 
 * @author Aleix Lafita
 * @since 4.1.1
 *
 */
public class OpenSymmetryGroup implements SymmetryGroup {

	private List<Matrix4d> generators;
	private List<Matrix4d> operators;
	private List<List<Integer>> operatorFactors;
	private List<List<Integer>> permutations;
	private String symmetry = "R"; // R stands for repeats

	/**
	 * The constructor build automatically all the variables from the generator
	 * matrix and the permutations.
	 * 
	 * @param generator
	 * @param permutations
	 */
	public OpenSymmetryGroup(Matrix4d generator,
			List<List<Integer>> permutations) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getOrder() {
		return operators.size();
	}

	@Override
	public List<Matrix4d> getGenerators() {
		return generators;
	}

	@Override
	public void setGenerator(int i, Matrix4d gen) {
		generators.set(i, gen);
	}

	@Override
	public List<Matrix4d> getOperators() {
		return operators;
	}

	@Override
	public Matrix4d getOperator(int i) {
		return operators.get(i);
	}

	@Override
	public List<List<Integer>> getOperatorFactors() {
		return operatorFactors;
	}

	@Override
	public int getOperatorOrder(int i) {
		// Does not make sense for non-closed groups
		return 0;
	}

	@Override
	public String getSymmetryString() {
		return symmetry + getOrder();
	}

	@Override
	public List<List<List<Integer>>> getAlignedSubunits() {
		
		List<List<List<Integer>>> alignedSubunits = new ArrayList<List<List<Integer>>>();
		for (int i = 0; i < permutations.size(); i++) {
			alignedSubunits.add(getAlignedSubunits(i));
		}
		return alignedSubunits;
	}

	@Override
	public List<List<Integer>> getAlignedSubunits(int i) {
		List<List<Integer>> aligned = new ArrayList<List<Integer>>();
		aligned.add(permutations.get(0).subList(0, permutations.get(i).size()));
		aligned.add(permutations.get(i));
		return aligned;
	}

}
