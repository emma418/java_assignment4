import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


/*
 * Student Name: Ricky Wong
 * Student ID: N01581738
 * Section: IGA
 * Logic: The class contains the View (GUI) of the program.
 */

/*
 *  ITC5201 – Assignment 4
 *  I declare that this assignment is my own work in accordance with Humber Academic Policy.
 *  No part of this assignment has been copied manually or electronically from any other source
 *  (including web sites) or distributed to other students/social media.
 *  Name: _Ricky Lok Ting Wong__ Student ID: ____N01581738_____ Date: ____2023-07-19______
 */

public class View extends JFrame {

    // Declare GUI components
    private JTextField tfId;
    private JTextField tfLastName;
    private JTextField tfFirstName;
    private JTextField tfMi;
    private JTextField tfAddress;
    private JTextField tfCity;
    private JTextField tfState;
    private JTextField tfTelephone;

    private JLabel lblId;
    private JLabel lblLastName;
    private JLabel lblFirstName;
    private JLabel lblMi;
    private JLabel lblAddress;
    private JLabel lblCity;
    private JLabel lblState;
    private JLabel lblTelephone;

    private JButton btnView;
    private JButton btnInsert;
    private JButton btnUpdate;
    private JButton btnClear;

    private JLabel lblPrompt;

    private JPanel panelData;
    private JPanel panelFirstRow;
    private JPanel panelSecondRow;
    private JPanel panelThirdRow;
    private JPanel panelForthRow;
    private JPanel panelFifthRow;

    private JPanel panelBtn;

    private DBUtils db;



