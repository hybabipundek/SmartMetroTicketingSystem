package utils;

public class Validation {
	
	public static boolean validateName(String name)    // use static so I can just use Validation.validateName(name) 
	{
		if(name == null || name.trim().isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
