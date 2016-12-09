package acl;

import com4j.*;

/**
 */
public enum INHERITANCE implements ComEnum {
  /**
   * <p>
   * Do not change settings
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  INHPARNOCHANGE(0),
  /**
   * <p>
   * Inherit from parent
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  INHPARYES(1),
  /**
   * <p>
   * Do not inherit, copy inheritable permissions
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  INHPARCOPY(2),
  /**
   * <p>
   * Do not inherit, do not copy inheritable permissions
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  INHPARNOCOPY(4),
  ;

  private final int value;
  INHERITANCE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
