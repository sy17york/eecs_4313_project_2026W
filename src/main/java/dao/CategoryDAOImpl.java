
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.product.Category;
import util.DBConnection;
/**
 * CategoryDAOImpl is the Data Access Object (DAO) implementation for managing
 * Category entities in the database. It provides CRUD operations for Category.
 */
public class CategoryDAOImpl implements DAO<Category> {
	private final String dbPath;
	/**
     * Constructor to initialize the database path.
     *
     * @param dbPath The path to the database file.
     */
	public CategoryDAOImpl(String dbPath) {
		this.dbPath = dbPath;
	}
	 /**
     * Searches for a Category by its ID.
     *
     * @param id The ID of the Category to search for.
     * @return Category object if found, otherwise null.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public Category searchById(Long id) throws Exception {
		String query = "SELECT * FROM Category WHERE id = ?";
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
     * Retrieves all Category records from the database.
     *
     * @return List of all Category objects.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public List<Category> getAll() throws Exception {
		List<Category> categories = new ArrayList<>();
		String query = "SELECT * FROM Category";
		try (Connection connection = DBConnection.getConnection(dbPath);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				categories.add(mapResultSet(resultSet));
			}
		}
		return categories;
	}
	/**
     * Inserts a new Category into the database.
     *
     * @param category The Category object to insert.
     * @return true if the insert was successful, false otherwise.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public boolean insert(Category category) throws Exception {
		String query = "INSERT INTO Category (id, CATEGORY_DESCRIPTION) VALUES (?, ?)";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			long newId = getNextId(connection);
			category.setId(newId);
			statement.setLong(1, newId);
			statement.setString(2, category.getCategoryDescription());
			return statement.executeUpdate() > 0;
		}
	}
	 /**
     * Removes a Category by its ID.
     *
     * @param id The ID of the Category to remove.
     * @return true if the removal was successful, false otherwise.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public boolean removeById(Long id) throws Exception {
		String query = "DELETE FROM Category WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			return statement.executeUpdate() > 0;
		}
	}
	/**
     * Updates an existing Category record in the database.
     *
     * @param category The Category object containing updated details.
     * @return true if the update was successful, false otherwise.
     * @throws Exception If any SQL or database connection errors occur.
     */
	@Override
	public boolean update(Category e) throws Exception {
		String query = "UPDATE Category SET CATEGORY_DESCRIPTION = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, e.getCategoryDescription());
			statement.setLong(2, e.getId());
			return statement.executeUpdate() > 0;
		}
	}
	 /**
     * Searches for a Category based on its description.
     *
     * @param category The Category object with the description to search for.
     * @return Category object if found, otherwise null.
     * @throws Exception If any SQL or database connection errors occur.
     */
	public Category searchByDetails(Category category) throws Exception {
		String query = "SELECT * FROM Category WHERE CATEGORY_DESCRIPTION = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, category.getCategoryDescription());

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return mapResultSet(resultSet); // Reuse existing mapping logic
			}
		}
		return null;
	}
	 /**
     * Searches for a Category by its name/description.
     *
     * @param categoryName The name or description of the category.
     * @return Category object if found, otherwise null.
     * @throws Exception If any SQL or database connection errors occur.
     */
	public Category searchByName(String categoryname) throws Exception {
		String query = "SELECT * FROM Category WHERE CATEGORY_DESCRIPTION = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, categoryname);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return mapResultSet(resultSet); // Reuse existing mapping logic
			}
		}
		return null;
	}
	/**
     * Retrieves the next available ID for the Category table.
     *
     * @param connection The database connection.
     * @return The next ID as a long.
     * @throws Exception If any SQL errors occur.
     */
	private long getNextId(Connection connection) throws Exception {
		String query = "SELECT MAX(id) AS maxId FROM Category";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
			if (resultSet.next()) {
				return resultSet.getLong("maxId") + 1;
			}
		}
		return 1;
	}
	/**
     * Maps a ResultSet row to a Category object.
     *
     * @param resultSet The ResultSet containing the data.
     * @return Category object mapped from the ResultSet.
     * @throws Exception If any SQL errors occur.
     */
	private Category mapResultSet(ResultSet resultSet) throws Exception {
		Category category = new Category();
		category.setId(resultSet.getLong("id"));
		category.setCategoryDescription(resultSet.getString("CATEGORY_DESCRIPTION"));
		return category;
	}


}
