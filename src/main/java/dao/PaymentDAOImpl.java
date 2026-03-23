package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.order.Payment;
import util.DBConnection;
/**
 * PaymentDAOImpl is a Data Access Object (DAO) implementation for managing
 * Payment entities in the database. It supports standard CRUD operations
 * and additional queries based on payment details.
 */
public class PaymentDAOImpl implements DAO<Payment> {

	private final String dbPath;
	 /**
     * Constructor to initialize the database path.
     *
     * @param dbPath The path to the database file.
     */
	public PaymentDAOImpl(String dbPath) {
		this.dbPath = dbPath;
	}
	 /**
     * Searches for a Payment by its ID.
     *
     * @param id The ID of the Payment.
     * @return The Payment object if found, otherwise null.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public Payment searchById(Long id) throws Exception {
		String query = "SELECT * FROM Payment WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return mapResultSet(resultSet);
			}
		}
		return null;
	}
	 /**
     * Retrieves all Payment records from the database.
     *
     * @return A list of Payment objects.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public List<Payment> getAll() throws Exception {
		List<Payment> payments = new ArrayList<>();
		String query = "SELECT * FROM Payment";
		try (Connection connection = DBConnection.getConnection(dbPath);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				payments.add(mapResultSet(resultSet));
			}
		}
		return payments;
	}
	/**
     * Inserts a new Payment record into the database.
     *
     * @param payment The Payment object to insert.
     * @return true if the insertion is successful, false otherwise.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public boolean insert(Payment payment) throws Exception {
		String query = "INSERT INTO Payment (id, card_number,pin) VALUES (?, ?,?)";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			long newId = getNextId(connection);
			payment.setId(newId);
			statement.setLong(1, newId);
			statement.setInt(2, payment.getCard_number());
			statement.setInt(3, payment.getPin());
			return statement.executeUpdate() > 0;
		}
	}

    /**
     * Removes a Payment record by its ID.
     *
     * @param id The ID of the Payment to remove.
     * @return true if the removal is successful, false otherwise.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public boolean removeById(Long id) throws Exception {
		String query = "DELETE FROM Payment WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			return statement.executeUpdate() > 0;
		}
	}
	  /**
     * Updates an existing Payment record in the database.
     *
     * @param payment The Payment object containing updated details.
     * @return true if the update is successful, false otherwise.
     * @throws Exception If any SQL or database errors occur.
     */
	@Override
	public boolean update(Payment e) throws Exception {
		String query = "UPDATE Payment SET card_number = ?,pin = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, e.getCard_number());
			statement.setInt(2, e.getPin());
			statement.setLong(3, e.getId());
			return statement.executeUpdate() > 0;
		}
	}
	  /**
     * Searches for a Payment by its card number and pin.
     *
     * @param payment The Payment object containing card number and pin.
     * @return The matching Payment object if found, otherwise null.
     * @throws Exception If any SQL or database errors occur.
     */
	public Payment searchByDetails(Payment payment) throws Exception {
		String query = """
				SELECT * FROM Payment
				WHERE card_number = ? AND pin = ?;
				""";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, payment.getCard_number());
			statement.setInt(2, payment.getPin());

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return mapResultSet(resultSet); // Reuse existing mapping logic
			}
		}
		return null;
	}
	 /**
     * Retrieves the next available ID for a new Payment record.
     *
     * @param connection The database connection.
     * @return The next available ID as a long.
     * @throws Exception If any SQL or database errors occur.
     */
	private long getNextId(Connection connection) throws Exception {
		String query = "SELECT MAX(id) AS maxId FROM Payment";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
			if (resultSet.next()) {
				return resultSet.getLong("maxId") + 1;
			}
		}
		return 1;
	}
	 /**
     * Maps a row in the ResultSet to a Payment object.
     *
     * @param resultSet The result set containing payment data.
     * @return A Payment object.
     * @throws Exception If any errors occur during mapping.
     */
	private Payment mapResultSet(ResultSet resultSet) throws Exception {
		Payment payment = new Payment();
		payment.setId(resultSet.getLong("id"));
		payment.setCard_number(resultSet.getInt("card_number"));
		payment.setPin(resultSet.getInt("pin"));
		return payment;
	}
}
