import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assume.assumeNoException;

import org.junit.Before;
import org.junit.Test;

import com.revature.exception.InvalidLoginException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Bank;

public class BankingTest {

	Bank testBank;
	int userA;
	int userB;
	
	
	@Before
	public void setup() {
		this.testBank = new Bank("src/test/resources/TestVault.bnk");
		this.userA = testBank.register("Hunter1");
		this.userB = testBank.register("myPassy");
		testBank.login(userB,"myPassy");
		testBank.deposit(1000000.0);
		testBank.logout();
		testBank.login(userA,"Hunter1");
		testBank.deposit(500.0);
		testBank.logout();
		System.out.println(testBank);
	}
	
	@Test
	public void checkAccurateDeposit() {
		testBank.login(userB,"myPassy");
		System.out.println(testBank.getBalance());
		double oldBalance = testBank.getBalance();
		testBank.deposit(5.0);
		assertEquals(oldBalance+5.0, testBank.getBalance(),0.1);
		assertNotEquals(oldBalance, testBank.getBalance(), 0.1);
		testBank.logout();
	}
	
	@Test
	public void testFileSaving() {
		System.out.println("testBank: "+testBank);
		Bank copy = new Bank("src/test/resources/TestVault.bnk");
		System.out.println("Copy: "+copy);
		Bank extra = new Bank("src/test/resources/TestVault2.bnk");
		int extraUser = extra.register("dummy");
		System.out.println("Extra: "+extra);
		copy.login(userB,"myPassy");
		testBank.login(userB,"myPassy");
		assertEquals(copy.getBalance(), testBank.getBalance(),0.5);
		testBank.logout();
		testBank.login(userA,"Hunter1");
		extra.login(extraUser,"dummy");
		assertNotEquals(extra.getBalance(), testBank.getBalance(), 0.5);
	}
	
	@Test (expected = InvalidLoginException.class)
	public void testBadPasswords() {
		testBank.login(userA, "Hunter2");
	}
}
