package ui;

import com.formdev.flatlaf.FlatLightLaf;
import database.CardDAO;
import database.DeckDAO;
import database.InitDB;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class MainScreen extends JFrame {

    public MainScreen() {
        FlatLightLaf.setup();
        InitDB.init();

        // ===== MENU =====
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu settingsMenu = new JMenu("Settings");

        for (String name : new String[]{"Save", "Save As", "Import", "Export"}) {
            JMenuItem item = new JMenuItem(name);
            if (name.equals("Import")) fileMenu.addSeparator();
            fileMenu.add(item);
        }

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);

        String[] columns = {"ID", "Deck", "Amount", "Learn", "⚙"};
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        JTable table = new JTable(model);
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
    }
}