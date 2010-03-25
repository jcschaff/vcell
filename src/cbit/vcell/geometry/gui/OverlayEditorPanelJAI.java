package cbit.vcell.geometry.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import org.vcell.util.BeanUtils;
import org.vcell.util.ISize;
import org.vcell.util.NumberUtils;
import org.vcell.util.Range;
import org.vcell.util.UserCancelException;
import org.vcell.util.gui.ColorIcon;
import org.vcell.util.gui.DialogUtils;
import org.vcell.util.gui.ZEnforcer;

import loci.formats.AWTImageTools;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import cbit.image.VCPixelClass;
import cbit.vcell.VirtualMicroscopy.ImageDataset;
import cbit.vcell.VirtualMicroscopy.ROI;
import cbit.vcell.VirtualMicroscopy.UShortImage;
import cbit.vcell.VirtualMicroscopy.Image.ImageStatistics;
import cbit.vcell.geometry.gui.ROIAssistPanel;
//comments added Jan 2008, this is the panel that displayed at the top of the FRAPDataPanel which deals with serials of images.
/**
 */
public class OverlayEditorPanelJAI extends JPanel{

	public static java.awt.Color[] CONTRAST_COLORS = new java.awt.Color[] {
		
		new Color(0xFF000000),//background
		
		new Color(0xFFFF0000),
		new Color(0xFF00FF00),
		new Color(0xFF0000FF),
		new Color(0xFFFFFF00),
		new Color(0xFFFF00FF),
		new Color(0xFF00FFFF),
		
		new Color(0xFFFF8080),
		new Color(0xFF80FF80),
		new Color(0xFF8080FF),
		new Color(0xFFFFFF80),
		new Color(0xFFFF80FF),
		new Color(0xFF80FFFF),
		
		new Color(0xFFFF4040),
		new Color(0xFF40FF40),
		new Color(0xFF4040FF),
		new Color(0xFFFFFF40),
		new Color(0xFFFF40FF),
		new Color(0xFF40FFFF),

		new Color(0xFF0C352A),
		new Color(0xFFFDC2EB),
		new Color(0xFF630350),
		new Color(0xFFC9FA50),
		new Color(0xFF014305),
		new Color(0xFFC3E4C8),
		new Color(0xFF3D260C),
		new Color(0xFFD5F82B),
		new Color(0xFF4900C9),
		new Color(0xFFE0D3BE),
		new Color(0xFF0A3668),
		new Color(0xFF84FADE),
		new Color(0xFF1B15FB),
		new Color(0xFFCEFB00),
		new Color(0xFF67068E),
		new Color(0xFFF6E20A),
		new Color(0xFF3D2A4D),
		new Color(0xFF9EFB63),
		new Color(0xFF76162A),
		new Color(0xFF8AFA93),
		new Color(0xFF0536C5),
		new Color(0xFFE6D06F),
		new Color(0xFF9A0D10),
		new Color(0xFFF4D22E),
		new Color(0xFF113994),
		new Color(0xFFBCD4A9),
		new Color(0xFF3B21A9),
		new Color(0xFFDEB5D7),
		new Color(0xFF5E2F03),
		new Color(0xFF49FDF1),
		new Color(0xFF741173),
		new Color(0xFFE8BF90),
		new Color(0xFF1A5B02),
		new Color(0xFF64FBB0),
		new Color(0xFF960485),
		new Color(0xFFA2E484),
		new Color(0xFF443188),
		new Color(0xFF6BE7FB),
		new Color(0xFF185557),
		new Color(0xFF89E0C8),
		new Color(0xFF543F37),
		new Color(0xFFA9E845),
		new Color(0xFF4028EF),
		new Color(0xFF98CFEF),
		new Color(0xFFAC0C64),
		new Color(0xFF98F52E),
		new Color(0xFF6A14E6),
		new Color(0xFFBAE711),
		new Color(0xFF822651),
		new Color(0xFF8DFF0D),
		new Color(0xFF3734D1),
		new Color(0xFFF6BA58),
		new Color(0xFFB5132B),
		new Color(0xFFD7C847),
		new Color(0xFF415220),
		new Color(0xFFDAD112),
		new Color(0xFF841B91),
		new Color(0xFFBCD25C),
		new Color(0xFFA200D7),
		new Color(0xFFD2B1B4),
		new Color(0xFFCC1303),
		new Color(0xFFA8B6FD),
		new Color(0xFF5E3C60),
		new Color(0xFF2CFFB4),
		new Color(0xFF016292),
		new Color(0xFFB1B8C5),
		new Color(0xFFAB05B0),
		new Color(0xFFC0A7EF),
		new Color(0xFF8F14BC),
		new Color(0xFFEA93E8),
		new Color(0xFF952F32),
		new Color(0xFF59F955),
		new Color(0xFF6C2AC0),
		new Color(0xFFF9B510),
		new Color(0xFFF40715),
		new Color(0xFF49F29A),
		new Color(0xFF1B6F3C),
		new Color(0xFF48EAC5),
		new Color(0xFF555E01),
		new Color(0xFFBDC62F),
		new Color(0xFF2B6070),
		new Color(0xFFB1BB8D),
		new Color(0xFF5B3CA7),
		new Color(0xFF11F6FA),
		new Color(0xFF81441F),
		new Color(0xFF74DC79),
		new Color(0xFF2D50C1),
		new Color(0xFF7EC3DA),
		new Color(0xFF0463D2),
		new Color(0xFFE8AB37),
		new Color(0xFFCA0C8F),
		new Color(0xFF68EA3C),
		new Color(0xFF2057E5),
		new Color(0xFF8ED34C),
		new Color(0xFF8C1EEF),
		new Color(0xFF8CC688),
		new Color(0xFFB20DF0),
		new Color(0xFFA3CF1E),
		new Color(0xFF0A8156),
		new Color(0xFF1DECD7),
		new Color(0xFFE90977),
		new Color(0xFFDD93A0),
		new Color(0xFFAE2399),
		new Color(0xFF59F116),
		new Color(0xFF16891D),
		new Color(0xFF38D4FD),
		new Color(0xFFC91E6E),
		new Color(0xFF69CAAB),
		new Color(0xFF415C99),
		new Color(0xFFF09360),
		new Color(0xFFBC15C4),
		new Color(0xFF00FFA2),
		new Color(0xFFE61A40),
		new Color(0xFFFB808F),
		new Color(0xFFCA3021),
		new Color(0xFF38F044),
		new Color(0xFF3C50FC),
		new Color(0xFFFE8E33),
		new Color(0xFFE206CD),
		new Color(0xFFD4AD00),
		new Color(0xFF963A8E),
		new Color(0xFF38E471),
		new Color(0xFFFC01B8),
		new Color(0xFFC5A15A),
		new Color(0xFF2F6FA8),
		new Color(0xFFFE6FB0),
		new Color(0xFF726721),
		new Color(0xFF0FF672),
		new Color(0xFFB64235),
		new Color(0xFF51D183),
		new Color(0xFFA05804),
		new Color(0xFF45C7D6),
		new Color(0xFF9E485C),
		new Color(0xFFE29A13),
		new Color(0xFF407D44),
		new Color(0xFF8AA8B4),
		new Color(0xFF905741),
		new Color(0xFF15E997),
		new Color(0xFF815186),
		new Color(0xFF9DB345),
		new Color(0xFF1AA200),
		new Color(0xFFABB806),
		new Color(0xFF6948FD),
		new Color(0xFF889EE4),
		new Color(0xFF893DDE),
		new Color(0xFFAD8FD3),
		new Color(0xFF676D4E),
		new Color(0xFF7ACB14),
		new Color(0xFFB02EDC),
		new Color(0xFFE16DD6),
		new Color(0xFFD3394E),
		new Color(0xFF85AE74),
		new Color(0xFFED380F),
		new Color(0xFF55AEF6),
		new Color(0xFF5F8017),
		new Color(0xFF1BF41B),
		new Color(0xFF7554C0),
		new Color(0xFF00FA34),
		new Color(0xFFDF23A8),
		new Color(0xFFCD818A),
		new Color(0xFFFE179C),
		new Color(0xFFC171FE),
		new Color(0xFF15A041),
		new Color(0xFF0DCFE8),
		new Color(0xFFF011EB),
		new Color(0xFF44D433),
		new Color(0xFF0886F2),
		new Color(0xFF57C356),
		new Color(0xFF0598A4),
		new Color(0xFF46BA9D),
		new Color(0xFF527C6E),
		new Color(0xFFB39D1B),
		new Color(0xFF616E8B),
		new Color(0xFFE76E80),
		new Color(0xFF6163DC),
		new Color(0xFF65A3BE),
		new Color(0xFF06A47D),
		new Color(0xFF8EAD1D),
		new Color(0xFF3F8585),
		new Color(0xFF11CDB9),
		new Color(0xFFC13EAA),
		new Color(0xFFE754F4),
		new Color(0xFF2E80E0),
		new Color(0xFF728DF0),
		new Color(0xFFB84E74),
		new Color(0xFFFF6255),
		new Color(0xFF5874B2),
		new Color(0xFFF25A98),
		new Color(0xFFBB5E1A),
		new Color(0xFF9C869A),
		new Color(0xFF00B550),
		new Color(0xFFAD8371),
		new Color(0xFF8F7B03),
		new Color(0xFFB073BA),
		new Color(0xFFBA36FE),
		new Color(0xFF11B6FF),
		new Color(0xFFDE3B85),
		new Color(0xFF7790AC),
		new Color(0xFFF23769),
		new Color(0xFF16D351),
		new Color(0xFF06BF23),
		new Color(0xFFEC711B),
		new Color(0xFF38A330),
		new Color(0xFF8C76F2),
		new Color(0xFF259C8F),
		new Color(0xFF04E60C),
		new Color(0xFF3989BA),
		new Color(0xFF649A93),
		new Color(0xFF966A6C),
		new Color(0xFF0AC98F),
		new Color(0xFFA05AAE),
		new Color(0xFF6FA92E),
		new Color(0xFF5473F8),
		new Color(0xFF3ACA10),
		new Color(0xFF459A5A),
		new Color(0xFFC759E2),
		new Color(0xFFD934E9),
		new Color(0xFF21B5AC),
		new Color(0xFF78910B),
		new Color(0xFFBF8109),
		new Color(0xFFA77336),
		new Color(0xFF00BBCB),
		new Color(0xFFB86652),
		new Color(0xFFD551B7),
		new Color(0xFFFE4D1E),
		new Color(0xFF1CA9D9),
		new Color(0xFF5E9841),
		new Color(0xFFA161E8),
		new Color(0xFF886FA3),
		new Color(0xFFD36167),
		new Color(0xFFE45545),
		new Color(0xFFEC41CB),
		new Color(0xFFF45B01),
		new Color(0xFFCF6B2F),
		new Color(0xFF47B400),
		new Color(0xFFC15E96),
		new Color(0xFF23B564)
};

	private static final long serialVersionUID = 1L;
	private static final double SHORT_TO_BYTE_FACTOR = 256;
	
