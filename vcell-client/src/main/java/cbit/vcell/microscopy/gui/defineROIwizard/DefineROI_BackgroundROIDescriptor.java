/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.microscopy.gui.defineROIwizard;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.vcell.util.gui.DialogUtils;
import org.vcell.wizard.WizardPanelDescriptor;

import cbit.vcell.client.task.AsynchClientTask;
import cbit.vcell.microscopy.FRAPData;
import cbit.vcell.microscopy.VFRAPPreference;
import cbit.vcell.microscopy.gui.VFrap_OverlayEditorPanelJAI;
import cbit.vcell.resource.VCellConfiguration;

public class DefineROI_BackgroundROIDescriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "DefineROI_backgroundROI";
    private JPanel imgPanel = null;
    public DefineROI_BackgroundROIDescriptor (JPanel imagePanel) {
    	super();
        imgPanel = imagePanel;
        JPanel bgPanel = new JPanel(new BorderLayout());
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(bgPanel);
        setProgressPopupShown(false); 
        setTaskProgressKnown(false);
    }
    
    public String getNextPanelDescriptorID() {
        return DefineROI_SummaryDescriptor.IDENTIFIER;
    }
    
    public String getBackPanelDescriptorID() {
        return DefineROI_BleachedROIDescriptor.IDENTIFIER;
    }
    
    public void aboutToDisplayPanel()
    {
    	((JPanel)getPanelComponent()).removeAll();
    	((JPanel)getPanelComponent()).add(imgPanel);
    	((DefineROI_Panel)imgPanel).adjustComponents(VFrap_OverlayEditorPanelJAI.DEFINE_BACKGROUNDROI);
    	(((DefineROI_Panel)imgPanel).getCenterPanel()).getOverlayEditorPanelJAI().setROIAssistVisible(false);
    	if(VCellConfiguration.getValue(VFRAPPreference.ROI_ASSIST_REQUIREMENT_TYPE, VFRAPPreference.ROI_ASSIST_REQUIRE_ALWAYS).equals(VFRAPPreference.ROI_ASSIST_REQUIRE_ALWAYS) &&
			((DefineROI_Panel)imgPanel).getFrapWorkspace().getWorkingFrapStudy().getFrapData().getRoi(FRAPData.VFRAP_ROI_ENUM.ROI_BACKGROUND.name()).getNonzeroPixelsCount()<1)
		{
			(((DefineROI_Panel)imgPanel).getCenterPanel()).getOverlayEditorPanelJAI().showROIAssist();
		}
    }
    
    public ArrayList<AsynchClientTask> preNextProcess()
    {
    	//create AsynchClientTask arraylist
		ArrayList<AsynchClientTask> taskArrayList = new ArrayList<AsynchClientTask>();
		
		final String nextROIStr = null;
		AsynchClientTask setCurrentROITask = new AsynchClientTask("", AsynchClientTask.TASKTYPE_SWING_BLOCKING) 
		{
			public void run(Hashtable<String, Object> hashTable) throws Exception
			{
				//save current ROI and load ROI in the panel it goes next to
				((DefineROI_Panel)imgPanel).setCurrentROI(nextROIStr, true);
				FRAPData fData = ((DefineROI_Panel)imgPanel).getFrapWorkspace().getWorkingFrapStudy().getFrapData();
				fData.setCurrentlyDisplayedROI(fData.getRoi(FRAPData.VFRAP_ROI_ENUM.ROI_CELL.name()), true);
			}
		};
		taskArrayList.add(setCurrentROITask);
		return taskArrayList;
    } 
 
    public ArrayList<AsynchClientTask> postNextProcess()
    {
    	//create AsynchClientTask arraylist
		ArrayList<AsynchClientTask> taskArrayList = new ArrayList<AsynchClientTask>();
		
		AsynchClientTask ifNeedROIAssistTask = new AsynchClientTask("", AsynchClientTask.TASKTYPE_SWING_BLOCKING) 
		{
			public void run(Hashtable<String, Object> hashTable) throws Exception
			{
				if((((DefineROI_Panel)imgPanel).getCenterPanel()).getOverlayEditorPanelJAI().isROIAssistVisible() &&
				   ((DefineROI_Panel)imgPanel).getFrapWorkspace().getWorkingFrapStudy().getFrapData().getRoi(FRAPData.VFRAP_ROI_ENUM.ROI_BACKGROUND.name()).getNonzeroPixelsCount()<1)
				{
					DialogUtils.showWarningDialog((imgPanel), "Background ROI is not applied. Please complete the following actions to apply: \n \'Resolve...\' -> \'Fill Voids\' -> \'Apply and Close\'. ");
				}
			}
		};
		taskArrayList.add(ifNeedROIAssistTask);
		return taskArrayList;
    }
    
    public ArrayList<AsynchClientTask> preBackProcess()
    {
    	//create AsynchClientTask arraylist
		ArrayList<AsynchClientTask> taskArrayList = new ArrayList<AsynchClientTask>();
    	
		final String backROIStr = FRAPData.VFRAP_ROI_ENUM.ROI_BLEACHED.name();
		AsynchClientTask setCurrentROITask = new AsynchClientTask("", AsynchClientTask.TASKTYPE_SWING_BLOCKING) 
		{
			public void run(Hashtable<String, Object> hashTable) throws Exception
			{
				//save current ROI and load ROI in the panel it backs to 
				((DefineROI_Panel)imgPanel).setCurrentROI(backROIStr, true);
			}
		};
		taskArrayList.add(setCurrentROITask);															
		return taskArrayList;
    }

}

