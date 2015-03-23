/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.mapping;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vcell.util.Compare;
import org.vcell.util.Issue;
import org.vcell.util.IssueContext;
import org.vcell.util.Matchable;
import org.vcell.util.TokenMangler;

import cbit.vcell.mapping.BioEvent.ParameterType;
import cbit.vcell.mapping.BioEvent.TriggerType;
import cbit.vcell.mapping.ParameterContext.GlobalParameterContext;
import cbit.vcell.mapping.ParameterContext.LocalParameter;
import cbit.vcell.mapping.ParameterContext.ParameterPolicy;
import cbit.vcell.math.MathUtilities;
import cbit.vcell.model.BioNameScope;
import cbit.vcell.model.Model;
import cbit.vcell.model.ModelUnitSystem;
import cbit.vcell.model.Parameter;
import cbit.vcell.parser.DivideByZeroException;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionBindingException;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.parser.NameScope;
import cbit.vcell.parser.ScopedSymbolTable;
import cbit.vcell.parser.SymbolTableEntry;
import cbit.vcell.units.UnitSystemProvider;
import cbit.vcell.units.VCUnitDefinition;
import cbit.vcell.units.VCUnitSystem;

@SuppressWarnings("serial")
public class BioEvent implements Matchable, Serializable, VetoableChangeListener, PropertyChangeListener {
	
	private static final String PROPERTY_NAME_NAME = "name";

	public class BioEventNameScope extends BioNameScope {
		private final NameScope children[] = new NameScope[0]; // always empty
		public BioEventNameScope(){
			super();
		}
		public NameScope[] getChildren() {
			//
			// no children to return
			//
			return children;
		}
		public String getName() {
			return BioEvent.this.getName();
		}
		public NameScope getParent() {
			if (BioEvent.this.simulationContext != null){
				return BioEvent.this.simulationContext.getNameScope();
			}else{
				return null;
			}
		}
		public ScopedSymbolTable getScopedSymbolTable() {
			return BioEvent.this.parameterContext;
		}
		public String getPathDescription() {
			if (simulationContext != null){
				return "/App(\""+simulationContext.getName()+"\")/Event(\""+BioEvent.this.getName()+"\")";
			}
			return null;
		}
		@Override
		public NamescopeType getNamescopeType() {
			return NamescopeType.bioeventType;
		}
	}
	
	public class EventAssignment implements Matchable, Serializable {
		private SymbolTableEntry target = null;
		private Expression assignmentExpression = null;
		public EventAssignment(SymbolTableEntry argTarget, Expression assignment) throws ExpressionBindingException {
			super();
			this.target = argTarget;
			this.assignmentExpression = assignment;
			assignmentExpression.bindExpression(BioEvent.this.parameterContext);
		}

		public boolean compareEqual(Matchable obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof EventAssignment)) {
				return false;
			}
			EventAssignment eventAssignment = (EventAssignment)obj;
			
			if (!Compare.isEqual(target.getName(), eventAssignment.target.getName())) {
				return false;
			}
			if (!Compare.isEqual(assignmentExpression, eventAssignment.assignmentExpression)) {
				return false;
			}

