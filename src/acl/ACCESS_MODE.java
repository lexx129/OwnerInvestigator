package acl;

import com4j.*;

/**
 */
public enum ACCESS_MODE implements ComEnum {
  /**
   * <p>
   * Adds an access allowed ACE for the specified user/group.
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  GRANT_ACCESS(1),
  /**
   * <p>
   * Replaces all existing access allowed ACEs for the specified user/group with a new ACE.
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  SET_ACCESS(2),
  /**
   * <p>
   * Adds an access denied ACE for the specified user/group.
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  DENY_ACCESS(3),
  /**
   * <p>
   * All existing access allowed ACEs or system audit ACEs for the specified user/group are removed.
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  REVOKE_ACCESS(4),
  /**
   * <p>
   * Adds an audit success ACE for the specified user/group.
   * </p>
   * <p>
   * The value of this constant is 5
   * </p>
   */
  SET_AUDIT_SUCCESS(5),
  /**
   * <p>
   * Adds an audit failure ACE for the specified user/group.
   * </p>
   * <p>
   * The value of this constant is 6
   * </p>
   */
  SET_AUDIT_FAILURE(6),
  ;

  private final int value;
  ACCESS_MODE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
