package controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.order.Address;
import model.order.Payment;
import model.user.User;
import service.AuthenticationService;
/**
 * AuthenticationController handles user login, registration, and logout actions.
 * It extends HttpServlet and processes HTTP GET and POST requests.
 */
@WebServlet("/AuthenticationController")
public class AuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthenticationService authService;
	/**
     * Initialize the servlet and configure the authentication service.
     *
     * @param config ServletConfig to fetch configuration details.
     */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String dbPath = config.getServletContext().getRealPath("/WEB-INF/db/projectDB.db");
		this.authService = new AuthenticationService(dbPath);
	}
	 /**
     * Handles HTTP GET requests.
     * Manages navigation to login page and logout actions.
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		 // Navigate to the login and registration page
		if ("loginPage".equals(action) || action == null) {
			req.getRequestDispatcher("/jsp/loginAndRegister.jsp").forward(req, resp);
		} else if ("logout".equals(action)) {// Invalidate the session for logout
			HttpSession session = req.getSession(false);
			if (session != null) {
				session.invalidate(); // Invalidate session on logout
			}
			resp.sendRedirect("ItemController");
		}
	}
	 /**
     * Handles HTTP POST requests.
     * Manages login and registration functionality.
     */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");

		try {
			if ("login".equals(action)) {// Retrieve username and password from request parameters
				String username = req.getParameter("username");
				String password = req.getParameter("password");

				User user = authService.loginUser(username, password);
				if (user != null) {// Store user information in session upon successful login
					HttpSession session = req.getSession();
					session.setAttribute("user", user); // Store user in session
					resp.sendRedirect("ItemController"); // Redirect to home page
				} else {
					req.setAttribute("error", "Invalid username or password.");
					req.getRequestDispatcher("/jsp/loginAndRegister.jsp").forward(req, resp);
				}
			} else if ("register".equals(action)) { // Handle user registration
				String username = req.getParameter("username");
				if(authService.ifExistsUser(username)) {// Check if the username already exists
					req.setAttribute("message", "username is exists! Please try again.");
					req.getRequestDispatcher("/jsp/loginAndRegister.jsp").forward(req, resp);
				}else {
					// Collect registration details
					String email = req.getParameter("email");
					String password = req.getParameter("password");
					// Create Address object from user input
					Address address = new Address(null, req.getParameter("street"), req.getParameter("province"),
							req.getParameter("country"), req.getParameter("zip"), req.getParameter("phone"));
					// Collect and parse payment information
						int cardNumber = Integer.parseInt(req.getParameter("card_number"));
						int pin = Integer.parseInt(req.getParameter("pin"));
						Payment payment = new Payment(null, cardNumber, pin);
						// Register the user
						if (authService.registerUser(username, email, password, address, payment)) {
							req.setAttribute("message", "Registration successful! Please log in.");
							req.getRequestDispatcher("/jsp/loginAndRegister.jsp").forward(req, resp);
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging

		}
	}
}
