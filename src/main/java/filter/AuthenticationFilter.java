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
/**
 * AuthenticationFilter ensures that only authenticated users can access protected resources.
 * It checks for the presence of a user session and redirects unauthenticated users to the login page.
 */
public class AuthenticationFilter implements Filter {
	 /**
     * Initializes the filter. Called once during filter creation.
     *
     * @param filterConfig Configuration information for this filter.
     * @throws ServletException If an exception occurs during initialization.
     */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization if needed
	}
	/**
     * Performs filtering to check if a user is authenticated.
     *
     * @param request  The incoming ServletRequest object.
     * @param response The outgoing ServletResponse object.
     * @param chain    The FilterChain to pass the request and response further down the chain.
     * @throws IOException      If an input/output error occurs during processing.
     * @throws ServletException If a servlet exception occurs during processing.
     */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		// Check if user is logged in
		if (session == null || session.getAttribute("user") == null) {
			// Redirect to login page if not logged in
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/AuthenticationController?action=loginPage");
			return;
		}

		// Continue with the request if logged in
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Cleanup if needed
	}
}
