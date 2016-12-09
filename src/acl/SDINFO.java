package acl;

import com4j.*;

/**
 */
public enum SDINFO implements ComEnum {
  /**
   * <p>
   * Process the DACL (permission information)
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  ACL_DACL(1),
  /**
   * <p>
   * Process the SACL (auditing information)
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  ACL_SACL(2),
  /**
   * <p>
   * Owner information
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  SD_OWNER(4),
  /**
   * <p>
   * Primary group information
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  SD_GROUP(8),
  ;

  private final int value;
  SDINFO(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