	public static final String INITIAL_BLEACH_AREA_TEXT = "Initial Bleach Area";
	public static final String WHOLE_CELL_AREA_TEXT = "Whole Cell Area";
	public static final String ROI_ASSIST_TEXT = "ROI Assist";
	//properties
	public static final String FRAP_DATA_CROP_PROPERTY = "FRAP_DATA_CROP_PROPERTY";
	public static final String FRAP_DATA_TIMEPLOTROI_PROPERTY = "FRAP_DATA_TIMEPLOTROI_PROPERTY";
	public static final String FRAP_DATA_CURRENTROI_PROPERTY = "FRAP_DATA_CURRENTROI_PROPERTY";
	public static final String FRAP_DATA_UNDOROI_PROPERTY = "FRAP_DATA_UNDOROI_PROPERTY";
	public static final String FRAP_DATA_DELETEROI_PROPERTY = "FRAP_DATA_DELETEROI_PROPERTY";
	public static final String FRAP_DATA_ADDNEWROI_PROPERTY = "FRAP_DATA_ADDNEWROI_PROPERTY";
	public static final String FRAP_DATA_SHOWROIASSIST_PROPERTY = "FRAP_DATA_SHOWROIASSIST_PROPERTY";
	public static final String FRAP_DATA_CLEARROI_PROPERTY = "FRAP_DATA_CLEARROI_PROPERTY";
	public static final String FRAP_DATA_CHECKROI_PROPERTY = "FRAP_DATA_CHECKROI_PROPERTY";
	public static final String FRAP_DATA_BLEND_PROPERTY = "FRAP_DATA_BLEND_PROPERTY";
	//used for new frap
	public static final int DISPLAY_WITH_ROIS = 0;
	public static final int DEFINE_CROP = 1;
	public static final int DEFINE_CELLROI = 2;
	public static final int DEFINE_BLEACHEDROI = 3;
	public static final int DEFINE_BACKGROUNDROI = 4;
	private ROISourceData roiSourceData = null;
	//scale factors
	public static final double DEFAULT_SCALE_FACTOR = 1.0;
	public static final double DEFAULT_OFFSET_FACTOR = 0.0;
	private double originalScaleFactor = DEFAULT_SCALE_FACTOR;
	private double originalOffsetFactor = DEFAULT_OFFSET_FACTOR;
	private ISize originalISize;
	//panel components
	private JComboBox roiComboBox;
	private JButton contrastButtonMinus;
	private JButton contrastButtonPlus;
	private JToggleButton cropButton;
	private JToggleButton fillButton;
	private JToggleButton eraseButton;
	private JToggleButton paintButton;
	private JButton importROIMaskButton;
	private JButton clearROIbutton;
	private JButton roiTimePlotButton;
	private JButton autoCropButton;
	private JButton roiAssistButton;
	private OverlayImageDisplayJAI imagePane = null;
	private JSlider timeSlider = null;
	private ImageDataset imageDataset = null;
	private ROI roi = null;
	private StringBuffer sb = new StringBuffer();
	private JScrollPane jScrollPane2 = null;
	private JPanel leftJPanel = null;
	private JButton zoomInButton = null;
	private JButton zoomOutButton = null;
	private Color highlightColor = Color.yellow.darker();
	private JSlider zSlider = null;
	private ButtonGroup roiDrawButtonGroup = new ButtonGroup();
	private Point lastHighlightPoint = null;
	private JLabel textLabel = null;
	private JPanel topJPanel = null;
	private JPanel editROIPanel = null;
	private JLabel viewTLabel;
	private JLabel viewZLabel;
	private JLabel editRoiLabel;
	private JButton checkRegionsButton;
	private JComboBox blendComboBox;
	private JLabel lblBlend;
	
	private JButton addROIButton;
	private JButton delROIButton;
	private boolean bAllowAddROI = true;
	//variables for undo function	
	UndoableEditSupport undoableEditSupport;
	ROI undoableROI;
		
	private BufferedImage[] allROICompositeImageArr;
	
	private Hashtable<String, Cursor> cursorsForROIsHash = null;
	
	public interface CustomROIImport {
		public boolean importROI(File importROIFile) throws Exception;
	};
	private CustomROIImport customROIImport;
	private JFileChooser openJFileChooser = new JFileChooser();
	private Range minmaxPixelValues = null;
	//variable used to avoid unnecessary firing of the combobox action event
	private String roiName;
	
	//ROI comboBox action
	ActionListener ROI_COMBOBOX_ACTIONLISTENER =
		new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				ComboboxROIName selectedItem = ((ComboboxROIName)roiComboBox.getSelectedItem());
				if(selectedItem != null){
					delROIButton.setEnabled(selectedItem.isDeleteable());
					checkRegionsButton.setEnabled(true);
					String selectedName = selectedItem.getROIName();
					if (!selectedName.equals(roiName)) {					
						firePropertyChange(FRAP_DATA_CURRENTROI_PROPERTY, null,selectedItem.getROIName());
						roiName = selectedName;
					}
				}else{
					delROIButton.setEnabled(false);
					checkRegionsButton.setEnabled(false);
					roiName = null;
					firePropertyChange(FRAP_DATA_CURRENTROI_PROPERTY, null,null);
				}
			}
		};
	public static class ComboboxROIName {
		private String roiName;
		private boolean bNameEdit;
//		private boolean bPaintable;
//		private boolean bErasable;
		private boolean bDeleteable;
		private Color highlightColor;
		public ComboboxROIName(String roiName,boolean bNameEdit,boolean bDeleteable,/*boolean bPaintable,boolean bErasable,*/Color highlightColor){
			this.roiName = roiName;
			this.bNameEdit = bNameEdit;
//			this.bPaintable = bPaintable;
//			this.bErasable = bErasable;
			this.bDeleteable = bDeleteable;
			this.highlightColor = highlightColor;
		}
		public String getROIName(){
			return roiName;
		}
		public boolean isNameEditable(){
			return bNameEdit;
		}
//		public boolean isPaintable(){
//			return bPaintable;
//		}
//		public boolean isErasable(){
//			return bErasable;
//		}
		public Color getHighlightColor(){
			return highlightColor;
		}
		public boolean isDeleteable(){
			return bDeleteable;
		}
		public String toString(){
			return getROIName();
		}
	}
	
	public static final UndoableEdit CLEAR_UNDOABLE_EDIT =
		new AbstractUndoableEdit(){
			public boolean canUndo() {
				return false;
			}
			public String getUndoPresentationName() {
				return null;
			}
			public void undo() throws CannotUndoException {
				super.undo();
			}
		};

		private ActionListener addROIActionListener  = new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				ComboboxROIName[] comboboxROINameArr = new ComboboxROIName[0];
				if(roiComboBox.getItemCount() > 0){
					comboboxROINameArr = new ComboboxROIName[roiComboBox.getItemCount()];
					for (int i = 0; i < comboboxROINameArr.length; i++) {
						comboboxROINameArr[i] =(ComboboxROIName)roiComboBox.getModel().getElementAt(i);
					}
				}
				firePropertyChange(FRAP_DATA_ADDNEWROI_PROPERTY, comboboxROINameArr, null);
			}
		};
	/**
	 * This is the default constructor
	 */
	public OverlayEditorPanelJAI() {
		super();
		initialize();
	}

	public void setUndoableEditSupport(UndoableEditSupport undoableEditSupport){
		this.undoableEditSupport = undoableEditSupport;
	}
	
	public static boolean isAutoCroppable(Rectangle cropRectangle,ImageDataset checkThisImageDataset){
		if(cropRectangle == null || checkThisImageDataset == null){
			throw new IllegalArgumentException("Crop Rectangle and Imagedataset cannot be null.");
		}
			if(cropRectangle != null &&
				cropRectangle.x == 0 && cropRectangle.y == 0 &&
				cropRectangle.width == checkThisImageDataset.getISize().getX() &&
				cropRectangle.height == checkThisImageDataset.getISize().getY()){
				return false;
			}
		return true;
	}
	private void waitCursor(boolean bOn){
		Container topLevelContainer = (JDesktopPane)BeanUtils.findTypeParentOfComponent(this, JDesktopPane.class);
		if(topLevelContainer == null){
			topLevelContainer = (Frame)BeanUtils.findTypeParentOfComponent(this, Frame.class);
		}
		BeanUtils.setCursorThroughout(topLevelContainer,
				(bOn?Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR):Cursor.getDefaultCursor()));
		if(!bOn){
			updateROICursor();
		}
	}
	public void updateROICursor(){
		getImagePane().setCursor(getROICursor());
	}
	private Cursor getROICursor(){
		if(roi == null || cursorsForROIsHash == null || cursorsForROIsHash.get(roi.getROIName()) == null){
			return Cursor.getDefaultCursor();
		}
		return cursorsForROIsHash.get(roi.getROIName());
//		if(roi.getROIName().equals(FRAPData.VFRAP_ROI_ENUM.ROI_CELL.name())){
//			return FRAPStudyPanel.ROI_CURSORS[FRAPStudyPanel.CURSOR_CELLROI];
//		}else if(roi.getROIName().equals(FRAPData.VFRAP_ROI_ENUM.ROI_BLEACHED.name())){
//			return FRAPStudyPanel.ROI_CURSORS[FRAPStudyPanel.CURSOR_BLEACHROI];
//		}else if(roi.getROIName().equals(FRAPData.VFRAP_ROI_ENUM.ROI_BACKGROUND.name())){
//			return FRAPStudyPanel.ROI_CURSORS[FRAPStudyPanel.CURSOR_BACKGROUNDROI];
//		}
//		throw new RuntimeException("Unknown ROI type "+roi.getROIName()+" while getting cursor");
	}
	
	public void setCursorsForROIs(Hashtable<String, Cursor> cursorsForROIsHash){
		this.cursorsForROIsHash = cursorsForROIsHash;
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints2.anchor = GridBagConstraints.NORTH;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.gridx = 2;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridwidth = 2;
		gridBagConstraints12.ipadx = 570;
		gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints12.fill = GridBagConstraints.BOTH;
		gridBagConstraints12.weighty = 1.0;
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.gridy = 1;
		gridBagConstraints12.weightx = 1;
		this.setSize(734, 534);
		final GridBagLayout gridBagLayout_1 = new GridBagLayout();
		gridBagLayout_1.rowHeights = new int[] {0,7,0};  
		this.setLayout(gridBagLayout_1);

		editROIPanel = new JPanel();
		final GridBagLayout gridBagLayout_2 = new GridBagLayout();
		gridBagLayout_2.rowHeights = new int[] {0,0,7};
		gridBagLayout_2.columnWidths = new int[] {0,7};
		editROIPanel.setLayout(gridBagLayout_2);
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.anchor = GridBagConstraints.WEST;
		gridBagConstraints_6.insets = new Insets(8, 2, 0, 0);
		gridBagConstraints_6.weightx = 0;
		gridBagConstraints_6.gridy = 0;
		gridBagConstraints_6.gridx = 1;
		add(editROIPanel, gridBagConstraints_6);

		final JLabel infoLabel = new JLabel();
		infoLabel.setText("Data Info:");
		final GridBagConstraints gridBagConstraints_12 = new GridBagConstraints();
		gridBagConstraints_12.insets = new Insets(0, 0, 0, 4);
		gridBagConstraints_12.anchor = GridBagConstraints.EAST;
		gridBagConstraints_12.gridy = 0;
		gridBagConstraints_12.gridx = 0;
		editROIPanel.add(infoLabel, gridBagConstraints_12);

		textLabel = new JLabel();
		textLabel.setPreferredSize(new Dimension(500, 20));
		textLabel.setMinimumSize(new Dimension(500, 20));
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_2.weightx = 1;
		gridBagConstraints_2.insets = new Insets(0, 2, 0, 0);
		gridBagConstraints_2.anchor = GridBagConstraints.WEST;
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.gridx = 1;
		editROIPanel.add(textLabel, gridBagConstraints_2);
		textLabel.setText("No FRAP DataSet loaded.");

		topJPanel = new JPanel();
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 1, 0, 0);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridx = 1;
		editROIPanel.add(topJPanel, gridBagConstraints);
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0,7,7,0,7};
		topJPanel.setLayout(gridBagLayout);

		autoCropButton = new JButton(new ImageIcon(getClass().getResource("/images/autoCrop.gif")));
		autoCropButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					if(imageDataset != null){
						waitCursor(true);
						Rectangle cropRect = imageDataset.getNonzeroBoundingRectangle();
						if(cropRect == null || !isAutoCroppable(cropRect, imageDataset)){
							DialogUtils.showWarningDialog(OverlayEditorPanelJAI.this, "No zero valued outer border found.  Use manual crop tool instead.");
							return;
						}
						imagePane.setCrop(
							new Point(
								(int)(cropRect.x*imagePane.getZoom()),
								(int)(cropRect.y*imagePane.getZoom())),
							new Point(
								(int)((cropRect.x+cropRect.width)*imagePane.getZoom()),
								(int)((cropRect.y+cropRect.height)*imagePane.getZoom())));
						waitCursor(false);
						if(!cropConfirm(cropRect)){
							imagePane.setCrop(null, null);
							return;
						}
						waitCursor(true);
						imagePane.setCrop(null, null);
						firePropertyChange(FRAP_DATA_CROP_PROPERTY, null,cropRect);						
					}else{
						DialogUtils.showErrorDialog(OverlayEditorPanelJAI.this, "No imageDataset to perform auto-crop on.");
					}
