package ui;

import com.formdev.flatlaf.FlatLightLaf;
import database.CardDAO;
import database.Database;
import database.DeckDAO;
import database.InitDB;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;

public class MainScreen extends JFrame {

    private String dbPath;
    public MainScreen() {
        FlatLightLaf.setup();
        InitDB.init();

        // ===== MENU =====
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu settingsMenu = new JMenu("Settings");

        JMenuItem importItem = new JMenuItem("Import");
        JMenuItem exportItem = new JMenuItem("Export");
        fileMenu.add(importItem);
        fileMenu.add(exportItem);

        exportItem.addActionListener( e -> {
            JFileChooser fileChooser = new JFileChooser();
            if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                try {
                    File file = fileChooser.getSelectedFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                    for(Object[] deck : DeckDAO.getAllDecks())
                    {
                        int deckId = (int) deck[0];
                        String deckName = (String) deck[1];
                        writer.write("DECK|" + deckName);
                        writer.newLine();

                        for(Object[] card : CardDAO.getCardsByDeck(deckId))
                        {
                            String front = (String) card[1];
                            String back = (String) card[2];
                            writer.write("CARD|" + front + "|" + back);
                            writer.newLine();
                        }
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Export success!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        String[] columns = {"ID", "Deck", "Amount", "Learn", "⚙"};
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        importItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    BufferedReader reader = new BufferedReader(new FileReader(file));

                    String line;
                    int currentDeckId = -1;

                    while ((line = reader.readLine()) != null) {

                        if (line.trim().isEmpty()) continue;

                        String[] parts = line.split("\\|");

                        if (parts.length < 2) continue;

                        // DECK
                        if (parts[0].equals("DECK")) {
                            currentDeckId = DeckDAO.insertDeck(parts[1]);
                            model.addRow(new Object[]{currentDeckId, parts[1], 0, 0, "⚙"});
                        }

                        // CARD
                        else if (parts[0].equals("CARD")) {

                            if (parts.length < 3) continue;

                            String front = parts[1];
                            String back = parts[2];

                            CardDAO.insertCard(currentDeckId, front, back);

                            // update amount
                            for (int i = 0; i < model.getRowCount(); i++) {
                                if ((int) model.getValueAt(i, 0) == currentDeckId) {
                                    int amount = (int) model.getValueAt(i, 2);
                                    model.setValueAt(amount + 1, i, 2);
                                }
                            }
                        }
                    }

                    reader.close();
                    JOptionPane.showMessageDialog(this, "Import success!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);


        JTable table = new JTable(model);
        table.setSelectionBackground(new Color(230, 230, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        for(Object[] row : DeckDAO.getAllDecks())
        {
            model.addRow(row);
        }
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.putClientProperty("JTable.showHorizontalLines", true);
        table.putClientProperty("JTable.showVerticalLines", false);

        // ===== HEADER =====
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setReorderingAllowed(false);
        header.putClientProperty("FlatLaf.style",
                "background:#f5f5f5; foreground:#333; height:40");

        // ===== CENTER TEXT =====
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // ===== FIX CỘT ⚙ =====
        table.getColumnModel().getColumn(4).setMaxWidth(40);
        table.getColumnModel().getColumn(4).setMinWidth(40);
        table.getColumnModel().getColumn(4).setResizable(false);

        // ===== SCROLL =====
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 220));

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.putClientProperty("FlatLaf.style",
                "arc:20; border:1,1,1,1,#ddd; background:#fff;");

        // ===== WRAPPER CENTER =====
        JPanel tableWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.add(scrollPane);

        // ===== TOOLBAR =====
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        Font font = new Font("Arial", Font.BOLD, 14);

        for (String name : new String[]{"Decks", "Add", "Browse", "Stats", "Sync"}) {
            JButton btn = new JButton(name);
            btn.setFont(font);
            btn.putClientProperty("JButton.buttonType", "roundRect");
            btn.putClientProperty("JComponent.arc", 20);
            btn.setPreferredSize(new Dimension(100, 40));
            btn.setFocusPainted(false);

            //Add card
            if(name.equals("Add"))
            {
                btn.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();

                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(this, "Please select a deck first!");
                        return;
                    }
                    int deckId = (int) table.getValueAt(selectedRow, 0);

                    JTextField frontField = new JTextField();
                    JTextField backField = new JTextField();
                    JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
                    panel.add(new JLabel("Front:"));
                    panel.add(frontField);
                    panel.add(new JLabel("Back"));
                    panel.add(backField);
                    int result = JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Add Card",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );
                    if (result == JOptionPane.OK_OPTION) {
                        String front = frontField.getText();
                        String back = backField.getText();

                        if (!front.isEmpty() && !back.isEmpty()) {
                            CardDAO.insertCard(deckId, front, back);
                            int currentAmount = (int) table.getValueAt(selectedRow, 2);
                            table.setValueAt(currentAmount + 1, selectedRow, 2);
                            JOptionPane.showMessageDialog(this, "Card Added");
                        }
                    }
                });
            }
            if(name.equals("Browse"))
            {
                btn.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(this, "Please select a deck first!");
                        return;
                    }

                    int deckId = (int) table.getValueAt(selectedRow, 0);
                    String deckName = (String) table.getValueAt(selectedRow, 1);

                    // mở màn hình CardScreen
                    new CardScreen(deckId, deckName);

                });
            }

            toolBar.add(btn);
        }

        add(toolBar, BorderLayout.NORTH);

        // ===== TABLE =====


        // ===== BUTTON CREATE =====
        JButton createBtn = new JButton("Create Deck");
        createBtn.setFont(font);
        createBtn.putClientProperty("JButton.buttonType", "roundRect");
        createBtn.putClientProperty("JComponent.arc", 30);
        createBtn.setPreferredSize(new Dimension(160, 50));

        createBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(
                    this,
                    "Enter deck name:",
                    "Create deck",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (name != null && !name.trim().isEmpty()) {
                int id = DeckDAO.insertDeck(name);
                if(id != -1)
                {
                    model.addRow(new Object[]{id, name, 0, 0, "⚙"});
                }
            }
        });

        //====== BUTTON DELETE ====
        JButton deleteBtn = new JButton("Delete Deck");
        deleteBtn.setFont(font);
        deleteBtn.putClientProperty("JButton.buttonType", "roundRect");
        deleteBtn.putClientProperty("JComponent.arc", 30);
        deleteBtn.setPreferredSize(new Dimension(160, 50));

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if(selectedRow == -1)
            {
                JOptionPane.showMessageDialog(this,"Select a deck first!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this deck?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                int deckId = (int) table.getValueAt(selectedRow, 0);

                DeckDAO.deleteDeck(deckId);

                model.removeRow(selectedRow);

                JOptionPane.showMessageDialog(this, "Deck deleted!");
            }
        });

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomWrapper.setBackground(Color.WHITE);
        bottomWrapper.add(createBtn);
        bottomWrapper.add(deleteBtn);

        // ===== MAIN =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(tableWrapper, BorderLayout.CENTER);
        mainPanel.add(bottomWrapper, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // ===== FRAME =====
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.requestFocusInWindow();
        createBtn.setFocusable(false);
        deleteBtn.setFocusable(false);

        createBtn.setFocusPainted(false);
        deleteBtn.setFocusPainted(false);
    }
}