// This tells Java which "folder" (package) this code belongs to
package com.northwind;

// Import statements - bringing in the tools we need for this program
import com.northwind.data.CustomerDao;  // Our customer database helper
import com.northwind.data.ShipperDao;   // Our shipper database helper
import com.northwind.model.Customer;     // The Customer blueprint/class
import com.northwind.model.Shipper;      // The Shipper blueprint/class
import org.apache.commons.dbcp2.BasicDataSource;  // Manages database connections efficiently
import java.util.List;  // For working with lists of objects

// This is the main Program class - the entry point of our application
// Think of this as the "control center" that tests all our database operations
public class Program {

    // The main method - this is where the program starts running
    // Java always looks for "main" when you run a program
    // It's like the "Start" button on your application
    public static void main(String[] args) {

        // Get database credentials from command line arguments
        // args[0] is the first argument (username), args[1] is the second (password)
        // It's like entering your username and password to log into a website
        String username = args[0];
        String password = args[1];

        // The database URL - tells us where the database is located
        // "localhost:3306" means the database is on this computer on port 3306
        // "northwind" is the name of the specific database we're using
        String url = "jdbc:mysql://localhost:3306/northwind";

        // Create a DataSource - this is like a "connection pool" to the database
        // Instead of opening a new connection every time (slow), it maintains a pool
        // Think of it like a taxi stand with multiple taxis ready to go
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);            // Tell it where the database is
        dataSource.setUsername(username);  // Tell it the username
        dataSource.setPassword(password);  // Tell it the password

        // Print a nice header to organize our output
        System.out.println("========================================");
        System.out.println("TESTING CUSTOMER DAO");
        System.out.println("========================================\n");

        // Create a CustomerDao object - this is our customer database helper
        // We pass it the dataSource so it can connect to the database
        CustomerDao customerDao = new CustomerDao(dataSource);

        // ============================================================
        // TEST 1: Get All Customers
        // This tests the getAll() method to retrieve every customer
        // ============================================================
        System.out.println("--- Test 1: Get All Customers ---");

        // Call getAll() which returns a List of Customer objects
        // A List is like an ordered collection - think of it as a shopping list
        List<Customer> customers = customerDao.getAll();

        // Print how many customers we found
        // .size() tells us how many items are in the list
        System.out.println("Total customers: " + customers.size());

        // If the list is not empty, print the first customer as a sample
        // .isEmpty() returns true if the list has no items
        // .get(0) gets the item at position 0 (the first item - lists start at 0!)
        if (!customers.isEmpty()) {
            System.out.println("First customer: " + customers.get(0));
        }

        // ============================================================
        // TEST 2: Find a Specific Customer
        // This tests the find() method to search for one customer by ID
        // ============================================================
        System.out.println("\n--- Test 2: Find Customer by ID ---");

        // Look for a customer with ID "ALFKI" (a sample customer in the database)
        // This returns either a Customer object (if found) or null (if not found)
        Customer foundCustomer = customerDao.find("ALFKI");

        // Check if we found the customer
        // "!= null" means "is not null" or "actually has a value"
        if (foundCustomer != null) {
            System.out.println("Found: " + foundCustomer);
        } else {
            System.out.println("Customer ALFKI not found.");
        }

        // ============================================================
        // TEST 3: Add a New Customer
        // This tests the add() method to insert a new customer into the database
        // ============================================================
        System.out.println("\n--- Test 3: Add New Customer ---");

        // Create a new Customer object with test data
        // We use "new Customer(...)" to create an instance using the constructor
        // Think of it like filling out a form with all the customer's information
        Customer newCustomer = new Customer(
                "TSTID",              // Customer ID - we choose this (it's the primary key)
                "Test Company",       // Company name
                "John Doe",           // Contact person's name
                "Manager",            // Their job title
                "123 Test St",        // Street address
                "Test City",          // City
                "TC",                 // Region/State abbreviation
                "12345",              // Postal/ZIP code
                "USA",                // Country
                "555-1234",           // Phone number
                "555-5678"            // Fax number
        );

