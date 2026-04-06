package ui;

import database.CardDAO;
import database.DeckDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FlashcardScreen extends JFrame {

    private JLabel frontLabel;
    private JLabel backLabel;
    private JLabel statsLabel;

    private List<Object[]> cards;
    private int currentIndex = 0;

    private int deckId;
    private Runnable onClose;

    public FlashcardScreen(int deckId, Runnable onClose) {
        this.deckId = deckId;
        this.onClose = onClose;

        setTitle("Flashcard");
        setSize(1080, 720);
        setLocationRelativeTo(null);

        cards = CardDAO.getCardsByDeck(deckId);

        if (cards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards in this deck!");
            dispose();
            return;
        }

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // ===== CARD PANEL =====
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

        frontLabel = new JLabel("");
        backLabel = new JLabel("");

        frontLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        frontLabel.setFont(new Font("Arial", Font.BOLD, 40));
        backLabel.setFont(new Font("Arial", Font.BOLD, 40));

        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(frontLabel);
        cardPanel.add(Box.createVerticalStrut(20));

        // ===== SEPARATOR =====
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 2));
        cardPanel.add(separator);
        cardPanel.add(Box.createVerticalStrut(20));

        cardPanel.add(backLabel);
        cardPanel.add(Box.createVerticalGlue());

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // ===== STATS =====
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statsLabel.setHorizontalAlignment(JLabel.CENTER);
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // ===== BUTTON =====
        JPanel buttonPanel = new JPanel();

        JButton showBtn = new JButton("Show Answer");
        JButton nextBtn = new JButton("Next");

        for (JButton btn : new JButton[]{showBtn, nextBtn}) {
            btn.setPreferredSize(new Dimension(160, 50));
            btn.putClientProperty("JButton.buttonType", "roundRect");
            btn.putClientProperty("JComponent.arc", 30);
            btn.setFocusPainted(false);
        }

        showBtn.addActionListener(e -> showAnswer());
        nextBtn.addActionListener(e -> nextCard());

        buttonPanel.add(showBtn);
        buttonPanel.add(nextBtn);

        bottomPanel.add(statsLabel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(buttonPanel);

        // ===== ADD =====
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadCard();

        setVisible(true);
    }

    // ===== LOAD CARD =====
    private void loadCard() {
        Object[] card = cards.get(currentIndex);

        String front = (String) card[1];
        frontLabel.setText(front);

        backLabel.setText("");

        updateStats();
    }

    // ===== SHOW ANSWER =====
    private void showAnswer() {
        Object[] card = cards.get(currentIndex);
        String back = (String) card[2];

        backLabel.setText(back);
    }

    // ===== NEXT CARD =====
    private void nextCard() {
        currentIndex++;

        if (currentIndex >= cards.size()) {
            DeckDAO.updateLearned(deckId, cards.size());

            new FinishScreen(() -> {
                if (onClose != null) onClose.run();
            });

            dispose();
            return;
        }

        DeckDAO.updateLearned(deckId, currentIndex);

        loadCard();
    }

    // ===== UPDATE STATS =====
    private void updateStats() {
        int learned = currentIndex;
        int remaining = cards.size() - currentIndex;

        statsLabel.setText(
                "<html><b>Learned:</b> " + learned +
                        " &nbsp;&nbsp;&nbsp; <b>Remaining:</b> " + remaining + "</html>"
        );
    }
}