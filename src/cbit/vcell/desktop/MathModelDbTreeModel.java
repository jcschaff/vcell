package cbit.vcell.desktop;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import cbit.vcell.mathmodel.*;
import java.util.Vector;
import cbit.vcell.solver.SolverResultSetInfo;
import cbit.vcell.solver.SimulationInfo;
import java.util.Enumeration;
import cbit.vcell.biomodel.BioModelInfo;
import cbit.vcell.clientdb.DatabaseListener;
import javax.swing.tree.DefaultTreeModel;

import org.vcell.util.DataAccessException;
import org.vcell.util.document.User;

import cbit.vcell.clientdb.DocumentManager;
import cbit.vcell.biomodel.BioModelMetaData;
/**
 * Insert the type's description here.
 * Creation date: (2/14/01 3:33:23 PM)
 * @author: Jim Schaff
 */
public class MathModelDbTreeModel extends javax.swing.tree.DefaultTreeModel implements cbit.vcell.clientdb.DatabaseListener {
	private boolean fieldLatestOnly = false;
	protected transient java.beans.PropertyChangeSupport propertyChange;
	private cbit.vcell.clientdb.DocumentManager fieldDocumentManager = null;

/**
 * BioModelDbTreeModel constructor comment.
 * @param root javax.swing.tree.TreeNode
 */
public MathModelDbTreeModel() {
	super(new BioModelNode("empty",false),true);
}


/**
 * The addPropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().addPropertyChangeListener(listener);
}


/**
 * The addPropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
	getPropertyChange().addPropertyChangeListener(propertyName, listener);
}


/**
 * Insert the method's description here.
 * Creation date: (11/28/00 1:06:51 PM)
 * @return cbit.vcell.desktop.BioModelNode
 * @param docManager cbit.vcell.clientdb.DocumentManager
 */
private BioModelNode createBaseTree() throws DataAccessException {
	MathModelInfo mathModelInfos[] = getDocumentManager().getMathModelInfos();
	BioModelNode rootRootNode = new BioModelNode("math models",true);
	//
	// get list of users (owners)
	//
	Vector ownerList = new Vector();
	ownerList.addElement(getDocumentManager().getUser());
	for (int i=0;i<mathModelInfos.length;i++){
		MathModelInfo mathModelInfo = mathModelInfos[i];
		if (!ownerList.contains(mathModelInfo.getVersion().getOwner())){
			ownerList.addElement(mathModelInfo.getVersion().getOwner());
		}
	}
	//
	// for each user
	//
	java.util.TreeMap treeMap = new java.util.TreeMap();
	for (int ownerIndex=0;ownerIndex<ownerList.size();ownerIndex++){
		User owner = (User)ownerList.elementAt(ownerIndex);
		BioModelNode ownerNode = createOwnerSubTree(owner);
		if(owner.equals(getDocumentManager().getUser()) || ownerNode.getChildCount() > 0){
			treeMap.put(owner.getName().toLowerCase(),ownerNode);
		}
	}
	//
	rootRootNode.add((BioModelNode)treeMap.remove(getDocumentManager().getUser().getName().toLowerCase()));
	BioModelNode otherUsersNode = new BioModelNode("Shared Models",true);
	rootRootNode.add(otherUsersNode);
	Object[] bmnArr = treeMap.values().toArray();
	for(int i = 0; i < bmnArr.length;i+= 1){
		otherUsersNode.add((BioModelNode)bmnArr[i]);
	}
	return rootRootNode;
}


/**
 * Insert the method's description here.
 * Creation date: (11/28/00 1:06:51 PM)
 * @return cbit.vcell.desktop.BioModelNode
 * @param docManager cbit.vcell.clientdb.DocumentManager
 */
private BioModelNode createOwnerSubTree(User owner) throws DataAccessException {
	MathModelInfo mathModelInfos[] = getDocumentManager().getMathModelInfos();
	//
	// for each user
	//
	BioModelNode rootNode = new BioModelNode(owner,true);
	for (int i=0;i<mathModelInfos.length;i++){
		MathModelInfo mathModelInfo = mathModelInfos[i];
		if (mathModelInfo.getVersion().getOwner().equals(owner)){
			BioModelNode bioModelNode = new BioModelNode(mathModelInfo.getVersion().getName(),true);
			rootNode.add(bioModelNode);
			//
			// get list of bioModels with the same branch
			//
			Vector mathModelBranchList = new Vector();
			mathModelBranchList.addElement(mathModelInfo);
			for (i=i+1;i<mathModelInfos.length;i++){
				if (mathModelInfos[i].getVersion().getBranchID().equals(mathModelInfo.getVersion().getBranchID())){
					mathModelBranchList.add(0,mathModelInfos[i]);
				}else{
					i--;
					break;
				}
			}
			MathModelInfo mathModelInfosInBranch[] = null;
			if (getLatestOnly()){
				mathModelInfosInBranch = new MathModelInfo[1];
				mathModelInfosInBranch[0] = (MathModelInfo)mathModelBranchList.elementAt(0);
			}else{
				mathModelInfosInBranch = new MathModelInfo[mathModelBranchList.size()];
				mathModelBranchList.copyInto(mathModelInfosInBranch);
			}
			for (int versionCount=0;versionCount<mathModelInfosInBranch.length;versionCount++){
				bioModelNode.add(createVersionSubTree(mathModelInfosInBranch[versionCount]));
			}
		}
	}
	return rootNode;
}


/**
 * Insert the method's description here.
 * Creation date: (11/28/00 2:41:43 PM)
 * @param bioModelNode cbit.vcell.desktop.BioModelNode
 * @param bioModelInfo cbit.vcell.biomodel.BioModelInfo
 */
private BioModelNode createVersionSubTree(MathModelInfo mmInfo) throws DataAccessException {
	//MathModelMetaData mathModelMetaData = getDocumentManager().getMathModelMetaData(mmInfo);
	//if (mathModelMetaData==null){
		//return null;
	//}
	BioModelNode versionNode = new BioModelNode(mmInfo,false);
	////
	//// add children of the BioModel to the node passed in
	////
	//if (mathModelMetaData.getVersion().getAnnot()!=null && mathModelMetaData.getVersion().getAnnot().length()>0){
		//versionNode.add(new BioModelNode(new Annotation(mathModelMetaData.getVersion().getAnnot()),false));
	//}

	////
	//// add simulations to mathModel
	////
	//Enumeration simEnum = mathModelMetaData.getSimulationInfos();
	//while (simEnum.hasMoreElements()){
		//SimulationInfo simInfo = (SimulationInfo)simEnum.nextElement();
		//BioModelNode simNode = new BioModelNode(simInfo,true);
		//versionNode.add(simNode);
		//if (simInfo.getVersion().getAnnot()!=null && simInfo.getVersion().getAnnot().length()>0){
			//simNode.add(new BioModelNode(new Annotation(simInfo.getVersion().getAnnot()),false));
		//}
		////
		//// add resultSet (optional) to simulation
		////
		//Enumeration rsEnum = mathModelMetaData.getResultSetInfos();
		//while (rsEnum.hasMoreElements()){
			//SolverResultSetInfo rsInfo = (SolverResultSetInfo)rsEnum.nextElement();
			//if (rsInfo.getSimulationInfo().getVersion().getVersionKey().equals(simInfo.getVersion().getVersionKey())){
				//BioModelNode rsNode = new BioModelNode(rsInfo,false);
				//simNode.add(rsNode);
			//}
		//}
	//}
	return versionNode;
}


/**
 * 
 * @param event cbit.vcell.clientdb.DatabaseEvent
 */
public void databaseDelete(cbit.vcell.clientdb.DatabaseEvent databaseEvent) {
	if (databaseEvent.getOldVersionInfo() instanceof MathModelInfo){
		MathModelInfo removedMathModelInfo = (MathModelInfo)databaseEvent.getOldVersionInfo();
		BioModelNode removedNode = ((BioModelNode)getRoot()).findNodeByUserObject(removedMathModelInfo);
		if (removedNode.getParent()!=null && removedNode.getSiblingCount()==1){ // just this one version
			removedNode = (BioModelNode)removedNode.getParent();
		}
		removeNodeFromParent(removedNode);
	}
}


/**
 * 
 * @param event cbit.vcell.clientdb.DatabaseEvent
 */
public void databaseInsert(cbit.vcell.clientdb.DatabaseEvent databaseEvent) {
	if (databaseEvent.getNewVersionInfo() instanceof MathModelInfo){
		try {
			MathModelInfo insertedMathModelInfo = (MathModelInfo)databaseEvent.getNewVersionInfo();
			//
			// get parent of updated version
			//
			//   model1                           (String)
			//      Fri Sept 2, 2001 12:00:00     (MathModelInfo)
			//      Fri Sept 1, 2001 10:00:00     (MathModelInfo)
			//
			//
			BioModelNode newVersionNode = createVersionSubTree(insertedMathModelInfo);
			//
			// find owner node (if it is displayed)
			//
			User owner = insertedMathModelInfo.getVersion().getOwner();
			BioModelNode ownerRoot = ((BioModelNode)getRoot()).findNodeByUserObject(owner);
			BioModelNode parentNode = null;
			if (ownerRoot!=null){
				parentNode = ownerRoot.findNodeByUserObject(insertedMathModelInfo.getVersion().getName());
			}
			if (parentNode==null){
				//
				// fresh insert 
				// Have to create parent node, for all versions of this mathModel, 
				// and stick it in the correct order in the tree.
				//
				parentNode = new BioModelNode(insertedMathModelInfo.getVersion().getName(),true);
				parentNode.insert(newVersionNode,0);
				//
				// if owner node exists, add MathModel parent and fire events to notify of the insertion
				//
				if (ownerRoot!=null){
					ownerRoot.insert(parentNode,0);  // !!!!!!!!!!!!!! new insert on top (index=0) .... should do insertion sort !!!!
					insertNodeInto(parentNode,ownerRoot,0);
				}
			}else{
				//
				// already versions there (just add child in the correct position within parent)
				//
				parentNode.insert(newVersionNode,0); // !!!!!!!!!! right now ignore order !!!!!!!!!!
				insertNodeInto(newVersionNode,parentNode,0);
			}
		} catch (DataAccessException e){
			e.printStackTrace(System.out);
			System.out.println("exception responding to databaseInsert(), refreshing whole tree");
			refreshTree();
		}
	}
}


/**
 * 
 * @param event cbit.vcell.clientdb.DatabaseEvent
 */
public void databaseRefresh(cbit.vcell.clientdb.DatabaseEvent event) {

	//Our parent will tell us what to do
}


/**
 * 
 * @param event cbit.vcell.clientdb.DatabaseEvent
 */
public void databaseUpdate(cbit.vcell.clientdb.DatabaseEvent databaseEvent) {
	//
	// the ClientDocumentManager usually throws an UPDATE when changing public/private status
	//
	if (databaseEvent.getOldVersionInfo() instanceof MathModelInfo){
		BioModelNode node = ((BioModelNode)getRoot()).findNodeByUserObject(databaseEvent.getOldVersionInfo());
		node.setUserObject(databaseEvent.getNewVersionInfo());
		nodeChanged(node);
	}
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.beans.PropertyChangeEvent evt) {
	getPropertyChange().firePropertyChange(evt);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, int oldValue, int newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, boolean oldValue, boolean newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}


/**
 * Gets the documentManager property (cbit.vcell.clientdb.DocumentManager) value.
 * @return The documentManager property value.
 * @see #setDocumentManager
 */
public cbit.vcell.clientdb.DocumentManager getDocumentManager() {
	return fieldDocumentManager;
}


/**
 * Gets the latestOnly property (boolean) value.
 * @return The latestOnly property value.
 * @see #setLatestOnly
 */
public boolean getLatestOnly() {
	return fieldLatestOnly;
}


/**
 * Accessor for the propertyChange field.
 */
protected java.beans.PropertyChangeSupport getPropertyChange() {
	if (propertyChange == null) {
		propertyChange = new java.beans.PropertyChangeSupport(this);
	};
	return propertyChange;
}


/**
 * The hasListeners method was generated to support the propertyChange field.
 */
public synchronized boolean hasListeners(java.lang.String propertyName) {
	return getPropertyChange().hasListeners(propertyName);
}


/**
 * Insert the method's description here.
 * Creation date: (2/14/01 3:50:24 PM)
 */
public void refreshTree() {
	if (getDocumentManager()!=null){
		try {
			setRoot(createBaseTree());
		}catch (DataAccessException e){
			e.printStackTrace(System.out);
		}
	}else{
		setRoot(new BioModelNode("empty"));
	}
}


/**
 * The removePropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().removePropertyChangeListener(listener);
}


/**
 * The removePropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void removePropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
	getPropertyChange().removePropertyChangeListener(propertyName, listener);
}


/**
 * Sets the documentManager property (cbit.vcell.clientdb.DocumentManager) value.
 * @param documentManager The new value for the property.
 * @see #getDocumentManager
 */
public void setDocumentManager(cbit.vcell.clientdb.DocumentManager documentManager) {
	cbit.vcell.clientdb.DocumentManager oldValue = fieldDocumentManager;
	fieldDocumentManager = documentManager;

	if (oldValue != null){
		oldValue.removeDatabaseListener(this);
	}
	if (documentManager != null){
		documentManager.addDatabaseListener(this);
	}

	firePropertyChange("documentManager", oldValue, documentManager);

	if (documentManager != oldValue){
		refreshTree();
	}
}


/**
 * Sets the latestOnly property (boolean) value.
 * @param latestOnly The new value for the property.
 * @see #getLatestOnly
 */
public void setLatestOnly(boolean latestOnly) {
	boolean oldValue = fieldLatestOnly;
	fieldLatestOnly = latestOnly;
	firePropertyChange("latestOnly", new Boolean(oldValue), new Boolean(latestOnly));
	if (latestOnly != oldValue){
		refreshTree();
	}
}
}