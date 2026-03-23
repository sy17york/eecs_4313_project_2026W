package model.user;
/**
 * Administrator represents a type of User with administrative privileges.
 * It extends the User class to inherit common user attributes and behaviors.
 */
public class Administrator extends User {

	public Administrator() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
     * Parameterized constructor to initialize an Administrator with specific details.
     *
     * @param id       The unique ID of the administrator.
     * @param username The username of the administrator.
     * @param password The password of the administrator.
     * @param email    The email of the administrator.
     */
	public Administrator(Long id, String username, String password, String email) {
		super(id, username, password, email);
		// TODO Auto-generated constructor stub
	}

	public Administrator(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUserType() {
		return "ADMIN";
	}

}
