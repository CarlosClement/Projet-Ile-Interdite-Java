package modeles;

import java.util.ArrayList;

import modeles.enums.SituationZone;
import modeles.enums.TypeZone;

public class Zone{
    private int x;
    private int y;
    private TypeZone typeZone;
    private SituationZone situationZone;
    private ObjetModele objet;
    private ArrayList<JoueurModele> joueursPresent;

    public Zone(int x, int y, TypeZone typeZone, SituationZone situationZone) {
        this.x = x;
        this.y = y;
        this.typeZone = typeZone;
        this.situationZone = situationZone;
        this.objet = null;
        this.joueursPresent = new ArrayList<>();
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public TypeZone getTypeZone() {
        return typeZone;
    }
    public SituationZone getSituationZone() {
        return situationZone;
    }

    public ObjetModele getObjet() { return objet; }
    public void setObjet(ObjetModele objet) { this.objet = objet; }
    public void retirerObjet() {
        this.objet.setVisible(false);
        this.setObjet(null);
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setTypeZone(TypeZone typeZone) {
        this.typeZone = typeZone;
    }
    public void setSituationZone(SituationZone situationZone) {
        this.situationZone = situationZone;
    }
    public void afficher() {
        System.out.println("Zone: (" + x + ", " + y + ") " + this.typeZone + " " + this.situationZone);
    }
    @Override
    public String toString(){
        return "[("+ x + ", " + y + ") " + this.typeZone + " " + this.situationZone + "]";
    }

    public ArrayList<JoueurModele> getJoueursPresent() {
        return joueursPresent;
    }

    public void ajouterJoueur(JoueurModele joueur) {
        if (!joueursPresent.contains(joueur)) {
            joueursPresent.add(joueur);
        }
    }

    public void retirerJoueur(JoueurModele joueur) {
        joueursPresent.remove(joueur);
    }
}
