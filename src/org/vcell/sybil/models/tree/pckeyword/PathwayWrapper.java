package org.vcell.sybil.models.tree.pckeyword;

/*   PathwayWrapper  --- by Oliver Ruebenacker, UCHC --- December 2009
 *   Wrapper for a tree node with a (Pathway Commons) Pathway
 */

import org.vcell.sybil.models.tree.NodeDataWrapper;
import org.vcell.sybil.util.http.pathwaycommons.search.DataSource;
import org.vcell.sybil.util.http.pathwaycommons.search.Pathway;

public class PathwayWrapper extends NodeDataWrapper<Pathway> {

	public PathwayWrapper(Pathway pathways) {
		super(pathways);
	}

	public Pathway data() { return (Pathway) super.data(); }
	public Pathway pathway() { return (Pathway) super.data(); }
	
	public String toString() {
		String primaryId = pathway().primaryId();
		DataSource dataSource = pathway().dataSource();
//wei		String dataSourceText = "";
//wei		if(dataSource != null) {
//wei			dataSourceText = ", " + dataSource.name();
//wei		}
		return primaryId + ":  " + pathway().name() + "  [" + dataSource.name() + "]"; //wei's code
	}
	
}
