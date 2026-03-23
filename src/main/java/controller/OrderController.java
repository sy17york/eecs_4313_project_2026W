package controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.cart.CartItem;
import model.cart.ShoppingCart;
import model.order.Address;
import model.order.Order;
import model.order.Payment;
import model.product.Item;
import model.user.Customer;
import service.ItemService;
import service.OrderService;
/**
 * OrderController handles order-related operations, including confirming orders,
 * placing orders, and managing address and payment details.
 */
@WebServlet("/OrderController")
public class OrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrderService orderService;
	private ItemService itemService;
	 /**
     * Initializes the servlet and sets up the service classes with the database path.
     */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String dbPath = config.getServletContext().getRealPath("/WEB-INF/db/projectDB.db");
		this.orderService = new OrderService(dbPath);
		this.itemService = new ItemService(dbPath);
	}
	 /**
     * Handles GET requests for order confirmation.
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		HttpSession session = req.getSession(true);
		Customer customer = (Customer) session.getAttribute("user");
		if ("confirmOrder".equals(action)) {
		    Address address = null;
		    Payment payment = null;

		    // Fetch existing address and payment details
		    try {
		        address = orderService.getAddressById(customer.getAddressid());
		        payment = orderService.getPaymentById(customer.getPaymentid());
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		 // Pass address and payment details to the JSP
		    req.setAttribute("address", address);
		    req.setAttribute("payment", payment);
		    req.getRequestDispatcher("/jsp/orderConfirmation.jsp").forward(req, resp);
		}
	}
	/**
     * Handles POST requests for placing orders and other actions like returning to the cart.
     */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		ShoppingCart cart;
		synchronized (session) {
			cart = (ShoppingCart) session.getAttribute("cart");
		}

		Customer customer = (Customer) session.getAttribute("user");


		if (cart == null || customer == null) { // Redirect to cart view if cart or customer is null
			resp.sendRedirect("ShoppingCartController?action=viewCart");
			return;
		}

		String action = req.getParameter("action");

		if ("placeOrder".equals(action)) {
		    try {
		        Address address = null;
		        // Handle address selection
		        if ("temporary".equals(req.getParameter("addressType"))) {// Create temporary address from form data
		            address = new Address(null, req.getParameter("street"), req.getParameter("province"),
		                    req.getParameter("country"), req.getParameter("zip"), req.getParameter("phone"));
		            orderService.insert(address); // Save temporary address
		        } else if ("existing".equals(req.getParameter("addressType"))) {// Retrieve existing address
		            address = orderService.getAddressById(customer.getAddressid());
		        }

		        Payment payment = null;
		        // Handle payment selection
		        if ("temporary".equals(req.getParameter("paymentType"))) { // Create temporary payment details
		            payment = new Payment(null, Integer.parseInt(req.getParameter("card_number")),
		                    Integer.parseInt(req.getParameter("pin")));

		        } else if ("existing".equals(req.getParameter("paymentType"))) {// Retrieve existing payment details
		            payment = orderService.getPaymentById(customer.getPaymentid());
		        }

		        // Validate address and payment
		        if (address == null || payment == null) {
		            req.setAttribute("error", "Address or Payment information is missing.");
		            req.getRequestDispatcher("/jsp/orderConfirmation.jsp").forward(req, resp);
		            return;
		        }

		        // Dummy payment algorithm
		        boolean paymentApproved = dummyPaymentAlgorithm(payment);
		        if (!paymentApproved) {
		            req.setAttribute("error", "Credit Card Authorization Failed. Please try again.");
		            req.getRequestDispatcher("/jsp/orderConfirmation.jsp").forward(req, resp);
		            return;
		        }

		        // Create and place the order
		        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		        Order order = new Order(null, customer, address, currentTimestamp, cart);
		        order.setStatus("completed");

		        order = orderService.placeOrder(order); // Save order to DB
		        if (order.getOrderId() != null) {
		            for (CartItem cartItem : cart.getItems()) {
		                Item item = cartItem.getItem();
		                int newStock = item.getStockQuantity() - cartItem.getQuantity();
		                item.setStockQuantity(newStock);
		                itemService.updateItem(item);
		            }
		         // Retrieve placed order for review
		            order = orderService.getOrderById(order.getOrderId());
		            req.setAttribute("order_review", order);
		            req.setAttribute("card", payment.getCard_number());

		            cart.clearCart(); // Clear cart after successful order
		            session.removeAttribute("cart");
		        } else {
		            req.setAttribute("error", "Failed to place the order.");
		        }

		        req.getRequestDispatcher("/jsp/orderSummary.jsp").forward(req, resp);

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}else if ("backToCart".equals(action)) {// Redirect back to the shopping cart
			resp.sendRedirect("ShoppingCartController?action=viewCart");
		}
	}
	 /**
     * Simulates a dummy payment validation algorithm.
     *
     * @param payment Payment object containing card details.
     * @return boolean indicating whether the payment was approved.
     */
	private boolean dummyPaymentAlgorithm(Payment payment) {

		return Math.random() >= 0.33; // Deny approximately every 3rd payment
	}

}
