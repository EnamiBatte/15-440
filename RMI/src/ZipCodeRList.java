public interface ZipCodeRList extends Remote440// extends YourRemote or whatever
{
    public String find(String city);
    public ZipCodeRList add(String city, String zipcode);
    public ZipCodeRList next();   
}