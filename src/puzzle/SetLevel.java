package puzzle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 *
 * @author soumia
 */
public class SetLevel extends JFrame implements ActionListener {

    private final JLabel intro;
    private final JPanel panelSelectImage; // That Containe All the Images
    private final ArrayList<JButton> ImageButtons;
    private final JPanel panelSelectDimension; // // That Containe All the radio btns
    private final JRadioButton easy;
    private final JRadioButton medium;
    private final JRadioButton hard;
    private final JRadioButton so_hard;
    private final JButton submitButton;
    private final JButton exitButton;

    private int height;
    private int width;
    private String name;

    // Définition de la classe SetLevel
    public SetLevel() {
        this.setUndecorated(true); // Enlève la barre de titre et les bordures
        this.setLayout(null);
        int toolkitWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        // Création et personnalisation du JLabel intro
        intro = new JLabel("Choisissez une image et une difficulté pour commencer !");
        intro.setFont(new Font("Z003", Font.ITALIC, 40));
        intro.setBounds(toolkitWidth / 225, 30, 600, 50);
        this.add(intro);

        // Création et personnalisation du JPanel panelSelectImage
        panelSelectImage = new JPanel(null);
        panelSelectImage.setOpaque(false);
        panelSelectImage.setBounds(0, 130, toolkitWidth, 300);
        this.add(panelSelectImage);

        int imageWith = toolkitWidth / 3 - 50;

        // Création d'une liste d'ImageButtons avec l'appel de la méthode addButton pour chaque image
        ImageButtons = new ArrayList<>(Arrays.asList(
                addButton("cow", 25, imageWith, panelSelectImage),
                addButton("dolphin", imageWith + 50 + 25, imageWith, panelSelectImage),
                addButton("Nasa", imageWith + imageWith + 100 + 25, imageWith, panelSelectImage)
        ));

        // Création et personnalisation du JPanel panelSelectDimension
        panelSelectDimension = new JPanel(null);
        panelSelectDimension.setOpaque(false);
        panelSelectDimension.setBounds(0, 500, toolkitWidth, 60);
        this.add(panelSelectDimension);

        // Création des boutons radio et positionnement
        easy = createRadioButton("Easy", toolkitWidth / 2 - 500);
        medium = createRadioButton("Medium", toolkitWidth / 2 - 200);
        hard = createRadioButton("Hard", toolkitWidth / 2 + 100);
        so_hard = createRadioButton("So Hard", toolkitWidth / 2 + 400);

        // Regroupement des boutons radio en un groupe
        groupButtonDimension();

        // Création et personnalisation des boutons submitButton et exitButton
        submitButton = createButton("GO", toolkitWidth / 2 - 50, 650);
        submitButton.addActionListener(this);
        exitButton = createButton("Sortir", toolkitWidth / 2 - 50, 710);
        exitButton.addActionListener(this);

        this.add(submitButton);
        this.add(exitButton);

        // Mettre la fenêtre en mode plein écran si possible
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this); // activer le mode plein écran
        } else {
            // sinon ajuster la taille et la position de la fenêtre
            this.setSize(gd.getDisplayMode().getWidth() / 2, gd.getDisplayMode().getHeight() / 2);
            this.setLocationRelativeTo(null);
        }
    }// fin du constructeur

    private JButton addButton(String imageName, int x, int width, JPanel panel) {
        // Crée un nouveau bouton avec une image redimensionnée pour correspondre aux dimensions spécifiées
        JButton button = new JButton(new ImageIcon(new ImageIcon("src/" + imageName + ".jpg").getImage().getScaledInstance(width - 5, 250 - 5, 1)));
        // Positionne le bouton sur le panneau
        button.setBounds(x, 0, width, 250);
        // Ajoute un écouteur d'événements pour le bouton
        button.addActionListener(this);
        // Ajoute le bouton au panneau
        panel.add(button);
        // Retourne le bouton créé
        return button;
    }// fin de la méthode addButton

    private JRadioButton createRadioButton(String text, int x) {
        // Créer un nouveau bouton radio avec le texte spécifié
        JRadioButton radioButton = new JRadioButton(text);
        // Définir l'icône sélectionnée pour le bouton radio
        radioButton.setSelectedIcon(new ImageIcon(new ImageIcon("src/r1_o.png").getImage().getScaledInstance(50, 50, 1)));
        // Définir l'icône non sélectionnée pour le bouton radio
        radioButton.setIcon(new ImageIcon(new ImageIcon("src/r1_c.png").getImage().getScaledInstance(50, 50, 1)));
        // Définir la police du texte du bouton radio
        radioButton.setFont(new Font("Z003", Font.ITALIC, 32));
        // Définir la position du bouton radio sur le panneau
        radioButton.setBounds(x, 0, 200, 50);
        // Ajouter le bouton radio au panneau "panelSelectDimension"
        panelSelectDimension.add(radioButton);
        // Retourner le bouton radio créé
        return radioButton;
    }// fin de la méthode createRadioButton

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Z003", Font.ITALIC, 32));
        button.setBackground(Color.decode("#c27cea"));
        button.setBounds(x, y, 100, 50);
        return button;
    }// fin de la méthode createButton

    /**
     * Vérifie si une option de dimension a été sélectionnée et met à jour les
     * valeurs de hauteur et de largeur correspondantes.
     *
     * @return Vrai si une option de dimension a été sélectionnée, faux sinon.
     */
    private boolean dimRadioButtonSelected() {
        boolean radioCheck;
        if (easy.isSelected()) {
            this.height = 3;
            this.width = 3;
            radioCheck = true;
        } else if (medium.isSelected()) {
            this.height = 4;
            this.width = 4;
            radioCheck = true;
        } else if (hard.isSelected()) {
            this.height = 5;
            this.width = 5;
            radioCheck = true;
        } else if (so_hard.isSelected()) {
            this.height = 6;
            this.width = 6;
            radioCheck = true;
        } else {
            radioCheck = false;
        }
        return radioCheck;
    }

    private void groupButtonDimension() {
        ButtonGroup bgDim = new ButtonGroup();
        bgDim.add(easy);
        bgDim.add(medium);
        bgDim.add(hard);
        bgDim.add(so_hard);
    }//end method groupButtonDimension()

    @Override
    public void actionPerformed(ActionEvent e) {
        // Si le bouton de sortie est cliqué, le programme est terminé
        if (e.getSource() == exitButton) {
            System.exit(0);
        } // Si le premier bouton d'image est cliqué, le nom de l'image est défini sur "cow"
        else if (e.getSource() == ImageButtons.get(0)) {
            name = "cow";
        } // Si le deuxième bouton d'image est cliqué, le nom de l'image est défini sur "dolphin"
        else if (e.getSource() == ImageButtons.get(1)) {
            name = "dolphin";
        } // Si le troisième bouton d'image est cliqué, le nom de l'image est défini sur "Nasa"
        else if (e.getSource() == ImageButtons.get(2)) {
            name = "Nasa";
        } // Si le bouton de soumission est cliqué
        else if (e.getSource() == submitButton) {
            // Si aucune image n'est sélectionnée, un message d'erreur est affiché
            if (name == null) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une image s'il vous plaît !!!", "Image", JOptionPane.INFORMATION_MESSAGE);
            } // Si une image est sélectionnée et une dimension est sélectionnée, le jeu de puzzle est lancé
            else if (dimRadioButtonSelected()) {
                // Faire la transition
                SwingUtilities.invokeLater(() -> {
                    PuzzlePanel puzzlePanel = new PuzzlePanel(height, width, name);
                    puzzlePanel.setVisible(true);
                    dispose();
                });
            } // Si une image est sélectionnée mais aucune dimension n'est sélectionnée, un message d'erreur est affiché
            else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une dimension de radio bouton !!!", "Dimension Radio", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//fin de la méthode actionPerformed

}
