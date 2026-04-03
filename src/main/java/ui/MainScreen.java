package ui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class MainScreen extends JFrame {

    public MainScreen() {
        FlatLightLaf.setup();

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
            toolBar.add(btn);
        }

        add(toolBar, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"Deck", "Amount", "Learn", "⚙"};
        Object[][] data = {
                {"English", 120, 10, "⚙"},
                {"Japanese", 80, 5, "⚙"},
                {"Math", 200, 25, "⚙"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);

        // 👉 chỉ scroll dọc
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
        table.getColumnModel().getColumn(3).setMaxWidth(40);
        table.getColumnModel().getColumn(3).setMinWidth(40);
        table.getColumnModel().getColumn(3).setResizable(false);

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
                model.addRow(new Object[]{name, 0, 0, "⚙"});
            }
        });

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomWrapper.setBackground(Color.WHITE);
        bottomWrapper.add(createBtn);

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