//					if(isAutoCroppable()){
//						Rectangle cropRect = null;
//						if(imageDataset != null){
//							cropRect = imageDataset.getNonzeroBoundingRectangle();
//						}
//						imagePane.setCrop(
//								new Point(
//									(int)(cropRect.x*imagePane.getZoom()),
//									(int)(cropRect.y*imagePane.getZoom())),
//								new Point(
//									(int)((cropRect.x+cropRect.width)*imagePane.getZoom()),
//									(int)((cropRect.y+cropRect.height)*imagePane.getZoom())));
//						waitCursor(false);
//						if(!cropConfirm(cropRect)){
//							imagePane.setCrop(null, null);
//							return;
//						}
//						waitCursor(true);
//						imagePane.setCrop(null, null);
//						firePropertyChange(FRAP_DATA_CROP_PROPERTY, null,cropRect);						
//					}else{
//						DialogUtils.showInfoDialog(OverlayEditorPanelJAI.this, "AutoCrop: No zero values around outer edges.");
//					}
				} catch (Exception e1) {
					e1.printStackTrace();
					DialogUtils.showErrorDialog(OverlayEditorPanelJAI.this, "Error AutoCrop:\n"+e1.getMessage());
				}finally{
					waitCursor(false);				
				}
			}
		});
//		autoCropButton.setText("Auto Crop DataSet");
//		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
//		gridBagConstraints_1.gridx = 0;
//		gridBagConstraints_1.gridy = 0;
//		gridBagConstraints_1.insets = new Insets(2, 2, 2, 2);
//		topJPanel.add(autoCropButton, gridBagConstraints_1);

		importROIMaskButton = new JButton(new ImageIcon(getClass().getResource("/images/importROI.gif")));
		importROIMaskButton.setVisible(false);
		importROIMaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(customROIImport == null){
					DialogUtils.showErrorDialog(OverlayEditorPanelJAI.this, "No import method is available.");
					return;
				}
				File inputFile = null;
				IFormatReader iFormatReader = null;
				try {
					int option = openJFileChooser.showOpenDialog(OverlayEditorPanelJAI.this);
					if (option == JFileChooser.APPROVE_OPTION){
						inputFile = openJFileChooser.getSelectedFile();
					}else{
						throw UserCancelException.CANCEL_GENERIC;
					}
					if(!customROIImport.importROI(inputFile)){
						ImageReader imageReader = new ImageReader();
						iFormatReader = imageReader.getReader(inputFile.getAbsolutePath());
						iFormatReader.setId(inputFile.getAbsolutePath());
						if(iFormatReader.getSizeX() * iFormatReader.getSizeY() != 
							getImagePane().getHighlightImage().getWidth()*getImagePane().getHighlightImage().getHeight()){
							throw new Exception(
								"Imported ROI mask size ("+
								iFormatReader.getSizeX()+","+iFormatReader.getSizeY()+")"+
								" does not match current Frap DataSet size ("+
								getImagePane().getHighlightImage().getWidth()+","+
								getImagePane().getHighlightImage().getHeight()+")");
						}
						BufferedImage roiMaskImage = iFormatReader.openImage(0);
						int maskColor = highlightColor.getRGB();
						for (int y = 0; y < iFormatReader.getSizeY(); y++) {
							for (int x = 0; x < iFormatReader.getSizeX(); x++) {
								if((roiMaskImage.getRGB(x, y)&0x00FFFFFF) != 0){
									getImagePane().getHighlightImage().setRGB(x,y,maskColor);
								}
							}
						}
						getImagePane().refreshImage();
					}
				} catch (UserCancelException uce) {
					//Do Nothing
				} catch (Exception e1) {
					e1.printStackTrace();
					DialogUtils.showErrorDialog(OverlayEditorPanelJAI.this, "Error importing ROI"+e1.getMessage());
				}finally{
					if(iFormatReader != null){try{iFormatReader.close();}catch(Exception e2){e2.printStackTrace();}}
				}
			}
		});
		
//		importROIMaskButton.setText("Import ROI Mask...");
//		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
//		gridBagConstraints_5.insets = new Insets(2, 2, 2, 2);
//		gridBagConstraints_5.gridy = 0;
//		gridBagConstraints_5.gridx = 1;
//		topJPanel.add(importROIMaskButton, gridBagConstraints_5);

		clearROIbutton = new JButton(new ImageIcon(getClass().getResource("/images/clearROI.gif")));
		clearROIbutton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(roiComboBox.getItemCount() == 0){
					giveROIRequiredWarning("clear ROI");
					return;
				}
				firePropertyChange(FRAP_DATA_CLEARROI_PROPERTY, ((ComboboxROIName)roiComboBox.getSelectedItem()), null);
				clearROI();
			}
		});
//		clearROIbutton.setText("Clear ROI");
//		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
//		gridBagConstraints_4.insets = new Insets(2, 2, 2, 2);
//		gridBagConstraints_4.gridy = 0;
//		gridBagConstraints_4.gridx = 2;
//		topJPanel.add(clearROIbutton, gridBagConstraints_4);

		roiTimePlotButton = new JButton(new ImageIcon(getClass().getResource("/images/plotROI.gif")));
		roiTimePlotButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(getImagePane() == null || getImagePane().getHighlightImage() == null){
					return;
				}
				boolean bHasROI = false;
				for (int y = 0; y < getImagePane().getHighlightImage().getHeight(); y++) {
					for (int x = 0; x < getImagePane().getHighlightImage().getWidth(); x++) {
						if((getImagePane().getHighlightImage().getRGB(x, y)&0x00FFFFFF) != 0){
							bHasROI = true;
							break;
						}
					}
					if(bHasROI){
						break;
					}
				}
				if(bHasROI){
					firePropertyChange(FRAP_DATA_TIMEPLOTROI_PROPERTY, null,new Boolean(true));
				}else{
					DialogUtils.showInfoDialog(OverlayEditorPanelJAI.this, 
						"ROI for "+roi.getROIName()+" is empty.\n"+
						"Paint, Fill or Import ROI using ROI tools.");
				}
			}
		});
//		roiTimePlotButton.setText("Time Plot ROI...");
//		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
//		gridBagConstraints_3.anchor = GridBagConstraints.WEST;
//		gridBagConstraints_3.gridx = 3;
//		gridBagConstraints_3.gridy = 0;
//		gridBagConstraints_3.insets = new Insets(2, 2, 2, 2);
//		topJPanel.add(roiTimePlotButton, gridBagConstraints_3);
//		BeanUtils.enableComponents(topJPanel, false);

		roiAssistButton = new JButton(new ImageIcon(getClass().getResource("/images/assistantROI.gif")));
		roiAssistButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(roiComboBox.getItemCount() == 0){
					giveROIRequiredWarning("ROI Assistant");
					return;
				}
				ComboboxROIName comboboxroiname = (ComboboxROIName)roiComboBox.getSelectedItem();
				firePropertyChange(FRAP_DATA_SHOWROIASSIST_PROPERTY, null,comboboxroiname);
			}
		});
