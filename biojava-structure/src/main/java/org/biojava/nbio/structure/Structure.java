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
 * Created on 26.04.2004
 * @author Andreas Prlic
 *
 */
package org.biojava.nbio.structure;

import org.biojava.nbio.structure.io.FileConvert;
import org.biojava.nbio.structure.io.PDBFileReader;

import java.util.List;
import java.util.Map;


/**
 *
 * Interface for a structure object. Provides access to the data of a PDB file.
 *
 * A structure object allows to access the PDB header information as well
 * as to the data from the ATOM records. The header information is
 * currently available through the following objects:
 * <ul>
 * <li>{@link PDBHeader}</li>
 * <li>{@link DBRef}</li>
 * <li>{@link EntityInfo}</li>
 * </ul>
 *
 * The structure object provides access to the data from the ATOM records through
 * a hierarchy of sub-object:
 * <pre>
 * Structure
 *         |
 *         {@link Chain}
 *             |
 *             {@link Group}
 *                 |
 *                 {@link Atom}
 * </pre>
 *
 * For more documentation on how to work with the Structure API please
 * see <a href="http://biojava.org/wiki/BioJava:CookBook#Protein_Structure" target="_top">
 * http://biojava.org/wiki/BioJava:CookBook#Protein_Structure</a>
 *
 * <p>
 *  The tutorial for the BioJava structure modules can be found at <a href="https://github.com/biojava/biojava3-tutorial/tree/master/structure">github</a>.
 * </p>
 *
 *
 * <hr/>
 * </hr>
 * <p>
 * Q: How can I get a Structure object from a PDB file?
 * </p>
 * <p>
 * A:
 * </p>
 * <pre>
 *  {@link Structure} loadStructure(String pathToPDBFile){
 * 		{@link PDBFileReader} pdbreader = new {@link PDBFileReader}();
 *
 * 		{@link Structure} structure = null;
 * 		try{
 * 			structure = pdbreader.getStructure(pathToPDBFile);
 * 			System.out.println(structure);
 * 		} catch (IOException e) {
 * 			e.printStackTrace();
 * 		}
 * 		return structure;
 * 	}
 *  </pre>
 *
 * <hr>
 * </hr>
 * <p>
 * Q: How can I calculate Phi and Psi angles of AminoAcids?
 * </p>
 * <p>
 * A:
 * </p>
 * <pre>
 *  void calcPhiPsi({@link Structure} structure){
 *
 *
 * 		// get the first chain from the structure
 *
 * 		{@link Chain} chain  = structure.getChain(0);
 *
 * 		// A protein chain consists of a number of groups. These can be either
 * 		// {@link AminoAcid}, {@link HetatomImpl Hetatom} or {@link NucleotideImpl Nucleotide} groups.
 * 		//
 * 		// Note: BioJava provides access to both the ATOM and SEQRES data in a PDB file.
 * 		// since we are interested in doing calculations here, we only request the groups
 * 		// from the ATOM records
 *
 * 		//  get the Groups of the chain that are AminoAcids.
 * 		List<Group> groups = chain.getAtomGroups(GroupType.AMINOACID);
 *
 * 		{@link AminoAcid} a;
 * 		{@link AminoAcid} b;
 * 		{@link AminoAcid} c ;
 *
 * 		for ( int i=0; i < groups.size(); i++){
 *
 * 			// since we requested only groups of type AMINOACID they will always be amino acids
 * 			// Nucleotide and Hetatom groups will not be present in the groups list.
 *
 * 			b = ({@link AminoAcid})groups.get(i);
 *
 * 			double phi =360.0;
 * 			double psi =360.0;
 *
 * 			if ( i > 0) {
 * 				a = ({@link AminoAcid})groups.get(i-1) ;
 * 				try {
 *
 * 					// the Calc class provides utility methods for various calculations on
 * 					// structures, groups and atoms
 *
 * 					phi = {@link Calc}.getPhi(a,b);
 * 				} catch ({@link StructureException} e){
 * 					e.printStackTrace();
 * 					phi = 360.0 ;
 * 				}
 * 			}
 * 			if ( i < groups.size()-1) {
 * 				c = ({@link AminoAcid})groups.get(i+1) ;
 * 				try {
 * 					psi = {@link Calc}.getPsi(b,c);
 * 				}catch ({@link StructureException} e){
 * 					e.printStackTrace();
 * 					psi = 360.0 ;
 * 				}
 * 			}
 *
 * 			System.out.print(b.getPDBCode() + " " + b.getPDBName() + ":"  );
 *
 * 			System.out.println(String.format("\tphi: %+7.2f psi: %+7.2f", phi, psi));
 *
 * 		}
 * </pre>
 * <hr>
 * </hr>
 *
 *
 *
 *
 * @author Andreas Prlic
 * @since 1.4
 * @version %I% %G%
 */
