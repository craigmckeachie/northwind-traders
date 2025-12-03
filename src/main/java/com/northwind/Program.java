package com.northwind;

import com.northwind.data.CustomerDao;
import com.northwind.data.ShipperDao;
import com.northwind.model.Customer;
import com.northwind.model.Shipper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];
        String url = "jdbc:mysql://localhost:3306/northwind";

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        System.out.println("========================================");
        System.out.println("TESTING CUSTOMER DAO");
        System.out.println("========================================\n");

        CustomerDao customerDao = new CustomerDao(dataSource);

        // Test 1: Get all customers
        System.out.println("--- Test 1: Get All Customers ---");
        List<Customer> customers = customerDao.getAll();
        System.out.println("Total customers: " + customers.size());
        if (!customers.isEmpty()) {
            System.out.println("First customer: " + customers.get(0));
        }

        // Test 2: Find a specific customer
        System.out.println("\n--- Test 2: Find Customer by ID ---");
        Customer foundCustomer = customerDao.find("ALFKI");
        if (foundCustomer != null) {
            System.out.println("Found: " + foundCustomer);
        } else {
            System.out.println("Customer ALFKI not found.");
        }

        // Test 3: Add a new customer
        System.out.println("\n--- Test 3: Add New Customer ---");
        Customer newCustomer = new Customer(
                "TSTID",
                "Test Company",
                "John Doe",
                "Manager",
                "123 Test St",
                "Test City",
                "TC",
                "12345",
                "USA",
                "555-1234",
                "555-5678"
        );
        Customer addedCustomer = customerDao.add(newCustomer);
        System.out.println("Added: " + addedCustomer);

        // Test 4: Verify the customer was added
        System.out.println("\n--- Test 4: Verify Customer Was Added ---");
        Customer verifyCustomer = customerDao.find("TSTID");
        if (verifyCustomer != null) {
            System.out.println("Verified: " + verifyCustomer);
        } else {
            System.out.println("Customer TSTID not found after adding.");
        }

        // Test 5: Update the customer
        System.out.println("\n--- Test 5: Update Customer ---");
        if (verifyCustomer != null) {
            verifyCustomer.setCompanyName("Updated Test Company");
            verifyCustomer.setContactName("Jane Smith");
            verifyCustomer.setPhone("555-9999");
            customerDao.update(verifyCustomer);
            System.out.println("Updated customer TSTID");

            Customer updatedCustomer = customerDao.find("TSTID");
            System.out.println("After update: " + updatedCustomer);
        }

        // Test 6: Delete the customer
        System.out.println("\n--- Test 6: Delete Customer ---");
        customerDao.delete("TSTID");
        System.out.println("Deleted customer TSTID");

        Customer deletedCustomer = customerDao.find("TSTID");
        if (deletedCustomer == null) {
            System.out.println("Confirmed: Customer TSTID no longer exists.");
        } else {
            System.out.println("Warning: Customer TSTID still exists after deletion.");
        }

        System.out.println("\n========================================");
        System.out.println("TESTING SHIPPER DAO");
        System.out.println("========================================\n");

        ShipperDao shipperDao = new ShipperDao(dataSource);

        // Test 1: Get all shippers
        System.out.println("--- Test 1: Get All Shippers ---");
        List<Shipper> shippers = shipperDao.getAll();
        System.out.println("Total shippers: " + shippers.size());
        for (Shipper shipper : shippers) {
            System.out.println(shipper);
        }

        // Test 2: Find a specific shipper
        System.out.println("\n--- Test 2: Find Shipper by ID ---");
        Shipper foundShipper = shipperDao.find(1);
        if (foundShipper != null) {
            System.out.println("Found: " + foundShipper);
        } else {
            System.out.println("Shipper with ID 1 not found.");
        }

        // Test 3: Add a new shipper
        System.out.println("\n--- Test 3: Add New Shipper ---");
        Shipper newShipper = new Shipper();
        newShipper.setCompanyName("Test Shipping Co");
        newShipper.setPhone("555-TEST");
        Shipper addedShipper = shipperDao.add(newShipper);
        System.out.println("Added: " + addedShipper);
        System.out.println("Generated ID: " + addedShipper.getShipperId());

        // Test 4: Verify the shipper was added
        System.out.println("\n--- Test 4: Verify Shipper Was Added ---");
        int newShipperId = addedShipper.getShipperId();
        Shipper verifyShipper = shipperDao.find(newShipperId);
        if (verifyShipper != null) {
            System.out.println("Verified: " + verifyShipper);
        } else {
            System.out.println("Shipper with ID " + newShipperId + " not found after adding.");
        }

        // Test 5: Update the shipper
        System.out.println("\n--- Test 5: Update Shipper ---");
        if (verifyShipper != null) {
            verifyShipper.setCompanyName("Updated Shipping Co");
            verifyShipper.setPhone("555-UPDT");
            shipperDao.update(verifyShipper);
            System.out.println("Updated shipper with ID " + newShipperId);

            Shipper updatedShipper = shipperDao.find(newShipperId);
            System.out.println("After update: " + updatedShipper);
        }

        // Test 6: Delete the shipper
        System.out.println("\n--- Test 6: Delete Shipper ---");
        shipperDao.delete(newShipperId);
        System.out.println("Deleted shipper with ID " + newShipperId);

        Shipper deletedShipper = shipperDao.find(newShipperId);
        if (deletedShipper == null) {
            System.out.println("Confirmed: Shipper with ID " + newShipperId + " no longer exists.");
        } else {
            System.out.println("Warning: Shipper with ID " + newShipperId + " still exists after deletion.");
        }

        System.out.println("\n========================================");
        System.out.println("ALL TESTS COMPLETED");
        System.out.println("========================================");
    }
}