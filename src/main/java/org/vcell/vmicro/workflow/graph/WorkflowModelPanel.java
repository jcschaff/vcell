package org.vcell.vmicro.workflow.graph;

import java.io.File;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.vcell.util.ClientTaskStatusSupport;
import org.vcell.vmicro.workflow.data.LocalWorkspace;
import org.vcell.workflow.DataInput;
import org.vcell.workflow.DataOutput;
import org.vcell.workflow.Task;
import org.vcell.workflow.TaskContext;
import org.vcell.workflow.Workflow;

import cbit.gui.graph.GraphLayoutManager;
import cbit.gui.graph.GraphModel;
import cbit.gui.graph.GraphResizeManager.ZoomRangeException;
import cbit.gui.graph.gui.CartoonTool.Mode;
import cbit.vcell.graph.gui.ZoomShapeIcon;
/**
 * This class was generated by a SmartGuide.
 * 
 */
@SuppressWarnings("serial")
public class WorkflowModelPanel extends JPanel implements java.awt.event.ActionListener, java.beans.PropertyChangeListener {
	private JPanel ivjFeatureSizePanel = null;
	private cbit.gui.graph.gui.GraphPane ivjGraphPane = null;
	protected transient java.beans.PropertyChangeSupport propertyChange;
	private boolean ivjConnPtoP1Aligning = false;
	private JPanel ivjJPanel1 = null;
	private JToolBar ivjJToolBar1 = null;
	private ButtonModel ivjSelection = null;
	private org.vcell.util.gui.JToolBarToggleButton ivjSelectButton = null;
	private org.vcell.util.gui.ButtonGroupCivilized ivjButtonGroupCivilized = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjJLabel3 = null;
	private JButton ivjAnnealLayoutButton = null;
	private JButton ivjCircleLayoutButton = null;
	private JLabel ivjJLabel4 = null;
	private JButton ivjLevellerLayoutButton = null;
	private JButton ivjRandomLayoutButton = null;
	private JButton ivjRelaxerLayoutButton = null;
	private JButton ivjZoomInButton = null;
	private JButton ivjZoomOutButton = null;
	private JButton ivjGlgLayoutJButton = null;
	private JButton ivjTransposeLayoutButton = null;
	private WorkflowGraphModel fieldWorkflowModel = null;
	private WorkflowCartoonTool fieldWorkflowCartoonTool1 = null;
	private Workflow fieldWorkflow = null;
	/**
	 * Constructor
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	public WorkflowModelPanel() {
		super();
		initialize();
	}
	/**
	 * CartoonPanel constructor comment.
	 * @param layout java.awt.LayoutManager
	 */
	public WorkflowModelPanel(java.awt.LayoutManager layout) {
		super(layout);
	}
	/**
	 * Method to handle events for the ActionListener interface.
	 * @param e java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		// user code begin {1}
		// user code end
		if (e.getSource() == getZoomInButton()) 
			connEtoC1(e);
		if (e.getSource() == getZoomOutButton()) 
			connEtoC2(e);
		if (e.getSource() == getRelaxerLayoutButton()) 
			connEtoM12(e);
		if (e.getSource() == getCircleLayoutButton()) 
			connEtoM7(e);
		if (e.getSource() == getRandomLayoutButton()) 
			connEtoM1(e);
		if (e.getSource() == getGlgLayoutJButton()) 
			connEtoM9(e);
		if (e.getSource() == getTransposeLayoutJButton()) 
			invokeTransposeLayout();
		if (e.getSource() == getLevellerLayoutButton()) 
			connEtoM13(e);
		if (e.getSource() == getAnnealLayoutButton()) 
			connEtoM2(e);
		// user code begin {2}
		// user code end
	}
	private void invokeTransposeLayout(){
		getWorkflowCartoonTool().transposeLayout();
	}
	/**
	 * The addPropertyChangeListener method was generated to support the propertyChange field.
	 * @param listener java.beans.PropertyChangeListener
	 */
	@Override
	public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().addPropertyChangeListener(listener);
	}
	/**
	 * connEtoC1:  (ZoomInButton.action.actionPerformed(java.awt.event.ActionEvent) --> ReactionCartoonEditorPanel.zoomInButton_ActionPerformed()V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.zoomInButton_ActionPerformed();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoC2:  (ZoomOutButton.action.actionPerformed(java.awt.event.ActionEvent) --> ReactionCartoonEditorPanel.zoomOutButton_ActionPerformed()V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.zoomOutButton_ActionPerformed();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM1:  (Button1.action.actionPerformed(java.awt.event.ActionEvent) --> CanvasTool.randomize()V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().layout(GraphLayoutManager.OldLayouts.RANDOMIZER);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM10:  (ReactionCartoonEditorPanel.initialize() --> GraphPane.graphModel)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM10() {
		try {
			// user code begin {1}
			// user code end
			getGraphPane().setGraphModel(getWorkflowModel());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM12:  (RelaxerButton.action.actionPerformed(java.awt.event.ActionEvent) --> ReactionCartoonTool.layout(Ljava.lang.String;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM12(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().layout(GraphLayoutManager.OldLayouts.RELAXER);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM13:  (LevellerButton.action.actionPerformed(java.awt.event.ActionEvent) --> ReactionCartoonTool.layout(Ljava.lang.String;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM13(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().layout(GraphLayoutManager.OldLayouts.LEVELLER);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM16:  (ReactionCartoonEditorPanel.initialize() --> ReactionCartoonTool1.graphPane)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM16() {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().setGraphPane(getGraphPane());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM18:  (ReactionCartoonEditorPanel.initialize() --> ReactionCartoonTool1.buttonGroup)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM18() {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().setButtonGroup(getButtonGroupCivilized());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM2:  (AnnealButton.action.actionPerformed(java.awt.event.ActionEvent) --> ReactionCartoonTool.layout(Ljava.lang.String;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().layout(GraphLayoutManager.OldLayouts.ANNEALER);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM3:  (SimpleGraphModelPanel.graph --> SimpleGraphModel.graph)
	 * @param arg1 java.beans.PropertyChangeEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM3(java.beans.PropertyChangeEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getWorkflowModel().setWorkflow(this.getWorkflow());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM5:  (ReactionCartoonEditorPanel.initialize() --> ButtonGroup1.add(Ljavax.swing.AbstractButton;)V)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM5() {
		try {
			// user code begin {1}
			// user code end
			getButtonGroupCivilized().add(getSelectButton());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM7:  (CircleButton.action.actionPerformed(java.awt.event.ActionEvent) --> ReactionCartoonTool.layout(Ljava.lang.String;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM7(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().layout(GraphLayoutManager.OldLayouts.CIRCULARIZER);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM8:  (SimpleGraphModelPanel.initialize() --> SimpleGraphModelTool1.setSimpleGraphModel(Lcbit.vcell.constraints.gui.SimpleGraphModel;)V)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM8() {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().setGraphModel(getWorkflowModel());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connEtoM9:  (GlgLayoutJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ReactionCartoonTool.layoutGlg()V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM9(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getWorkflowCartoonTool().layoutGlg();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connPtoP1SetSource:  (ButtonGroup1.selection <--> selection1.this)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP1SetSource() {
		/* Set the source from the target */
		try {
			if (ivjConnPtoP1Aligning == false) {
				// user code begin {1}
				// user code end
				ivjConnPtoP1Aligning = true;
				if ((getSelection() != null)) {
					getButtonGroupCivilized().setSelection(getSelection());
				}
				// user code begin {2}
				// user code end
				ivjConnPtoP1Aligning = false;
			}
		} catch (java.lang.Throwable ivjExc) {
			ivjConnPtoP1Aligning = false;
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connPtoP1SetTarget:  (ButtonGroup1.selection <--> selection1.this)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP1SetTarget() {
		/* Set the target from the source */
		try {
			if (ivjConnPtoP1Aligning == false) {
				// user code begin {1}
				// user code end
				ivjConnPtoP1Aligning = true;
				setSelection(getButtonGroupCivilized().getSelection());
				// user code begin {2}
				// user code end
				ivjConnPtoP1Aligning = false;
			}
		} catch (java.lang.Throwable ivjExc) {
			ivjConnPtoP1Aligning = false;
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * connPtoP2SetTarget:  (Selection.actionCommand <--> ReactionCartoonTool1.modeString)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP2SetTarget() {
		/* Set the target from the source */
		try {
			if ((getSelection() != null)) {
				getWorkflowCartoonTool().setModeString(getSelection().getActionCommand());
			}
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * The firePropertyChange method was generated to support the propertyChange field.
	 * @param propertyName java.lang.String
	 * @param oldValue java.lang.Object
	 * @param newValue java.lang.Object
	 */
	@Override
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
	}
	/**
	 * Return the AnnealLayoutButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getAnnealLayoutButton() {
		if (ivjAnnealLayoutButton == null) {
			try {
				ivjAnnealLayoutButton = new javax.swing.JButton();
				ivjAnnealLayoutButton.setName("AnnealLayoutButton");
				ivjAnnealLayoutButton.setText("anl");
				ivjAnnealLayoutButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjAnnealLayoutButton.setActionCommand("AnnealLayout");
				ivjAnnealLayoutButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjAnnealLayoutButton.setFont(new java.awt.Font("Arial", 1, 10));
				ivjAnnealLayoutButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjAnnealLayoutButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjAnnealLayoutButton;
	}
	/**
	 * Return the ButtonGroup1 property value.
	 * @return cbit.gui.ButtonGroupCivilized
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private org.vcell.util.gui.ButtonGroupCivilized getButtonGroupCivilized() {
		if (ivjButtonGroupCivilized == null) {
			try {
				ivjButtonGroupCivilized = new org.vcell.util.gui.ButtonGroupCivilized();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjButtonGroupCivilized;
	}
	/**
	 * Return the CircleLayoutButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getCircleLayoutButton() {
		if (ivjCircleLayoutButton == null) {
			try {
				ivjCircleLayoutButton = new javax.swing.JButton();
				ivjCircleLayoutButton.setName("CircleLayoutButton");
				ivjCircleLayoutButton.setText("crc");
				ivjCircleLayoutButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjCircleLayoutButton.setActionCommand("CircleLayout");
				ivjCircleLayoutButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjCircleLayoutButton.setFont(new java.awt.Font("Arial", 1, 10));
				ivjCircleLayoutButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjCircleLayoutButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCircleLayoutButton;
	}
	/**
	 * Comment
	 */
//	private java.lang.String getCycleizer() {
//		return CartoonTool.CYCLEIZER;
//	}
	/**
	 * Return the FeatureSizePanel property value.
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getFeatureSizePanel() {
		if (ivjFeatureSizePanel == null) {
			try {
				ivjFeatureSizePanel = new javax.swing.JPanel();
				ivjFeatureSizePanel.setName("FeatureSizePanel");
				ivjFeatureSizePanel.setPreferredSize(new java.awt.Dimension(22, 396));
				ivjFeatureSizePanel.setLayout(new java.awt.BorderLayout());
				ivjFeatureSizePanel.setMinimumSize(new java.awt.Dimension(22, 396));
				getFeatureSizePanel().add(getJPanel1(), "South");
				getFeatureSizePanel().add(getJScrollPane1(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFeatureSizePanel;
	}
	/**
	 * Comment
	 */
//	private java.lang.String getForceDirect() {
//		return CartoonTool.FORCEDIRECT;
//	}
	/**
	 * Return the GlgLayoutJButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getTransposeLayoutJButton() {
		if (ivjTransposeLayoutButton == null) {
			try {
				ivjTransposeLayoutButton = new javax.swing.JButton();
				ivjTransposeLayoutButton.setName("TransposeLayoutButton");
				ivjTransposeLayoutButton.setText("G'");
				ivjTransposeLayoutButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjTransposeLayoutButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjTransposeLayoutButton.setFont(new java.awt.Font("Arial", 1, 10));
				ivjTransposeLayoutButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjTransposeLayoutButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTransposeLayoutButton;
	}
	/**
	 * Return the GlgLayoutJButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getGlgLayoutJButton() {
		if (ivjGlgLayoutJButton == null) {
			try {
				ivjGlgLayoutJButton = new javax.swing.JButton();
				ivjGlgLayoutJButton.setName("GlgLayoutJButton");
				ivjGlgLayoutJButton.setText("glg");
				ivjGlgLayoutJButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjGlgLayoutJButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjGlgLayoutJButton.setFont(new java.awt.Font("Arial", 1, 10));
				ivjGlgLayoutJButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjGlgLayoutJButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjGlgLayoutJButton;
	}
	/**
	 * Gets the graph property (cbit.vcell.mapping.potential.Graph) value.
	 * @return The graph property value.
	 * @see #setGraph
	 */
	public Workflow getWorkflow(){
		return fieldWorkflow;
	}
	/**
	 * Return the GraphPane property value.
	 * @return cbit.vcell.graph.GraphPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private cbit.gui.graph.gui.GraphPane getGraphPane() {
		if (ivjGraphPane == null) {
			try {
				ivjGraphPane = new cbit.gui.graph.gui.GraphPane();
				ivjGraphPane.setName("GraphPane");
				ivjGraphPane.setBounds(0, 0, 372, 364);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjGraphPane;
	}
	/**
	 * Return the JLabel3 property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getJLabel3() {
		if (ivjJLabel3 == null) {
			try {
				ivjJLabel3 = new javax.swing.JLabel();
				ivjJLabel3.setName("JLabel3");
				ivjJLabel3.setText(" ");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJLabel3;
	}
	/**
	 * Return the JLabel4 property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getJLabel4() {
		if (ivjJLabel4 == null) {
			try {
				ivjJLabel4 = new javax.swing.JLabel();
				ivjJLabel4.setName("JLabel4");
				ivjJLabel4.setText(" ");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJLabel4;
	}
	/**
	 * Return the JPanel1 property value.
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1() {
		if (ivjJPanel1 == null) {
			try {
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel1;
	}
	/**
	 * Return the JScrollPane1 property value.
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getJScrollPane1() {
		if (ivjJScrollPane1 == null) {
			try {
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setPreferredSize(new java.awt.Dimension(22, 396));
				ivjJScrollPane1.setMinimumSize(new java.awt.Dimension(22, 396));
				getJScrollPane1().setViewportView(getGraphPane());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * Return the JToolBar1 property value.
	 * @return javax.swing.JToolBar
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JToolBar getJToolBar1() {
		if (ivjJToolBar1 == null) {
			try {
				ivjJToolBar1 = new javax.swing.JToolBar();
				ivjJToolBar1.setName("JToolBar1");
				ivjJToolBar1.setFloatable(false);
				ivjJToolBar1.setBorder(new javax.swing.border.EtchedBorder());
				ivjJToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
				getJToolBar1().add(getSelectButton(), getSelectButton().getName());
				getJToolBar1().add(getJLabel3(), getJLabel3().getName());
				getJToolBar1().add(getZoomInButton(), getZoomInButton().getName());
				getJToolBar1().add(getZoomOutButton(), getZoomOutButton().getName());
				getJToolBar1().add(getJLabel4(), getJLabel4().getName());
				getJToolBar1().add(getRandomLayoutButton(), getRandomLayoutButton().getName());
				getJToolBar1().add(getCircleLayoutButton(), getCircleLayoutButton().getName());
				getJToolBar1().add(getAnnealLayoutButton(), getAnnealLayoutButton().getName());
				getJToolBar1().add(getLevellerLayoutButton(), getLevellerLayoutButton().getName());
				getJToolBar1().add(getRelaxerLayoutButton(), getRelaxerLayoutButton().getName());
				getJToolBar1().add(getGlgLayoutJButton(), getGlgLayoutJButton().getName());
				getJToolBar1().add(getTransposeLayoutJButton(), getTransposeLayoutJButton().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJToolBar1;
	}
	/**
	 * Return the LevellerLayoutButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getLevellerLayoutButton() {
		if (ivjLevellerLayoutButton == null) {
			try {
				ivjLevellerLayoutButton = new javax.swing.JButton();
				ivjLevellerLayoutButton.setName("LevellerLayoutButton");
				ivjLevellerLayoutButton.setText("lev");
				ivjLevellerLayoutButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjLevellerLayoutButton.setActionCommand("LevellerLayout");
				ivjLevellerLayoutButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjLevellerLayoutButton.setFont(new java.awt.Font("Arial", 1, 10));
				ivjLevellerLayoutButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjLevellerLayoutButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLevellerLayoutButton;
	}
	/**
	 * Accessor for the propertyChange field.
	 * @return java.beans.PropertyChangeSupport
	 */
	protected java.beans.PropertyChangeSupport getPropertyChange() {
		if (propertyChange == null) {
			propertyChange = new java.beans.PropertyChangeSupport(this);
		};
		return propertyChange;
	}
	/**
	 * Return the RandomLayoutButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getRandomLayoutButton() {
		if (ivjRandomLayoutButton == null) {
			try {
				ivjRandomLayoutButton = new javax.swing.JButton();
				ivjRandomLayoutButton.setName("RandomLayoutButton");
				ivjRandomLayoutButton.setText("rnd");
				ivjRandomLayoutButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjRandomLayoutButton.setActionCommand("RandomLayout");
				ivjRandomLayoutButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjRandomLayoutButton.setFont(new java.awt.Font("Arial", 1, 10));
				ivjRandomLayoutButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjRandomLayoutButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRandomLayoutButton;
	}
	/**
	 * Return the RelaxerLayoutButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getRelaxerLayoutButton() {
		if (ivjRelaxerLayoutButton == null) {
			try {
				ivjRelaxerLayoutButton = new javax.swing.JButton();
				ivjRelaxerLayoutButton.setName("RelaxerLayoutButton");
				ivjRelaxerLayoutButton.setText("rlx");
				ivjRelaxerLayoutButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjRelaxerLayoutButton.setActionCommand("RelaxerLayout");
				ivjRelaxerLayoutButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjRelaxerLayoutButton.setFont(new java.awt.Font("Arial", 1, 10));
				ivjRelaxerLayoutButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjRelaxerLayoutButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRelaxerLayoutButton;
	}
	/**
	 * Return the JToolBarToggleButton2 property value.
	 * @return cbit.gui.JToolBarToggleButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private org.vcell.util.gui.JToolBarToggleButton getSelectButton() {
		if (ivjSelectButton == null) {
			try {
				ivjSelectButton = new org.vcell.util.gui.JToolBarToggleButton();
				ivjSelectButton.setName("SelectButton");
				ivjSelectButton.setText("");
				ivjSelectButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjSelectButton.setActionCommand(Mode.SELECT.getActionCommand());
				ivjSelectButton.setSelected(true);
				ivjSelectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/select.gif")));
				ivjSelectButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjSelectButton.setMinimumSize(new java.awt.Dimension(28, 28));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSelectButton;
	}
	/**
	 * Return the selection1 property value.
	 * @return javax.swing.ButtonModel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.ButtonModel getSelection() {
		// user code begin {1}
		// user code end
		return ivjSelection;
	}
	/**
	 * Return the SimpleGraphModel property value.
	 * @return cbit.vcell.graph.SimpleGraphModel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	public WorkflowGraphModel getWorkflowModel() {
		if (fieldWorkflowModel == null) {
			try {
				fieldWorkflowModel = new WorkflowGraphModel();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return fieldWorkflowModel;
	}
	/**
	 * Return the SimpleGraphModelTool1 property value.
	 * @return cbit.vcell.graph.SimpleGraphCartoonTool
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private WorkflowCartoonTool getWorkflowCartoonTool() {
		if (fieldWorkflowCartoonTool1 == null) {
			try {
				fieldWorkflowCartoonTool1 = new WorkflowCartoonTool();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return fieldWorkflowCartoonTool1;
	}
	/**
	 * Comment
	 */
//	private java.lang.String getStabilizer() {
//		return CartoonTool.STABILIZER;
//	}
	/**
	 * Return the ZoomInButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getZoomInButton() {
		if (ivjZoomInButton == null) {
			try {
				ivjZoomInButton = new javax.swing.JButton();
				ivjZoomInButton.setName("ZoomInButton");
				ivjZoomInButton.setText("");
				ivjZoomInButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjZoomInButton.setActionCommand("ZoomIn");
				ivjZoomInButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjZoomInButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ivjZoomInButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
				ZoomShapeIcon.setZoomOverlayEditorMod(ivjZoomInButton, ZoomShapeIcon.Sign.plus);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjZoomInButton;
	}
	/**
	 * Return the ZoomOutButton property value.
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getZoomOutButton() {
		if (ivjZoomOutButton == null) {
			try {
				ivjZoomOutButton = new javax.swing.JButton();
				ivjZoomOutButton.setName("ZoomOutButton");
				ivjZoomOutButton.setText("");
				ivjZoomOutButton.setMaximumSize(new java.awt.Dimension(28, 28));
				ivjZoomOutButton.setActionCommand("ZoomOut");
				ivjZoomOutButton.setPreferredSize(new java.awt.Dimension(28, 28));
				ivjZoomOutButton.setMinimumSize(new java.awt.Dimension(28, 28));
				ZoomShapeIcon.setZoomOverlayEditorMod(ivjZoomOutButton, ZoomShapeIcon.Sign.minus);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjZoomOutButton;
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	private void handleException(Throwable exception) {

		/* Uncomment the following lines to print uncaught exceptions to stdout */
		System.out.println("--------- UNCAUGHT EXCEPTION --------- in CartoonPanel");
		exception.printStackTrace(System.out);
	}
	/**
	 * Initializes connections
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getButtonGroupCivilized().addPropertyChangeListener(this);
		getZoomInButton().addActionListener(this);
		getZoomOutButton().addActionListener(this);
		getRelaxerLayoutButton().addActionListener(this);
		getCircleLayoutButton().addActionListener(this);
		getRandomLayoutButton().addActionListener(this);
		getGlgLayoutJButton().addActionListener(this);
		getTransposeLayoutJButton().addActionListener(this);
		getLevellerLayoutButton().addActionListener(this);
		getAnnealLayoutButton().addActionListener(this);
		this.addPropertyChangeListener(this);
		connPtoP1SetTarget();
		connPtoP2SetTarget();
	}
	/**
	 * Initialize class
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("CartoonPanel");
			setPreferredSize(new java.awt.Dimension(54, 425));
			setLayout(new java.awt.BorderLayout());
			setSize(472, 422);
			setMinimumSize(new java.awt.Dimension(54, 425));
			add(getFeatureSizePanel(), "Center");
			add(getJToolBar1(), "West");
			initConnections();
			connEtoM5();
			connEtoM10();
			connEtoM16();
			connEtoM8();
			connEtoM18();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			JFrame frame = new javax.swing.JFrame();
			WorkflowModelPanel aWorkflowModelPanel;
			aWorkflowModelPanel = new WorkflowModelPanel();
			frame.setContentPane(aWorkflowModelPanel);
			frame.setSize(aWorkflowModelPanel.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			LocalWorkspace localWorkspace = new LocalWorkspace(new File("C:\\temp"));
			Workflow workflow = new Workflow("temp");
			class Task1 extends Task {
				final DataInput<Double> in = new DataInput<Double>(Double.class,"in", this);
				final DataOutput<Double> out = new DataOutput<Double>(Double.class,"out", this);
				public Task1(){
					super("t1");
					addInput(in);
					addOutput(out);
				}
				@Override
				protected void compute0(TaskContext context, ClientTaskStatusSupport clientTaskStatusSupport) throws Exception {
					System.out.println("executing task "+getName());
				}
			};
			class Task2 extends Task {
				final DataInput<Double> in = new DataInput<Double>(Double.class,"in", this);
				final DataOutput<Double> out = new DataOutput<Double>(Double.class,"out", this);
				public Task2(){
					super("t2");
					addInput(in);
					addOutput(out);
				}
				@Override
				protected void compute0(TaskContext context, ClientTaskStatusSupport clientTaskStatusSupport) throws Exception {
					System.out.println("executing task "+getName());
				}
			};
			Task1 task1 = new Task1();
			workflow.addTask(task1);
			Task2 task2 = new Task2();
			workflow.addTask(task2);
			workflow.connect2(task1.out,  task2.in);
			
			aWorkflowModelPanel.setWorkflow(workflow);
			
			frame.setVisible(true);
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of javax.swing.JPanel");
			exception.printStackTrace(System.out);
		}
	}
	/**
	 * Method to handle events for the PropertyChangeListener interface.
	 * @param evt java.beans.PropertyChangeEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		// user code begin {1}
		// user code end
		if (evt.getSource() == getButtonGroupCivilized() && (evt.getPropertyName().equals("selection"))) 
			connPtoP1SetTarget();
		if (evt.getSource() == this && (evt.getPropertyName().equals("workflow"))) 
			connEtoM3(evt);
		// user code begin {2}
		// user code end
	}
	/**
	 * The removePropertyChangeListener method was generated to support the propertyChange field.
	 * @param listener java.beans.PropertyChangeListener
	 */
	@Override
	public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().removePropertyChangeListener(listener);
	}
	/**
	 * Sets the graph property (cbit.vcell.mapping.potential.Graph) value.
	 * @param graph The new value for the property.
	 * @see #getGraph
	 */
	public void setWorkflow(Workflow workflow) {
		Workflow oldValue = fieldWorkflow;
		fieldWorkflow = workflow;
		firePropertyChange("workflow", oldValue, workflow);
	}
	/**
	 * Set the selection1 to a new value.
	 * @param newValue javax.swing.ButtonModel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void setSelection(javax.swing.ButtonModel newValue) {
		if (ivjSelection != newValue) {
			try {
				ivjSelection = newValue;
				connPtoP1SetSource();
				connPtoP2SetTarget();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		};
		// user code begin {3}
		// user code end
	}

	private void zoomInButton_ActionPerformed() {
		if (getWorkflowModel()!=null){
			try { getWorkflowModel().getResizeManager().zoomIn(); } 
			catch (ZoomRangeException e) { } // Do nothing
			catch (GraphModel.NotReadyException e) { } // Do nothing
		}
	}

	private void zoomOutButton_ActionPerformed() {
		if (getWorkflowModel()!=null){
			try { getWorkflowModel().getResizeManager().zoomOut(); } 
			catch (ZoomRangeException e) { } // Do nothing
			catch (GraphModel.NotReadyException e) { } // Do nothing
		}
	}

}
