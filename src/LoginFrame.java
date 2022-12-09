import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class LoginFrame  implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton submitButton;
    JFrame loginFrame;
    JLabel loginLabel;
    public static String folderName;
    public LoginFrame (){
        //frame init
        loginFrame = new JFrame();
        loginFrame.setSize(400, 320);
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.getContentPane().setBackground(new Color(171, 206, 215));

        //placing the components
        loginLabel = new JLabel("Login");
        loginLabel.setBounds(180, 60, 100, 20);
        loginFrame.add(loginLabel);

        //text field
        usernameField = new JTextField();
        usernameField.setBounds(150, 100, 100, 25);
        loginFrame.add(usernameField);

        //password field
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 140, 100, 25);
        loginFrame.add(passwordField);

        //login button
        submitButton = new JButton("Login");
        submitButton.setBounds(160, 180, 80, 20);
        submitButton.addActionListener(this);
        loginFrame.add(submitButton);

        //frame init(cont.)
        loginFrame.setLayout(null);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == submitButton) {
                //file manipulate
                File file = new File("src/authentication-details.txt");
                BufferedReader reader = new BufferedReader(new FileReader("src/authentication-details.txt"));
                String line;

                //initial string to collect username and password
                String user = "";
                String pass = "";
                while((line = reader.readLine()) != null){
                    if(usernameField.getText().equals(line)){
                        user = line;
                    }
                    if(passwordField.getText().equals(line)){
                        pass = line;
                    }
                }
                reader.close();

                //get each character from passwordField it return char
                char[] passwordCollected = passwordField.getPassword();
                String passField = "";
                for(char p : passwordCollected){
                    passField += p;
                }

                //authentication condition
                if(usernameField.getText().equals(user) && passField.equals(pass)) {
                    JOptionPane.showMessageDialog(null, "Login Successfully!");
                    try {
                        folderName = user;
                        AppFrame appFrame = new AppFrame();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    loginFrame.dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Incorrect username or password!");
                }
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }
}
