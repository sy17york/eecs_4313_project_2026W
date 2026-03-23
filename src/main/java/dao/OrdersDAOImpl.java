package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.cart.CartItem;
import model.cart.ShoppingCart;
import model.order.Address;
import model.order.Order;
import model.product.Category;
import model.product.Item;
import model.user.Customer;
import model.user.User;
import util.DBConnection;
/**
 * OrdersDAOImpl is a Data Access Object (DAO) implementation for managing
 * Order entities in the database. It supports CRUD operations, user-specific
 * order retrieval, and filtering of sales based on criteria.
 */
public class OrdersDAOImpl implements DAO<Order> {
	private final String dbPath;
	/**
     * Constructor to initialize the database path.
     *
     * @param dbPath The path to the database file.
     */
	public OrdersDAOImpl(String dbPath) {
		this.dbPath = dbPath;
	}
	  /**
     * Searches for an Order by its ID.
     *
     * @param id The ID of the order.
     * @return The Order object if found, or null if not.
     * @throws Exception If any database or SQL errors occur.
     */
	@Override
	public Order searchById(Long id) throws Exception {
		String orderQuery = """
				SELECT o.id, o.userID, o.dateOfPurchase, o.AddressID, o.totalamount, o.status,
				       a.street, a.province, a.country, a.zip, a.phone,
				       u.username, u.password, u.email
				FROM Orders o
				JOIN Address a ON o.AddressID = a.id
				JOIN User u ON o.userID = u.user_id
				WHERE o.id = ?;
				""";

		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(orderQuery)) {
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Order order = mapOrder(resultSet);
				order.setItems(fetchOrderItems(id)); // Fetch related items
				return order;
			}
		}
		return null;
	}
    /**
     * Retrieves all orders from the database.
     *
     * @return A list of all Order objects.
     * @throws Exception If any database or SQL errors occur.
     */
	@Override
	public List<Order> getAll() throws Exception {
		List<Order> orders = new ArrayList<>();
		String orderQuery = """
				SELECT o.id, o.userID, o.dateOfPurchase, o.AddressID, o.totalamount, o.status,
				       a.street, a.province, a.country, a.zip, a.phone,
				       u.username, u.password, u.email
				FROM Orders o
				JOIN Address a ON o.AddressID = a.id
				JOIN User u ON o.userID = u.user_id;
				""";

		try (Connection connection = DBConnection.getConnection(dbPath);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(orderQuery)) {
			while (resultSet.next()) {
				Order order = mapOrder(resultSet);
				order.setItems(fetchOrderItems(order.getOrderId())); // Fetch related items
				orders.add(order);
			}
		}catch (Exception e) {
	        System.err.println("Error in OrdersDAOImpl.getAll: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
		return orders;
	}
    /**
     * Inserts a new Order into the database, including its items.
     *
     * @param order The Order object to insert.
     * @return true if the operation is successful.
     * @throws Exception If any database or SQL errors occur.
     */
	@Override
	public boolean insert(Order order) throws Exception {
		String orderQuery = "INSERT INTO Orders (id, userID, dateOfPurchase, AddressID, totalamount, status) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection(dbPath)) {

				// Insert Order
				try (PreparedStatement statement = connection.prepareStatement(orderQuery)) {
					long newId = getNextOrderId(connection);
					order.setOrderId(newId);
					statement.setLong(1, newId);
					statement.setLong(2, order.getCustomer().getId());
					statement.setTimestamp(3, new java.sql.Timestamp(order.getOrderDate().getTime()));
					statement.setLong(4, order.getAddress().getId());
					statement.setDouble(5, order.getTotalAmount());
					statement.setString(6, order.getStatus());
					statement.executeUpdate();


				// Insert Order Items
				insertOrderItems(connection, order.getOrderId(), order.getItems());


				return true;
			} catch (Exception e) {

				e.printStackTrace();
				throw e;
			}
		}
	}
	 /**
     * Removes an order by its ID.
     *
     * @param id The ID of the order to remove.
     * @return true if the removal is successful.
     * @throws Exception If any database or SQL errors occur.
     */
	@Override
	public boolean removeById(Long id) throws Exception {
		String deleteItemsQuery = "DELETE FROM OrderItems WHERE order_id = ?";
		String deleteOrderQuery = "DELETE FROM Orders WHERE id = ?";

		try (Connection connection = DBConnection.getConnection(dbPath)) {
			connection.setAutoCommit(false);

			try (PreparedStatement itemsStmt = connection.prepareStatement(deleteItemsQuery);
					PreparedStatement orderStmt = connection.prepareStatement(deleteOrderQuery)) {
				itemsStmt.setLong(1, id);
				itemsStmt.executeUpdate();

				orderStmt.setLong(1, id);
				orderStmt.executeUpdate();

				connection.commit();
				return true;
			} catch (Exception e) {
				connection.rollback();
				throw e;
			}
		}
	}
	/**
	 * Updates an Order in the database. Not supported for completed orders.
	 *
	 * @param order The Order object to update.
	 * @throws UnsupportedOperationException If update operation is attempted.
	 */
	@Override
	public boolean update(Order order) throws Exception {
		throw new UnsupportedOperationException("Update is not allowed for completed Orders.");
	}


	/**
	 * Inserts order items into the OrderItems table as part of an Order transaction.
	 *
	 * @param connection The database connection for batch processing.
	 * @param orderId The ID of the order these items belong to.
	 * @param cart The ShoppingCart containing the items to insert.
	 * @throws Exception If any SQL errors occur.
	 */
	private void insertOrderItems(Connection connection, Long orderId, ShoppingCart cart) throws Exception {
		String query = "INSERT INTO OrderItems (order_id, item_id, quantity) VALUES (?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (CartItem item : cart.getItems()) {
				statement.setLong(1, orderId);
				statement.setLong(2, item.getItem().getItemId());
				statement.setInt(3, item.getQuantity());
				statement.addBatch();// Add to batch for efficiency
			}
			statement.executeBatch();// Execute the batch insert
		}
	}
	/**
	 * Searches for all orders associated with a specific user ID.
	 *
	 * @param id The user ID for whom to fetch orders.
	 * @return A list of Order objects belonging to the user.
	 * @throws Exception If any database or SQL errors occur.
	 */
	public List<Order> searchByUserId(Long id) throws Exception {
		List<Order> orders = new ArrayList<>();
		String orderQuery = """
				SELECT o.id, o.userID, o.dateOfPurchase, o.AddressID, o.totalamount, o.status,
				       a.street, a.province, a.country, a.zip, a.phone,
				       u.username, u.password, u.email
				FROM Orders o
				JOIN Address a ON o.AddressID = a.id
				JOIN User u ON o.userID = u.user_id
				WHERE o.userID = ?;
				""";

		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(orderQuery)) {
			statement.setLong(1, id);// Bind user ID
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {// Map each result to an Order object
				Order order = mapOrder(resultSet);
				order.setItems(fetchOrderItems(order.getOrderId())); // Fetch related items
				orders.add(order);
			}
		}
		return orders;
	}
	/**
	 * Filters orders based on dynamic criteria: username, product name, start date, and end date.
	 *
	 * @param username The username to filter orders by (optional).
	 * @param productName The product name to filter orders by (optional).
	 * @param startDate The start date for the order filter (optional).
	 * @param endDate The end date for the order filter (optional).
	 * @return A list of filtered Order objects.
	 * @throws Exception If any database or SQL errors occur.
	 */
	public List<Order> filterSalesByCriteria(String username, String productName, java.sql.Date startDate, java.sql.Date endDate) throws Exception {
		List<Order> orders = new ArrayList<>();
		String query = """
				    SELECT DISTINCT o.id, o.userID, o.dateOfPurchase, o.AddressID, o.totalamount, o.status,
				           u.username,u.password, u.email,
				           a.street, a.province, a.country, a.zip, a.phone,
				           oi.item_id, oi.quantity, i.name, i.price
				    FROM Orders o
				    LEFT JOIN User u ON o.userID = u.user_id
				    LEFT JOIN Address a ON o.AddressID = a.id
				    LEFT JOIN OrderItems oi ON o.id = oi.order_id
				    LEFT JOIN Item i ON oi.item_id = i.itemID
				    WHERE (u.username = ? OR ? IS NULL)
					  AND (i.name = ? OR ? IS NULL)
					  AND (o.dateOfPurchase >= ? OR ? IS NULL)
					  AND (o.dateOfPurchase <= ? OR ? IS NULL)
					ORDER BY o.dateOfPurchase DESC;
				""";


		try (Connection connection = DBConnection.getConnection(dbPath);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, username);
	        statement.setString(2, username);
	        statement.setString(3, productName);
	        statement.setString(4, productName);
	        statement.setString(5, startDate != null ? startDate.toString() : null);
	        statement.setString(6, startDate != null ? startDate.toString() : null);
	        statement.setString(7, endDate != null ? endDate.toString() : null);
	        statement.setString(8, endDate != null ? endDate.toString() : null);

	        ResultSet resultSet = statement.executeQuery();
	        Set<Long> orderIds = new HashSet<>();
	        while (resultSet.next()) {
	            long orderId = resultSet.getLong("id");
	            if (!orderIds.contains(orderId)) {
	                Order order = mapOrder(resultSet);
	                order.setItems(fetchOrderItems(orderId));
	                orders.add(order);
	                orderIds.add(orderId);
	            }
	        }
		}
		return orders;
	}
	/**
     * Retrieves the next available order ID.
     *
     * @param connection Database connection.
     * @return Next available ID.
     */
	private long getNextOrderId(Connection connection) throws Exception {
		String query = "SELECT MAX(id) AS maxId FROM Orders";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
			if (resultSet.next()) {
				return resultSet.getLong("maxId") + 1;
			}
		}
		return 1;
	}
	  /**
     * Maps a result set to an Order object.
     *
     * @param resultSet The result set containing order data.
     * @return Mapped Order object.
     */
	private Order mapOrder(ResultSet resultSet) throws Exception {
		Order order = new Order();
		order.setOrderId(resultSet.getLong("id"));
		order.setOrderDate(resultSet.getTimestamp("dateOfPurchase"));
		order.setTotalAmount(resultSet.getDouble("totalamount"));
		order.setStatus(resultSet.getString("status"));

		// Map Customer
		User customer = new Customer(resultSet.getLong("userID"), resultSet.getString("username"),
				resultSet.getString("password"), resultSet.getString("email"));
		order.setCustomer((Customer) customer);

		// Map Address
		Address address = new Address(resultSet.getLong("AddressID"), resultSet.getString("street"),
				resultSet.getString("province"), resultSet.getString("country"), resultSet.getString("zip"),
				resultSet.getString("phone"));
		order.setAddress(address);

		return order;
	}
	/**
	 * Maps a single row in the ResultSet to a CartItem object.
	 *
	 * @param resultSet The result set containing item details.
	 * @return The corresponding CartItem object.
	 * @throws Exception If any errors occur during mapping.
	 */
	private CartItem mapOrderItem(ResultSet resultSet) throws Exception {
		Item item = new Item();
		Category cate = new Category();
		cate.setId(resultSet.getLong("category_id"));
		cate.setCategoryDescription(resultSet.getString("CATEGORY_DESCRIPTION"));

		item.setItemId(resultSet.getLong("item_id"));
		item.setName(resultSet.getString("name"));
		item.setDescription(resultSet.getString("description"));
		item.setPrice(resultSet.getDouble("price"));
		item.setStockQuantity(resultSet.getInt("stock"));
		item.setCategory(cate);
		item.setBrand(resultSet.getString("brand"));


		return new CartItem(item, resultSet.getInt("quantity"));
	}
	/**
     * Fetches items associated with a specific order.
     *
     * @param orderId The ID of the order.
     * @return ShoppingCart containing the order items.
     * @throws Exception If any database or SQL errors occur.
     */
	private ShoppingCart fetchOrderItems(Long orderId) throws Exception {
		ShoppingCart cart = new ShoppingCart();
		String query = """
				SELECT oi.item_id, oi.quantity, i.name, i.description, i.brand, i.quantity AS stock, i.price,
				       i.category_id, c.CATEGORY_DESCRIPTION
				FROM OrderItems oi
				JOIN Item i ON oi.item_id = i.itemID
				JOIN Category c ON c.id = i.category_id
				WHERE oi.order_id = ?;
				""";

		try (Connection connection = DBConnection.getConnection(dbPath);
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, orderId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				cart.addItem(mapOrderItem(resultSet));
			}
		}
		return cart;
	}
}
