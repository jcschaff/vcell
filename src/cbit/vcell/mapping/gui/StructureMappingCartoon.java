package cbit.vcell.mapping.gui;
/*
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
 */
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import cbit.gui.graph.GraphEvent;
import cbit.gui.graph.GraphModel;
import cbit.gui.graph.Shape;
import cbit.vcell.geometry.GeometryClass;
import cbit.vcell.geometry.GeometryException;
import cbit.vcell.graph.FeatureMappingShape;
import cbit.vcell.graph.FeatureShape;
import cbit.vcell.graph.GeometryClassLegendShape;
import cbit.vcell.graph.GeometryContextContainerShape;
import cbit.vcell.graph.GeometryContextGeometryShape;
import cbit.vcell.graph.GeometryContextStructureShape;
import cbit.vcell.graph.StructureMappingFeatureShape;
import cbit.vcell.graph.SubVolumeContainerShape;
import cbit.vcell.mapping.FeatureMapping;
import cbit.vcell.mapping.GeometryContext;
import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.mapping.StructureMapping;
import cbit.vcell.model.Feature;
import cbit.vcell.model.Membrane;
import cbit.vcell.model.Structure;

public class StructureMappingCartoon extends GraphModel implements PropertyChangeListener {
	private SimulationContext simulationContext = null;
	//private GeometryContext geometryContext = null;
	private SubVolumeContainerShape subVolumeContainerShape = null;

	public GeometryContext getGeometryContext() {
		if (getSimulationContext() != null){
			return getSimulationContext().getGeometryContext();
		} else {
			return null;
		}
	}

	private SimulationContext getSimulationContext() { return simulationContext; }

