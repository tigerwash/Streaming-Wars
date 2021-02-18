package streaming.gui;

import streaming.db.DbUtils;
import streaming.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AdminMainView {

    private static final String loginCard = "login";
    private static final String adminCard = "admin";

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel contentPane;
    private LoginPage loginPage;
    private AdminPage adminPage;
    private User user;
    private DbUtils dbUtils;

    public static void main(String[] args) {
        new AdminMainView();
    }

    public AdminMainView() {
        dbUtils = new DbUtils();
        dbUtils.initializeDb();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
                try {
                    createAndShowUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createAndShowUI() throws IOException {
        frame = new JFrame("Streaming War");
        cardLayout = new PageViewer();
        contentPane = new JPanel(cardLayout);

        loginPage = new LoginPage();
        adminPage = new AdminPage(new User(), null, false);

        contentPane.add(loginPage, loginCard);
        contentPane.add(adminPage, adminCard);

        loginPage.login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = loginPage.getUsernameField().getText();
                String password = loginPage.getPasswordField().getText();
                boolean useDb = loginPage.isUseDb();
                user = User.authenticateUser(userName, password);
                if (user != null) {
                    contentPane.remove(adminPage);
                    try {
                        adminPage = new AdminPage(user, dbUtils, useDb);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    contentPane.add(adminPage, adminCard);

                    adminPage.logoutButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frame.dispose();
                            try {
                                createAndShowUI();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                }
                cardLayout.show(contentPane, adminCard);
                frame.pack();
            }
        });

        adminPage.logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                try {
                    createAndShowUI();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        frame.add(contentPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(150, 150);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

}