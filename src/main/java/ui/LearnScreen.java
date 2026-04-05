package ui;

import javax.swing.*;
import java.awt.*;

public class LearnScreen extends JFrame {
    public LearnScreen(int deckId, String deckName, int amount, int learned)
    {
        setTitle("Learn - " + deckName);
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new BorderLayout());

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 5, 5));

        JLabel nameLabel = new JLabel("Deck: " + deckName);
        JLabel amountLabel = new JLabel("Amount: " + amount);
        JLabel learnedLabel = new JLabel("Learned: " + learned);

        Font font = new Font("Arial", Font.BOLD, 22);
        nameLabel.setFont(font);
        amountLabel.setFont(font);
        learnedLabel.setFont(font);

        infoPanel.add(nameLabel);
        infoPanel.add(amountLabel);
        infoPanel.add(learnedLabel);

        //Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        JButton studyBtn = new JButton("Study Now");
        studyBtn.setFont(new Font("Arial", Font.BOLD, 16));
        studyBtn.setPreferredSize(new Dimension(150, 50));

        studyBtn.putClientProperty("JButton.buttonType", "roundRect");
        studyBtn.putClientProperty("JComponent.arc", 30);
        studyBtn.setFocusPainted(false);

        studyBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Start Learning...");
        });

        buttonPanel.add(studyBtn);

        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        add(mainPanel);
        setVisible(true);
    }
}