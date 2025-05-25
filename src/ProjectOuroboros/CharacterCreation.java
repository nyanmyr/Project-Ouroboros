
package ProjectOuroboros;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Icon;
import javax.swing.JLabel;

public class CharacterCreation extends javax.swing.JFrame {

    private BufferedImage originalImage;
    private Map<String, Color> customColors;

    public CharacterCreation() {
        initComponents();

        customColors = new LinkedHashMap<>();
        customColors.put("Soft Purple", new Color(160, 100, 200));
        customColors.put("Orange Tint", new Color(255, 140, 0));
        customColors.put("Ocean Blue", new Color(30, 144, 255));
        customColors.put("Lime Green", new Color(50, 205, 50));
        customColors.put("Warm Gray", new Color(120, 120, 120));
        customColors.put("Deep Red", new Color(200, 20, 20));

        ImageIcon originalIcon = new ImageIcon("src\\ProjectOuroboros\\Images\\CharacterCreationTesting\\Lineart\\torso_lin.png");

        ImageIcon temp = new ImageIcon("src\\ProjectOuroboros\\Images\\CharacterCreationTesting\\Color\\torso_col.png");
        Image tempImage = temp.getImage();
        BufferedImage original = toBufferedImage(tempImage);
        Color target = new Color(255, 100, 50); // Custom RGB color
        BufferedImage recolored = recolorImage(original, target);

        ImageIcon recoloredIcon = new ImageIcon(recolored);
        label_TorsoColor.setIcon(recoloredIcon);

        Timer t = new Timer();
        TimerTask breathingAnimation = new TimerTask() {

            double angleDegrees = 0;
            boolean breathPhase = false;
            int waitCount = 0;
            int waitTime = 0;
            boolean wait = false;

            @Override
            public void run() {

                if (!wait) {
                    // turn into ternary operator
                    if (!breathPhase) {
                        angleDegrees--;
                    } else {
                        angleDegrees++;
                    }

                    label_HeadLineart.setLocation(label_HeadLineart.getX(), bop(label_HeadLineart, "y", 2, breathPhase, false));
                    label_HeadColor.setLocation(label_HeadLineart.getX(), bop(label_HeadLineart, "y", 2, breathPhase, false));

                    label_NeckLineart.setLocation(label_NeckLineart.getX(), bop(label_NeckLineart, "y", 2, breathPhase, false));
                    label_NeckColor.setLocation(label_NeckLineart.getX(), bop(label_NeckLineart, "y", 2, breathPhase, false));

                    label_ArmLineart.setLocation(bop(label_ArmLineart, "x", 1, breathPhase, true),
                            bop(label_ArmLineart, "y", 1, breathPhase, false));
                    label_ArmColor.setLocation(bop(label_ArmLineart, "x", 1, breathPhase, true),
                            bop(label_ArmLineart, "y", 1, breathPhase, false));

                    label_TorsoLineart.setLocation(bop(label_TorsoLineart, "x", 1, breathPhase, true),
                            bop(label_TorsoLineart, "y", 1, breathPhase, false));
                    label_TorsoColor.setLocation(bop(label_TorsoLineart, "x", 1, breathPhase, true),
                            bop(label_TorsoLineart, "y", 1, breathPhase, false));

                    ImageIcon rotatedIcon = new ImageIcon(rotateImageByDegrees(toBufferedImage(originalIcon.getImage()), angleDegrees));
                    label_TorsoLineart.setIcon(rotatedIcon);

                    if (angleDegrees <= -3 || angleDegrees >= 0) {
                        breathPhase = !breathPhase;
                        wait = !wait;
                        if (breathPhase) {
                            // breath in wait time
                            waitTime = 3;
                        } else {
                            // breath out wait time
                            waitTime = 12;
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

        t.scheduleAtFixedRate(breathingAnimation, 0, 120);

        setExtendedState(CharacterCreation.MAXIMIZED_BOTH);
    }

    public static BufferedImage recolorImage(BufferedImage sourceImage, Color targetColor) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int newRGB = targetColor.getRGB() & 0x00FFFFFF; // Remove alpha bits

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = sourceImage.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;

                if (alpha != 0) {
                    // Apply the new color while preserving alpha
                    int recolored = (alpha << 24) | newRGB;
                    result.setRGB(x, y, recolored);
                } else {
                    result.setRGB(x, y, pixel); // Keep transparent
                }
            }
        }

        return result;
    }

    private int bop(JLabel label, String axis, int bopAmount, boolean phase, boolean inverted) {

        bopAmount = !phase ? bopAmount * -1 : bopAmount;

        switch (axis) {
            case "y" -> {
                return label.getY() + bopAmount;
            }
            case "x" -> {
                return label.getX() + bopAmount;
            }
            default -> {
                return 0;
            }
        }

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
        label_HeadLineart = new javax.swing.JLabel();
        label_HeadColor = new javax.swing.JLabel();
        label_NeckLineart = new javax.swing.JLabel();
        label_NeckColor = new javax.swing.JLabel();
        label_ArmLineart = new javax.swing.JLabel();
        label_ArmColor = new javax.swing.JLabel();
        label_TorsoLineart = new javax.swing.JLabel();
        label_TorsoColor = new javax.swing.JLabel();
        combobox_ColorChooser = new javax.swing.JComboBox<>();
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

        label_HeadLineart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Lineart/head_lin.png"))); // NOI18N
        panel_Headshot.add(label_HeadLineart);
        label_HeadLineart.setBounds(90, 100, 122, 150);

        label_HeadColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Color/head_col.png"))); // NOI18N
        panel_Headshot.add(label_HeadColor);
        label_HeadColor.setBounds(90, 100, 122, 150);

        label_NeckLineart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_NeckLineart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Lineart/neck_lin.png"))); // NOI18N
        panel_Headshot.add(label_NeckLineart);
        label_NeckLineart.setBounds(90, 190, 90, 110);

        label_NeckColor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_NeckColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Color/neck_col.png"))); // NOI18N
        panel_Headshot.add(label_NeckColor);
        label_NeckColor.setBounds(90, 190, 90, 110);

        label_ArmLineart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Lineart/arm_lin.png"))); // NOI18N
        panel_Headshot.add(label_ArmLineart);
        label_ArmLineart.setBounds(20, 320, 72, 108);

        label_ArmColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Color/arm_col.png"))); // NOI18N
        panel_Headshot.add(label_ArmColor);
        label_ArmColor.setBounds(20, 320, 72, 108);

        label_TorsoLineart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_TorsoLineart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Lineart/torso_lin.png"))); // NOI18N
        panel_Headshot.add(label_TorsoLineart);
        label_TorsoLineart.setBounds(-10, 240, 300, 200);

        label_TorsoColor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_TorsoColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectOuroboros/Images/CharacterCreationTesting/Color/torso_col.png"))); // NOI18N
        panel_Headshot.add(label_TorsoColor);
        label_TorsoColor.setBounds(-10, 240, 300, 200);

        panel_CharacterCreation.add(panel_Headshot);
        panel_Headshot.setBounds(10, 10, 300, 400);

        combobox_ColorChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panel_CharacterCreation.add(combobox_ColorChooser);
        combobox_ColorChooser.setBounds(320, 10, 72, 22);

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
    private javax.swing.JComboBox<String> combobox_ColorChooser;
    private javax.swing.JLabel label_ArmColor;
    private javax.swing.JLabel label_ArmLineart;
    private javax.swing.JLabel label_Background;
    private javax.swing.JLabel label_HeadColor;
    private javax.swing.JLabel label_HeadLineart;
    private javax.swing.JLabel label_NeckColor;
    private javax.swing.JLabel label_NeckLineart;
    private javax.swing.JLabel label_TorsoColor;
    private javax.swing.JLabel label_TorsoLineart;
    private javax.swing.JPanel panel_CharacterCreation;
    private javax.swing.JPanel panel_Headshot;
    private javax.swing.JPanel panel_Mainpanel;
    // End of variables declaration//GEN-END:variables
}

// Variables declaration - do not modify                     
    // End of variables declaration      
