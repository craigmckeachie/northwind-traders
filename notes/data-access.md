# Data Access

- store username and passwords as configured command-line arguments
- use the `DataSource` object to store username, password, and database url

- a model class for each table (Ex. Customer)
- model classes go in `model` package

- do data access in a data access object (DAO), one DAO per table
- DAOs go in `data` package
- DAO should return:
    - (get one) one object created from the Customer class blueprint
    - or
    - (get all) `List` of Customer objects
    - or
    - (insert/add/create) one object created from the Customer class
        - the returned Customer object will have the id from the database auto-increment column
    - update returns void
        - because we already have the customer id
- Data access best practices
    - should take in the `DataSource` object in the constructor
    - use a `try` resources blocks
    - let resources be auto-closed for us
    - handle `Exception` in the `catch`
    - use `PreparedStatement` to avoid SQL Injection
    - set parameters use `PreparedStatement`'s `setString`, `setInt`, `setX`...
    - process the ResultSet and read the column labels using `getString`, `getInt`, `getX`