    View() {
        createComponents();
        addComponentsToLayout();
        setFrameConfig();
        getConnection();
    }
    public void getConnection() {
        // Create a new connection
        setPrompt("Database connecting ...");
        try {
            db = new DBUtils();
            setPrompt("Database connected :)");
        } catch (ClassNotFoundException e) {
            setPrompt("Error: Unable to establish database connection - Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            setPrompt("Error: Unable to establish database connection - Please check credentials.");
            e.printStackTrace();
        }
    }

    private void createComponents() {

        // Create labels and text fields
        lblId = new JLabel("ID");
        tfId = new JTextField(12);
        lblLastName = new JLabel("Last Name");
        tfLastName = new JTextField(10);
        lblFirstName = new JLabel("First Name");
        tfFirstName = new JTextField(10);
        lblMi = new JLabel("mi");
        tfMi = new JTextField(3);
        lblAddress = new JLabel("Address");
        tfAddress = new JTextField(15);
        lblCity = new JLabel("City");
        tfCity = new JTextField(15);
        lblState = new JLabel("State");
        tfState = new JTextField(3);
        lblTelephone = new JLabel("Telephone");
        tfTelephone = new JTextField(10);
        lblPrompt = new JLabel("");
        List<JTextField> textFields = Arrays.asList(tfId, tfLastName, tfFirstName, tfMi, tfAddress, tfCity, tfState, tfTelephone);

        // Create buttons
        btnView = new JButton("View");
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = tfId.getText();
                    PreparedStatement pstmtView = db.getPreparedViemStatement();
                    pstmtView.setString(1, id);
                    ResultSet rs = pstmtView.executeQuery();
                    if (rs.next()) {
                        ResultSetMetaData rsmt = rs.getMetaData();
                        int columnCount = rsmt.getColumnCount();
                        for (int i = 1; i <= columnCount - 1; i++){
                            textFields.get(i - 1).setText(rs.getString(i));
                        }
                        setPrompt("Record found!");
                    } else {
                        setPrompt("No record found!");
                        clearTextFields();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        });
        btnInsert = new JButton("Insert");
        btnInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement pstmtInsert = db.getPreparedInsertStatment();
                    String id = tfId.getText();
                    if (db.idExsits(id)) {
                        setPrompt("This ID has been in the table, please try again");
                        return;
                    }
                    else if (tfId.getText().trim().isEmpty()) {
                        setPrompt("ID can not be null, please try again");
                        return;
                    }
                    else {
                        pstmtInsert.setString(1, id);
                        for (int i = 1; i < textFields.size(); i++) {
                            pstmtInsert.setString(i + 1, textFields.get(i).getText());
                        }
                        pstmtInsert.executeUpdate();
                        setPrompt("Insert a new data successfully!");
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        });
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = tfId.getText();
                    PreparedStatement pstmtUpdate = db.getPreparedUpdateStatement();
                    PreparedStatement pstmtViem = db.getPreparedViemStatement();
                    pstmtViem.setString(1, id);
                    ResultSet rs = pstmtViem.executeQuery();
                    if (rs.next()) {
                        for (int i = 1; i < textFields.size(); i++) {
                            if (!textFields.get(i).getText().isEmpty()){
                                pstmtUpdate.setString(i, textFields.get(i).getText());
                            }
                            else {
                                pstmtUpdate.setString(i, rs.getString(i + 1));
                            }
                        }
                        pstmtUpdate.setString(textFields.size(), id);
                        pstmtUpdate.executeUpdate();
                        setPrompt("Update a data successfully!");
                    }
                    else {
                        setPrompt("This record is not included in the table, please try again");
                        return;
                    }

                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        });
        btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = tfId.getText();
                    PreparedStatement pstmtDelete = db.getPreparedDeleteStatement();
                    if (db.idExsits(id)) {
                        pstmtDelete.setString(1, id);
                        pstmtDelete.executeUpdate();
                        setPrompt("The record is deleted successfully");
                        clearTextFields();
                    }
                    else {
                        setPrompt("Record not found");
                        clearTextFields();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        });

        FlowLayout deLayout = new FlowLayout(FlowLayout.LEFT);
        // Create panels
        panelData = new JPanel(deLayout);
        panelFirstRow = new JPanel(deLayout);
        panelSecondRow = new JPanel(deLayout);
        panelThirdRow = new JPanel(deLayout);
        panelForthRow = new JPanel(deLayout);
        panelFifthRow = new JPanel(deLayout);
        panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));

        return;
    }

    public void clearTextFields() {
        tfId.setText("");
        tfLastName.setText("");
        tfFirstName.setText("");
        tfMi.setText("");
        tfCity.setText("");
        tfState.setText("");
        tfAddress.setText("");
        tfTelephone.setText("");
    }

    private void addComponentsToLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        panelData.setLayout(new GridLayout(5, 1));

        addComponents(panelFirstRow, lblId, tfId);
        addComponents(panelSecondRow, lblLastName, tfLastName, lblFirstName, tfFirstName, lblMi, tfMi);
        addComponents(panelThirdRow, lblAddress, tfAddress);
        addComponents(panelForthRow, lblCity, tfCity, lblState, tfState);
        addComponents(panelFifthRow, lblTelephone, tfTelephone);
        addComponents(panelData, panelFirstRow, panelSecondRow, panelThirdRow, panelForthRow, panelFifthRow);
        panelData.setBorder(new TitledBorder(new EtchedBorder(), "Staff Information"));

        gbc.fill = GridBagConstraints.BOTH;
        // set panelData to take 80% of the container height
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.8;
        add(panelData, gbc);

        // set panelBtn to take 20% of the container height
        addComponents(panelBtn, btnView, btnInsert, btnUpdate, btnClear);
        gbc.gridy = 1;
        gbc.weighty = 0.2;
        add(panelBtn, gbc);

        // set lblPrompt to be displayed at the bottom left
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.gridy = 2;
        gbc.weighty = 0;
        add(lblPrompt, gbc);

        pack();
        return;
    }

    private void setFrameConfig() {
        setTitle("");
        setSize(500, 280);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        return;
    }

    private void addComponents(JPanel container, JComponent... args) {
        for (JComponent comp : args) {
            container.add(comp);
        }
        return;
    }

    public void setPrompt(String msg) {
        this.lblPrompt.setText(msg);
    }

}
