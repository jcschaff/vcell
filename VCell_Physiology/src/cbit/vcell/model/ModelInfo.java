package cbit.vcell.model;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.io.*;

import org.vcell.util.document.Version;
import org.vcell.util.document.VersionInfo;

/**
 * This class was generated by a SmartGuide.
 * 
 */
public class ModelInfo implements Serializable,VersionInfo {
	private Version version = null;
/**
 * This method was created in VisualAge.
 * @param argVersion cbit.sql.Version
 */
public ModelInfo(Version argVersion) {
	super();
	this.version = argVersion;
}
/**
 * Insert the method's description here.
 * Creation date: (1/25/01 12:24:41 PM)
 * @return boolean
 * @param object java.lang.Object
 */
public boolean equals(Object object) {
	if (object instanceof ModelInfo){
		if (!getVersion().getVersionKey().equals(((ModelInfo)object).getVersion().getVersionKey())){
			return false;
		}
		return true;
	}
	return false;
}
/**
 * This method was created in VisualAge.
 * @return cbit.sql.Version
 */
public Version getVersion() {
	return version;
}
/**
 * Insert the method's description here.
 * Creation date: (1/25/01 12:28:06 PM)
 * @return int
 */
public int hashCode() {
	return getVersion().getVersionKey().hashCode();
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String toString() {
		return "ModelInfo(Version="+version+")";
}
}
