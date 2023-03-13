package puzzle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author soumia
 */
public class PuzzlePanel extends JFrame implements ActionListener {

    // Cette variable représente un panneau (ou une zone) de l'interface graphique qui sera utilisé pour afficher quelque chose.
    private JPanel panel;
    // Cette variable représente une image source, c'est-à-dire une image qui sera utilisée pour créer d'autres images.
    private transient BufferedImage source;
    // Cette variable représente une image redimensionnée qui sera affichée sur l'interface graphique.
    private transient BufferedImage resized;
    // Cette variable représente une liste de boutons qui seront affichés sur l'interface graphique.
    private final transient List<JButton> buttons;
    // Cette variable représente une liste de points qui seront utilisés pour déterminer la solution du puzzle.
    private transient List<Point> solution;
    // Cette variable représente un objet de type Timer qui sera utilisé pour mesurer le temps écoulé.
    private transient final Timer timer;
    // Cette variable représente le nombre de secondes qui se sont écoulées depuis le début du jeu.
    private int seconds = 0;
    // Cette variable représente un bouton qui permettra à l'utilisateur de revenir en arrière dans le jeu.
    private final JButton backButton;
    // Cette variable représente un bouton qui permettra à l'utilisateur de quitter le jeu.
    private final JButton exitButton;
    // Cette variable représente une étiquette qui affichera le score du joueur.
    private final JLabel scoreLabel;
    // Cette variable représente le score du joueur.
    private int score = 0;
    // Cette variable est un booléen qui permettra de savoir si le joueur a déjà effectué un premier clic ou non.
    private boolean firstClick = false;
    // Cette variable représente l'indice du dernier bouton cliqué par le joueur.
    private int lastClicked = 0;

    public PuzzlePanel(int height, int width, String s) {
        this.setLayout(null);
        int toolkitWidth = Toolkit.getDefaultToolkit().getScreenSize().height;
        // Générer la liste de points de solution pour le puzzle
        // On génère une séquence d'entiers allant de 0 à (hauteur x largeur)-1
        solution = IntStream.range(0, height * width)
                // Pour chaque entier i, on crée un objet Point avec les coordonnées (i / largeur, i modulo largeur) et on stocke le Point dans un objet Stream<Point>
                .mapToObj(i -> new Point(i / width, i % width))
                // On collecte les éléments du Stream dans une liste de points de solution
                .collect(Collectors.toList());

        try {
            // Charger l'image à partir du fichier source
            source = ImageIO.read(new File("src/" + s + ".jpg"));

            // Redimensionner l'image à une taille fixe pour la grille de boutons
            resized = resizeImage(source, 900, 600, BufferedImage.TYPE_INT_ARGB);
        } catch (IOException ex) {
            Logger.getLogger(PuzzlePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Créer une grille de boutons pour le puzzle
        panel = new JPanel(new GridLayout(height, width, 10, 10));
        panel.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 450,
                toolkitWidth / 2 - 300, 900, 600);
        this.add(panel);

        // Créer des boutons pour chaque portion d'image
        buttons = IntStream.range(0, height * width) // Créer un flux d'entiers allant de 0 à (hauteur x largeur)
                .mapToObj(i -> {
                    int x = i % width; // Calculer la position x de la portion d'image
                    int y = i / width; // Calculer la position y de la portion d'image

                    // Créer une nouvelle image en recadrant la source à partir des positions x et y
                    Image image = createImage(new FilteredImageSource(resized.getSource(),
                            new CropImageFilter(x * resized.getWidth() / width,
                                    y * resized.getHeight() / height,
                                    resized.getWidth() / width, resized.getHeight() / height)));

                    // Créer un bouton avec une image pour chaque portion d'image
                    JButton button = new JButton(new ImageIcon(image));

                    // Stocker la position de la portion d'image dans les propriétés du bouton
                    button.putClientProperty("position", new Point(y, x));

                    // Ajouter un écouteur de clic pour le bouton
                    button.addActionListener(this::handleClick);

                    return button; // Retourner le bouton créé
                })
                .collect(Collectors.toList()); // Collecter tous les boutons dans une liste

        // Mélanger les boutons pour le puzzle
        Collections.shuffle(buttons);

        // Ajouter les boutons à la grille de boutons
        buttons.forEach(panel::add);

        // Créer un panneau pour les boutons de commande
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, 50);
        add(buttonPanel);

        // Créer un bouton de retour
        backButton = new JButton("Back");
        backButton.addActionListener(this);
        backButton.setBounds(20, 10, 100, 40);
        buttonPanel.add(backButton);

        // Créer une étiquette qui affichera le temps écoulé
        JLabel timerLabel = new JLabel();
        timerLabel.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 50, 0, 100, 50);
        timerLabel.setFont(new Font("Z003", Font.PLAIN, 30));
        buttonPanel.add(timerLabel);

