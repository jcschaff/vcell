package cbit.vcell.client.desktop.biomodel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.vcell.util.gui.DialogUtils;
import org.vcell.util.gui.GuiUtils;

import cbit.vcell.client.desktop.DatabaseWindowPanel;
import cbit.vcell.client.desktop.biomodel.DocumentEditorTreeModel.DocumentEditorTreeFolderClass;
import cbit.vcell.client.desktop.biomodel.DocumentEditorTreeModel.DocumentEditorTreeFolderNode;
import cbit.vcell.client.desktop.mathmodel.MathModelEditor;
import cbit.vcell.desktop.BioModelMetaDataPanel;
import cbit.vcell.desktop.BioModelNode;
import cbit.vcell.desktop.GeometryMetaDataPanel;
import cbit.vcell.desktop.MathModelMetaDataPanel;
import cbit.vcell.mapping.BioEvent;
import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.math.AnnotatedFunction;
import cbit.vcell.model.Feature;
import cbit.vcell.model.Membrane;
import cbit.vcell.model.Model.ModelParameter;
import cbit.vcell.model.ReactionStep;
import cbit.vcell.model.SpeciesContext;
import cbit.vcell.solver.Simulation;
import cbit.vcell.xml.gui.MiriamTreeModel.LinkNode;
/**
 * Insert the type's description here.
 * Creation date: (5/3/2004 2:55:18 PM)
 * @author: Ion Moraru
 */
@SuppressWarnings("serial")
public abstract class DocumentEditor extends JPanel {
	protected enum DocumentEditorPopupMenuAction {
		add_new,
		delete,
		add_new_app_deterministic,
		add_new_app_stochastic,
		copy_app,
		rename,
	}
	protected static final double DEFAULT_DIVIDER_LOCATION = 0.68;
	protected final static int RIGHT_BOTTOM_TAB_PROPERTIES_INDEX = 0;
	protected static final String DATABASE_PROPERTIES_TAB_TITLE = "Database File Info";
	protected IvjEventHandler eventHandler = new IvjEventHandler();
	
	protected JTree documentEditorTree = null;
	protected SelectionManager selectionManager = new SelectionManager();

	protected JPanel emptyPanel = new JPanel();
	private JPopupMenu popupMenu = null;
	private JMenu addNewAppMenu = null;
	private JMenuItem addNewAppDeterministicMenuItem = null;
	private JMenuItem addNewAppStochasticMenuItem = null;
	private JMenuItem expandAllMenuItem = null;
	private JMenuItem collapseAllMenuItem = null;
	private JMenuItem addNewMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem renameMenuItem;
	
	protected DatabaseWindowPanel databaseWindowPanel = null;
	protected JTabbedPane leftBottomTabbedPane = null;
	protected JSplitPane rightSplitPane = null;
	protected BioModelMetaDataPanel bioModelMetaDataPanel = null;
	protected MathModelMetaDataPanel mathModelMetaDataPanel = null;
	protected GeometryMetaDataPanel geometryMetaDataPanel = null;
	
	protected JTabbedPane rightBottomTabbedPane = null;
	protected JPanel rightBottomEmptyPanel = null;
	private JSeparator popupMenuSeparator = null;
	private DocumentEditorTreeCellEditor documentEditorTreeCellEditor;
	private TreePath mouseClickPath = null;
	private long mouseClickPathTimeStamp = System.currentTimeMillis();
	
