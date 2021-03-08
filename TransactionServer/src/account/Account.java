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
	
	public int getBalance() {
		return balance;
	}
	
	public int getAccountNum() {
		return accountNum;
	}
	
	public String getName() {
		return name;
	}

	public void setBalance(int incBalance) {
		balance = incBalance;
		
	}

}
