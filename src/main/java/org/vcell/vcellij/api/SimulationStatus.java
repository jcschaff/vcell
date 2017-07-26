/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.vcell.vcellij.api;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-07-26")
public class SimulationStatus implements org.apache.thrift.TBase<SimulationStatus, SimulationStatus._Fields>, java.io.Serializable, Cloneable, Comparable<SimulationStatus> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SimulationStatus");

  private static final org.apache.thrift.protocol.TField SIM_STATE_FIELD_DESC = new org.apache.thrift.protocol.TField("simState", org.apache.thrift.protocol.TType.I32, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new SimulationStatusStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new SimulationStatusTupleSchemeFactory();

  /**
   * 
   * @see SimulationState
   */
  public SimulationState simState; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see SimulationState
     */
    SIM_STATE((short)1, "simState");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SIM_STATE
          return SIM_STATE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SIM_STATE, new org.apache.thrift.meta_data.FieldMetaData("simState", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, SimulationState.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SimulationStatus.class, metaDataMap);
  }

  public SimulationStatus() {
  }

  public SimulationStatus(
    SimulationState simState)
  {
    this();
    this.simState = simState;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SimulationStatus(SimulationStatus other) {
    if (other.isSetSimState()) {
      this.simState = other.simState;
    }
  }

  public SimulationStatus deepCopy() {
    return new SimulationStatus(this);
  }

  @Override
  public void clear() {
    this.simState = null;
  }

  /**
   * 
   * @see SimulationState
   */
  public SimulationState getSimState() {
    return this.simState;
  }

  /**
   * 
   * @see SimulationState
   */
  public SimulationStatus setSimState(SimulationState simState) {
    this.simState = simState;
    return this;
  }

  public void unsetSimState() {
    this.simState = null;
  }

  /** Returns true if field simState is set (has been assigned a value) and false otherwise */
  public boolean isSetSimState() {
    return this.simState != null;
  }

  public void setSimStateIsSet(boolean value) {
    if (!value) {
      this.simState = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case SIM_STATE:
      if (value == null) {
        unsetSimState();
      } else {
        setSimState((SimulationState)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case SIM_STATE:
      return getSimState();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case SIM_STATE:
      return isSetSimState();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof SimulationStatus)
      return this.equals((SimulationStatus)that);
    return false;
  }

  public boolean equals(SimulationStatus that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_simState = true && this.isSetSimState();
    boolean that_present_simState = true && that.isSetSimState();
    if (this_present_simState || that_present_simState) {
      if (!(this_present_simState && that_present_simState))
        return false;
      if (!this.simState.equals(that.simState))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetSimState()) ? 131071 : 524287);
    if (isSetSimState())
      hashCode = hashCode * 8191 + simState.getValue();

    return hashCode;
  }

  @Override
  public int compareTo(SimulationStatus other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetSimState()).compareTo(other.isSetSimState());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSimState()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.simState, other.simState);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("SimulationStatus(");
    boolean first = true;

    sb.append("simState:");
    if (this.simState == null) {
      sb.append("null");
    } else {
      sb.append(this.simState);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (simState == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'simState' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class SimulationStatusStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SimulationStatusStandardScheme getScheme() {
      return new SimulationStatusStandardScheme();
    }
  }

  private static class SimulationStatusStandardScheme extends org.apache.thrift.scheme.StandardScheme<SimulationStatus> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SimulationStatus struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SIM_STATE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.simState = org.vcell.vcellij.api.SimulationState.findByValue(iprot.readI32());
              struct.setSimStateIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, SimulationStatus struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.simState != null) {
        oprot.writeFieldBegin(SIM_STATE_FIELD_DESC);
        oprot.writeI32(struct.simState.getValue());
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SimulationStatusTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SimulationStatusTupleScheme getScheme() {
      return new SimulationStatusTupleScheme();
    }
  }

  private static class SimulationStatusTupleScheme extends org.apache.thrift.scheme.TupleScheme<SimulationStatus> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SimulationStatus struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI32(struct.simState.getValue());
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SimulationStatus struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.simState = org.vcell.vcellij.api.SimulationState.findByValue(iprot.readI32());
      struct.setSimStateIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

