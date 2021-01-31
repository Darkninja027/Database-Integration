import java.io.File;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;

//TODO;
// - use a regular expression for email validation
// - add a character limit for passwords
// - check to see if username exists: DONE
// - Create a Validation class to make main code more concise 

/*================================================================================================
    The GUi That will show a window on the screen and allow the user to be able to interact with it
=================================================================================================*/
class LoginGUI implements ActionListener, FocusListener {

    // = The main components for the overall form =\\
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    // =============================================\\

    // = The Components for the welcome form =\\
    JButton Register = new JButton("Register");
    JButton Login = new JButton("Login");
    // =======================================\\

    // = Components for the registration form =\\
    JButton register_Back = new JButton("Back");
    JButton register_Submit = new JButton("Register");
    JTextField tUser = new JTextField();
    JPasswordField tPass = new JPasswordField();
    JTextField tEmail = new JTextField();
    JTextField tDateDay = new JTextField("D");
    JTextField tDateMonth = new JTextField("M");
    JTextField tDateYear = new JTextField("Y");
    JLabel userError = new JLabel("Please enter a Username");
    JLabel passError = new JLabel("Please enter a Password");
    JLabel emailError = new JLabel("Please enter a valid Email");
    JLabel dateError = new JLabel("Please enter a valid date");
    Font f = new Font("Serif", Font.PLAIN, 10);
    // ========================================\\

    // = Placeholder for creating a new User =\\
    User user;
    Validators formValidator;
    // =======================================\\

