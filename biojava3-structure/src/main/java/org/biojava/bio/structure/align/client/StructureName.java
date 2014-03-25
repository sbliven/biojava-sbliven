package org.biojava.bio.structure.align.client;


import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Matcher;

import org.biojava.bio.structure.ResidueRange;
import org.biojava.bio.structure.StructureIdentifier;
import org.biojava.bio.structure.align.util.AtomCache;
import org.biojava.bio.structure.io.util.FileDownloadUtils;


/** A utility class that makes working with names of structures, domains and ranges easier.
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
		CATH,
		FILE
	};


	Source mySource = null; 
	private  List<ResidueRange> ranges ;
	
	
	public StructureName(String name){
		if ( name.length() <  4)
			throw new IllegalArgumentException("This is not a valid StructureName:" + name);

		this.name = name;

		this.pdbId = parsePdbId();
		this.chainId = parseChainId();
		this.ranges = parseRanges();
	}

	private List<ResidueRange> parseRanges() {
		if(name == null) {
			return null;//file
		}
		return ResidueRange.parseMultiple(name);
	}

	/** PDB IDs are always returned as upper case
	 * 
	 * @return upper case PDB ID
	 */
	public String getPdbId(){

		return pdbId;
	}


	public String getChainId(){

		return chainId;
	}

	public String getName(){

		return name;
	}

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
		if ( isFile() ) {
			mySource = Source.FILE;
			return null;
		}
		else if ( isScopName() ) {
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
		if (name == null) {
			return null; //files
		}
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

	@Override
	public String getIdentifier() {
		return name;
	}

	@Override
	public List<ResidueRange> getResidueRanges() {
		return ranges;
	}

	@Override
	public List<String> getRanges() {
		return ResidueRange.toStrings(ranges);
	}

	public boolean hasRanges(){
		return (ranges != null && ranges.size() > 0);
	}
	
	public boolean isPdbId(){
		if (name.length() == 4)
			return true;
		return false;
	}

	public boolean isFile() {
		File file = new File(FileDownloadUtils.expandUserHome(name));
		return file.exists();
	}


}
