package org.vcell.vis.mapping.chombo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.vcell.util.DataAccessException;
import org.vcell.util.FileUtils;
import org.vcell.vis.chombo.ChomboDataset;
import org.vcell.vis.chombo.ChomboDataset.ChomboCombinedVolumeMembraneDomain;
import org.vcell.vis.chombo.ChomboMembraneVarData;
import org.vcell.vis.chombo.ChomboMeshData;
import org.vcell.vis.io.ChomboFileReader;
import org.vcell.vis.io.ChomboFiles;
import org.vcell.vis.io.VtuFileContainer;
import org.vcell.vis.io.VtuVarInfo;
import org.vcell.vis.mapping.VisDomain;
import org.vcell.vis.vismesh.thrift.ChomboIndexData;
import org.vcell.vis.vismesh.thrift.ChomboSurfaceIndex;
import org.vcell.vis.vismesh.thrift.ChomboVolumeIndex;
import org.vcell.vis.vismesh.thrift.VisMesh;
import org.vcell.vis.vtk.VtkGridUtils;

import cbit.vcell.math.MathException;
import cbit.vcell.math.VariableType.VariableDomain;
import cbit.vcell.simdata.OutputContext;
import cbit.vcell.simdata.VCData;

public class ChomboVtkFileWriter {
	
	public static final String MEMBRANE_DOMAIN_SUFFIX = "_Membrane";

	public interface ProgressListener {
		public void progress(double percentDone);
	}
	
	public static class SimpleChomboCellIndices implements ChomboCellIndices {
		public final int boxLevel;
		public final int boxNumber;
		public final int boxIndex;
		public SimpleChomboCellIndices(int boxLevel, int boxNumber, int boxIndex){
			this.boxLevel = boxLevel;
			this.boxNumber = boxNumber;
			this.boxIndex = boxIndex;
		}
		
		@Override
		public int getLevel() {
			return boxLevel;
		}
		@Override
		public int getBoxNumber() {
			return boxNumber;
		}
		@Override
		public int getBoxIndex() {
			return boxIndex;
		}
	}

	public static class SimpleChomboVisMembraneIndex implements ChomboVisMembraneIndex {
		public final int membraneIndex;
		
		public SimpleChomboVisMembraneIndex(int membraneIndex){
			this.membraneIndex = membraneIndex;
		}
		
		@Override
		public int getChomboIndex() {
			return membraneIndex;
		}
	}

	public File[] writeVtuExportFiles(ChomboFiles chomboFiles, File destinationDirectory, ProgressListener progressListener) throws Exception{
		throw new RuntimeException("VTK export not implemnented");
//		if (destinationDirectory==null || !destinationDirectory.isDirectory()){
//			throw new RuntimeException("destinationDirectory '"+destinationDirectory+" not valid");
//		}
//		ArrayList<File> files = new ArrayList<File>();
//		int numFiles = chomboFiles.getDomains().size() * chomboFiles.getTimeIndices().size();
//		HashMap<String, VisMesh> domainMeshMap = new HashMap<String, VisMesh>();
//		int filesProcessed = 0;
//		for (int timeIndex : chomboFiles.getTimeIndices()){
//			ChomboDataset chomboDataset = ChomboFileReader.readDataset(chomboFiles,timeIndex);
//			for (ChomboDomain chomboDomain : chomboDataset.getDomains()){
//				ChomboMeshData chomboMeshData = chomboDomain.getChomboMeshData();
//				ChomboMeshMapping chomboMeshMapping = new ChomboMeshMapping();
//				VisMesh visMesh = domainMeshMap.get(chomboDomain.getName());
//				if (visMesh == null){
//					visMesh = chomboMeshMapping.fromMeshData(chomboMeshData, chomboDomain);
//					domainMeshMap.put(chomboDomain.getName(),visMesh);
//				}
//				
//				boolean bMembrane = false;
//				VisDomain visDomain = new VisDomain(chomboDomain.getName(),visMesh);
//				File file = new File(destinationDirectory,chomboFiles.getCannonicalFilePrefix(visDomain.getName(),timeIndex)+".vtu");
//				VtkGridUtils.writeChomboVolumeVtkGrid(visDomain,file);
//				files.add(file);
//				
//				if (chomboMeshData.getMembraneVarData().size() > 0)
//				{
//					bMembrane = true;
//					String filename = chomboFiles.getSimID() + "_" + chomboFiles.getJobIndex() + "_VCellVariableSolution_" + String.format("%06d",timeIndex);
//					File memVtuFile = new File(destinationDirectory,filename+".vtu");
//					File memfile = new File(destinationDirectory,filename+".vtu");
//					VtkGridUtils.writeChomboMembraneVtkGrid(visDomain, memVtuFile);
//					files.add(memfile);
//				}
//
//				filesProcessed++;
//				if (progressListener!=null){
//					progressListener.progress(((double)filesProcessed)/numFiles);
//				}
//			}
//		}
//		return files.toArray(new File[0]);
//
	}

