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

public class CardScreen extends JFrame {

    public CardScreen(int deckId, String deckName){

        setTitle("Cards - " + deckName);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.LIGHT_GRAY);

        String[] columns = {"ID", "Front", "Back"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);
        table.setSelectionBackground(new Color(230, 230, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        // Ẩn ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(Color.LIGHT_GRAY);

        //Center Card
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table.setRowHeight(45);

        JScrollPane scrollPane = new JScrollPane(table);
        // thêm vào frame
        add(scrollPane, BorderLayout.CENTER);

        for(Object[] row : CardDAO.getCardsByDeck(deckId))
        {
            model.addRow(row);
        }

        // frame settings
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        JButton editBtn = new JButton("Edit");
        Font font = new Font("Arial", Font.BOLD, 14);
        editBtn.setFont(font);
        editBtn.putClientProperty("JButton.buttonType", "roundRect");
        editBtn.putClientProperty("JComponent.arc", 30);
        editBtn.setPreferredSize(new Dimension(160, 50));

        editBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if(selectedRow == -1)
            {
                JOptionPane.showMessageDialog(this,"Select a card first!");
                return;
            }
            int cardId = (int) table.getValueAt(selectedRow, 0);

            String currentFront = (String) table.getValueAt(selectedRow, 1);
            String currentBack = (String) table.getValueAt(selectedRow, 2);
            JTextField frontField = new JTextField(currentFront);
            JTextField backField = new JTextField(currentBack);

            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
            panel.add(new JLabel("Front:"));
            panel.add(frontField);
            panel.add(new JLabel("Back:"));
            panel.add(backField);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Edit Card",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String front = frontField.getText();
                String back = backField.getText();

                if (!front.isEmpty() && !back.isEmpty()) {

                    // update database
                    CardDAO.updateCard(cardId, front, back);

                    // update UI
                    table.setValueAt(front, selectedRow, 1);
                    table.setValueAt(back, selectedRow, 2);

                    JOptionPane.showMessageDialog(this, "Card updated!");
                }
            }
        });

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(font);
        deleteBtn.putClientProperty("JButton.buttonType", "roundRect");
        deleteBtn.putClientProperty("JComponent.arc", 30);
        deleteBtn.setPreferredSize(new Dimension(160, 50));

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if(selectedRow == -1)
            {
                JOptionPane.showMessageDialog(this,"Select a card first!");
                return;
            }
            int cardId = (int) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this card?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );
            if(confirm == JOptionPane.YES_OPTION)
            {
                CardDAO.deleteCard(cardId);
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Card deleted!");
            }
        });

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomWrapper.setBackground(Color.WHITE);
        bottomWrapper.add(editBtn);
        bottomWrapper.add(deleteBtn);
        add(bottomWrapper, BorderLayout.SOUTH);

        this.requestFocusInWindow();
        editBtn.setFocusable(false);
        deleteBtn.setFocusable(false);

        editBtn.setFocusPainted(false);
        deleteBtn.setFocusPainted(false);
    }
}
