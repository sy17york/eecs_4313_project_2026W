package service;

import dao.AddressDAOImpl;
import dao.PaymentDAOImpl;
import dao.UserDAOImpl;
import model.order.Address;
import model.order.Payment;
import model.user.Customer;
import model.user.User;
/**
 * AuthenticationService handles user registration and login functionalities.
 * It interacts with User, Address, and Payment DAOs to manage user data.
 */
public class AuthenticationService {
	private final UserDAOImpl userDAO;
	private final AddressDAOImpl addressDAO;
	private final PaymentDAOImpl paymentDAO;
	/**
     * Initializes AuthenticationService with DAO implementations.
     *
     * @param dbPath The path to the SQLite database.
     */
	public AuthenticationService(String dbPath) {
		this.userDAO = new UserDAOImpl(dbPath);
		this.addressDAO = new AddressDAOImpl(dbPath);
		this.paymentDAO = new PaymentDAOImpl(dbPath);
	}
	/**
     * Registers a new user with username, email, password, address, and payment details.
     *
     * Steps:
     * 1. Check if the address already exists; if not, insert it.
     * 2. Check if the payment already exists; if not, insert it.
     * 3. Create a new user object with the provided details and IDs for address and payment.
     * 4. Insert the new user into the database.
     *
     * @param username The username for the new user.
     * @param email    The email for the new user.
     * @param password The password for the new user.
     * @param address  The Address object containing address details.
     * @param payment  The Payment object containing payment details.
     * @return true if the registration is successful, false otherwise.
     * @throws Exception If an error occurs during database operations.
     */
	public boolean registerUser(String username, String email, String password, Address address, Payment payment)
			throws Exception {

		Address existingAddress = addressDAO.searchByDetails(address);// Check for existing address; insert if not present
		if (existingAddress == null) {
			addressDAO.insert(address);
		} else {
			address = existingAddress;// Reuse existing address
		}
		Payment existingPayment = paymentDAO.searchByDetails(payment);// Check for existing payment; insert if not present
		if (existingAddress == null) {
			paymentDAO.insert(payment);
		} else {
			payment = existingPayment;// Reuse existing payment
		}
		  // Create a new Customer user and associate the IDs of address and payment
		User newUser = new Customer(null, username, password, email);

		newUser.setAddressid(address.getId());
		newUser.setPaymentid(payment.getId());

		return userDAO.insert(newUser);// Insert the new user into the database
	}
	/**
     * Checks if a user with the given username already exists.
    *
    * @param username The username to check.
    * @return true if the username exists, false otherwise.
    * @throws Exception If an error occurs during the database query.
    */
	public boolean ifExistsUser(String username) throws Exception {
		if (userDAO.getUserByUsername(username) != null) {
			return true;
		}
		return false;
	}
	/**
     * Validates user login by checking the provided username and password.
     *
     * Steps:
     * 1. Retrieve the user by username.
     * 2. Compare the provided password with the stored password.
     * 3. Return the user object if validation is successful; otherwise, return null.
     *
     * @param username The username provided during login.
     * @param password The password provided during login.
     * @return The User object if login is successful, null otherwise.
     * @throws Exception If an error occurs during the database query.
     */
	public User loginUser(String username, String password) throws Exception {
		User user = userDAO.getUserByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;// Return user if credentials match
		}
		return null;// Return null if credentials do not match
	}
}
