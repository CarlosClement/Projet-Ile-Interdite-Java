package controleurs;

import modeles.IleModele;
import modeles.JoueurModele;
import modeles.ObjetModele;
import modeles.enums.ActionSpeciale;
import modeles.enums.Direction;
import modeles.enums.SituationZone;
import modeles.Zone;
import modeles.enums.TypeObjets;

import java.util.ArrayList;

public class JoueurControleur{
    private JoueurModele joueur;
    private IleModele modele;

    public JoueurControleur(JoueurModele joueur, IleModele modele){
        this.joueur = joueur;
        this.modele = modele;
    }

    public JoueurModele getJoueur() {
        return joueur;
    }

    public boolean deplacer(Direction direction){
        switch (direction) {
            case HAUT:
                if (this.joueur.getY() - 1 >= 0 && modele.getTab()[joueur.getX()][joueur.getY()-1].getSituationZone() != SituationZone.SUBMERGE) {
                    this.joueur.setY(this.joueur.getY() - 1, this.modele);
                    joueur.SetNbActions(joueur.getNbActions()-1);
                    return true;
                }
                return false;
            case BAS:
                if ((this.joueur.getY() + 1) < IleModele.getTailleIle() && modele.getTab()[joueur.getX()][joueur.getY()+1].getSituationZone() != SituationZone.SUBMERGE) {
                    this.joueur.setY(this.joueur.getY() + 1, this.modele);
                    joueur.SetNbActions(joueur.getNbActions()-1);
                    return true;
                }
                return false;
            case GAUCHE:
                if (this.joueur.getX() - 1 >= 0 && modele.getTab()[joueur.getX()-1][joueur.getY()].getSituationZone() != SituationZone.SUBMERGE) {
                    this.joueur.setX(this.joueur.getX() - 1, this.modele);
                    joueur.SetNbActions(joueur.getNbActions()-1);
                    return true;
                }
                return false;
            case DROITE:
                if ((this.joueur.getX() + 1) < IleModele.getTailleIle() && modele.getTab()[joueur.getX()+1][joueur.getY()].getSituationZone() != SituationZone.SUBMERGE) {
                    this.joueur.setX(this.joueur.getX() + 1, this.modele);
                    joueur.SetNbActions(joueur.getNbActions()-1);
                    return true;
                }
                return false;
        }
        return false;
    }

    public boolean assecher(){
        // Vérifie si la case actuelle OU une case adjacente est inondée
        boolean peutAssecher = false;

        // Case actuelle
        if (modele.getTab()[joueur.getX()][joueur.getY()].getSituationZone() == SituationZone.INONDEE) {
            peutAssecher = true;
        }
        // Cases adjacentes
        else {
            Zone[] voisins = joueur.getVoisins(modele);
            for (Zone voisin : voisins) {
                if (voisin.getSituationZone() == SituationZone.INONDEE) {
                    peutAssecher = true;
                    break;
                }
            }
        }
        return peutAssecher;
    }

    public boolean ramasserArtefact() {
        Zone zoneActuelle = modele.getTab()[joueur.getX()][joueur.getY()];
        ObjetModele objet = zoneActuelle.getObjet();

        if (objet != null && objet.getVisible() && joueur.getNbClesElt(objet.getType()) >= 4 && objet.EstArtefact()) {
            joueur.ajouterArtefact(objet.getType());
            joueur.elimineClesElt(4, objet.getType());
            zoneActuelle.retirerObjet();
            return true;
        }
        return false;
    }

    public boolean echangerCle(JoueurModele autreJoueur, TypeObjets cle){
        if (joueur.getCles().isEmpty()){
            return false;
        }
        autreJoueur.ajouterCle(cle);
        joueur.elimineClesElt(1, cle);

        return true;
    }

    public boolean utiliserSacDeSable(int x, int y) {
        if (joueur.utiliserActionSpeciale(ActionSpeciale.SAC_DE_SABLE)) {
            if (modele.getTab()[x][y].getSituationZone() == SituationZone.INONDEE) {
                modele.getTab()[x][y].setSituationZone(SituationZone.NORMALE);
                return true;
            }
        }
        return false;
    }

    public boolean utiliserHelicoptere(int x, int y, ArrayList<JoueurModele> joueursATransporter) {
        if (joueur.utiliserActionSpeciale(ActionSpeciale.HELICOPTERE)) {
            if (modele.getTab()[x][y].getSituationZone() != SituationZone.SUBMERGE) {
                // Déplacer le joueur actuel
                joueur.Bouger(x, y, modele);

                // Déplacer les joueurs sélectionnés
                for (JoueurModele j : joueursATransporter) {
                    j.Bouger(x, y, modele);
                }

                return true;
            }
        }
        return false;
    }

}
