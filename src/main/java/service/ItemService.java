package service;

import java.sql.SQLException;
import java.util.List;

import dao.ItemDAOImpl;
import model.product.Item;
/**
 * ItemService provides a layer of abstraction for managing product items.
 * It interacts with the Item DAO (Data Access Object) to perform CRUD operations
 * and search/filter functionalities.
 */
public class ItemService {
	private final ItemDAOImpl itemDAO;// DAO for interacting with Item data
	/**
     * Initializes ItemService with a database path.
     *
     * @param dbPath The path to the SQLite database.
     */
	public ItemService(String dbPath) {
		this.itemDAO = new ItemDAOImpl(dbPath);
	}

    /**
     * Retrieves all items from the database.
     *
     * @return A list of all Item objects.
     * @throws Exception If any database or SQL errors occur.
     */
	public List<Item> getAllItems() throws Exception {
		return itemDAO.getAll();
	}
	/**
     * Retrieves a specific item by its ID.
     *
     * @param itemId The ID of the item to retrieve.
     * @return The Item object if found, otherwise null.
     * @throws Exception If any database or SQL errors occur.
     */
	public Item getItemById(Long itemId) throws Exception {
		return itemDAO.searchById(itemId);
	}
	/**
     * Searches for items that match a given keyword in their name, description,
     * brand, or category.
     *
     * @param keyword The search keyword.
     * @return A list of matching Item objects.
     * @throws Exception If any database or SQL errors occur.
     */
	public List<Item> searchItemByKeyword(String keyword) throws Exception {
		return itemDAO.searchByKeyword(keyword);
	}
	/**
     * Searches for items within a specific category.
     *
     * @param categoryId The ID of the category.
     * @return A list of items belonging to the specified category.
     * @throws Exception If any database or SQL errors occur.
     */
	public List<Item> searchItemByCategory(Long categoryId) throws Exception {
		return itemDAO.searchByCategory(categoryId);
	}

    /**
     * Updates an existing item in the database.
     *
     * @param item The Item object containing updated details.
     * @throws Exception If any database or SQL errors occur.
     */
	public void updateItem(Item item) throws Exception {
		itemDAO.update(item);
	}
	 /**
     * Searches for items by a specific brand name.
     *
     * @param brandStr The name of the brand to filter by.
     * @return A list of items belonging to the specified brand.
     * @throws Exception If any database or SQL errors occur.
     */
	public List<Item> searchItemByBrand(String brandStr) throws Exception {
		return itemDAO.searchByBrand(brandStr);


	}
	/**
     * Retrieves all distinct brand names from the database.
     *
     * @return A list of all unique brand names as Strings.
     * @throws SQLException If any database errors occur.
     */
	public List<String> getAllBrands() throws SQLException {
		return itemDAO.getAllBrands();
	}

}
