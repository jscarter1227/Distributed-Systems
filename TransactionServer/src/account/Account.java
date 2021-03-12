package account;

public class Account {
	String name;
	int accountNum;
	int balance;
	
	public Account(String incName, int incAccountNumber, int incInitialBalance) {
		name = incName;
		accountNum = incAccountNumber;
		balance = incInitialBalance;
		
	}
	
	// Returns balance of account
	public int getBalance() {
		return balance;
	}
	
	// Gets the number of the ccount
	public int getAccountNum() {
		return accountNum;
	}
	
	// Removes balance with amount specified
	public void withdraw(int amount) {
		balance -= amount;
	}
	
	// Adds to balance with amount specified
	public void deposit(int amount) {
		balance += amount;
	}
	
	// Gets the name of account
	public String getName() {
		return name;
	}

	// Sets the balance of the account
	public void setBalance(int incBalance) {
		balance = incBalance;
		
	}

}
