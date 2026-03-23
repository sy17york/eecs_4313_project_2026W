package model.order;

import java.util.Date;

import model.cart.ShoppingCart;
import model.user.Customer;
/**
 * Order represents a purchase made by a customer.
 * It includes details such as the order ID, customer, address, order date,
 * list of items (ShoppingCart), total amount, and the order status.
 */
public class Order {
	 	private Long orderId;        // Unique identifier for the order
	    private Customer customer;   // The customer who placed the order
	    private Address address;     // Shipping address for the order
	    private Date orderDate;      // Date the order was placed
	    private ShoppingCart items;  // Items included in the order
	    private double totalAmount;  // Total cost of the order
	    private String status;       // Current status of the order (e.g., Pending, Completed)

	    /**
	     * Default constructor that initializes an empty Order object.
	     */
	public Order() {
		super();
	}
	/**
     * Parameterized constructor to create an Order with specific details.
     * The total amount is calculated based on the items in the ShoppingCart,
     * and the default status is set to "Pending".
     *
     * @param orderId   The unique ID of the order.
     * @param customer  The customer placing the order.
     * @param address   The shipping address for the order.
     * @param orderDate The date the order was placed.
     * @param items     The ShoppingCart containing items in the order.
     */
	public Order(Long orderId, Customer customer, Address address, Date orderDate, ShoppingCart items) {

		this.orderId = orderId;
		this.customer = customer;
		this.address = address;
		this.orderDate = orderDate;
		this.items = items;
		this.totalAmount = items.getTotalPrice();
		this.status = "Pending";

	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public ShoppingCart getItems() {
		return items;
	}

	public void setItems(ShoppingCart items) {
		this.items = items;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}