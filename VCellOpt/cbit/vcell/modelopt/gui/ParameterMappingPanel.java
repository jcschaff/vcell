/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cbit.vcell.modelopt.gui;

import cbit.gui.ScopedExpressionTableCellRenderer;

/**
 * Insert the type's description here.
 * Creation date: (9/23/2003 12:23:30 PM)
 * @author: Jim Schaff
 */
public class ParameterMappingPanel extends javax.swing.JPanel {
	private javax.swing.JScrollPane ivjJScrollPane1 = null;
	private cbit.vcell.messaging.admin.sorttable.JSortTable ivjScrollPaneTable = null;
	private ParameterMappingTableModel ivjparameterMappingTableModel = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private cbit.vcell.modelopt.ParameterEstimationTask fieldParameterEstimationTask = null;
	private ParameterMappingTableCellRenderer ivjParameterMappingTableCellRenderer1 = null;

class IvjEventHandler implements java.beans.PropertyChangeListener {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() == ParameterMappingPanel.this && (evt.getPropertyName().equals("parameterEstimationTask"))) 
				connEtoM1(evt);
		};
	};

/**
 * ModelParameterPanel constructor comment.
 */
public ParameterMappingPanel() {
	super();
	initialize();
}

/**
 * ModelParameterPanel constructor comment.
 * @param layout java.awt.LayoutManager
 */
public ParameterMappingPanel(java.awt.LayoutManager layout) {
	super(layout);
}


/**
 * ModelParameterPanel constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public ParameterMappingPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}


/**
 * ModelParameterPanel constructor comment.
 * @param isDoubleBuffered boolean
 */
public ParameterMappingPanel(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}


