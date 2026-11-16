package vues;

import modeles.IleModele;
import modeles.ObjetModele;
import affichage.ImageManager;

import javax.swing.*;
import java.awt.*;

public class ObjetVue extends JPanel {
    private ObjetModele objet;
    private IleModele modele;
    private static final int TAILLE_OBJET = IleVue.TAILLE_CASE / 4;

    ObjetVue(IleModele modele, ObjetModele objet) {
        this.modele = modele;
        this.objet = objet;

        setOpaque(false);
        setPreferredSize(new Dimension(modele.getTailleIle() * IleVue.TAILLE_CASE, modele.getTailleIle() * IleVue.TAILLE_CASE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!objet.getVisible()) {
            return;
        }

        super.paintComponent(g);

        Image img = getTypeImage(objet);

        if (objet.EstArtefact()) {
            int objX = objet.getX() * IleVue.TAILLE_CASE + (IleVue.TAILLE_CASE - TAILLE_OBJET) / 4;
            int objY = objet.getY() * IleVue.TAILLE_CASE + (IleVue.TAILLE_CASE - TAILLE_OBJET) / 4;
            g.drawImage(img, objX, objY, TAILLE_OBJET, TAILLE_OBJET, this);

        }
    }

    private Image getTypeImage(ObjetModele objet) {
        switch (objet.getType()) {
            case EAU:
                return ImageManager.getImage("artefactEau");
            case FEU:
                return ImageManager.getImage("artefactFeu");
            case TERRE:
                return ImageManager.getImage("artefactTerre");
            default:
                return ImageManager.getImage("artefactAir");
        }
    }
}