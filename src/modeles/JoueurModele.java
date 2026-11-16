package modeles;

import modeles.enums.ActionSpeciale;
import modeles.enums.SituationZone;
import modeles.enums.TypeObjets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class JoueurModele{
    private int x_pos, y_pos;
    private Zone zone;
    private boolean vivant;
    private int NbActions;
    private boolean estActif;
    private Color couleur;
    private ArrayList<TypeObjets> artefacts = new ArrayList<>();
    private ArrayList<TypeObjets> cles = new ArrayList<>();
    private ArrayList<ActionSpeciale> actionsSpeciales = new ArrayList<>();

    public JoueurModele(IleModele ile, int x, int y, Color couleur){
        if (x < 0 || x >= IleModele.getTailleIle() || y < 0 || y >= IleModele.getTailleIle()) {
            throw new IllegalArgumentException("Vous etes en dehors de l'île !");
        }
        this.x_pos = x;
        this.y_pos = y;
        this.zone = ile.getTab()[x][y];
        this.zone.ajouterJoueur(this);
        this.vivant = true;
        this.NbActions = 3;
        this.estActif = false;
        this.couleur = couleur;
    }

    public int getX(){ return x_pos; }
    public int getY(){ return y_pos; }

    public void setX(int x_pos, IleModele ile) {
        // Retirer de l'ancienne zone
        this.zone.retirerJoueur(this);

        this.x_pos = x_pos;
        this.zone = ile.getTab()[this.x_pos][this.y_pos];
        this.zone.ajouterJoueur(this);
    }
    public void setY(int y_pos, IleModele ile) {
        this.zone.retirerJoueur(this);

        this.y_pos = y_pos;
        this.zone = ile.getTab()[this.x_pos][this.y_pos];
        this.zone.ajouterJoueur(this);
    }
    public void setZone(Zone z){
        this.zone = z;
        this.zone.ajouterJoueur(this);
    }

    public void ajouterActionSpeciale(ActionSpeciale action) {
        actionsSpeciales.add(action);
    }

    public boolean utiliserActionSpeciale(ActionSpeciale action) {
        if (actionsSpeciales.contains(action)) {
            return true;
        }
        return false;
    }

    public ArrayList<ActionSpeciale> getActionsSpeciales() {
        return actionsSpeciales;
    }

    public void Bouger(int x, int y, IleModele ile) {
        // Retirer le joueur de l'ancienne zone
        this.zone.retirerJoueur(this);

        this.x_pos = x;
        this.y_pos = y;
        this.zone = ile.getTab()[x][y];

        // Ajouter le joueur à la nouvelle zone
        this.zone.ajouterJoueur(this);
    }

    public Color getCouleur(){
        return couleur;
    }

    public int getNbActions(){return NbActions;}
    public boolean getActif(){return estActif;}
    public Zone getZone(){ return zone; }
    public boolean getVivant(){ return vivant; }

    public void SetNbActions(int nb){this.NbActions = nb;}

    public void setEstActif(boolean b){estActif = b;}

    public void ajouterArtefact(TypeObjets artefact) {
        artefacts.add(artefact);
    }

    public void ajouterCle(TypeObjets cle) {
        cles.add(cle);
    }

    public ArrayList<TypeObjets> getArtefacts() {
        return artefacts;
    }

    public ArrayList<TypeObjets> getCles() {
        return cles;
    }

    public Zone[] getVoisins(IleModele ile) {
        ArrayList<Zone> voisinsList = new ArrayList<>();
        // Voisin du haut (y-1)
        if (y_pos - 1 >= 0) {
            voisinsList.add(ile.getTab()[x_pos][y_pos - 1]);
        }
        // Voisin du bas (y+1)
        if (y_pos + 1 < IleModele.getTailleIle()) {
            voisinsList.add(ile.getTab()[x_pos][y_pos + 1]);
        }
        // Voisin de gauche (x-1)
        if (x_pos - 1 >= 0) {
            voisinsList.add(ile.getTab()[x_pos - 1][y_pos]);
        }
        // Voisin de droite (x+1)
        if (x_pos + 1 < IleModele.getTailleIle()) {
            voisinsList.add(ile.getTab()[x_pos + 1][y_pos]);
        }
        // Conversion de la ArrayList en tableau simple
        return voisinsList.toArray(new Zone[0]);
    }

    public Zone[] getAssechables(IleModele ile) {
        ArrayList<Zone> Assechables = new ArrayList<>();
        if (ile.getTab()[this.getX()][this.getY()].getSituationZone() == SituationZone.INONDEE){
            Assechables.add(ile.getTab()[this.getX()][this.getY()]);
        }
        for (Zone z : this.getVoisins(ile)){
            if (z.getSituationZone() == SituationZone.INONDEE){
                Assechables.add(z);
            }
        }
        return Assechables.toArray(new Zone[0]);
    }

    public Zone[] getVoisinsNonSubmerges(IleModele ile) {
        ArrayList<Zone> VoisinsNonSubmerges = new ArrayList<>();
        for (Zone z : this.getVoisins(ile)){
            if (z.getSituationZone() != SituationZone.SUBMERGE){
                VoisinsNonSubmerges.add(z);
            }
        }
        return VoisinsNonSubmerges.toArray(new Zone[0]);
    }

    public int getNbClesElt(TypeObjets type) {
        int cpt = 0;
        for (TypeObjets t : this.cles) {
            if (t == type) {
                cpt++;
            }
        }
        return cpt;
    }

    public void elimineClesElt(int n, TypeObjets t) {
        Iterator<TypeObjets> iterator = cles.iterator();
        int compteur = 0;

        while (iterator.hasNext() && compteur < n) {
            if (iterator.next() == t) {
                iterator.remove();
                compteur++;
            }
        }
    }

    @Override
    public String toString() {
        return "Joueur positionné en (" + x_pos + ", " + y_pos + ") sur " + zone.toString();
    }
}