	private class IvjEventHandler implements ActionListener, PropertyChangeListener,TreeSelectionListener, MouseListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == expandAllMenuItem) {
				TreePath[] selectedPaths = documentEditorTree.getSelectionPaths();
				for (TreePath tp : selectedPaths) {
					Object lastSelectedPathComponent = tp.getLastPathComponent();
					if (lastSelectedPathComponent instanceof BioModelNode) {
						GuiUtils.treeExpandAll(documentEditorTree, (BioModelNode)lastSelectedPathComponent, true);
					}
				}
			} else if (e.getSource() == collapseAllMenuItem) {
				TreePath[] selectedPaths = documentEditorTree.getSelectionPaths();
				for (TreePath tp : selectedPaths) {
					Object lastSelectedPathComponent = tp.getLastPathComponent();
					if (lastSelectedPathComponent instanceof BioModelNode) {
						BioModelNode selectedNode = (BioModelNode)lastSelectedPathComponent;
						if (selectedNode.getParent() == null) {// root
							for (int i = 0; i < selectedNode.getChildCount(); i ++) {
								GuiUtils.treeExpandAll(documentEditorTree, (BioModelNode) selectedNode.getChildAt(i), false);
							}
						} else {
							GuiUtils.treeExpandAll(documentEditorTree, selectedNode, false);
						}
					}
				}
			} else if (e.getSource() == addNewMenuItem) {
				popupMenuActionPerformed(DocumentEditorPopupMenuAction.add_new);
			} else if (e.getSource() == deleteMenuItem) {
				popupMenuActionPerformed(DocumentEditorPopupMenuAction.delete);
			} else if (e.getSource() == renameMenuItem) {
				documentEditorTree.startEditingAtPath(documentEditorTree.getSelectionPath());
			} else if (e.getSource() == addNewAppDeterministicMenuItem) {
				popupMenuActionPerformed(DocumentEditorPopupMenuAction.add_new_app_deterministic);
			} else if (e.getSource() == addNewAppStochasticMenuItem) {
				popupMenuActionPerformed(DocumentEditorPopupMenuAction.add_new_app_stochastic);
			}
		};
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() == selectionManager) {
				onSelectedObjectsChange();
			}
		};
		
		public void mouseClicked(MouseEvent e) {
			TreePath oldClickPath = mouseClickPath;
			long oldClickTimeStamp = mouseClickPathTimeStamp;			
			mouseClickPath = null;
			if (e.getClickCount() == 1) {
				if (!isClickPathSelected(e)) {
					return;
				}
				TreePath[] newClickPaths = documentEditorTree.getSelectionPaths();
				if (newClickPaths != null && newClickPaths.length == 1) {
					mouseClickPath = newClickPaths[0];
					mouseClickPathTimeStamp = System.currentTimeMillis();
				}
				if (oldClickPath != null && mouseClickPath != null && oldClickPath.getLastPathComponent() == mouseClickPath.getLastPathComponent()) {
					if (mouseClickPathTimeStamp - oldClickTimeStamp > 2000) {
						return;
					}
					if (isRenamable(mouseClickPath)) {
						documentEditorTree.startEditingAtPath(mouseClickPath);
					}
				}
			} else if (e.getClickCount() == 2) {
				Object node = documentEditorTree.getLastSelectedPathComponent();
				if (node instanceof LinkNode) {
					String link = ((LinkNode)node).getLink();
					if (link != null) {
						DialogUtils.browserLauncher(documentEditorTree, link, "failed to launch", false);
					}
				}
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			if (e.getSource() == documentEditorTree) {
				documentEditorTree_tryPopupTrigger(e);
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == documentEditorTree) {
				documentEditorTree_tryPopupTrigger(e);
			}
		}

		
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			if (e.getSource() == documentEditorTree) {
				treeSelectionChanged0(e);
			}
		}
	};

/**
 * BioModelEditor constructor comment.
 */
public DocumentEditor() {
	super();
	initialize();
}

protected abstract void popupMenuActionPerformed(DocumentEditorPopupMenuAction action);

public void onSelectedObjectsChange() {
	Object[] selectedObjects = selectionManager.getSelectedObjects();
	setRightBottomPanelOnSelection(selectedObjects);
}

