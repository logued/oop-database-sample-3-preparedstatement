/**
 * Jan 2025
 * Connect to Database and execute a Prepared Statement to INSERT a row in a database.
 * Prepared statements should be used to prevent "SQL Injection" attacks on a database.
 */
package dkit.oop;

import java.sql.*;
import java.time.LocalDate;

public class AppMain {
    public static void main(String[] args) {
        AppMain appMain = new AppMain();
        appMain.start();
    }

    public void start() {
        System.out.println("MySQL Connect - PreparedStatement - Sample 3.");

        String url = "jdbc:mysql://localhost/";
        String dbName = "test";
        String fullURL = url + dbName;  // join together
        String userName = "root";
        String password = "";

        String firstName = "Charlie";     // Normally, these variables would be filled
        String lastName = "Haughey";      // from input boxes in the User Interface of the
        String birthDate = "1950-02-01";  // Java application.

        // Prepare the Query String using "?" to indicate field parameters.
        //
        String query1 = "INSERT INTO test.Customers VALUES (null, ?, ?, ?)";

        // 'null' tell the prepared statement that we want to allow the database
        // to assign an id using auto-increment.
        // i.e. use it with auto-increment fields.

        // Try-with-Resources style
        try (Connection connection = DriverManager.getConnection(fullURL, userName, password);
             PreparedStatement preparedStatement1 = connection.prepareStatement( query1 );
             )
        {
            System.out.println("Connected to the database");
            System.out.println("Building a PreparedStatement to insert a new row in database.");

//          preparedStatement = conn.prepareStatement("INSERT INTO test.Customers VALUES (null, ?, ?, ?)";
//          Parameters are indexed starting with 1, and correspond to order of the question marks above.
//          1 corresponds to first question mark, 2 to the second one, and so on...
//          As the first field is an Auto-Increment field in the database, we specify a null value for it.

            preparedStatement1.setString(1, firstName);  // bind 'firstName" with the first "?" parameter
            preparedStatement1.setString(2, lastName);
            preparedStatement1.setDate(3, Date.valueOf(birthDate));

            preparedStatement1.executeUpdate();  // will INSERT a new row

            //////////  Code below creates a PreparedStatement to retrieve and display ALL rows in Table ///////////////////

            // Statements allow us to issue SQL queries to the database
            Statement statement = connection.createStatement();

            // Execute the Prepared Statement and get a result set
            ResultSet resultSet = statement.executeQuery("select * from Customers");

            // Note above that we use executeQuery() for a select query to retrieve data,
            // but,
            // we use executeUpdate() for prepared statements that modify the database
            // such as insert, update and delete queries.

            while (resultSet.next()) {
                // It is possible to get the columns via name (as shown)
                // or via the column number.  First column is indexed as 1 (and not 0 as in arrays)
                // e.g. resultSet.getString(2); gets the second column.
                int customerId = resultSet.getInt("CUSTOMER_ID");
                String fName = resultSet.getString("FIRST_NAME");
                //String fName = resultSet.getString(2); // same effect as line above
                String lName = resultSet.getString("LAST_NAME");
                Date dobAsDate = resultSet.getDate("DATE_OF_BIRTH");
                // Date is not the best type to use for a DOB.  LocalDate is better, so convert as follows:
                LocalDate dob = dobAsDate.toLocalDate();

                System.out.print(customerId + ", ");
                System.out.print(fName + ", ");
                System.out.print(lName + ", ");
                System.out.println(dob);
            }
            System.out.println("Disconnected from database");
            System.out.println("Every time you run this, another \"Charlie Haughey\" row is INSERTED !");
        } catch (SQLException ex) {
            System.out.println("Failed to connect to database - check MySQL is running and that you are using the correct database details");
            ex.printStackTrace();
        }
    }
}

//TODO Q1.
// Run the program and check that it works.  Each time you run the program
// it adds an extra "Charlie Haughey" row (with a new ID.)
// Check to see that this is the case.

//TODO Q2.
// Create and execute a prepared statement that will update record with ID 101
// by setting the first name to "Robert" and the last name to "Zimmerman".
// Use a PreparedStatement (sql UPDATE)
// Execute and check that the change has been made.

