/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.vcell.optimization.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum OptimizationMethodType implements org.apache.thrift.TEnum {
  EvolutionaryProgram(0),
  SRES(1),
  GeneticAlgorithm(2),
  GeneticAlgorithmSR(3),
  HookeJeeves(4),
  LevenbergMarquardt(5),
  NelderMead(6),
  ParticleSwarm(7),
  RandomSearch(8),
  SimulatedAnnealing(9),
  SteepestDescent(10),
  Praxis(11),
  TruncatedNewton(12);

  private final int value;

  private OptimizationMethodType(int value) {
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
  public static OptimizationMethodType findByValue(int value) { 
    switch (value) {
      case 0:
        return EvolutionaryProgram;
      case 1:
        return SRES;
      case 2:
        return GeneticAlgorithm;
      case 3:
        return GeneticAlgorithmSR;
      case 4:
        return HookeJeeves;
      case 5:
        return LevenbergMarquardt;
      case 6:
        return NelderMead;
      case 7:
        return ParticleSwarm;
      case 8:
        return RandomSearch;
      case 9:
        return SimulatedAnnealing;
      case 10:
        return SteepestDescent;
      case 11:
        return Praxis;
      case 12:
        return TruncatedNewton;
      default:
        return null;
    }
  }
}
