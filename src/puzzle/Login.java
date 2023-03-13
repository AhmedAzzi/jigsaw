package puzzle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author soumia
 */
public class Login extends JFrame implements ActionListener {

    private final JButton start;
    private final JTextField username;

    // Le constructeur de la classe Login
    public Login() {
        setLayout(null); // Définir la disposition à null pour permettre un positionnement manuel des éléments
        setPreferredSize(new Dimension(700, 600)); // Définir les dimensions préférées de la fenêtre
        // Charger une icône pour la fenêtre depuis un fichier "icon.png"
        try {
            Image im = ImageIO.read(new File("src/icon.png"));
            setIconImage(im);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        // Ajouter une étiquette pour demander le nom d'utilisateur
        JLabel name = new JLabel("Entrez votre nom:");
        name.setFont(new Font("Z003", Font.ITALIC, 32));
        name.setBounds(400, 150, 300, 30);
        this.add(name);

        // Ajouter un champ de texte pour saisir le nom d'utilisateur
        username = new JTextField();
        username.setFont(new Font("Z003", Font.PLAIN, 24));
        username.setBounds(400, 185, 250, 40);
        username.setBackground(new Color(0, 0, 0, 0)); // Définir un fond transparent
        username.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)); // Ajouter une bordure en bas
        this.add(username);

        // Ajouter un bouton "Start" pour lancer le jeu
        start = new JButton("Start");
        start.setFont(new Font("Z003", Font.ITALIC, 24));
        start.setBackground(Color.decode("#387ba5")); // Définir une couleur de fond
        start.setBounds(450, 250, 150, 50);
        start.addActionListener(this); // Ajouter un écouteur d'événements pour le clic
        this.add(start);

        // Ajouter une image de fond pour la fenêtre
        JLabel bg = new JLabel(new ImageIcon(new ImageIcon("puzzle_image_background.jpg").getImage().getScaledInstance(700, 600, 0)));
        bg.setBounds(0, 0, 700, 600);
        this.add(bg);

        this.pack(); // Ajuster la taille de la fenêtre en fonction des éléments
        this.setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        this.setResizable(false); // Empêcher le redimensionnement de la fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Définir l'action de fermeture de la fenêtre
    }

    // La méthode principale
    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException {
        // Définit l'apparence de l'interface utilisateur comme Nimbus
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        // Crée une nouvelle instance de la classe Login et la rend visible
        new Login().setVisible(true);
    }

    // La méthode appelée lorsqu'un événement est déclenché
    @Override
    public void actionPerformed(ActionEvent e) {
        // Vérifie si l'événement a été déclenché par le bouton "Start"
        if (e.getSource() == start) {
            // Vérifie si le champ de nom d'utilisateur est vide
            if (username.getText().trim().isEmpty()) {
                // Affiche une boîte de dialogue d'information demandant à l'utilisateur de saisir un nom d'utilisateur
                JOptionPane.showMessageDialog(this, "Enter User Name to Continue", "User Name",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Effectue la transition vers une autre fenêtre en créant une nouvelle instance de SetLevel et en la rendant visible
                SwingUtilities.invokeLater(() -> {
                    SetLevel setLevel = new SetLevel();
                    setLevel.setVisible(true);
                    dispose(); // ferme la fenêtre actuelle
                });
            }
        }
    }
}
