package utils;

public class Validation {
	
	public static boolean validateName(String name)    // use static so I can just use Validation.validateName(name) 
	{
		if(name == null || name.trim().isEmpty())   //only need one condition is true, then the whole statement is true
		{
			return false;
		}
		else if(name.trim().length() < 3)
		{
			return false;
		}
		else if(!name.matches("[a-zA-Z]+"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateEmail(String email)
	{
		if(email == null || email.trim().isEmpty())  // "||" will check the first condition first, then if it's not fit the first condition, it will go check the second condition. 
		{
			return false;
		}
		else if(!email.contains("@gmail.com"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validatePassword(String password)
	{
		if(password == null || )
	}
}
