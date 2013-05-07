/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on Nov 5, 2009
 * Author: Andreas Prlic 
 *
 */

package demo;

import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureTools;
import org.biojava.bio.structure.align.StructureAlignmentFactory;
import org.biojava.bio.structure.align.ce.CeMain;
import org.biojava.bio.structure.align.ce.CeParameters;
import org.biojava.bio.structure.align.client.FarmJobParameters;
import org.biojava.bio.structure.align.client.JFatCatClient;
import org.biojava.bio.structure.align.model.AFPChain;
import org.biojava.bio.structure.align.util.AtomCache;

public class CPSearch implements Runnable {
	public static Logger logger =  Logger.getLogger("org.biojava");

	AtomicBoolean interrupted ;

	Structure structure1;
	String name1;

	CeMain ce, ceCP;

	AtomCache cache;

	Writer out;
	
	//List<AlignTimes> times;
	
	public class AlignTimes {
		String name;
		long ceTime;
		long ceCPTime;
		public AlignTimes(String name, long ceTime, long ceCPTime ) {
			this.name=name;
			this.ceTime=ceTime;
			this.ceCPTime=ceCPTime;
		}
		public String toString() {
			return String.format("%s\t%d\t%d",name,ceTime,ceCPTime);
		}
	}

	public CPSearch( Structure s1, String name1, AtomCache cache, String outFile ) throws StructureException, IOException {

		ce = (CeMain) StructureAlignmentFactory.getAlgorithm(CeMain.algorithmName);
		CeParameters params = (CeParameters) ce.getParameters();
		params.setMaxGapSize(0);
		params.setCheckCircular(false);

		ceCP = (CeMain) StructureAlignmentFactory.getAlgorithm(CeMain.algorithmName);
		params = (CeParameters) ceCP.getParameters();
		params.setMaxGapSize(0);
		params.setCheckCircular(true);

		structure1 = s1;
		this.cache = cache;
		this.name1 = name1;

		interrupted = new AtomicBoolean(false);

		out = (new FileWriter(outFile));
		//times = new LinkedList<AlignTimes>();
	}

	public static void main(String[] args) {
		String usage = "usage: CPSearch queryID outFile cacheLocation";
		if(args.length!=3) {
			System.err.println(usage+"\nError:Expected 3 arguments.");
		}

		String name1 = args[0];
		String outFile = args[1];

		AtomCache cache = new AtomCache(args[2], true);
		Structure s1;
		try {
			s1 = cache.getStructure(name1);

			CPSearch search = new CPSearch(s1,name1,cache,outFile);

			/*Thread t = new Thread(search);
			t.start();*/
			search.run(); //main thread
		} catch (StructureException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	public List<AlignTimes> getTimes() {
		return times;
	}*/
	public void run() {
		System.out.println("running CPSearch.");

		String serverLocation = FarmJobParameters.DEFAULT_SERVER_URL;

		SortedSet<String> representatives = JFatCatClient.getRepresentatives(serverLocation,40);
		System.out.format("Comparing %s to %d representatives\n", name1, representatives.size() );

		try {
			out.write("Name1\tName2\tTime1\tCPTime1\tTime2\tCPTime2\n");
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		Atom[] ca1;
		ca1 = StructureTools.getAtomCAArray(structure1);

		for (String repre : new String[] {"1tim.A"} ){
			if ( interrupted.get()) {
				System.err.println("User interrupted alignments.");
				break;
			}
			try {
				//Structure structure2 = cache.getStructure(repre);

				Atom[] ca2;

				ca2 = cache.getAtoms(repre,true);
//				ca2 = StructureTools.getAtomCAArray(structure2);

				AFPChain afpChain;

				long t1 = System.currentTimeMillis();
				afpChain = ce.align(ca1, StructureTools.cloneCAArray(ca2));
				long t2 = System.currentTimeMillis();
				afpChain = ce.align(ca1, StructureTools.cloneCAArray(ca2));
				long t3 = System.currentTimeMillis();
				afpChain = ceCP.align(ca1, StructureTools.cloneCAArray(ca2));
				long t4 = System.currentTimeMillis();
				afpChain = ceCP.align(ca1, StructureTools.cloneCAArray(ca2));
				long t5 = System.currentTimeMillis();
			
				//AlignTimes times = new AlignTimes( repre , t2-t1, t3-t2);
				//this.times.add(times);
				out.write(String.format("%s\t%s\t%d\t%d\t%d\t%d\n",name1,repre,t2-t1,t3-t2,t4-t3,t5-t4));
				System.out.print(String.format("%s\t%s\t%d\t%d\t%d\t%d\n",name1,repre,t2-t1,t3-t2,t4-t3,t5-t4));
				
				out.flush();
			} catch ( IOException e){
				e.printStackTrace();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** stops what is currently happening and does not continue
	 * 
	 *
	 */
	public void interrupt() {
		interrupted.set(true);
	}

}
