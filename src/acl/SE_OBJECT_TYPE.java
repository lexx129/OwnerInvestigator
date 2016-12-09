package acl;

import com4j.*;

/**
 */
public enum SE_OBJECT_TYPE implements ComEnum {
  /**
   * <p>
   * Files/directories
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  SE_FILE_OBJECT(1),
  /**
   * <p>
   * Services
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  SE_SERVICE(2),
  /**
   * <p>
   * Printers
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  SE_PRINTER(3),
  /**
   * <p>
   * Registry keys
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  SE_REGISTRY_KEY(4),
  /**
   * <p>
   * Network shares
   * </p>
   * <p>
   * The value of this constant is 5
   * </p>
   */
  SE_LMSHARE(5),
  /**
   * <p>
   * WMI Namespace
   * </p>
   * <p>
   * The value of this constant is 11
   * </p>
   */
  SE_WMIGUID_OBJECT(11),
  ;

  private final int value;
  SE_OBJECT_TYPE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
