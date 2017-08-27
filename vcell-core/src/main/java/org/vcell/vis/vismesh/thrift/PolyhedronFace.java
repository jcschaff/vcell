/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.vcell.vis.vismesh.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)")
public class PolyhedronFace implements org.apache.thrift.TBase<PolyhedronFace, PolyhedronFace._Fields>, java.io.Serializable, Cloneable, Comparable<PolyhedronFace> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PolyhedronFace");

  private static final org.apache.thrift.protocol.TField VERTICES_FIELD_DESC = new org.apache.thrift.protocol.TField("vertices", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new PolyhedronFaceStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new PolyhedronFaceTupleSchemeFactory();

  public java.util.List<java.lang.Integer> vertices; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VERTICES((short)1, "vertices");

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
        case 1: // VERTICES
          return VERTICES;
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
    tmpMap.put(_Fields.VERTICES, new org.apache.thrift.meta_data.FieldMetaData("vertices", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.LIST        , "IntList")));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PolyhedronFace.class, metaDataMap);
  }

  public PolyhedronFace() {
  }

  public PolyhedronFace(
    java.util.List<java.lang.Integer> vertices)
  {
    this();
    this.vertices = vertices;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PolyhedronFace(PolyhedronFace other) {
    if (other.isSetVertices()) {
      java.util.List<java.lang.Integer> __this__vertices = new java.util.ArrayList<java.lang.Integer>(other.vertices.size());
      for (java.lang.Integer other_element : other.vertices) {
        __this__vertices.add(other_element);
      }
      this.vertices = __this__vertices;
    }
  }

  public PolyhedronFace deepCopy() {
    return new PolyhedronFace(this);
  }

  @Override
  public void clear() {
    this.vertices = null;
  }

  public int getVerticesSize() {
    return (this.vertices == null) ? 0 : this.vertices.size();
  }

  public java.util.Iterator<java.lang.Integer> getVerticesIterator() {
    return (this.vertices == null) ? null : this.vertices.iterator();
  }

  public void addToVertices(int elem) {
    if (this.vertices == null) {
      this.vertices = new java.util.ArrayList<java.lang.Integer>();
    }
    this.vertices.add(elem);
  }

  public java.util.List<java.lang.Integer> getVertices() {
    return this.vertices;
  }

  public PolyhedronFace setVertices(java.util.List<java.lang.Integer> vertices) {
    this.vertices = vertices;
    return this;
  }

  public void unsetVertices() {
    this.vertices = null;
  }

  /** Returns true if field vertices is set (has been assigned a value) and false otherwise */
  public boolean isSetVertices() {
    return this.vertices != null;
  }

  public void setVerticesIsSet(boolean value) {
    if (!value) {
      this.vertices = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case VERTICES:
      if (value == null) {
        unsetVertices();
      } else {
        setVertices((java.util.List<java.lang.Integer>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VERTICES:
      return getVertices();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case VERTICES:
      return isSetVertices();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof PolyhedronFace)
      return this.equals((PolyhedronFace)that);
    return false;
  }

  public boolean equals(PolyhedronFace that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_vertices = true && this.isSetVertices();
    boolean that_present_vertices = true && that.isSetVertices();
    if (this_present_vertices || that_present_vertices) {
      if (!(this_present_vertices && that_present_vertices))
        return false;
      if (!this.vertices.equals(that.vertices))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetVertices()) ? 131071 : 524287);
    if (isSetVertices())
      hashCode = hashCode * 8191 + vertices.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(PolyhedronFace other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetVertices()).compareTo(other.isSetVertices());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVertices()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.vertices, other.vertices);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("PolyhedronFace(");
    boolean first = true;

    sb.append("vertices:");
    if (this.vertices == null) {
      sb.append("null");
    } else {
      sb.append(this.vertices);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (vertices == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'vertices' was not present! Struct: " + toString());
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

  private static class PolyhedronFaceStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public PolyhedronFaceStandardScheme getScheme() {
      return new PolyhedronFaceStandardScheme();
    }
  }

  private static class PolyhedronFaceStandardScheme extends org.apache.thrift.scheme.StandardScheme<PolyhedronFace> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PolyhedronFace struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VERTICES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list8 = iprot.readListBegin();
                struct.vertices = new java.util.ArrayList<java.lang.Integer>(_list8.size);
                int _elem9;
                for (int _i10 = 0; _i10 < _list8.size; ++_i10)
                {
                  _elem9 = iprot.readI32();
                  struct.vertices.add(_elem9);
                }
                iprot.readListEnd();
              }
              struct.setVerticesIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, PolyhedronFace struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.vertices != null) {
        oprot.writeFieldBegin(VERTICES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, struct.vertices.size()));
          for (int _iter11 : struct.vertices)
          {
            oprot.writeI32(_iter11);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PolyhedronFaceTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public PolyhedronFaceTupleScheme getScheme() {
      return new PolyhedronFaceTupleScheme();
    }
  }

  private static class PolyhedronFaceTupleScheme extends org.apache.thrift.scheme.TupleScheme<PolyhedronFace> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PolyhedronFace struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.vertices.size());
        for (int _iter12 : struct.vertices)
        {
          oprot.writeI32(_iter12);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PolyhedronFace struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list13 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, iprot.readI32());
        struct.vertices = new java.util.ArrayList<java.lang.Integer>(_list13.size);
        int _elem14;
        for (int _i15 = 0; _i15 < _list13.size; ++_i15)
        {
          _elem14 = iprot.readI32();
          struct.vertices.add(_elem14);
        }
      }
      struct.setVerticesIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