	public double[] getVtuMeshData(ChomboFiles chomboFiles, OutputContext outputContext, File destinationDirectory, double time, VtuVarInfo var, int timeIndex) throws Exception {
		
		ChomboDataset chomboDataset = ChomboFileReader.readDataset(chomboFiles,chomboFiles.getTimeIndices().get(timeIndex));
		String domainName = var.domainName;
		ChomboCombinedVolumeMembraneDomain chomboCombinedVolumeMembraneDomain = chomboDataset.getDomainFromVolumeOrMembraneName(domainName);
		ChomboMeshData chomboMeshData = chomboCombinedVolumeMembraneDomain.getChomboMeshData();
		
		File chomboIndexDataFile = getChomboIndexDataFileName(destinationDirectory, chomboFiles, domainName);
		if (!chomboIndexDataFile.exists()){
			writeEmptyMeshFiles(chomboFiles, destinationDirectory, null);
		}
		
		double data[] = null;
		
		switch (var.variableDomain){
		
			case VARIABLEDOMAIN_CONTOUR:{
				break;
			}
			case VARIABLEDOMAIN_MEMBRANE: {
				ChomboIndexData chomboIndexData = VtkGridUtils.readChomboIndexData(chomboIndexDataFile);
				List<ChomboVisMembraneIndex> cellIndices = new ArrayList<ChomboVisMembraneIndex>();
				for (ChomboSurfaceIndex chomboSurfaceIndex : chomboIndexData.chomboSurfaceIndices){
					cellIndices.add(new SimpleChomboVisMembraneIndex(chomboSurfaceIndex.index));
				}
				data = chomboMeshData.getMembraneCellData(var.name, cellIndices);
				break;
			}
			case VARIABLEDOMAIN_NONSPATIAL:{
				break;
			}
			case VARIABLEDOMAIN_POSTPROCESSING:{
				break;
			}
			case VARIABLEDOMAIN_UNKNOWN:{
				break;
			}
			case VARIABLEDOMAIN_VOLUME: {
				ChomboIndexData chomboIndexData = VtkGridUtils.readChomboIndexData(chomboIndexDataFile);
				List<ChomboCellIndices> cellIndices = new ArrayList<ChomboCellIndices>();
				for (ChomboVolumeIndex chomboVolIndex : chomboIndexData.getChomboVolumeIndices()){
					cellIndices.add(new SimpleChomboCellIndices(chomboVolIndex.getLevel(), chomboVolIndex.getBoxNumber(), chomboVolIndex.getBoxIndex()));
				}
				data = chomboMeshData.getVolumeCellData(var.name, cellIndices);
				break;
			}
			default:{
				throw new RuntimeException("unsupported variable type "+var.variableDomain.name()+" for variable "+var.name);
			}
		}
		
		return data;
	}

