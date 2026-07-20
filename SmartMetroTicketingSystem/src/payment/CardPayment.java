package payment;
import java.util.Scanner;

public class CardPayment implements Payment{
	String cardNumber;
	
	public CardPayment(String cardNumber) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter card number (e.g. 1234-5678-1234-5678, 0 to exit): ");
		
		do {
			this.cardNumber = input.next();
		}while(!validateCardNum(this.cardNumber) || !luhnAlgorithm());
		
		input.close();
		
	}
	
	public boolean pay(double amount) {
		System.out.printf("Processing card payment of RM %.2f.", amount);
		System.out.println("Card number: " + maskCardNumber(cardNumber));
		return true;
		
	}
	
	private String maskCardNumber(String cardNumber) {
			return "****-****-****-" + cardNumber.substring(cardNumber.length()-4);
	}
	
	private boolean validateCardNum(String cardNumber) {
		//check length (with dashes)
		if(cardNumber.length() != 19) {
			System.out.println("Invalid card number. Enter again.\n");
			return false;
		}
		
		//check format
		if(!cardNumber.substring(4,5).equals("-") || 
				!cardNumber.substring(9,10).equals("-") || 
				!cardNumber.substring(14,15).equals("-")) {
			System.out.println("Invalid format. Please follow specified format. (e.g. 1234-5678-1234-5678)\n");
			return false;
		}
		
		//remove dashes
		String number = cardNumber.replaceAll("-","");
		
		//check if all number in card number is digit
		if(!number.matches("\\d+")) {
			System.out.println("Card number should contain only digits. Enter again.\n");
			return false;
		}
		
		else
			return true;
	}
	
	private boolean luhnAlgorithm() {
		//remove dashes
		String number = cardNumber.replaceAll("-","");
			
		//convert string into array of integers
		int[] digits = new int[number.length()];
		for(int i = 0; i<number.length();i++) {
			digits[i] = Character.getNumericValue(number.charAt(i));
		}
		
		//luhn algorithm
		int sum = 0;
		boolean doubleDigit = false; //odd digit is false, even digit is true
		
		for(int i = digits.length - 1; i >= 0; i--) { //start from the right most digit
			int currentDigit = digits[i];
			
			if(doubleDigit) { //is true, means it is second digits
				currentDigit *= 2; //double every second digits
				if(currentDigit > 9) //if doubling results bigger than 9, subtract 9
					currentDigit -= 9;
			}
			
			sum += currentDigit; //sum all digits
			doubleDigit = !doubleDigit; //toggles between true and false
		}
		
		boolean isValid;
		if(sum % 10 == 0) { //sum must be divisible by 10 to be valid card number
			isValid = true;
			System.out.println("Card number is valid.");
		}
		else {
			isValid = false;
			System.out.println("Card number invalid.");
		}

		return isValid;
		
	}
	
}
