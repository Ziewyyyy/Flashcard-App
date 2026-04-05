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
import java.util.List;

public class FlashcardScreen extends JFrame{
    private JLabel frontLabel;
    private JLabel backLabel;
    private List<Object[]> cards;
    private int currentIndex = 0;
    public FlashcardScreen(int deckId)
    {
        setTitle("Flashcard");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        cards = CardDAO.getCardsByDeck(deckId);

        if (cards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards in this deck!");
            dispose();
            return;
        }

        //Card Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

        frontLabel = new JLabel("");
        backLabel = new JLabel("");

        frontLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        frontLabel.setFont(new Font("Arial", Font.BOLD, 28));
        backLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(frontLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        cardPanel.add(backLabel);
        cardPanel.add(Box.createVerticalGlue());

        //Button
        JPanel bottomPanel = new JPanel();

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

        bottomPanel.add(showBtn);
        bottomPanel.add(nextBtn);

        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadCard();

        setVisible(true);
    }

    private void loadCard() {
        Object[] card = cards.get(currentIndex);

        String front = (String) card[1];
        String back = (String) card[2];

        frontLabel.setText(front);
        backLabel.setText("");
    }

    private void showAnswer() {
        Object[] card = cards.get(currentIndex);
        String back = (String) card[2];

        backLabel.setText(back);
    }

    private void nextCard() {
        currentIndex++;

        if (currentIndex >= cards.size()) {
            JOptionPane.showMessageDialog(this, "You finished!");
            dispose();
            return;
        }

        loadCard();
    }
}
