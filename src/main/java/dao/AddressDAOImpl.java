package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.order.Address;
import util.DBConnection;
/**
 * AddressDAOImpl is the Data Access Object (DAO) implementation for managing
 * Address entities in the database. It provides CRUD operations for Address.
 */
public class AddressDAOImpl implements DAO<Address> {
	private final String dbPath;
	 /**
     * Constructor to initialize the database path.
     *
     * @param dbPath The path to the database file.
     */
	public AddressDAOImpl(String dbPath) {
		this.dbPath = dbPath;
	}
	/**
     * Searches for an Address by its ID.
     *
     * @param id The ID of the Address to search for.
     * @return Address object if found, otherwise null.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public Address searchById(Long id) throws Exception {
		String query = "SELECT * FROM Address WHERE id = ?";
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
     * Retrieves all Address records from the database.
     *
     * @return List of all Address objects.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public List<Address> getAll() throws Exception {
		List<Address> addresses = new ArrayList<>();
		String query = "SELECT * FROM Address";
		try (Connection connection = DBConnection.getConnection(dbPath);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				addresses.add(mapResultSet(resultSet));
			}
		}
		return addresses;
	}
	 /**
     * Inserts a new Address into the database.
     *
     * @param address The Address object to insert.
     * @return true if the insert was successful, false otherwise.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public boolean insert(Address address) throws Exception {
		String query = "INSERT INTO Address (id, street, province, country, zip, phone) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			long newId = getNextId(connection);
			address.setId(newId);
			statement.setLong(1, newId);
			statement.setString(2, address.getStreet());
			statement.setString(3, address.getProvince());
			statement.setString(4, address.getCountry());
			statement.setString(5, address.getZip());
			statement.setString(6, address.getPhone());
			return statement.executeUpdate() > 0;
		}
	}
	 /**
     * Removes an Address by its ID.
     *
     * @param id The ID of the Address to remove.
     * @return true if the removal was successful, false otherwise.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public boolean removeById(Long id) throws Exception {
		String query = "DELETE FROM Address WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			return statement.executeUpdate() > 0;
		}
	}
	  /**
     * Updates an existing Address record in the database.
     *
     * @param address The Address object containing updated details.
     * @return true if the update was successful, false otherwise.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public boolean update(Address address) throws Exception {
		String query = "UPDATE Address SET street = ?, province = ?, country = ?, zip = ?, phone = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, address.getStreet());
			statement.setString(2, address.getProvince());
			statement.setString(3, address.getCountry());
			statement.setString(4, address.getZip());
			statement.setString(5, address.getPhone());
			statement.setLong(6, address.getId());
			return statement.executeUpdate() > 0;
		}
	}
	 /**
     * Searches for an Address based on its details (street, province, country, zip, and phone).
     *
     * @param address The Address object with the details to search for.
     * @return Address object if found, otherwise null.
     * @throws Exception If any SQL or database connection errors occur.
     */
	public Address searchByDetails(Address address) throws Exception {
		String query = """
				SELECT * FROM Address
				WHERE street = ? AND province = ? AND country = ? AND zip = ? AND phone = ?;
				""";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, address.getStreet());
			statement.setString(2, address.getProvince());
			statement.setString(3, address.getCountry());
			statement.setString(4, address.getZip());
			statement.setString(5, address.getPhone());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return mapResultSet(resultSet); // Reuse existing mapping logic
			}
		}
		return null;
	}
	 /**
     * Retrieves the next available ID for the Address table.
     *
     * @param connection The database connection.
     * @return The next ID as a long.
     * @throws Exception If any SQL errors occur.
     */
	private long getNextId(Connection connection) throws Exception {
		String query = "SELECT MAX(id) AS maxId FROM Address";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
			if (resultSet.next()) {
				return resultSet.getLong("maxId") + 1;
			}
		}
		return 1;
	}
	 /**
     * Maps a ResultSet row to an Address object.
     *
     * @param resultSet The ResultSet containing the data.
     * @return Address object mapped from the ResultSet.
     * @throws Exception If any SQL errors occur.
     */
	private Address mapResultSet(ResultSet resultSet) throws Exception {
		Address address = new Address();
		address.setId(resultSet.getLong("id"));
		address.setStreet(resultSet.getString("street"));
		address.setProvince(resultSet.getString("province"));
		address.setCountry(resultSet.getString("country"));
		address.setZip(resultSet.getString("zip"));
		address.setPhone(resultSet.getString("phone"));
		return address;
	}
}