private boolean isClickPathSelected(MouseEvent e) {
	Point mousePoint = e.getPoint();
	TreePath path = documentEditorTree.getPathForLocation(mousePoint.x, mousePoint.y);
    if (path == null) {
    	return false; 
    }
	Object rightClickNode = path.getLastPathComponent();
	if (rightClickNode == null || !(rightClickNode instanceof BioModelNode)) {
		return false;
	}
	TreePath[] selectedPaths = documentEditorTree.getSelectionPaths();
	if (selectedPaths == null || selectedPaths.length == 0) {
		return false;
	} 
	boolean bFound = false;
	for (TreePath tp : selectedPaths) {
		if (tp.equals(path)) {
			bFound = true;
			break;
		}
	}
	return bFound;
}
private void documentEditorTree_tryPopupTrigger(MouseEvent e) {
	if (e.isPopupTrigger()) {	
		if (isClickPathSelected(e)) {
			Point mousePoint = e.getPoint();
			getPopupMenu().show(documentEditorTree, mousePoint.x, mousePoint.y);
		}
	}
}	

/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	exception.printStackTrace(System.out);
}

/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		setLayout(new BorderLayout());
				
		documentEditorTree = new javax.swing.JTree();
		documentEditorTree.setEditable(true);
		documentEditorTreeCellEditor = new DocumentEditorTreeCellEditor(documentEditorTree);
		documentEditorTree.setCellEditor(documentEditorTreeCellEditor);
		documentEditorTree.setName("bioModelEditorTree");
		ToolTipManager.sharedInstance().registerComponent(documentEditorTree);			
		documentEditorTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		int rowHeight = documentEditorTree.getRowHeight();
		if(rowHeight < 10) { 
			rowHeight = 20; 
		}
		documentEditorTree.setRowHeight(rowHeight + 2);
		
		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		databaseWindowPanel = new DatabaseWindowPanel(false, false);
		leftBottomTabbedPane  = new JTabbedPane();
		leftBottomTabbedPane.addTab("VCell Database", databaseWindowPanel);
		
		JScrollPane treePanel = new javax.swing.JScrollPane(documentEditorTree);		
		treePanel.setMinimumSize(new java.awt.Dimension(198, 148));
		leftSplitPane.setTopComponent(treePanel);
		leftBottomTabbedPane.setMinimumSize(new java.awt.Dimension(198, 148));
		leftSplitPane.setBottomComponent(leftBottomTabbedPane);
		leftSplitPane.setResizeWeight(0.5);
		leftSplitPane.setDividerLocation(300);
		leftSplitPane.setDividerSize(8);
		leftSplitPane.setOneTouchExpandable(true);

		rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		rightSplitPane.setResizeWeight(0.7);
		rightSplitPane.setDividerLocation(550);
		rightSplitPane.setDividerSize(8);
		rightSplitPane.setOneTouchExpandable(true);
		
		rightBottomEmptyPanel = new JPanel(new GridBagLayout());
		rightBottomEmptyPanel.setBackground(Color.white);
		JLabel label = new JLabel("Select only one object (e.g. species, reaction, simulation) to show properties.");
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.insets = new Insets(10,10,4,4);
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_START;
		rightBottomEmptyPanel.add(label, gbc);
		
		rightBottomTabbedPane = new JTabbedPane();
		rightBottomTabbedPane.addTab("Object Properties", rightBottomEmptyPanel);		
		rightBottomTabbedPane.setMinimumSize(new java.awt.Dimension(198, 148));		
		rightSplitPane.setBottomComponent(rightBottomTabbedPane);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(270);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.3);
		splitPane.setDividerSize(8);
		splitPane.setLeftComponent(leftSplitPane);
		splitPane.setRightComponent(rightSplitPane);
		
		add(splitPane, BorderLayout.CENTER);
		
		
		selectionManager.addPropertyChangeListener(eventHandler);
		
		databaseWindowPanel.setSelectionManager(selectionManager);
		documentEditorTree.addTreeSelectionListener(eventHandler);
		documentEditorTree.addMouseListener(eventHandler);
		
		bioModelMetaDataPanel = new BioModelMetaDataPanel();
		bioModelMetaDataPanel.setSelectionManager(selectionManager);
		mathModelMetaDataPanel = new MathModelMetaDataPanel();
		mathModelMetaDataPanel.setSelectionManager(selectionManager);
		geometryMetaDataPanel = new GeometryMetaDataPanel();
		geometryMetaDataPanel.setSelectionManager(selectionManager);

	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}

