package service;

import java.util.ArrayList;
import java.util.List;

import dao.AddressDAOImpl;
import dao.CategoryDAOImpl;
import dao.ItemDAOImpl;
import dao.OrdersDAOImpl;
import dao.PaymentDAOImpl;
import dao.UserDAOImpl;
import model.order.Address;
import model.order.Order;
import model.order.Payment;
import model.product.Category;
import model.product.Item;
import model.user.User;
/**
 * AdminService provides services for managing users, inventory, orders,
 * categories, addresses, and payments. It acts as a bridge between the
 * DAO implementations and the business layer.
 */
public class AdminService {
	private final ItemDAOImpl itemDAO;
	private final UserDAOImpl userDAO;
	private final OrdersDAOImpl ordersDAO;
	private final CategoryDAOImpl categoryDAO;
	private final AddressDAOImpl addressDAO;
	private final PaymentDAOImpl paymentDAO;
	/**
     * Initializes AdminService with DAO implementations.
     *
     * @param dbPath The path to the SQLite database.
     */
	public AdminService(String dbPath) {
		this.itemDAO = new ItemDAOImpl(dbPath);
		this.userDAO = new UserDAOImpl(dbPath);
		this.ordersDAO = new OrdersDAOImpl(dbPath);
		this.categoryDAO = new CategoryDAOImpl(dbPath);
		this.addressDAO = new AddressDAOImpl(dbPath);
		this.paymentDAO = new PaymentDAOImpl(dbPath);
	}
	 /**
     * Retrieves the complete sales history.
     *
     * @return A list of all orders.
     * @throws Exception If an error occurs while fetching the data.
     */
	public List<Order> getSalesHistory() throws Exception {
		List<Order> sales = ordersDAO.getAll();
	    if (sales == null) {
	        sales = new ArrayList<>(); // Ensure it's never null
	    }
	    return sales;
	}

    /**
     * Retrieves all items in the inventory.
     *
     * @return A list of all items.
     * @throws Exception If an error occurs while fetching the data.
     */
	public List<Item> getInventory() throws Exception {
		return itemDAO.getAll();
	}

    /**
     * Retrieves all registered users.
     *
     * @return A list of all users.
     * @throws Exception If an error occurs while fetching the data.
     */
	public List<User> getAllUsers() throws Exception {
		return userDAO.getAll();
	}
	 /**
     * Updates the stock quantity of an item in the inventory.
     *
     * @param itemId   The ID of the item.
     * @param quantity The new stock quantity.
     * @throws Exception If the item is not found or an error occurs.
     */
	public void updateInventory(Long itemId, int quantity) throws Exception {
		Item item = itemDAO.searchById(itemId);
		if (item != null) {
			item.setStockQuantity(quantity);
			itemDAO.update(item);
		} else {
			throw new Exception("Item not found");
		}
	}

    /**
     * Inserts a new item into the inventory.
     *
     * @param item The item to be inserted.
     * @return The ID of the newly inserted item.
     * @throws Exception If an error occurs during insertion.
     */
	public Long insertItem(Item item) throws Exception {

		// Check if the Category already exists


		// Place the order
		itemDAO.insert(item);
		return item.getItemId();
	}
	/**
     * Updates a user's email.
     *
     * @param userId   The ID of the user.
     * @param newEmail The new email to set.
     * @throws Exception If the user is not found or an error occurs.
     */
	public void updateUser(Long userId, String newEmail) throws Exception {
		User user = userDAO.searchById(userId);
		if (user != null) {
			user.setEmail(newEmail);
			userDAO.update(user);
		} else {
			throw new Exception("User not found");
		}
	}
	/**
     * Retrieves a category by its ID.
     *
     * @param cateid The ID of the category.
     * @return The Category object.
     * @throws Exception If an error occurs while fetching the category.
     */
	public Category getCategorieById(Long cateid) throws Exception {
		return categoryDAO.searchById(cateid);
	}
	/**
     * Retrieves all categories.
     *
     * @return A list of all categories.
     * @throws Exception If an error occurs while fetching categories.
     */
	public List<Category> getAllCategories() throws Exception {
		return categoryDAO.getAll();
	}
	/**
     * Removes an item from the inventory by its ID.
     *
     * @param itemId The ID of the item to remove.
     * @throws Exception If an error occurs during removal.
     */
	public void removeItem(Long itemId) throws Exception {
		itemDAO.removeById(itemId);

	}
	/**
     * Adds a new category.
     *
     * @param category The category to add.
     * @throws Exception If an error occurs during insertion.
     */
	public void addCategory(Category category) throws Exception {
		categoryDAO.insert(category);

	}

