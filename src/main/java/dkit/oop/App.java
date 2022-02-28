/**
 * Feb 2022
 * Connect to Database and execute Prepared Statement to INSERT a row in database.
 */

package dkit.oop;
import java.sql.*;

public class App
{
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


        try ( Connection connection = DriverManager.getConnection(url+dbName,userName,password);
              PreparedStatement preparedStatement
                      = connection.prepareStatement("insert into test.Customers values (null, ?, ?, ?)"); )
        {
            System.out.println("Connected to the database");
            System.out.println("Inserting row for Charlie Haughey....");


//                preparedStatement = conn.prepareStatement("insert into test.Customers values (?, ?, ?, ?)");
//                Parameters are numbered starting with 1, and correspond to order of the question marks above
//                1 corresponds to first question mark, and so on...
//                As the first field is an Auto-Incremant field in the database,  specify a null value for it

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setDate( 3, Date.valueOf(birthDate));

            preparedStatement.executeUpdate();

            // Statements allow us to issue SQL queries to the database
            Statement statement = connection.createStatement();

            // Execute the Prepared Statement and get a result set
            ResultSet resultSet = statement.executeQuery("select * from Customers");

            while (resultSet.next())
            {
                // It is possible to get the columns via name (as shown)
                // or via the column number which starts at 1
                // e.g. resultSet.getString(2);
                customerId = resultSet.getInt("custID");
                String fname = resultSet.getString("firstName");
                //String fname = resultSet.getString(2); // same effect as line above
                String lname = resultSet.getString("lastName");
                Date dob = resultSet.getDate("dob");

                System.out.print( customerId + ", ");
                System.out.print( fname + ", ");
                System.out.print( lname + ", ");
                System.out.println( dob );
            }
            System.out.println("Disconnected from database");
        }
        catch (SQLException ex)
        {
            System.out.println("Failed to connect to database - check MySQL is running and that you are using the correct database details");
            ex.printStackTrace();
        }
    }
}
