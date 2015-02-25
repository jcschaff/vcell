package org.vcell.model.rbm;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.vcell.util.gui.DialogUtils;
import org.vcell.util.gui.EditorScrollTable;

import cbit.vcell.client.ClientRequestManager;
import cbit.vcell.client.desktop.biomodel.IssueManager;
import cbit.vcell.client.desktop.biomodel.SelectionManager;
import cbit.vcell.client.task.ClientTaskDispatcher;
import cbit.vcell.graph.ReactionCartoonEditorPanel;
import cbit.vcell.mapping.MathMapping;
import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.model.Model;
import cbit.vcell.model.Model.RbmModelContainer;

public class NetworkConstraintsPanel extends JPanel {

	private JTextField maxIterationTextField;
	private JTextField maxMolTextField;
	private NetworkConstraints networkConstraints;
	private EventHandler eventHandler = new EventHandler();
	private SimulationContext fieldSimulationContext;
	private IssueManager fieldIssueManager;
	private SelectionManager fieldSelectionManager;
	
	private JLabel seedSpeciesLabel;
	private JLabel reactionRulesLabel;
	private JLabel generatedSpeciesLabel;
	private JLabel generatedReactionsLabel;
	private JButton viewGeneratedSpeciesButton;
	private JButton viewGeneratedReactionsButton;
	
	private JTextArea netGenConsoleText;
	private EditorScrollTable molecularTypeTable = null;
//	private ApplicationMolecularTypeTableModel molecularTypeTableModel = null;
	private JButton refreshMathButton;
	private JButton viewNetworkButton;
	private JButton cancelNetGenButton;
	
	private final static String consoleTextExample = "Iteration   0:     4 species      0 rxns\n" +
													"Iteration   1:     8 species      4 rxns\n" +
													"Iteration   2:    18 species     22 rxns\n" +
													"Iteration   3:    39 species     80 rxns\n" +
													"Iteration   4:    69 species    210 rxns\n" +
													"Iteration   5:   103 species    433 rxns\n" +
													"Iteration   6:   106 species    672 rxns\n" +
													"Iteration   7:   106 species    672 rxns\n" +
													"Iteration   8:   106 species    672 rxns\n" +
													"Iteration   9:   106 species    672 rxns\n" +
													"Iteration  10:   106 species    672 rxns\n" +
													"Iteration  11:   106 species    691 rxns\n";
	
