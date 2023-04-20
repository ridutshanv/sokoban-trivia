package client;

import static client.SmileGUI.getSmileAPIResponse;
import static client.Utils.gson;
import com.google.gson.JsonObject;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.JOptionPane;

public class SokobanGUI extends javax.swing.JFrame {

    static SokobanGUI sokobanGUI;
    static SmileGUI smileGUI;
    static GameArena sokobanGame;
    public static int currentLevel = 1;

    public SokobanGUI() {
        initComponents();
        sokobanGUI = this;  //  Set the static variable to the current instance so we can access it later from a static context.
        sokobanGame = new GameArena();
        sokobanGame.setSize(510, 500);
        sokobanGame.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        add(sokobanGame);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SOKOBAN");
        setBackground(new java.awt.Color(0, 0, 0));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

        switch (evt.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                sokobanGame.moveRight();
                break;

            case KeyEvent.VK_LEFT:
                sokobanGame.moveLeft();
                break;

            case KeyEvent.VK_DOWN:
                sokobanGame.moveDown();
                break;

            case KeyEvent.VK_UP:
                sokobanGame.moveUp();
        }

        if (sokobanGame.levelComplete()) {
            if (smileGUI != null) {  //  We only want one instance of the smile GUI running.
                return;
            }

            try {
                smileGUI = new SmileGUI();
                smileGUI.setVisible(true);

                String response = getSmileAPIResponse();

                //  Parse response body to JSON object using the Gson library
                JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

                System.out.println(jsonObject);

                smileGUI.imageURL = new URL(jsonObject.get("question").getAsString());
                smileGUI.answer = jsonObject.get("solution").getAsInt();

                smileGUI.displayImage();
            } catch (IOException e) {
                System.out.println("IO Error.");
            } catch (InterruptedException er) {
                System.out.println("Interrupted Error.");
            }
        }
    }//GEN-LAST:event_formKeyPressed

    public static void newLevel() {
        if (smileGUI != null) {  //  Remove the Smile GUI after the outcome has been determined.
            smileGUI.dispose();
            smileGUI = null;
        }

        if (currentLevel != 3) {
            currentLevel++;
            sokobanGame.loadlevel(currentLevel);
            sokobanGame.repaint();
        } else {
            JOptionPane.showMessageDialog(sokobanGUI, "Congratulations! You have completed the game.");
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SokobanGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SokobanGUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}