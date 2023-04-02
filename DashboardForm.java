import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame {
    private JPanel DashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public DashboardForm() {

        setTitle("Dashboard");
        setContentPane(DashboardPanel);//we display the panel inside this dialog
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

       boolean hasRegistredUsers = connectToDatabase();
       if (hasRegistredUsers) {
           LoginForm loginForm = new LoginForm( this);
           User user = LoginForm.user;

           if (user != null) {
               lbAdmin.setText("User: " + user.name);
               setLocationRelativeTo(null);
               setVisible(true);
           }// if
           else {
               dispose();
           }//else
       }//if (hasRegistredUsers)

       /* This code checks if there are registered users in the database by calling the connectToDatabase() method.
       If there are registered users, it creates an instance of LoginForm, which is a login form that users can use to enter their credentials.
       The LoginForm is passed a reference to the current DashboardForm object so that the login form can be displayed on top of it.

After the LoginForm is displayed and the user has entered their credentials,
the code checks if the returned User object is not null.
If it is not null, the code sets the text of the lbAdmin label to "User: "
followed by the user's name, and displays the DashboardForm by setting its visibility to true.
If the user cancels or fails to log in, the DashboardForm is disposed by calling the dispose() method.*/

        else { //show registration form
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

/* If connectToDatabase() returns false, indicating that there are no registered users in the database,
the code block within the else statement is executed.

This code creates a new instance of the RegistrationForm class and displays it.
The RegistrationForm class is presumably a form
where new users can register by entering their name, email, phone, address, and a password.

The RegistrationForm constructor takes a DashboardForm object as an argument,
which is used to set the location of the form on the screen.
After the user submits the registration form, the registrationForm.user field is checked to see if a new user has been successfully registered.

If a new user is registered, their name is displayed in the lbAdmin label,
and the dialog is made visible on the screen. If registration is unsuccessful,
the dialog is disposed.*/

            if (user != null) {
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }//if (user != null)
           else {
               dispose();
            }// else
       }// else
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New user: " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }//if (user != null)
            }//public void actionPerformed(ActionEvent e)
        });//btnRegister.addActionListener
    }//public DashboardForm


    private boolean connectToDatabase(){
        boolean hasRegistredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/myStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
            //first, connect to MYSQL server and create the DB if not created
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS myStore");
            statement.close();
            conn.close();

           //second, connect to the DB and create the table users if not created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "User_ID integer PRIMARY KEY AUTO_INCREMENT,"
                    + "name varchar     (40),"
                    + "email varchar    (40),"
                   +  "phone varchar     (40),"
                   + "address varchar  (40),"
                    + "password varchar  (40),"
                    +")";
                    statement.executeUpdate(sql);
/* The above code creates a database table called "users" if it doesn't already exist.
The table has 6 columns: User_ID, name, email, phone, address, and password.
The first column, User_ID, is set as the primary key with the AUTO_INCREMENT attribute,
which means that the database will automatically generate a
unique value for this column each time a new record is inserted into the table.

The remaining columns are defined as varchar data types with a length of 40 characters.
This means that each column can store up to 40 characters of text data.*/


                    //check if we have the users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) from user");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0){ //if the number reg users is higher than 0 we will set the no to true
                    hasRegistredUsers = true;
                }//if (numUsers > 0)
            }//if (resultSet.next())

            statement.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }//catch


        return hasRegistredUsers;



    }//private boolean connectToDatabase

    //create the main method
    public static void main(String[] args){
        //then create an object of type Dashboard Form
        DashboardForm myForm = new DashboardForm();
    }

}//public class DashboardForm extends JFrame