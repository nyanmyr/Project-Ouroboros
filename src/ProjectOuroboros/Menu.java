//
// unique:
//      - replayability
//
// classes:
//      - merchant, fighter, sorcerer, marksman
// prowesses:
//      - financial, martial, sorcery, marksmanship
//
//
//
// game play loop:
//      - start of phase
//      - grind/ completing quests to get money
//      - phase battle
//      - boss battle
//          - if (victorious) cycle/game ends, evil is defeated
//          - else if (retreated) next phase starts, evil grows stronger
//          - else (defeated) cycle/ game ends, evil wins.
//
// turn loop (1 movement)
//      - hunger, thirst, energy is changed
//      - event: battle, encounter
//      - save progress
//
//
// ideas:
// • fog of war
// • world creation menu
// • weather map
// • loading
//
// features:
// • start menu
//      - load world
//      - use world
//      - difficulty/ game settings menu
//      - character creation menu
// • world creation screen
//      - Spawn locations: village, town, city, wilderness
// • movement
//      - attrition
//      - speed
// • map
//      - locations: secret locations, church of Dalia
// • save (Jtable table)
//      - map
//      - location
// • load
// • battle
//      - random enemy generator
// • party
//      - skills
// • events
// • points
// • character interaction/ dialogue screen
// • hunger/ thirst/ energy
// • date/ time/ turn
// • weather
//
// make:
// • town name display
// • town/ biome name hide key
// • Optimize
// • world save/ load system
//
// optimize:
// • change Frr biome to fr
// • remove unused tiles
// - settlementTileMap
// - riverTileMap
// • background tile >> image of background
//
//
package ProjectOuroboros;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Menu extends javax.swing.JFrame {
    
    String version = "version 0.0.4 (prototype)";

    public Menu() {
        initComponents();
        
        label_Version.setText(version);

        setExtendedState(Menu.MAXIMIZED_BOTH);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_Mainpanel = new javax.swing.JPanel();
        button_Quit = new javax.swing.JButton();
        button_Settings = new javax.swing.JButton();
        button_Load = new javax.swing.JButton();
        button_New = new javax.swing.JButton();
        label_Version = new javax.swing.JLabel();
        label_Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(3840, 2160));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setSize(new java.awt.Dimension(1920, 1080));

        panel_Mainpanel.setBackground(new java.awt.Color(255, 255, 255));
        panel_Mainpanel.setToolTipText(null);
        panel_Mainpanel.setMaximumSize(new java.awt.Dimension(3840, 2160));
        panel_Mainpanel.setMinimumSize(new java.awt.Dimension(1280, 720));
        panel_Mainpanel.setName(""); // NOI18N
        panel_Mainpanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
        panel_Mainpanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panel_MainpanelComponentResized(evt);
            }
        });
        panel_Mainpanel.setLayout(null);

        button_Quit.setBackground(new java.awt.Color(51, 51, 51));
        button_Quit.setForeground(new java.awt.Color(204, 255, 255));
        button_Quit.setText("QUIT");
        button_Quit.setToolTipText(null);
        button_Quit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_Quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_QuitActionPerformed(evt);
            }
        });
        panel_Mainpanel.add(button_Quit);
        button_Quit.setBounds(20, 190, 130, 50);

        button_Settings.setBackground(new java.awt.Color(51, 51, 51));
        button_Settings.setForeground(new java.awt.Color(204, 255, 255));
        button_Settings.setText("SETTINGS");
        button_Settings.setToolTipText(null);
        button_Settings.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_Settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SettingsActionPerformed(evt);
            }
        });
        panel_Mainpanel.add(button_Settings);
        button_Settings.setBounds(20, 130, 130, 50);

        button_Load.setBackground(new java.awt.Color(51, 51, 51));
        button_Load.setForeground(new java.awt.Color(204, 255, 255));
        button_Load.setText("LOAD");
        button_Load.setToolTipText(null);
        button_Load.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_Load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_LoadActionPerformed(evt);
            }
        });
        panel_Mainpanel.add(button_Load);
        button_Load.setBounds(20, 70, 130, 50);

        button_New.setBackground(new java.awt.Color(51, 51, 51));
        button_New.setForeground(new java.awt.Color(204, 255, 255));
        button_New.setText("NEW");
        button_New.setToolTipText(null);
        button_New.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewActionPerformed(evt);
            }
        });
        panel_Mainpanel.add(button_New);
        button_New.setBounds(20, 10, 130, 50);

        label_Version.setForeground(new java.awt.Color(102, 102, 102));
        label_Version.setText("version 0.0.1 (prototype)");
        panel_Mainpanel.add(label_Version);
        label_Version.setBounds(20, 250, 200, 25);

        label_Background.setBackground(new java.awt.Color(0, 0, 0));
        label_Background.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        label_Background.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/menu_screen.png"))); // NOI18N
        label_Background.setToolTipText(null);
        label_Background.setAlignmentY(0.0F);
        label_Background.setMaximumSize(new java.awt.Dimension(3840, 2160));
        panel_Mainpanel.add(label_Background);
        label_Background.setBounds(0, 0, 1920, 1080);

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

        ImageIcon ii = new ImageIcon(getClass().getResource("/ProjectOuroboros/Images/menu_screen.png"));

        Dimension screenSize = panel_Mainpanel.getSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int width;
        int height;

//        System.out.println("screenWidth: " + screenWidth);
//        System.out.println("screenHeight: " + screenHeight);

        Image image = (ii).getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);
        ii = new ImageIcon(image);
        label_Background.setIcon(ii);

        label_Background.setBounds(0, 0, screenWidth, screenHeight);
        
        label_Version.setBounds((int) (screenHeight * 0.025), (int) (screenHeight * 0.955), 200, 25);

        width = (int) (screenWidth * 0.05) < 250 ? 250 : (int) (screenWidth * 0.05);
        height = (int) (screenHeight * 0.05) < 50 ? 50 : (int) (screenHeight * 0.05);
        button_New.setBounds((int) (screenWidth * 0.5) - width / 2, (int) (screenHeight * 0.5) - height / 2, width, height);
        button_Load.setBounds((int) (screenWidth * 0.5) - width / 2, (int) (screenHeight * 0.5) - height / 2 + 60, width, height);
        button_Settings.setBounds((int) (screenWidth * 0.5) - width / 2, (int) (screenHeight * 0.5) - height / 2 + 120, width, height);
        button_Quit.setBounds((int) (screenWidth * 0.5) - width / 2, (int) (screenHeight * 0.5) - height / 2 + 180, width, height);

    }//GEN-LAST:event_panel_MainpanelComponentResized

    private void button_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewActionPerformed
        
        dispose();
        new LoadingSequence().setVisible(true);
        
    }//GEN-LAST:event_button_NewActionPerformed

    private void button_LoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_LoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_LoadActionPerformed

    private void button_SettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SettingsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_SettingsActionPerformed

    private void button_QuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_QuitActionPerformed
        
        System.exit(0);
        
    }//GEN-LAST:event_button_QuitActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_Load;
    private javax.swing.JButton button_New;
    private javax.swing.JButton button_Quit;
    private javax.swing.JButton button_Settings;
    private javax.swing.JLabel label_Background;
    private javax.swing.JLabel label_Version;
    private javax.swing.JPanel panel_Mainpanel;
    // End of variables declaration//GEN-END:variables
}

// Variables declaration - do not modify                     
    // End of variables declaration      
