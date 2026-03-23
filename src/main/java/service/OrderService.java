package service;

import dao.AddressDAOImpl;
import dao.OrdersDAOImpl;
import dao.PaymentDAOImpl;
import model.order.Address;
import model.order.Order;
import model.order.Payment;
/**
 * OrderService handles order placement and retrieval functionalities.
 * It interacts with Orders, Address, and Payment DAOs to manage order-related data.
 */
public class OrderService {
	private final OrdersDAOImpl ordersDAO;// DAO for order operations
	private final AddressDAOImpl addressDAO;// DAO for address operations
	private final PaymentDAOImpl paymentDAO;// DAO for payment operations
	  /**
     * Initializes OrderService with DAO implementations.
     *
     * @param dbPath The path to the SQLite database.
     */
	public OrderService(String dbPath) {
		this.ordersDAO = new OrdersDAOImpl(dbPath);
		this.addressDAO = new AddressDAOImpl(dbPath);
		this.paymentDAO = new PaymentDAOImpl(dbPath);
	}
	 /**
     * Places a new order into the database.
     *
     * Steps:
     * 1. Check if the address already exists; insert it if not.
     * 2. Place the order using the Orders DAO.
     *
     * @param order The Order object containing order details.
     * @return The placed Order object if successful; null otherwise.
     * @throws Exception If any database or SQL errors occur.
     */
	public Order placeOrder(Order order) throws Exception {

		// Check if the address already exists
		Address existingAddress = addressDAO.searchByDetails(order.getAddress());
		if (existingAddress == null) {
			addressDAO.insert(order.getAddress());
		} else {
			order.setAddress(existingAddress);
		}

		// Place the order
		if(ordersDAO.insert(order)) {return order;}
		else {return null;}// Return null if the order placement failed

	}
		/**
	     * Retrieves an address by its ID.
	     *
	     * @param addressid The ID of the address to retrieve.
	     * @return The Address object if found.
	     * @throws Exception If any database or SQL errors occur.
	     */
	public Address getAddressById(Long addressid) throws Exception {
		return addressDAO.searchById(addressid);
	}
	/**
     * Retrieves an order by its ID.
     *
     * @param orderid The ID of the order to retrieve.
     * @return The Order object if found.
     * @throws Exception If any database or SQL errors occur.
     */
	public Order getOrderById(Long orderid) throws Exception {
		return ordersDAO.searchById(orderid);
	}
	/**
     * Retrieves payment details by payment ID.
     *
     * @param paymentid The ID of the payment details to retrieve.
     * @return The Payment object if found.
     * @throws Exception If any database or SQL errors occur.
     */
	public Payment getPaymentById(Long paymentid) throws Exception {
		return paymentDAO.searchById(paymentid);
	}
	/**
     * Inserts a new address into the database.
     *
     * @param address The Address object to insert.
     * @throws Exception If any database or SQL errors occur.
     */
	public void insert(Address address) throws Exception {
		addressDAO.insert(address);

	}
}