			return true;
		}
		public final SymbolTableEntry getTarget() {
			return target;
		}
		public final Expression getAssignmentExpression() {
			return assignmentExpression;
		}
		public void setAssignmentExpression(Expression expr) throws ExpressionBindingException {
			Expression exp = new Expression(expr);
			exp.bindExpression(BioEvent.this.parameterContext);
			this.assignmentExpression = exp; 
		}
		public void rebind() throws ExpressionBindingException {
			// target = mathDescription.getVariable(target.getName());
			assignmentExpression.bindExpression(BioEvent.this.parameterContext);
		}	
	}

	public static enum ParameterType {
		GeneralTriggerFunction(0,"GeneralTriggerFunction","triggerFunction","fires on each rising edge"),
		Observable(1,"Observable","observable","observable to watch"),
		Threshold(2,"Threshold", "threshold","threshold for observable"),
		SingleTriggerTime(4, "SingleTriggerTime", "triggerTime","time of trigger"),
		RangeMinTime(5,"RangeMinTime", "minTime","time range min"),
		RangeMaxTime(6,"RangeMaxTime", "maxTime","time range max"),
		RangeNumTimes(7, "RangeNumTimes", "numTimes","num times in range"),
		TriggerDelay(8, "TriggerDelay", "delay","trigger delay"),
		TimeListItem(9, "TimeListItem", null,"time list entry"),
		UserDefined(10, "UserDefined", null,"user defined");
		
		private final int role;
		private final String roleXmlName;
		private final String defaultName;
		private final String description;
		
		private ParameterType(int role,String roleXmlName,String defaultName,String description){
			this.role = role;
			this.roleXmlName = roleXmlName;
			this.defaultName = defaultName;
			this.description = description;
		}
		
		public int getRole(){
			return role;
		}
	
		public String getRoleXmlName(){
			return roleXmlName;
		}
	
		public String getDefaultName() {
			return defaultName;
		}
	
		public String getDescription() {
			return description;
		}
		
		public static ParameterType fromRole(int role){
			for (ParameterType type : values()){
				if (type.getRole()==role){
					return type;
				}
			}
			return null;
		}

		public static ParameterType fromRoleXmlName(String roleStr) {
			for (ParameterType type : values()){
				if (type.getRoleXmlName().equals(roleStr)){
					return type;
				}
			}
			return null;
		}
	}

	public static enum TriggerType {
		GeneralTrigger("GeneralTrigger"),
		SingleTriggerTime("SingleTriggerTime"),
		ObservableAboveThreshold("ObservableAboveThreshold"),
		ObservableBelowThreshold("ObservableBelowThreshold"),
		LinearRangeTimes("LinearRangeTimes"),
		LogRangeTimes("LogRangeTimes"),
		ListOfTimes("ListOfTimes");
		
		private final String xmlName;
		private TriggerType(String xmlName){
			this.xmlName = xmlName;
		}
		
		public String getXmlName(){
			return this.xmlName;
		}
		
		public static TriggerType fromXmlName(String xmlName){
			for (TriggerType tt : values()){
				if (tt.xmlName.equals(xmlName)){
					return tt;
				}
			}
			return null;
		}
	}
	
	private class ParameterContextSettings implements Serializable, ParameterPolicy, UnitSystemProvider, GlobalParameterContext {

		@Override /* ParameterPolicy */
		public boolean isUserDefined(LocalParameter localParameter) {
			return (localParameter.getRole() == ParameterType.UserDefined.getRole());
		}

		@Override /* ParameterPolicy */
		public boolean isExpressionEditable(LocalParameter localParameter) {
			return (localParameter.getExpression()!=null);
		}

		@Override /* ParameterPolicy */
		public boolean isNameEditable(LocalParameter localParameter) {
			return true;
		}

		@Override /* ParameterPolicy */
		public boolean isUnitEditable(LocalParameter localParameter) {
			return isUserDefined(localParameter);
		}
		
		@Override /* UnitSystemProvider */
		public VCUnitSystem getUnitSystem() {
			return getSimulationContext().getModel().getUnitSystem();
		}
		
		@Override /* GlobalParameterContext */
		public ScopedSymbolTable getSymbolTable() {
			return getSimulationContext().getModel();
		}
		
		@Override /* GlobalParameterContext */
		public Parameter getParameter(String name) {
			return getSimulationContext().getModel().getModelParameter(name);
		}
		
		@Override /* GlobalParameterContext */
		public Parameter addParameter(String name, Expression exp, VCUnitDefinition unit) throws PropertyVetoException {
			Model model = getSimulationContext().getModel();
			return model.addModelParameter(model.new ModelParameter(name, exp, Model.ROLE_UserDefined, unit));
		}

	};
	
	private final ParameterContextSettings parameterContextSettings = new ParameterContextSettings();
	private TriggerType triggerType;
	private boolean bUseValuesFromTriggerTime;
	private BioEventNameScope nameScope = new BioEventNameScope();
	private final ParameterContext parameterContext;
	private String name;
	private ArrayList<EventAssignment> eventAssignmentList = new ArrayList<EventAssignment>();

	protected SimulationContext simulationContext = null;
	protected transient java.beans.PropertyChangeSupport propertyChange;
	protected transient VetoableChangeSupport vetoPropertyChange;
	
	
	public BioEvent(String name, SimulationContext simContext){
		this(name, TriggerType.SingleTriggerTime, true, simContext);
	}
	
	public BioEvent(String name, TriggerType triggerType, boolean bUseValuesFromTrigger, SimulationContext simContext) {
		this(name, triggerType, bUseValuesFromTrigger, null, simContext);
	}

	private BioEvent(String name, TriggerType triggerType, boolean bUseValuesFromTrigger, ArrayList<EventAssignment> eventAssignmentList, SimulationContext simContext) {
		super();
		this.name = name; 
		this.bUseValuesFromTriggerTime = bUseValuesFromTrigger;
		this.eventAssignmentList = eventAssignmentList;
		simulationContext = simContext;
		this.parameterContext = new ParameterContext(getNameScope(),parameterContextSettings, parameterContextSettings);
		// propagate property change events from parameter context to Kinetic Law listeners.

		setTriggerType(triggerType);
		
		parameterContext.addPropertyChangeListener(this);
		
	}

	public BioEvent(BioEvent argBioEvent, SimulationContext argSimContext) {
		this.simulationContext = argSimContext;
		this.name = argBioEvent.getName();
		this.bUseValuesFromTriggerTime = argBioEvent.bUseValuesFromTriggerTime;
		this.triggerType = argBioEvent.getTriggerType();
		this.parameterContext = new ParameterContext(getNameScope(),parameterContextSettings, parameterContextSettings);
		for (LocalParameter p : argBioEvent.parameterContext.getLocalParameters()){
			try {
				parameterContext.addLocalParameter(p.getName(), new Expression(p.getExpression()), p.getRole(), p.getUnitDefinition(), p.getDescription());
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException("failed to copy parameters for BioEvent "+getName(),e);
			}
		}
		for (int i = 0; i < argBioEvent.getNumEventAssignments(); i++) {
			EventAssignment oldEA = argBioEvent.getEventAssignments().get(i);
			EventAssignment newEA;
			try {
				newEA = new EventAssignment(simulationContext.getEntry(oldEA.getTarget().getName()), new Expression(oldEA.getAssignmentExpression()));
			} catch (ExpressionBindingException e) {
				e.printStackTrace(System.out);
				throw new RuntimeException("Error copying event assignment'" + oldEA.getTarget().getName() + "' in event '" + name + "' : " + e.getMessage());
			}
			this.eventAssignmentList.add(newEA);
		}
	}

	public final TriggerType getTriggerType() {
		return this.triggerType;
	}
	
	public SimulationContext getSimulationContext() {
		return simulationContext;
	}

	public ScopedSymbolTable getScopedSymbolTable(){
		return parameterContext;
	}

	public void setName(String newValue) throws PropertyVetoException {
		String oldValue = name;
		fireVetoableChange(PROPERTY_NAME_NAME, oldValue, newValue);
		this.name = newValue;
		firePropertyChange(PROPERTY_NAME_NAME, oldValue, newValue);
	}

	public ArrayList<EventAssignment> getEventAssignments() {
		return eventAssignmentList;
	}

	public int getNumEventAssignments() {
		return eventAssignmentList.size();
	}
	
	
	public Expression getParameterValue(ParameterType parameterType) {
		if(parameterContext.getLocalParameterFromRole(parameterType.getRole()) == null) {
			return null;
		}
		return parameterContext.getLocalParameterFromRole(parameterType.getRole()).getExpression();
	}

	public LocalParameter getParameter(ParameterType parameterType) {
		return parameterContext.getLocalParameterFromRole(parameterType.getRole());
	}

	public ParameterType getParameterType(LocalParameter parameter) {
		if (!parameterContext.contains(parameter)){
			throw new RuntimeException("parameter "+parameter.getName()+" not found in bioEvent "+getName());
		}
		return ParameterType.fromRole(parameter.getRole());
	}

	public void setParameterValue(ParameterType parameterType, Expression expression) throws ExpressionBindingException, PropertyVetoException {
		parameterContext.getLocalParameterFromRole(parameterType.getRole()).setExpression(expression);
	}
	
	public void gatherIssues(IssueContext issueContext, List<Issue> issueList){
		// look for negative times ... etc.
	}
	
	@Override
	public boolean compareEqual(Matchable obj) {
		if (obj instanceof BioEvent){
			BioEvent other = (BioEvent)obj;
			if (!Compare.isEqual(getName(),other.getName())){
				return false;
			}
			if (getTriggerType() != other.getTriggerType()){
				return false;
			}
			if (!Compare.isEqual(parameterContext,  other.parameterContext)){
				return false;
			}
			if (this.bUseValuesFromTriggerTime != other.bUseValuesFromTriggerTime){
				return false;
			}
			if (!Compare.isEqual(eventAssignmentList, other.eventAssignmentList)) {
				return false;
			}
			return true;
		}
		return false;
	}

	public LocalParameter[] getEventParameters() {
		return parameterContext.getLocalParameters();
	}

	public Parameter[] getProxyParameters() {
		return parameterContext.getProxyParameters();
	}

	public Parameter[] getUnresolvedParameters() {
		return parameterContext.getUnresolvedParameters();
	}

	public void renameParameter(String name, String newName) throws ExpressionException, PropertyVetoException {
		parameterContext.renameLocalParameter(name, newName);
	}

	public void convertParameterType(Parameter param, boolean bConvertToGlobal) throws PropertyVetoException, ExpressionBindingException {
		if ((param instanceof LocalParameter) && ((LocalParameter)param).getRole() != ParameterType.UserDefined.getRole()) {
			throw new RuntimeException("Cannot convert pre-defined local parameter : \'" + param.getName() + "\' to global parameter.");
		}

		parameterContext.convertParameterType(param, bConvertToGlobal, parameterContextSettings);
	}

	public void setParameterValue(LocalParameter parm, Expression exp, boolean autocreateLocalParameter) throws PropertyVetoException, ExpressionException {
		parameterContext.setParameterValue(parm, exp, autocreateLocalParameter);
	}

	public void resolveUndefinedUnits() {
		parameterContext.resolveUndefinedUnits();
	}

	public void refreshDependencies() {
		removePropertyChangeListener(this);
		addPropertyChangeListener(this);

		parameterContext.removePropertyChangeListener(this);
		parameterContext.addPropertyChangeListener(this);
		
		parameterContext.refreshDependencies();
	}

	
	
	public Expression generateTriggerExpression() throws ExpressionException {

		SymbolTableEntry timeSymbolTableEntry = getSimulationContext().getModel().getTIME();
		Expression tExp = new Expression(timeSymbolTableEntry,getNameScope());

		switch (triggerType){
		case GeneralTrigger:{
			return new Expression(getParameter(ParameterType.GeneralTriggerFunction),getNameScope());
		}
		case ListOfTimes:{
			ArrayList<LocalParameter> timeParams = new ArrayList<LocalParameter>();
			for (LocalParameter p : parameterContext.getLocalParameters()){
				if (p.getRole() == ParameterType.TimeListItem.getRole()){
					timeParams.add(p);
				}
			}
			
			if (timeParams.size()==0){
				throw new RuntimeException("no times found for BioEvent "+getName());
			}
			
			//
			// only one time, very simple  (t >= t0)
			//
			if (timeParams.size()==1){
				Expression timeParamExp = new Expression(timeParams.get(0), getNameScope());
				return Expression.relational(">=", tExp, timeParamExp);
			}
			
			//
			// if more than one, flatten time expressions to get list of doubles for event triggers.
			// these numbers are sorted to find the minimum distance between adjacent times.
			// epsilon is 1/2 of this inter-trigger time interval (to insert a falling edge between events)
			//
			ArrayList<Double> timeValues = new ArrayList<Double>();
			for (LocalParameter p : timeParams){
				timeValues.add(MathUtilities.substituteModelParameters(p.getExpression(), getScopedSymbolTable()).flatten().evaluateConstant());
			}
			Collections.sort(timeValues);
			double epsilon = Double.MAX_VALUE;
			for (int i=0; i<timeValues.size()-1; i++){
				double absdiff = Math.abs(timeValues.get(i) - timeValues.get(i+1));
				epsilon = Math.min(epsilon,  absdiff);
			}
			epsilon/= 2.0;
			
			//
			// construct (((t >= t0) && (t <= t0+epsilon)) || ((t >= t1) && (t <= t1+epsilon)) || ((t >= t2) && (t <= t2+epsilon))) 
			//
			ArrayList<Expression> timeClauses = new ArrayList<Expression>();
			for (LocalParameter timeParam : timeParams){
				Expression timeParamExp = new Expression(timeParam, getNameScope());
				Expression timeParamExpPlusEpsilon = Expression.add(timeParamExp, new Expression(epsilon));
				timeClauses.add(
						Expression.and(
							Expression.relational(">=", tExp, timeParamExp), 
							Expression.relational("<=", tExp, timeParamExpPlusEpsilon)));
			}
			if (timeClauses.size()==1){
				return timeClauses.get(0);
			}else{
				// put all individual time clauses together
				return Expression.or(timeClauses.toArray(new Expression[timeClauses.size()]));
			}
		}
		case LinearRangeTimes:
		case LogRangeTimes:{
			//
			// if more than one, flatten time expressions to get list of doubles for event triggers.
			// these numbers are sorted to find the minimum distance between adjacent times.
			// epsilon is 1/2 of this inter-trigger time interval (to insert a falling edge between events)
			//
			LocalParameter firstTimeParam = getParameter(ParameterType.RangeMinTime);
			LocalParameter lastTimeParam = getParameter(ParameterType.RangeMaxTime);
			LocalParameter numTimesParam = getParameter(ParameterType.RangeNumTimes);
			double firstTime = MathUtilities.substituteModelParameters(firstTimeParam.getExpression(), getScopedSymbolTable()).flatten().evaluateConstant();
			double lastTime = MathUtilities.substituteModelParameters(lastTimeParam.getExpression(), getScopedSymbolTable()).flatten().evaluateConstant();
			double numTimes = MathUtilities.substituteModelParameters(numTimesParam.getExpression(), getScopedSymbolTable()).flatten().evaluateConstant();
			Expression first = new Expression(firstTimeParam,getNameScope());
			Expression last = new Expression(lastTimeParam,getNameScope());
			Expression num = new Expression(numTimesParam,getNameScope());
			
			
			ArrayList<Expression> timeExps = new ArrayList<Expression>();
			ArrayList<Double> timeValues = new ArrayList<Double>();

			timeValues.add(firstTime);
			timeExps.add(first);
			if (getTriggerType() == TriggerType.LinearRangeTimes){
				double delta = (lastTime-firstTime)/(numTimes - 1);
				Expression deltaExp = Expression.div(Expression.add(last,Expression.negate(first)), Expression.add(num, new Expression(-1)));
				for (int i=1;i<numTimes;i++){
					timeValues.add(firstTime + delta * i);
					Expression eventTime = Expression.add(first, Expression.mult(deltaExp, new Expression(i)));
					timeExps.add(eventTime);
				}
			}else if (getTriggerType() == TriggerType.LogRangeTimes){
				double ratio = lastTime/firstTime;
				Expression ratioExp = Expression.div(last, first);
				double n_minus_1 = numTimes - 1;
				Expression n_minus_1_exp = Expression.add(num, new Expression(-1));
				for (int i=1;i<numTimes;i++){
					timeValues.add(firstTime * Math.pow(ratio, i/n_minus_1));
					timeExps.add(Expression.mult(first, Expression.power(ratioExp, Expression.div(new Expression(i), n_minus_1_exp))));
				}
			}
			
			double epsilon = Double.MAX_VALUE;
			for (int i=0; i<timeValues.size()-1; i++){
				double absdiff = Math.abs(timeValues.get(i) - timeValues.get(i+1));
				epsilon = Math.min(epsilon,  absdiff);
			}
			epsilon/= 2.0;
			
			//
			// construct (((t >= t0) && (t <= t0+epsilon)) || ((t >= t1) && (t <= t1+epsilon)) || ((t >= t2) && (t <= t2+epsilon))) 
			//
			ArrayList<Expression> timeClauses = new ArrayList<Expression>();
			for (Expression timeExp : timeExps){
				Expression timeExpPlusEpsilon = Expression.add(timeExp, new Expression(epsilon));
				timeClauses.add(
						Expression.and(
							Expression.relational(">=", tExp, timeExp), 
							Expression.relational("<=", tExp, timeExpPlusEpsilon)));
			}
			if (timeClauses.size()==1){
				return timeClauses.get(0);
			}else{
				// put all individual time clauses together
				return Expression.or(timeClauses.toArray(new Expression[timeClauses.size()]));
			}
		}
		case ObservableAboveThreshold:{
			Expression highTrigger = Expression.relational(">=", new Expression(getParameter(ParameterType.Observable),getNameScope()), new Expression(getParameter(ParameterType.Threshold), getNameScope()));
			return highTrigger;
		}
		case ObservableBelowThreshold:{
			Expression lowTrigger = Expression.relational("<=", new Expression(getParameter(ParameterType.Observable),getNameScope()), new Expression(getParameter(ParameterType.Threshold), getNameScope()));
			return lowTrigger;
		}
		case SingleTriggerTime:{
			SymbolTableEntry time = getSimulationContext().getModel().getTIME();
			return Expression.relational(">=", new Expression(time,getNameScope()), new Expression(getParameter(ParameterType.SingleTriggerTime), getNameScope()));
		}
		default:{
			throw new RuntimeException("unexpected triggerType "+getTriggerType());
		}
		}
	}
	
	

	
	/**
	 * The addPropertyChangeListener method was generated to support the propertyChange field.
	 */
	public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().addPropertyChangeListener(listener);
	}

	public void addEventAssignment(EventAssignment eventAssign) throws PropertyVetoException {
		if (eventAssign == null){
			return;
		}	
		try {
			eventAssign.getAssignmentExpression().bindExpression(this.simulationContext);
		} catch (ExpressionBindingException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		ArrayList<EventAssignment> newEventAssignmentList = new ArrayList<EventAssignment>(eventAssignmentList);
		newEventAssignmentList.add(eventAssign);
		setEventAssignmentsList(newEventAssignmentList);
	}   

	/**
	 * The removePropertyChangeListener method was generated to support the propertyChange field.
	 */
	public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().removePropertyChangeListener(listener);
	}

	public void removeEventAssignment(EventAssignment eventAssign) throws PropertyVetoException {
		if (eventAssign == null){
			return;
		}	
		if (eventAssignmentList.contains(eventAssign)){
			ArrayList<EventAssignment> newEventAssignmentList = new ArrayList<EventAssignment>(eventAssignmentList);
			newEventAssignmentList.remove(eventAssign);
			setEventAssignmentsList(newEventAssignmentList);
		}
	}         

	public void setEventAssignmentsList(ArrayList<EventAssignment> eventsAssigns) throws PropertyVetoException {
		ArrayList<EventAssignment> oldValue = eventAssignmentList;
		fireVetoableChange("eventAssignments", oldValue, eventsAssigns);
		eventAssignmentList = eventsAssigns;
		firePropertyChange("eventAssignments", oldValue, eventsAssigns);
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
	}

	public void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
			throws PropertyVetoException {
				getVetoPropertyChange().fireVetoableChange(propertyName, oldValue, newValue);
			}

	protected java.beans.PropertyChangeSupport getPropertyChange() {
		if (propertyChange == null) {
			propertyChange = new java.beans.PropertyChangeSupport(this);
		};
		return propertyChange;
	}

	protected VetoableChangeSupport getVetoPropertyChange() {
		if (vetoPropertyChange == null) {
			vetoPropertyChange = new java.beans.VetoableChangeSupport(this);
		};
		return vetoPropertyChange;
	}

	public void bind() throws ExpressionBindingException {
		parameterContext.refreshDependencies();
		for (EventAssignment eventAssignment : eventAssignmentList) {
			eventAssignment.rebind();
		}
	}
	
	public String getName() {
		return name;
	}

	public BioNameScope getNameScope() {
		return nameScope;
	}
	
	public void vetoableChange(PropertyChangeEvent evt)	throws PropertyVetoException {
		if (evt.getSource() == this && evt.getPropertyName().equals(PROPERTY_NAME_NAME)) {
			String newName = (String) evt.getNewValue();
			if (simulationContext.getBioEvent(newName) != null) {
				throw new PropertyVetoException("An event with name '" + newName + "' already exists!",evt);
			}
			if (simulationContext.getEntry(newName)!=null){
				throw new PropertyVetoException("Cannot use existing symbol '" + newName + "' as an event name",evt);
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
	}

	public boolean getUseValuesFromTriggerTime() {
		return this.bUseValuesFromTriggerTime;
	}

	public void setTriggerType(TriggerType triggerType) {
		if (triggerType == this.triggerType){
			return;
		}
		
		this.triggerType = triggerType;
		
		ModelUnitSystem modelUnitSystem = getSimulationContext().getModel().getUnitSystem();
		VCUnitDefinition unit_TBD = modelUnitSystem.getInstance_TBD();
		VCUnitDefinition unit_Dimensionless = modelUnitSystem.getInstance_DIMENSIONLESS();
		VCUnitDefinition unit_modelTime = modelUnitSystem.getTimeUnit();

		LocalParameter delayParam = parameterContext.new LocalParameter(
				ParameterType.TriggerDelay.getDefaultName(), 
				new Expression(0.0), 
				ParameterType.TriggerDelay.getRole(), 
				unit_modelTime, 
				ParameterType.TriggerDelay.getDescription());
		
		LocalParameter generatedGeneralTriggerParam = parameterContext.new LocalParameter(
				ParameterType.GeneralTriggerFunction.getDefaultName(), 
				null, 
				ParameterType.GeneralTriggerFunction.getRole(), 
				unit_Dimensionless, 
				ParameterType.GeneralTriggerFunction.getDescription());
		
		switch (triggerType){
		case GeneralTrigger: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
					delayParam,
					parameterContext.new LocalParameter(
							ParameterType.GeneralTriggerFunction.getDefaultName(), 
							new Expression(0.0), 
							ParameterType.GeneralTriggerFunction.getRole(), 
							unit_Dimensionless, 
							ParameterType.GeneralTriggerFunction.getDescription()),
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		case ObservableAboveThreshold:
		case ObservableBelowThreshold: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
						delayParam,
						generatedGeneralTriggerParam,
						parameterContext.new LocalParameter(
								ParameterType.Observable.getDefaultName(), 
								null, 
								ParameterType.Observable.getRole(), 
								unit_TBD, 
								ParameterType.Observable.getDescription()), 
						parameterContext.new LocalParameter(
								ParameterType.Threshold.getDefaultName(), 
								new Expression(1.0), 
								ParameterType.Threshold.getRole(), 
								unit_TBD, 
								ParameterType.Threshold.getDescription()), 
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		case SingleTriggerTime: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
						delayParam,
						generatedGeneralTriggerParam,
						parameterContext.new LocalParameter(
								ParameterType.SingleTriggerTime.getDefaultName(), 
								new Expression(1.0), 
								ParameterType.SingleTriggerTime.getRole(), 
								unit_modelTime, 
								ParameterType.SingleTriggerTime.getDescription()), 
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		case LinearRangeTimes:
		case LogRangeTimes: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
						delayParam,
						generatedGeneralTriggerParam,
						parameterContext.new LocalParameter(
								ParameterType.RangeMinTime.getDefaultName(), 
								new Expression(1.0), 
								ParameterType.RangeMinTime.getRole(), 
								unit_modelTime, 
								ParameterType.RangeMinTime.getDescription()), 
						parameterContext.new LocalParameter(
								ParameterType.RangeMaxTime.getDefaultName(), 
								new Expression(10.0), 
								ParameterType.RangeMaxTime.getRole(), 
								unit_modelTime, 
								ParameterType.RangeMaxTime.getDescription()), 
						parameterContext.new LocalParameter(
								ParameterType.RangeNumTimes.getDefaultName(), 
								new Expression(9), 
								ParameterType.RangeNumTimes.getRole(), 
								unit_modelTime, 
								ParameterType.RangeNumTimes.getDescription()), 
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		case ListOfTimes: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
						delayParam,
						generatedGeneralTriggerParam,
						parameterContext.new LocalParameter(
								ParameterType.TimeListItem.getDefaultName()+"0", 
								new Expression(1.0), 
								ParameterType.TimeListItem.getRole(), 
								unit_modelTime, 
								ParameterType.TimeListItem.getDescription()), 
						parameterContext.new LocalParameter(
								ParameterType.TimeListItem.getDefaultName()+"1", 
								new Expression(2.0), 
								ParameterType.TimeListItem.getRole(), 
								unit_modelTime, 
								ParameterType.TimeListItem.getDescription()), 
						parameterContext.new LocalParameter(
								ParameterType.TimeListItem.getDefaultName()+"2", 
								new Expression(3.0), 
								ParameterType.TimeListItem.getRole(), 
								unit_modelTime, 
								ParameterType.TimeListItem.getDescription()), 
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		default:{
			throw new RuntimeException("unsupported rule-based kinetic law "+triggerType);
		}
		}
	}

	public void setTimeList(Expression[] listExps) throws ExpressionBindingException, PropertyVetoException {
		if (triggerType != TriggerType.ListOfTimes){
			throw new RuntimeException("list of times only available for "+TriggerType.ListOfTimes.name()+" trigger type");
		}
		//
		// remove any existing TimeListItem parameters
		//
		ArrayList<LocalParameter> parameters = new ArrayList<LocalParameter>();
		for (LocalParameter p : getEventParameters()){
			if (p.getRole() != ParameterType.TimeListItem.getRole()){
				parameters.add(p);
			}
		}
		//
		// generate new TimeListItem parameters from function arguments
		//
		VCUnitDefinition timeUnit = getSimulationContext().getModel().getUnitSystem().getTimeUnit();
		String name = "t0";
		for (Expression exp : listExps){
			String description = ParameterType.TimeListItem.description;
			int role = ParameterType.TimeListItem.getRole();
			parameters.add(parameterContext.new LocalParameter(name, exp, role, timeUnit, description));
			name = TokenMangler.getNextEnumeratedToken(name);
		}
		setParameters(parameters.toArray(new LocalParameter[0]));
	}

	public void setUseValuesFromTriggerTime(boolean bUseValuesFromTriggerTime) {
		this.bUseValuesFromTriggerTime = bUseValuesFromTriggerTime;
	}

	public LocalParameter createNewParameter(String paramName, ParameterType parameterType, Expression exp, VCUnitDefinition unit) {
		return parameterContext.new LocalParameter(paramName, exp, parameterType.role, unit, parameterType.description);
	}

	public void setParameters(LocalParameter[] parameters) throws PropertyVetoException, ExpressionBindingException {
		parameterContext.setLocalParameters(parameters);
	}

	public String getTriggerDescription() {
		switch (triggerType){
		case GeneralTrigger:{
			return "on condition: "+getParameter(ParameterType.GeneralTriggerFunction).getExpression().infix();
		}
		case LinearRangeTimes:
		case LogRangeTimes:{
			Expression numTimesExp = getParameter(ParameterType.RangeNumTimes).getExpression();
			String numTimes = numTimesExp.infix();
			if (numTimesExp.isNumeric()){
				double numTimesDouble;
				try {
					numTimesDouble = numTimesExp.evaluateConstant();
					if (numTimesDouble == Math.floor(numTimesDouble)){
						numTimes = Integer.toString((int)Math.floor(numTimesDouble));
					}
				} catch (ExpressionException e) {
					e.printStackTrace();
				}
			}
			String minTime = getParameter(ParameterType.RangeMinTime).getExpression().infix();
			String maxTime = getParameter(ParameterType.RangeMaxTime).getExpression().infix();
			String timeUnit = getParameter(ParameterType.RangeMinTime).getUnitDefinition().getSymbol();
			if (triggerType == TriggerType.LinearRangeTimes){
				return "at "+numTimes+" times from "+minTime+" to "+maxTime+" "+timeUnit+" (linear scale)";
			}else{
				return "at "+numTimes+" times from "+minTime+" to "+maxTime+" "+timeUnit+" (log scale)";
			}
		}
		case ListOfTimes:{
			StringBuffer sb = new StringBuffer();
			for (LocalParameter p : getEventParameters()){
				if (p.getRole() == ParameterType.TimeListItem.getRole()){
					if (sb.length()==0){
						sb.append(p.getExpression().infix());
					}else{
						sb.append(", "+p.getExpression().infix());
					}
				}
			}
			return "at times: ["+sb.toString()+"]";
		}
		case ObservableAboveThreshold:{
			return "when "+getParameter(ParameterType.Observable).getExpression().infix()+" is above "+getParameter(ParameterType.Threshold).getExpression().infix();
		}
		case ObservableBelowThreshold:{
			return "when "+getParameter(ParameterType.Observable).getExpression().infix()+" is below "+getParameter(ParameterType.Threshold).getExpression().infix();
		}
		case SingleTriggerTime:{
			LocalParameter triggerTimeParam = getParameter(ParameterType.SingleTriggerTime);
			return "at time of "+triggerTimeParam.getExpression().infix()+" "+triggerTimeParam.getUnitDefinition().getSymbol();
		}
		default:{
			return "not yet implemented";
		}
		}
	}

}
