package acl;

import com4j.*;

/**
 */
public enum LISTNAMES implements ComEnum {
  /**
   * <p>
   * List names
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  LIST_NAME(1),
  /**
   * <p>
   * List SIDs
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  LIST_SID(2),
  /**
   * <p>
   * List names and SIDs
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  LIST_NAME_SID(3),
  ;

  private final int value;
  LISTNAMES(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
