package acl;

/**
 */
public enum RETCODES {
  /**
   * <p>
   * OK
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  RTN_OK, // 0
  /**
   * <p>
   * Usage instructions were printed
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  RTN_USAGE, // 1
  /**
   * <p>
   * General error
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  RTN_ERR_GENERAL, // 2
  /**
   * <p>
   * Parameter(s) incorrect
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  RTN_ERR_PARAMS, // 3
  /**
   * <p>
   * The object was not set
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  RTN_ERR_OBJECT_NOT_SET, // 4
  /**
   * <p>
   * The call to GetNamedSecurityInfo () failed
   * </p>
   * <p>
   * The value of this constant is 5
   * </p>
   */
  RTN_ERR_GETSECINFO, // 5
  /**
   * <p>
   * The SID for a trustee could not be found
   * </p>
   * <p>
   * The value of this constant is 6
   * </p>
   */
  RTN_ERR_LOOKUP_SID, // 6
  /**
   * <p>
   * Directory permissions specified are invalid
   * </p>
   * <p>
   * The value of this constant is 7
   * </p>
   */
  RTN_ERR_INV_DIR_PERMS, // 7
  /**
   * <p>
   * Printer permissions specified are invalid
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  RTN_ERR_INV_PRN_PERMS, // 8
  /**
   * <p>
   * Registry permissions specified are invalid
   * </p>
   * <p>
   * The value of this constant is 9
   * </p>
   */
  RTN_ERR_INV_REG_PERMS, // 9
  /**
   * <p>
   * Service permissions specified are invalid
   * </p>
   * <p>
   * The value of this constant is 10
   * </p>
   */
  RTN_ERR_INV_SVC_PERMS, // 10
  /**
   * <p>
   * Share permissions specified are invalid
   * </p>
   * <p>
   * The value of this constant is 11
   * </p>
   */
  RTN_ERR_INV_SHR_PERMS, // 11
  /**
   * <p>
   * A privilege could not be enabled
   * </p>
   * <p>
   * The value of this constant is 12
   * </p>
   */
  RTN_ERR_EN_PRIV, // 12
  /**
   * <p>
   * A privilege could not be disabled
   * </p>
   * <p>
   * The value of this constant is 13
   * </p>
   */
  RTN_ERR_DIS_PRIV, // 13
  /**
   * <p>
   * No notification function was given
   * </p>
   * <p>
   * The value of this constant is 14
   * </p>
   */
  RTN_ERR_NO_NOTIFY, // 14
  /**
   * <p>
   * An error occured in the list function
   * </p>
   * <p>
   * The value of this constant is 15
   * </p>
   */
  RTN_ERR_LIST_FAIL, // 15
  /**
   * <p>
   * FindFile reported an error
   * </p>
   * <p>
   * The value of this constant is 16
   * </p>
   */
  RTN_ERR_FINDFILE, // 16
  /**
   * <p>
   * GetSecurityDescriptorControl () failed
   * </p>
   * <p>
   * The value of this constant is 17
   * </p>
   */
  RTN_ERR_GET_SD_CONTROL, // 17
  /**
   * <p>
   * An internal program error occured
   * </p>
   * <p>
   * The value of this constant is 18
   * </p>
   */
  RTN_ERR_INTERNAL, // 18
  /**
   * <p>
   * SetEntriesInAcl () failed
   * </p>
   * <p>
   * The value of this constant is 19
   * </p>
   */
  RTN_ERR_SETENTRIESINACL, // 19
  /**
   * <p>
   * A registry path is incorrect
   * </p>
   * <p>
   * The value of this constant is 20
   * </p>
   */
  RTN_ERR_REG_PATH, // 20
  /**
   * <p>
   * Connect to a remote registry failed
   * </p>
   * <p>
   * The value of this constant is 21
   * </p>
   */
  RTN_ERR_REG_CONNECT, // 21
  /**
   * <p>
   * Opening a registry key failed
   * </p>
   * <p>
   * The value of this constant is 22
   * </p>
   */
  RTN_ERR_REG_OPEN, // 22
  /**
   * <p>
   * Enumeration of registry keys failed
   * </p>
   * <p>
   * The value of this constant is 23
   * </p>
   */
  RTN_ERR_REG_ENUM, // 23
  /**
   * <p>
   * Preparation failed
   * </p>
   * <p>
   * The value of this constant is 24
   * </p>
   */
  RTN_ERR_PREPARE, // 24
  /**
   * <p>
   * The call to SetNamedSecurityInfo () failed
   * </p>
   * <p>
   * The value of this constant is 25
   * </p>
   */
  RTN_ERR_SETSECINFO, // 25
  /**
   * <p>
   * Incorrect list options specified
   * </p>
   * <p>
   * The value of this constant is 26
   * </p>
   */
  RTN_ERR_LIST_OPTIONS, // 26
  /**
   * <p>
   * A SD could not be converted to/from string format
   * </p>
   * <p>
   * The value of this constant is 27
   * </p>
   */
  RTN_ERR_CONVERT_SD, // 27
  /**
   * <p>
   * ACL listing failed
   * </p>
   * <p>
   * The value of this constant is 28
   * </p>
   */
  RTN_ERR_LIST_ACL, // 28
  /**
   * <p>
   * Looping through an ACL failed
   * </p>
   * <p>
   * The value of this constant is 29
   * </p>
   */
  RTN_ERR_LOOP_ACL, // 29
  /**
   * <p>
   * Deleting an ACE failed
   * </p>
   * <p>
   * The value of this constant is 30
   * </p>
   */
  RTN_ERR_DEL_ACE, // 30
  /**
   * <p>
   * Copying an ACL failed
   * </p>
   * <p>
   * The value of this constant is 31
   * </p>
   */
  RTN_ERR_COPY_ACL, // 31
  /**
   * <p>
   * Adding an ACE failed
   * </p>
   * <p>
   * The value of this constant is 32
   * </p>
   */
  RTN_ERR_ADD_ACE, // 32
  /**
   * <p>
   * No backup/restore file was specified
   * </p>
   * <p>
   * The value of this constant is 33
   * </p>
   */
  RTN_ERR_NO_LOGFILE, // 33
  /**
   * <p>
   * The backup/restore file could not be opened
   * </p>
   * <p>
   * The value of this constant is 34
   * </p>
   */
  RTN_ERR_OPEN_LOGFILE, // 34
  /**
   * <p>
   * A read operation from the backup/restore file failed
   * </p>
   * <p>
   * The value of this constant is 35
   * </p>
   */
  RTN_ERR_READ_LOGFILE, // 35
  /**
   * <p>
   * A write operation from the backup/restore file failed
   * </p>
   * <p>
   * The value of this constant is 36
   * </p>
   */
  RTN_ERR_WRITE_LOGFILE, // 36
  /**
   * <p>
   * The operating system is not supported
   * </p>
   * <p>
   * The value of this constant is 37
   * </p>
   */
  RTN_ERR_OS_NOT_SUPPORTED, // 37
  /**
   * <p>
   * The security descriptor is invalid
   * </p>
   * <p>
   * The value of this constant is 38
   * </p>
   */
  RTN_ERR_INVALID_SD, // 38
  /**
   * <p>
   * The call to SetSecurityDescriptorDacl () failed
   * </p>
   * <p>
   * The value of this constant is 39
   * </p>
   */
  RTN_ERR_SET_SD_DACL, // 39
  /**
   * <p>
   * The call to SetSecurityDescriptorSacl () failed
   * </p>
   * <p>
   * The value of this constant is 40
   * </p>
   */
  RTN_ERR_SET_SD_SACL, // 40
  /**
   * <p>
   * The call to SetSecurityDescriptorOwner () failed
   * </p>
   * <p>
   * The value of this constant is 41
   * </p>
   */
  RTN_ERR_SET_SD_OWNER, // 41
  /**
   * <p>
   * The call to SetSecurityDescriptorGroup () failed
   * </p>
   * <p>
   * The value of this constant is 42
   * </p>
   */
  RTN_ERR_SET_SD_GROUP, // 42
  /**
   * <p>
   * The domain specified is invalid
   * </p>
   * <p>
   * The value of this constant is 43
   * </p>
   */
  RTN_ERR_INV_DOMAIN, // 43
  /**
   * <p>
   * An error occured, but it was ignored
   * </p>
   * <p>
   * The value of this constant is 44
   * </p>
   */
  RTN_ERR_IGNORED, // 44
  /**
   * <p>
   * The creation of an SD failed
   * </p>
   * <p>
   * The value of this constant is 45
   * </p>
   */
  RTN_ERR_CREATE_SD, // 45
  /**
   * <p>
   * Memory allocation failed
   * </p>
   * <p>
   * The value of this constant is 46
   * </p>
   */
  RTN_ERR_OUT_OF_MEMORY, // 46
  /**
   * <p>
   * No action specified - nothing to do
   * </p>
   * <p>
   * The value of this constant is 47
   * </p>
   */
  RTN_ERR_NO_ACTN_SPECIFIED, // 47
  /**
   * <p>
   * WMI permissions specified are invalid
   * </p>
   * <p>
   * The value of this constant is 48
   * </p>
   */
  RTN_ERR_INV_WMI_PERMS, // 48
  /**
   * <p>
   * Recursion is not possible
   * </p>
   * <p>
   * The value of this constant is 49
   * </p>
   */
  RTN_WRN_RECURSION_IMPOSSIBLE, // 49
}
