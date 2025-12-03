package com.northwind.data;

import com.northwind.model.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private DataSource dataSource;

    public CustomerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();

        String query = """
                SELECT CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax
                FROM Customers;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
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

                    customers.add(customer);
                }

            }
        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        return customers;
    }

    public Customer find(String customerId) {
        Customer customer = null;

        String query = """
                SELECT CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax
                FROM Customers
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
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

            }
        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        return customer;
    }

    public Customer add(Customer customer) {
        String query = """
                INSERT INTO Customers (CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, customer.getCustomerId());
            statement.setString(2, customer.getCompanyName());
            statement.setString(3, customer.getContactName());
            statement.setString(4, customer.getContactTitle());
            statement.setString(5, customer.getAddress());
            statement.setString(6, customer.getCity());
            statement.setString(7, customer.getRegion());
            statement.setString(8, customer.getPostalCode());
            statement.setString(9, customer.getCountry());
            statement.setString(10, customer.getPhone());
            statement.setString(11, customer.getFax());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error adding the customer. Please try again.");
            e.printStackTrace();
        }

        return customer;
    }

    public void update(Customer customer) {
        String query = """
                UPDATE Customers
                SET CompanyName = ?, ContactName = ?, ContactTitle = ?, Address = ?, City = ?, Region = ?, PostalCode = ?, Country = ?, Phone = ?, Fax = ?
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, customer.getCompanyName());
            statement.setString(2, customer.getContactName());
            statement.setString(3, customer.getContactTitle());
            statement.setString(4, customer.getAddress());
            statement.setString(5, customer.getCity());
            statement.setString(6, customer.getRegion());
            statement.setString(7, customer.getPostalCode());
            statement.setString(8, customer.getCountry());
            statement.setString(9, customer.getPhone());
            statement.setString(10, customer.getFax());
            statement.setString(11, customer.getCustomerId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error updating the customer. Please try again.");
            e.printStackTrace();
        }
    }

    public void delete(String customerId) {
        String query = """
                DELETE FROM Customers
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, customerId);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error deleting the customer. Please try again.");
            e.printStackTrace();
        }
    }
}