        // Initialiser un minuteur qui mettra à jour le temps toutes les secondes
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String timeString = String.format("%02d:%02d:%02d", hours, minutes, secs);
                timerLabel.setText(timeString);
                seconds++;
            }
        }, 1000, 1000);

        // Créer une étiquette qui affichera le score
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 250, 0, 200, 50);
        scoreLabel.setFont(new Font("Z003", Font.PLAIN, 30));
        buttonPanel.add(scoreLabel);

        // Créer un bouton de sortie
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Z003", Font.ITALIC, 32));
        exitButton.setForeground(Color.decode("#c27cea"));
        exitButton.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - 120, toolkitWidth - 60, 100, 50);
        add(exitButton);
        exitButton.addActionListener(this);

        // Créer une étiquette d'image
        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon("src/" + s + ".jpg").getImage().getScaledInstance(150, 100, 1)));
        imageLabel.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - 200, buttonPanel.getHeight(), 150, 100);
        this.add(imageLabel);

        // Activer le mode plein écran si pris en charge par l'ordinateur
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } // Sinon, redimensionner la fenêtre à la moitié de la taille de l'écran et la centrer
        else {
            this.setSize(gd.getDisplayMode().getWidth() / 2, gd.getDisplayMode().getHeight() / 2);
            this.setLocationRelativeTo(null);
        }

        // Définir l'action par défaut lors de la fermeture de la fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Rendre la fenêtre visible
        this.setVisible(true);

    }

    // Cette méthode est appelée lorsqu'un bouton est cliqué
    private void handleClick(ActionEvent e) {
        // Récupère le bouton qui a été cliqué
        JButton button = (JButton) e.getSource();
        // Récupère l'indice du bouton dans la liste des boutons
        int buttonIndex = buttons.indexOf(button);
        // Si c'est le premier clic
        if (firstClick) {
            // Echange la position du bouton cliqué avec le dernier bouton cliqué
            Collections.swap(buttons, buttonIndex, lastClicked);
            // Supprime tous les boutons du panel
            panel.removeAll();
            // Ajoute chaque bouton un par un au panel
            for (JButton btn : buttons) {
                // Sauf le bouton qui vient d'être cliqué
                if (!btn.equals(button)) {
                    // Enlève la bordure des autres boutons
                    btn.setBorder(null);
                }
                panel.add(btn);
            }
            // Valide la mise à jour du panel
            panel.validate();
        } else {
            // Si c'est le deuxième clic, ajoute une bordure rouge au bouton cliqué
            button.setBorder(BorderFactory.createLineBorder(Color.red, 3, true));
        }
        // Met à jour le label du score
        scoreLabel.setText("Score: " + score++);

        // Inverse la valeur de la variable firstClick
        firstClick = !firstClick;
        // Met à jour l'indice du dernier bouton cliqué
        lastClicked = buttonIndex;
        // Vérifie si la solution est trouvée
        checkSolution();
    }

    // Cette méthode vérifie si la solution a été trouvée
// Vérifie si le puzzle a été résolu
    private void checkSolution() {
        // Crée une liste de points qui représente la position actuelle de chaque bouton
        // On utilise la méthode stream() de la liste de boutons pour appliquer une opération à chaque élément
        // On utilise la méthode map() pour transformer chaque bouton en un Point représentant sa position
        // On utilise getClientProperty() pour récupérer la propriété "position" que nous avons définie lors de la création des boutons
        // On utilise ensuite collect() pour rassembler tous les points dans une liste
        List<Point> currentPositions = buttons.stream()
                .map(btn -> (Point) btn.getClientProperty("position"))
                .collect(Collectors.toList());

        // Si la solution correspond à la position actuelle des boutons
        if (solution.equals(currentPositions)) {
            // Arrête le timer
            timer.cancel();

            // Affiche un message de félicitations avec le temps écoulé et le score obtenu
            JOptionPane.showMessageDialog(panel, "Vous avez terminé en " + seconds + " secondes, et votre score est de " + score,
                    "Félicitations", JOptionPane.INFORMATION_MESSAGE);

            // Réinitialise le temps écoulé et cache le panel
            seconds = 0;
            panel.setVisible(false);

            // Attend une seconde avant d'afficher l'image finale
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PuzzlePanel.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Affiche l'image finale redimensionnée
            JLabel finalImage = new JLabel(new ImageIcon(resized));
            finalImage.setBounds(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
            this.add(finalImage);
        }
    }

    // Cette méthode redimensionne une image avec les dimensions spécifiées et le type d'image donné
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) {
        // Crée une nouvelle image redimensionnée avec les dimensions et le type donnés
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        // Dessine l'image originale dans la nouvelle image avec les dimensions données
        resizedImage.getGraphics().drawImage(originalImage, 0, 0, width, height, null);
        // Retourne l'image redimensionnée
        return resizedImage;
    }

    // Cette méthode est appelée lorsqu'un événement se produit dans l'interface graphique
    @Override
    public void actionPerformed(ActionEvent e) {
        // Si l'événement est causé par le bouton "exitButton", ferme l'application
        if (e.getSource() == exitButton) {
            System.exit(0);
        }
        // Si l'événement est causé par le bouton "backButton", arrête le timer et réinitialise le temps écoulé
        // puis crée une nouvelle fenêtre "SetLevel" et ferme la fenêtre actuelle pour revenir à la sélection de niveau
        if (e.getSource() == backButton) {
            timer.cancel();
            seconds = 0;

            // Effectue une transition vers la fenêtre "SetLevel"
            SwingUtilities.invokeLater(() -> {
                SetLevel setLevel = new SetLevel();
                setLevel.setVisible(true);
                dispose();
            });
        }
    }

}//end class PuzzlePanel
