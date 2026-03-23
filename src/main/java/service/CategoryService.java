package service;

import java.util.List;

import dao.CategoryDAOImpl;
import model.product.Category;
/**
 * CategoryService provides a layer of abstraction for managing product categories.
 * It interacts with the Category DAO (Data Access Object) to perform CRUD operations.
 */
public class CategoryService {
	private final CategoryDAOImpl cateDAO;
	 /**
     * Initializes CategoryService with a database path.
     *
     * @param dbPath The path to the SQLite database.
     */
	public CategoryService(String dbPath) {
		this.cateDAO = new CategoryDAOImpl(dbPath);
	}
	  /**
     * Retrieves all categories from the database.
     *
     * @return A list of all Category objects.
     * @throws Exception If any database or SQL errors occur.
     */
	public List<Category> getAllCate() throws Exception {
		return cateDAO.getAll();
	}
	/**
     * Searches for a specific category by its ID.
     *
     * @param cateId The ID of the category to search for.
     * @return The Category object if found, otherwise null.
     * @throws Exception If any database or SQL errors occur.
     */
	public Category searchById(Long cateId) throws Exception {
		return cateDAO.searchById(cateId);
	}
	/**
     * Adds a new category to the database.
     *
     * @param cate The Category object to insert.
     * @throws Exception If any database or SQL errors occur.
     */
	public void addCate(Category cate) throws Exception {
		cateDAO.insert(cate);
	}

    /**
     * Updates an existing category in the database.
     *
     * @param item The Category object containing updated details.
     * @throws Exception If any database or SQL errors occur.
     */
	public void updateCate(Category item) throws Exception {
		cateDAO.update(item);
	}
	  /**
     * Removes a category from the database by its ID.
     *
     * @param cateId The ID of the category to remove.
     * @throws Exception If any database or SQL errors occur.
     */
	public void removeCate(Long cateId) throws Exception {
		cateDAO.removeById(cateId);
	}
}