        // Add the customer to the database
        // The add() method returns the customer object back to us
        Customer addedCustomer = customerDao.add(newCustomer);
        System.out.println("Added: " + addedCustomer);

        // ============================================================
        // TEST 4: Verify the Customer Was Added
        // This confirms our add() worked by trying to find the customer we just added
        // ============================================================
        System.out.println("\n--- Test 4: Verify Customer Was Added ---");

        // Try to find the customer we just added using their ID "TSTID"
        Customer verifyCustomer = customerDao.find("TSTID");

        // Check if we found them
        if (verifyCustomer != null) {
            System.out.println("Verified: " + verifyCustomer);
        } else {
            // If null, something went wrong with our add operation
            System.out.println("Customer TSTID not found after adding.");
        }

        // ============================================================
        // TEST 5: Update the Customer
        // This tests the update() method to modify an existing customer's information
        // ============================================================
        System.out.println("\n--- Test 5: Update Customer ---");

        // Only try to update if we successfully found the customer
        if (verifyCustomer != null) {

            // Modify the customer's information using setter methods
            // Setters let us change the values of the object's fields
            verifyCustomer.setCompanyName("Updated Test Company");  // Change company name
            verifyCustomer.setContactName("Jane Smith");            // Change contact person
            verifyCustomer.setPhone("555-9999");                    // Change phone number

            // Send the updated customer to the database
            // update() saves these changes to the database
            customerDao.update(verifyCustomer);
            System.out.println("Updated customer TSTID");

            // Retrieve the customer again to see the changes
            // This confirms the update worked
            Customer updatedCustomer = customerDao.find("TSTID");
            System.out.println("After update: " + updatedCustomer);
        }

        // ============================================================
        // TEST 6: Delete the Customer
        // This tests the delete() method to remove a customer from the database
        // This is permanent - be careful with delete operations!
        // ============================================================
        System.out.println("\n--- Test 6: Delete Customer ---");

        // Delete the test customer by their ID
        customerDao.delete("TSTID");
        System.out.println("Deleted customer TSTID");

        // Try to find the customer to confirm deletion
        Customer deletedCustomer = customerDao.find("TSTID");

        // If find() returns null, the customer is gone (success!)
        if (deletedCustomer == null) {
            System.out.println("Confirmed: Customer TSTID no longer exists.");
        } else {
            // If we still find them, something went wrong
            System.out.println("Warning: Customer TSTID still exists after deletion.");
        }

        // Print a header for the next section of tests
        System.out.println("\n========================================");
        System.out.println("TESTING SHIPPER DAO");
        System.out.println("========================================\n");

        // Create a ShipperDao object - our shipper database helper
        // Uses the same dataSource (connection pool) as CustomerDao
        ShipperDao shipperDao = new ShipperDao(dataSource);

        // ============================================================
        // TEST 1: Get All Shippers
        // This tests the getAll() method to retrieve every shipper
        // ============================================================
        System.out.println("--- Test 1: Get All Shippers ---");

        // Get all shippers from the database
        List<Shipper> shippers = shipperDao.getAll();
        System.out.println("Total shippers: " + shippers.size());

        // Loop through all shippers and print each one
        // "for (Type variable : collection)" is called an "enhanced for loop"
        // It's like saying "for each shipper in the shippers list, do this..."
        for (Shipper shipper : shippers) {
            System.out.println(shipper);
        }

        // ============================================================
        // TEST 2: Find a Specific Shipper
        // This tests the find() method to search for one shipper by ID
        // Notice: shipper IDs are int (numbers), not String like customer IDs
        // ============================================================
        System.out.println("\n--- Test 2: Find Shipper by ID ---");

        // Look for a shipper with ID 1
        // We pass an int (1) not a String ("1") because ShipperID is a number
        Shipper foundShipper = shipperDao.find(1);

        if (foundShipper != null) {
            System.out.println("Found: " + foundShipper);
        } else {
            System.out.println("Shipper with ID 1 not found.");
        }

