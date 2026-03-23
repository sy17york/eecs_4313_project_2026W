package service;

import java.util.ArrayList;
import java.util.List;

import dao.AddressDAOImpl;
import dao.OrdersDAOImpl;
import dao.PaymentDAOImpl;
import dao.UserDAOImpl;
import model.order.Address;
import model.order.Order;
import model.order.Payment;
import model.user.User;
/**
 * UserService handles user-related operations such as retrieving user details,
 * updating user profiles, managing addresses and payments, and fetching order history.
 * It serves as an abstraction layer that interacts with DAOs for database operations.
 */
public class UserService {
	private final UserDAOImpl userDAO;// DAO for user operations
	private final AddressDAOImpl addressDAO; // DAO for address operations
	private final PaymentDAOImpl paymentDAO;// DAO for payment operations
	private final OrdersDAOImpl ordersDAO;// DAO for order operations
	 /**
     * Initializes UserService with DAO implementations using the specified database path.
     *
     * @param dbPath The path to the SQLite database.
     */
	public UserService(String dbPath) {
		this.userDAO = new UserDAOImpl(dbPath);
		this.addressDAO = new AddressDAOImpl(dbPath);
		this.paymentDAO = new PaymentDAOImpl(dbPath);
		this.ordersDAO = new OrdersDAOImpl(dbPath);
	}

    /**
     * Retrieves user details by user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User object if found.
     * @throws Exception If any database or SQL errors occur.
     */
	public User getUserById(Long userId) throws Exception {
		return userDAO.searchById(userId);
	}
	 /**
     * Updates a user's profile information such as username, email, and password.
     *
     * @param userId   The ID of the user to update.
     * @param name     The new username.
     * @param email    The new email.
     * @param password The new password (consider hashing for security).
     * @throws Exception If the user is not found or input is invalid.
     */
	public void updateUserProfile(Long userId, String name, String email, String password) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		// Fetch the existing user from the database
		User user = userDAO.searchById(userId);
		if (user == null) {
			throw new Exception("User not found");
		}
		// Update user fields if new values are provided
		if (name != null && !name.isEmpty()) {
			user.setUsername(name);
		}
		if (email != null && !email.isEmpty()) {
			user.setEmail(email);
		}
		if (password != null && !password.isEmpty()) {
			user.setPassword(password);
		}
		userDAO.update(user); // Persist the updates to the database
	}
	 /**
     * Retrieves an address by its ID.
     *
     * @param addressid The ID of the address.
     * @return The Address object if found.
     * @throws Exception If any database or SQL errors occur.
     */
	public Address getAddressById(Long addressid) throws Exception {
		return addressDAO.searchById(addressid);
	}
	 /**
     * Retrieves payment details by payment ID.
     *
     * @param paymentid The ID of the payment.
     * @return The Payment object if found.
     * @throws Exception If any database or SQL errors occur.
     */
	public Payment getPaymentById(Long paymentid) throws Exception {
		return paymentDAO.searchById(paymentid);
	}
	/**
     * Updates an existing address in the database.
     *
     * @param address The Address object containing updated details.
     * @throws Exception If any database or SQL errors occur.
     */
	public void updateAddress(Address address) throws Exception {
		addressDAO.update(address);

	}
	/**
     * Updates an existing payment method in the database.
     *
     * @param payment The Payment object containing updated details.
     * @throws Exception If any database or SQL errors occur.
     */
	public void updatePayment(Payment payment) throws Exception {
		paymentDAO.update(payment);

	}
	/**
     * Retrieves the order history for a specific user.
     *
     * @param user_id The ID of the user whose order history is requested.
     * @return A list of Order objects; returns an empty list if no orders are found.
     * @throws Exception If any database or SQL errors occur.
     */
	public List<Order> getMyOrder(Long user_id) throws Exception {
		List<Order> sales = ordersDAO.searchByUserId(user_id);
	    if (sales == null) {
	        sales = new ArrayList<>(); // Ensure it's never null
	    }
	    return sales;
	}
	/**
     * Retrieves the details of a specific order by its ID.
     *
     * @param orderId The ID of the order.
     * @return The Order object containing order details.
     * @throws Exception If any database or SQL errors occur.
     */
	public Order getOrderDetails(Long orderId) throws Exception {
		return ordersDAO.searchById(orderId);
	}

}
