package controller;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.product.Category;
import model.product.Item;
import service.CategoryService;
import service.ItemService;
/**
 * ItemController handles actions related to items, such as displaying,
 * searching, sorting, filtering, and viewing item details.
 */
@WebServlet("/ItemController")
public class ItemController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Services to handle item and category operations
	private ItemService itemService;
	private CategoryService categoryService;
	 /**
     * Initializes the servlet and services by loading the database path.
     *
     * @param config ServletConfig to fetch servlet-specific configuration.
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
		this.itemService = new ItemService(dbPath);
		this.categoryService = new CategoryService(dbPath);
		try {
			if (itemService.getAllBrands()==null||categoryService.getAllCate()==null) {
			    throw new ServletException("Database not found. Please ensure the database file is manually copied to: " + dbPath);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 /**
     * Handles HTTP GET requests for different actions such as display, search,
     * sorting, and filtering of items.
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 // Retrieve the action parameter from the request
		try {
			// Determine the action
			String action = req.getParameter("action");
			List<Item> items = null;
			List<Category> categories = categoryService.getAllCate();
			List<String> brands = itemService.getAllBrands();
			// Default action: Load all items
			if (action == null) { // Default action: Load all items if no action is specified
				items = itemService.getAllItems();
				req.setAttribute("items", items);

			}else if ("search".equals(action)) {
				 // Handle search by keyword
				String keyword = req.getParameter("keyword");
				items = itemService.searchItemByKeyword(keyword);
				req.setAttribute("items", items);
			} else if ("sort".equals(action)) {
				 // Handle sorting based on the "sortBy" parameter
				String sortBy = req.getParameter("sortBy");
				items = itemService.getAllItems();
				items.sort(sortBy != null ? getComparator(sortBy) : Comparator.comparing(Item::getItemId));
				req.setAttribute("items", items);
			} else if ("filtercate".equals(action)) {
				// Handle filtering by category

			        String categoryIdStr = req.getParameter("categoryId");
			        if (categoryIdStr == null) {
			            throw new IllegalArgumentException("Category ID is required for filtering.");
			        }
			        Long categoryId = Long.parseLong(categoryIdStr);
			        items = itemService.searchItemByCategory(categoryId);
			        req.setAttribute("items", items);

			}else if ("filterbrand".equals(action)) {
				// Handle filtering by brand

			        String brandStr = req.getParameter("brand");
			        if (brandStr == null) {
			            throw new IllegalArgumentException("brand is required for filtering.");
			        }

			        items = itemService.searchItemByBrand(brandStr);
			        req.setAttribute("items", items);

			}else if ("viewItemDetails".equals(action)) {
				// Handle viewing item details

				 String itemIdStr = req.getParameter("itemId");
			        if (itemIdStr == null) {
			            throw new IllegalArgumentException("item ID is required for detail.");
			        }
			        Long itemId = Long.parseLong(itemIdStr);
			        Item item = itemService.getItemById(itemId);
			        req.setAttribute("itemDetails", item);

		}


			req.setAttribute("categories", categories);
			req.setAttribute("brands", brands);
			req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);


		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging

		}
	}
	 /**
     * Returns a Comparator<Item> based on the sorting criteria.
     *
     * @param sortBy Sorting option provided in the request.
     * @return Comparator for sorting items.
     */
	private Comparator<Item> getComparator(String sortBy) {
		switch (sortBy) {
		case "category":
			return Comparator.comparing(Item::getCategoryId);
		case "brand":
			return Comparator.comparing(Item::getBrand);
		case "price":
			return Comparator.comparing(Item::getPrice);
		case "priceDesc":
            return Comparator.comparing(Item::getPrice).reversed();
        case "name":
            return Comparator.comparing(Item::getName);
		default:
			throw new IllegalArgumentException("Invalid sort option");
		}
	}
}
