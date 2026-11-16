package vues;

import modeles.IleModele;
import modeles.JoueurModele;
import affichage.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JoueurVue extends JPanel {
    private JoueurModele joueur;
    private IleModele modele;
    private static final int TAILLE_JOUEUR = IleVue.TAILLE_CASE / 3;

    public JoueurVue(IleModele modele, JoueurModele joueur) {
        this.modele = modele;
        this.joueur = joueur;

        setOpaque(false);
        setPreferredSize(new Dimension(modele.getTailleIle() * IleVue.TAILLE_CASE,
                modele.getTailleIle() * IleVue.TAILLE_CASE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Tous les joueurs dans la zone actuelle
        ArrayList<JoueurModele> joueursDansZone = modele.getTab()[joueur.getX()][joueur.getY()].getJoueursPresent();
        int nbJoueurs = joueursDansZone.size();

        for (int i = 0; i < nbJoueurs; i++) {
            JoueurModele currentPlayer = joueursDansZone.get(i);
            Image img = getJoueurImage(currentPlayer);

            int decalageX = (i % 2) * TAILLE_JOUEUR - TAILLE_JOUEUR/2;
            int decalageY = (i / 2) * TAILLE_JOUEUR - TAILLE_JOUEUR/2;

            int joueurX = joueur.getX() * IleVue.TAILLE_CASE
                    + (IleVue.TAILLE_CASE - TAILLE_JOUEUR) / 2
                    + decalageX;

            int joueurY = joueur.getY() * IleVue.TAILLE_CASE
                    + (IleVue.TAILLE_CASE - TAILLE_JOUEUR) / 2
                    + decalageY;

            if (!currentPlayer.getVivant()) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                g2d.drawImage(img, joueurX, joueurY, TAILLE_JOUEUR, TAILLE_JOUEUR, this);
            }
            else if (currentPlayer.getActif()) {
                g2d.drawImage(img, joueurX, joueurY, TAILLE_JOUEUR, TAILLE_JOUEUR, this);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
                g2d.setColor(new Color(255, 0, 0, 100));
                g2d.fillRect(joueurX, joueurY, TAILLE_JOUEUR, TAILLE_JOUEUR);
            }
            else {
                g2d.drawImage(img, joueurX, joueurY, TAILLE_JOUEUR, TAILLE_JOUEUR, this);
            }

            g2d.setComposite(AlphaComposite.SrcOver);
        }
    }

    private Image getJoueurImage(JoueurModele player) {
        Color c = player.getCouleur();

        if (c.equals(new Color(102, 0, 161))) {
            return ImageManager.getImage("joueurEau");
        } else if (c.equals(Color.GREEN)) {
            return ImageManager.getImage("joueurAir");
        } else if (c.equals(Color.CYAN)) {
            return ImageManager.getImage("joueurFeu");
        } else if (c.equals(Color.YELLOW)) {
            return ImageManager.getImage("joueurTerre");
        }
        return ImageManager.getImage("joueurEau");
    }
}
