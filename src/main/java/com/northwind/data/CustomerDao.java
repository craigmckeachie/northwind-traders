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
// This class is responsible for talking to the database and managing customer information
// Think of it as a librarian who can:
// - Show you all books (getAll)
// - Find a specific book (find)
// - Add a new book (add)
// - Update book information (update)
// - Remove a book (delete)
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

    // METHOD 1: GET ALL CUSTOMERS
    // This method retrieves ALL customers from the database and returns them as a list
    // It's like asking "Show me everyone in your database"
    public List<Customer> getAll() {

        // Create an empty ArrayList to store all the customers we'll find
        // It starts empty, but we'll add customers to it as we find them
        List<Customer> customers = new ArrayList<>();

        // This is the SQL query - a command we'll send to the database
        // SQL is like asking the database: "Give me all this information from the Customers table"
        String query = """
                SELECT CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax
                FROM Customers;
                """;

        // Try-with-resources: automatically closes database connections when done
        // Like borrowing a book and automatically returning it, even if you drop it!
        try (Connection connection = dataSource.getConnection();  // Open the door to the database
             PreparedStatement statement = connection.prepareStatement(query)) {  // Prepare our question

            // Execute the query and get back a ResultSet (a table of results)
            try (ResultSet resultSet = statement.executeQuery()) {

                // Loop through each row in the results
                // "while resultSet.next()" means "while there's another row, move to it"
                while (resultSet.next()) {

                    // Build a Customer object from the current row's data
                    // It's like filling out a form with information from each column
                    Customer customer = new Customer(
                            resultSet.getString("CustomerID"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("ContactName"),
                            resultSet.getString("ContactTitle"),
                            resultSet.getString("Address"),
                            resultSet.getString("City"),
                            resultSet.getString("Region"),
                            resultSet.getString("PostalCode"),
                            resultSet.getString("Country"),
                            resultSet.getString("Phone"),
                            resultSet.getString("Fax"));

                    // Add this customer to our list
                    customers.add(customer);
                }
            }

        } catch (SQLException e) {
            // If something goes wrong, print a friendly error message
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();  // Print technical details for developers
        }

        // Return the list (might be empty if no customers exist or an error occurred)
        return customers;
    }

    // METHOD 2: FIND ONE CUSTOMER BY ID
    // This searches for a specific customer using their unique ID
    // It's like looking up a specific person in a phone book
    public Customer find(String customerId) {

        // Start with null (nothing) - we'll replace this if we find the customer
        // null is like an empty box - it represents "no value yet"
        Customer customer = null;

        // SQL query with a ? placeholder - this is for security!
        // The ? is like a fill-in-the-blank that we'll safely fill in later
        // WHERE CustomerID = ? means "only get the row where the ID matches what I specify"
        String query = """
                SELECT CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax
                FROM Customers
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the ? placeholder with the actual customer ID
            // The "1" means "the first placeholder" (in case there were multiple)
            // This is safer than putting the ID directly in the query (prevents SQL injection attacks)
            statement.setString(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                // "if" instead of "while" because we expect at most ONE result
                // Customer IDs are unique, so there should only be one match
                if (resultSet.next()) {
                    // We found them! Build the Customer object
                    customer = new Customer(
                            resultSet.getString("CustomerID"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("ContactName"),
                            resultSet.getString("ContactTitle"),
                            resultSet.getString("Address"),
                            resultSet.getString("City"),
                            resultSet.getString("Region"),
                            resultSet.getString("PostalCode"),
                            resultSet.getString("Country"),
                            resultSet.getString("Phone"),
                            resultSet.getString("Fax"));
                }
                // If resultSet.next() is false, customer stays null (customer not found)
            }

        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        // Return the customer (or null if not found)
        return customer;
    }

    // METHOD 3: ADD A NEW CUSTOMER
    // This inserts a brand new customer into the database
    // It's like adding a new contact to your phone
    public Customer add(Customer customer) {

        // INSERT INTO means "add a new row to this table"
        // The ? marks are placeholders for all the customer's information
        // We have 11 placeholders because we're inserting 11 pieces of information
        String query = """
                INSERT INTO Customers (CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in each ? placeholder with data from the customer object
            // Think of it like filling out a form, one field at a time
            // The number (1, 2, 3...) corresponds to which ? we're filling in (left to right)
            statement.setString(1, customer.getCustomerId());      // 1st ? gets the ID
            statement.setString(2, customer.getCompanyName());     // 2nd ? gets the company name
            statement.setString(3, customer.getContactName());     // 3rd ? gets the contact name
            statement.setString(4, customer.getContactTitle());    // And so on...
            statement.setString(5, customer.getAddress());
            statement.setString(6, customer.getCity());
            statement.setString(7, customer.getRegion());
            statement.setString(8, customer.getPostalCode());
            statement.setString(9, customer.getCountry());
            statement.setString(10, customer.getPhone());
            statement.setString(11, customer.getFax());            // 11th ? gets the fax

            // executeUpdate() runs the INSERT command
            // Unlike executeQuery() which retrieves data, executeUpdate() changes data
            // It's like pressing the "Save" button
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error adding the customer. Please try again.");
            e.printStackTrace();
        }

        // Return the customer object we just added
        return customer;
    }

    // METHOD 4: UPDATE AN EXISTING CUSTOMER
    // This modifies information for a customer that already exists in the database
    // It's like editing a contact in your phone
    public void update(Customer customer) {

        // UPDATE means "modify an existing row"
        // SET specifies which columns to change and to what values
        // WHERE specifies which customer to update (using their ID)
        // Without WHERE, it would update EVERY customer - that would be bad!
        String query = """
                UPDATE Customers
                SET CompanyName = ?, ContactName = ?, ContactTitle = ?, Address = ?, City = ?, Region = ?, PostalCode = ?, Country = ?, Phone = ?, Fax = ?
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the placeholders with the customer's updated information
            // Notice: CustomerID comes LAST (position 11) because it's in the WHERE clause at the end
            statement.setString(1, customer.getCompanyName());     // Update company name
            statement.setString(2, customer.getContactName());     // Update contact name
            statement.setString(3, customer.getContactTitle());    // Update contact title
            statement.setString(4, customer.getAddress());         // Update address
            statement.setString(5, customer.getCity());            // Update city
            statement.setString(6, customer.getRegion());          // Update region
            statement.setString(7, customer.getPostalCode());      // Update postal code
            statement.setString(8, customer.getCountry());         // Update country
            statement.setString(9, customer.getPhone());           // Update phone
            statement.setString(10, customer.getFax());            // Update fax
            statement.setString(11, customer.getCustomerId());     // Which customer to update (WHERE clause)

            // Execute the update - save the changes to the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error updating the customer. Please try again.");
            e.printStackTrace();
        }

        // This method returns nothing (void) - it just does the update
    }

    // METHOD 5: DELETE A CUSTOMER
    // This permanently removes a customer from the database
    // It's like deleting a contact from your phone - be careful!
    public void delete(String customerId) {

        // DELETE FROM means "remove a row from this table"
        // WHERE specifies which customer to delete
        // Without WHERE, it would delete ALL customers - disaster!
        String query = """
                DELETE FROM Customers
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in which customer to delete
            statement.setString(1, customerId);

            // Execute the deletion - the customer is now gone from the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error deleting the customer. Please try again.");
            e.printStackTrace();
        }

        // This method returns nothing (void) - it just performs the deletion
    }
}

// SUMMARY OF THIS CLASS (CRUD OPERATIONS):
// This class provides 5 methods that cover all basic database operations:
//
// C - CREATE: add() creates a new customer
// R - READ:   getAll() reads all customers, find() reads one specific customer
// U - UPDATE: update() modifies an existing customer
// D - DELETE: delete() removes a customer
//
// These are called "CRUD operations" and are fundamental to almost every database application!
// Think of this class as your customer database toolkit - everything you need to manage customers.