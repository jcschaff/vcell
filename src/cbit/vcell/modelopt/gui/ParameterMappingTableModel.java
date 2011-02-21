package cbit.vcell.modelopt.gui;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;

import org.vcell.util.gui.ScrollTable;

import cbit.vcell.client.desktop.biomodel.VCellSortTableModel;
import cbit.vcell.model.Kinetics.UnresolvedParameter;
import cbit.vcell.model.Model.ModelParameter;
import cbit.vcell.model.Parameter;
import cbit.vcell.modelopt.ModelOptimizationSpec;
import cbit.vcell.modelopt.ParameterEstimationTask;
import cbit.vcell.modelopt.ParameterMappingSpec;
import cbit.vcell.parser.ExpressionException;
/**
 * Insert the type's description here.
 * Creation date: (2/23/01 10:52:36 PM)
 * @author: 
 */
@SuppressWarnings("serial")
public class ParameterMappingTableModel extends VCellSortTableModel<ParameterMappingSpec> implements PropertyChangeListener {

	private final static int COLUMN_NAME = 0;
	private final static int COLUMN_SCOPE = 1;
	private final static int COLUMN_MODELVALUE = 2;
	private final static int COLUMN_SELECTED = 3;
	public final static int COLUMN_CURRENTVALUE = 4;
	private final static int COLUMN_LOWVALUE = 5;
	private final static int COLUMN_HIGHVALUE = 6;
	public final static int COLUMN_SOLUTION = 7;
	private final static String LABELS[] = { "Parameter", "Context",  "Model Value", "Optimize", "Initial Guess", "Lower", "Upper", "Solution"  };
	private ParameterEstimationTask fieldParameterEstimationTask = null;

	private static class ParameterColumnComparator implements Comparator<ParameterMappingSpec> {
		private int index;
		private int scale = 1;

		public ParameterColumnComparator(int index, boolean ascending){
			this.index = index;
			scale = ascending ? 1 : -1;
		}
		
