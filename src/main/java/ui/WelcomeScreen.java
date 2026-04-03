package ui;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JWindow {

    public WelcomeScreen() {

        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#006d77"));

        JLabel label = new JLabel("Flashcard App", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(Color.decode("#83c5be"));

        topPanel.add(label, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.decode("#edf6f9"));
        bottomPanel.setPreferredSize(new Dimension(1080, 360));

        root.add(topPanel, BorderLayout.CENTER);
        root.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(root);

        setSize(1080, 720);
        setLocationRelativeTo(null);
    }
}