private void treeSelectionChanged0(TreeSelectionEvent treeSelectionEvent) {
	try {
		treeSelectionChanged();
		TreePath[] paths = documentEditorTree.getSelectionModel().getSelectionPaths();
		List<Object> selectedObjects = new ArrayList<Object>();
		if (paths != null) {
			for (TreePath path : paths) {
				Object node = path.getLastPathComponent();
				if (node != null && (node instanceof BioModelNode)) {
					selectedObjects.add(((BioModelNode)node).getUserObject());
				}
			}
		}
		if (selectedObjects.size() > 0) {
			selectionManager.setSelectedObjects(selectedObjects.toArray());
		}
	}catch (Exception ex){
		ex.printStackTrace(System.out);
	}
}

protected abstract void setRightBottomPanelOnSelection(Object[] selections);
protected abstract void treeSelectionChanged();

protected SimulationContext getSelectedSimulationContext() {
	if (this instanceof MathModelEditor) {
		return null;
	}
	
	// make sure only one simulation context is selected
	TreePath[] selectedPaths = documentEditorTree.getSelectionPaths();
	SimulationContext simulationContext = null;
	for (TreePath path : selectedPaths) {
		Object node = path.getLastPathComponent();
		if (!(node instanceof BioModelNode)) {
			return null;
		}
		BioModelNode n = (BioModelNode)node;
		while (true) {
			Object userObject = n.getUserObject();
			if (userObject instanceof SimulationContext) {
				if (simulationContext == null) {
					simulationContext = (SimulationContext)userObject;
					break;
				} else if (simulationContext != userObject) {
					return null;
				}
			}
			TreeNode parent = n.getParent();
			if (parent == null || !(parent instanceof BioModelNode)) {
				break;
			}
			n = (BioModelNode)parent;
		}
	}
	return simulationContext;
}

