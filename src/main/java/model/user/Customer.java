package model.user;
/**
 * Customer represents a type of User who can interact with the system as a customer.
 * It extends the User class to inherit common user attributes and behaviors.
 */
public class Customer extends User {
	 /**
     * Default constructor that initializes an empty Customer object.
     */
	public Customer() {
		super();

	}
	 /**
     * Copy constructor to create a Customer object from an existing User object.
     *
     * @param user The User object to copy from.
     */
	public Customer(User user) {
		super(user);

	}
	  /**
     * Parameterized constructor to initialize a Customer with specific details.
     *
     * @param id       The unique ID of the customer.
     * @param username The username of the customer.
     * @param password The password of the customer.
     * @param email    The email of the customer.
     */
	public Customer(Long id, String username, String password, String email) {
		super(id, username, password, email);

	}

	@Override
	public String getUserType() {
		return "CUSTOMER";
	}

}