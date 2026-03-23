package controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.cart.CartItem;
import model.cart.ShoppingCart;
import model.product.Item;
import service.ItemService;
/**
 * ShoppingCartController handles operations related to the shopping cart,
 * including adding, updating, removing items, and viewing the cart.
 */
@WebServlet("/ShoppingCartController")
public class ShoppingCartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ItemService itemService;
	 /**
     * Initializes the servlet and sets up the ItemService with the database path.
     */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String dbPath = config.getServletContext().getRealPath("/WEB-INF/db/projectDB.db");
		this.itemService = new ItemService(dbPath);
	}
	/**
     * Handles POST requests for adding, updating, and removing items from the cart.
     */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		ShoppingCart cart;

		synchronized (session) { // Retrieve or create a new ShoppingCart object
			cart = (ShoppingCart) session.getAttribute("cart");
			if (cart == null) {
				cart = new ShoppingCart();
				session.setAttribute("cart", cart);
			}
		}

		String action = req.getParameter("action");

		try {
			if ("add".equals(action)) {// Add an item to the cart
				Long itemId = Long.parseLong(req.getParameter("itemId"));
				int quantity = Integer.parseInt(req.getParameter("quantity"));
				Item item = itemService.getItemById(itemId);
				if (item != null) {
					cart.addItem(new CartItem(item, quantity));
				}

			} else if ("update".equals(action)) { // Update the quantity of an existing item
				Long itemId = Long.parseLong(req.getParameter("itemId"));
				int newQuantity = Integer.parseInt(req.getParameter("quantity"));
				Item item = itemService.getItemById(itemId);
				if (item != null) {
					cart.update(new CartItem(item, newQuantity), newQuantity);
				}

			} else if ("remove".equals(action)) {// Remove an item from the cart
				Long itemId = Long.parseLong(req.getParameter("itemId"));
				Item item = itemService.getItemById(itemId);
				if (item != null) {
					cart.remove(item);
				}
			}
			// Redirect back to the cart view after modification
			resp.sendRedirect("ShoppingCartController?action=viewCart");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
     * Handles GET requests for viewing the shopping cart.
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		ShoppingCart cart = (session != null) ? (ShoppingCart) session.getAttribute("cart") : null;

		if ("viewCart".equals(req.getParameter("action"))) {// Pass the shopping cart to the JSP for display
			req.setAttribute("cart", cart);
			req.getRequestDispatcher("/jsp/cart.jsp").forward(req, resp);
		}
	}
}
