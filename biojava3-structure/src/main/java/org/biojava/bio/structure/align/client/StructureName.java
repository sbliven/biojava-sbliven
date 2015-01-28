package org.biojava.bio.structure.align.client;


import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Matcher;

import org.biojava.bio.structure.ResidueRange;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.StructureIdentifier;
import org.biojava.bio.structure.SubstructureIdentifier;
import org.biojava.bio.structure.align.util.AtomCache;
import org.biojava.bio.structure.cath.CathDomain;
import org.biojava.bio.structure.cath.CathFactory;
import org.biojava.bio.structure.scop.ScopDomain;
import org.biojava.bio.structure.scop.ScopFactory;


/** 
 * A utility class that makes working with names of structures, domains and ranges easier.
 * 
 * Accepts a wide range of identifier formats, including {@link ScopDomain},
 * {@link CathDomain}, PDP domains, and {@link SubstructureIdentifier} residue
 * ranges.
 * 
 * Where possible, data is extracted from the input string. Otherwise, range 
 * information may be loaded from one of the factory classes:
 * {@link CathFactory},{@link ScopFactory}, etc.
 * 
 * @param name the name. e.g. 4hhb, 4hhb.A, d4hhba_, PDP:4HHBAa etc.
 */
public class StructureName implements Comparable<StructureName>, Serializable, StructureIdentifier{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4021229518711762954L;
	protected String name;
	protected String pdbId;
	protected String chainId;

	String cathPattern = "[0-9][a-z0-9][a-z0-9][a-z0-9].[0-9][0-9]";


	private enum Source {
		PDB,
		SCOP,
		PDP,
		CATH
	};


	Source mySource = null; 
	private  List<ResidueRange> ranges ;
	
	
	public StructureName(String name){
		if ( name.length() <  4)
			throw new IllegalArgumentException("This is not a valid StructureName:" + name);

		this.name = name;

		this.pdbId = parsePdbId();

		this.chainId = parseChainId();
	}

	/** PDB IDs are always returned as upper case
	 * 
	 * @return upper case PDB ID
	 */
	@Override
	public String getPdbId(){

		return pdbId;
	}


	public String getChainId(){

		return chainId;
	}

	public String getName(){

		return name;
	}

	@Override
	public String toString(){
		StringWriter s = new StringWriter();

		s.append(name);

		s.append(" PDB ID: ");
		s.append(getPdbId());

		if ( isScopName()) {
			s.append(" is a SCOP name");
		}

		String chainID= getChainId();
		if ( chainID != null) {
			s.append(" has chain ID: ");
			s.append(chainID);

		}

		if ( isPDPDomain())
			s.append(" is a PDP domain");

		return s.toString();

	}

	public boolean isScopName() {
		if (name.startsWith("d") && name.length() >6)		
			return true;
		return false;
	}



	public boolean hasChainID(){
		//return name.contains(AtomCache.CHAIN_SPLIT_SYMBOL);


		if ( chainId != null)
			return true;
		return false;
	}

	public boolean isPDPDomain(){
		return name.startsWith(AtomCache.PDP_DOMAIN_IDENTIFIER);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StructureName other = (StructureName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(StructureName o) {
		if ( this.equals(o))
			return 0;
		if ( o.getPdbId() == null)
			return -1;
		if ( this.getPdbId() == null)
			return 1;

		if ( ! o.getPdbId().equals(this.getPdbId())){
			return this.getPdbId().compareTo(o.getPdbId());
		}

		return this.getName().compareTo(o.getName());

	}

	private String parsePdbId(){
		if ( isScopName() ) {
			mySource = Source.SCOP;
			return name.substring(1,5).toUpperCase();
		}
		else if ( name.startsWith(AtomCache.PDP_DOMAIN_IDENTIFIER)){
			// starts with PDP:
			// eg: PDP:3LGFAa
			mySource = Source.PDP;
			return name.substring(4,8).toUpperCase();
		} else  if ( isCathID()){
			mySource = Source.CATH;
			return name.substring(0,4);
		} else  {
			mySource = Source.PDB;
			// all other names start with PDB id
			return name.substring(0,4).toUpperCase();
		}

	}


	private String parseChainId(){
		//TODO Support multi-character chainIds -sbliven 2015-01-28
		if (name.length() == 6){
			// name is PDB.CHAINID style (e.g. 4hhb.A)


			if ( name.substring(4,5).equals(AtomCache.CHAIN_SPLIT_SYMBOL)) {
				return name.substring(5,6);
			}
		}  else if ( isCathID()){
			return name.substring(4,5);
		} else  if ( name.startsWith("d")){



			Matcher scopMatch = AtomCache.scopIDregex.matcher(name);
			if( scopMatch.matches() ) {
				//String pdbID = scopMatch.group(1);
				String chainID = scopMatch.group(2);
				//String domainID = scopMatch.group(3);
				// unfortunately SCOP chain IDS are lowercase!
				return chainID.toUpperCase();
			}


		} else if ( name.startsWith(AtomCache.PDP_DOMAIN_IDENTIFIER)){
			// eg. PDP:4HHBAa
			String chainID = name.substring(8,9);
			//System.out.println("chain " + chainID + " for " + name);
			return chainID;
		}

		return null;
	}

	public boolean isCathID(){

		if ( name.length() != 7 )
			return false;

		return name.matches(cathPattern);
	}

	public boolean hasRanges(){
		return (ranges != null && ranges.size() > 0);
	}
	
	public boolean isPdbId(){
		if (name.length() == 4)
			return true;
		return false;
	}

	@Override
	public String getIdentifier() {
		return getName();
	}

	private StructureIdentifier realize() {
		switch(mySource) {
		case CATH:
			return CathFactory.getCathDatabase().getDescriptionByCathId(getIdentifier());
		case SCOP:
			return ScopFactory.getSCOP().getDomainByScopID(getIdentifier());
		case PDP:
			//TODO -sbliven 2015-01-28
			//PDPProvider provider = new RemotePDPProvider(false);
		case PDB:
		default:
			return new SubstructureIdentifier(getIdentifier());
		}
	}

	@Override
	public SubstructureIdentifier toCanonical() {
		return realize().toCanonical();
	}

	@Override
	public Structure reduce(Structure input) throws StructureException {
		return realize().reduce(input);
	}


}
