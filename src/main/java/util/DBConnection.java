package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection is a utility class responsible for establishing
 * a connection to the SQLite database using JDBC.
 */
public class DBConnection {
	  /**
     * Establishes a connection to the SQLite database located at the specified path.
     *
     * @param dbPath The file path to the SQLite database.
     * @return A Connection object if the connection is successful; null otherwise.
     */
	public static Connection getConnection(String dbPath) {
		try {
			String url = "jdbc:sqlite:" + dbPath;// Build the JDBC connection string for SQLite

			 // Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			return DriverManager.getConnection(url);// Establish and return the database connection
		} catch (SQLException e) {
			 System.err.println("Database connection error: " + e.getMessage());
		        e.printStackTrace();
		        return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;// Return null in case of any failure
	}
}
