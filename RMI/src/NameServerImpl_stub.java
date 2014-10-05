import java.lang.reflect.Method;


public class NameServerImpl_stub implements NameServer {

	RemoteObjectRef ref;
	
	
	@Override
	public void setRemoteObjectRef(RemoteObjectRef r) {
		ref = r;
	}

	@Override
	public RemoteObjectRef getRemoteObjectRef() {
		return this.ref;
	}

	@Override
	public RemoteObjectRef match(String name) {
		RMIMessage temp = new RMIMessage(new Object[] {name},"match");
		temp.invoke(ref);
		return  (RemoteObjectRef) temp.getReturnValue();
	}

	@Override
	public NameServer add(String s, RemoteObjectRef r, NameServer n) {
		RMIMessage temp = new RMIMessage(new Object[] {s,r,n},"add");
		temp.invoke(ref);
		return (NameServer) temp.getReturnValue();
	}

	@Override
	public NameServer next() {
		RMIMessage temp = new RMIMessage(null,"add");
		temp.invoke(ref);
		return (NameServer) temp.getReturnValue();
	}
	
}
