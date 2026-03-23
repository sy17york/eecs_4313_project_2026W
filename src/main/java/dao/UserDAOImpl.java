package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.user.Administrator;
import model.user.Customer;
import model.user.User;
import util.DBConnection;
/**
 * UserDAOImpl is a Data Access Object (DAO) implementation for managing
 * User entities in the database. It provides CRUD operations and additional
 * methods for retrieving user-specific data such as address and payment details.
 */
public class UserDAOImpl implements DAO<User> {
	private final String dbPath;
	 /**
     * Constructor to initialize the database path.
     *
     * @param dbPath The path to the database file.
     */
	public UserDAOImpl(String dbPath) {
		this.dbPath = dbPath;
	}
	 /**
     * Searches for a User by its ID.
     *
     * @param id The ID of the user.
     * @return The User object if found, otherwise null.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public User searchById(Long id) throws Exception {
		String query = "SELECT * FROM User WHERE user_id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return mapUser(resultSet);
			}
		}
		return null;
	}
	  /**
     * Retrieves all User records from the database.
     *
     * @return A list of all User objects.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public List<User> getAll() throws Exception {
		List<User> users = new ArrayList<>();
		String query = "SELECT * FROM User";
		try (Connection connection = DBConnection.getConnection(dbPath);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				users.add(mapUser(resultSet));
			}
		}
		return users;
	}
	  /**
     * Inserts a new User record into the database.
     *
     * @param user The User object to insert.
     * @return true if the insertion was successful, false otherwise.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public boolean insert(User user) throws Exception {


		String query = "INSERT INTO User (user_id, username, password, email, user_type, address_id,payment_id) VALUES (?, ?, ?, ?, ?,?,?)";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			long newId = getNextId(connection);
			user.setId(newId);
			statement.setLong(1, newId);
			statement.setString(2, user.getUsername());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getUserType());
			statement.setLong(6, user.getAddressid());
			statement.setLong(7, user.getPaymentid());
			return statement.executeUpdate() > 0;
		}
	}

    /**
     * Removes a User record by its ID.
     *
     * @param id The ID of the User to remove.
     * @return true if the removal was successful, false otherwise.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public boolean removeById(Long id) throws Exception {
		String query = "DELETE FROM User WHERE user_id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			return statement.executeUpdate() > 0;
		}
	}
	 /**
     * Updates an existing User record in the database.
     *
     * @param user The User object containing updated data.
     * @return true if the update was successful, false otherwise.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public boolean update(User user) throws Exception {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}

		String query = "UPDATE User SET username = ?, password = ?, email = ?, user_type = ?,address_id = ?,payment_id = ? WHERE user_id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getEmail());
			statement.setString(4, user.getUserType());
			statement.setLong(5, user.getAddressid());
			statement.setLong(6, user.getPaymentid());
			statement.setLong(7, user.getId());
			return statement.executeUpdate() > 0;
		}
	}
	 /**
     * Retrieves a User by its username (case-insensitive).
     *
     * @param username The username to search for.
     * @return The User object if found, otherwise null.
     * @throws Exception If any SQL or database errors occur.
     */
	public User getUserByUsername(String username) throws Exception {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("Username cannot be null or empty");
		}

		String query = "SELECT * FROM User WHERE LOWER(username) = LOWER(?)";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, username);// Bind the user ID to the query parameter
			ResultSet resultSet = statement.executeQuery();
			// Check if a result exists and return the address ID
			if (resultSet.next()) {
				return mapUser(resultSet);
			}
		}
		return null;// Return null if no address is found
	}
	/**
	 * Retrieves the address ID associated with a specific user.
	 *
	 * @param userid The ID of the user whose address ID is to be retrieved.
	 * @return The address ID if found, otherwise null.
	 * @throws Exception If any database or SQL errors occur.
	 */
	public Long searchAddressId(Long userid) throws Exception {
		String query = "SELECT address_id FROM User WHERE user_id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, userid);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong("address_id");
			}
		}
		return null;
	}
	/**
	 * Retrieves the payment ID associated with a specific user.
	 *
	 * @param userid The ID of the user whose payment ID is to be retrieved.
	 * @return The payment ID if found, otherwise null.
	 * @throws Exception If any database or SQL errors occur.
	 */
	public Long searchPaymentId(Long userid) throws Exception {
		String query = "SELECT payment_id FROM User WHERE user_id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, userid);// Bind the user ID to the query parameter
			ResultSet resultSet = statement.executeQuery();
			// Check if a result exists and return the payment ID
			if (resultSet.next()) {
				return resultSet.getLong("payment_id");
			}
		}
		return null; // Return null if no payment ID is found
	}
	  /**
     * Checks if a user is an administrator.
     *
     * @param user The User object to check.
     * @return true if the user is an administrator, otherwise false.
     * @throws Exception If any SQL or database errors occur.
     */
	public Boolean isAdmins(User user) throws Exception {
		if (user == null || user.getId() == null) {
			return false;
		}

		String query = "SELECT user_type FROM User WHERE user_id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, user.getId());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return "ADMIN".equalsIgnoreCase(resultSet.getString("user_type"));
			}
		}
		return false;
	}
	 /**
     * Retrieves the next available user ID.
     *
     * @param connection The active database connection.
     * @return The next user ID as a long.
     * @throws Exception If any SQL errors occur.
     */
	private long getNextId(Connection connection) throws Exception {
		String query = "SELECT MAX(user_id) AS maxId FROM User";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
			if (resultSet.next()) {
				return resultSet.getLong("maxId") + 1;
			}
		}
		return 1;
	}
	/**
     * Maps a ResultSet row to a User object, distinguishing between Customer and Administrator.
     *
     * @param resultSet The result set containing user data.
     * @return The corresponding User object.
     * @throws Exception If any errors occur during mapping.
     */
	private User mapUser(ResultSet resultSet) throws Exception {
		String userType = resultSet.getString("user_type");
		User user;

		if ("ADMIN".equalsIgnoreCase(userType)) {
			user = new Administrator();
		} else {
			user = new Customer();
		}

		user.setId(resultSet.getLong("user_id"));
		user.setUsername(resultSet.getString("username"));
		user.setPassword(resultSet.getString("password"));
		user.setEmail(resultSet.getString("email"));
		user.setAddressid(resultSet.getLong("address_id"));
		user.setPaymentid(resultSet.getLong("payment_id"));
		return user;
	}
}
