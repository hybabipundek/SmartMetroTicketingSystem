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
		else if(!name.matches("[a-zA-Z ]+"))
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
		if(password == null || password.trim().isEmpty())
		{
			return false;
		}
		else if(password.length() < 6)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateNumber(String input)  //double change to String
	{
		if(input == null || input.trim().isEmpty())
		{
			return false;
		}
		
		try
		{
			//if cannot convert to double, means the input is not a number
			//So NumberFormatException will occur.
			double number = Double.parseDouble(input);  //Convert String into double
			
			if(number < 0)
			{
				return false;
			}
			else
			{
				return true;
			}	
		}
		catch(NumberFormatException e)  
		// If the input cannot be converted to double, NumberFormatException occurs.
		// catch handles the exception and returns false.
		{
			return false;
		}
	}
	
	public static boolean validatePassengerID(String id)
	{
		if(id == null || id.trim().isEmpty())
		{
			return false;
		}
		else if(!id.matches("P\\d{3}"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateAdminID(String id)
	{
		if(id == null || id.trim().isEmpty())
		{
			return false;
		}
		else if(!id.matches("A\\d{3}"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateStationID(String id)
	{
		if(id == null || id.trim().isEmpty())
		{
			return false;
		}
		else if(!id.matches("S\\d{3}"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateTrainID(String id)
	{
		if(id == null || id.trim().isEmpty())
		{
			return false;
		}
		else if(!id.matches("T\\d{3}"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateRouteID(String id)
	{
		if(id == null || id.trim().isEmpty())
		{
			return false;
		}
		else if(!id.matches("R\\d{3}"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateTicketID(String id)
	{
		if(id == null || id.trim().isEmpty())
		{
			return false;
		}
		else if(!id.matches("T\\d{3}"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validateCapacity(String capacityXX)
	{
		if(capacityXX == null || capacityXX.trim().isEmpty())
		{
			return false;
		}
		try
		{
			int number = Integer.parseInt(capacityXX);
			
			if(number < 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	public static boolean validateChoice(String input, int min , int max)
	{
		if(input == null || input.trim().isEmpty())
		{
			return false;
		}
		try
		{
			int choice = Integer.parseInt(input);
			
			if(choice < min || choice > max)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
}