//		roiAssistButton.setText(ROI_ASSIST_TEXT);
//		final GridBagConstraints gridBagConstraints_16 = new GridBagConstraints();
//		gridBagConstraints_16.insets = new Insets(2, 2, 2, 0);
//		gridBagConstraints_16.gridy = 0;
//		gridBagConstraints_16.gridx = 4;
//		topJPanel.add(roiAssistButton, gridBagConstraints_16);

		viewZLabel = new JLabel();
		viewZLabel.setText("View Z:");
		final GridBagConstraints gridBagConstraints_17 = new GridBagConstraints();
		gridBagConstraints_17.insets = new Insets(0, 0, 0, 4);
		gridBagConstraints_17.anchor = GridBagConstraints.EAST;
		gridBagConstraints_17.gridy = 2;
		gridBagConstraints_17.gridx = 0;
		editROIPanel.add(viewZLabel, gridBagConstraints_17);

		final JPanel panel_1 = new JPanel();
		final GridBagLayout gridBagLayout_4 = new GridBagLayout();
		gridBagLayout_4.columnWidths = new int[] {7,0};
		panel_1.setLayout(gridBagLayout_4);
		final GridBagConstraints gridBagConstraints_18 = new GridBagConstraints();
		gridBagConstraints_18.insets = new Insets(0, 2, 0, 0);
		gridBagConstraints_18.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_18.weightx = 0;
		gridBagConstraints_18.gridy = 2;
		gridBagConstraints_18.gridx = 1;
		editROIPanel.add(panel_1, gridBagConstraints_18);
		final GridBagConstraints gridBagConstraints_19 = new GridBagConstraints();
		gridBagConstraints_19.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_19.anchor = GridBagConstraints.WEST;
		gridBagConstraints_19.weightx = 1;
		gridBagConstraints_19.gridy = 0;
		gridBagConstraints_19.gridx = 0;
		panel_1.add(getZSlider(), gridBagConstraints_19);

		viewTLabel = new JLabel();
		viewTLabel.setText("View Time:");
		final GridBagConstraints gridBagConstraints_13 = new GridBagConstraints();
		gridBagConstraints_13.insets = new Insets(0, 0, 0, 4);
		gridBagConstraints_13.anchor = GridBagConstraints.EAST;
		gridBagConstraints_13.gridy = 3;
		gridBagConstraints_13.gridx = 0;
		editROIPanel.add(viewTLabel, gridBagConstraints_13);

		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_15 = new GridBagConstraints();
		gridBagConstraints_15.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_15.weightx = 1;
		gridBagConstraints_15.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints_15.anchor = GridBagConstraints.WEST;
		gridBagConstraints_15.gridy = 0;
		gridBagConstraints_15.gridx = 0;
		panel.add(getTimeSlider(), gridBagConstraints_15);
		
		final GridBagConstraints gridBagConstraints_14 = new GridBagConstraints();
		gridBagConstraints_14.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_14.insets = new Insets(0, 2, 0, 0);
		gridBagConstraints_14.anchor = GridBagConstraints.WEST;
		gridBagConstraints_14.gridy = 3;
		gridBagConstraints_14.gridx = 1;
		editROIPanel.add(panel, gridBagConstraints_14);

		editRoiLabel = new JLabel();
		editRoiLabel.setText("Active ROI:");
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.insets = new Insets(0, 0, 0, 4);
		gridBagConstraints_7.anchor = GridBagConstraints.EAST;
		gridBagConstraints_7.gridy = 4;
		gridBagConstraints_7.gridx = 0;
		editROIPanel.add(editRoiLabel, gridBagConstraints_7);

		final JPanel editROIButtonPanel = new JPanel();
		final GridBagLayout gridBagLayout_3 = new GridBagLayout();
		gridBagLayout_3.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		gridBagLayout_3.columnWidths = new int[] {0,7,7, 0, 0, 0};
		editROIButtonPanel.setLayout(gridBagLayout_3);
		final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
		gridBagConstraints_8.weightx = 0;
		gridBagConstraints_8.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_8.insets = new Insets(0, 2, 0, 0);
		gridBagConstraints_8.anchor = GridBagConstraints.WEST;
		gridBagConstraints_8.gridy = 4;
		gridBagConstraints_8.gridx = 1;
		editROIPanel.add(editROIButtonPanel, gridBagConstraints_8);
		this.add(getLeftJPanel(), gridBagConstraints2);
		this.add(getJScrollPane2(), gridBagConstraints12);

		roiComboBox = new JComboBox();
		roiComboBox.setRenderer(new ListCellRenderer() {
			private DefaultListCellRenderer listCellRender = new DefaultListCellRenderer();
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				ComboboxROIName comboboxROIName = (ComboboxROIName)value;
				if(comboboxROIName == null){//return blank
					return listCellRender.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
				}
				if(comboboxROIName.getHighlightColor() == null){//return text only
					return listCellRender.getListCellRendererComponent(list, comboboxROIName.getROIName(), index, isSelected, cellHasFocus);
				}
				//return text with small color box
				Icon icon = new ColorIcon(20,20,comboboxROIName.getHighlightColor());
				JLabel jlable = (JLabel)listCellRender.getListCellRendererComponent(list, icon, index, isSelected, cellHasFocus);
				jlable.setText(comboboxROIName.getROIName());
				return jlable;
			}
		});
		roiComboBox.addActionListener(ROI_COMBOBOX_ACTIONLISTENER);
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_1.insets = new Insets(4, 4, 4, 5);
		gridBagConstraints_1.weightx = 1;
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.gridx = 0;
		editROIButtonPanel.add(roiComboBox, gridBagConstraints_1);

		addROIButton = new JButton();
		addROIButton.addActionListener(addROIActionListener);
		addROIButton.setText("Add ROI...");
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.insets = new Insets(4, 4, 4, 5);
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.gridx = 1;
		editROIButtonPanel.add(addROIButton, gridBagConstraints_3);

		delROIButton = new JButton();
		delROIButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				firePropertyChange(FRAP_DATA_DELETEROI_PROPERTY, ((ComboboxROIName)roiComboBox.getSelectedItem()), null);
