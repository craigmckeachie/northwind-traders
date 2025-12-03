package com.northwind.data;

import com.northwind.model.Customer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private DataSource dataSource;

    public CustomerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Customer> getAll(){
        List<Customer> customers = new ArrayList<>();
        // Customer 1: Alfreds Futterkiste
        Customer customer1 = new Customer(
                "ALFKI",
                "Alfreds Futterkiste",
                "Maria Anders",
                "Sales Representative",
                "Obere Str. 57",
                "Berlin",
                null, // Region is often null for some Northwind data
                "12209",
                "Germany",
                "030-0074321",
                "030-0076545"
        );

        // Customer 2: Ana Trujillo Emparedados y helados
        Customer customer2 = new Customer(
                "ANATR",
                "Ana Trujillo Emparedados y helados",
                "Ana Trujillo",
                "Owner",
                "Avda. de la Constitución 2222",
                "México D.F.",
                null,
                "05021",
                "Mexico",
                "(5) 555-4729",
                "(5) 555-3745"
        );

        // Customer 3: Antonio Moreno Taquería
        Customer customer3 = new Customer(
                "ANTON",
                "Antonio Moreno Taquería",
                "Antonio Moreno",
                "Owner",
                "Mataderos 2312",
                "México D.F.",
                null,
                "05023",
                "Mexico",
                "(5) 555-3932",
                null // Fax is often null/missing in some Northwind data
        );

        // Customer 4: Around the Horn
        Customer customer4 = new Customer(
                "AROUT",
                "Around the Horn",
                "Thomas Hardy",
                "Sales Representative",
                "120 Hanover Sq.",
                "London",
                null,
                "WA1 1DP",
                "UK",
                "(171) 555-7788",
                "(171) 555-6750"
        );

        // Customer 5: Berglunds snabbköp
        Customer customer5 = new Customer(
                "BERGS",
                "Berglunds snabbköp",
                "Christina Berglund",
                "Order Administrator",
                "Berguvsvägen 8",
                "Luleå",
                null,
                "S-958 22",
                "Sweden",
                "0921-12 34 65",
                "0921-12 34 67"
        );

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        customers.add(customer5);


        return customers;
    }

}
