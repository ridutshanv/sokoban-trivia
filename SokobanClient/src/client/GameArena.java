package client;

import static client.Utils.gson;
import static client.Utils.httpClient;
import static client.Utils.stringToDeep;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameArena extends JPanel {

    //  Arena Size
    final int COLS = 10, ROWS = 8;

    //  Images 
    final String TILES_IMAGE = "tiles.jpeg";
    final String WALLS_IMAGE = "walls.jpeg";
    final String CRATES_IMAGE = "crates.jpeg";
    final String GAMER_IMAGE = "gamer.jpeg";
    final String BLADES_IMAGE = "blades.jpeg";

    //  Player Location
    int gamerRow, gamerCol;

    //  Starting Level
    int baseLevel = 01;

    GameElement[][] gameArray;

    public GameArena() {
        gameArray = new GameElement[ROWS][COLS];
        loadlevel(baseLevel);
        repaint();
    }

    public void loadlevel(int level) {
        //  Load the level from the server
        Map<String, Integer> data = new HashMap<>();
        data.put("level", level);

        String json = gson.toJson(data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/api/levels"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200:
                    //  Success
                    char[][] curLevel = stringToDeep(response.body());
                    // Load all tiles
                    for (int r = 0; r < ROWS; r++) {
                        for (int c = 0; c < COLS; c++) {
                            gameArray[r][c] = new Tiles(TILES_IMAGE);
                        }
                    }
                    // Load other elements
                    for (int r = 0; r < ROWS; r++) {
                        for (int c = 0; c < COLS; c++) {
                            switch (curLevel[r][c]) {
                                case 'b':
                                    gameArray[r][c] = new Blades(BLADES_IMAGE);
                                    break;
                                case 'w':
                                    gameArray[r][c].elementOnTop = new Walls(WALLS_IMAGE);
                                    break;
                                case 'c':
                                    gameArray[r][c].elementOnTop = new Crates(CRATES_IMAGE);
                                    break;
                                case 'g':
                                    gameArray[r][c].elementOnTop = new Gamer(GAMER_IMAGE);
                                    gamerRow = r;
                                    gamerCol = c;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    break;
                case 400:
                    JOptionPane.showMessageDialog(this, response.body());
                default:
                    JOptionPane.showMessageDialog(this, "Error! Something went wrong.");
            }
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, "Error! Something went wrong.");
        }
    }

    public void paintComponent(Graphics g) {

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int xLoc = 10 + c * 30;
                int yLoc = 10 + r * 30;
                BufferedImage img;
                if (gameArray[r][c].elementOnTop == null) {
                    img = gameArray[r][c].getImage();
                } else {
                    img = gameArray[r][c].elementOnTop.getImage();
                }

                g.drawImage(img, xLoc, yLoc, 30, 30, this);
            }
        }
    }

    public void moveRight() {
        if (gamerCol + 1 < COLS) {
            if (gameArray[gamerRow][gamerCol + 1].elementOnTop != null) {
                if (gameArray[gamerRow][gamerCol + 1].elementOnTop instanceof Crates) {   // It's just a crate
                    // Look into the next location
                    if (gameArray[gamerRow][gamerCol + 2].elementOnTop == null) {
                        // Move crate
                        gameArray[gamerRow][gamerCol + 2].elementOnTop
                                = gameArray[gamerRow][gamerCol + 1].elementOnTop;
                        gameArray[gamerRow][gamerCol + 1].elementOnTop = null;
                        // Move the gamer
                        gameArray[gamerRow][gamerCol + 1].elementOnTop
                                = gameArray[gamerRow][gamerCol].elementOnTop;
                        gameArray[gamerRow][gamerCol].elementOnTop = null;
                        gamerCol++;
                    }
                } else {
                    // walls stay still
                }
            } else {
                // Move the gamer
                gameArray[gamerRow][gamerCol + 1].elementOnTop
                        = gameArray[gamerRow][gamerCol].elementOnTop;
                gameArray[gamerRow][gamerCol].elementOnTop = null;
                gamerCol++;
            }
        }
        repaint();
    }

    public void moveLeft() {
        if (gamerCol - 1 > 0) {
            if (gameArray[gamerRow][gamerCol - 1].elementOnTop != null) {
                if (gameArray[gamerRow][gamerCol - 1].elementOnTop instanceof Crates) {   // It's just a crate
                    // Look into the next location
                    if (gameArray[gamerRow][gamerCol - 2].elementOnTop == null) {
                        // Move the crate
                        gameArray[gamerRow][gamerCol - 2].elementOnTop
                                = gameArray[gamerRow][gamerCol - 1].elementOnTop;
                        gameArray[gamerRow][gamerCol - 1].elementOnTop = null;
                        // Move the gamer
                        gameArray[gamerRow][gamerCol - 1].elementOnTop
                                = gameArray[gamerRow][gamerCol].elementOnTop;
                        gameArray[gamerRow][gamerCol].elementOnTop = null;
                        gamerCol--;
                    }
                } else {
                    // walls stay still
                }
            } else {
                // Move the gamer
                gameArray[gamerRow][gamerCol - 1].elementOnTop
                        = gameArray[gamerRow][gamerCol].elementOnTop;
                gameArray[gamerRow][gamerCol].elementOnTop = null;
                gamerCol--;
            }
        }
        repaint();
    }

    public void moveDown() {
        if (gamerRow + 1 < ROWS) {
            if (gameArray[gamerRow + 1][gamerCol].elementOnTop != null) {
                if (gameArray[gamerRow + 1][gamerCol].elementOnTop instanceof Crates) {   // It's just a crate
                    // Look into the next location
                    if (gameArray[gamerRow + 2][gamerCol].elementOnTop == null) {
                        // Move crate
                        gameArray[gamerRow + 2][gamerCol].elementOnTop
                                = gameArray[gamerRow + 1][gamerCol].elementOnTop;
                        gameArray[gamerRow + 1][gamerCol].elementOnTop = null;
                        // Move the gamer
                        gameArray[gamerRow + 1][gamerCol].elementOnTop
                                = gameArray[gamerRow][gamerCol].elementOnTop;
                        gameArray[gamerRow][gamerCol].elementOnTop = null;
                        gamerRow++;
                    }
                } else {
                    // walls stay still
                }
            } else {
                // Move the gamer
                gameArray[gamerRow + 1][gamerCol].elementOnTop
                        = gameArray[gamerRow][gamerCol].elementOnTop;
                gameArray[gamerRow][gamerCol].elementOnTop = null;
                gamerRow++;
            }
        }
        repaint();
    }

    public void moveUp() {
        if (gamerRow - 1 > 0) {
            if (gameArray[gamerRow - 1][gamerCol].elementOnTop != null) {
                if (gameArray[gamerRow - 1][gamerCol].elementOnTop instanceof Crates) {   // It's just a crate
                    // Look into the next location
                    if (gameArray[gamerRow - 2][gamerCol].elementOnTop == null) {
                        // Move crate
                        gameArray[gamerRow - 2][gamerCol].elementOnTop
                                = gameArray[gamerRow - 1][gamerCol].elementOnTop;
                        gameArray[gamerRow - 1][gamerCol].elementOnTop = null;
                        // Move the gamer
                        gameArray[gamerRow - 1][gamerCol].elementOnTop
                                = gameArray[gamerRow][gamerCol].elementOnTop;
                        gameArray[gamerRow][gamerCol].elementOnTop = null;
                        gamerRow--;
                    }
                } else {
                    // walls stay still
                }
            } else {
                // Move the gamer
                gameArray[gamerRow - 1][gamerCol].elementOnTop
                        = gameArray[gamerRow][gamerCol].elementOnTop;
                gameArray[gamerRow][gamerCol].elementOnTop = null;
                gamerRow--;
            }
        }
        repaint();
    }

    public boolean levelComplete() {
        boolean over = true;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (gameArray[r][c] instanceof Blades) {
                    if (gameArray[r][c].elementOnTop == null) {
                        over = false;
                    } else if (!(gameArray[r][c].elementOnTop instanceof Crates)) {
                        over = false;
                    }
                }
            }
        }
        return over;
    }
}