// This line tells Java which "folder" (package) this code belongs to
// Think of packages like organizing files on your computer into folders
package com.northwind.data;

// These are "import" statements - they're like telling Java "I need to use these tools"
import com.northwind.model.Shipper;    // This imports the Shipper class (a blueprint for shipper objects)
import javax.sql.DataSource;           // This helps us connect to a database
import java.sql.Connection;            // This represents an active connection to the database
import java.sql.PreparedStatement;     // This helps us safely send commands to the database
import java.sql.ResultSet;             // This holds the results we get back from the database
import java.sql.SQLException;          // This helps us handle database errors
import java.sql.Statement;             // This is needed to retrieve auto-generated keys
import java.util.ArrayList;            // This is a flexible list that can grow and shrink
import java.util.List;                 // This is the general concept of a list

// DAO stands for "Data Access Object"
// This class is responsible for talking to the database and managing shipper information
// Think of it as a librarian who can:
// - Show you all books (getAll)
// - Find a specific book (find)
// - Add a new book (add)
// - Update book information (update)
// - Remove a book (delete)
public class ShipperDao {

    // This is a "field" or "instance variable" - it's data that belongs to this object
    // DataSource is like having the address and key to the database
    // The "private" keyword means only this class can directly access it
    private DataSource dataSource;

    // This is a "constructor" - it runs when you create a new ShipperDao object
    // It's like initializing/setting up the object when it's born
    // The constructor takes a DataSource as input and stores it for later use
    public ShipperDao(DataSource dataSource) {
        // "this.dataSource" refers to the field above
        // "dataSource" (without "this") refers to the parameter we received
        // This line says: "Store the dataSource I received into my field for later"
        this.dataSource = dataSource;
    }

    // METHOD 1: GET ALL SHIPPERS
    // This method retrieves ALL shippers from the database and returns them as a list
    // It's like asking "Show me everyone in your database"
    public List<Shipper> getAll() {

        // Create an empty ArrayList to store all the shippers we'll find
        // It starts empty, but we'll add shippers to it as we find them
        List<Shipper> shippers = new ArrayList<>();

        // This is the SQL query - a command we'll send to the database
        // SQL is like asking the database: "Give me all this information from the Shippers table"
        // Notice: We only have 3 columns (ShipperID, CompanyName, Phone) - much simpler than Customer!
        String query = """
                SELECT ShipperID, CompanyName, Phone
                FROM Shippers;
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

                    // Build a Shipper object from the current row's data
                    // IMPORTANT: Notice we use getInt() for ShipperID because it's a number (int)
                    // For Customer, we used getString() because CustomerID was text (String)
                    Shipper shipper = new Shipper(
                            resultSet.getInt("ShipperID"),        // Get the shipper's ID as an integer
                            resultSet.getString("CompanyName"),   // Get the company name as a string
                            resultSet.getString("Phone"));        // Get the phone number as a string

                    // Add this shipper to our list
                    shippers.add(shipper);
                }
            }

        } catch (SQLException e) {
            // If something goes wrong, print a friendly error message
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();  // Print technical details for developers
        }

        // Return the list (might be empty if no shippers exist or an error occurred)
        return shippers;
    }

    // METHOD 2: FIND ONE SHIPPER BY ID
    // This searches for a specific shipper using their unique ID
    // It's like looking up a specific person in a phone book
    // IMPORTANT: Notice the parameter is "int shipperId" not "String shipperId"
    public Shipper find(int shipperId) {

        // Start with null (nothing) - we'll replace this if we find the shipper
        // null is like an empty box - it represents "no value yet"
        Shipper shipper = null;

        // SQL query with a ? placeholder - this is for security!
        // The ? is like a fill-in-the-blank that we'll safely fill in later
        // WHERE ShipperID = ? means "only get the row where the ID matches what I specify"
        String query = """
                SELECT ShipperID, CompanyName, Phone
                FROM Shippers
                WHERE ShipperID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the ? placeholder with the actual shipper ID
            // IMPORTANT: We use setInt() not setString() because ShipperID is an integer
            // The "1" means "the first placeholder" (in case there were multiple)
            statement.setInt(1, shipperId);

            try (ResultSet resultSet = statement.executeQuery()) {

                // "if" instead of "while" because we expect at most ONE result
                // Shipper IDs are unique, so there should only be one match
                if (resultSet.next()) {
                    // We found them! Build the Shipper object
                    shipper = new Shipper(
                            resultSet.getInt("ShipperID"),        // Remember: getInt() for numbers
                            resultSet.getString("CompanyName"),
                            resultSet.getString("Phone"));
                }
                // If resultSet.next() is false, shipper stays null (shipper not found)
            }

        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        // Return the shipper (or null if not found)
        return shipper;
    }