		/**
		 * Compares its two arguments for order.  Returns a negative integer,
		 * zero, or a positive integer as the first argument is less than, equal
		 * to, or greater than the second.<p>
		 */
		public int compare(ParameterMappingSpec pms1, ParameterMappingSpec pms2){
			
			Parameter parm1 = pms1.getModelParameter();
			Parameter parm2 = pms2.getModelParameter();
			
			switch (index){
				case COLUMN_NAME:{
					return scale * parm1.getName().compareToIgnoreCase(parm2.getName());
				}
				case COLUMN_MODELVALUE:{
					try {
						Double parm1_Value = new Double(parm1.getExpression().evaluateConstant());
						Double parm2_Value = new Double(parm2.getExpression().evaluateConstant());
						return scale * parm1_Value.compareTo(parm2_Value);
					} catch (ExpressionException e1) {
						e1.printStackTrace(System.out);
						return scale * parm1.getExpression().infix().compareToIgnoreCase(parm2.getExpression().infix());
					}
				}
				case COLUMN_SCOPE:{
					return scale * parm1.getNameScope().getName().compareToIgnoreCase(parm2.getNameScope().getName());
				}
				case COLUMN_SELECTED:{
					String sel1 = (new Boolean(pms1.isSelected())).toString();
					String sel2 = (new Boolean(pms2.isSelected())).toString();
					return scale * sel1.compareToIgnoreCase(sel2);
				}
				case COLUMN_CURRENTVALUE:{
					return scale * (new Double(pms1.getCurrent())).compareTo(new Double(pms2.getCurrent()));
				}
				case COLUMN_LOWVALUE:{
					return scale * (new Double(pms1.getLow())).compareTo(new Double(pms2.getLow()));
				}
				case COLUMN_HIGHVALUE:{					
					return scale * (new Double(pms1.getHigh())).compareTo(new Double(pms2.getHigh()));
				}
			}
			return 1;
		}
	}
	
/**
 * ReactionSpecsTableModel constructor comment.
 */
public ParameterMappingTableModel(ScrollTable table) {
	super(table, LABELS);
	addPropertyChangeListener(this);
}

/**
 * Insert the method's description here.
 * Creation date: (2/24/01 12:24:35 AM)
 * @return java.lang.Class
 * @param column int
 */
public Class<?> getColumnClass(int column) {
	switch (column){
		case COLUMN_NAME:{
			return String.class;
		}
		case COLUMN_SCOPE:{
			return String.class;
		}
		case COLUMN_CURRENTVALUE:{
			return Double.class;
		}
		case COLUMN_MODELVALUE:{
			return Double.class;
		}
		case COLUMN_HIGHVALUE:{
			return Double.class;
		}
		case COLUMN_LOWVALUE:{
			return Double.class;
		}
		case COLUMN_SELECTED:{
			return Boolean.class;
		}
		case COLUMN_SOLUTION:{
			return Double.class;
		}
		default:{
			return Object.class;
		}
	}
}

/**
 * Gets the parameterEstimationTask property (cbit.vcell.modelopt.ParameterEstimationTask) value.
 * @return The parameterEstimationTask property value.
 * @see #setParameterEstimationTask
 */
public ParameterEstimationTask getParameterEstimationTask() {
	return fieldParameterEstimationTask;
}

/**
 * Insert the method's description here.
 * Creation date: (9/23/2003 1:24:52 PM)
 * @return cbit.vcell.model.Parameter
 * @param row int
 */
private void refreshData() {
	if (getParameterEstimationTask() == null) {
		setData(null);
	} else {
		setData(Arrays.asList(getParameterEstimationTask().getModelOptimizationSpec().getParameterMappingSpecs()));
	}
}


/**
 * getValueAt method comment.
 */
public Object getValueAt(int row, int col) {
	if (col<0 || col>=getColumnCount()){
		throw new RuntimeException("ParameterTableModel.getValueAt(), column = "+col+" out of range ["+0+","+(getColumnCount()-1)+"]");
	}
	if (row<0 || row>=getRowCount()){
		throw new RuntimeException("ParameterTableModel.getValueAt(), row = "+row+" out of range ["+0+","+(getRowCount()-1)+"]");
	}
	ParameterMappingSpec parameterMappingSpec = getValueAt(row);
	switch (col){
		case COLUMN_NAME:{
			return parameterMappingSpec.getModelParameter().getName();
		}
		case COLUMN_SCOPE:{
			if (parameterMappingSpec.getModelParameter() instanceof UnresolvedParameter){
				return "unresolved";
			}else if (parameterMappingSpec.getModelParameter().getNameScope()==null){
				return "null";
			} if (parameterMappingSpec.getModelParameter() instanceof ModelParameter) {
				return "Model";
			} else{
				return parameterMappingSpec.getModelParameter().getNameScope().getName();
			}
		}
		case COLUMN_MODELVALUE:{
			try {
				return new Double(parameterMappingSpec.getModelParameter().getExpression().evaluateConstant());
			}catch (ExpressionException e){
				e.printStackTrace(System.out);
				return null;
			}
		}
		case COLUMN_CURRENTVALUE:{
			return new Double(parameterMappingSpec.getCurrent());
		}
		case COLUMN_LOWVALUE:{
			return new Double(parameterMappingSpec.getLow());
		}
		case COLUMN_HIGHVALUE:{
			return new Double(parameterMappingSpec.getHigh());
		}
		case COLUMN_SELECTED:{
			return new Boolean(parameterMappingSpec.isSelected());
		}
		case COLUMN_SOLUTION:{
			if (getParameterEstimationTask()!=null) {
				return getParameterEstimationTask().getCurrentSolution(parameterMappingSpec);
			}
			return null;
		}
		default:{
			return null;
		}
	}
}


/**
 * Insert the method's description here.
 * Creation date: (2/24/01 12:27:46 AM)
 * @return boolean
 * @param rowIndex int
 * @param columnIndex int
 */
public boolean isCellEditable(int rowIndex, int columnIndex) {
	if (columnIndex == COLUMN_NAME){
		return false;
	}else if (columnIndex == COLUMN_SCOPE){
		return false;
	}else if (columnIndex == COLUMN_SELECTED){
		return true;
	}else if (columnIndex == COLUMN_CURRENTVALUE){
		return true;
	}else if (columnIndex == COLUMN_LOWVALUE){
		return true;
	}else if (columnIndex == COLUMN_HIGHVALUE){
		return true;
	}else if (columnIndex == COLUMN_MODELVALUE){
		return false;
	}else if (columnIndex == COLUMN_SOLUTION){
		return false;
	}else{
		return false;
	}
}


/**
 * isSortable method comment.
 */
public boolean isSortable(int col) {
	switch (col){
		case COLUMN_NAME:{
			return true;
		}
		case COLUMN_SCOPE:{
			return true;
		}
		case COLUMN_CURRENTVALUE:{
			return true;
		}
		case COLUMN_MODELVALUE:{
			return true;
		}
		case COLUMN_HIGHVALUE:{
			return true;
		}
		case COLUMN_LOWVALUE:{
			return true;
		}
		case COLUMN_SELECTED:{
			return true;
		}
		case COLUMN_SOLUTION:{
			return false;
		}
		default:{
			return false;
		}
	}
}


/**
	 * This method gets called when a bound property is changed.
	 * @param evt A PropertyChangeEvent object describing the event source 
	 *   and the property that has changed.
	 */
public void propertyChange(java.beans.PropertyChangeEvent evt) {
	if (evt.getSource() == this && evt.getPropertyName().equals("parameterEstimationTask")) {
		ParameterEstimationTask oldValue = (ParameterEstimationTask)evt.getOldValue();
		if (oldValue!=null){
			oldValue.removePropertyChangeListener(this);
			oldValue.getModelOptimizationSpec().removePropertyChangeListener(this);
			ParameterMappingSpec[] oldPMS = oldValue.getModelOptimizationSpec().getParameterMappingSpecs();
			for (int i = 0; oldPMS!=null && i < oldPMS.length; i++){
				oldPMS[i].removePropertyChangeListener(this);
			}
		}
		ParameterEstimationTask newValue = (ParameterEstimationTask)evt.getNewValue();
		if (newValue!=null){
			newValue.addPropertyChangeListener(this);
			newValue.getModelOptimizationSpec().addPropertyChangeListener(this);
			ParameterMappingSpec[] newPMS = newValue.getModelOptimizationSpec().getParameterMappingSpecs();
			for (int i = 0; newPMS!=null && i < newPMS.length; i++){
				newPMS[i].addPropertyChangeListener(this);
			}
		}
		refreshData();
	} else if (evt.getSource() == getParameterEstimationTask() && evt.getPropertyName().equals("optimizationResultSet")) {
		refreshData();
	} else if (evt.getSource() instanceof ModelOptimizationSpec && evt.getPropertyName().equals("parameterMappingSpecs")) {
		ParameterMappingSpec[] oldValues = (ParameterMappingSpec[])evt.getOldValue();
		if (oldValues!=null){
			for (int i = 0; i < oldValues.length; i++){
				oldValues[i].removePropertyChangeListener(this);
			}
		}
		ParameterMappingSpec[] newValues = (ParameterMappingSpec[])evt.getNewValue();
		if (newValues!=null){
			for (int i = 0; i < newValues.length; i++){
				newValues[i].addPropertyChangeListener(this);
			}
		}
		refreshData();
	} else if (evt.getSource() instanceof ParameterMappingSpec) {
		fireTableDataChanged();
	}
}


/**
 * Sets the parameterEstimationTask property (cbit.vcell.modelopt.ParameterEstimationTask) value.
 * @param parameterEstimationTask The new value for the property.
 * @see #getParameterEstimationTask
 */
public void setParameterEstimationTask(ParameterEstimationTask parameterEstimationTask) {
	ParameterEstimationTask oldValue = fieldParameterEstimationTask;
	fieldParameterEstimationTask = parameterEstimationTask;
	firePropertyChange("parameterEstimationTask", oldValue, parameterEstimationTask);
}


	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex<0 || columnIndex>=getColumnCount()){
			throw new RuntimeException("ParameterTableModel.setValueAt(), column = "+columnIndex+" out of range ["+0+","+(getColumnCount()-1)+"]");
		}
		ParameterMappingSpec parameterMappingSpec = getValueAt(rowIndex);
		switch (columnIndex){
			case COLUMN_SELECTED:{
				if (aValue instanceof Boolean){
					Boolean bSelected = (Boolean)aValue;
					if (parameterMappingSpec.isSelected() != bSelected.booleanValue()){
						parameterMappingSpec.setSelected(bSelected.booleanValue());
						fireTableRowsUpdated(rowIndex,rowIndex);
					}
				}
				break;
			}
			case COLUMN_LOWVALUE:{
				if (aValue instanceof Double){
					double value = ((Double)aValue).doubleValue();
					parameterMappingSpec.setLow(value);
					fireTableRowsUpdated(rowIndex,rowIndex);
				}else if (aValue instanceof String) {
					String newDoubleString = (String)aValue;
					try {
						parameterMappingSpec.setLow(Double.parseDouble(newDoubleString));
						fireTableRowsUpdated(rowIndex,rowIndex);
					}catch (NumberFormatException e){
						if (newDoubleString.equals("") || newDoubleString.equalsIgnoreCase("-Infinity") || newDoubleString.equalsIgnoreCase("-Inf")){
							parameterMappingSpec.setLow(Double.NEGATIVE_INFINITY);
							fireTableRowsUpdated(rowIndex,rowIndex);
						}
					}
				}
				break;
			}
			case COLUMN_HIGHVALUE:{
				if (aValue instanceof Double){
					double value = ((Double)aValue).doubleValue();
					parameterMappingSpec.setHigh(value);
					fireTableRowsUpdated(rowIndex,rowIndex);
				}else if (aValue instanceof String) {
					String newDoubleString = (String)aValue;
					try {
						parameterMappingSpec.setHigh(Double.parseDouble(newDoubleString));
						fireTableRowsUpdated(rowIndex,rowIndex);
					}catch (NumberFormatException e){
						if (newDoubleString.equals("") || newDoubleString.equalsIgnoreCase("Infinity") || newDoubleString.equalsIgnoreCase("Inf")){
							parameterMappingSpec.setHigh(Double.POSITIVE_INFINITY);
							fireTableRowsUpdated(rowIndex,rowIndex);
						}
					}
				}
				break;
			}
			case COLUMN_CURRENTVALUE:{
				if (aValue instanceof Double){
					double value = ((Double)aValue).doubleValue();
					parameterMappingSpec.setCurrent(value);
					fireTableRowsUpdated(rowIndex,rowIndex);
				}else if (aValue instanceof String) {
					parameterMappingSpec.setCurrent(Double.parseDouble((String)aValue));
					fireTableRowsUpdated(rowIndex,rowIndex);
				}
				break;
			}
		}
	}

	protected Comparator<ParameterMappingSpec> getComparator(int col, boolean ascending) {		
    	return new ParameterColumnComparator(col, ascending);
  	}
}