    /*
     * =============================================================================
     * ================================== Constructor for the main form this is how
     * the form is constructed depending on which state of the application is
     * currently in
     * =============================================================================
     * ===================================
     */
    public LoginGUI(Validators val) {
        formValidator = val;
        // = Code to set up the first form that originally shows up on applocation start
        // =\\
        frame.setTitle("Welcome");
        frame.setSize(300, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        panel.setLayout(null);
        frame.add(panel);

        Register.setBounds(95, 50, 100, 50);
        Login.setBounds(95, 125, 100, 50);
        Register.addActionListener(this);
        Login.addActionListener(this);
        panel.add(Register);
        panel.add(Login);

        frame.setVisible(true);
        // ==============================================================================\\
    }

    /*
     * =========================================================== Actions performed
     * when specific Buttons have been pressed.
     * ===========================================================
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        e.getSource();
        if (e.getSource() == Register) {
            registrationSetup();
        } else if (e.getSource() == Login) {
            // do something
            setupLogin();
        } else if (e.getSource() == register_Back) {
            welcomeSetup();
        } else if (e.getSource() == register_Submit) {
            boolean filledFields = checkFields();
            if (filledFields) {
                if (validateForm()) {
                    user = new User(tUser.getText(), tPass.getText(), tEmail.getText(),
                            tDateDay.getText() + "-" + tDateMonth.getText() + "-" + tDateYear.getText(), "Users.json");
                    user.hashPass(user.getPass());
                    user.submit("Users.json");
                    showSuccess(1);
                }
            }

        }

    }

    /*
     * =============================================================================
     * =============================================== The focus events used so that
     * the date pricker with user creation has placeholder text and the placeholder
     * text disappears
     * =============================================================================
     * ===============================================
     */
    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == tDateDay) {
            tDateDay.setForeground(Color.BLACK);
            tDateDay.setText("");
        } else if (e.getSource() == tDateMonth) {
            tDateMonth.setForeground(Color.BLACK);
            tDateMonth.setText("");
        } else if (e.getSource() == tDateYear) {
            tDateYear.setForeground(Color.BLACK);
            tDateYear.setText("");
        }
    }

    /*
     * =============================================================================
     * ============================================= if the date picker for user
     * creation textfields are deselected and left blank the placeholder text is
     * then reinitialised
     * =============================================================================
     * =============================================
     */
    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == tDateDay && tDateDay.getText().equals("")) {
            tDateDay.setText("D");
            tDateDay.setForeground(Color.LIGHT_GRAY);
        } else if (e.getSource() == tDateMonth && tDateMonth.getText().equals("")) {
            tDateMonth.setText("M");
            tDateMonth.setForeground(Color.LIGHT_GRAY);

        } else if (e.getSource() == tDateYear && tDateYear.getText().equals("")) {
            tDateYear.setText("Y");
            tDateYear.setForeground(Color.LIGHT_GRAY);
        }

    }

    /*
     * =============================================================================
     * =============== The set of instructions that sets up the form in order for
     * the user to enter a new account
     * =============================================================================
     * ================
     */
    private void registrationSetup() {

        // = resets and formats the new form =\\
        panel.removeAll();
        frame.setSize(290, 300);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Registration");
        // ===================================\\

        // = Adds the logic of the buttons and adds the buttons to the panel =\\
        register_Back.setBounds(20, 220, 90, 20);
        register_Back.addActionListener(this);
        panel.add(register_Back);

        register_Submit.setBounds(160, 220, 90, 20);
        register_Submit.addActionListener(this);
        panel.add(register_Submit);

        // ==================================================================\\

        // = adds the given labels to the form =\\
        // = Error labels are declared globally =\\

        JLabel lUser = new JLabel("Username:");
        lUser.setBounds(20, 50, 100, 50);

        JLabel lPass = new JLabel("Password:");
        lPass.setBounds(20, 90, 150, 50);

        JLabel lEmail = new JLabel("Email:");
        lEmail.setBounds(20, 130, 100, 50);

        JLabel lDOB = new JLabel("Date of Birth:");
        lDOB.setBounds(20, 170, 100, 50);

        userError.setBounds(100, 65, 100, 50);
        userError.setFont(f);
        userError.setForeground(Color.RED);
        userError.setVisible(false);

        passError.setBounds(100, 105, 100, 50);
        passError.setFont(f);
        passError.setForeground(Color.RED);
        passError.setVisible(false);

        emailError.setBounds(100, 145, 100, 50);
        emailError.setFont(f);
        emailError.setForeground(Color.RED);
        emailError.setVisible(false);

        dateError.setBounds(100, 185, 100, 50);
        dateError.setFont(f);
        dateError.setForeground(Color.RED);
        dateError.setVisible(false);

        panel.add(lUser);
        panel.add(lPass);
        panel.add(lEmail);
        panel.add(lDOB);
        panel.add(userError);
        panel.add(passError);
        panel.add(emailError);
        panel.add(dateError);
        // ========================================\\

        // = TextFields for user input setup and positioning =\\
        tUser.setBounds(100, 65, 150, 20);
        tPass.setBounds(100, 105, 150, 20);
        tEmail.setBounds(100, 145, 150, 20);
        tDateDay.setBounds(100, 185, 33, 20);
        tDateMonth.setBounds(140, 185, 33, 20);
        tDateYear.setBounds(180, 185, 33, 20);

        tDateDay.setForeground(Color.LIGHT_GRAY);
        tDateMonth.setForeground(Color.LIGHT_GRAY);
        tDateYear.setForeground(Color.LIGHT_GRAY);

        tDateDay.addFocusListener(this);
        tDateMonth.addFocusListener(this);
        tDateYear.addFocusListener(this);

        panel.add(tUser);
        panel.add(tPass);
        panel.add(tEmail);
        panel.add(tDateDay);
        panel.add(tDateMonth);
        panel.add(tDateYear);
        // ===================================================\\
    }

    /*
     * ================================== Reinitializes the welcome form
     * ===================================
     */
    private void welcomeSetup() {
        clearRegistration();
        frame.setTitle("Welcome");
        frame.setSize(300, 350);
        frame.setLocationRelativeTo(null);
        panel.removeAll();
        panel.add(Register);
        panel.add(Login);
    }

    private void showSuccess(int state) {
        JFrame f = new JFrame("Success");
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.CENTER);
        JPanel p = new JPanel(layout);
        JButton b = new JButton("Done");
        JLabel l = new JLabel("Registration was successful");
        f.setSize(250,100);
        if(state == 2){
            l.setText("Login Successful");
            f.setSize(220,100);
        }
        
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.add(p);
        
        p.add(l);
        p.add(b);
        b.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(state == 2){
                    System.exit(0);
                }
                else{
                    f.dispose();
                    welcomeSetup();
                }
            }
            
        });
        
        
    }

    private void clearRegistration(){
        tUser.setText("");
        tPass.setText("");
        tEmail.setText("");
        tDateDay.setText("D");
        tDateDay.setForeground(Color.DARK_GRAY);
        tDateMonth.setText("M");
        tDateMonth.setForeground(Color.DARK_GRAY);
        tDateYear.setText("Y");
        tDateYear.setForeground(Color.DARK_GRAY);
    }

    private void setupLogin(){
        panel.removeAll();

        frame.setSize(300,200);
        frame.setResizable(false);
        frame.setTitle("Login");

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBack = new JButton("Back");
        JButton loginSubmit = new JButton("Login");

        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        JLabel Error = new JLabel("Username / Password incorrect"); 

        panel.add(userField);
        panel.add(passField);
        panel.add(loginBack);
        panel.add(loginSubmit);
        panel.add(userLabel);
        panel.add(passLabel);
        panel.add(Error);

        userLabel.setBounds(50, 0, 200, 20);
        userLabel.setFont(f);
        userField.setBounds(50, 20, 200, 20);
        passLabel.setBounds(50, 40, 200, 20);
        passLabel.setFont(f);
        passField.setBounds(50,60,200,20);
        loginBack.setBounds(50,100,80,20);
        loginSubmit.setBounds(170, 100, 80, 20);
        loginBack.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                welcomeSetup();
            }
            
        });

        loginSubmit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Validators val = new Validators("User.json");
                if(val.validateLogin("Users.json", userField.getText(), passField.getText())){
                    showSuccess(2);
                }
                else{
                    Error.setVisible(true);
                }
            }
            
        });

        Error.setFont(f);
        Error.setForeground(Color.RED);
        Error.setBounds(50, 80, 200, 20);
        Error.setVisible(false);

    }

    private boolean validateForm(){
        boolean validUsername = formValidator.validateUsername(tUser.getText());
        boolean validDate = formValidator.validateDate(tDateDay.getText(), tDateMonth.getText(), tDateYear.getText());
        boolean validPassword = formValidator.validatePassword(tPass.getText());
        boolean validEmail = formValidator.validateEmail(tEmail.getText());
        boolean validform = true;
        
        if(!validUsername){
            userError.setText("Username already exists");
            userError.setVisible(true);
            validform = false;
        }
        if(!validDate){
            dateError.setVisible(true);
            validform = false;
        }
        if(!validPassword){
            passError.setText("Password must be 8 characters");
            passError.setVisible(true);
            validform = false;
        }
        if(!validEmail){
            emailError.setVisible(true);
            validform = false;
        }

        return validform;
    }


    public User getUser() {
        return user;
    }
    
    private boolean checkFields(){

        userError.setVisible(false);
        passError.setVisible(false);
        emailError.setVisible(false);
        dateError.setVisible(false);

        if(tUser.getText().equals("")){
            userError.setVisible(true);
            userError.setText("Please Enter a username");
            return false;
        }
        if(tPass.getText().equals("")){
            passError.setVisible(true);
            passError.setText("Please enter a password");
            return false;
        }
        if(tEmail.getText().equals("")){
            emailError.setVisible(true);
            return false;
        }
        if(tDateDay.getText().equals("D")|| tDateMonth.getText().equals("M")||tDateYear.getText().equals("Y")){
            dateError.setVisible(true);
            return false;
        }
        return true;
    }
}

public class Threading{
    static String filename = "Users.json";
    static Validators val;
    public static void main(String[]args){
        fileInit(filename);
        val = new Validators(filename);
        LoginGUI gui = new LoginGUI(val);
    }
    private static void fileInit(String fName){
        try {
            File f = new File(fName);
            if(!f.exists()){
                f.createNewFile();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

