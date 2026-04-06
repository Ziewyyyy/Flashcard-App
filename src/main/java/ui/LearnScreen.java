package ui;

import database.DeckDAO;

import javax.swing.*;
import java.awt.*;

public class LearnScreen extends JFrame {

    private JLabel amountLabel;
    private JLabel learnedLabel;
    private int deckId;

    public LearnScreen(int deckId, String deckName, int amount, int learned)
    {
        this.deckId = deckId;

        setTitle("Learn - " + deckName);
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 5, 5));

        JLabel nameLabel = new JLabel("Deck: " + deckName);
        amountLabel = new JLabel("Amount: " + amount);
        learnedLabel = new JLabel("Learned: " + learned);

        Font font = new Font("Arial", Font.BOLD, 22);
        nameLabel.setFont(font);
        amountLabel.setFont(font);
        learnedLabel.setFont(font);

        infoPanel.add(nameLabel);
        infoPanel.add(amountLabel);
        infoPanel.add(learnedLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        JButton studyBtn = new JButton("Study Now");
        studyBtn.setFont(new Font("Arial", Font.BOLD, 16));
        studyBtn.setPreferredSize(new Dimension(150, 50));

        studyBtn.putClientProperty("JButton.buttonType", "roundRect");
        studyBtn.putClientProperty("JComponent.arc", 30);
        studyBtn.setFocusPainted(false);

        studyBtn.addActionListener(e -> {
            DeckDAO.updateLearned(deckId, 0);
            dispose();
            new FlashcardScreen(deckId, () -> {
                Object[] deck = DeckDAO.getDeckById(deckId);

                new LearnScreen(
                        deckId,
                        (String) deck[1],
                        (int) deck[2],
                        (int) deck[3]
                );
            });
        });

        buttonPanel.add(studyBtn);

        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        add(mainPanel);
        setVisible(true);
    }

    private void refreshData() {
        SwingUtilities.invokeLater(() -> {
            Object[] deck = DeckDAO.getDeckById(deckId);

            int amount = (int) deck[2];
            int learned = (int) deck[3];

            amountLabel.setText("Amount: " + amount);
            learnedLabel.setText("Learned: " + learned);
        });
    }
}