package ui;

import javax.swing.*;
import java.awt.*;

public class FinishScreen extends JFrame {

    public FinishScreen(Runnable onClose) {
        setTitle("Completed");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Congratulations!");
        JLabel message = new JLabel("You have finished this deck for now.");

        title.setFont(new Font("Arial", Font.BOLD, 26));
        message.setFont(new Font("Arial", Font.PLAIN, 18));

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton closeBtn = new JButton("Back");
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.setPreferredSize(new Dimension(140, 45));

        closeBtn.putClientProperty("JButton.buttonType", "roundRect");
        closeBtn.putClientProperty("JComponent.arc", 25);
        closeBtn.setFocusPainted(false);

        closeBtn.addActionListener(e -> {
            dispose();
        });

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(message);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(closeBtn);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        setVisible(true);
    }
}