    /**
     * Retrieves a category by its name.
     *
     * @param categoryname The name of the category.
     * @return The Category object.
     * @throws Exception If an error occurs during retrieval.
     */
	public Category getCategoryByName(String categoryname) throws Exception {
		return categoryDAO.searchByName(categoryname);
	}
	/**
     * Retrieves a user's details by ID.
     *
     * @param userId The user's ID.
     * @return The User object.
     * @throws Exception If the user is not found.
     */
	public User getUserById(Long userId) throws Exception {
		return userDAO.searchById(userId);
	}
	 /**
     * Retrieves a user's purchase history.
     *
     * @param userId The ID of the user.
     * @return A list of orders associated with the user.
     * @throws Exception If an error occurs during retrieval.
     */
	public List<Order> getUserPurchaseHistory(Long userId) throws Exception {
		return ordersDAO.searchByUserId(userId);
	}
	 /**
     * Retrieves a user's address details.
     *
     * @param userId The ID of the user.
     * @return The Address object.
     * @throws Exception If the address is not found.
     */
	public Address getUserAddress(Long userId) throws Exception {
		Long id = userDAO.searchAddressId(userId);
		return addressDAO.searchById(id);

	}
	/**
     * Retrieves a user's payment details.
     *
     * @param userId The ID of the user.
     * @return The Payment object.
     * @throws Exception If the payment details are not found.
     */
	public Payment getUserPayment(Long userId) throws Exception {
		Long id = userDAO.searchPaymentId(userId);
		return paymentDAO.searchById(id);
	}
	 /**
     * Updates a user's information (email and username).
     *
     * @param userId   The ID of the user.
     * @param email    The new email.
     * @param username The new username.
     * @throws Exception If the user is not found.
     */
	public void updateUserInfo(Long userId, String email, String username) throws Exception {
		User user = userDAO.searchById(userId);
		user.setEmail(email);
		user.setUsername(username);
		userDAO.update(user);
	}
	/**
     * Updates a user's address.
     *
     * @param userId  The ID of the user.
     * @param address The Address object to update.
     * @throws Exception If an error occurs during update.
     */
	public void updateUserAddress(Long userId, Address address) throws Exception {
		Address existingAddress = addressDAO.searchByDetails(address);
		if (existingAddress == null) {
			addressDAO.insert(address);
		} else {
			address = existingAddress;
		}

		User user = userDAO.searchById(userId);
		user.setAddressid(address.getId());
		userDAO.update(user);

	}
	 /**
     * Updates a user's payment information.
     *
     * @param userId  The ID of the user.
     * @param payment The Payment object to update.
     * @throws Exception If an error occurs during update.
     */
	public void updateUserPayment(Long userId, Payment payment) throws Exception {
		Payment existingPayment = paymentDAO.searchByDetails(payment);
		if (existingPayment == null) {
			paymentDAO.insert(payment);
		} else {
			payment = existingPayment;
		}

		User user = userDAO.searchById(userId);
		user.setPaymentid(payment.getId());
		userDAO.update(user);

	}
	/**
     * Filters sales history based on optional criteria: username, product name, and date range.
     *
     * @param username   The username for filtering.
     * @param productName The product name for filtering.
     * @param startDate   The start date for filtering.
     * @param endDate     The end date for filtering.
     * @return A list of orders matching the criteria.
     * @throws Exception If an error occurs during filtering.
     */
	public List<Order> filterSalesHistoryByCriteria(String username, String productName, java.sql.Date startDate, java.sql.Date endDate) throws Exception {
	    return ordersDAO.filterSalesByCriteria(username, productName, startDate, endDate);
	}
	 /**
     * Retrieves details of a specific order.
     *
     * @param orderId The ID of the order.
     * @return The Order object containing the order details.
     * @throws Exception If the order is not found.
     */
	public Order getOrderDetails(Long orderId) throws Exception {
		return ordersDAO.searchById(orderId);
	}

}
