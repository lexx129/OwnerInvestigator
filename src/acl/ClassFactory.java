package acl;

import com4j.*;

/**
 * Defines methods to create COM objects
 */
public abstract class ClassFactory {
  private ClassFactory() {} // instanciation is not allowed


  /**
   * SetACL COM Server
   */
  public static acl.ISetACLCOMServer createSetACLCOMServer() {
    return COM4J.createInstance( acl.ISetACLCOMServer.class, "{13379563-8F21-4579-8AC7-CBCD488735DB}" );
  }
}
