// This line tells Java which "folder" (package) this code belongs to
// Think of packages like organizing files on your computer into folders
package com.northwind.data;

// These are "import" statements - they're like telling Java "I need to use these tools"
import com.northwind.model.Product;    // This imports the Product class (a blueprint for product objects)
import javax.sql.DataSource;           // This helps us connect to a database
import java.sql.Connection;            // This represents an active connection to the database
import java.sql.PreparedStatement;     // This helps us safely send commands to the database
import java.sql.ResultSet;             // This holds the results we get back from the database
import java.sql.SQLException;          // This helps us handle database errors
import java.sql.Statement;             // This is needed to retrieve auto-generated keys
import java.util.ArrayList;            // This is a flexible list that can grow and shrink
import java.util.List;                 // This is the general concept of a list

// DAO stands for "Data Access Object"
// This class is responsible for talking to the database and managing product information
// Think of it as a librarian who can:
// - Show you all books (getAll)
// - Find a specific book (find)
// - Add a new book (add)
// - Update book information (update)
// - Remove a book (delete)
public class ProductDao {

    // This is a "field" or "instance variable" - it's data that belongs to this object
    // DataSource is like having the address and key to the database
    // The "private" keyword means only this class can directly access it
    private DataSource dataSource;

    // This is a "constructor" - it runs when you create a new ProductDao object
    // It's like initializing/setting up the object when it's born
    // The constructor takes a DataSource as input and stores it for later use
    public ProductDao(DataSource dataSource) {
        // "this.dataSource" refers to the field above
        // "dataSource" (without "this") refers to the parameter we received
        // This line says: "Store the dataSource I received into my field for later"
        this.dataSource = dataSource;
    }

