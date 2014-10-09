
public class ZipCodeRListImpl_stub extends Remote440Stub implements ZipCodeRList {

	@Override
	public String find(String city) {
		RMIMessage temp = new RMIMessage(new Object[] {city},"find");
		return (String) execute(temp);
	}

	@Override
	public ZipCodeRList add(String city, String zipcode) {
		RMIMessage temp = new RMIMessage(new Object[] {city,zipcode},"add");
		return (ZipCodeRList) execute(temp);
	}

	@Override
	public ZipCodeRList next() {
		RMIMessage temp = new RMIMessage(null,"next");
		return (ZipCodeRList) execute(temp);
	}

}