	public VtuVarInfo[] getVtuVarInfos(ChomboFiles chomboFiles,	OutputContext outputContext, VCData vcData) throws DataAccessException {
		//
		// read the time=0 chombo dataset into memory to get the var names (probably a more efficient way of doing this).
		//
		ChomboDataset chomboDataset;
		try {
			int timeIndex = 0;
			chomboDataset = ChomboFileReader.readDataset(chomboFiles,chomboFiles.getTimeIndices().get(timeIndex));
		} catch (Exception e) {
			throw new DataAccessException("failed to read chombo dataset: "+e.getMessage(),e);
		}
		
		//
		// for each ChomboDomain get list of built-in (mesh) variables, component (regular) volume variables, and Membrane Variables (still tied to the volume).
		//
		ArrayList<VtuVarInfo> varInfos = new ArrayList<VtuVarInfo>();
		for (ChomboCombinedVolumeMembraneDomain chomboCombinedVolumeMembraneDomain : chomboDataset.getCombinedVolumeMembraneDomains()){
			ChomboMeshData chomboMeshData = chomboCombinedVolumeMembraneDomain.getChomboMeshData();
			for (String builtinVarName : chomboMeshData.getVolumeBuiltinNames()){
				String varName = builtinVarName;
				String domainName = chomboCombinedVolumeMembraneDomain.getVolumeDomainName();
				String displayName = "("+domainName+")  "+varName;
				VariableDomain variableDomain = VariableDomain.VARIABLEDOMAIN_VOLUME;
				boolean bMeshVariable = true;
				varInfos.add(new VtuVarInfo(varName, displayName, domainName, variableDomain, bMeshVariable));
			}
			for (String componentVarName : chomboMeshData.getVolumeDataNames()){
				String varName = componentVarName;
				String domainName = chomboCombinedVolumeMembraneDomain.getVolumeDomainName();
				String displayName = "("+domainName+")  "+varName;
				VariableDomain variableDomain = VariableDomain.VARIABLEDOMAIN_VOLUME;
				boolean bMeshVariable = false;
				varInfos.add(new VtuVarInfo(varName, displayName, domainName, variableDomain, bMeshVariable));
			}
			for (ChomboMembraneVarData membraneVarData : chomboMeshData.getMembraneVarData()){
				String varName = membraneVarData.getName();
				String membraneDomainName = chomboCombinedVolumeMembraneDomain.getMembraneDomainName();
				String displayName = "("+membraneDomainName+")  "+varName;
				VariableDomain variableDomain = VariableDomain.VARIABLEDOMAIN_MEMBRANE;
				boolean bMeshVariable = false;
				varInfos.add(new VtuVarInfo(varName, displayName, membraneDomainName, variableDomain, bMeshVariable));
			}
			for (String builtinVarName : chomboMeshData.getMembraneBuiltinNames()){
				String varName = builtinVarName;
				String membraneDomainName = chomboCombinedVolumeMembraneDomain.getMembraneDomainName();
				String displayName = "("+membraneDomainName+")  "+varName;
				VariableDomain variableDomain = VariableDomain.VARIABLEDOMAIN_MEMBRANE;
				boolean bMeshVariable = true;
				varInfos.add(new VtuVarInfo(varName, displayName, membraneDomainName, variableDomain, bMeshVariable));
			}
		}
		return varInfos.toArray(new VtuVarInfo[0]);
	}
	
	private File getVtuMeshFileName(File directory, ChomboFiles chomboFiles, String domainName) {
		return new File(directory,chomboFiles.getCannonicalFilePrefix(domainName)+".vtu");
	}
	
	private File getChomboIndexDataFileName(File directory, ChomboFiles chomboFiles, String domainName) {
		return new File(directory,chomboFiles.getCannonicalFilePrefix(domainName)+".chomboindex");
	}
	
