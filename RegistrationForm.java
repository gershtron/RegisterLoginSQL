import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;


public class RegistrationForm extends JDialog { //step 1 in here; we transferred the RegistrationForm class into a JDialog by adding "extends JDialog" to the syntax

    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField passwordField;
    private JPasswordField ConfirmpasswordField;

    private JPanel registerPanel;
    private JButton registerButton;
    private JButton cancelButton;


    public RegistrationForm(JFrame parent) { // step 2; now we create a constructor, this constructor requires a JFrame which is the frame that will display this dialog
        //step3 we initialise the dialog
        super(parent); //we call the parent constructor which requires a JFrame
        setTitle("Create new account");
        setContentPane(registerPanel);//we display the panel inside this dialog
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent); //this statement allows us to display the dialog in the middle of the frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //step 15

        //step 4 we create an action listener to the register button





        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }//public void actionPerformed
        });//cancelButton.addActionListener

        setVisible(true); //we make the dialog visible (this was moved after step 6 from the above super-parent attributes
    }// public RegistrationForm(JFrame parent)

    private void registerUser() { //step 5,1 this is the method we created above, then also becomes step 8
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(passwordField.getPassword());
        String Confirmpassword = String.valueOf(ConfirmpasswordField.getPassword());

        //step 8,1 if fields are empty we need to show error message
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all the fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;

        } //step 8,2 we need to also check if the password matchup
        if (!password.equals(Confirmpassword)){
            JOptionPane.showMessageDialog( this,
                    "Confirm Password does not match",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }//if (!password.equals(Confirmpassword)

        //step 9 (& step 9.2)
        user = addUserToDatabase (name, email, phone, address,password);
        if (user != null){ //display error message "else" if user null
            dispose();
        }//if (user != null)

        else {
            JOptionPane.showMessageDialog( this,
                    "Failed to register new user",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }//else

    }//private void registerUser()

//step 9,1 implement above

    //step 9,2 we create global variable called user and make it public and initialise variable by adding it to above method ( user = addUserToDatab)
   public User user;
    public User addUserToDatabase (String name, String email, String phone, String address,String password ){
        User user = null;
        // step 10 add variables to allow us to connect to database
        final String DB_URL = "jdbc:mysql://localhost/myStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
           Connection conn = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);


        //step 11 create SQL statement to allow us to create new user
        Statement stmt = conn.createStatement ();
        String sql = "INSERT INTO users (name, email, phone, address, password)" +
                "Values (?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement (sql);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3,phone);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, password);

        //step 12 Insert row into table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }

            stmt.close(); //step 13 close the connections
            conn.close();
    }catch (Exception e){
        e.printStackTrace();
    }//catch

        return user;
    }//private User addUserToDatabase

public static void main(String[] args){
        RegistrationForm myForm = new RegistrationForm(null);
        //step 14 update the registration method
    User user = myForm.user;
    if (user != null){
        System.out.println("Successfull registration of: " +user.name);
    }//if
    else {
        System.out.println("Registrtation Cancelled");
    }//else

}//public static void main
    }//public class RegistrationForm extends JDialog