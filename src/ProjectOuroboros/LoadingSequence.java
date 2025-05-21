package ProjectOuroboros;

import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingSequence extends javax.swing.JFrame {

    public LoadingSequence(String loadingType) {
        
        System.out.println("Loading Sequence: " + loadingType);

        Timer t = new Timer();
        
        initComponents();

        setExtendedState(LoadingSequence.MAXIMIZED_BOTH);

        label.setText("...");

        TimerTask tt = new TimerTask () {
          
            int progress = 0;
            
            @Override
            public void run() {
                progress++;
                if (progress >= 2) {
                    label.setText("Loading Terrain...");
                    new WorldCreation(loadingType).setVisible(true);
                    dispose();
                    t.cancel();
                }
            }
            
        };
        
        t.scheduleAtFixedRate(tt, 0, 1000);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_Mainpanel = new javax.swing.JPanel();
        label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setSize(new java.awt.Dimension(0, 0));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        panel_Mainpanel.setBackground(new java.awt.Color(0, 0, 0));
        panel_Mainpanel.setToolTipText(null);
        panel_Mainpanel.setMaximumSize(new java.awt.Dimension(3840, 2160));
        panel_Mainpanel.setMinimumSize(new java.awt.Dimension(1280, 720));
        panel_Mainpanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
        panel_Mainpanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panel_MainpanelComponentResized(evt);
            }
        });
        panel_Mainpanel.setLayout(null);

        label.setBackground(new java.awt.Color(0, 0, 0));
        label.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        label.setForeground(new java.awt.Color(255, 255, 255));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("Loading...");
        label.setToolTipText(null);
        panel_Mainpanel.add(label);
        label.setBounds(536, 540, 500, 48);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_Mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_Mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void panel_MainpanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panel_MainpanelComponentResized

        Dimension screenSize = panel_Mainpanel.getSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        label.setBounds((int) (screenWidth * 0.5), (int) (screenHeight * 0.5), label.getWidth(), label.getHeight());

    }//GEN-LAST:event_panel_MainpanelComponentResized

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked


    }//GEN-LAST:event_formMouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoadingSequence(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    private javax.swing.JPanel panel_Mainpanel;
    // End of variables declaration//GEN-END:variables
}

// Variables declaration - do not modify                     
    // End of variables declaration      
