package acl.events;

import com4j.*;

/**
 * SetACL Event Interface
 */
@IID("{35F76182-7F52-4D6A-BD6E-1317345F98FB}")
public abstract class _ISetACLCOMServerEvents {
  // Methods:
  /**
   * <p>
   * Receives string messages that would appear on the screen in the command line version
   * </p>
   * @param message Mandatory java.lang.String parameter.
   */

  @DISPID(1)
  public void messageEvent(
    String message) {
        throw new UnsupportedOperationException();
//      System.out.println(message);
  }


  // Properties:
}
