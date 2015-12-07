package org.biojava.nbio.structure.symmetry.core;

/**
 * Point groups include cyclic (e.g. "C2"), dihedral (e.g. "D3"), and polyhedral
 * (e.g. "I") symmetry. They form an algebraic group over a set of linear
 * transformation operations. Applying these operations to a point will result
 * in a set of equivalent points. Since it is a point group, there exists a
 * point (often the origin, but not neccessarily so) such that all operations
 * will leave this point unchanged. The operations are equivalent to rotations
 * around an axis by a rational fraction of PI.
 * <p>
 * The PointGroup class is closely related to {@link RotationGroup}, but tries
 * to abstract out the algebraic group from the chains being aligned.
 * 
 * @author Spencer Bliven
 * @since 4.1.1
 * @see SymmetryGroup
 *
 */
public interface PointGroup extends SymmetryGroup {

}
