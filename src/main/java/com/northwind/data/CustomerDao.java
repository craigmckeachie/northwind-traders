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

    public Customer find(int customerId){
        throw new UnsupportedOperationException();
    }

    public Customer add(Customer customer){
        throw new UnsupportedOperationException();
    }

    public void update(Customer customer){
        throw new UnsupportedOperationException();
    }

    public void delete(int customerId){
        throw new UnsupportedOperationException();
    }



}