	public void propertyChange(PropertyChangeEvent event) {
		try {
			refreshAll();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public void refreshAll() {
		{
			GeometryClass[] geometryClasses = getGeometryContext().getGeometry().getGeometryClasses();
			for (int i=0;i<geometryClasses.length;i++){
				Shape testShape = getShapeFromModelObject(geometryClasses[i]);
				if(testShape instanceof GeometryClassLegendShape){
					geometryClasses[i].removePropertyChangeListener((GeometryClassLegendShape)testShape);
				}
			}	
		}
		clearAllShapes();
		if (getSimulationContext() == null){
			fireGraphChanged(new GraphEvent(this));
			return;
		}
		GeometryContextGeometryShape geometryShape = 
			new GeometryContextGeometryShape(this, getGeometryContext().getGeometry());
		GeometryContextStructureShape structureShape = 
			new GeometryContextStructureShape(this, getGeometryContext().getModel());
		GeometryContextContainerShape containerShape = 
			new GeometryContextContainerShape(this, getGeometryContext(), structureShape, geometryShape);
		addShape(containerShape);
		addShape(geometryShape);
		addShape(structureShape);
		getGeometryContext().removePropertyChangeListener(this);
		getGeometryContext().addPropertyChangeListener(this);
		// create all StructureShapes
		Structure structures[] = getGeometryContext().getModel().getStructures();
		for (int i=0; i<structures.length; i++){
			if (structures[i] instanceof Feature){
				StructureMappingFeatureShape smShape = 
					new StructureMappingFeatureShape((Feature)structures[i], 
							getGeometryContext().getModel(), this);
				addShape(smShape);
				containerShape.addChildShape(smShape);
				structures[i].removePropertyChangeListener(this);
				structures[i].addPropertyChangeListener(this);
			}	
		}
		// create all SubvolumeLegendShapes (for legend)
		GeometryClass[] geometryClasses = getGeometryContext().getGeometry().getGeometryClasses();
		for (int i=0;i<geometryClasses.length;i++){
			GeometryClassLegendShape geometryClassLegendShape = 
				new GeometryClassLegendShape(geometryClasses[i], getGeometryContext().getGeometry(), this, 10);
			geometryClasses[i].addPropertyChangeListener(geometryClassLegendShape);
			addShape(geometryClassLegendShape);
			geometryShape.addChildShape(geometryClassLegendShape);
		}	
		if((subVolumeContainerShape == null) || 
				(subVolumeContainerShape.getModelObject() != getGeometryContext().getGeometry())){
			subVolumeContainerShape = 
				new SubVolumeContainerShape(getGeometryContext().getGeometry(), this);			
		}
		subVolumeContainerShape.removeAllChildren();
		subVolumeContainerShape.setBrightImage(getGeometryContext().getGeometry().getGeometrySpec().getThumbnailImage().getCurrentValue());
		addShape(subVolumeContainerShape);
		geometryShape.addChildShape(subVolumeContainerShape);
		StructureMapping structureMappings[] = getGeometryContext().getStructureMappings();
		for (int i=0;i<structureMappings.length;i++){
			StructureMapping structureMapping = structureMappings[i];
			if (structureMapping instanceof FeatureMapping){
				FeatureMapping featureMapping = (FeatureMapping)structureMapping;
				structureMapping.removePropertyChangeListener(this);
				structureMapping.addPropertyChangeListener(this);
				if (featureMapping.getGeometryClass()!=null){
					FeatureShape featureShape = 
						(FeatureShape) getShapeFromModelObject(featureMapping.getFeature());
					GeometryClassLegendShape geometryClassLegendShape = 
						(GeometryClassLegendShape) getShapeFromModelObject(featureMapping.getGeometryClass());
					FeatureMappingShape fmShape = 
						new FeatureMappingShape(featureMapping,featureShape, geometryClassLegendShape, this);
					addShape(fmShape);
					containerShape.addChildShape(fmShape);
				}
			}
		}	
		// assign children to shapes according to heirarchy in Model
		int nullParentCount=0;
		List<Shape> shapes = getShapes();
		for(Shape shape : shapes) {
			// for each featureShape, find corresponding featureShape
			if (shape instanceof FeatureShape){
				FeatureShape fs = (FeatureShape)shape;
				Membrane membrane = fs.getFeature().getMembrane();
				if (membrane!=null){
					// add this feature as child to parent feature
					Feature parentFeature = membrane.getOutsideFeature();
					FeatureShape parentFeatureShape = (FeatureShape) getShapeFromModelObject(parentFeature);
					if (!parentFeatureShape.contains(fs)){
						parentFeatureShape.addChildShape(fs);
					}
				} else {
					if(!structureShape.contains(fs)) {
						structureShape.addChildShape(fs);
					}
					nullParentCount++;
				}		
			}	
		}	
		fireGraphChanged(new GraphEvent(this));
	}

	public void setSimulationContext(SimulationContext aSimulationContext) 
	throws GeometryException, Exception {
		if(this.simulationContext != null) {
			this.simulationContext.getGeometryContext().removePropertyChangeListener(this);
			this.simulationContext.getGeometry().removePropertyChangeListener(this);
			this.simulationContext.getGeometry().getGeometrySpec().removePropertyChangeListener(this);
			if (simulationContext.getGeometry().getGeometrySurfaceDescription() != null) {
				simulationContext.getGeometry().getGeometrySurfaceDescription().removePropertyChangeListener(this);
			}
			this.simulationContext.removePropertyChangeListener(this);
			StructureMapping oldStructureMappings[] = 
				simulationContext.getGeometryContext().getStructureMappings();
			for (int i=0;i<oldStructureMappings.length;i++){
				oldStructureMappings[i].removePropertyChangeListener(this);
			}
		}
		this.simulationContext = aSimulationContext;
		if (this.simulationContext != null){
			this.simulationContext.getGeometryContext().addPropertyChangeListener(this);
			this.simulationContext.getGeometry().addPropertyChangeListener(this);
			this.simulationContext.getGeometry().getGeometrySpec().addPropertyChangeListener(this);
			if (simulationContext.getGeometry().getGeometrySurfaceDescription() != null) {
				simulationContext.getGeometry().getGeometrySurfaceDescription().addPropertyChangeListener(this);
			}
			this.simulationContext.addPropertyChangeListener(this);
			StructureMapping newStructureMappings[] = 
				simulationContext.getGeometryContext().getStructureMappings();
			for (int i=0;i<newStructureMappings.length;i++){
				newStructureMappings[i].addPropertyChangeListener(this);
			}
		}
		refreshAll();
	}
}