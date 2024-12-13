
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;


public class CollisionMaker extends JFrame {

    String mapName;

    BufferedImage map;
    boolean[][] collisionMap;
    public JPanel panel;

    final int TILE_SIZE = 16;
    final int SCALE = 2;

    public CollisionMaker(String mapName) {
        this.mapName = mapName;
        setTitle("Collision Maker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            map = ImageIO.read(new File("res/levelMaps/" + mapName + ".png"));

            int width = map.getWidth() / TILE_SIZE;
            int height = map.getHeight() / TILE_SIZE;
            collisionMap = new boolean[height][width];

            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    Color pixelColor = new Color(map.getRGB(j * TILE_SIZE, i * TILE_SIZE));
                    // Check if the color is very dark (close to black)
                    if(pixelColor.getRed() < 10 && pixelColor.getGreen() < 10 && pixelColor.getBlue() < 10){
                        collisionMap[i][j] = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g.drawImage(map, 0, 0, map.getWidth() * 2, map.getHeight() * 2, null);
                g2.setColor(new Color(255, 0, 0, 80));

                for (int row = 0; row < collisionMap.length; row++) {
                    for (int col = 0; col < collisionMap[0].length; col++) {
                        if (collisionMap[row][col]) {
                            g2.fillRect(col * TILE_SIZE * SCALE, row * TILE_SIZE * SCALE, TILE_SIZE * SCALE, TILE_SIZE * SCALE);
                        }
                    }
                }
            }
        };

        panel.setPreferredSize(new Dimension(map.getWidth() * SCALE, map.getHeight() * SCALE));

        panel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    int x = evt.getX();
                    int y = evt.getY();

                    int row = (int) (y / (TILE_SIZE * SCALE));
                    int col = (int) (x / (TILE_SIZE * SCALE));

                    if (row >= 0 && row < collisionMap.length && col >= 0 && col < collisionMap[0].length) {
                        collisionMap[row][col] = !collisionMap[row][col];
                        panel.repaint();
                    }
                }
            }
        );


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveCollision());
        setLayout(new BorderLayout());
        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void saveCollision() {
        try {
            PrintWriter writer = new PrintWriter("res/LevelMapsCollisons/" + mapName + "_collision_map.txt");
            for (int row = 0; row < collisionMap.length; row++) {
               //writer.print("{");
                for (int col = 0; col < collisionMap[0].length; col++) {
                    writer.print(collisionMap[row][col] ? "1" : "0");
                    if (col < collisionMap[0].length - 1) writer.print(" ");
                }
                //writer.print("}");
                if(row != collisionMap.length - 1){
                  //  writer.print(",");
                }
                writer.println();
            }
            writer.flush();
            JOptionPane.showMessageDialog(this, "Collision map saved successfully!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
