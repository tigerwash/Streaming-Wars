package streaming.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LoginPage extends JPanel {

    private JPanel userPanel;
    private JPanel passwordPanel;
    private JPanel buttonsPanel;
    private JPanel fieldsPanel;

    private JLabel usernameLabel;
    private JLabel passwordLabel;

    private JTextField usernameField;
    private JTextField passwordField;

    private JRadioButton dbButton;
    private boolean useDb;

    JButton login;

    public LoginPage() {
        userPanel = new JPanel();
        passwordPanel = new JPanel();
        buttonsPanel = new JPanel();
        fieldsPanel = new JPanel();

        usernameLabel = new JLabel("username:");
        passwordLabel = new JLabel("password:");

        usernameField = new JTextField();
        passwordField = new JTextField();

        login = new JButton("Login");

        this.setLayout(new BorderLayout(10, 15));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        fieldsPanel.setLayout(new BorderLayout());

        usernameField.setColumns(8);
        passwordField.setColumns(8);

        userPanel.add(usernameLabel);
        userPanel.add(usernameField);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        this.setupDbButton();
        dbButton.setActionCommand("useDb");

        fieldsPanel.add(userPanel, BorderLayout.NORTH);
        fieldsPanel.add(passwordPanel, BorderLayout.CENTER);
        fieldsPanel.add(dbButton, BorderLayout.SOUTH);
        buttonsPanel.add(login);

        this.add(fieldsPanel, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.SOUTH);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JTextField getPasswordField() {
        return passwordField;
    }

    public boolean isUseDb() {
        return useDb;
    }

    private void setupDbButton() {
        dbButton = new JRadioButton("Using DB");
        dbButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    useDb = true;
                } else if (state == ItemEvent.DESELECTED) {
                    useDb = false;
                }
            }
        });
    }
}
