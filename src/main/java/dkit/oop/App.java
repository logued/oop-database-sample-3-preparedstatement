/**
 * Feb 2022
 * Connect to Database and execute Prepared Statement to INSERT a row in database.
 */

package dkit.oop;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    public void start() {
        System.out.println("MySQL Connect - PreparedStatement - Sample 3.");

        String url = "jdbc:mysql://localhost/";
        String dbName = "test";
        String userName = "root";
        String password = "";

        int customerId = 123;
        String firstName = "Charlie";     // Normally, these variables would be filled
        String lastName = "Haughey";      // from input boxes in the User Interface of the
        String birthDate = "1950-02-01";  // Java application.

        // Try-with-Resources style
        try (Connection connection = DriverManager.getConnection(url + dbName, userName, password);
             PreparedStatement preparedStatement =
                     connection.prepareStatement("insert into test.Customers values (null, ?, ?, ?)");
             )
        {

            System.out.println("Connected to the database");
            System.out.println("Building a PreparedStatement to insert a new row in database.");

//          preparedStatement = conn.prepareStatement("insert into test.Customers values (?, ?, ?, ?)");
//          Parameters are numbered starting with 1, and correspond to order of the question marks above.
//          1 corresponds to first question mark, and so on...
//          As the first field is an Auto-Increment field in the database,  we specify a null value for it.

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setDate(3, Date.valueOf(birthDate));

            preparedStatement.executeUpdate();  // will INSERT a new row

            // Statements allow us to issue SQL queries to the database
            Statement statement = connection.createStatement();

            // Execute the Prepared Statement and get a result set
            ResultSet resultSet = statement.executeQuery("select * from Customers");

            while (resultSet.next()) {
                // It is possible to get the columns via name (as shown)
                // or via the column number.  First column is indexed as 1 (and not 0 as in arrays)
                // e.g. resultSet.getString(2); gets the second column.
                customerId = resultSet.getInt("custID");
                String fName = resultSet.getString("firstName");
                //String fName = resultSet.getString(2); // same effect as line above
                String lName = resultSet.getString("lastName");
                Date dobAsDate = resultSet.getDate("dob");
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
