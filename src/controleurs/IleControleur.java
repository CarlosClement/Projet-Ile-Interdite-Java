package controleurs;

import modeles.IleModele;
import modeles.JoueurModele;
import modeles.Zone;
import modeles.enums.ActionSpeciale;
import modeles.enums.SituationZone;
import modeles.enums.TypeObjets;

import java.util.ArrayList;
import java.util.Random;

public class IleControleur{
    private IleModele modele;
    private JoueurModele[] joueurs;
    private int joueurActuel;

    public IleControleur(IleModele modele, JoueurModele[] joueurs) {
        this.modele = modele;
        this.joueurs = joueurs;
        this.joueurActuel = 0;
    }
    public int chercherCle(){
        Random rand = new Random();
        int chance = rand.nextInt(5);
        int cpt = 0;

        switch(chance) {
            case 0:
                cpt = 0; break;
            case 1:
                if (modele.getTab()[joueurs[joueurActuel].getX()][joueurs[joueurActuel].getY()].getSituationZone() == SituationZone.NORMALE){
                    modele.getTab()[joueurs[joueurActuel].getX()][joueurs[joueurActuel].getY()].setSituationZone(SituationZone.INONDEE);
                }
                else{
                    modele.getTab()[joueurs[joueurActuel].getX()][joueurs[joueurActuel].getY()].setSituationZone(SituationZone.SUBMERGE);
                }
                cpt = 1; break;
            case 2:
                TypeObjets[] types = TypeObjets.values();
                TypeObjets typeCle = types[new Random().nextInt(types.length)];
                joueurs[joueurActuel].ajouterCle(typeCle);
                cpt = 2; break;
            case 3:
                joueurs[joueurActuel].ajouterActionSpeciale(ActionSpeciale.SAC_DE_SABLE);
                cpt = 3; break;
            case 4:
                joueurs[joueurActuel].ajouterActionSpeciale(ActionSpeciale.HELICOPTERE);
                cpt = 4; break;
        }
        boolean fuite = false;
        for (JoueurModele j : this.joueurs){
            if (modele.getTab()[j.getX()][j.getY()].getSituationZone() == SituationZone.SUBMERGE){
                if (j.getVoisinsNonSubmerges(this.modele).length == 0){
                    return 5;
                }
                else fuite = true;
            }
        }
        if (fuite) return 6;

        return cpt;
    }

    public int finDeTour() {
        //Recherche de clés. Soit ne donne rien, soit inondation zonnedujoueur soit clé aléatoire, soit action spéciale
        // Soit victoire, soit défaite
        inonderZoneAleatoire();
        if (verifierVictoire()){
            return 1;
        }
        if (verifierDefaite()){
            return 2;
        }
        boolean fuite = false;
        for (JoueurModele j : this.joueurs){
            if (modele.getTab()[j.getX()][j.getY()].getSituationZone() == SituationZone.SUBMERGE){
                if (j.getVoisinsNonSubmerges(this.modele).length == 0){
                    return 3;
                }
                else fuite = true;
            }
        }
        if (fuite) return 4;
        // Passage au joueur suivant
        joueurs[joueurActuel].setEstActif(false);
        joueurActuel = (joueurActuel + 1) % joueurs.length;
        joueurs[joueurActuel].SetNbActions(3);
        joueurs[joueurActuel].setEstActif(true);

        return 0;
    }

    private void inonderZoneAleatoire() {
        Random rand = new Random();
        int r1 = rand.nextInt(IleModele.getTailleIle());
        int r2 = rand.nextInt(IleModele.getTailleIle());
        for (int in = 0; in < 3; in++) {
            int essai = 0;
            while (modele.getTab()[r1][r2].getSituationZone() == SituationZone.SUBMERGE) {
                r1 = rand.nextInt(IleModele.getTailleIle());
                r2 = rand.nextInt(IleModele.getTailleIle());
                essai++;
                if (essai >= 10) break;
            }
            if (modele.getTab()[r1][r2].getSituationZone() != SituationZone.SUBMERGE) {
                if (modele.getTab()[r1][r2].getSituationZone() == SituationZone.INONDEE) {
                    modele.getTab()[r1][r2].setSituationZone(SituationZone.SUBMERGE);
                } else {
                    modele.getTab()[r1][r2].setSituationZone(SituationZone.INONDEE);
                }
                r1 = rand.nextInt(IleModele.getTailleIle());
                r2 = rand.nextInt(IleModele.getTailleIle());
            }
        }
    }

    public boolean verifierVictoire() {
        // Vérifier que tous les joueurs sont sur l'héliport
        Zone heliport = modele.getTab()[IleModele.getTailleIle()/2][IleModele.getTailleIle()/2];

        for (JoueurModele joueur : joueurs) {
            if (joueur.getX() != heliport.getX() || joueur.getY() != heliport.getY()) {
                return false;
            }
        }

        // Vérifier que les 4 artefacts ont été collectés
        ArrayList<TypeObjets> artefactsCollectes = new ArrayList<>();
        for (JoueurModele joueur : joueurs) {
            artefactsCollectes.addAll(joueur.getArtefacts());
        }

        return artefactsCollectes.contains(TypeObjets.EAU) &&
                artefactsCollectes.contains(TypeObjets.FEU) &&
                artefactsCollectes.contains(TypeObjets.TERRE) &&
                artefactsCollectes.contains(TypeObjets.VENT);
    }

    public boolean verifierDefaite() {
        //Si l'un des joueurs est mort
        for (JoueurModele joueur : joueurs) {
            if (!joueur.getVivant()) {
                return false;
            }
        }
        //Si l'un des quatre temples sont inondés
        Zone templeEau = modele.getTab()[0][0];
        Zone templeFeu = modele.getTab()[0][IleModele.getTailleIle()-1];
        Zone templeTerre = modele.getTab()[IleModele.getTailleIle()-1][0];
        Zone templeVent = modele.getTab()[IleModele.getTailleIle()-1][IleModele.getTailleIle()-1];

        //Si l'héliport est submergé
        Zone heliport = modele.getTab()[IleModele.getTailleIle()/2][IleModele.getTailleIle()/2];
        return heliport.getSituationZone() == SituationZone.SUBMERGE ||
                (templeEau.getSituationZone() == SituationZone.SUBMERGE && templeEau.getObjet() != null)||
                (templeFeu.getSituationZone() == SituationZone.SUBMERGE && templeFeu.getObjet() != null)||
                (templeTerre.getSituationZone() == SituationZone.SUBMERGE && templeTerre.getObjet() != null)||
                (templeVent.getSituationZone() == SituationZone.SUBMERGE && templeVent.getObjet() != null);
    }

    public int getJoueurActuel(){return joueurActuel;}

    public void setJoueurActuel(int joueur){
        this.joueurActuel = joueur;
    }
}
