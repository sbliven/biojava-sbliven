package org.biojava.nbio.structure;

import java.util.ArrayList;

/**
 * A stub StructureIdentifier, representing the full structure in all cases.
 * @author Spencer Bliven
 *
 */
public class PassthroughIdentifier implements StructureIdentifier {

	private String identifier;
	public PassthroughIdentifier(String identifier) {
		this.identifier = identifier;
	}
	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getPdbId() {
		return null;
	}

	@Override
	public SubstructureIdentifier toCanonical() {
		return new SubstructureIdentifier(null, new ArrayList<ResidueRange>());
	}

	@Override
	public Structure reduce(Structure input) throws StructureException {
		return input;
	}

}
