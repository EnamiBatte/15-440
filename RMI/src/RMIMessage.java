import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
	
	public boolean invoke(Object callee)
	{
		Method m;
		try {
			m = callee.getClass().getMethod(methodName);
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
