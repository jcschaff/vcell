/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package cbit.vcell.client.pyvcellproxy;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)")
public class PostProcessingData implements org.apache.thrift.TBase<PostProcessingData, PostProcessingData._Fields>, java.io.Serializable, Cloneable, Comparable<PostProcessingData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PostProcessingData");

  private static final org.apache.thrift.protocol.TField VARIABLE_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("variableList", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField PLOT_DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("plotData", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new PostProcessingDataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new PostProcessingDataTupleSchemeFactory();

  public java.util.List<VariableInfo> variableList; // required
  public java.util.List<PlotData> plotData; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VARIABLE_LIST((short)1, "variableList"),
    PLOT_DATA((short)2, "plotData");

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
        case 1: // VARIABLE_LIST
          return VARIABLE_LIST;
        case 2: // PLOT_DATA
          return PLOT_DATA;
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
    tmpMap.put(_Fields.VARIABLE_LIST, new org.apache.thrift.meta_data.FieldMetaData("variableList", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.LIST        , "VariableList")));
    tmpMap.put(_Fields.PLOT_DATA, new org.apache.thrift.meta_data.FieldMetaData("plotData", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, PlotData.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PostProcessingData.class, metaDataMap);
  }

  public PostProcessingData() {
  }

  public PostProcessingData(
    java.util.List<VariableInfo> variableList,
    java.util.List<PlotData> plotData)
  {
    this();
    this.variableList = variableList;
    this.plotData = plotData;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PostProcessingData(PostProcessingData other) {
    if (other.isSetVariableList()) {
      java.util.List<VariableInfo> __this__variableList = new java.util.ArrayList<VariableInfo>(other.variableList.size());
      for (VariableInfo other_element : other.variableList) {
        __this__variableList.add(new VariableInfo(other_element));
      }
      this.variableList = __this__variableList;
    }
    if (other.isSetPlotData()) {
      java.util.List<PlotData> __this__plotData = new java.util.ArrayList<PlotData>(other.plotData.size());
      for (PlotData other_element : other.plotData) {
        __this__plotData.add(new PlotData(other_element));
      }
      this.plotData = __this__plotData;
    }
  }

  public PostProcessingData deepCopy() {
    return new PostProcessingData(this);
  }

  @Override
  public void clear() {
    this.variableList = null;
    this.plotData = null;
  }

  public int getVariableListSize() {
    return (this.variableList == null) ? 0 : this.variableList.size();
  }

  public java.util.Iterator<VariableInfo> getVariableListIterator() {
    return (this.variableList == null) ? null : this.variableList.iterator();
  }

  public void addToVariableList(VariableInfo elem) {
    if (this.variableList == null) {
      this.variableList = new java.util.ArrayList<VariableInfo>();
    }
    this.variableList.add(elem);
  }

  public java.util.List<VariableInfo> getVariableList() {
    return this.variableList;
  }

  public PostProcessingData setVariableList(java.util.List<VariableInfo> variableList) {
    this.variableList = variableList;
    return this;
  }

  public void unsetVariableList() {
    this.variableList = null;
  }

  /** Returns true if field variableList is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableList() {
    return this.variableList != null;
  }

  public void setVariableListIsSet(boolean value) {
    if (!value) {
      this.variableList = null;
    }
  }

  public int getPlotDataSize() {
    return (this.plotData == null) ? 0 : this.plotData.size();
  }

  public java.util.Iterator<PlotData> getPlotDataIterator() {
    return (this.plotData == null) ? null : this.plotData.iterator();
  }

  public void addToPlotData(PlotData elem) {
    if (this.plotData == null) {
      this.plotData = new java.util.ArrayList<PlotData>();
    }
    this.plotData.add(elem);
  }

  public java.util.List<PlotData> getPlotData() {
    return this.plotData;
  }

  public PostProcessingData setPlotData(java.util.List<PlotData> plotData) {
    this.plotData = plotData;
    return this;
  }

  public void unsetPlotData() {
    this.plotData = null;
  }

  /** Returns true if field plotData is set (has been assigned a value) and false otherwise */
  public boolean isSetPlotData() {
    return this.plotData != null;
  }

  public void setPlotDataIsSet(boolean value) {
    if (!value) {
      this.plotData = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case VARIABLE_LIST:
      if (value == null) {
        unsetVariableList();
      } else {
        setVariableList((java.util.List<VariableInfo>)value);
      }
      break;

    case PLOT_DATA:
      if (value == null) {
        unsetPlotData();
      } else {
        setPlotData((java.util.List<PlotData>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VARIABLE_LIST:
      return getVariableList();

    case PLOT_DATA:
      return getPlotData();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case VARIABLE_LIST:
      return isSetVariableList();
    case PLOT_DATA:
      return isSetPlotData();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof PostProcessingData)
      return this.equals((PostProcessingData)that);
    return false;
  }

  public boolean equals(PostProcessingData that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_variableList = true && this.isSetVariableList();
    boolean that_present_variableList = true && that.isSetVariableList();
    if (this_present_variableList || that_present_variableList) {
      if (!(this_present_variableList && that_present_variableList))
        return false;
      if (!this.variableList.equals(that.variableList))
        return false;
    }

    boolean this_present_plotData = true && this.isSetPlotData();
    boolean that_present_plotData = true && that.isSetPlotData();
    if (this_present_plotData || that_present_plotData) {
      if (!(this_present_plotData && that_present_plotData))
        return false;
      if (!this.plotData.equals(that.plotData))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetVariableList()) ? 131071 : 524287);
    if (isSetVariableList())
      hashCode = hashCode * 8191 + variableList.hashCode();

    hashCode = hashCode * 8191 + ((isSetPlotData()) ? 131071 : 524287);
    if (isSetPlotData())
      hashCode = hashCode * 8191 + plotData.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(PostProcessingData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetVariableList()).compareTo(other.isSetVariableList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableList, other.variableList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPlotData()).compareTo(other.isSetPlotData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPlotData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.plotData, other.plotData);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("PostProcessingData(");
    boolean first = true;

    sb.append("variableList:");
    if (this.variableList == null) {
      sb.append("null");
    } else {
      sb.append(this.variableList);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("plotData:");
    if (this.plotData == null) {
      sb.append("null");
    } else {
      sb.append(this.plotData);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (variableList == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableList' was not present! Struct: " + toString());
    }
    if (plotData == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'plotData' was not present! Struct: " + toString());
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

  private static class PostProcessingDataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public PostProcessingDataStandardScheme getScheme() {
      return new PostProcessingDataStandardScheme();
    }
  }

  private static class PostProcessingDataStandardScheme extends org.apache.thrift.scheme.StandardScheme<PostProcessingData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PostProcessingData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VARIABLE_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list32 = iprot.readListBegin();
                struct.variableList = new java.util.ArrayList<VariableInfo>(_list32.size);
                VariableInfo _elem33;
                for (int _i34 = 0; _i34 < _list32.size; ++_i34)
                {
                  _elem33 = new VariableInfo();
                  _elem33.read(iprot);
                  struct.variableList.add(_elem33);
                }
                iprot.readListEnd();
              }
              struct.setVariableListIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PLOT_DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list35 = iprot.readListBegin();
                struct.plotData = new java.util.ArrayList<PlotData>(_list35.size);
                PlotData _elem36;
                for (int _i37 = 0; _i37 < _list35.size; ++_i37)
                {
                  _elem36 = new PlotData();
                  _elem36.read(iprot);
                  struct.plotData.add(_elem36);
                }
                iprot.readListEnd();
              }
              struct.setPlotDataIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, PostProcessingData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.variableList != null) {
        oprot.writeFieldBegin(VARIABLE_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.variableList.size()));
          for (VariableInfo _iter38 : struct.variableList)
          {
            _iter38.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.plotData != null) {
        oprot.writeFieldBegin(PLOT_DATA_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.plotData.size()));
          for (PlotData _iter39 : struct.plotData)
          {
            _iter39.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PostProcessingDataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public PostProcessingDataTupleScheme getScheme() {
      return new PostProcessingDataTupleScheme();
    }
  }

  private static class PostProcessingDataTupleScheme extends org.apache.thrift.scheme.TupleScheme<PostProcessingData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PostProcessingData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.variableList.size());
        for (VariableInfo _iter40 : struct.variableList)
        {
          _iter40.write(oprot);
        }
      }
      {
        oprot.writeI32(struct.plotData.size());
        for (PlotData _iter41 : struct.plotData)
        {
          _iter41.write(oprot);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PostProcessingData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list42 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.variableList = new java.util.ArrayList<VariableInfo>(_list42.size);
        VariableInfo _elem43;
        for (int _i44 = 0; _i44 < _list42.size; ++_i44)
        {
          _elem43 = new VariableInfo();
          _elem43.read(iprot);
          struct.variableList.add(_elem43);
        }
      }
      struct.setVariableListIsSet(true);
      {
        org.apache.thrift.protocol.TList _list45 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.plotData = new java.util.ArrayList<PlotData>(_list45.size);
        PlotData _elem46;
        for (int _i47 = 0; _i47 < _list45.size; ++_i47)
        {
          _elem46 = new PlotData();
          _elem46.read(iprot);
          struct.plotData.add(_elem46);
        }
      }
      struct.setPlotDataIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

