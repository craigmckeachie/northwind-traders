package com.northwind.data;

import com.northwind.model.Shipper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShipperDao {
    private DataSource dataSource;

    public ShipperDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Shipper> getAll() {
        List<Shipper> shippers = new ArrayList<>();

        String query = """
                SELECT ShipperID, CompanyName, Phone
                FROM Shippers;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Shipper shipper = new Shipper(
                            resultSet.getInt("ShipperID"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("Phone"));

                    shippers.add(shipper);
                }

            }
        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        return shippers;
    }

    public Shipper find(int shipperId) {
        Shipper shipper = null;

        String query = """
                SELECT ShipperID, CompanyName, Phone
                FROM Shippers
                WHERE ShipperID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, shipperId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    shipper = new Shipper(
                            resultSet.getInt("ShipperID"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("Phone"));
                }

            }
        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        return shipper;
    }

    public Shipper add(Shipper shipper) {
        String query = """
                INSERT INTO Shippers (CompanyName, Phone)
                VALUES (?, ?);
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, shipper.getCompanyName());
            statement.setString(2, shipper.getPhone());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    shipper.setShipperId(generatedId);
                }
            }

        } catch (SQLException e) {
            System.out.println("There was an error adding the shipper. Please try again.");
            e.printStackTrace();
        }

        return shipper;
    }

    public void update(Shipper shipper) {
        String query = """
                UPDATE Shippers
                SET CompanyName = ?, Phone = ?
                WHERE ShipperID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, shipper.getCompanyName());
            statement.setString(2, shipper.getPhone());
            statement.setInt(3, shipper.getShipperId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error updating the shipper. Please try again.");
            e.printStackTrace();
        }
    }

    public void delete(int shipperId) {
        String query = """
                DELETE FROM Shippers
                WHERE ShipperID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, shipperId);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error deleting the shipper. Please try again.");
            e.printStackTrace();
        }
    }
}