//				roiComboBox.removeItemAt(roiComboBox.getSelectedIndex());
//				if(roiComboBox.getItemCount() == 0){
//					roiComboBox.setEnabled(false);
//				}
			}
		});
		delROIButton.setText("Delete ROI...");
		delROIButton.setEnabled(false);
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.insets = new Insets(4, 4, 4, 5);
		gridBagConstraints_4.gridy = 0;
		gridBagConstraints_4.gridx = 2;
		editROIButtonPanel.add(delROIButton, gridBagConstraints_4);
		
		checkRegionsButton = new JButton("Check...");
		checkRegionsButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				firePropertyChange(FRAP_DATA_CHECKROI_PROPERTY, ((ComboboxROIName)roiComboBox.getSelectedItem()), null);
			}
		});
		checkRegionsButton.setEnabled(false);
		GridBagConstraints gbc_checkRegionsButton = new GridBagConstraints();
		gbc_checkRegionsButton.insets = new Insets(4, 4, 4, 5);
		gbc_checkRegionsButton.gridx = 3;
		gbc_checkRegionsButton.gridy = 0;
		editROIButtonPanel.add(checkRegionsButton, gbc_checkRegionsButton);
		
		lblBlend = new JLabel("blend");
		GridBagConstraints gbc_lblBlend = new GridBagConstraints();
		gbc_lblBlend.insets = new Insets(4, 8, 4, 2);
		gbc_lblBlend.anchor = GridBagConstraints.EAST;
		gbc_lblBlend.gridx = 4;
		gbc_lblBlend.gridy = 0;
		editROIButtonPanel.add(lblBlend, gbc_lblBlend);
		
		blendComboBox = new JComboBox();
		for (int i = 0; i <= 100; i+= 5) {
			blendComboBox.insertItemAt(new Integer(i), i/5);
		}
		blendComboBox.setSelectedItem(new Integer(50));
		blendComboBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				firePropertyChange(FRAP_DATA_BLEND_PROPERTY,null,  (Integer)blendComboBox.getSelectedItem());
			}
		});
		GridBagConstraints gbc_blendComboBox = new GridBagConstraints();
		gbc_blendComboBox.insets = new Insets(4, 4, 4, 4);
		gbc_blendComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_blendComboBox.gridx = 5;
		gbc_blendComboBox.gridy = 0;
		editROIButtonPanel.add(blendComboBox, gbc_blendComboBox);
		
		roiDrawButtonGroup.add(paintButton);
		roiDrawButtonGroup.add(eraseButton);
		roiDrawButtonGroup.add(fillButton);
		roiDrawButtonGroup.add(cropButton);
		
		BeanUtils.enableComponents(getLeftJPanel(), false);
		BeanUtils.enableComponents(editROIPanel, false);
	}
	
	public ROI showAssistDialog(ROI originalROI,BitSet maskBitSet,final boolean bInvertThreshold/*,final boolean bInvertMask,final boolean bNeedsValidation*/)
	{
		JDialog roiDialog = new JDialog((JFrame)BeanUtils.findTypeParentOfComponent(OverlayEditorPanelJAI.this, JFrame.class));
		roiDialog.setTitle("Create Region of Interest (ROI) using intensity thresholding");
		roiDialog.setModal(true);
		ROIAssistPanel roiAssistPanel = new ROIAssistPanel();
//		ROI originalROI = null;
//		try{
//			if(roi != null){
//				originalROI = new ROI(roi);
//			}
//		}catch(Exception ex){
//			ex.printStackTrace();
//			//can't happen
//		}
		roiAssistPanel.init(roiDialog, originalROI, getRoiSouceData(),OverlayEditorPanelJAI.this,maskBitSet,bInvertThreshold/*,bInvertMask,bNeedsValidation*/);
		roiDialog.setContentPane(roiAssistPanel);
		roiDialog.pack();
//		roiDialog.setSize(400,200);
		ZEnforcer.showModalDialogOnTop(roiDialog, OverlayEditorPanelJAI.this);
		
		return roiAssistPanel.getFinalResultROI();
	}
	
	public void adjustComponentsForVFRAP(int choice)
	{
		if(choice == DISPLAY_WITH_ROIS)
		{
			setAllComponentsVisible();
			roiDrawButtonGroup.remove(paintButton);
			roiDrawButtonGroup.remove(eraseButton);
			roiDrawButtonGroup.remove(fillButton);
			roiDrawButtonGroup.remove(cropButton);
			
			cropButton.setVisible(false);
			autoCropButton.setVisible(false);
			paintButton.setVisible(false);
			paintButton.setSelected(false);
			eraseButton.setVisible(false);
			fillButton.setVisible(false);
			importROIMaskButton.setVisible(false);
			clearROIbutton.setVisible(false);
			roiTimePlotButton.setVisible(false);
			roiAssistButton.setVisible(false);

			editRoiLabel.setEnabled(true);
			addROIButton.setVisible(false);
			delROIButton.setVisible(false);
			checkRegionsButton.setVisible(false);
			lblBlend.setVisible(false);
			blendComboBox.setVisible(false);
			
		}
		else if(choice == DEFINE_CROP)
		{
			setAllComponentsVisible();
						
			zoomInButton.setEnabled(false);
			zoomOutButton.setEnabled(false);
			contrastButtonPlus.setEnabled(false);
			contrastButtonMinus.setEnabled(false);
			cropButton.setEnabled(true);
			cropButton.setSelected(true);//paint button will not be selected
			autoCropButton.setEnabled(true);
			paintButton.setSelected(false);
			paintButton.setEnabled(false);
			paintButton.setFocusPainted(false);
			paintButton.setBorderPainted(false);
			eraseButton.setEnabled(false);
			fillButton.setEnabled(false);
			importROIMaskButton.setEnabled(false);
			clearROIbutton.setEnabled(false);
			roiTimePlotButton.setEnabled(false);
			roiAssistButton.setEnabled(false);
			//other components
			roiComboBox.setVisible(false);
			roiComboBox.setEnabled(false);
			editRoiLabel.setVisible(false);
//			zSlider.setVisible(false);
//			viewZLabel.setVisible(false);
			addROIButton.setVisible(false);
			delROIButton.setVisible(false);
			checkRegionsButton.setVisible(false);
			lblBlend.setVisible(false);
			blendComboBox.setVisible(false);
		}
		else if(choice == DEFINE_CELLROI || choice == DEFINE_BLEACHEDROI || choice == DEFINE_BACKGROUNDROI)
		{
			setAllComponentsVisible();
						
			zoomInButton.setEnabled(true);
			zoomOutButton.setEnabled(true);
			contrastButtonPlus.setEnabled(true);
			contrastButtonMinus.setEnabled(true);
			cropButton.setEnabled(false);
			autoCropButton.setEnabled(false);
			paintButton.setEnabled(true);
			eraseButton.setEnabled(true);
			fillButton.setEnabled(true);
			importROIMaskButton.setEnabled(true);
			clearROIbutton.setEnabled(true);
			roiTimePlotButton.setEnabled(true);
			roiAssistButton.setEnabled(true);
			//other components
			roiComboBox.setVisible(false);
			roiComboBox.setEnabled(false);
			editRoiLabel.setVisible(false);
//			zSlider.setVisible(false);
//			viewZLabel.setVisible(false);
			addROIButton.setVisible(false);
			delROIButton.setVisible(false);
			checkRegionsButton.setVisible(false);
			lblBlend.setVisible(false);
			blendComboBox.setVisible(false);
		}
	}
	
	private void setAllComponentsVisible()
	{
		BeanUtils.enableComponents(getLeftJPanel(), true);
		BeanUtils.enableComponents(editROIPanel, true);
		//buttons
		zoomInButton.setVisible(true);
		zoomOutButton.setVisible(true);
		contrastButtonPlus.setVisible(true);
		contrastButtonMinus.setVisible(true);
		cropButton.setVisible(true);
		autoCropButton.setVisible(true);
		paintButton.setVisible(true);
		paintButton.setSelected(true);
		eraseButton.setVisible(true);
		fillButton.setVisible(true);
		importROIMaskButton.setVisible(true);
		clearROIbutton.setVisible(true);
		roiTimePlotButton.setVisible(true);
		roiAssistButton.setVisible(true);
		//other components
		roiComboBox.setVisible(true);
		editRoiLabel.setVisible(true);
//		zSlider.setVisible(true);
//		viewZLabel.setVisible(true);
		addROIButton.setVisible(true);
		delROIButton.setVisible(true);
		checkRegionsButton.setVisible(true);
		lblBlend.setVisible(true);
		blendComboBox.setVisible(true);
		
		roiDrawButtonGroup.add(paintButton);
		roiDrawButtonGroup.add(eraseButton);
		roiDrawButtonGroup.add(fillButton);
		roiDrawButtonGroup.add(cropButton);
	}
	
	private void clearROI(){
		saveUndoableROI();
		if (roi!=null){
			short[] roiPixels = roi.getRoiImages()[getRoiImageIndex()].getPixels();
			for (int i = 0; i < roiPixels.length; i++) {
				roiPixels[i] = 0;
			}
			refreshROI();
		}
		fireUndoableEditROI(EDITTYPE_CLEAR);
	}
	
	private void refreshROI(){
		if (roi!=null/* && imageDataset != null*/){
//			BufferedImage highlightImage = createHighlightImageFromROI();
//			short[] highlightImageWritebackBuffer = roi.getRoiImages()[getRoiImageIndex()].getPixels();
//			getImagePane().setAllROICompositeImage((allROICompositeImageArr==null?null:allROICompositeImageArr[getRoiImageIndex()]));
			getImagePane().setHighlightImageAndWritebackBuffer(
				createHighlightImageFromROI(getROI()),
				roi.getRoiImages()[getRoiImageIndex()].getPixels());
		}else{
//			getImagePane().setAllROICompositeImage(null);
			getImagePane().setHighlightImageAndWritebackBuffer(null,null);
		}
		
		getImagePane().setAllROICompositeImage((allROICompositeImageArr==null?null:allROICompositeImageArr[getRoiImageIndex()]));
	}

	public Hashtable<String, Color> getAllCompositeROINamesAndColors(){
		Hashtable<String, Color> roiNameColor = new Hashtable<String, Color>();
		for (int i = 0; i < roiComboBox.getItemCount(); i++) {
			roiNameColor.put(((ComboboxROIName)roiComboBox.getItemAt(i)).getROIName(), ((ComboboxROIName)roiComboBox.getItemAt(i)).getHighlightColor());
		}
		return roiNameColor;
	}
	public ROI getROI(){
		return roi;
	}
	
	public void setModeRemoveROIWhenPainting(boolean bMode){
		imagePane.setModeRemoveROIWhenPainting(bMode);
	}
	public void setBlendPercent(int blendPercent){
		imagePane.setBlendPercent(blendPercent);
	}
	/**
	 * Method setROI.
	 * @param argROI ROI
	 */
	public void setROI(ROI argROI){
		if (argROI != null /*&& roi != argROI*/ /*&& (this.isShowing() || roi == null)*/){
			roi = argROI;
			roiName = roi.getROIName();
			
//			refreshROI();
			if(!imagePane.getModeRemoveROIWhenPainting()){
				if(roi != null){
					for (int i = 0; i < roiComboBox.getItemCount(); i++) {
						if(((ComboboxROIName)roiComboBox.getItemAt(i)).getROIName().equals(roi.getROIName())){
							roiComboBox.setSelectedIndex(i);
							break;
						}
					}
				}
			}
			updateROICursor();
		}else{
//			if(roiComboBox.getItemCount() != 0){
//				throw new Exception("ROI cannot be null when ROI combobox is not empty");
//			}
			roi = null;
			roiName =  null;
		}
		refreshROI();
	}
	
	public void setTimeIndex(int timeIndex){
		if(timeSlider.getValue() == timeIndex){
			forceImage();
		}else{
			timeSlider.setValue(timeIndex);
		}
	}
	
	public void saveUserChangesToROI(){
		short[] roiPixels = getImagePane().getHighlightImageWritebackImageBuffer();
		if (roiPixels!=null){
			BufferedImage highlightImage = imagePane.getHighlightImage();
			byte[] redChannelPixels = AWTImageTools.getBytes(highlightImage)[0];
			for (int i = 0; i < roiPixels.length; i++) {
				if (redChannelPixels[i]==0){
					roiPixels[i] = 0;
				}else{
					roiPixels[i] = (short)0xffff;
				}
			}
		}
	}
	
	/**
	 * Method createUnderlyingImage.
	 * @param image UShortImage
	 * @return BufferedImage
	 */
	private BufferedImage createUnderlyingImage(UShortImage image){
		int width = image.getNumX();
		int height = image.getNumY();
		ImageStatistics imageStats = image.getImageStatistics();
		short[] pixels = image.getPixels();
		byte[][] byteData = new byte[1][width*height];
		for (int i = 0; i < byteData[0].length; i++) {
			byteData[0][i] = (imageStats.maxValue < SHORT_TO_BYTE_FACTOR?(byte)pixels[i]:(byte)(((int)(pixels[i]&0x0000FFFF))/SHORT_TO_BYTE_FACTOR));
//			byteData[1][i] = byteData[0][i];
//			byteData[2][i] = byteData[0][i];
		}
//		return AWTImageTools.makeImage(byteData, width, height,false);
//		Image tempImage = AWTImageTools.makeImage(byteData, width, height,false);
		int[] cmap = new int[256];
		//colormap (grayscale)
		for(int i=0;i<256;i+= 1){
			int iv = (int)(0x000000FF&i);
			cmap[i] = 0xFF000000 | iv<<16 | iv<<8 | i;
//			if(i<16){
//				cmap[i] = 0xFFFF0000;
//			}
		}
		IndexColorModel indexColorModel =
			new java.awt.image.IndexColorModel(
				8, cmap.length,cmap,0,
				false /*false means NOT USE alpha*/   ,
				-1/*NO transparent single pixel*/,
				java.awt.image.DataBuffer.TYPE_BYTE);

		WritableRaster raster = indexColorModel.createCompatibleWritableRaster(image.getNumX(), image.getNumY());
		for (int i = 0; i < byteData[0].length; i++) {
			((DataBufferByte)raster.getDataBuffer()).getData()[i] = byteData[0][i];
		}
	    BufferedImage result = new BufferedImage(indexColorModel,raster, false, null);
//	    Graphics2D g = result.createGraphics();
//	    g.drawImage(result, 0, 0, null);
//	    g.dispose();

	    return result;
//		return AWTImageTools.makeBuffered(tempImage, indexColorModel);
	}
	
	public void displaySpecialData(short[] specialData,int width, int height) throws Exception{
		if(specialData == null){
			forceImage();
			return;
		}
		UShortImage specialUShortImage = new UShortImage(specialData,null,null,width,height,1);
		BufferedImage specialBufferedImage = createUnderlyingImage(specialUShortImage);
		imagePane.setUnderlyingImage(specialBufferedImage, false,null);
	}
	/**
	 * Method createHighlightImageFromROI.
	 * @return BufferedImage
	 */
	private BufferedImage createHighlightImageFromROI(ROI highlightImageROI){
		UShortImage roiImage = highlightImageROI.getRoiImages()[getRoiImageIndex()];
		int width = roiImage.getNumX();
		int height = roiImage.getNumY();
		byte[][] highlightData = new byte[1][width*height];
		for (int i = 0; i < highlightData[0].length; i++) {
			if (roiImage.getPixels()[i] != 0){
				highlightData[0][i] = (byte)highlightColor.getRed();
//				highlightData[1][i] = (byte)highlightColor.getGreen();
//				highlightData[2][i] = (byte)highlightColor.getBlue();
			}
		}
		Image tempImage = AWTImageTools.makeImage(highlightData, width, height,false);
		int[] cmap = new int[256];
		//colormap (grayscale)
		for(int i=0;i<256;i+= 1){
//			int iv = (int)(0x000000FF&i);
//			cmap[i] = 0xFF000000 | iv<<16 | iv<<8 | i;
			if(i != 0){
				cmap[i] = 0xFF000000 | highlightColor.getRGB();//0xFFFFFF00;
			}else{
				cmap[1] = 0xFF000000;
			}
		}
		IndexColorModel indexColorModel =
			new java.awt.image.IndexColorModel(
				8, cmap.length,cmap,0,
				false /*false means NOT USE alpha*/   ,
				-1/*NO transparent single pixel*/,
				java.awt.image.DataBuffer.TYPE_BYTE);

		return AWTImageTools.makeBuffered(tempImage, indexColorModel);
	}
	
	public void setAllowAddROI(boolean bAllowAddROI){
		this.bAllowAddROI = bAllowAddROI;
	}
	public void setROITimePlotVisible(boolean bROITimePlotVisible){
		roiTimePlotButton.setVisible(bROITimePlotVisible);
	}