	private class EventHandler implements FocusListener, ActionListener, PropertyChangeListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == maxIterationTextField) {
				changeMaxIteration();
			} else if (e.getSource() == maxMolTextField) {
				changeMaxMolPerSpecies();
			} else if(e.getSource() == getViewGeneratedSpeciesButton()) {
				viewGeneratedSpecies();
			} else if(e.getSource() == getViewGeneratedReactionsButton()) {
				viewGeneratedReactions();
			} else if(e.getSource() == getRefreshMathButton()) {
				refreshMath();
			} else if(e.getSource() == getViewNetworkButton()) {
				viewNetwork();
			} else if(e.getSource() == getCancelNetGenButton()) {
				cancelNetGen();
			}
		}

		public void focusGained(FocusEvent e) {
		}
		public void focusLost(FocusEvent e) {
			if (e.getSource() == maxIterationTextField) {
				changeMaxIteration();
			} else if (e.getSource() == maxMolTextField) {
				changeMaxMolPerSpecies();
			}
		}
		
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() instanceof SimulationContext && evt.getPropertyName().equals("mathDescription") && evt.getNewValue()!=null){
				refreshInterface();
			} else if(evt.getSource() instanceof Model && evt.getPropertyName().equals(RbmModelContainer.PROPERTY_NAME_MOLECULAR_TYPE_LIST)) {
				refreshInterface();
			}
		}
	}
	
	public NetworkConstraintsPanel() {
		super();
		initialize();
	}
	
	public void changeMaxMolPerSpecies() {
		String text = maxMolTextField.getText();
		if (text == null || text.trim().length() == 0) {
			return;
		}
		networkConstraints.setMaxMoleculesPerSpecies(Integer.valueOf(text));
	}

	public void changeMaxIteration() {
		String text = maxIterationTextField.getText();
		if (text == null || text.trim().length() == 0) {
			return;
		}
		networkConstraints.setMaxIteration(Integer.valueOf(text));
	}
	
	private JButton getViewGeneratedSpeciesButton() {
		if (viewGeneratedSpeciesButton == null) {
			viewGeneratedSpeciesButton = new javax.swing.JButton("View");
			viewGeneratedSpeciesButton.setName("ViewGeneratedSpeciesButton");
		}
		return viewGeneratedSpeciesButton;
	}
	private JButton getViewGeneratedReactionsButton() {
		if (viewGeneratedReactionsButton == null) {
			viewGeneratedReactionsButton = new javax.swing.JButton("View");
			viewGeneratedReactionsButton.setName("ViewGeneratedReactionsButton");
		}
		return viewGeneratedReactionsButton;
	}
	private JButton getRefreshMathButton() {
		if (refreshMathButton == null) {
			refreshMathButton = new javax.swing.JButton(" Refresh Math ");
			refreshMathButton.setName("RefreshMathButton");
		}
		return refreshMathButton;
	}
	private JButton getViewNetworkButton() {
		if (viewNetworkButton == null) {
			viewNetworkButton = new javax.swing.JButton(" View Network ");
			viewNetworkButton.setName("ViewNetworkButton");
		}
		return viewNetworkButton;
	}
	private JButton getCancelNetGenButton() {
		if (cancelNetGenButton == null) {
			cancelNetGenButton = new javax.swing.JButton("Cancel NetGen");
			cancelNetGenButton.setName("CancelNetGenButton");
		}
		return cancelNetGenButton;
	}


	private void initialize() {
		netGenConsoleText = new JTextArea();
		maxIterationTextField = new JTextField();
		maxMolTextField = new JTextField();
		seedSpeciesLabel = new JLabel();
		reactionRulesLabel = new JLabel();
		generatedSpeciesLabel = new JLabel();
		generatedReactionsLabel = new JLabel();
		molecularTypeTable = new EditorScrollTable();
//		molecularTypeTableModel = new ApplicationMolecularTypeTableModel(molecularTypeTable);
		
		JScrollPane netGenConsoleScrollPane = new JScrollPane(netGenConsoleText);
		netGenConsoleScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		maxIterationTextField.addActionListener(eventHandler);
		maxMolTextField.addActionListener(eventHandler);
		
		getViewGeneratedSpeciesButton().addActionListener(eventHandler);
		getViewGeneratedReactionsButton().addActionListener(eventHandler);
		getRefreshMathButton().addActionListener(eventHandler);
		getViewNetworkButton().addActionListener(eventHandler);
		getCancelNetGenButton().addActionListener(eventHandler);
		
		netGenConsoleText.addFocusListener(eventHandler);
		maxIterationTextField.addFocusListener(eventHandler);
		maxMolTextField.addFocusListener(eventHandler);
		
		// ------------------------------------------- The 2 group boxes --------------------------
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		Border loweredEtchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();

		TitledBorder titleLeft = BorderFactory.createTitledBorder(loweredEtchedBorder, " Rules ");
		titleLeft.setTitleJustification(TitledBorder.LEFT);
		titleLeft.setTitlePosition(TitledBorder.TOP);

		TitledBorder titleRight = BorderFactory.createTitledBorder(loweredEtchedBorder, " Networking ");
		titleRight.setTitleJustification(TitledBorder.LEFT);
		titleRight.setTitlePosition(TitledBorder.TOP);
		
		leftPanel.setBorder(titleLeft);
		rightPanel.setBorder(titleRight);
		netGenConsoleScrollPane.setBorder(loweredBevelBorder);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 3, 2, 1);
		add(leftPanel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 1, 2, 3);	//  top, left, bottom, right 
		add(rightPanel, gbc);

		// ------------------------------------------- Populating the left group box ---------------
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		
		leftPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		leftPanel.add(top, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		leftPanel.add(bottom, gbc);
		
		top.setLayout(new GridBagLayout());		// --- top
		int gridy = 0;
		gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(15, 10, 4, 4);
		top.add(seedSpeciesLabel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(15, 4, 4, 10);
		top.add(reactionRulesLabel, gbc);
		
		gridy++;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = gridy;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(12, 10, 4, 4);
		top.add(new JLabel("Max Iteration"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(12, 4, 4, 10);
		top.add(maxIterationTextField, gbc);
		
		gridy++;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = gridy;
		gbc.insets = new Insets(5, 10, 4, 4);
		gbc.anchor = GridBagConstraints.LINE_START;
		top.add(new JLabel("Max Molecules/Species"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(5, 4, 4, 10);
		top.add(maxMolTextField, gbc);

		bottom.setLayout(new GridBagLayout());		// --- bottom
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(20, 4, 4, 10);
		bottom.add(molecularTypeTable, gbc);

		
		// ------------------------------------------- Populating the right group box ------------
		top = new JPanel();
		bottom = new JPanel();
		
		rightPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		rightPanel.add(top, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		rightPanel.add(bottom, gbc);
		
		top.setLayout(new GridBagLayout());		// --- top
		gridy = 0;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 4, 4, 10);
		top.add(generatedSpeciesLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = gridy;
//		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 4, 4, 10);
		top.add(getViewGeneratedSpeciesButton(), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = gridy;
//		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 4, 4, 10);
		top.add(getRefreshMathButton(), gbc);

		gridy++;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 4, 4, 10);
		top.add(generatedReactionsLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = gridy;
//		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 4, 4, 10);
		top.add(getViewGeneratedReactionsButton(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = gridy;
//		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 4, 4, 10);
		top.add(getViewNetworkButton(), gbc);

		bottom.setLayout(new GridBagLayout());		// --- bottom
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1.0;			// get all the available space
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(20, 4, 4, 10);
		bottom.add(netGenConsoleScrollPane, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
//		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(20, 4, 4, 10);
		bottom.add(getCancelNetGenButton(), gbc);

		netGenConsoleText.setFont(new Font("monospaced", Font.PLAIN, 11));
//		netGenConsoleText.setBorder(loweredBevelBorder);
		netGenConsoleText.setEditable(false);
	}
	
	public void setNetworkConstraints(NetworkConstraints newValue) {
		if (networkConstraints == newValue) {
			return;
		}
		networkConstraints = newValue;
		refreshInterface();
	}
	
	public void setSimulationContext(SimulationContext simulationContext) {
		if(simulationContext == null) {
			return;
		}
		if(fieldSimulationContext != null) {
			fieldSimulationContext.removePropertyChangeListener(eventHandler);
		}
		fieldSimulationContext = simulationContext;
		fieldSimulationContext.addPropertyChangeListener(eventHandler);
		
		Model m = fieldSimulationContext.getModel();
		if(m != null) {
			m.removePropertyChangeListener(eventHandler);
			m.addPropertyChangeListener(eventHandler);
		}
		refreshInterface();
	}

	public void setSelectionManager(SelectionManager selectionManager) {
		fieldSelectionManager = selectionManager;
	}
	public void setIssueManager(IssueManager issueManager) {
		fieldIssueManager = issueManager;
	}
	
	private void refreshInterface() {
		String text1 = null;
		String text2 = null;
		if (networkConstraints != null) {
			text1 = networkConstraints.getMaxIteration() + "";
			text2 = networkConstraints.getMaxMoleculesPerSpecies() + "";
		}
		maxIterationTextField.setText(text1);
		maxMolTextField.setText(text2);
		
		MathMapping mm = fieldSimulationContext.getMostRecentlyCreatedMathMapping();
		String text3 = null;
		String text4 = null;
		String text5 = null;
		String text6 = null;
		if(mm != null) {
			text3 = fieldSimulationContext.getModel().getNumSpeciesContexts() + "";
			int numReactions = fieldSimulationContext.getModel().getNumReactions();
			numReactions += fieldSimulationContext.getModel().getRbmModelContainer().getReactionRuleList().size();
			text5 = numReactions + "";
			if(fieldSimulationContext.getModel().getRbmModelContainer().isEmpty()) {
				text4 = "N/A (rule based model needed)";
				text6 = "N/A (rule based model needed)";
			} else {
				text4 = mm.getSimulationContext().getModel().getNumSpeciesContexts() + "";
				text6 = mm.getSimulationContext().getModel().getNumReactions() + "";
			}
		} else {
			text3 = fieldSimulationContext.getModel().getNumSpeciesContexts() + "";
			int numReactions = fieldSimulationContext.getModel().getNumReactions();
			numReactions += fieldSimulationContext.getModel().getRbmModelContainer().getReactionRuleList().size();
			text5 = numReactions + "";
			if(fieldSimulationContext.getModel().getRbmModelContainer().isEmpty()) {
				text4 = "N/A (rule based model needed)";
				text6 = "N/A (rule based model needed)";
			} else {
				text4 = "unavailable (no math mapping)";
				text6 = "unavailable (no math mapping)";
			}
		}
		seedSpeciesLabel.setText("Seed Species: " + text3);
		generatedSpeciesLabel.setText("Generated Species: " + text4);
		reactionRulesLabel.setText("Reaction Rules: " + text5);
		generatedReactionsLabel.setText("Generated Reactions: " + text6);
		
		netGenConsoleText.setText(consoleTextExample);
	}
	
	private void refreshMath() {
		ClientTaskDispatcher.dispatch(NetworkConstraintsPanel.this, new Hashtable<String, Object>(), ClientRequestManager.updateMath(this, fieldSimulationContext), false);
	}
	private void viewNetwork() {
		try {
			ReactionCartoonEditorPanel reactionCartoonEditorPanel;
			reactionCartoonEditorPanel = new ReactionCartoonEditorPanel();
			reactionCartoonEditorPanel.setSize(300, 100);
			
			MathMapping mm = fieldSimulationContext.getMostRecentlyCreatedMathMapping();
			reactionCartoonEditorPanel.setModel(mm.getSimulationContext().getModel());
			
			DialogUtils.showComponentCloseDialog(this, reactionCartoonEditorPanel, "Flattened reaction diagram.");
		} catch (Throwable exception) {
			System.err.println("Exception occurred viewing Network");
			exception.printStackTrace(System.out);
		}
	}
	private void cancelNetGen() {
		System.out.println("cancelNetGen button pressed");
	}
	private void viewGeneratedSpecies() {
		System.out.println("viewGeneratedSpecies button pressed");
	}
	private void viewGeneratedReactions() {
		System.out.println("viewGeneratedReactions button pressed");
	}


}
