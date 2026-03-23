package dao;

import java.util.List;
/**
 * DAO is a generic interface defining standard CRUD operations for data access objects.
 *
 * @param <T> The type of object that this DAO will manage.
 */
public interface DAO<T> {
	 /**
     * Searches for an object by its unique ID.
     *
     * @param id The unique identifier of the object.
     * @return The object if found, otherwise null.
     * @throws Exception If any database or query errors occur.
     */
	T searchById(Long id) throws Exception;
	 /**
     * Retrieves all objects of type T from the database.
     *
     * @return A list of all objects.
     * @throws Exception If any database or query errors occur.
     */
	List<T> getAll() throws Exception;
	/**
     * Inserts a new object into the database.
     *
     * @param e The object to insert.
     * @return true if the insertion is successful, false otherwise.
     * @throws Exception If any database or query errors occur.
     */
	boolean insert(T e) throws Exception;

    /**
     * Removes an object from the database using its unique ID.
     *
     * @param id The unique identifier of the object to remove.
     * @return true if the removal is successful, false otherwise.
     * @throws Exception If any database or query errors occur.
     */
	boolean removeById(Long id) throws Exception;
	/**
     * Updates an existing object in the database.
     *
     * @param e The object containing updated details.
     * @return true if the update is successful, false otherwise.
     * @throws Exception If any database or query errors occur.
     */
	boolean update(T e) throws Exception;
}
