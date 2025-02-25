// KHONG CHEE LEONG TP075846
// JUSTIN NG KEN HONG TP073469

package apu.hostel.management;

import javax.swing.*;
import apu.hostel.management.APUHostelManagement.User;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;


public class WelcomePageGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public WelcomePageGUI() {
        new APUHostelManagement();
    
        setTitle("APU Hostel Management System");
        setSize(1024, 768); 
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
    
        JPanel roleSelectionPanel = createRoleSelectionPanel();
        JPanel staffAuthCodePanel = createAuthCodePanel("Staff Authorization Code", "StaffLogin");
        JPanel managerAuthCodePanel = createAuthCodePanel("Manager Authorization Code", "ManagerLogin");
        JPanel residentLoginPanel = createLoginPanel("Resident Login Page", "ResidentMenu");
        JPanel staffLoginPanel = createLoginPanel("Staff Login Page", "StaffMenu");
        JPanel residentRegistrationPanel = createRegistrationPanel("Resident Registration", "registerResident");
        JPanel staffRegistrationPanel = createRegistrationPanel("Staff Registration", "registerStaff");
        JPanel managerLoginPanel = createLoginPanel("Manager Login Page", "ManagerMenu");
        JPanel managerRegistrationPanel = createRegistrationPanel("Manager Registration", "registerManager");

    
        mainPanel.add(roleSelectionPanel, "RoleSelection");
        mainPanel.add(staffAuthCodePanel, "StaffAuthCode");
        mainPanel.add(managerAuthCodePanel, "ManagerAuthCode");
        mainPanel.add(residentLoginPanel, "ResidentLogin");
        mainPanel.add(staffLoginPanel, "StaffLogin");
        mainPanel.add(residentRegistrationPanel, "ResidentRegistration");
        mainPanel.add(staffRegistrationPanel, "StaffRegistration");
        mainPanel.add(managerLoginPanel, "ManagerLogin");
        mainPanel.add(managerRegistrationPanel, "ManagerRegistration"); 

    
        add(mainPanel);
        cardLayout.show(mainPanel, "RoleSelection");

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(e -> {
            if (e.isAltDown()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_HOME:
                        cardLayout.show(mainPanel, "RoleSelection");
                        return true;
                    case KeyEvent.VK_LEFT:
                        
                        if (mainPanel.getComponent(0).isVisible()) {
                            return true; 
                        }
                        cardLayout.previous(mainPanel);
                        return true;
                }
            }
            return false;
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int choice = JOptionPane.showConfirmDialog(WelcomePageGUI.this,
                    "Are you sure you want to close this window?", "Confirm Close",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                
            }
        });
    }

    private JPanel createRoleSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                setTitle("APU Hostel Management System - Role Selection");
            }
        });

        JLabel welcomeLabel = new JLabel("Welcome to APU Hostel Management Fees Payment System (AHMFPS)", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28)); 
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(48, 0, 0, 0)); 
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    
        // APU Logo
        JLabel apuLogo = new JLabel(new ImageIcon(new ImageIcon("images/apu_logo.png")
        .getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
        apuLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(apuLogo);
        centerPanel.add(Box.createVerticalStrut(30));

        JLabel roleLabel = new JLabel("Select Your Role:", SwingConstants.CENTER);
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(roleLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(245, 245, 245));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton managerButton = createButton("Manager", "manager_icon.png");
        JButton staffButton = createButton("Staff","staff_icon.png");
        JButton residentButton = createButton("Resident", "resident_icon.png");

        Dimension buttonSize = new Dimension(250, 50);
        Font buttonFont = new Font("Arial", Font.PLAIN, 24);
        
        for (JButton button : new JButton[]{managerButton, staffButton, residentButton}) {
            button.setPreferredSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setFont(buttonFont);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonsPanel.add(button);
            buttonsPanel.add(Box.createVerticalStrut(15));
        }

        managerButton.addActionListener(e -> cardLayout.show(mainPanel, "ManagerAuthCode"));
        staffButton.addActionListener(e -> cardLayout.show(mainPanel, "StaffAuthCode"));
        residentButton.addActionListener(e -> cardLayout.show(mainPanel, "ResidentLogin"));
    
        centerPanel.add(buttonsPanel);
        centerPanel.add(Box.createVerticalStrut(20));
    
        panel.add(centerPanel, BorderLayout.CENTER);

        managerButton.setToolTipText("Login as Manager (Alt+M)");
        staffButton.setToolTipText("Login as Staff (Alt+S)");
        residentButton.setToolTipText("Login as Resident (Alt+R)");

        addButtonHoverEffect(managerButton);
        addButtonHoverEffect(staffButton); 
        addButtonHoverEffect(residentButton);

        managerButton.setMnemonic(KeyEvent.VK_M);  
        staffButton.setMnemonic(KeyEvent.VK_S);    
        residentButton.setMnemonic(KeyEvent.VK_R); 

        return panel;
    }

    private JPanel createAuthCodePanel(String title, String nextCard) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                setTitle("APU Hostel Management System - " + title); 
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = createButton("Back", "back_icon.png");
        backButton.setFont(new Font("Arial", Font.PLAIN, 21)); 
        backButton.setPreferredSize(new Dimension(125, 57)); 
        backButton.setMaximumSize(new Dimension(102, 57)); 
        backButton.setMinimumSize(new Dimension(102, 57)); 
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "RoleSelection"));
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "back");
        panel.getActionMap().put("back", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            
            @Override 
            public void actionPerformed(ActionEvent e) {
                backButton.doClick();
            }
        });
        topPanel.add(backButton, BorderLayout.WEST);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); 

        JTextField authCodeField = new JTextField();
        authCodeField.setFont(new Font("Arial", Font.PLAIN, 24)); 
        authCodeField.setForeground(Color.GRAY);
        authCodeField.setPreferredSize(new Dimension(300, 40));

        authCodeField.setBorder(BorderFactory.createCompoundBorder(
            authCodeField.getBorder(), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        authCodeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (authCodeField.getText().equals(title)) {
                    authCodeField.setText("");
                    authCodeField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (authCodeField.getText().isEmpty()) {
                    authCodeField.setForeground(Color.GRAY);
                    authCodeField.setText(title);
                }
            }
        });

        JButton submitButton = createButton("Submit", "submit_icon.png");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 24)); 
        submitButton.setPreferredSize(new Dimension(150, 50));
        authCodeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitButton.doClick();
                }
            }
        });
        submitButton.addActionListener(e -> {
            String authCode = authCodeField.getText();
            String role = title.contains("Staff") ? "staff" : "manager";
            if (APUHostelManagement.isValidAuthCode(authCode, role)) {
                cardLayout.show(mainPanel, nextCard);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid authorization code. Access denied.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridy = 0;
        centerPanel.add(titleLabel, gbc);
        gbc.gridy = 1;
        centerPanel.add(authCodeField, gbc);
        gbc.gridy = 2;
        centerPanel.add(submitButton, gbc);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                authCodeField.setText(title);
                authCodeField.setForeground(Color.GRAY);
                SwingUtilities.invokeLater(() -> authCodeField.requestFocusInWindow());
            }
        });

        addButtonHoverEffect(backButton);
        addButtonHoverEffect(submitButton);
        backButton.setMnemonic(KeyEvent.VK_B);     
        backButton.setToolTipText("Go back (Alt+B)");
        submitButton.setToolTipText("Submit the authorization code (Alt+S)");
        submitButton.setMnemonic(KeyEvent.VK_S);  

        addFocusHighlight(authCodeField);
        
        return panel;
        }

    private JPanel createLoginPanel(String title, String menuCard) {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = createButton("Back", "back_icon.png");
        backButton.setFont(new Font("Arial", Font.PLAIN, 21));
        backButton.setPreferredSize(new Dimension(125, 57));
        backButton.setMaximumSize(new Dimension(102, 57));
        backButton.setMinimumSize(new Dimension(102, 57));
        
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "RoleSelection"));
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "back");
        panel.getActionMap().put("back", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override 
            public void actionPerformed(ActionEvent e) {
                backButton.doClick();
            }
        });
        topPanel.add(backButton, BorderLayout.WEST);
        panel.add(topPanel, BorderLayout.NORTH);
    
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        centerPanel.add(titleLabel, gbc);
    
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel usernameLabel = createFieldLabel("Username:");
        centerPanel.add(usernameLabel, gbc);
    
        gbc.gridy++;
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 24));
        usernameField.setForeground(Color.GRAY);
        usernameField.setText("Username");
        usernameField.setPreferredSize(new Dimension(300, 40));
        centerPanel.add(usernameField, gbc);
    
        
        usernameField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent evt) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setForeground(Color.GRAY);
                    usernameField.setText("Username");
                }
            }
        });
    
        
        gbc.gridy++;
        JLabel passwordLabel = createFieldLabel("Password:");
        centerPanel.add(passwordLabel, gbc);
    
        gbc.gridy++;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 24));
        passwordField.setForeground(Color.GRAY);
        passwordField.setText("Password");
        passwordField.setEchoChar((char) 0);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setMargin(new Insets(0, 5, 0, 25));
    
        JPanel passwordPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            public void paintBorder(Graphics g) {
                // Override to prevent border painting
            }
        };
        passwordPanel.setPreferredSize(new Dimension(320, 40));
        passwordPanel.setOpaque(false);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        JLabel showHideIcon = new JLabel(new ImageIcon("images/show_icon.png"));
        showHideIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showHideIcon.setPreferredSize(new Dimension(20, 20));
        showHideIcon.setBorder(null);
        showHideIcon.setOpaque(false);
        showHideIcon.setVerticalAlignment(SwingConstants.CENTER);
        passwordPanel.add(showHideIcon, BorderLayout.EAST);
        
        centerPanel.add(passwordPanel, gbc);
    
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('*');
                }
            }
            public void focusLost(FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText("Password");
                    passwordField.setEchoChar((char) 0);
                }
            }
        });
    
        showHideIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (passwordField.getEchoChar() == '*') {
                    passwordField.setEchoChar((char) 0);
                    showHideIcon.setIcon(new ImageIcon(new ImageIcon("images/hide_icon.png")
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                } else {
                    passwordField.setEchoChar('*');
                    showHideIcon.setIcon(new ImageIcon(new ImageIcon("images/show_icon.png")
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }
            }
        });
    
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = createButton("Login", "login_icon.png");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 24));
        loginButton.setPreferredSize(new Dimension(200, 50));
        centerPanel.add(loginButton, gbc);
    
        
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Don't have an account? Register here");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(Color.BLUE);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setFocusPainted(false);
        centerPanel.add(registerButton, gbc);

        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setForeground(new Color(255, 64, 129)); // pink
            }
            public void mouseExited(MouseEvent e) {
                registerButton.setForeground(Color.BLUE);
            }
        });
    
        
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
    
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
    
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User user = null;
        
            
            if (username.equals("Username") || username.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Username field cannot be empty", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                usernameField.requestFocus();
                return;
            }
        
            if (password.equals("Password") || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Password field cannot be empty", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.requestFocus();
                return;
            }
        
            try {
                if (title.equals("Resident Login Page")) {
                    user = APUHostelManagement.loginResident(username, password);
                } else if (title.equals("Staff Login Page")) {
                    user = APUHostelManagement.loginStaff(username, password);
                } else if (title.equals("Manager Login Page")) {
                    user = APUHostelManagement.loginManager(username, password);
                }
        
                if (user != null) {
                    if (user.getIsActive()) {
                        if (user instanceof APUHostelManagement.Resident) {
                            new ResidentMainPageGUI((APUHostelManagement.Resident) user);
                        } else if (user instanceof APUHostelManagement.Staff) {
                            new StaffMainPageGUI((APUHostelManagement.Staff) user);
                        } else if (user instanceof APUHostelManagement.Manager) {
                            new ManagerMainPageGUI((APUHostelManagement.Manager) user);
                        }
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Your account is deactivated. Please contact the administrator.",
                            "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid username or password", 
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "An error occurred during login", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        
        registerButton.addActionListener(e -> {
            if (title.equals("Resident Login Page")) {
                cardLayout.show(mainPanel, "ResidentRegistration");
            } else if (title.equals("Staff Login Page")) {
                cardLayout.show(mainPanel, "StaffRegistration");
            } else if (title.equals("Manager Login Page")) {
                cardLayout.show(mainPanel, "ManagerRegistration");
            }
        });
    
        
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                usernameField.setText("Username");
                usernameField.setForeground(Color.GRAY);
                passwordField.setText("Password");
                passwordField.setForeground(Color.GRAY);
                passwordField.setEchoChar((char) 0);
                showHideIcon.setIcon(new ImageIcon(new ImageIcon("images/show_icon.png")
                    .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
                setTitle("APU Hostel Management System - " + title);
            }
        });
    
        
        addButtonHoverEffect(backButton);
        addButtonHoverEffect(loginButton);
    
        backButton.setMnemonic(KeyEvent.VK_B);
        loginButton.setMnemonic(KeyEvent.VK_L);
        registerButton.setMnemonic(KeyEvent.VK_R);
    
        
        backButton.setToolTipText("Go back (Alt+B)");
        loginButton.setToolTipText("Login (Alt+L)");
        registerButton.setToolTipText("Register (Alt+R)");
        usernameField.setToolTipText("3-12 characters, letters, numbers and underscore only");
        passwordField.setToolTipText("8-12 chars with uppercase, number & special char");
    
        
        addFocusHighlight(usernameField);
        addFocusHighlight(passwordField);

        JLabel decorativeBanner = new JLabel(new ImageIcon("images/hostel_banner.jpg"));
        panel.add(decorativeBanner, BorderLayout.SOUTH);

        // Add subtle background pattern
        panel.setBackground(new Color(245, 245, 245));
    
        panel.add(centerPanel, BorderLayout.CENTER);
    
        return panel;
    }

    private JPanel createRegistrationPanel(String title, String registerMethod) {
        JPanel panel = new JPanel(new BorderLayout());
    
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = createButton("Back", "back_icon.png");
        backButton.setFont(new Font("Arial", Font.PLAIN, 21));
        backButton.setPreferredSize(new Dimension(125, 57));
        backButton.setMaximumSize(new Dimension(102, 57));
        backButton.setMinimumSize(new Dimension(102, 57));
        
        backButton.addActionListener(e -> {
            if (registerMethod.equals("registerManager")) {
                cardLayout.show(mainPanel, "ManagerLogin");
            } else if (registerMethod.equals("registerResident")) {
                cardLayout.show(mainPanel, "ResidentLogin");
            } else if (registerMethod.equals("registerStaff")) {
                cardLayout.show(mainPanel, "StaffLogin");
            }
        });
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "back");
        panel.getActionMap().put("back", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override 
            public void actionPerformed(ActionEvent e) {
                backButton.doClick();
            }
        });
        topPanel.add(backButton, BorderLayout.WEST);
        panel.add(topPanel, BorderLayout.NORTH);
    
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(38, 38, 38, 38));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
    
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        centerPanel.add(titleLabel, gbc);
    
        
        JLabel formatLink = new JLabel("<html><u>View Format Requirements</u></html>");
        formatLink.setHorizontalAlignment(SwingConstants.CENTER);
        formatLink.setForeground(Color.BLUE);
        formatLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formatLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showFormatRequirements();
            }
        });

        formatLink.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                formatLink.setText("<html><a href='' style='color:#FF4081'>View Format Requirements</a></html>");
            }
            public void mouseExited(MouseEvent evt) {
                formatLink.setText("<html><a href=''>View Format Requirements</a></html>");
            }
        });
        centerPanel.add(formatLink, gbc);
    
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(5, 5, 5, 5);
        formGbc.anchor = GridBagConstraints.WEST;

        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.gridwidth = 2;
        JLabel icPassportLabel = createFieldLabel("IC/Passport Number:");
        formPanel.add(icPassportLabel, formGbc);

        formGbc.gridy = 1;
        formGbc.gridwidth = 1;
        JTextField icPassportField = new JTextField();
        icPassportField.setFont(new Font("Arial", Font.PLAIN, 24));
        icPassportField.setForeground(Color.GRAY);
        icPassportField.setText("IC/Passport Number");
        icPassportField.setPreferredSize(new Dimension(300, 40));
        icPassportField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(icPassportField, formGbc);

        formGbc.gridx = 1;
        JLabel icPassportIconLabel = new JLabel();
        icPassportIconLabel.setPreferredSize(new Dimension(30, 30));
        formPanel.add(icPassportIconLabel, formGbc);
    
        
        icPassportField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (icPassportField.getText().equals("IC/Passport Number")) {
                    icPassportField.setText("");
                    icPassportField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent evt) {
                if (icPassportField.getText().isEmpty()) {
                    icPassportField.setForeground(Color.GRAY);
                    icPassportField.setText("IC/Passport Number");
                }
            }
        });
    
        formGbc.gridx = 0;
        formGbc.gridy = 2;
        formGbc.gridwidth = 2;
        JLabel createUsernameLabel = createFieldLabel("Create Username:");
        formPanel.add(createUsernameLabel, formGbc);

        formGbc.gridy = 3;
        formGbc.gridwidth = 1;
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 24));
        usernameField.setForeground(Color.GRAY);
        usernameField.setText("Create Username");
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(usernameField, formGbc);

        formGbc.gridx = 1;
        JLabel usernameIconLabel = new JLabel();
        usernameIconLabel.setPreferredSize(new Dimension(30, 30));
        formPanel.add(usernameIconLabel, formGbc);
    
        usernameField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (usernameField.getText().equals("Create Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent evt) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setForeground(Color.GRAY);
                    usernameField.setText("Create Username");
                }
            }
        });
    
        
        formGbc.gridx = 0;
        formGbc.gridy = 4;
        formGbc.gridwidth = 2;
        JLabel createPasswordLabel = createFieldLabel("Create Password:");
        formPanel.add(createPasswordLabel, formGbc);

        formGbc.gridy = 5;
        formGbc.gridwidth = 1;
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setPreferredSize(new Dimension(300, 40));
        passwordPanel.setOpaque(false);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 24));
        passwordField.setForeground(Color.GRAY);
        passwordField.setText("Create Password");
        passwordField.setEchoChar((char) 0);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        JLabel showHideIcon = new JLabel(new ImageIcon(new ImageIcon("images/show_icon.png")
            .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        showHideIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showHideIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        showHideIcon.setOpaque(false);
        passwordPanel.add(showHideIcon, BorderLayout.EAST);
        
        formPanel.add(passwordPanel, formGbc);
        
        formGbc.gridx = 1;
        JLabel passwordIconLabel = new JLabel();
        passwordIconLabel.setPreferredSize(new Dimension(30, 30));
        formPanel.add(passwordIconLabel, formGbc);

        
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals("Create Password")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('*');
                }
            }
            public void focusLost(FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText("Create Password");
                    passwordField.setEchoChar((char) 0);
                }
            }
        });
    
        showHideIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (passwordField.getEchoChar() == '*') {
                    passwordField.setEchoChar((char) 0);
                    showHideIcon.setIcon(new ImageIcon(new ImageIcon("images/hide_icon.png")
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                } else {
                    passwordField.setEchoChar('*');
                    showHideIcon.setIcon(new ImageIcon(new ImageIcon("images/show_icon.png")
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }
            }
        });
        
        formGbc.gridx = 0;
        formGbc.gridy = 6;
        formGbc.gridwidth = 2;
        JLabel contactNumberLabel = createFieldLabel("Contact Number:");
        formPanel.add(contactNumberLabel, formGbc);

        formGbc.gridy = 7;
        formGbc.gridwidth = 1;
        JTextField contactNumberField = new JTextField();
        contactNumberField.setFont(new Font("Arial", Font.PLAIN, 24));
        contactNumberField.setForeground(Color.GRAY);
        contactNumberField.setText("Contact Number");
        contactNumberField.setPreferredSize(new Dimension(300, 40));
        contactNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(contactNumberField, formGbc);

        formGbc.gridx = 1;
        JLabel contactNumberIconLabel = new JLabel();
        contactNumberIconLabel.setPreferredSize(new Dimension(30, 30));
        formPanel.add(contactNumberIconLabel, formGbc);

        contactNumberField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (contactNumberField.getText().equals("Contact Number")) {
                    contactNumberField.setText("");
                    contactNumberField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent evt) {
                if (contactNumberField.getText().isEmpty()) {
                    contactNumberField.setForeground(Color.GRAY);
                    contactNumberField.setText("Contact Number");
                }
            }
        });

        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(formPanel, gbc);
    
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = createButton("Register", "register_icon.png");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 24));
        registerButton.setPreferredSize(new Dimension(200, 50));
        centerPanel.add(registerButton, gbc);
    
        
        icPassportField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    usernameField.requestFocus();
                }
            }
        });
    
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
    
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    contactNumberField.requestFocus();
                }
            }
        });
    
        contactNumberField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerButton.doClick();
                }
            }
        });
    
        
        registerButton.addActionListener(e -> {
            String icPassportNumber = icPassportField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String contactNumber = contactNumberField.getText();
    
            try {
                if (registerMethod.equals("registerManager")) {
                    APUHostelManagement.registerManager(icPassportNumber, username, password, contactNumber);
                } else if (registerMethod.equals("registerResident")) {
                    APUHostelManagement.registerResident(icPassportNumber, username, password, contactNumber);
                } else if (registerMethod.equals("registerStaff")) {
                    APUHostelManagement.registerStaff(icPassportNumber, username, password, contactNumber);
                }
    
                JOptionPane.showMessageDialog(this, "Registration successful.");
                cardLayout.show(mainPanel, "RoleSelection");
            } catch (Exception ex) {
                showErrorDialog(ex.getMessage());
            }
        });
    
        panel.add(centerPanel, BorderLayout.CENTER);
    
        
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                icPassportField.setText("IC/Passport Number");
                icPassportField.setForeground(Color.GRAY);
                usernameField.setText("Create Username");
                usernameField.setForeground(Color.GRAY);
                passwordField.setText("Create Password");
                passwordField.setForeground(Color.GRAY);
                passwordField.setEchoChar((char) 0);
                contactNumberField.setText("Contact Number");
                contactNumberField.setForeground(Color.GRAY);
                icPassportField.requestFocusInWindow();
            }
        });

        addButtonHoverEffect(backButton);
        addButtonHoverEffect(registerButton);
    
        backButton.setMnemonic(KeyEvent.VK_B);
        registerButton.setMnemonic(KeyEvent.VK_R);
        
        addFocusHighlight(icPassportField);
        addFocusHighlight(usernameField);
        addFocusHighlight(passwordField);
        addFocusHighlight(contactNumberField);

        backButton.setToolTipText("Go back (Alt+B)");
        registerButton.setToolTipText("Register (Alt+R)");
        icPassportField.setToolTipText("IC/Passport number in the format XXXXXX-XX-XXXX");
        usernameField.setToolTipText("3-12 characters, letters, numbers and underscore only");
        passwordField.setToolTipText("8-12 chars with uppercase, number & special char");
        contactNumberField.setToolTipText("Contact number in the format 01X-XXX-XXXX");
    
        
        addValidationListeners(icPassportField, usernameField, passwordField, contactNumberField,
            icPassportIconLabel, usernameIconLabel, passwordIconLabel, contactNumberIconLabel);
    
        return panel;
    }
    
    private void validateICPassport(JTextField field, JLabel iconLabel) {
        String value = field.getText().trim();
        if (!value.equals("IC/Passport Number")) {
            try {
                APUHostelManagement.validateICPassport(value);
                setValidField(field, iconLabel);
            } catch (Exception e) {
                setInvalidField(field, iconLabel, e.getMessage());
            }
        }
    }
    
    private void validateUsername(JTextField field, JLabel iconLabel) {
        String value = field.getText().trim();
        if (!value.equals("Create Username")) {
            try {
                APUHostelManagement.validateUsername(value);
                setValidField(field, iconLabel);
            } catch (Exception e) {
                setInvalidField(field, iconLabel, e.getMessage());
            }
        }
    }
    
    private void validatePassword(JPasswordField field, JTextField usernameField, JLabel iconLabel) {
        String password = new String(field.getPassword()).trim();
        String username = usernameField.getText().trim();
        if (!password.equals("Create Password")) {
            try {
                APUHostelManagement.validatePassword(password, username);
                setValidField(field, iconLabel);
            } catch (Exception e) {
                setInvalidField(field, iconLabel, e.getMessage());
            }
        }
    }
    
    private void validateContactNumber(JTextField field, JLabel iconLabel) {
        String value = field.getText().trim();
        if (!value.equals("Contact Number")) {
            try {
                APUHostelManagement.validateContactNumber(value);
                setValidField(field, iconLabel);
            } catch (Exception e) {
                setInvalidField(field, iconLabel, e.getMessage());
            }
        }
    }
    
    private void setValidField(JComponent field, JLabel iconLabel) {
        field.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        iconLabel.setIcon(new ImageIcon(new ImageIcon("images/green_check_icon.png")
            .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    }
    
    private void setInvalidField(JComponent field, JLabel iconLabel, String message) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED));
        iconLabel.setIcon(new ImageIcon(new ImageIcon("images/red_warning_icon.png")
            .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    }
    
    private void addValidationListeners(JTextField icPassportField, JTextField usernameField, 
        JPasswordField passwordField, JTextField contactNumberField,
        JLabel icPassportIconLabel, JLabel usernameIconLabel, 
        JLabel passwordIconLabel, JLabel contactNumberIconLabel) {
    
        icPassportField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateICPassport(icPassportField, icPassportIconLabel);
            }
        });
        icPassportField.getDocument().addDocumentListener(
            new ValidationListener(() -> validateICPassport(icPassportField, icPassportIconLabel)));
    
        usernameField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateUsername(usernameField, usernameIconLabel);
            }
        });
        usernameField.getDocument().addDocumentListener(
            new ValidationListener(() -> validateUsername(usernameField, usernameIconLabel)));
    
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validatePassword(passwordField, usernameField, passwordIconLabel);
            }
        });
        passwordField.getDocument().addDocumentListener(
            new ValidationListener(() -> validatePassword(passwordField, usernameField, passwordIconLabel)));
    
        contactNumberField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateContactNumber(contactNumberField, contactNumberIconLabel);
            }
        });
        contactNumberField.getDocument().addDocumentListener(
            new ValidationListener(() -> validateContactNumber(contactNumberField, contactNumberIconLabel)));
    }
    
    
    private void showErrorDialog(String message) {
        String userFriendlyMessage = String.format(
            "<html><body style='width: 300px'>" +
            "<h3 style='color: #CC0000'>Error</h3>" +
            "<p>%s</p>" +
            "<hr>" +
            "<p><b>Please check:</b></p>" +
            "<ul>" +
            "<li>Input formats are correct</li>" +
            "<li>Required fields are filled</li>" +
            "</ul>" +
            "</body></html>",
            message
        );
        JOptionPane.showMessageDialog(
            this,
            userFriendlyMessage,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private void addFocusHighlight(JComponent field) {
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            public void focusLost(FocusEvent e) {
                String text = "";
                if (field instanceof JTextField) {
                    text = ((JTextField) field).getText();
                } else if (field instanceof JPasswordField) {
                    text = new String(((JPasswordField) field).getPassword());
                }
                
                if (!text.isEmpty()) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                }
            }
        });
    }
    
    private void showFormatRequirements() {
        String requirements = 
            "<html><body style='width: 300px; padding: 10px;'>" +
            "<h2>Format Requirements</h2>" +
            "<ul>" +
            "<li>IC: XXXXXX-XX-XXXX or Passport: AXXXXXXXX</li>" +
            "<li>Username: 3-12 chars, letters, numbers, underscore (_)</li>" +
            "<li>Password: 8-12 chars, 1 uppercase, 1 number, 1 special (!@#$%^&*())</li>" +
            "<li>Contact: 01X-XXX-XXXX (Example: 012-345-6789)</li>" +
            "</ul></body></html>";
    
        JOptionPane.showMessageDialog(this, requirements, 
            "Format Requirements", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addButtonHoverEffect(JButton button) {
        Color originalColor = button.getBackground();
        Color darkerColor = getDarkerColor(originalColor);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darkerColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(button.getForeground(), 2),
                    BorderFactory.createEmptyBorder(3, 13, 3, 13)
                ));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(button.getForeground(), 1),
                    BorderFactory.createEmptyBorder(4, 14, 4, 14)
                ));
            }
        });
    }
    
    private Color getDarkerColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], Math.min(1f, hsb[1] * 1.1f), Math.max(0, hsb[2] - 0.15f));
    }
    
    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        
        try {
            ImageIcon icon = new ImageIcon(new ImageIcon("images/" + iconPath)
                .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
            button.setIcon(icon);
            button.setIconTextGap(15);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            
            if (text.contains("Back")) {
                button.setBackground(new Color(245, 245, 245));
                button.setForeground(new Color(66, 66, 66));
            } else if (text.contains("Login") || text.contains("Submit")) {
                button.setBackground(new Color(227, 242, 253));
                button.setForeground(new Color(21, 101, 192));
            } else if (text.contains("Register")) {
                button.setBackground(new Color(225, 245, 254));
                button.setForeground(new Color(0, 131, 143));
            } else {
                button.setBackground(new Color(250, 250, 250));
                button.setForeground(new Color(33, 33, 33));
            }
            
            // Add default border matching text color
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(button.getForeground(), 1),
                BorderFactory.createEmptyBorder(4, 14, 4, 14)
            ));
            
            // Add shadow effect
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(button.getForeground(), 1),
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(4, 14, 4, 14),
                    BorderFactory.createEmptyBorder(0, 0, 2, 0)
                )
            ));
            
        } catch (Exception e) {
            System.err.println("Could not load icon: " + iconPath);
        }
        
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomePageGUI frame = new WelcomePageGUI();
            frame.setVisible(true);
        });
    }
}