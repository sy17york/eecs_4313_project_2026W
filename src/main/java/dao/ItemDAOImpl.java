package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.product.Category;
import model.product.Item;
import util.DBConnection;
/**
 * ItemDAOImpl is the Data Access Object (DAO) implementation for managing
 * Item entities in the database. It provides CRUD operations and additional
 * search, sort, and filter functionalities.
 */
public class ItemDAOImpl implements DAO<Item> {
	private final String dbPath;
	/**
     * Constructor to initialize the database path.
     *
     * @param dbPath Path to the database file.
     */
	public ItemDAOImpl(String dbPath) {
		this.dbPath = dbPath;

	}
	 /**
     * Searches for an Item by its ID, including its associated Category.
     *
     * @param id The ID of the Item.
     * @return The Item object if found, otherwise null.
     */
	@Override
	public Item searchById(Long id) throws Exception {
		String query = "SELECT * FROM Item i JOIN Category c ON i.category_id = c.id WHERE i.itemID = ?";
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
     * Retrieves all Items from the database along with their Categories.
     *
     * @return List of all Item objects.
     */
	@Override
	public List<Item> getAll() throws Exception {
		List<Item> items = new ArrayList<>();
		String query = """
				SELECT i.itemID, i.name, i.description, i.brand, i.quantity, i.price,
				       i.category_id, c.CATEGORY_DESCRIPTION
				FROM Item i
				JOIN Category c ON i.category_id = c.id;
				""";
		try (Connection connection = DBConnection.getConnection(dbPath);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				items.add(mapResultSet(resultSet));
			}
		}
		return items;
	}

    /**
     * Inserts a new Item into the database.
     *
     * @param item The Item object to insert.
     * @return true if insertion is successful, false otherwise.
     */
	@Override
	public boolean insert(Item item) throws Exception {
		String query = "INSERT INTO Item (itemID, name, description, category_id, brand, quantity, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			System.out.println("ItemDAOImpl initialized with dbPath: " + dbPath);
			long newId = getNextId(connection);
			item.setItemId(newId);
			statement.setLong(1, newId);
			statement.setString(2, item.getName());
			statement.setString(3, item.getDescription());
			statement.setLong(4, item.getCategory().getId());
			statement.setString(5, item.getBrand());
			statement.setInt(6, item.getStockQuantity());
			statement.setDouble(7, item.getPrice());
			return statement.executeUpdate() > 0;
		}
	}
	 /**
     * Removes an Item from the database by its ID.
     *
     * @param id The ID of the Item to remove.
     * @return true if removal is successful, false otherwise.
     */
	@Override
	public boolean removeById(Long id) throws Exception {
		String query = "DELETE FROM Item WHERE itemID = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			return statement.executeUpdate() > 0;
		}
	}
	   /**
     * Updates an existing Item in the database.
     *
     * @param item The Item object with updated data.
     * @return true if update is successful, false otherwise.
     */
	@Override
	public boolean update(Item item) throws Exception {
		String query = "UPDATE Item SET name = ?, description = ?, category_id = ?, brand = ?, quantity = ?, price = ? WHERE itemID = ?";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, item.getName());
			statement.setString(2, item.getDescription());
			statement.setLong(3, item.getCategoryId());
			statement.setString(4, item.getBrand());
			statement.setInt(5, item.getStockQuantity());
			statement.setDouble(6, item.getPrice());
			statement.setLong(7, item.getItemId());
			return statement.executeUpdate() > 0;
		}
	}
    /**
     * Searches for Items by Category ID.
     *
     * @param categoryId The ID of the Category.
     * @return List of matching Item objects.
     */
	public List<Item> searchByCategory(Long categoryId) throws Exception {
		List<Item> products = new ArrayList<>();
		String query = """
				SELECT i.itemID, i.name, i.description, c.CATEGORY_DESCRIPTION, i.category_id, i.brand, i.quantity, i.price
				FROM Item i
				JOIN Category c ON i.category_id = c.id
				WHERE i.category_id = ?;
				""";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, categoryId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				products.add(mapResultSet(resultSet));
			}
		}
		return products;
	}
	/**
	 * Searches for items matching a specific keyword in their name, description,
	 * brand, or category description. Uses SQL LIKE statements for partial matches.
	 *
	 * @param keyword The search keyword to look for.
	 * @return List of Item objects matching the keyword.
	 * @throws Exception If any SQL or database connection errors occur.
	 */
	public List<Item> searchByKeyword(String keyword) throws Exception {
		List<Item> products = new ArrayList<>();
		String query = """
				SELECT i.itemID, i.name, i.description, i.category_id, c.CATEGORY_DESCRIPTION, i.brand, i.quantity, i.price
				FROM Item i
				JOIN Category c ON i.category_id = c.id
				WHERE i.name LIKE ? OR i.description LIKE ? OR i.brand LIKE ? OR c.CATEGORY_DESCRIPTION LIKE ?;
				""";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			String wildcardKeyword = "%" + keyword + "%";
			statement.setString(1, wildcardKeyword);
			statement.setString(2, wildcardKeyword);
			statement.setString(3, wildcardKeyword);
			statement.setString(4, wildcardKeyword);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				products.add(mapResultSet(resultSet));
			}
		}
		return products;
	}
	/**
	 * Sorts all items by their category ID in ascending order.
	 *
	 * @return List of sorted Item objects.
	 * @throws Exception If any SQL or database connection errors occur.
	 */
	public List<Item> sortByCategory() throws Exception {
		String query = "SELECT * FROM Item ORDER BY category_id ASC";
		return executeSortQuery(query);
	}