public interface Structure extends Cloneable {


	/**
	 * Return an identical copy of this Structure object
	 *
	 * @return identical copy of this Structure object
	 */
	 Structure clone();

	/**
	 * String representation of object.
	 */
	@Override
	String toString();

	/**
	 * Set PDB code of structure .
	 *
	 * @param pdb_id  a String specifying the PDBCode
	 * @see #getPDBCode
	 */
	void setPDBCode (String pdb_id) ;

	/**
	 * Get PDB code of structure.
	 *
	 * @return a String representing the PDBCode value
	 * @see #setPDBCode
	 */
	 String  getPDBCode () ;

	/**
	 * Set biological name of Structure .
	 *
	 * @param name  a String specifying the biological name of the Structure
	 * @see #getName
	 */
	 void setName(String name);

	/**
	 * Get biological name of Structure.
	 *
	 * @return a String representing the biological name of the Structure
	 * @see #setName
	 */
	 String getName();

	/**
	 * Get an identifier corresponding to this structure
	 * @return The StructureIdentifier used to create this structure
	 */
	 StructureIdentifier getStructureIdentifier();

	/**
	 * Set the identifier corresponding to this structure
	 * @param structureIdentifier the structureIdentifier corresponding to this structure
	 */
	 void setStructureIdentifier(StructureIdentifier structureIdentifier);

	/**
	   sets/gets an List of  Maps which corresponds to the CONECT lines in the PDB file:

	   <pre>
	   COLUMNS         DATA TYPE        FIELD           DEFINITION
	   ---------------------------------------------------------------------------------
		1 -  6         Record name      "CONECT"
		7 - 11         Integer          serial          Atom serial number
	   12 - 16         Integer          serial          Serial number of bonded atom
	   17 - 21         Integer          serial          Serial number of bonded atom
	   22 - 26         Integer          serial          Serial number of bonded atom
	   27 - 31         Integer          serial          Serial number of bonded atom
	   32 - 36         Integer          serial          Serial number of hydrogen bonded
	   atom
	   37 - 41         Integer          serial          Serial number of hydrogen bonded
	   atom
	   42 - 46         Integer          serial          Serial number of salt bridged
	   atom
	   47 - 51         Integer          serial          Serial number of hydrogen bonded
	   atom
	   52 - 56         Integer          serial          Serial number of hydrogen bonded
	   atom
	   57 - 61         Integer          serial          Serial number of salt bridged
	   atom
	   </pre>

	   the HashMap for a single CONECT line contains the following fields:

	   <li> atomserial (mandatory) : Atom serial number</li>
	   <li> bond1 .. bond4 (optional): Serial number of bonded atom</li>
	   <li> hydrogen1 .. hydrogen4 (optional):Serial number of hydrogen bonded atom</li>
	   <li> salt1 .. salt2 (optional): Serial number of salt bridged atom</li>

	   *
	   * @param connections  a List object specifying the connections
	   * @see #getConnections
	   * @deprecated use {@link Atom#addBond(Bond)} instead
	*/
	@Deprecated
	 void setConnections(List<Map<String,Integer>> connections);

	/**
	 * Return the connections value.
	 * @return a List object representing the connections value
	 * @see #setConnections
	 * @deprecated use {@link Atom#getBonds()} instead
	 */
	@Deprecated
	 List<Map<String,Integer>> getConnections();

	/**
	 * Return number of Chains in this Structure.
	 * @return an int representing the number of Chains in this Structure
	 */
	 int size() ;

	/**
	 * Return number of chains of model.
	 *
	 * @param modelnr  an int specifying the number of the Model that should be used
	 * @return an int representing the number of Chains in this Model
	 */
	 int size(int modelnr);

	/**
	 * Return the number of models .
	 * In this implementation also XRAY structures have "1 model", since
	 * model is the container for the chains.
	 * to test if a Structure is an NMR structure use {@link #isNmr()}.
	 *
	 * @return an int representing the number of models in this Structure
	 * @see #isNmr()
	 */
	 int nrModels() ;