    // METHOD 3: ADD A NEW SHIPPER
    // This inserts a brand new shipper into the database
    // It's like adding a new contact to your phone
    // KEY DIFFERENCE FROM CUSTOMER: We don't insert the ShipperID - the database creates it!
    public Shipper add(Shipper shipper) {

        // INSERT INTO means "add a new row to this table"
        // CRITICAL DIFFERENCE: Notice ShipperID is NOT in this query!
        // Why? Because ShipperID is AUTO_INCREMENT - the database generates it automatically
        // We only provide CompanyName and Phone - the database will create the ID for us
        String query = """
                INSERT INTO Shippers (CompanyName, Phone)
                VALUES (?, ?);
                """;

        // Notice the second parameter: Statement.RETURN_GENERATED_KEYS
        // This tells the database: "After you create the new row, send me back the ID you generated"
        // It's like saying "When you assign me a ticket number, please tell me what it is"
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Fill in the 2 placeholders with data from the shipper object
            // Notice: We only have 2 values because we're not setting the ID
            statement.setString(1, shipper.getCompanyName());  // 1st ? gets the company name
            statement.setString(2, shipper.getPhone());        // 2nd ? gets the phone number

            // executeUpdate() runs the INSERT command and saves the new row
            statement.executeUpdate();

            // Now retrieve the auto-generated ShipperID that the database created
            // This is like asking "What ID number did you assign to this new shipper?"
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                // Check if we got a generated key back
                if (generatedKeys.next()) {
                    // Get the generated ID from the first column (index 1)
                    // This is the ShipperID the database automatically created
                    int generatedId = generatedKeys.getInt(1);

                    // Set this ID on our shipper object so it knows its own ID now
                    // Before this, the shipper object had no ID (or ID = 0)
                    // Now it knows its actual database ID!
                    shipper.setShipperId(generatedId);
                }
            }

        } catch (SQLException e) {
            System.out.println("There was an error adding the shipper. Please try again.");
            e.printStackTrace();
        }

        // Return the shipper object - it now has its ID set!
        return shipper;
    }

    // METHOD 4: UPDATE AN EXISTING SHIPPER
    // This modifies information for a shipper that already exists in the database
    // It's like editing a contact in your phone
    public void update(Shipper shipper) {

        // UPDATE means "modify an existing row"
        // SET specifies which columns to change and to what values
        // WHERE specifies which shipper to update (using their ID)
        // Without WHERE, it would update EVERY shipper - that would be bad!
        String query = """
                UPDATE Shippers
                SET CompanyName = ?, Phone = ?
                WHERE ShipperID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the placeholders with the shipper's updated information
            // Notice: ShipperID comes LAST (position 3) because it's in the WHERE clause at the end
            statement.setString(1, shipper.getCompanyName());  // Update company name
            statement.setString(2, shipper.getPhone());        // Update phone

            // IMPORTANT: Use setInt() for the ShipperID because it's an integer
            statement.setInt(3, shipper.getShipperId());       // Which shipper to update (WHERE clause)

            // Execute the update - save the changes to the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error updating the shipper. Please try again.");
            e.printStackTrace();
        }

        // This method returns nothing (void) - it just does the update
    }

    // METHOD 5: DELETE A SHIPPER
    // This permanently removes a shipper from the database
    // It's like deleting a contact from your phone - be careful!
    // IMPORTANT: Notice the parameter is "int shipperId" not "String shipperId"
    public void delete(int shipperId) {

        // DELETE FROM means "remove a row from this table"
        // WHERE specifies which shipper to delete
        // Without WHERE, it would delete ALL shippers - disaster!
        String query = """
                DELETE FROM Shippers
                WHERE ShipperID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in which shipper to delete
            // IMPORTANT: Use setInt() because ShipperID is an integer
            statement.setInt(1, shipperId);

            // Execute the deletion - the shipper is now gone from the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error deleting the shipper. Please try again.");
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
//    - ShipperID is int (number) vs CustomerID is String (text)
//    - This means we use getInt()/setInt() instead of getString()/setString()
//
// 2. AUTO-INCREMENT ID:
//    - Shippers: Database generates the ID automatically
//    - Customers: We provide the ID ourselves
//    - This is why the add() method is different - we need to retrieve the generated ID
//
// 3. FEWER FIELDS:
//    - Shipper has only 3 fields (ID, CompanyName, Phone)
//    - Customer has 11 fields (ID, CompanyName, ContactName, etc.)
//    - This makes the code simpler and easier to maintain
//
// 4. ADD METHOD SPECIAL HANDLING:
//    - We use Statement.RETURN_GENERATED_KEYS to get back the auto-generated ID
//    - We then set this ID on the shipper object before returning it
//    - This is crucial so the calling code knows the new shipper's ID
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