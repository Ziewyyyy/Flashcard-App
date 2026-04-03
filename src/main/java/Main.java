import ui.WelcomeScreen;
import ui.MainScreen;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.setVisible(true);
        FlatLightLaf.setup();

        Timer timer = new Timer(3000, e -> {
            welcomeScreen.dispose();

            MainScreen mainScreen = new MainScreen();
            mainScreen.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}