    // METHOD 1: GET ALL SHIPPERS
    // This method retrieves ALL products from the database and returns them as a list
    // It's like asking "Show me everyone in your database table"
    public List<Product> getAll() {

        // Create an empty ArrayList to store all the products we'll find
        // It starts empty, but we'll add products to it as we find them
        List<Product> products = new ArrayList<>();

        // This is the SQL query - a command we'll send to the database
        // SQL is like asking the database: "Give me all this information from the Products table"
        // Notice: We only have 3 columns (ProductID, CompanyName, Phone) - much simpler than Customer!
        String query = """ 
        SELECT ProductID, ProductName, SupplierID, CategoryID, QuantityPerUnit, UnitPrice, UnitsInStock, UnitsOnOrder, ReorderLevel, Discontinued
        FROM Products;
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

                    // Build a Product object from the current row's data
                    Product product = new Product(
                            resultSet.getInt("ProductID"),              // Get the product's ID as an integer
                            resultSet.getString("ProductName"),         // Get the product name as a string
                            resultSet.getInt("SupplierID"),             // Get the supplier ID as an integer
                            resultSet.getInt("CategoryID"),             // Get the category ID as an integer
                            resultSet.getString("QuantityPerUnit"),     // Get the quantity per unit as a string
                            resultSet.getDouble("UnitPrice"),           // Get the unit price as a double
                            resultSet.getInt("UnitsInStock"),           // Get the units in stock as an integer
                            resultSet.getInt("UnitsOnOrder"),           // Get the units on order as an integer
                            resultSet.getInt("ReorderLevel"),           // Get the reorder level as an integer
                            resultSet.getBoolean("Discontinued"));      // Get the discontinued status as a boolean

                    // Add this product to our list
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            // If something goes wrong, print a friendly error message
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();  // Print technical details for developers
        }

        // Return the list (might be empty if no products exist or an error occurred)
        return products;
    }

    // METHOD 2: FIND ONE SHIPPER BY ID
    // This searches for a specific product using their unique ID
    // It's like looking up a specific person in a phone book
    // IMPORTANT: Notice the parameter is "int productId" not "String productId"
    public Product find(int productId) {

        // Start with null (nothing) - we'll replace this if we find the product
        // null is like an empty box - it represents "no value yet"
        Product product = null;

        // SQL query with a ? placeholder - this is for security!
        // The ? is like a fill-in-the-blank that we'll safely fill in later
        // WHERE ProductID = ? means "only get the row where the ID matches what I specify"
        String query = """
                SELECT ProductID, CompanyName, Phone
                FROM Products
                WHERE ProductID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the ? placeholder with the actual product ID
            // IMPORTANT: We use setInt() not setString() because ProductID is an integer
            // The "1" means "the first placeholder" (in case there were multiple)
            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                // "if" instead of "while" because we expect at most ONE result
                // Product IDs are unique, so there should only be one match
                if (resultSet.next()) {
                    // We found them! Build the Product object
                    // Build a Product object from the current row's data
                     product = new Product(
                            resultSet.getInt("ProductID"),              // Get the product's ID as an integer
                            resultSet.getString("ProductName"),         // Get the product name as a string
                            resultSet.getInt("SupplierID"),             // Get the supplier ID as an integer
                            resultSet.getInt("CategoryID"),             // Get the category ID as an integer
                            resultSet.getString("QuantityPerUnit"),     // Get the quantity per unit as a string
                            resultSet.getDouble("UnitPrice"),           // Get the unit price as a double
                            resultSet.getInt("UnitsInStock"),           // Get the units in stock as an integer
                            resultSet.getInt("UnitsOnOrder"),           // Get the units on order as an integer
                            resultSet.getInt("ReorderLevel"),           // Get the reorder level as an integer
                            resultSet.getBoolean("Discontinued"));      // Get the discontinued status as a boolean
                }
                // If resultSet.next() is false, product stays null (product not found)
            }

        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        // Return the product (or null if not found)
        return product;
    }

    // METHOD 3: ADD A NEW SHIPPER
    // This inserts a brand new product into the database
    // It's like adding a new contact to your phone
    // KEY DIFFERENCE FROM CUSTOMER: We don't insert the ProductID - the database creates it!
    public Product add(Product product) {

        // INSERT INTO means "add a new row to this table"
        // CRITICAL DIFFERENCE: Notice ProductID is NOT in this query!
        // Why? Because ProductID is AUTO_INCREMENT - the database generates it automatically
        // We only provide CompanyName and Phone - the database will create the ID for us
        String query = """
                INSERT INTO Products (CompanyName, Phone)
                VALUES (?, ?);
                """;

        // Notice the second parameter: Statement.RETURN_GENERATED_KEYS
        // This tells the database: "After you create the new row, send me back the ID you generated"
        // It's like saying "When you assign me a ticket number, please tell me what it is"
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Fill in the 2 placeholders with data from the product object
            // Notice: We only have 2 values because we're not setting the ID
//            statement.setString(1, product.getCompanyName());  // 1st ? gets the company name
//            statement.setString(2, product.getPhone());        // 2nd ? gets the phone number

            // executeUpdate() runs the INSERT command and saves the new row
            statement.executeUpdate();

            // Now retrieve the auto-generated ProductID that the database created
            // This is like asking "What ID number did you assign to this new product?"
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                // Check if we got a generated key back
                if (generatedKeys.next()) {
                    // Get the generated ID from the first column (index 1)
                    // This is the ProductID the database automatically created
                    int generatedId = generatedKeys.getInt(1);

                    // Set this ID on our product object so it knows its own ID now
                    // Before this, the product object had no ID (or ID = 0)
                    // Now it knows its actual database ID!
                    product.setProductId(generatedId);
                }
            }

        } catch (SQLException e) {
            System.out.println("There was an error adding the product. Please try again.");
            e.printStackTrace();
        }

        // Return the product object - it now has its ID set!
        return product;
    }

    // METHOD 4: UPDATE AN EXISTING SHIPPER
    // This modifies information for a product that already exists in the database
    // It's like editing a contact in your phone
    public void update(Product product) {

        // UPDATE means "modify an existing row"
        // SET specifies which columns to change and to what values
        // WHERE specifies which product to update (using their ID)
        // Without WHERE, it would update EVERY product - that would be bad!
        String query = """
                UPDATE Products
                SET CompanyName = ?, Phone = ?
                WHERE ProductID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the placeholders with the product's updated information
            // Notice: ProductID comes LAST (position 3) because it's in the WHERE clause at the end
//            statement.setString(1, product.getCompanyName());  // Update company name
//            statement.setString(2, product.getPhone());        // Update phone

            // IMPORTANT: Use setInt() for the ProductID because it's an integer
            statement.setInt(3, product.getProductId());       // Which product to update (WHERE clause)

            // Execute the update - save the changes to the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error updating the product. Please try again.");
            e.printStackTrace();
        }

        // This method returns nothing (void) - it just does the update
    }

    // METHOD 5: DELETE A SHIPPER
    // This permanently removes a product from the database
    // It's like deleting a contact from your phone - be careful!
    // IMPORTANT: Notice the parameter is "int productId" not "String productId"
    public void delete(int productId) {

        // DELETE FROM means "remove a row from this table"
        // WHERE specifies which product to delete
        // Without WHERE, it would delete ALL products - disaster!
        String query = """
                DELETE FROM Products
                WHERE ProductID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in which product to delete
            // IMPORTANT: Use setInt() because ProductID is an integer
            statement.setInt(1, productId);

            // Execute the deletion - the product is now gone from the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error deleting the product. Please try again.");
            e.printStackTrace();
        }

        // This method returns nothing (void) - it just performs the deletion
    }
}

// ============================================================
// SUMMARY: KEY DIFFERENCES BETWEEN SHIPPERDAO AND CUSTOMERDAO
// ============================================================
//
// 1. DATA TYPES:
//    - ProductID is int (number) vs CustomerID is String (text)
//    - This means we use getInt()/setInt() instead of getString()/setString()
//
// 2. AUTO-INCREMENT ID:
//    - Products: Database generates the ID automatically
//    - Customers: We provide the ID ourselves
//    - This is why the add() method is different - we need to retrieve the generated ID
//
// 3. FEWER FIELDS:
//    - Product has only 3 fields (ID, CompanyName, Phone)
//    - Customer has 11 fields (ID, CompanyName, ContactName, etc.)
//    - This makes the code simpler and easier to maintain
//
// 4. ADD METHOD SPECIAL HANDLING:
//    - We use Statement.RETURN_GENERATED_KEYS to get back the auto-generated ID
//    - We then set this ID on the product object before returning it
//    - This is crucial so the calling code knows the new product's ID
//
// 5. SAME CRUD PATTERN:
//    - Despite the differences, both follow the same CRUD pattern:
//      C - CREATE: add()
//      R - READ:   getAll() and find()
//      U - UPDATE: update()
//      D - DELETE: delete()
//
// This demonstrates how the DAO pattern provides a consistent interface
// for database operations, even when the underlying tables have different structures!