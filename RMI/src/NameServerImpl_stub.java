import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;


public class NameServerImpl_stub extends Remote440Stub implements NameServer {

	@Override
	public RemoteObjectRef match(String name) {
		RMIMessage temp = new RMIMessage(new Object[] {name},"match");
		return (RemoteObjectRef) execute(temp);
		
	}

	@Override
	public NameServer add(String s, RemoteObjectRef r, NameServer n) {
		RMIMessage temp = new RMIMessage(new Object[] {s,r,n},"add");
		return (NameServer) execute(temp);
	}

	@Override
	public NameServer next() {
		RMIMessage temp = new RMIMessage(null,"add");
		return (NameServer) execute(temp);
	}
	
}
