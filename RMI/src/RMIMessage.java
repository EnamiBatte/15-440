import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* An object to store all the information needed to invoke a method
 * And once actually on the server to invoke that method
 * 
 */
public class RMIMessage implements Serializable {
	private Object[] args;
	private String methodName;
	private Object returnValue;
	
	public RMIMessage(Object[] arguments, String method)
	{
		methodName = method;
		args = arguments;
	}
	
	public Object[] getArgs()
	{
		return args;
	}
	public String getMethod()
	{
		return methodName;
	}
	public Object getReturnValue()
	{
		return returnValue;
	}
	
	
	public void setArgs(Object[] arguments)
	{
		args = arguments;
	}
	public void setMethod(String method)
	{
		methodName = method;
	}
	public void setReturnValue(Object value)
	{
		returnValue = value;
	}
	
	//Converts from our information to a method call
	//Stores the method result
	public boolean invoke(Object callee)
	{
		Method m;
		try {
			//Handles Arguments passed in to the method
			if(args != null){
				Class[] argTypes = new Class[args.length];
				int count = 0;
				for(Object arg: args)
				{
					argTypes[count++] = arg.getClass();
				}
				m = callee.getClass().getMethod(methodName,argTypes);
			}
			else {
				m = callee.getClass().getMethod(methodName);
			}
			System.out.println("RMIMessage invoking "+methodName);
			//Call the actual local instance of the method
			returnValue = m.invoke(callee, args);
			return true;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
}