/**
 * connEtoM1:  (ParameterMappingPanel.parameterEstimationTask --> parameterMappingTableModel.parameterEstimationTask)
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.beans.PropertyChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getparameterMappingTableModel().setParameterEstimationTask(this.getParameterEstimationTask());
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoM2:  (ParameterMappingPanel.initialize() --> ScrollPaneTable.setDefaultRenderer(Ljava.lang.Class;Ljavax.swing.table.TableCellRenderer;)V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2() {
	try {
		// user code begin {1}
		// user code end
		getScrollPaneTable().setDefaultRenderer(Double.class, getParameterMappingTableCellRenderer1());
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}

/**
 * connEtoM3:  (ModelParameterPanel.initialize() --> ScrollPaneTable.setDefaultRenderer(Ljava.lang.Class;Ljavax.swing.table.TableCellRenderer;)V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM3() {
	try {
		// user code begin {1}
		// user code end
		getScrollPaneTable().setDefaultRenderer(String.class, new ScopedExpressionTableCellRenderer());
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoM4:  (ModelParameterPanel.initialize() --> ScrollPaneTable.setDefaultRenderer(Ljava.lang.Class;Ljavax.swing.table.TableCellRenderer;)V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM4() {
	try {
		// user code begin {1}
		// user code end
		getScrollPaneTable().setDefaultRenderer(cbit.vcell.parser.ScopedExpression.class, new ScopedExpressionTableCellRenderer());
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}

/**
 * connPtoP1SetTarget:  (modelParameterTableModel.this <--> ScrollPaneTable.model)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP1SetTarget() {
	/* Set the target from the source */
	try {
		getScrollPaneTable().setModel(getparameterMappingTableModel());
		getScrollPaneTable().createDefaultColumnsFromModel();
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
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
			ivjJScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			ivjJScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			getJScrollPane1().setViewportView(getScrollPaneTable());
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
 * Gets the parameterEstimationTask property (cbit.vcell.modelopt.ParameterEstimationTask) value.
 * @return The parameterEstimationTask property value.
 * @see #setParameterEstimationTask
 */
public cbit.vcell.modelopt.ParameterEstimationTask getParameterEstimationTask() {
	return fieldParameterEstimationTask;
}


/**
 * Return the ParameterMappingTableCellRenderer1 property value.
 * @return cbit.vcell.modelopt.gui.ParameterMappingTableCellRenderer
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private ParameterMappingTableCellRenderer getParameterMappingTableCellRenderer1() {
	if (ivjParameterMappingTableCellRenderer1 == null) {
		try {
			ivjParameterMappingTableCellRenderer1 = new cbit.vcell.modelopt.gui.ParameterMappingTableCellRenderer();
			ivjParameterMappingTableCellRenderer1.setName("ParameterMappingTableCellRenderer1");
			ivjParameterMappingTableCellRenderer1.setText("ParameterMappingTableCellRenderer1");
			ivjParameterMappingTableCellRenderer1.setBounds(600, 458, 225, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterMappingTableCellRenderer1;
}


/**
 * Return the parameterMappingTableModel property value.
 * @return cbit.vcell.modelopt.gui.ParameterMappingTableModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private ParameterMappingTableModel getparameterMappingTableModel() {
	if (ivjparameterMappingTableModel == null) {
		try {
			ivjparameterMappingTableModel = new cbit.vcell.modelopt.gui.ParameterMappingTableModel();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjparameterMappingTableModel;
}


/**
 * Return the ScrollPaneTable property value.
 * @return cbit.vcell.messaging.admin.sorttable.JSortTable
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private cbit.vcell.messaging.admin.sorttable.JSortTable getScrollPaneTable() {
	if (ivjScrollPaneTable == null) {
		try {
			ivjScrollPaneTable = new cbit.vcell.messaging.admin.sorttable.JSortTable();
			ivjScrollPaneTable.setName("ScrollPaneTable");
			getJScrollPane1().setColumnHeaderView(ivjScrollPaneTable.getTableHeader());
			getJScrollPane1().getViewport().setBackingStoreEnabled(true);
			ivjScrollPaneTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
			ivjScrollPaneTable.setBounds(0, 0, 200, 200);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjScrollPaneTable;
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
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	this.addPropertyChangeListener(ivjEventHandler);
	connPtoP1SetTarget();
}

/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("ModelParameterPanel");
		setLayout(new java.awt.BorderLayout());
		setSize(655, 226);
		add(getJScrollPane1(), "Center");
		initConnections();
		connEtoM3();
		connEtoM4();
		connEtoM2();
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
		javax.swing.JFrame frame = new javax.swing.JFrame();
		ParameterMappingPanel aParameterMappingPanel;
		aParameterMappingPanel = new ParameterMappingPanel();
		frame.setContentPane(aParameterMappingPanel);
		frame.setSize(aParameterMappingPanel.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JPanel");
		exception.printStackTrace(System.out);
	}
}


/**
 * Sets the parameterEstimationTask property (cbit.vcell.modelopt.ParameterEstimationTask) value.
 * @param parameterEstimationTask The new value for the property.
 * @see #getParameterEstimationTask
 */
public void setParameterEstimationTask(cbit.vcell.modelopt.ParameterEstimationTask parameterEstimationTask) {
	cbit.vcell.modelopt.ParameterEstimationTask oldValue = fieldParameterEstimationTask;
	fieldParameterEstimationTask = parameterEstimationTask;
	firePropertyChange("parameterEstimationTask", oldValue, parameterEstimationTask);
}


/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88G4E0171B4GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E13DBB8DD4D467152CFFC5C38CDAFFD009224106EDE956B4740433B5B926B52DCDD8233B2675ECB92DB9EB3326EBF2580D5930296DBE860DD8D0B112EA3B18105424C8E078718F818DB02082834E91B28AB148E3668923433C491B078CB0405E6F3B6FFB73985EB0460D4BB977FC6F3B5F4F3D5F3D773B5F7D6EFCF0B96F476CCEB364F0DC5AEECE79BB1346F1D9E7B86ED7BBBEBB4CF011AEEBA627797BG
	60A0F76776AEA8E320EDED2CB5B31B5BFFBD836A89D00F00BA99FC08BBF4BD6994FC8421938BEDC90BE7EC1D1CCE67B0233307561764E7C1399FA091F0245CA5927ACF649FD170498A1EC11A01633652BE6B73F3955CA528F78144GA40858BF894A3D1472740AF2255F75B9F15C0E67A443BEB20FE6CE8605EE9CEBD14ABC6E2D34D4B72175A665130C89077A9AG3278F46E760593D0D6D9EE771C37F5BBE4639D1B60F49A7BC43B60945D3231275FD1D1E161A53ECF10852919F73B9D2E9E8B6F921C1C7CA519
	77536399F49E18836A1CC4F087CF90DA09D05FG30CD45371FA178BA783E8EA0CBECFD677EDA2570FFED78C9EEEB4F33FB8F84D9DF04046BCA107C6B3394F4277F096AE2290F48BC855AF2BC564CFCGB30093A08260FF288D2C9CAE003226DBB609FDFD222B466D2EF55A25E632DE782E28009685F7C53009121D63D04F3F4BAF5461E7B240F119473F6AB814C95C89267378AB0FF149CFACB06C5111C9F25C30A1560332D86D714B84E95785251D6C99EF1363E9DF65343439D74A766C56211D7B3332BDA94ABC
	3BE4BF6DAA1968FABA3501E3701D26488271F3A8BECC03C77BBCCC71249E85EDBBE5E69B5776B3DE4C3BD6F059258BF56590754388C3D7BF72606D8F64E5A3353B58FE2439CF65E58B452F536011179B94CF6A5B216D5A9DEBA65A572323E43F2E057A9600D4006C81EBE689009920EB005958EB4345F7E9E33033645AFE0753AEC89C0EFD5477C7A8852FAC718DAE0F4C3BEC0215179CFC37D370F0CA1FD5BEA2036EF1633ECFED5F86B4AE8AAEC162E50768D2772D877BBC6DBBC87AC882EDEDF37489C0204F4D
	31F9FF6CBB84251377481F396D3CAC78FBE07BFA9F51499D5E8AB34284700DF259B4C6F58A6B0F8208D36550B0C266EB92A4F0979595E6D13C5D6FEEA3AB61D287081CBF273EE3B5FCEF9AE0B25E4091DC15571A5966E573040D3C2319274EE1A36C7352A05BE38FD05B69057E61G2B3C4CF6DE3B46EC27EE5FF7394297676B5ACEA4587196AF5ACC2AB7F08FAD24F3937E72862D1955034C6F9FBDF16DAE76F238D7FF1FB1688DE8DF25541146693B2231BBC4FBFAC0FD0FD2223C4F9B487A3AE0DDBE0068C1A6
	739F0C64218D57729E01D96DE5898CCE307BB500FC7E7C8C313DCCE54F6CD5E89C65704C44BEAF53BE89B0FFB2402EA1EBE6A1C09B00EF0849D4B85325436BDE4D5C132D7328D2FE90229F528A1BC00B711DFE97B24DD44AD0BC21AC9F9FA56719F0C7F049F502DBF05961432334A50E923A4AFE3FA92AE31E26F8B7788793ECC80F0ADFBBCA76B0AF4B12233BDF96DA85D90693595F1EC4E9F97A3B5DFE5742E87D53A8D917B8G7EC16D3FF9146CAD1B68F289D4296A90257D1F695E961CC29FB0ED927BDDB262
	370C11B86836B02878162DE3EFF344B7B87B091F4258E295CF6464169CFDE0AA4455F1BB06483E170E9F5148A896F0A4C6DAC74AA136F7877E42766E31213F65565CF09B744E2028158F992A061518E7583FF7112797B9FD5EFFC1FD0C0277A02F0B1D44D7D19F2D3ACD0CF352691899E3FFC6FD0C0B0428B72303B8255FF3068F89AFFA7DCC90D9DDA15A178429146D3F01F442239BF81F8690BD4C765F55B4524EFC1E3271CC225309EA023654E1A24B6493B99AF9A48C23AC772B6D99A7356D55BEEC0F70B1BA
	F506ECE2AF42177D829CB5044DA6D527CD06F7825A2C0AFE313D55C0746B5478752E91C2B3371C4535B1BE746D09D0EEG5881E006BE58EFED856B97BFB2315FDA40650C12FE42B956EF73887A6114916C47C17BFB004779CE1EE77D3AC6F11D5ED16C9FBD0AB241FED91F32FEEB14FE1BC77DF4D3603BEA0C5EA1CC2C5F2EB174D539E3F88694C2E964181D3FCE6D1946503778BB3C576899G1DD7D4B4D28342B8B9191EF524FF78980B07B30A086E57C0FDB3C04A98337D0D7BD8BCBC6FF69C177CCA691EE071
	FEC6982E2BB04CEF7B3DA9B5EF23EDBDF51150F4C21BAF0C69FB3DC1FBA657F8BC224DC14381C527A3695A3D0138A969FF007226C372488DE03EDE267F70F442AF09979434A20F0C6CF26E2B5EA97CBB89E5B0F2C1AB035587C6282D6949A0D2D97BAA0D8CE2DCEDFB1097DF612777460BF637AAF71D1527188ED30096460A7565D98AAE7FEF402E81508F44D33C4365109D3C53B1A4649FC75FB7D42275FDD150BF99001B8236BE055D494A4CCDF45F41E9F1136FF74A0C0DFCB3B1AF2313F775986929D0E95658
	1BD1A6EC9BA7B0DFF95C2A707FD2B9633FF08A33356F3ED72D701274588AAE031FEE503BFBC53C52E330CFC179C6CC714BF94B5C6E749DGA80BD7AB18ACB6C11F2C29F04723F427D6B23AF5D3195CE61F4B6250E77AG17B015496D0DF364DC11FB9D9E2C9FBDCF4675210BD56EA77FD12365B9F1CF04AE4F3D0FC69936CFC5DEF32679F936AF6FCE77AEE7BCFFF11E71EC07BE5153989FEB4F131C016A6F6BBDB21CE2B4EC66BD3795DF7F6F13764179FFDB234A84664E1A4E666F2E8D348F145542DAF61F2E1B
	4EE4F546244A8AF0896A9CCBCDC4D63C54D306759F1B186CD0CE3EF6A6273024846E482187F565C4E07BF4C5CEB37CF20A0F69CEDF83007152CB936E1276997E5855BBE3E21C0BF7AC9F75F191B3700E152E62E7D0FC34860FEB5DDF44569A8FED595FE07702730DE45F48D00F840883D8833085008BE73EAD6BA249E5CCFA6FEAF00122DC3C33F5DC002478339FDEA4F4A126E177210BC4EFB4266158F99D5FC44E53DC20D98EE081688588D7F9E83E941207FA2FAE8FA867EF1F523B334705ABF77C7009F246F1
	CD650163B684745D9ED047F16D934699836A59B3470FC39F75E4BDC925CD702B0247437790E7445B7B9CAE23C714E46A1660B0EC05EF0C92B8A58EFC2AC86B8B73E7E28C5086E56FCC66BB3ED742FCC758CCF6EEF616517C9254B7GEC1B496E8FAF5335F033A63FF3B1481945EC4B50CC4E79FC285781DC877082C483A482A44FE23ADDF229B224EEC9105FA65650A04D9498D5ABB2FF3D625EF415D699CAD7BBE7690D8B2763F29E38BB3964BF4064B96FDC285C15BE1FABC20E2B8A608575B058C2745086EDEE
	00F000D800F5GDB81B2A2189EDE3DD49BCA8FB56C02843AE01F93552170EBBBFFEF7A78F2423877826AF9913AFA2C3DB7FA87A60C8B54FF39AE3DE8BA4E92F1F77A770FBBE73AB77DB7059CE70F50537F1B17097EFD509685308AA089A085A06BC1EBE64903CC7FB58DA1752F06F4136B9F73D029B4B63F8473DF87C05C0B1F90DC9854239EE47BE1CA91B9DFD72B7588DAA7719B56F30A6EA6DF938A487C41F5D2743550884C89B1C079DEEC8C1E2FE8E9544FD734B56A67ABBA9A8FA29E6650E6BBD9FBF76364
	790CDB0D41739989CD41797CC113BE1F4FB469737913264979FC21A9981FE8AF4F503B41A6505DD65506BEBE4DECA88560A8157BCF7FF8005271F8848FBDAC6AF81967306DDF65F7B91DDCCC8BD8B247728BD34A77AAFD6AE53119449AF89E775413B9F46E202DB6C9F4BA49CD945A71CCFD23A8C331271C596CCCFD36181C29E6285B81FC3319CDBFD9C276723C597AB699270347786E9F69993DFAB646F7E9AA7E97943FCE0347384FDE46FE1F5A84ED658FB1997E0E5ED922219E2F62AE1C25F9E328A7BD2446
	FE4D41ED61D0333EAD7C29195C2BBDE23FE49382751D57FCC4ED73BB0E86D968A36A40BE4769F89982F0E1023D1450715896EC7C5966E0B6053AAE28546A7A24EAEB4BAA19CCD2D4197CDBA5138997496E874F1FE363AD32E8A9D5F91B5A92DCD633DA74E5955992DCD68BDBC24BAA26A5382C9EED89AD2BD5AD647E5F6D30BBA4A58306EBDCFCDEAB23D2D5F66535DAFC19AA3BA7687D6A98486824AAA759144964F4D3927BC0D2C8B34D92DCCE9996FDB96D378417533B1650F2FA4F92DCCE9FD9C24B49E85117
	530182121FF57203E23F4CF12BA229FFAAE071F79D542F83CC83080A44B89C7D4A6383E43E1BF01A02E9363A859B97B1070C5D7E899B1B8A7D33GD788E086F04EE1F22DB8CB7634468315A2FE73A9FACF8561ED1AC3FEE32F3D8AD5E55EF327593CC766421CGE6GA7C044DC66135EAAE271C5BC60564C9D6F03B64D8D66134E1690FE364D8D3C0B1AA8BEF5EE60DD7479E2F6974D78A67070CD36575653BB498628EFD7F19F14B2DF4F4DE3FCEEA6E6271C5BAFE93EEF15FD9DE77864315B547BCA837518F3D6
	49BF14D64DA3B2A82075E373187E3FE06DE5E8EF1E33CC37E150A78AE0ABC06ABC54974E312E120C491E8F719F9D13D3255A68FC9CF39DCA77FC9C23645CEB47733105564367337CC9CB81BBDBE2G37F9BE336D170DE44F28E726119F10ABAAEAC512EBB763C64173F2C1C106B2BEFB81BBAF5752B9AFC13D8B403B0065533C9F333CE57C37D6F09BFE2D1F3B5D52B6E208DC203CE5D82049CD95F427A34DF78A994D94E84FDFC8F055055ADCBED97B33E9F88740F6E9DCBBB9DB0EF35A7658F16D045F77F7EB5B
	FFB62E1D4868AF4AF8DC5B739F31356DFCFE95677C7E34206FD05C8BF1CD7314B2EF7D223435D11637F11E97BE573EE749DE6CB4248519A7C9993FF921DFC6A89F5BE9A61F2CA8A61FB9278365734138757F68F420FC6627EB5B5FB89DA81F9F0D136FDF4F844A6771DD580EBE60201170D4877C74C6B13F10D7CCF0BE28472A3821E266AB9223182F783404586CD6355EC06B64B7892CEFAC566EE7526F6B77993F99C7A3589D617F66B3FEDDC23E79FED93CA2F89CC3B4654FF1A50B08DCFEDF42F44AC19D4F03
	72D2367FE540058344812C835836087919EAFA4E34C298656AB15AC8D82F3CAB9234F6D6B2ECB6248731330C45A84FA3CA99715F0B14F7D3CB4E32B360923405ADE63E23342F1A1B187F6A91C438F5CA03783B42C525565A5FED951D7DFD42045CBC5A723F1CE56BCEDC2C3E212239749428E7AC31E666ADE1EB7DE5E5BE149A12F0E57188123155A63A85FB3D57AD899E72B8836500EB282AE2EBA807F95CCBD8EEEDDA95239B3704517D9E6D3B856A29G59510C6EFF52FCF51D58CFFEE320F3234D34955C5B3B
	286EFB9C3764AB0F43B3FE41A7047F7C68407B48B20ABF999DF89FF9CEBDB32A21ADB6DA4DC33793FB4CF8581AF98C208A208DC08608FC185DC5E6DEAD61C264A75AC4379982CB27662A096BBBDCF8EF6F451E5943FCF3425FFF1BCB1CD1977477325807514657AAE5E25222B4D2627AB6F610756D00366C2590A38318813AG3CCB597AB6DEAD4475555B9DA453465BFA692F05ECF1D607B060C7132091675E71B9619467B8FC6DA3A82D7CADC5BAE4AAD1E2B3B442F1FC397B96C430EDFC0F2A7B5E53DFD5B6E8
	3B79C672BB4D9D58EE8E9B6F44F0341617AC2253E19BD4FC0F097A6EA2327717B21BBFD3CC786E95A547106812A70EE4FE2B157A739A00A4981B370C1EC545F913701535CC9B3F3CB0C46E997EB09933645876EEA9F9CF207D192F11B69BEBBEEB3BFC63CAFDEBC3C77D0D4B97AEB0BB0899507A3C72BFCC51FDB7C120D76141374C6F7350AFF416321CFB42B2F636AEA88DBCDB670CBB9B5715861E2D04263F7D2D524033F57C7842324033150DC75B79688BF29EEEDF46EC7073EB9F86339FC01AE02F61F940EC
	E77959D03643456865B27FAE6438401CAF0E5B96F21C3E1F1ACE7DFEEECC201F1ACD7179B101FE6AF08573D3A721ADAA06458B35D57736DECB48F17A6B1D99F21C7EFABF263F836F1C305EE20A4FD89E385E44AA365E9CE873AEE76B0D7E04D3F9D929795ED0F07F6FDA2F5CD79ACACE36FD121CECFB101CECFB101CEC7B64B9576E7660B9D7C73B768D996E4D72CE32F713C19729G39AB2C19A5GF5AB58DEFD331D6CD57CD5A6702C28377710DC88F969AA2BEF4EBA022F75250E506FE3FF53C15E2BF40B32AC
	76117999BE250368CB965D940938B7BA080CA4C7CF2F0CDD957CEFBB685B9961A6C523AF4EAEA67925F13E586F340D567AABED8D269A730DD653154BE6F3ED4D159BB5662BB56D2D9C7373370ACFCCB27E5265AB8D9D17DB5A74E640710F95139C0EC9A03FE1B3FFBFA030F3BF77BB0FF0396FD79B186F8D34232E950A1FDF613FCB6DD7F5387886D96F9AE8CBD675F667BA59C841FC2CC50208D3128791CBDF06315F53CA353EE0A74C177D48647795A51F7A85D91FC7715B1893DFF3085CB19DAE2743A51870F5
	333A6F9E28706F73F91A6F239577FF4F1F7EFF20D1F84E3F26D31A6F272B6EBF6D1F5FD79A286F6A4E607B7CE2E768FDEE6924EF55E0FF368D3A157C67E71D4406441BB7BD026ACFBABA33D5DC0347A2FA9CA4B10AF67F8FDDF9D09E837B2C86308378GA28162GD683ECFA046D076D1D646E95ECBFE062087AB209F73037264BEE10752A3E963709425737EE64F01AB7C0746990C6ADCA1E0046E1A9C01F44CA089B23380CD844A9EFF83B024B71D7DD2165382DCB5FDF3E56B551DF3E5E256FAF7F2BCB6BAF91
	77FBE5DEC97D918B713BF83227B1975D4C3B789EFA16603D1A7FD873AE6A2A43AE77A2BE573B4F0F3FA4907AD0477F6BAA38C572DC712FF23BE56E433BF9FA2B784E63E35AD8B9F53AADE82C1C31927DD76ECA3F6FC47B69F312F5DF833C3C125949D3370B0259891CF82DFD3C538937095BB5AEBB3967548E4A027F6C151CE4FE8F6B23B4E25BA0FD9BF9CBB366FF81D0CB878872C89A83A693GGB4B7GGD0CB818294G94G88G88G4E0171B472C89A83A693GGB4B7GG8CGGGGGGGGGGG
	GGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGE093GGGG
**end of data**/
}
}