package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.cart.CartItem;
import model.cart.ShoppingCart;
import service.ItemService;
/**
 * StockAvailabilityFilter ensures that all items in the shopping cart have sufficient stock
 * before proceeding to the next step in the order process.
 * If insufficient stock is detected, an error message is displayed to the user.
 */
public class StockAvailabilityFilter implements Filter {
	private ItemService itemService;
	/**
     * Initializes the filter and sets up the ItemService using the database path.
     *
     * @param filterConfig The filter configuration, used to retrieve the database path.
     * @throws ServletException If initialization fails.
     */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String dbPath = filterConfig.getServletContext().getRealPath("/WEB-INF/db/projectDB.db");
		this.itemService = new ItemService(dbPath);
	}
	/**
     * Performs filtering to validate stock availability for items in the shopping cart.
     *
     * Steps:
     * 1. Validate that the shopping cart exists in the user's session.
     * 2. Check the stock quantity for each cart item.
     * 3. If any item has insufficient stock, display an error message and stop processing.
     * 4. If all items are available, allow the request to proceed.
     *
     * @param request  The incoming ServletRequest object.
     * @param response The outgoing ServletResponse object.
     * @param chain    The FilterChain to pass the request and response further down the chain.
     * @throws IOException      If an input/output error occurs.
     * @throws ServletException If a servlet error occurs.
     */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(false);// Retrieve the session and the shopping cart

		if (session == null || session.getAttribute("cart") == null) {
			((HttpServletResponse) response).sendRedirect("ShoppingCartController?action=viewCart");// Redirect to cart view if no cart is found
			return;
		}

		ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
		try { // Iterate through all cart items to check stock availability
			for (CartItem cartItem : cart.getItems()) {
				int availableQuantity = itemService.getItemById(cartItem.getItem().getItemId()).getStockQuantity();
				if (cartItem.getQuantity() > availableQuantity) {// Insufficient stock: Set error message and forward to the cart page
					String errorMessage = "Insufficient stock for item: " + cartItem.getItem().getName()
							+ ". Available: " + availableQuantity + ", Requested: " + cartItem.getQuantity();
					request.setAttribute("error", errorMessage);
					request.getRequestDispatcher("/jsp/cart.jsp").forward(request, response);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(request, response); // If stock is sufficient for all items, proceed with the request
	}

	@Override
	public void destroy() {
		// Clean up resources if needed
	}
}
