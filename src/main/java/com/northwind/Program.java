package com.northwind;

import com.northwind.data.CustomerDao;
import org.apache.commons.dbcp2.BasicDataSource;


public class Program {
    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];
        String url = "jdbc:mysql://localhost:3306/Northwind";

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);


        CustomerDao customerDao = new CustomerDao(dataSource);



    }
}
