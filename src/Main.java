import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the map name: ");
        String mapName = scanner.nextLine().trim();

        SwingUtilities.invokeLater(() -> {
            CollisionMaker marker = new CollisionMaker( mapName);
            marker.setTitle("Click tiles to mark walls");
            marker.setVisible(true);
        });
    }
}