	/**
	 * Test if this structure is an NMR structure.
	 *
	 * @return true if this Structure has been solved by NMR
	 * @see #nrModels()
	 */
	 boolean isNmr() ;

	/**
	 * Test if this structure is a crystallographic structure, i.e. it is an asymmetric unit
	 * from which it is possible to reconstruct the crystal lattice given cell parameters and
	 * space group.
	 *
	 * @return true if crystallographic, false otherwise
	 */
	 boolean isCrystallographic();

	/** set NMR flag.
	 *
	 * @param nmr  true to declare that this Structure has been solved by NMR.
	 */
	@Deprecated
	 void setNmr(boolean nmr);


	/**
	 * Add a new model.
	 *
	 * @param model  a List object containing the Chains of the new Model
	 */
	 void addModel(List<Chain> model);


	/**
	 * A convenience function if one wants to edit and replace the
	 * models in a structure. Allows to set (replace) the model at position
	 * with the new List of Chains.
	 * @param position starting at 0
	 * @param model list of chains representing a model
	 */
	 void setModel(int position, List<Chain> model);

	/**
	 * Retrieve all Chains belonging to a model .
	 * @see #getChains(int modelnr)
	 *
	 * @param modelnr  an int
	 * @return a List object containing the Chains of Model nr. modelnr

	 */
	 List<Chain> getModel(int modelnr);

	/**
	 * Retrieve all chains - if it is a NMR structure will return the chains of the first model.
	 * This is the same as getChains(0);
	 * @see #getModel(int modelnr)
	 * @see #getChains(int modelnr)
	 *
	 * @return a List object containing the Chains of Model nr. modelnr
	 */
	 List<Chain> getChains();


	/** Return all polymeric chains.
	 *
	 * @return all polymeric chains.
     */
	 List<Chain> getPolyChains();

	/** Return all non-polymeric chains.
	 *
	 * @return all nonpolymeric chains.
     */
	 List<Chain> getNonPolyChains();


	/**
	 * Set the chains of a structure, if this is a NMR structure,
	 * this will only set model 0.
	 *
	 * @see #setChains(int, List)
	 *
	 * @param chains the list of chains for this structure.
	 */
	 void setChains(List<Chain> chains);

	/**
	 * Retrieve all chains of a model.
	 * @see #getModel
	 *
	 * @param modelnr  an int
	 * @return a List object containing the Chains of Model nr. modelnr
	 */
	 List<Chain> getChains(int modelnr);

	/**
	 * Set the chains for a model
	 * @param chains the chains for a model
	 * @param modelnr the number of the model
	 */
	 void setChains( int modelnr, List<Chain> chains);

	/**
	 * Add a new chain.
	 *
	 * @param chain  a Chain object
	 */
	 void addChain(Chain chain);

	/**
	 * Add a new chain, if several models are available.
	 *
	 * @param chain    a Chain object
	 * @param modelnr  an int specifying to which model the Chain should be added
	 */
	 void addChain(Chain chain, int modelnr);

	/**
	 * Retrieve a chain by its position within the Structure .
	 *
	 * @param pos  an int for the position in the List of Chains.
	 * @return a Chain object
	*/
	 Chain getChain(int pos);

	/**
	 * Retrieve a chain by its position within the Structure and model number.
	 *
	 * @param pos      an int
	 * @param modelnr  an int
	 * @return a Chain object
	*/
	 Chain getChain( int modelnr, int pos);


	/** Get a chain by its asym ID and model number
	 *
	 * @param asymId 'private' chain ID in mmcif
	 * @param model the model nr
     * @return
     */
	Chain getChain( String asymId, int model) throws StructureException;;


	/** Get a chain by its asym ID
	 *
	 * @param asymId

	 * @return
	 */
	Chain getChain( String asymId) throws StructureException;;


	/**
	 * Request a particular chain from a structure.
	 * by default considers only the first model.
	 * @param authId name of a chain that should be returned
	 * @return Chain the requested chain
	 * @throws StructureException
	 */
	 Chain findChain(String authId)
	throws StructureException;


	/**
	 * Check if a chain with the name authId is contained in this structure.
	 *
	 * @param authId the name of the chain
	 * @return true if a chain with the name authId is found
	 */
	 boolean hasChain(String authId);

	/**
	 * Request a particular chain from a particular model
	 * @param modelnr the number of the model to use
	 * @param authId the name of a chain that should be returned
	 * @return Chain the requested chain
	 * @throws StructureException
	 */
	 Chain findChain(String authId, int modelnr)
	throws StructureException;

