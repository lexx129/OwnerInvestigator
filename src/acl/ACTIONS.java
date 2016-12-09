package acl;

import com4j.*;

/**
 */
public enum ACTIONS implements ComEnum {
  /**
   * <p>
   * Add an ACE
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  ACTN_ADDACE(1),
  /**
   * <p>
   * List the entries in the security descriptor
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  ACTN_LIST(2),
  /**
   * <p>
   * Set the owner
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  ACTN_SETOWNER(4),
  /**
   * <p>
   * Set the primary group
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  ACTN_SETGROUP(8),
  /**
   * <p>
   * Clear the DACL of any non-inherited ACEs
   * </p>
   * <p>
   * The value of this constant is 16
   * </p>
   */
  ACTN_CLEARDACL(16),
  /**
   * <p>
   * Clear the SACL of any non-inherited ACEs
   * </p>
   * <p>
   * The value of this constant is 32
   * </p>
   */
  ACTN_CLEARSACL(32),
  /**
   * <p>
   * Set the flag 'allow inheritable permissions from the parent object to propagate to this object'
   * </p>
   * <p>
   * The value of this constant is 64
   * </p>
   */
  ACTN_SETINHFROMPAR(64),
  /**
   * <p>
   * Reset permissions on all sub-objects and enable propagation of inherited permissions
   * </p>
   * <p>
   * The value of this constant is 128
   * </p>
   */
  ACTN_RESETCHILDPERMS(128),
  /**
   * <p>
   * Replace one trustee by another in all ACEs
   * </p>
   * <p>
   * The value of this constant is 256
   * </p>
   */
  ACTN_REPLACETRUSTEE(256),
  /**
   * <p>
   * Remove all ACEs belonging to a certain trustee
   * </p>
   * <p>
   * The value of this constant is 512
   * </p>
   */
  ACTN_REMOVETRUSTEE(512),
  /**
   * <p>
   * Copy the permissions for one trustee to another
   * </p>
   * <p>
   * The value of this constant is 1024
   * </p>
   */
  ACTN_COPYTRUSTEE(1024),
  /**
   * <p>
   * Replace one domain by another in all ACEs
   * </p>
   * <p>
   * The value of this constant is 256
   * </p>
   */
  ACTN_REPLACEDOMAIN(256),
  /**
   * <p>
   * Remove all ACEs belonging to a certain domain
   * </p>
   * <p>
   * The value of this constant is 512
   * </p>
   */
  ACTN_REMOVEDOMAIN(512),
  /**
   * <p>
   * Copy the permissions for one domain to another
   * </p>
   * <p>
   * The value of this constant is 1024
   * </p>
   */
  ACTN_COPYDOMAIN(1024),
  /**
   * <p>
   * Restore entire security descriptors backup up with the list function
   * </p>
   * <p>
   * The value of this constant is 2048
   * </p>
   */
  ACTN_RESTORE(2048),
  /**
   * <p>
   * Process all trustee actions
   * </p>
   * <p>
   * The value of this constant is 4096
   * </p>
   */
  ACTN_TRUSTEE(4096),
  /**
   * <p>
   * Process all domain actions
   * </p>
   * <p>
   * The value of this constant is 8192
   * </p>
   */
  ACTN_DOMAIN(8192),
  /**
   * <p>
   * Delete orphaned SIDs
   * </p>
   * <p>
   * The value of this constant is 16384
   * </p>
   */
  ACTN_DELORPHANEDSIDS(16384),
  ;

  private final int value;
  ACTIONS(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