        // ============================================================
        // TEST 3: Add a New Shipper
        // This tests the add() method to insert a new shipper
        // KEY DIFFERENCE: ShipperID is auto-increment, so we don't set it!
        // ============================================================
        System.out.println("\n--- Test 3: Add New Shipper ---");

        // Create a new Shipper using the no-argument constructor
        // We don't set the ID because the database will generate it automatically
        Shipper newShipper = new Shipper();

        // Use setters to fill in the information we DO control
        newShipper.setCompanyName("Test Shipping Co");
        newShipper.setPhone("555-TEST");

        // Add the shipper to the database
        // IMPORTANT: After adding, the shipper object will have its ID set
        // The database generated the ID and our add() method retrieved it
        Shipper addedShipper = shipperDao.add(newShipper);
        System.out.println("Added: " + addedShipper);

        // Print the auto-generated ID
        // This proves the database created a unique ID for us
        System.out.println("Generated ID: " + addedShipper.getShipperId());

        // ============================================================
        // TEST 4: Verify the Shipper Was Added
        // This confirms our add() worked by trying to find the shipper
        // ============================================================
        System.out.println("\n--- Test 4: Verify Shipper Was Added ---");

        // Save the generated ID in a variable so we can use it later
        // We need this ID for finding, updating, and deleting this shipper
        int newShipperId = addedShipper.getShipperId();

        // Try to find the shipper using the auto-generated ID
        Shipper verifyShipper = shipperDao.find(newShipperId);

        if (verifyShipper != null) {
            System.out.println("Verified: " + verifyShipper);
        } else {
            System.out.println("Shipper with ID " + newShipperId + " not found after adding.");
        }

        // ============================================================
        // TEST 5: Update the Shipper
        // This tests the update() method to modify an existing shipper
        // ============================================================
        System.out.println("\n--- Test 5: Update Shipper ---");

        if (verifyShipper != null) {

            // Modify the shipper's information
            verifyShipper.setCompanyName("Updated Shipping Co");
            verifyShipper.setPhone("555-UPDT");

            // Save changes to the database
            shipperDao.update(verifyShipper);
            System.out.println("Updated shipper with ID " + newShipperId);

            // Retrieve the shipper again to see the changes
            Shipper updatedShipper = shipperDao.find(newShipperId);
            System.out.println("After update: " + updatedShipper);
        }

        // ============================================================
        // TEST 6: Delete the Shipper
        // This tests the delete() method to remove a shipper
        // ============================================================
        System.out.println("\n--- Test 6: Delete Shipper ---");

        // Delete by the shipper's ID
        shipperDao.delete(newShipperId);
        System.out.println("Deleted shipper with ID " + newShipperId);

        // Try to find the shipper to confirm deletion
        Shipper deletedShipper = shipperDao.find(newShipperId);

        if (deletedShipper == null) {
            System.out.println("Confirmed: Shipper with ID " + newShipperId + " no longer exists.");
        } else {
            System.out.println("Warning: Shipper with ID " + newShipperId + " still exists after deletion.");
        }

        // Print a final message showing all tests are complete
        System.out.println("\n========================================");
        System.out.println("ALL TESTS COMPLETED");
        System.out.println("========================================");
    }
}

// ============================================================
// SUMMARY: What This Program Does
// ============================================================
// This program is a comprehensive test suite that exercises all CRUD operations
// (Create, Read, Update, Delete) for both Customer and Shipper entities.
//
// For each entity, we test:
// 1. READ ALL - Get every record in the table
// 2. READ ONE - Find a specific record by ID
// 3. CREATE - Add a new record
// 4. VERIFY - Confirm the record was added
// 5. UPDATE - Modify the record
// 6. DELETE - Remove the record
// 7. VERIFY - Confirm the record was deleted
//
// Key Learning Points:
// - How to use DAO classes to interact with a database
// - The complete lifecycle of database records (CRUD)
// - Difference between String IDs (Customer) and int auto-increment IDs (Shipper)
// - How to verify operations succeeded by reading back the data
// - Proper cleanup (deleting test data) so we don't clutter the database
//
// This pattern of testing is important in real applications to ensure
// your database operations work correctly before using them in production!