	/**
	 * Request a particular group from a structure.
	 * by default considers only the first model in the structure.
	 * @param authId the name of the chain to use
	 * @param pdbResnum the PDB residue number of the requested group
	 * @return Group the requested Group
	 * @throws StructureException
	 */
	  Group findGroup(String authId, String pdbResnum)
			throws StructureException;

	/**
	 * Request a particular group from a structure.
	 * considers only model nr X. count starts with 0.
	 * @param authId the chain name of the chain to use
	 * @param pdbResnum the PDB residue number of the requested group
	 * @param modelnr the number of the model to use
	 * @return Group the requested Group
	 * @throws StructureException
	 */
	   Group findGroup(String authId, String pdbResnum, int modelnr)
	 throws StructureException;


	 /**
	  * Request a chain by its PDB code
	  * by default takes only the first model
	  *
	  * @param authId the chain name
	  * @return the Chain that matches the authId
	  * @throws StructureException
	  */
	  Chain getChainByPDB(String authId)
		 throws StructureException;


	/** Retrieve a polymeric chain based on the 'internal' chain
	 * ID (asymId)
 	 * @param asymId get a polymeric chain based on its asymId (chain Id)
	 * @return a polymeric chain
	 * @throws StructureException
     */
	 Chain getPolyChain(String asymId)
			throws StructureException;

	/** Retrieve a non-polymeric chain based on the 'internal' chain
	 * ID (asymId)
	 * @param asymId get a non-polymeric chain based on its asymId (chain Id)
	 * @return a non-polymeric chain
	 * @throws StructureException
	 */
	 Chain getNonPolyChain(String asymId)
			throws StructureException;

	/** Retrieve a polymeric chain based on the 'public' chain
	 * name (authId)
	 * @param authId get a polymeric chain based on its 'public' chain name
	 * @return a polymeric chain
	 * @throws StructureException
	 */
	 Chain getPolyChainByPdb(String authId)
			throws StructureException;

	/** Retrieve a non-polymeric chain based on the 'public' chain
	 * name (authId)
	 * @param authId get a non-polymeric chain based on its public name
	 * @return a non-polymeric chain
	 * @throws StructureException
	 */
	 Chain getNonPolyChainByPdb(String authId)
			throws StructureException;

	 /**
	  * Request a chain by its PDB code
	  * by default takes only the first model
	  *
	  * @param authId the chain name
	  * @param modelnr request a particular model;
	  * @return the Chain that matches the authId in the model
	  * @throws StructureException
	  */
	  Chain getChainByPDB(String authId, int modelnr)
		 throws StructureException;


	/**
	 * Create a String that contains this Structure's contents in PDB file format.
	 *
	 * @return a String that looks like a PDB file
	 * @see FileConvert
	 */
	 String toPDB();

	/**
	 * Create a String that contains this Structure's contents in MMCIF file format.
	 * @return a String representation of the Structure object in mmCIF.
	 */
	 String toMMCIF();

	/**
	 * Set the EntityInfo
	 *
	 * @param molList list of entityinfo objects
	 */
	 void setEntityInfos(List<EntityInfo> molList);
	
	/**
	 * Get all the EntityInfo for this Structure.
	 *
	 * @return a list of EntityInfos
	 */
	 List<EntityInfo> getEntityInfos();

	/**
	 * Add an EntityInfo to this Structure
	 */
	 void addEntityInfo(EntityInfo entityInfo);

	/**
	 * Set the list of database references for this structure
	 * @param dbrefs list of DBRef objects
	 *
	 */
	 void setDBRefs(List<DBRef> dbrefs);

	/**
	 * Get the list of database references
	 *
	 * @return list of DBRef objects
	 */
	 List<DBRef> getDBRefs();

	/**
	 * Request a particular entity by its entity id (mol id in legacy PDB format)
	 *
	 * @param entityId the number of the entity
	 * @return a entityInfo
	 * @deprecated use {@link #getEntityById(int)} instead
	 */
	 EntityInfo getCompoundById(int entityId);

	/**
	 * Request a particular entity by its entity id (mol id in legacy PDB format)
	 *
	 * @param entityId the number of the entity
	 * @return an entity 
	 */	
	 EntityInfo getEntityById(int entityId);

	/**
	 * Return the header information for this PDB file
	 *
	 * @return the PDBHeader object
	 */
	 PDBHeader getPDBHeader();

