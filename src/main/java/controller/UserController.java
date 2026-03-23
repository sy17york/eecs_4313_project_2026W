package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.order.Address;
import model.order.Order;
import model.order.Payment;
import model.user.User;
import service.UserService;
/**
 * UserController handles user profile management, including viewing the profile,
 * updating personal information, address, payment details, and viewing order details.
 */
@WebServlet("/UserController")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L; // Added serialVersionUID
	private UserService userService;
	/**
     * Initializes the servlet and sets up the UserService with the database path.
     */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String dbPath = config.getServletContext().getRealPath("/WEB-INF/db/projectDB.db");
		this.userService = new UserService(dbPath);
	}
	 /**
     * Handles GET requests for user profile management and order details.
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		try {
			if ("profile".equals(action)) { // Handle viewing user profile

					HttpSession session = req.getSession();
					User user = (User) session.getAttribute("user");

					if (user != null) {// Fetch user's address, payment info, and orders
						Address address = userService.getAddressById(user.getAddressid());
						Payment payment = userService.getPaymentById(user.getPaymentid());
						List<Order> orders = userService.getMyOrder(user.getId());

						 // Set data as request attributes
						req.setAttribute("sales", orders);
						session.setAttribute("address", address);
						session.setAttribute("payment", payment);
						req.getRequestDispatcher("/jsp/profile.jsp").forward(req, resp);  // Forward to profile JSP
					} else {
						// Redirect to login if the user is not logged in
						resp.sendRedirect("AuthenticationController?action=loginPage");
					}


			}else if ("viewOrderDetails".equals(action)) { // Handle viewing details of a specific order
				Long orderId = Long.parseLong(req.getParameter("orderId"));
				Order order = userService.getOrderDetails(orderId);

				req.setAttribute("orderDetails", order);
				req.getRequestDispatcher("/jsp/profile.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
		}
	}
	 /**
     * Handles POST requests for updating personal information, address, and payment details.
     */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");

		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute("user");

			if (user == null) {// Redirect to login if the session has expired or user is not logged in
				resp.sendRedirect("AuthenticationController?action=loginPage");
				return;
			}
			 String successMessage = null;// Message to display upon successful updates
			switch (action) {
			case "updatePersonal": // Update personal information
				updatePersonalInformation(req, user);
				successMessage = "Personal information updated successfully!";
				break;
			case "updateAddress":// Update address information
				updateAddressInformation(req, user);
				successMessage = "Address information updated successfully!";
				break;
			case "updatePayment": // Update payment information
				updatePaymentInformation(req, user);
				successMessage = "Payment information updated successfully!";
				break;
			default: // Handle invalid action
				req.setAttribute("error", "Invalid action specified.");

				return;
			}

			// Refresh session data after updates
			session.setAttribute("user", userService.getUserById(user.getId()));
			if (successMessage != null) {
	            session.setAttribute("profileSuccessMessage", successMessage);
	        }
			resp.sendRedirect("UserController?action=profile"); // Redirect back to the profile page
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging

		}
	}
	/**
     * Updates user's personal information.
     */
	private void updatePersonalInformation(HttpServletRequest req, User user) throws Exception {
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		userService.updateUserProfile(user.getId(), name, email, password);
	}

    /**
     * Updates user's address information.
     */
	private void updateAddressInformation(HttpServletRequest req, User user) throws Exception {
		Address address = userService.getAddressById(user.getAddressid());
		if (address != null) {
			address.setStreet(req.getParameter("street"));
			address.setProvince(req.getParameter("province"));
			address.setCountry(req.getParameter("country"));
			address.setZip(req.getParameter("zip"));
			address.setPhone(req.getParameter("phone"));
			userService.updateAddress(address);
		}
	}
	 /**
     * Updates user's payment information.
     */
	private void updatePaymentInformation(HttpServletRequest req, User user) throws Exception {
		Payment payment = userService.getPaymentById(user.getPaymentid());
		if (payment != null) {
			payment.setCard_number(Integer.parseInt(req.getParameter("card_number")));
			payment.setPin(Integer.parseInt(req.getParameter("pin")));
			userService.updatePayment(payment);
		}
	}
}