/**
 * Sorts all items by their brand name in ascending order.
 *
 * @return List of sorted Item objects.
 * @throws Exception If any SQL or database connection errors occur.
 */
	public List<Item> sortByBrand() throws Exception {
		String query = "SELECT * FROM Item ORDER BY brand ASC";
		return executeSortQuery(query);
	}
	/**
	 * Sorts all items by their price in ascending order.
	 *
	 * @return List of sorted Item objects.
	 * @throws Exception If any SQL or database connection errors occur.
	 */
	public List<Item> sortByPrice() throws Exception {
		String query = "SELECT * FROM Item ORDER BY price ASC";
		return executeSortQuery(query);
	}
	/**
	 * Executes a query that sorts items based on a specific criterion.
	 *
	 * @param query The SQL query to execute for sorting items.
	 * @return List of sorted Item objects.
	 * @throws Exception If any SQL or database connection errors occur.
	 */
	private List<Item> executeSortQuery(String query) throws Exception {
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			List<Item> items = new ArrayList<>();
			while (resultSet.next()) {
				items.add(mapResultSet(resultSet));
			}
			return items;
		}
	}
	/**
     * Retrieves the next available ID for the Item table.
     *
     * @param connection The database connection.
     * @return The next ID as a long.
     */
	private long getNextId(Connection connection) throws Exception {
		String query = "SELECT MAX(itemID) AS maxId FROM Item";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
			if (resultSet.next()) {
				return resultSet.getLong("maxId") + 1;
			}
		}
		return 1;
	}
    /**
     * Maps a ResultSet row to an Item object.
     *
     * @param resultSet The ResultSet containing data.
     * @return The mapped Item object.
     */
	private Item mapResultSet(ResultSet resultSet) throws Exception {
		Item item = new Item();
		item.setItemId(resultSet.getLong("itemID"));
		item.setName(resultSet.getString("name"));
		item.setDescription(resultSet.getString("description"));
		item.setBrand(resultSet.getString("brand"));
		item.setStockQuantity(resultSet.getInt("quantity"));
		item.setPrice(resultSet.getDouble("price"));

		// Map Category object and set it in the Item
		Category category = new Category();
		category.setId(resultSet.getLong("category_id"));
		category.setCategoryDescription(resultSet.getString("CATEGORY_DESCRIPTION"));
		item.setCategory(category);

		return item;
	}
	/**
	 * Searches for items by their brand name.
	 *
	 * @param brandStr The brand name to filter items by.
	 * @return List of Item objects that match the specified brand.
	 * @throws Exception If any SQL or database connection errors occur.
	 */
	public List<Item> searchByBrand(String brandStr) throws Exception {
		List<Item> products = new ArrayList<>();
		String query = """
				SELECT i.itemID, i.name, i.description, c.CATEGORY_DESCRIPTION, i.category_id, i.brand, i.quantity, i.price
				FROM Item i
				JOIN Category c ON i.category_id = c.id
				WHERE i.brand = ?;
				""";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, brandStr);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				products.add(mapResultSet(resultSet));
			}
		}
		return products;
	}
	/**
	 * Retrieves a list of all unique brand names from the Item table.
	 *
	 * @return List of distinct brand names as Strings.
	 * @throws SQLException If any SQL or database connection errors occur.
	 */
	public List<String> getAllBrands() throws SQLException {
		List<String> brands = new ArrayList<>();
		String query = """
				SELECT DISTINCT brand
				FROM Item


				""";
		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {

			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				brands.add(resultSet.getString("brand"));
			}
		}
		return brands;
	}
}
