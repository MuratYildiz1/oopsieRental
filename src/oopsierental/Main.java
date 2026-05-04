package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        System.out.println("OOPSIE RENTAL SYSTEM STARTING...");

        SwingUtilities.invokeLater(() -> {
            LoginGUI loginScreen = new LoginGUI();
            loginScreen.setVisible(true);
        });
    }
}