	public VtuFileContainer getEmptyVtuMeshFiles(ChomboFiles chomboFiles, File destinationDirectory) throws IOException, MathException, DataAccessException {
		//
		// for each domain in cartesian mesh, get the mesh file
		//
		Set<String> volumeDomainNames = chomboFiles.getVolumeDomainNames();
		ArrayList<File> meshFiles = new ArrayList<File>();
		ArrayList<String> domains = new ArrayList<String>();
		//
		// look at domains returned from chomboFiles (volume domains only) and check for existence of corresponding volume mesh files.
		//
		boolean bMeshFileMissing = false;
		for (String volumeDomainName : volumeDomainNames){
			File volumeMeshFile = getVtuMeshFileName(destinationDirectory, chomboFiles, volumeDomainName);
			meshFiles.add(volumeMeshFile);
			domains.add(volumeDomainName);
			if (!volumeMeshFile.exists()){
				bMeshFileMissing = true;
			}
		}
		
		//
		// if any volume mesh files are missing, need to force write of all mesh files (both volume and membrane)
		//
		if (bMeshFileMissing){
			writeEmptyMeshFiles(chomboFiles, destinationDirectory, null);
		}
		
		//
		// all mesh files (volume and membrane) are assumed to already exist at this point.
		// if a membrane mesh file is not found, than it doesn't exist, if it is found add it to the list.
		//
		for (String volumeDomainName : volumeDomainNames){
			String membraneDomainName = volumeDomainName+MEMBRANE_DOMAIN_SUFFIX;
			File membraneMeshFile = getVtuMeshFileName(destinationDirectory, chomboFiles, membraneDomainName);
			if (membraneMeshFile.exists()){
				meshFiles.add(membraneMeshFile);
				domains.add(membraneDomainName);
			}
		}

		//
		// for all mesh files (volume and membrane) make a VtuMesh entry (with domainName and file contents).
		//
		VtuFileContainer vtuFileContainer = new VtuFileContainer();
		for (int i=0;i<meshFiles.size();i++){
			byte[] vtuMeshFileContents = FileUtils.readByteArrayFromFile(meshFiles.get(i));
			vtuFileContainer.addVtuMesh(new VtuFileContainer.VtuMesh(domains.get(i), 0.0, vtuMeshFileContents));
		}
		return vtuFileContainer;
	}

	public File[] writeEmptyMeshFiles(ChomboFiles chomboFiles, File destinationDirectory, ProgressListener progressListener) throws IOException, MathException, DataAccessException {
		ArrayList<File> meshFiles = new ArrayList<File>();
		int timeIndex = 0;
		
		HashMap<String, VisMesh> domainMeshMap = new HashMap<String, VisMesh>();
		ChomboDataset chomboDataset;
		try {
			chomboDataset = ChomboFileReader.readDataset(chomboFiles,chomboFiles.getTimeIndices().get(timeIndex));
		} catch (Exception e) {
			throw new DataAccessException("failed to read Chombo Dataset: "+e.getMessage(),e);
		}
		for (ChomboCombinedVolumeMembraneDomain chomboCombinedVolumeMembraneDomain : chomboDataset.getCombinedVolumeMembraneDomains()){
			ChomboMeshData chomboMeshData = chomboCombinedVolumeMembraneDomain.getChomboMeshData();
			ChomboMeshMapping chomboMeshMapping = new ChomboMeshMapping();
			VisMesh visMesh = domainMeshMap.get(chomboCombinedVolumeMembraneDomain.getVolumeDomainName());
			if (visMesh == null){
				visMesh = chomboMeshMapping.fromMeshData(chomboMeshData, chomboCombinedVolumeMembraneDomain);
				domainMeshMap.put(chomboCombinedVolumeMembraneDomain.getVolumeDomainName(),visMesh);
			}
			VisDomain visDomainVolume = new VisDomain(chomboCombinedVolumeMembraneDomain.getVolumeDomainName(),visMesh);
			String volumeDomainName = visDomainVolume.getName();
			
			//
			// write volume mesh file
			//
			{
			File volumeMeshFile = getVtuMeshFileName(destinationDirectory, chomboFiles, volumeDomainName);
			File chomboIndexDataFile = getChomboIndexDataFileName(destinationDirectory, chomboFiles, volumeDomainName);
			VtkGridUtils.writeChomboVolumeVtkGrid(visDomainVolume, volumeMeshFile, chomboIndexDataFile);
			meshFiles.add(volumeMeshFile);
			}
						
			if (chomboMeshData.getMembraneVarData().size() > 0)	{
				//
				// write membrane mesh file
				//
				VisDomain visDomainMembrane = new VisDomain(chomboCombinedVolumeMembraneDomain.getMembraneDomainName(),visMesh);
				String membraneDomainName = visDomainMembrane.getName();
				
				File membraneMeshFile = getVtuMeshFileName(destinationDirectory, chomboFiles, membraneDomainName);
				File membraneIndexDataFile = getChomboIndexDataFileName(destinationDirectory, chomboFiles, membraneDomainName);
				VtkGridUtils.writeChomboMembraneVtkGrid(visDomainMembrane, membraneMeshFile, membraneIndexDataFile);
				meshFiles.add(membraneMeshFile);
			}
		}
		return meshFiles.toArray(new File[0]);
	}

}