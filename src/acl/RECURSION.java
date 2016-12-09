package acl;

import com4j.*;

/**
 */
public enum RECURSION implements ComEnum {
  /**
   * <p>
   * Do not recurse
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  RECURSE_NO(1),
  /**
   * <p>
   * Recurse, processing containers only
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  RECURSE_CONT(2),
  /**
   * <p>
   * Recurse, processing objects only
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  RECURSE_OBJ(4),
  /**
   * <p>
   * Recurse, processing containers and objects
   * </p>
   * <p>
   * The value of this constant is 6
   * </p>
   */
  RECURSE_CONT_OBJ(6),
  ;

  private final int value;
  RECURSION(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
