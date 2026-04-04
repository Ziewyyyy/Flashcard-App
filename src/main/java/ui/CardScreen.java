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
    }
}