private boolean isRenamable(TreePath path) {
	Object obj = path.getLastPathComponent();
	if (obj != null && (obj instanceof BioModelNode)) {	
		BioModelNode selectedNode = (BioModelNode) obj;
		Object userObject = selectedNode.getUserObject();
		if (userObject instanceof ReactionStep
				|| userObject instanceof Feature
				|| userObject instanceof SpeciesContext
				|| userObject instanceof ModelParameter
				|| userObject instanceof SimulationContext
				|| userObject instanceof Simulation
				|| userObject instanceof Membrane
				|| userObject instanceof BioEvent) {			
			return true;
		}
	}
	return false;
}
private void construcutPopupMenu() {
	popupMenu.removeAll();
	TreePath[] selectedPaths = documentEditorTree.getSelectionPaths();
	boolean bDelete = false;
	boolean bRename = false;
	boolean bExpand = true;
	boolean bAddNew = false;
	boolean bAddNewApp = false;
	for (TreePath tp : selectedPaths) {
		Object obj = tp.getLastPathComponent();
		if (obj == null || !(obj instanceof BioModelNode)) {
			continue;
		}
		if (documentEditorTree.getModel().isLeaf(obj)) {
			bExpand = false;
		}
		
		BioModelNode selectedNode = (BioModelNode) obj;
		Object userObject = selectedNode.getUserObject();
		if (userObject instanceof DocumentEditorTreeFolderNode) {
			DocumentEditorTreeFolderClass folderClass = ((DocumentEditorTreeFolderNode) userObject).getFolderClass();
			if (folderClass == DocumentEditorTreeFolderClass.APPLICATTIONS_NODE) {
				bAddNewApp = true;
			} else if (folderClass == DocumentEditorTreeFolderClass.REACTIONS_NODE
					|| folderClass == DocumentEditorTreeFolderClass.STRUCTURES_NODE
					|| folderClass == DocumentEditorTreeFolderClass.SPECIES_NODE
					|| folderClass == DocumentEditorTreeFolderClass.GLOBAL_PARAMETER_NODE
					|| folderClass == DocumentEditorTreeFolderClass.SIMULATIONS_NODE
					|| folderClass == DocumentEditorTreeFolderClass.EVENTS_NODE
//					|| folderClass == DocumentEditorTreeFolderClass.OUTPUT_FUNCTIONS_NODE // can't do this now since add new is pop up right now.
					|| folderClass == DocumentEditorTreeFolderClass.MATH_SIMULATIONS_NODE
				) {
				bAddNew = (selectedPaths.length == 1);
				bDelete = false;
				bRename = false;
			}
		} else if (userObject instanceof ReactionStep
				|| userObject instanceof Feature
				|| userObject instanceof SpeciesContext
				|| userObject instanceof ModelParameter
				|| userObject instanceof SimulationContext
				|| userObject instanceof Simulation
			) {			
			bDelete = true;
			bRename = true;
		} else if (userObject instanceof Membrane) {
			bDelete = false;
			bRename = true;
		} else {
			SimulationContext selectedSimulationContext = getSelectedSimulationContext();
			if (userObject instanceof BioEvent) {
				bDelete = selectedSimulationContext != null;
				bRename = true;			
			} else if (userObject instanceof AnnotatedFunction) {			
				bDelete = selectedSimulationContext != null || this instanceof MathModelEditor;
				bRename = false;
			}
		}
	}
	if (selectedPaths.length != 1) {
		bRename = false;
	}
	if (bAddNewApp) {
		if (addNewAppMenu == null) {
			addNewAppMenu = new JMenu("Add New");
			addNewAppDeterministicMenuItem = new JMenuItem(BioModelEditorApplicationsPanel.MENU_TEXT_DETERMINISTIC_APPLICATION);
			addNewAppDeterministicMenuItem.addActionListener(eventHandler);
			addNewAppStochasticMenuItem = new JMenuItem(BioModelEditorApplicationsPanel.MENU_TEXT_STOCHASTIC_APPLICATION);
			addNewAppStochasticMenuItem.addActionListener(eventHandler);
			addNewAppMenu.add(addNewAppDeterministicMenuItem);
			addNewAppMenu.add(addNewAppStochasticMenuItem);
		}
		popupMenu.add(addNewAppMenu);
	}
	if (bAddNew) {
		if (addNewMenuItem == null) {
			addNewMenuItem = new javax.swing.JMenuItem("Add New");
			addNewMenuItem.addActionListener(eventHandler);
		}
		popupMenu.add(addNewMenuItem);
	}
	if (bRename) {
		if (renameMenuItem == null) {
			renameMenuItem = new javax.swing.JMenuItem("Rename");
			renameMenuItem.addActionListener(eventHandler);
		}
		popupMenu.add(renameMenuItem);
	}
	if (bDelete) {
		if (deleteMenuItem == null) {
			deleteMenuItem = new javax.swing.JMenuItem("Delete");
			deleteMenuItem.addActionListener(eventHandler);
		}
		popupMenu.add(deleteMenuItem);
	}
	
	if (bExpand) {
		if (expandAllMenuItem == null) {
			popupMenuSeparator = new JSeparator();
			expandAllMenuItem = new javax.swing.JMenuItem("Expand All");
			collapseAllMenuItem = new javax.swing.JMenuItem("Collapse All");
			expandAllMenuItem.addActionListener(eventHandler);
			collapseAllMenuItem.addActionListener(eventHandler);
		}
		if (bAddNew || bDelete || bAddNewApp || bRename) {
			popupMenu.add(popupMenuSeparator);
		}
		popupMenu.add(expandAllMenuItem);
		popupMenu.add(collapseAllMenuItem);
	}
}

private JPopupMenu getPopupMenu() {
	if (popupMenu == null) {
		popupMenu = new javax.swing.JPopupMenu();	
	}
	construcutPopupMenu();
	return popupMenu;
}

}