package model.user;
/**
 * User is an abstract base class representing a system user.
 * It provides common fields and methods for all types of users (e.g., Customer, Administrator).
 */
public abstract class User {
		private Long id;             // Unique identifier for the user
	    private String username;     // Username for the user
	    private String password;     // Password for the user (ensure proper hashing in practice)
	    private String email;        // Email address of the user
	    private Long addressid;      // ID of the associated address
	    private Long paymentid;      // ID of the associated payment method

	    /**
	     * Default constructor that initializes an empty User object.
	     */
	    public User() {
	        // Default constructor
	    }

	    /**
	     * Parameterized constructor to initialize a User with specific details.
	     *
	     * @param id       The unique identifier of the user.
	     * @param username The username of the user.
	     * @param password The user's password (consider hashing for security).
	     * @param email    The email address of the user.
	     */
		public User(Long id, String username, String password, String email) {
			this.id = id;
			this.username = username;
			this.password = password;
			this.email = email;
		}
		 /**
	     * Copy constructor to create a User object from an existing User instance.
	     *
	     * @param user The User object to copy from.
	     */
		public User(User user) {
			this.id = user.id;
			this.username = user.username;
			this.password = user.password;
			this.email = user.email;
		}

		// Getters and setters for common fields
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public abstract String getUserType();

		public Long getAddressid() {
			return addressid;
		}

		public void setAddressid(Long addressid) {
			this.addressid = addressid;
		}

		public Long getPaymentid() {
			return paymentid;
		}

		public void setPaymentid(Long paymentid) {
			this.paymentid = paymentid;
		}
}