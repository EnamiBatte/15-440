
public class ZipCodeServerImpl_stub extends Remote440Stub implements ZipCodeServer {

	@Override
	public void initialise(ZipCodeList newlist) {
		RMIMessage temp = new RMIMessage(new Object[] {newlist},"initialise");
		execute(temp);
	}

	@Override
	public String find(String city) {
		RMIMessage temp = new RMIMessage(new Object[] {city},"find");
		return (String) execute(temp);
	}

	@Override
	public ZipCodeList findAll() {
		RMIMessage temp = new RMIMessage(null,"findAll");
		return (ZipCodeList) execute(temp);
	}

	@Override
	public void printAll() {
		RMIMessage temp = new RMIMessage(null,"printAll");
		execute(temp);
	}

}
