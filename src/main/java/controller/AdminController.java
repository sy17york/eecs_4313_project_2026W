package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.order.Address;
import model.order.Order;
import model.order.Payment;
import model.product.Category;
import model.product.Item;
import model.user.User;
import service.AdminService;
/**
 * AdminController - Handles all administrative actions like managing inventory,
 * sales history, and user information.
 */
@WebServlet("/AdminController")
@MultipartConfig
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L; // serialVersionUID for Serializable class
	private AdminService adminService;
	/**
     * Initializes the AdminService with the database path.
     */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String dbPath = config.getServletContext().getRealPath("/WEB-INF/db/projectDB.db");

		System.out.println("Database path in: " + dbPath);
		 File dbFile = new File(dbPath);
		    if (!dbFile.exists()) {
		        throw new ServletException("Database not found. Please ensure the database file is manually copied to: " + dbPath);
		    }
		this.adminService = new AdminService(dbPath);

		    try {
				if (adminService.getAllCategories()==null) {
				    throw new ServletException("Database not found. Please ensure the database file is manually copied to: " + dbPath);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//login there?
	}
	 /**
     * Handles GET requests for various administrative actions.
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		resp.setContentType("text/html");


		try {
			if ("viewSalesHistory".equals(action)) {
				 List<Order> orders = adminService.getSalesHistory();
				 for (Order order : orders) {
					    System.out.println("Order ID: " + order.getOrderId());
					    System.out.println("Customer: " + (order.getCustomer() != null ? order.getCustomer().getUsername() : "null"));
					    System.out.println("Order Date: " + order.getOrderDate());
					    System.out.println("Total Amount: " + order.getTotalAmount());
					}
		         req.setAttribute("sales", orders);
		         req.setAttribute("message", req.getParameter("message"));

		         req.getRequestDispatcher("/jsp/partials/salesHistoryPartial.jsp").forward(req, resp);


			} else if ("filterSales".equals(action)) {

			        String username = req.getParameter("username") != null && !req.getParameter("username").isEmpty()
			                ? req.getParameter("username")
			                : null;

			        String productName = req.getParameter("productName") != null && !req.getParameter("productName").isEmpty()
			                ? req.getParameter("productName")
			                : null;

			        String date1Str = req.getParameter("date1");
			        String date2Str = req.getParameter("date2");
			        java.sql.Date startDate = null;
			        java.sql.Date endDate = null;

			        if (date1Str != null && !date1Str.isEmpty()) {
			            java.util.Date utilDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(date1Str);
			            startDate = new java.sql.Date(utilDate1.getTime());
			        }
			        if (date2Str != null && !date2Str.isEmpty()) {
			            java.util.Date utilDate2 = new SimpleDateFormat("yyyy-MM-dd").parse(date2Str);
			            endDate = new java.sql.Date(utilDate2.getTime());
			        }

			        // Fetch filtered sales data from the service
			        System.out.println("Filter Params - Username: " + username + ", Product: " + productName
			        	    + ", StartDate: " + startDate + ", EndDate: " + endDate);
			        List<Order> filteredOrders = adminService.filterSalesHistoryByCriteria(username, productName, startDate, endDate);

			        req.setAttribute("sales", filteredOrders);
			        req.getRequestDispatcher("/jsp/partials/salesHistoryPartial.jsp").forward(req, resp);



			} else if ("viewOrderDetails".equals(action)) {
				Long orderId = Long.parseLong(req.getParameter("orderId"));
				Order order = adminService.getOrderDetails(orderId);

				req.setAttribute("order", order);
				req.getRequestDispatcher("/jsp/partials/orderDetailsPartial.jsp").forward(req, resp);
			} else if ("viewInventory".equals(action)) {
				 HttpSession session = req.getSession();

				    // Retrieve message from session and set it as a request attribute for JSP
				    String message = (String) session.getAttribute("message");
				    if (message != null) {
				        req.setAttribute("message", message);
				        session.removeAttribute("message"); // Clear the message after retrieving it
				    }
				req.setAttribute("inventory", adminService.getInventory());
				req.setAttribute("categories", adminService.getAllCategories());

				req.getRequestDispatcher("/jsp/partials/inventoryPartial.jsp").forward(req, resp);

			} else if ("manageUsers".equals(action)) {
				req.setAttribute("users", adminService.getAllUsers());

				req.getRequestDispatcher("/jsp/partials/manageUsersPartial.jsp").forward(req, resp);
			} else if ("viewUserDetails".equals(action)) {
				Long userId = Long.parseLong(req.getParameter("userId"));
				User user = adminService.getUserById(userId);
				List<Order> orders = adminService.getUserPurchaseHistory(userId);
				Address address = adminService.getUserAddress(userId);
				Payment payment = adminService.getUserPayment(userId);

				req.setAttribute("user", user);
				req.setAttribute("orders", orders);
				req.setAttribute("address", address);
				req.setAttribute("payment", payment);
				req.getRequestDispatcher("/jsp/partials/userDetailsPartial.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 /**
     * Handles POST requests for actions like updating user info, inventory, or uploading images.
     */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		resp.setContentType("text/html");
		System.out.println("Action: " + action);

		try {
			if ("updateUserInfo".equals(action)) {
				Long userId = Long.parseLong(req.getParameter("userId"));
				String email = req.getParameter("email");
				String username = req.getParameter("username");
				adminService.updateUserInfo(userId, email, username);

				req.setAttribute("message", "User information updated successfully!");
				resp.sendRedirect("AdminController?action=manageUsers");

			} else if ("updateAddress".equals(action)) {
				Long userId = Long.parseLong(req.getParameter("userId"));
				String street = req.getParameter("street");
				String province = req.getParameter("province");
				String country = req.getParameter("country");
				String zip = req.getParameter("zip");
				String phone = req.getParameter("phone");

				Address address = new Address(null, street, province, country, zip, phone);
				adminService.updateUserAddress(userId, address);

				req.setAttribute("message", "Address updated successfully!");
				resp.sendRedirect("AdminController?action=viewUserDetails&userId=" + userId);

			} else if ("updatePayment".equals(action)) {
				Long userId = Long.parseLong(req.getParameter("userId"));
				int cardNumber = Integer.parseInt(req.getParameter("cardNumber"));
				int pin = Integer.parseInt(req.getParameter("pin"));

				Payment payment = new Payment(null, cardNumber, pin);
				adminService.updateUserPayment(userId, payment);

				req.setAttribute("message", "Payment information updated successfully!");
				resp.sendRedirect("AdminController?action=viewUserDetails&userId=" + userId);
			} else if ("updateInventory".equals(action)) {
				// Handle updating inventory
				Long itemId = Long.parseLong(req.getParameter("id"));
				int quantity = Integer.parseInt(req.getParameter("quantity"));
				adminService.updateInventory(itemId, quantity);
				  HttpSession session = req.getSession();
				    session.setAttribute("message", "Inventory updated successfully!");
				resp.sendRedirect("AdminController?action=viewInventory");

			} else if ("uploadImage".equals(action)) {
				// Get item ID
				int itemId = Integer.parseInt(req.getParameter("itemId"));

				// Get the uploaded file
				Part filePart = req.getPart("itemImage");

				String fileType = filePart.getContentType();
			    if (!"image/jpeg".equals(fileType)) {
			        req.setAttribute("message", "Only JPG files are allowed.");
			        resp.sendRedirect("AdminController?action=viewInventory");
			        return;
			    }

			    // Check file extension
			    String submittedFileName = filePart.getSubmittedFileName();
			    if (!submittedFileName.toLowerCase().endsWith(".jpg")) {
			        req.setAttribute("message", "Only JPG files are allowed.");
			        resp.sendRedirect("AdminController?action=viewInventory");
			        return;
			    }

				String fileName = itemId + ".jpg";

				// Save the file to the 'images' directory
				String uploadPath = getServletContext().getRealPath("/images");
				File file = new File(uploadPath, fileName);
				try (InputStream fileContent = filePart.getInputStream();
						OutputStream outputStream = new FileOutputStream(file)) {
					fileContent.transferTo(outputStream);
				}

				HttpSession session = req.getSession();
			    session.setAttribute("message", "Image uploaded successfully!");
				resp.sendRedirect("AdminController?action=viewInventory");

			} else if ("insertItem".equals(action)) {
				// Gather input data
				String name = req.getParameter("name");
				String description = req.getParameter("description");
				String categoryName = req.getParameter("categoryName").toLowerCase();
				String brand = req.getParameter("brand").toUpperCase();
				int stockQuantity = Integer.parseInt(req.getParameter("stockQuantity"));
				double price = Double.parseDouble(req.getParameter("price"));

								Category category = null;
		        if (categoryName != null && !categoryName.isEmpty()) {
		            category = adminService.getCategoryByName(categoryName);
		            if (category == null) {
		                category = new Category();
		                category.setCategoryDescription(categoryName);
		                adminService.addCategory(category); // Add new category
		            }
		        } else {
		            String categoryId = req.getParameter("categoryId");
		            if (categoryId != null && !categoryId.isEmpty()) {
		                category = adminService.getCategorieById(Long.parseLong(categoryId));
		            }
		        }

		        if (category == null) {
		            throw new Exception("Category is required.");
		        }

				Item item = new Item(null, name, description, price, stockQuantity, category, brand);
				// Insert item into the database

				adminService.insertItem(item);
				System.out.println("Inserting Item: " + item.getItemDetails());

				HttpSession session = req.getSession();
			    session.setAttribute("message", "Item added successfully!");
				resp.sendRedirect("AdminController?action=viewInventory");

			} else if ("removeItem".equals(action)) {
				// Remove item by ID
				Long itemId = Long.parseLong(req.getParameter("itemId"));
				adminService.removeItem(itemId);

				// Remove associated image
				String uploadPath = getServletContext().getRealPath("/images");
				File file = new File(uploadPath, itemId + ".jpg");
				if (file.exists()) {
					file.delete();
				}

				HttpSession session = req.getSession();
			    session.setAttribute("message", "Item removed successfully!");
				resp.sendRedirect("AdminController?action=viewInventory");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
