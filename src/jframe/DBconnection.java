/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jframe;
 
import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
    // Static method to get a database connection
    public static Connection getConnection() {
        Connection con = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish the connection to the database
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/library management","root","");
        } catch (Exception e) {
            // Print the exception for debugging
            e.printStackTrace();
        }
        // Return the connection object
        return con;
    }
}
