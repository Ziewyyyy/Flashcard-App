package ui;
import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    public class RoundedPanel extends JPanel {
        public RoundedPanel() {
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(240, 240, 240));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            g2.dispose();
            super.paintComponent(g);
        }
    }
    public MainScreen() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu settingsMenu = new JMenu("Settings");

        for (String name : new String[]{"Save", "Save As", "Import", "Export"})
        {
            JMenuItem item = new JMenuItem(name);
            if (name.equals("Import")) {
                fileMenu.addSeparator();
            }
            fileMenu.add(item);
        }

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);

        setJMenuBar(menuBar);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        Dimension size = new Dimension(100, 40);
        Font font = new Font("Arial", Font.BOLD, 14);

        for (String name : new String[]{"Decks", "Add", "Browse", "Stats", "Sync"}) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setPreferredSize(size);
            roundedPanel.setLayout(new BorderLayout());

            JButton btn = new JButton(name);
            btn.setFont(font);
            btn.setForeground(new Color(50, 50, 50));

            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            roundedPanel.add(btn, BorderLayout.CENTER);

            toolBar.add(roundedPanel);
        }

        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setPreferredSize(new Dimension(150, 50));
        roundedPanel.setLayout(new BorderLayout());

        JButton createBtn = new JButton("Create Deck");
        createBtn.setFont(font);
        createBtn.setForeground(new Color(50, 50, 50));

        createBtn.setContentAreaFilled(false);
        createBtn.setBorderPainted(false);
        createBtn.setFocusPainted(false);

        roundedPanel.add(createBtn, BorderLayout.CENTER);

        add(toolBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(roundedPanel, BorderLayout.SOUTH);
        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomWrapper.setBackground(Color.WHITE);

        bottomWrapper.add(roundedPanel);

        mainPanel.add(bottomWrapper, BorderLayout.SOUTH);

        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}