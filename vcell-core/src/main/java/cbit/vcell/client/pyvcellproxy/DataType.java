/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package cbit.vcell.client.pyvcellproxy;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum DataType implements org.apache.thrift.TEnum {
  CELLDATA(0),
  POINTDATA(1);

  private final int value;

  private DataType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static DataType findByValue(int value) { 
    switch (value) {
      case 0:
        return CELLDATA;
      case 1:
        return POINTDATA;
      default:
        return null;
    }
  }
}
