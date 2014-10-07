
public class ZipCodeServerImpl_stub implements ZipCodeServer {

	private RemoteObjectRef ror;
	@Override
	public void setRemoteObjectRef(RemoteObjectRef r) {
		this.ror = r;
	}

	@Override
	public RemoteObjectRef getRemoteObjectRef() {
		return ror;
	}

	@Override
	public void initialise(ZipCodeList newlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String find(String city) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ZipCodeList findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printAll() {
		// TODO Auto-generated method stub
		
	}

}