	/**
	 * Return whether or not the entry has an associated journal article
	 * or ation. The JRNL section is not mandatory and thus may not be
	 * present.
	 * @return flag if a JournalArticle has been found.
	 */
	 boolean hasJournalArticle();

	/**
	 * Get the associated publication as defined by the JRNL records in a PDB
	 * file.
	 * @return a JournalArticle
	 */
	 JournalArticle getJournalArticle();

	/**
	 * Set the associated publication as defined by the JRNL records in a PDB
	 * file.
	 * @param journalArticle a JournalArticle object
	 */
	 void setJournalArticle(JournalArticle journalArticle);

	/**
	 * Get the list of disulfide Bonds as they have been defined in the PDB files
	 *
	 * @return a list of Bonds
	 */
	 List<Bond> getSSBonds();

	/**
	 * Set the list of SSBonds for this structure
	 *
	 * @param ssbonds
	 */
	 void setSSBonds(List<Bond> ssbonds);

	/**
	 * Add a single disulfide Bond to this structure
	 *
	 * @param ssbond a disulfide bond
	 */
	 void addSSBond(Bond ssbond);

	/**
	 * Set the the header information for this PDB file
	 *
	 * @param header the PDBHeader object
	 */
	 void setPDBHeader(PDBHeader header);

	/**
	 * Get the ID used by Hibernate
	 *
	 * @return the ID used by Hibernate
	 */
	 Long getId() ;

	/** set the ID used by Hibernate
	 *
	 * @param id the id
	 */
	 void setId(Long id) ;

	/**
	 * @param sites the sites to set in the structure
	 */
	 void setSites(List<Site> sites);

	/**
	 * @return the sites contained in this structure
	 */
	 List<Site> getSites();

	 List<Group> getHetGroups();

	/**
	 * Set a flag to indicate if this structure is a biological assembly
	 * @param biologicalAssembly true if biological assembly, otherwise false
	 * @since 3.2
	 */
	 void setBiologicalAssembly(boolean biologicalAssembly);

	/**
	 * Get flag that indicates if this structure is a biological assembly
	 * @return  true if biological assembly, otherwise false
	 * @since 3.2
	 */
	 boolean isBiologicalAssembly();

	/**
	 * Set crystallographic information for this structure
	 * @param crystallographicInfo crystallographic information
	 * @since 3.2
	 */

	 void setCrystallographicInfo(PDBCrystallographicInfo crystallographicInfo);

	/**
	 * Get crystallographic information for this structure
	 * @return PDBCrystallographicInfo crystallographic information
	 * @since 3.2
	 */
	 PDBCrystallographicInfo getCrystallographicInfo();

	/**
	 * Resets all models of this Structure
	 * @since 4.0.1
	 */
	 void resetModels();

	/**
	 * Returns the PDB identifier associated with this StructureIdentifier.
	 * @deprecated From BioJava 4.2, use {@link #getPDBCode()} or
	 *  <code>getStructureIdentifier().toCanonical().getPdbId()</code>
	 */
	@Deprecated
	String getPdbId();

	/**
	 * Returns the list of {@link ResidueRange ResidueRanges} that this StructureIdentifier defines.
	 * This is a unique representation.
	 * @deprecated From BioJava 4.2, use
	 *  <code>getStructureIdentifier().toCanonical().getResidueRanges()</code>
	 */
	@Deprecated
	List<? extends ResidueRange> getResidueRanges();

	/**
	 * Returns a list of residue ranges. For example:
	 * <pre>
	 * getRanges().get(0): 'A'
	 * getRanges().get(1): 'B_5-100'
	 * </pre>
	 * This is a unique representation.
	 * @deprecated From BioJava 4.2, use
	 *  <code>getStructureIdentifier().toCanonical().getRanges()</code>
	 */
	@Deprecated
	List<String> getRanges();

	/**
	 * Get a string representing this structure's contents. The following places
	 * are searched for a non-null value, with the first being returned:
	 * <ol>
	 * <li>{@link #getStructureIdentifier()}.getIdentifier(), which should give
	 *     the string originally used to create the structure
	 * <li>{@link #getName()}
	 * <li>A combination of {@link #getPDBCode()} with a heuristic description
	 *     of the residue ranges, in {@link SubstructureIdentifier} format.
	 * </ol>
	 * @return A {@link SubstructureIdentifier}-format string describing the residue ranges in this structure
	 * @since The behavior of this method changed in BioJava 4.2. Previously it
	 *  returned the same value as {@link #getPDBCode()}
	 */
	String getIdentifier();
}
