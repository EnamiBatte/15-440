import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Remote440Stub implements Remote440 {
	private RemoteObjectRef ror;
	@Override
	public void setRemoteObjectRef(RemoteObjectRef r) {
		ror = r;
	}

	@Override
	public RemoteObjectRef getRemoteObjectRef() {
		return ror;
	}

	public Object execute(RMIMessage rmi)
	{
		try {
			Socket sock = new Socket(ror.getIP(),ror.getPort());
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(rmi);
			RMIMessage response = (RMIMessage) in.readObject();
			return response.getReturnValue();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
