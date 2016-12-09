package acl;

import com4j.*;

/**
 */
public enum PROPAGATION implements ComEnum {
  /**
   * <p>
   * The specific access permissions will only be applied to the container, and will not be inherited by objects created within the container.
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  NO_INHERITANCE(0),
  /**
   * <p>
   * The specific access permissions will only be inherited by objects created within the specific container. The access permissions will not be applied to the container itself.
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  SUB_OBJECTS_ONLY_INHERIT(1),
  /**
   * <p>
   * The specific access permissions will be inherited by containers created within the specific container, will be applied to objects created within the container, but will not be applied to the container itself.
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  SUB_CONTAINERS_ONLY_INHERIT(2),
  /**
   * <p>
   * Combination of SUB_OBJECTS_ONLY_INHERIT and SUB_CONTAINERS_ONLY_INHERIT.
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  SUB_CONTAINERS_AND_OBJECTS_INHERIT(3),
  /**
   * <p>
   * Do not propogate permissions, only the direct descendent gets permissions.
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  INHERIT_NO_PROPAGATE(4),
  /**
   * <p>
   * The specific access permissions will not affect the object they are set on but its childern only (depending on other propagation flags).
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  INHERIT_ONLY(8),
  ;

  private final int value;
  PROPAGATION(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