//	public void addROIName(String roiName,boolean isEditable,String selectROIName){
//		addROIName0(roiName,isEditable,selectROIName,true);
//	}
	public void addROIName(String roiName,
			boolean isNameEditable,String selectROIName,boolean bDeleteable,/*boolean bPaintable,boolean bErasable,*/
			Color highlightColor){
//		roiComboBox.removeAllItems();
		try{
			roiComboBox.removeActionListener(ROI_COMBOBOX_ACTIONLISTENER);
			roiComboBox.setEnabled(true);
			roiComboBox.addItem(new ComboboxROIName(roiName,isNameEditable,bDeleteable,/*bPaintable,bErasable,*/highlightColor));
			for (int i = 0; i < roiComboBox.getItemCount(); i++) {
				if(((ComboboxROIName)roiComboBox.getItemAt(i)).getROIName().equals(selectROIName)){
					roiComboBox.setSelectedIndex(i);
					break;
				}
			}

//			for (int i = 0; i < predefinedROINames.length; i++) {
//				roiComboBox.addItem(predefinedROINames[i]);
//			}
//			roiComboBox.setSelectedIndex(0);
		}finally{
			roiComboBox.addActionListener(ROI_COMBOBOX_ACTIONLISTENER);
			ROI_COMBOBOX_ACTIONLISTENER.actionPerformed(new ActionEvent(roiComboBox,0,roiComboBox.getSelectedItem().toString()));
		}
	}
	public void deleteROIName(ComboboxROIName delComboboxROIName){
		if(delComboboxROIName != null){
			roiComboBox.removeItem(delComboboxROIName);			
		}else{
			roiComboBox.removeAllItems();
		}
		if(roiComboBox.getItemCount() == 0){
			roiComboBox.setEnabled(false);
		}

	}
	public void setAllROICompositeImage(BufferedImage[] allROICompositeImageArr){
		this.allROICompositeImageArr = allROICompositeImageArr;
		imagePane.setAllROICompositeImage(allROICompositeImageArr[getZ()]);
//		getImagePane().setAllROICompositeImage(allROICompositeImage);
	}
	/** Sets the viewer to display the given images. * @param argImageDataset ImageDataset
	 */
	public void setImages(ImageDataset argImageDataset,boolean bNew,double originalScaleFactor,double originalOffsetFactor) {
		imageDataset = argImageDataset;
		BufferedImage underlyingImage = null;
		if (imageDataset!=null){
			this.originalScaleFactor = originalScaleFactor;
			this.originalOffsetFactor = originalOffsetFactor;
			originalISize = (bNew?imageDataset.getISize():originalISize);
			if(!timeSlider.isEnabled()) //if the component is already enabled, don't do anything
			{
				BeanUtils.enableComponents(leftJPanel, true);
				BeanUtils.enableComponents(topJPanel, true);
				BeanUtils.enableComponents(editROIPanel, true);
			}
			if(!bAllowAddROI){
				addROIButton.setEnabled(false);
				delROIButton.setEnabled(false);
			}else{
				delROIButton.setEnabled(roiComboBox.getItemCount() != 0);
				checkRegionsButton.setEnabled(roiComboBox.getItemCount() != 0);
				roiComboBox.setEnabled(roiComboBox.getItemCount() != 0);
			}
			
			timeSlider.setVisible(imageDataset.getSizeT() > 1);
			viewTLabel.setVisible(timeSlider.isVisible());
			int currTimeSliderValue = timeSlider.getValue();
			if(imageDataset.getSizeT() > 1){
//				timeSlider.setLabelTable(timeSlider.createStandardLabels(imageDataset.getSizeT()-1,1));
				Hashtable<Integer, JComponent> labeltable = new Hashtable<Integer, JComponent>();
				labeltable.put(1, new JLabel(imageDataset.getImageTimeStamps()[0]+""));
				labeltable.put(imageDataset.getSizeT(),
					new JLabel(NumberUtils.formatNumber(imageDataset.getImageTimeStamps()[imageDataset.getSizeT()-1])));
				timeSlider.setLabelTable(labeltable);
				timeSlider.setMaximum(imageDataset.getSizeT());
				//Added because setMaximum SOMETIMES resets the label table
				timeSlider.setLabelTable(labeltable);
			}
			timeSlider.setValue(Math.max(1,Math.min(imageDataset.getSizeT(),currTimeSliderValue)));
			timeSlider.setMajorTickSpacing(imageDataset.getSizeT());
			timeSlider.setEnabled(imageDataset.getSizeT() > 1);
			
			zSlider.setVisible(imageDataset.getSizeZ() > 1);
			viewZLabel.setVisible(zSlider.isVisible());
			if(imageDataset.getSizeZ() > 1){
				zSlider.setLabelTable(zSlider.createStandardLabels(imageDataset.getSizeZ()-1,1));
			}
			int currZSliderValue = timeSlider.getValue();
			zSlider.setValue(Math.max(1,Math.min(imageDataset.getSizeZ(),currZSliderValue)));
			zSlider.setMaximum(imageDataset.getSizeZ());
			zSlider.setMajorTickSpacing(imageDataset.getSizeZ());
			zSlider.setEnabled(imageDataset.getSizeZ() > 1);
			
			minmaxPixelValues = calcMinMaxPixelValueRange(imageDataset);
			underlyingImage = createUnderlyingImage(imageDataset.getImage((zSlider.getValue()-1),0,(timeSlider.getValue()-1)));
//			autoCropButton.setEnabled(isAutoCroppable());
//			if(getROI() != null &&
//				(getROI().getISize().getX() != underlyingImage.getWidth() ||
//				getROI().getISize().getY() != underlyingImage.getHeight() ||
//				getROI().getISize().getZ() != imageDataset.getSizeZ())){
//				
//				getImagePane().setHighlightImageAndWritebackBuffer(null, null);
//			}
		}else{
			minmaxPixelValues = null;
			this.originalScaleFactor = DEFAULT_SCALE_FACTOR;
			this.originalOffsetFactor = DEFAULT_OFFSET_FACTOR;
			originalISize = null;
			timeSlider.setValue(1);
			timeSlider.setMaximum(1);
			timeSlider.setLabelTable(null);
			timeSlider.setEnabled(false);
			zSlider.setValue(1);
			zSlider.setMaximum(1);
			zSlider.setLabelTable(null);
			zSlider.setEnabled(false);
			BeanUtils.enableComponents(leftJPanel, false);
			BeanUtils.enableComponents(topJPanel, false);
			BeanUtils.enableComponents(editROIPanel, false);
			underlyingImage = null;
//			setROI(null);
		}

		if((getROI() != null && imageDataset != null) &&
				(getROI().getISize().getX() != imageDataset.getISize().getX() ||
				getROI().getISize().getY() != imageDataset.getISize().getY() ||
				getROI().getISize().getZ() != imageDataset.getSizeZ())){
				
				roiComboBox.removeAllItems();
				try {
					setROI(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		updateLabel(-1, -1);
		getImagePane().setUnderlyingImage(underlyingImage,bNew,minmaxPixelValues);
		if(imageDataset != null && bNew){
			contrastButtonPlus.doClick();
		}
	}

	private Range calcMinMaxPixelValueRange(ImageDataset argImageDataset){
		UShortImage[] allImages = argImageDataset.getAllImages();
		double min = 0;
		double max = min;
		for (int i = 0; i < allImages.length; i++) {
			ImageStatistics imageStats = allImages[i].getImageStatistics();
			if(i==0 || imageStats.minValue < min){min = imageStats.minValue;}
			if(i==0 || imageStats.maxValue > max){max = imageStats.maxValue;}
		}
		if(max < SHORT_TO_BYTE_FACTOR){
			return new Range(min,max);
		}
		return new Range(min/SHORT_TO_BYTE_FACTOR,max/SHORT_TO_BYTE_FACTOR);
//		short min = argImageDataset.getPixelsZ(0, 0)[0];
//		short max = min;
//		for (int c = 0; c < argImageDataset.getSizeC(); c++) {
//			for (int t = 0; t < argImageDataset.getSizeT(); t++) {
//				short[] pixelVals = argImageDataset.getPixelsZ(c, t);
//				for (int p = 0; p < pixelVals.length; p++) {
//					if(pixelVals[p] < min){min = pixelVals[p];}
//					if(pixelVals[p] > max){max = pixelVals[p];}
//				}
//			}
//		}
//		return new Range(min,max);
	}
	
	public Color getROIColor(String roiName){
		Color roiColor = null;
		for (int i = 0; i < roiComboBox.getItemCount(); i++) {
			ComboboxROIName comboboxROIName = (ComboboxROIName)roiComboBox.getItemAt(i);
			if(comboboxROIName.getROIName().equals(roiName)){
				return comboboxROIName.getHighlightColor();
			}
		}
		return roiColor;
	}
	/** Gets the currently displayed image. * @return BufferedImage
	 */
	private BufferedImage getImage() {
		if(imageDataset == null){
			return null;
		}
		int ndx = getImageIndex();
		if (imageDataset == null || ndx >= imageDataset.getAllImages().length){
			return null;
		}
		UShortImage image = imageDataset.getAllImages()[ndx];
		return createUnderlyingImage(image);
	}

	private void saveUndoableROI(){
		if(roi == null){
			return;
		}
//		saveUserChangesToROI();
		try{
			undoableROI = new ROI(roi);
		}catch(Exception e2){
			e2.printStackTrace();
			//can't happen
		}
	}
	private static final String EDITTYPE_FILL = "fill";
	private static final String EDITTYPE_PAINT = "paint";
	private static final String EDITTYPE_ERASE = "erase";
	private static final String EDITTYPE_CLEAR = "clear";
	private void fireUndoableEditROI(final String editType){
		if(undoableROI == null){
			return;
		}
		final ROI originalROI = undoableROI;
		undoableROI = null;
		if(editType != null && undoableEditSupport != null){
			undoableEditSupport.postEdit(
				new AbstractUndoableEdit(){
					public boolean canUndo() {
						return true;
					}
					public String getUndoPresentationName() {
						return editType+" "+originalROI.getROIName();
					}
					public void undo() throws CannotUndoException {
						super.undo();
						firePropertyChange(FRAP_DATA_UNDOROI_PROPERTY, null,originalROI);
					}
				}
			);			
		}else{
			if(undoableEditSupport != null)
			{
				undoableEditSupport.postEdit(OverlayEditorPanelJAI.CLEAR_UNDOABLE_EDIT);
			}
		}
		
	}
	
	private ISize getISizeDataset(){
		ISize isizeDataset = null;
		if(imageDataset != null){
			return imageDataset.getISize();
		}else if(roi != null){
			return roi.getISize();
		}else if(allROICompositeImageArr != null){
			isizeDataset = new ISize(allROICompositeImageArr[0].getWidth(), allROICompositeImageArr[0].getHeight(), allROICompositeImageArr.length);
		}
		return isizeDataset;
	}
	/**
	 * Method getImagePane.
	 * @return OverlayImageDisplayJAI
	 */
	private OverlayImageDisplayJAI getImagePane(){
		if (imagePane == null){
			imagePane = new OverlayImageDisplayJAI();
			//imagePane = new ZoomableOverlayImagePane();
			imagePane.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseExited(MouseEvent e){
					updateLabel(-1, -1);
				}
				public void mousePressed(MouseEvent e){
					updateLabel(e.getX(), e.getY());
					lastHighlightPoint = e.getPoint();
					saveUndoableROI();
					if(paintButton.isSelected() || eraseButton.isSelected()){
						if(roiComboBox.getItemCount() == 0){
							giveROIRequiredWarning("paint or erase");
							return;
						}
						drawHighlight(e.getX(), e.getY(), 10, eraseButton.isSelected());
					}
				}
				public void mouseReleased(MouseEvent e){

					if(paintButton.isSelected()){
						fireUndoableEditROI(EDITTYPE_PAINT);
					}else if(eraseButton.isSelected()){
						fireUndoableEditROI(EDITTYPE_ERASE);
					}else if(fillButton.isSelected()){
						//done later in 'click'
					}else{
						fireUndoableEditROI(null);//remove any pending undoableEdit
					}
					
					if(cropButton.isSelected()){
						ISize iSize = getISizeDataset();
						if(iSize == null || imagePane.getCrop() == null){
							imagePane.setCrop(null, null);
							return;
						}
						//Copy crop from mousedragged rectangle
						Rectangle cropRect =(Rectangle)imagePane.getCrop().clone();
						//Check crop within bounds of dataset
						cropRect.x = (int)(cropRect.x/imagePane.getZoom());
						cropRect.y = (int)(cropRect.y/imagePane.getZoom());
						cropRect.width = (int)(cropRect.width/imagePane.getZoom());
						cropRect.height = (int)(cropRect.height/imagePane.getZoom());
						if(cropRect.x < 0){cropRect.x = 0;}
						if(cropRect.y < 0){cropRect.y = 0;}
						if(cropRect.x >= iSize.getX() ||
							cropRect.y >= iSize.getY()){
							imagePane.setCrop(null, null);
							return;
						}
						if(cropRect.x+cropRect.width >= iSize.getX()){
							cropRect.width = iSize.getX()-cropRect.x;
						}
						if(cropRect.y+cropRect.height >= iSize.getY()){
							cropRect.height = iSize.getY()-cropRect.y;
						}
						if(cropRect.width <= 0){
							cropRect.width = 1;
						}if(cropRect.height <= 0){
							cropRect.height = 1;
						}

						if(!cropConfirm(cropRect)){
							return;
						}
						//turn off crop rectangle
						imagePane.setCrop(null, null);
						//tell manager about crop
						firePropertyChange(FRAP_DATA_CROP_PROPERTY, null, cropRect);
					}
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(fillButton.isSelected()){
						if(roiComboBox.getItemCount() == 0){
							giveROIRequiredWarning("fill");
							return;
						}
						fireUndoableEditROI(EDITTYPE_FILL);
						try{
							waitCursor(true);
							if(imagePane.getHighlightImage() != null){
								ROI.fillAtPoint(
										(int)(e.getPoint().getX()/imagePane.getZoom()),
										(int)(e.getPoint().getY()/imagePane.getZoom()),
										imagePane.getHighlightImage(),
										highlightColor.getRGB());								
							}
							if(imagePane.getAllROICompositeImage() != null){
								ROI.fillAtPoint(
										(int)(e.getPoint().getX()/imagePane.getZoom()),
										(int)(e.getPoint().getY()/imagePane.getZoom()),
										imagePane.getAllROICompositeImage(),
										((ComboboxROIName)roiComboBox.getSelectedItem()).getHighlightColor().getRGB());								
							}
						}finally{
							imagePane.refreshImage();
							waitCursor(false);
						}
					}
				}
			});
			imagePane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {   
				@Override
				public void mouseDragged(java.awt.event.MouseEvent e) {
					updateLabel(e.getX(), e.getY());
					if(paintButton.isSelected() || eraseButton.isSelected()){
						drawHighlight(e.getX(), e.getY(), 10, eraseButton.isSelected());
						lastHighlightPoint = e.getPoint();
//						VirtualFrapLoader.mf.setSaveStatus(true);
					}else if(cropButton.isSelected()){
						imagePane.setCrop(lastHighlightPoint, e.getPoint());
					}
				}
				@Override
				public void mouseMoved(java.awt.event.MouseEvent e) {
					updateLabel(e.getX(), e.getY());
				}
			});
		}
		return imagePane;
	}
	
	private void giveROIRequiredWarning(String toolDescription){
		DialogUtils.showWarningDialog(this, "You must add at least 1 ROI before trying to use the '"+toolDescription+"' tool.");
		addROIActionListener.actionPerformed(null);
	}
	private boolean cropConfirm(Rectangle cropRect){
		JLabel cropConfirmJlabel =
			new JLabel("Crop FRAP data to new bounds?: ("+cropRect.x+","+cropRect.y+") to ("+
					(cropRect.x+cropRect.width-1)+","+(cropRect.y+cropRect.height-1)+")");
		cropConfirmJlabel.setPreferredSize(new Dimension(300,40));
		cropConfirmJlabel.setMinimumSize(new Dimension(300,40));
		if(DialogUtils.showComponentOKCancelDialog(
			OverlayEditorPanelJAI.this,
			cropConfirmJlabel,
			"Confirm Crop FRAP Data to new boundaries.") != JOptionPane.OK_OPTION){
			imagePane.setCrop(null, null);
			return false;
		}
//		VirtualFrapLoader.mf.setSaveStatus(true);
		return true;
	}
	/**
	 * Method drawHighlight.
	 * @param x int
	 * @param y int
	 * @param radius int
	 * @param erase boolean
	 */
	private void drawHighlight(int x, int y, int radius, boolean bErase){
		if(roiComboBox.getSelectedItem() == null){
			return;
		}
//		if(bErase && !((ComboboxROIName)roiComboBox.getSelectedItem()).isErasable()){
//			return;
//		}else if(!bErase && !((ComboboxROIName)roiComboBox.getSelectedItem()).isPaintable()){
//			return;
//		}
		imagePane.drawHighlight(x, y, radius, bErase, highlightColor,
				((ComboboxROIName)roiComboBox.getSelectedItem()).getHighlightColor(), lastHighlightPoint);
		repaint();
	}

	/**
	 * This method initializes timeSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getTimeSlider() {
		if (timeSlider == null) {
			timeSlider = new JSlider(1,1);
			timeSlider.setPaintLabels(true);
			timeSlider.setMajorTickSpacing(100);
			timeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					forceImage();
//					updateLabel(-1, -1);
//					if(imageDataset != null){
//						BufferedImage image = getImage();
//						if (image != null){
//							imagePane.setUnderlyingImage(image,false);
//						}
//					}
//					imagePane.repaint();
				}
			});
		}
		return timeSlider;
	}
	private void forceImage(){
		updateLabel(-1, -1);
		if(imageDataset != null){
			BufferedImage image = getImage();
			if (image != null){
				imagePane.setUnderlyingImage(image,false,minmaxPixelValues);
			}
		}
		imagePane.repaint();

	}
	/** Gets the index of the currently displayed image. * @return int
	 */
	private int getImageIndex() { return imageDataset.getIndexFromZCT(getZ(),0,getT()); }
	
	/** Gets the index of the currently displayed image. * @return int
	 */
	private int getRoiImageIndex() { return getZ(); }
	
	/** Gets the T value of the currently displayed image. * @return int
	 */
	public int getT() { return Math.max(0,Math.min(imageDataset.getSizeT(),timeSlider.getValue())) - 1; }

	/** Gets the Z value of the currently displayed image. * @return int
	 */
	public int getZ() { return Math.max(0,Math.min(imageDataset.getSizeZ(),zSlider.getValue())) - 1; }


	/** Updates cursor probe label. * @param x int
	 * @param y int
	 */
	protected void updateLabel(int inx, int iny) {
		if (imageDataset == null) {
			return;
		}
		float zoom = imagePane.getZoom();
		if((inx/zoom)>= imageDataset.getISize().getX() || (iny/zoom)>= imageDataset.getISize().getY()){
			inx = -1;
			iny = -1;
		}
		int x = (int)(inx/zoom);
		int y = (int)(iny/zoom);
		UShortImage[] images = imageDataset.getAllImages();
		int ndx = getImageIndex();
		sb.setLength(0);
		boolean bMultipleZ = imageDataset.getSizeZ() > 1;
		if (bMultipleZ) {
			sb.append("; Z=");
			sb.append(getZ() + 1);
			sb.append("/");
			sb.append(imageDataset.getSizeZ());
		}
		if (imageDataset.getSizeT() > 1) {
			sb.append((bMultipleZ?"; ":"")+"T=");
//			sb.append(getT() + 1);
			sb.append(imageDataset.getImageTimeStamps()[getT()]);
//			sb.append("/");
//			sb.append(imageDataset.getSizeT());
		}
//		BufferedImage image = ImageTools.makeImage(images[ndx].getPixels(), images[ndx].getNumX(), images[ndx].getNumY());
//		int w = image == null ? -1 : image.getWidth();
//		int h = image == null ? -1 : image.getHeight();
		int w = imageDataset == null ? -1 : imageDataset.getISize().getX();
		int h = imageDataset == null ? -1 : imageDataset.getISize().getY();
		if (x >= w) x = w - 1;
		if (y >= h) y = h - 1;
		if (x >= 0 && y >= 0 && inx >= 0 && iny >= 0 &&
				x < imageDataset.getISize().getX() &&
				y < imageDataset.getISize().getY()){
			if (images.length > 1) sb.append("; ");
			sb.append("X=");
			sb.append(x);
			if (w > 0) {
				sb.append("/");
				sb.append(w);
			}
			sb.append("; Y=");
			sb.append(y);
			if (h > 0) {
				sb.append("/");
				sb.append(h);
			}
			sb.append("; zoom("+NumberUtils.formatNumber(imagePane.getZoom(), 3)+")");
			sb.append("; contr("+imagePane.getContrastDescription()+")");
			if (imageDataset != null) {
//				Raster r = image.getRaster();
//				double[] pix = r.getPixel(x, y, (double[]) null);
				short[] pix = null;
				try{
					pix = new short[imageDataset.getSizeC()];
					for (int i = 0; i < pix.length; i++) {
						pix[i] = imageDataset.getImage(getZ(), i, getT()).getPixel(x, y, 0);						
					}
				}catch(Exception e){
					pix = null;
					e.printStackTrace();
					//do nothing
				}
				sb.append("; value"+(isOriginalValueScaled()?"(scld)":""));
				if(pix != null){
					sb.append(pix.length > 1 ? "s=(" : "=");
					for (int i=0; i<pix.length; i++) {
						if (i > 0) sb.append(", ");
						sb.append((int)(pix[i]&0x0000FFFF));
					}
					if (pix.length > 1) sb.append(")");
				}else{
					sb.append(" error");
				}
				if(isOriginalValueScaled()){
					sb.append("; value(orig)");
					if(pix != null){
						sb.append(pix.length > 1 ? "s=(" : "=");
						for (int i=0; i<pix.length; i++) {
							if (i > 0) sb.append(", ");
							sb.append(
								NumberUtils.formatNumber(
								(((int)(pix[i]&0x0000FFFF))-originalOffsetFactor)/
								originalScaleFactor
								, 6));
						}
						if (pix.length > 1) sb.append(")");
					}else{
						sb.append(" error");
					}
					
				}
				
//				sb.append(" udnerly="+Integer.toHexString(imagePane.getUnderlyingImage().getRGB(x, y)));
			}
		}else{
			sb.append("; zoom("+NumberUtils.formatNumber(imagePane.getZoom(), 3)+")");
			sb.append("; contr("+imagePane.getContrastDescription()+")");
		}
		sb.append(" ");
		textLabel.setText(sb.toString());
	}

	private boolean isOriginalValueScaled(){
		if(originalOffsetFactor != 0 || originalScaleFactor != 1){
			return true;
		}
		return false;
	}
	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getImagePane());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLeftJPanel() {
		if (leftJPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.ipady = 0;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.anchor = GridBagConstraints.NORTH;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.ipady = 0;
			gridBagConstraints.gridy = 0;
			
			//this so call "leftPanel" has been move to the right side of the editor panel for the new frap.
			leftJPanel = new JPanel();
			final GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.rowHeights = new int[] {0,0,7,7,7,0,7};
			leftJPanel.setLayout(gridBagLayout);
			leftJPanel.add(getZoomInButton(), gridBagConstraints);
			leftJPanel.add(getZoomOutButton(), gridBagConstraints3);

			contrastButtonPlus = new JButton(new ImageIcon(getClass().getResource("/images/contrastUp.gif")));
			contrastButtonPlus.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					imagePane.increaseContrast();
					updateLabel(-1,-1);
				}
			});
			contrastButtonPlus.setPreferredSize(new Dimension(32, 32));
			contrastButtonPlus.setMinimumSize(new Dimension(32, 32));
			contrastButtonPlus.setMaximumSize(new Dimension(32, 32));
			contrastButtonPlus.setMargin(new Insets(2, 2, 2, 2));
			contrastButtonPlus.setToolTipText("Increase Contrast");
			final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
			gridBagConstraints_4.gridy = 2;
			gridBagConstraints_4.gridx = 0;
			leftJPanel.add(contrastButtonPlus, gridBagConstraints_4);

			contrastButtonMinus = new JButton(new ImageIcon(getClass().getResource("/images/contrastDown.gif")));
			contrastButtonMinus.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					imagePane.decreaseContrast();
					updateLabel(-1,-1);
				}
			});
			contrastButtonMinus.setPreferredSize(new Dimension(32, 32));
			contrastButtonMinus.setMinimumSize(new Dimension(32, 32));
			contrastButtonMinus.setMaximumSize(new Dimension(32, 32));
			contrastButtonMinus.setMargin(new Insets(2, 2, 2, 2));
			contrastButtonMinus.setToolTipText("Decrease Contrast");
			final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
			gridBagConstraints_6.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints_6.gridy = 3;
			gridBagConstraints_6.gridx = 0;
			leftJPanel.add(contrastButtonMinus, gridBagConstraints_6);

			cropButton = new JToggleButton(new ImageIcon(getClass().getResource("/images/crop.gif")));
			cropButton.setPreferredSize(new Dimension(32, 32));
			cropButton.setMinimumSize(new Dimension(32, 32));
			cropButton.setMaximumSize(new Dimension(32, 32));
			cropButton.setMargin(new Insets(2, 2, 2, 2));
			cropButton.setToolTipText("Crop");
			final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
			gridBagConstraints_5.gridy = 4;
			gridBagConstraints_5.gridx = 0;
			leftJPanel.add(cropButton, gridBagConstraints_5);
			
			
			autoCropButton.setPreferredSize(new Dimension(32, 32));
			autoCropButton.setMinimumSize(new Dimension(32, 32));
			autoCropButton.setMaximumSize(new Dimension(32, 32));
			autoCropButton.setMargin(new Insets(2, 2, 2, 2));
			autoCropButton.setToolTipText("Auto Crop");
			final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
			gridBagConstraints_7.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints_7.gridy = 5;
			gridBagConstraints_7.gridx = 0;
			leftJPanel.add(autoCropButton, gridBagConstraints_7);
			
			paintButton = new JToggleButton(new ImageIcon(getClass().getResource("/images/paint.gif")));
			paintButton.setSelected(true);
			paintButton.setPreferredSize(new Dimension(32, 32));
			paintButton.setMinimumSize(new Dimension(32, 32));
			paintButton.setMaximumSize(new Dimension(32, 32));
			paintButton.setPreferredSize(new Dimension(32, 32));
			paintButton.setMinimumSize(new Dimension(32, 32));
			paintButton.setMaximumSize(new Dimension(32, 32));
			paintButton.setMargin(new Insets(2, 2, 2, 2));
			paintButton.setToolTipText("Paint");
			final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
			gridBagConstraints_1.gridy = 6;
			gridBagConstraints_1.gridx = 0;
			leftJPanel.add(paintButton, gridBagConstraints_1);

			eraseButton = new JToggleButton(new ImageIcon(getClass().getResource("/images/eraser.gif")));
			eraseButton.setPreferredSize(new Dimension(32, 32));
			eraseButton.setMinimumSize(new Dimension(32, 32));
			eraseButton.setMaximumSize(new Dimension(32, 32));
			eraseButton.setPreferredSize(new Dimension(32, 32));
			eraseButton.setMinimumSize(new Dimension(32, 32));
			eraseButton.setMaximumSize(new Dimension(32, 32));
			eraseButton.setMargin(new Insets(2, 2, 2, 2));
			eraseButton.setToolTipText("Erase");
			final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
			gridBagConstraints_2.gridy = 7;
			gridBagConstraints_2.gridx = 0;
			leftJPanel.add(eraseButton, gridBagConstraints_2);

			fillButton = new JToggleButton(new ImageIcon(getClass().getResource("/images/fill.gif")));
			fillButton.setPreferredSize(new Dimension(32, 32));
			fillButton.setMinimumSize(new Dimension(32, 32));
			fillButton.setMaximumSize(new Dimension(32, 32));
			fillButton.setPreferredSize(new Dimension(32, 32));
			fillButton.setMinimumSize(new Dimension(32, 32));
			fillButton.setMaximumSize(new Dimension(32, 32));
			fillButton.setMargin(new Insets(2, 2, 2, 2));
			fillButton.setToolTipText("Fill");
			final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
			gridBagConstraints_3.gridy = 8;
			gridBagConstraints_3.gridx = 0;
			gridBagConstraints_3.insets = new Insets(0, 0, 10, 0);
			leftJPanel.add(fillButton, gridBagConstraints_3);

			importROIMaskButton.setPreferredSize(new Dimension(32, 32));
			importROIMaskButton.setMinimumSize(new Dimension(32, 32));
			importROIMaskButton.setMaximumSize(new Dimension(32, 32));
			importROIMaskButton.setPreferredSize(new Dimension(32, 32));
			importROIMaskButton.setMinimumSize(new Dimension(32, 32));
			importROIMaskButton.setMaximumSize(new Dimension(32, 32));
			importROIMaskButton.setMargin(new Insets(2, 2, 2, 2));
			importROIMaskButton.setToolTipText("Import ROI mask from file");
			final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
			gridBagConstraints_8.gridy = 9;
			gridBagConstraints_8.gridx = 0;
			leftJPanel.add(importROIMaskButton, gridBagConstraints_8);
			
			clearROIbutton.setPreferredSize(new Dimension(32, 32));
			clearROIbutton.setMinimumSize(new Dimension(32, 32));
			clearROIbutton.setMaximumSize(new Dimension(32, 32));
			clearROIbutton.setPreferredSize(new Dimension(32, 32));
			clearROIbutton.setMinimumSize(new Dimension(32, 32));
			clearROIbutton.setMaximumSize(new Dimension(32, 32));
			clearROIbutton.setMargin(new Insets(2, 2, 2, 2));
			clearROIbutton.setToolTipText("Clear ROI");
			final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
			gridBagConstraints_9.gridy = 10;
			gridBagConstraints_9.gridx = 0;
			leftJPanel.add(clearROIbutton, gridBagConstraints_9);
			
			roiTimePlotButton.setPreferredSize(new Dimension(32, 32));
			roiTimePlotButton.setMinimumSize(new Dimension(32, 32));
			roiTimePlotButton.setMaximumSize(new Dimension(32, 32));
			roiTimePlotButton.setPreferredSize(new Dimension(32, 32));
			roiTimePlotButton.setMinimumSize(new Dimension(32, 32));
			roiTimePlotButton.setMaximumSize(new Dimension(32, 32));
			roiTimePlotButton.setMargin(new Insets(2, 2, 2, 2));
			roiTimePlotButton.setToolTipText("ROI time plot");
			final GridBagConstraints gridBagConstraints_10 = new GridBagConstraints();
			gridBagConstraints_10.gridy = 11;
			gridBagConstraints_10.gridx = 0;
			leftJPanel.add(roiTimePlotButton, gridBagConstraints_10);
			
			roiAssistButton.setPreferredSize(new Dimension(32, 32));
			roiAssistButton.setMinimumSize(new Dimension(32, 32));
			roiAssistButton.setMaximumSize(new Dimension(32, 32));
			roiAssistButton.setPreferredSize(new Dimension(32, 32));
			roiAssistButton.setMinimumSize(new Dimension(32, 32));
			roiAssistButton.setMaximumSize(new Dimension(32, 32));
			roiAssistButton.setMargin(new Insets(2, 2, 2, 2));
			roiAssistButton.setToolTipText("ROI Assist");
			final GridBagConstraints gridBagConstraints_11 = new GridBagConstraints();
			gridBagConstraints_11.gridy = 12;
			gridBagConstraints_11.gridx = 0;
			leftJPanel.add(roiAssistButton, gridBagConstraints_11);
			
		}
		return leftJPanel;
	}

	/**
	 * This method initializes zoomInButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getZoomInButton() {
		if (zoomInButton == null) {
			zoomInButton = new JButton();
			zoomInButton.setMargin(new Insets(2, 2, 2, 2));
			zoomInButton.setMinimumSize(new Dimension(32, 32));
			zoomInButton.setMaximumSize(new Dimension(32, 32));
			zoomInButton.setIcon(new ImageIcon(getClass().getResource("/images/zoomin.gif")));
			zoomInButton.setPreferredSize(new Dimension(32, 32));
			zoomInButton.setIcon(new ImageIcon(getClass().getResource("/images/zoomin.gif")));
			zoomInButton.setToolTipText("Zoom In");
			zoomInButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getImagePane().setZoom(getImagePane().getZoom()*1.3f);
					updateLabel(-1,-1);
				}
			});
		}
		return zoomInButton;
	}
	
	/**
	 * This method initializes zoomOutButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getZoomOutButton() {
		if (zoomOutButton == null) {
			zoomOutButton = new JButton();
			zoomOutButton.setPreferredSize(new Dimension(32, 32));
			zoomOutButton.setMinimumSize(new Dimension(32, 32));
			zoomOutButton.setMaximumSize(new Dimension(32, 32));
			zoomOutButton.setMargin(new Insets(2, 2, 2, 2));
			zoomOutButton.setIcon(new ImageIcon(getClass().getResource("/images/zoomout.gif")));
			zoomOutButton.setPreferredSize(new Dimension(32, 32));
			zoomOutButton.setToolTipText("Zoom Out");
			zoomOutButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getImagePane().setZoom(getImagePane().getZoom()/1.3f);
					updateLabel(-1,-1);
				}
			});
			zoomOutButton.setIcon(new ImageIcon(getClass().getResource("/images/zoomout.gif")));
		}
		return zoomOutButton;
	}


	/**
	 * This method initializes zSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getZSlider() {
		if (zSlider == null) {
			zSlider = new JSlider(1,1);
			zSlider.setMajorTickSpacing(100);
			zSlider.setPaintLabels(true);
			zSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					saveUserChangesToROI();
					updateLabel(-1, -1);
					BufferedImage image = getImage();
					if (image != null){
						imagePane.setUnderlyingImage(image,false,minmaxPixelValues);
					}
					refreshROI();
					imagePane.repaint();
				}
			});
		}
		return zSlider;
	}
	
	public void setCustomROIImport(CustomROIImport customROIImport){
		this.customROIImport = customROIImport;
	}

	public void setDefaultImportDirAndFilters(File defaultImportDir,FileFilter[] fileFilterArr){
	    openJFileChooser.setCurrentDirectory(defaultImportDir);
	    for (int i = 0; i < fileFilterArr.length; i++) {
	    	openJFileChooser.addChoosableFileFilter(fileFilterArr[i]);
		}
	}
	
	public ROISourceData getRoiSouceData()
	{
		if(roiSourceData == null){
			roiSourceData = new ROISourceData(){
				public void addReplaceRoi(ROI newROI) {
					roi = newROI;
					refreshROI();
//					imagePane.setHighlightImageAndWritebackBuffer(createHighlightImageFromROI(newROI),
//							newROI.getRoiImages()[getRoiImageIndex()].getPixels());
					
					
//					roi.setRoiName(originalROI.getROIName());
//					roi.setROIImages(originalROI.getRoiImages());
//					refreshROI();
//					if(roi != null){
//						for (int i = 0; i < roiComboBox.getItemCount(); i++) {
//							if(((ComboboxROIName)roiComboBox.getItemAt(i)).getROIName().equals(roi.getROIName())){
//								roiComboBox.setSelectedIndex(i);
//								break;
//							}
//						}
//					}
//					updateROICursor();
				}
				public ROI getCurrentlyDisplayedROI(){
					return roi;
				}
				public ImageDataset getImageDataset() {
					return OverlayEditorPanelJAI.this.imageDataset;
				}
			};
		}
		return roiSourceData;
	}

	public void setRoiSouceData(ROISourceData roiSourceData) 
	{
		this.roiSourceData = roiSourceData;
	}
}
