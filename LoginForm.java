import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog { //1
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel LoginPanel;

    public LoginForm(JFrame parent){
        super(parent); //2 we call the parent constructor which requires a JFrame
        setTitle("Create new account");
        setContentPane(LoginPanel);//we display the panel inside this dialog
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent); //this statement allows us to display the dialog in the middle of the frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

               user = getAuthenticatedUser (email,password); //this method allows us to check of all details are ok

                if (user != null) {
                    dispose();
                }//if
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                }// else
            }// public void actionPerformed
        });//btnOK.addActionListener

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }// public void actionPerformed
        });//btnCancel.addActionListener

        setVisible(true);
    }//public LoginForm

    public static User user; //we create a global user here and initiate it to the method above getAuthenticated
    private User getAuthenticatedUser (String email, String password) {//method above here initiated, to authenticate user details in database
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost/myStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);

            Statement stmt = conn.createStatement (); //to see if these two user detail parameters is in the database
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement (sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery(); //preparedstatement allows us to execute SQL query and result will be stored in resultset variable
/*This code block sets up a connection to the database and creates a prepared statement to select user details
from the database where the email and password match the provided email and password.
The statement uses placeholders (?) to represent the values to be filled in later with the setString() method.

The executeQuery() method is then called on the prepared statement to execute the SQL query.
The result of the query is stored in the resultSet variable,
which can be used to retrieve the selected rows from the database. */



           if (resultSet.next()) { //if we found the user in the datebase we need to create the user object and to fill it with the details in the DB
               user = new User();
               user.name = resultSet.getString("name");
               user.email = resultSet.getString("email");
               user.phone = resultSet.getString("phone");
               user.address = resultSet.getString("address");
               user.password = resultSet.getString("password");
           }//if (resultSet.next())

            //and we close the connection with the database again
            stmt.close();
           conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }//catch




            return user;
    }// private User getAuthenticatedUser



    public static void main(String[] args) {//3
        LoginForm loginForm = new LoginForm(null);
        User user = LoginForm.user;
        if (user != null){
            System.out.println("Successfull Authentication of: " +user.name);
            System.out.println("            Email: " +user.email);
            System.out.println("            Phone: " +user.phone);
            System.out.println("          Address: " +user.address);
        }//if
        else {
            System.out.println("Authentication Cancelled");
        }//else

    }// public static void main
}//public class LoginForm extends JDialog
