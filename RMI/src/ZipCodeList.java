import java.io.Serializable;

public class ZipCodeList implements Serializable
{
    String city;
    String ZipCode;
    ZipCodeList next;

    public ZipCodeList(String c, String z, ZipCodeList n)
    {
		city=c;
		ZipCode=z;
		next=n;
    }
}