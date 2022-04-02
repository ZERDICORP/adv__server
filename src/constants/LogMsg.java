package constants;

  
  
public interface LogMsg
{ 
  String SERVER_WENT_DOWN_FOR_SOME_REASON = "server went down for some reason";
	String UNABLE_TO_READ_INCOMING_PACKET = "unable to read incoming packet.. close connection";
	String FAILED_TO_SEND_PACKET = "failed to send packet";
	String COULD_NOT_CLOSE_CONNECTION_CORRECTLY = "could not close the connection correctly";
	String FAILED_TO_HANDLE_PACKET = "failed to handle packet";
}
