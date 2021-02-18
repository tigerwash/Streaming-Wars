package streaming.gui;

import streaming.AdminSystem;
import streaming.db.DbUtils;
import streaming.user.User;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.PrintStream;

public class AdminPage extends JPanel {
    private JLabel nameLabel;
    private JLabel roleLabel;
    private JPanel userPanel;
    private JPanel controlPanel;
    private JPanel displayPanel;
    private JPanel fieldsPanel;
    private JTextArea outputArea;
    private JTextArea inputArea;
    private JButton buttonSubmit;
    private JButton buttonClear;
    private JRadioButton verboseButton;
    private PrintStream standardOut;
    private AdminSystem adminSystem;
    private String userName;
    private String userRole;
    private boolean isVerbose;

    JButton logoutButton;

    public AdminPage(User user, DbUtils dbUtils, boolean useDb) throws IOException {
        this.adminSystem = AdminSystem.getInstance();
        this.adminSystem.setUser(user);
        this.adminSystem.setUseDb(useDb);
        this.adminSystem.setDbUtils(dbUtils);
        this.userName = user.getName() == null || user.getName().equals("") ? "guest": user.getName();
        this.userRole = user.getRole() == null || user.getName().equals("") ? "guest": user.getRole().getName();
        this.isVerbose = false;
        this.setupFrameLayout();
    }

    public void setupFrameLayout() {

        // user info panel
        userPanel = new JPanel();
        nameLabel = new JLabel("User: " + this.userName);
        roleLabel = new JLabel("   Role: " + this.userRole + "   ");
        this.setupVerboseButton();
        verboseButton.setActionCommand("verbose");
        logoutButton = new JButton("Logout");
        userPanel.add(nameLabel);
        userPanel.add(roleLabel);
        userPanel.add(verboseButton);
        userPanel.add(logoutButton);

        controlPanel = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        // add submit button
        constraints.gridx = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;
        this.setupSubmitButton();
        controlPanel.add(buttonSubmit, constraints);

        // add clear button
        constraints.gridx = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;
        this.setupClearButton();
        controlPanel.add(buttonClear, constraints);

        // add input area
        constraints.gridx = 2;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(10, 10, 10, 10);
        inputArea = new JTextArea(1, 85);
        inputArea.setLineWrap(true);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(new JScrollPane(inputArea), constraints);

        // add display area
        displayPanel = new JPanel();
        outputArea = new JTextArea(20, 100);
        outputArea.setEditable(false);
        PrintStream printStream = new PrintStream(new StdoutToTextAreaStream(outputArea));
        standardOut = System.out;
        System.setOut(printStream);
        System.setErr(printStream);
        displayPanel.add(new JScrollPane(outputArea));

        // add all panel to field with vertical layout
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(userPanel);
        fieldsPanel.add(controlPanel);
        fieldsPanel.add(displayPanel);

        this.add(fieldsPanel);
    }

    private void setupVerboseButton() {
        verboseButton = new JRadioButton("Verbose");
        verboseButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    isVerbose = true;
                } else if (state == ItemEvent.DESELECTED) {
                    isVerbose = false;
                }
            }
        });
    }

    private void setupClearButton() {
        buttonClear = new JButton("Clear");
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    inputArea.getDocument().remove(0,
                            inputArea.getDocument().getLength());
                    standardOut.println("Text area cleared");
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setupSubmitButton() {
        buttonSubmit = new JButton("Submit");
        buttonSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    adminSystem.processSingleLine(inputArea.getText(), isVerbose);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    inputArea.getDocument().remove(0,
                            inputArea.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setUser(User user) {
        this.adminSystem.setUser(user);
        this.userName = user.getName() == null || user.getName().equals("") ? "guest": user.getName();
        this.userRole = user.getRole() == null || user.getName().equals("") ? "guest": user.getRole().getName();
    }
}
