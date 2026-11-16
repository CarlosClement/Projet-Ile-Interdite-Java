package modeles;

import modeles.enums.TypeObjets;

public class ObjetModele{
    private int x;
    private int y;
    private boolean estArtefact;
    private boolean estVisible;
    private TypeObjets typeObjet;

    public ObjetModele(int x, int y, boolean estArtefact, boolean estVisible, TypeObjets typeObjet) {
        this.x = x;
        this.y = y;
        this.estArtefact = estArtefact;
        this.estVisible = estVisible;
        this.typeObjet = typeObjet;
    }

    public boolean EstArtefact(){
        return estArtefact;
    }
    public boolean getVisible(){
        return estVisible;
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public TypeObjets getType(){return typeObjet;}
    public void setVisible(boolean b){this.estVisible = b;}
}
