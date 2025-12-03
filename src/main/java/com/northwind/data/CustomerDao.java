// This line tells Java which "folder" (package) this code belongs to
// Think of packages like organizing files on your computer into folders
package com.northwind.data;

// These are "import" statements - they're like telling Java "I need to use these tools"
// Just like you might say "I need a hammer and screwdriver" before starting a project
import com.northwind.model.Customer;  // This imports the Customer class (a blueprint for customer objects)
import javax.sql.DataSource;           // This helps us connect to a database
import java.sql.Connection;            // This represents an active connection to the database
import java.sql.PreparedStatement;     // This helps us safely send commands to the database
import java.sql.ResultSet;             // This holds the results we get back from the database
import java.sql.SQLException;          // This helps us handle database errors
import java.util.ArrayList;            // This is a flexible list that can grow and shrink
import java.util.List;                 // This is the general concept of a list

// DAO stands for "Data Access Object"
// This class is responsible for talking to the database and getting customer information
// Think of it as a librarian who fetches books (data) from the library (database) for you
public class CustomerDao {

    // This is a "field" or "instance variable" - it's data that belongs to this object
    // DataSource is like having the address and key to the database
    // The "private" keyword means only this class can directly access it (it's private!)
    private DataSource dataSource;

    // This is a "constructor" - it runs when you create a new CustomerDao object
    // It's like initializing/setting up the object when it's born
    // The constructor takes a DataSource as input and stores it for later use
    public CustomerDao(DataSource dataSource) {
        // "this.dataSource" refers to the field above
        // "dataSource" (without "this") refers to the parameter we received
        // This line says: "Store the dataSource I received into my field for later"
        this.dataSource = dataSource;
    }

    // This is a "method" (a function that belongs to a class)
    // It retrieves ALL customers from the database and returns them as a list
    // "public" means anyone can call this method
    // "List<Customer>" means it returns a list of Customer objects
    // "getAll()" is the method name with empty parentheses (no parameters needed)
    public List<Customer> getAll() {

        // Create an empty ArrayList to store all the customers we'll find
        // It starts empty, but we'll add customers to it as we find them
        List<Customer> customers = new ArrayList<>();

        // This is the SQL query - a command we'll send to the database
        // The three quotes (""") let us write the query on multiple lines for readability
        // SQL is like asking the database a question: "Give me all this information from the Customers table"
        String query = """
                SELECT CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax
                FROM Customers;
                """;

        // This is a "try-with-resources" block - it automatically closes resources when done
        // Think of it like borrowing a book from the library and automatically returning it when finished
        // Even if something goes wrong, the book gets returned!
        try (Connection connection = dataSource.getConnection();  // Get a connection to the database
             PreparedStatement statement = connection.prepareStatement(query)) {  // Prepare our SQL query

            // Execute the query and get back a ResultSet (the results from the database)
            // ResultSet is like a spreadsheet with rows and columns of data
            try (ResultSet resultSet = statement.executeQuery()) {

                // "while" loop: keep going as long as there are more rows to read
                // resultSet.next() moves to the next row and returns true if there is one
                // It's like reading a book page by page - move to next page, is there one? Yes? Keep reading.
                while (resultSet.next()) {

                    // Create a new Customer object using data from the current row
                    // resultSet.getString("CustomerID") gets the value from the "CustomerID" column
                    // We're pulling out each piece of information and using it to build a Customer object
                    Customer customer = new Customer(
                            resultSet.getString("CustomerID"),      // Get the customer's ID
                            resultSet.getString("CompanyName"),     // Get the company name
                            resultSet.getString("ContactName"),     // Get the contact person's name
                            resultSet.getString("ContactTitle"),    // Get their job title
                            resultSet.getString("Address"),         // Get the street address
                            resultSet.getString("City"),            // Get the city
                            resultSet.getString("Region"),          // Get the region/state
                            resultSet.getString("PostalCode"),      // Get the zip/postal code
                            resultSet.getString("Country"),         // Get the country
                            resultSet.getString("Phone"),           // Get the phone number
                            resultSet.getString("Fax"));            // Get the fax number

                    // Add this customer to our list
                    // We're building up our collection one customer at a time
                    customers.add(customer);
                }
            }

            // "catch" block: this runs if something goes wrong (an exception occurs)
            // SQLException means something went wrong with the database operation
        } catch (SQLException e) {
            // Print a user-friendly error message to the console
            System.out.println("There was an error retrieving the data. Please try again.");

            // Print the technical error details (helpful for developers debugging)
            // "e" is the exception object that contains information about what went wrong
            e.printStackTrace();
        }

        // Return the list of customers (could be empty if there were no customers or an error occurred)
        // This is what gets sent back to whoever called this method
        return customers;
    }
}