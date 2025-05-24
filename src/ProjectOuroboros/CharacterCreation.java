
package ProjectOuroboros;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class CharacterCreation extends javax.swing.JFrame {

    boolean temp = false;

    public CharacterCreation() {
        initComponents();

        ImageIcon originalIcon = new ImageIcon("src\\ProjectOuroboros\\Images\\CharacterCreationTesting\\torso.png");

        Timer t = new Timer();
        TimerTask tt = new TimerTask() {

            double angleDegrees = 0;
            boolean temp = false;
            int waitCount = 0;
            int waitTime = 0;
            boolean wait = false;

            @Override
            public void run() {

                if (!wait) {
                    // turn into ternary operator
                    if (!temp) {
                        angleDegrees--;
                        label_Head.setBounds(label_Head.getX(), label_Head.getY() - 2, label_Head.getWidth(), label_Head.getHeight());
                        label_Neck.setBounds(label_Neck.getX(), label_Neck.getY() - 2, label_Neck.getWidth(), label_Neck.getHeight());
                        label_Arm.setBounds(label_Arm.getX() + 2, label_Arm.getY() - 1, label_Arm.getWidth(), label_Arm.getHeight());
                        label_Torso.setBounds(label_Torso.getX() + 1, label_Torso.getY() - 1
                                , label_Torso.getWidth(), label_Torso.getHeight());
                    } else {
                        angleDegrees++;
                        label_Head.setBounds(label_Head.getX(), label_Head.getY() + 2, label_Head.getWidth(), label_Head.getHeight());
                        label_Neck.setBounds(label_Neck.getX(), label_Neck.getY() + 2, label_Neck.getWidth(), label_Neck.getHeight());
                        label_Arm.setBounds(label_Arm.getX() - 2, label_Arm.getY() + 1, label_Arm.getWidth(), label_Arm.getHeight());
                        label_Torso.setBounds(label_Torso.getX() - 1, label_Torso.getY() + 1
                                , label_Torso.getWidth(), label_Torso.getHeight());
                    }

                    ImageIcon rotatedIcon = new ImageIcon(rotateImageByDegrees(toBufferedImage(originalIcon.getImage()), angleDegrees));
                    label_Torso.setIcon(rotatedIcon);
                    if (angleDegrees <= -3 || angleDegrees >= 0) {
                        temp = !temp;
                        wait = !wait;
                        if (temp) {
                            waitTime = 6;
                        } else {
                            waitTime = 16;
                        }
                    }
                } else {
                    waitCount++;
                    if (waitCount > waitTime) {
                        waitCount = 0;
                        wait = !wait;
                    }
                }

            }

        };

        t.scheduleAtFixedRate(tt, 0, 120);

//        setExtendedState(CharacterCreation.MAXIMIZED_BOTH);
    }

    // Converts an Image to BufferedImage
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    // Rotates a BufferedImage by the given angle in degrees
    private static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians)), cos = Math.abs(Math.cos(radians));
        int w = img.getWidth(), h = img.getHeight();
        int newW = (int) Math.floor(w * cos + h * sin);
        int newH = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        AffineTransform at = new AffineTransform();
        at.translate((newW - w) / 2.0, (newH - h) / 2.0);
        at.rotate(radians, w / 2.0, h / 2.0);

        g2d.drawRenderedImage(img, at);
        g2d.dispose();

        return rotated;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_Mainpanel = new javax.swing.JPanel();
        panel_CharacterCreation = new javax.swing.JPanel();
        panel_Headshot = new javax.swing.JPanel();
        label_Head = new javax.swing.JLabel();
        label_Neck = new javax.swing.JLabel();
        label_Arm = new javax.swing.JLabel();
        label_Torso = new javax.swing.JLabel();
        button_New = new javax.swing.JButton();
        label_Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        panel_CharacterCreation.setBackground(new java.awt.Color(63, 63, 63));
        panel_CharacterCreation.setLayout(null);

        panel_Headshot.setBackground(new java.awt.Color(187, 187, 186));
        panel_Headshot.setLayout(null);

        label_Head.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/head.png"))); // NOI18N
        panel_Headshot.add(label_Head);
        label_Head.setBounds(90, 100, 122, 150);

        label_Neck.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Neck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/neck.png"))); // NOI18N
        panel_Headshot.add(label_Neck);
        label_Neck.setBounds(90, 190, 90, 110);

        label_Arm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/arm.png"))); // NOI18N
        panel_Headshot.add(label_Arm);
        label_Arm.setBounds(20, 320, 72, 110);

        label_Torso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Torso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/torso.png"))); // NOI18N
        panel_Headshot.add(label_Torso);
        label_Torso.setBounds(-10, 240, 300, 200);

        panel_CharacterCreation.add(panel_Headshot);
        panel_Headshot.setBounds(10, 10, 300, 400);

        panel_Mainpanel.add(panel_CharacterCreation);
        panel_CharacterCreation.setBounds(170, 40, 580, 420);

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

        ImageIcon ii = new ImageIcon(getClass().getResource("/ProjectOuroboros/Images/menu_placeholder.png"));

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

        panel_CharacterCreation.setBounds(200, 100, screenWidth - 400, screenHeight - 200);

        width = (int) (screenWidth * 0.05) < 250 ? 250 : (int) (screenWidth * 0.05);
        height = (int) (screenHeight * 0.05) < 50 ? 50 : (int) (screenHeight * 0.05);
        button_New.setBounds((int) (screenWidth * 0.5) - width / 2, (int) (screenHeight * 0.5) - height / 2 + 200, width, height);


    }//GEN-LAST:event_panel_MainpanelComponentResized

    private void button_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewActionPerformed


    }//GEN-LAST:event_button_NewActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CharacterCreation().setVisible(true);
            }

        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_New;
    private javax.swing.JLabel label_Arm;
    private javax.swing.JLabel label_Background;
    private javax.swing.JLabel label_Head;
    private javax.swing.JLabel label_Neck;
    private javax.swing.JLabel label_Torso;
    private javax.swing.JPanel panel_CharacterCreation;
    private javax.swing.JPanel panel_Headshot;
    private javax.swing.JPanel panel_Mainpanel;
    // End of variables declaration//GEN-END:variables
}

// Variables declaration - do not modify                     
    // End of variables declaration      
