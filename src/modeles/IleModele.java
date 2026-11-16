package modeles;

import modeles.enums.SituationZone;
import modeles.enums.TypeZone;

public class IleModele{
    private static final int TAILLE_ILE = 8;
    private Zone[][] tab;

    public IleModele() {
        tab = new Zone[TAILLE_ILE][TAILLE_ILE];
        for (int i = 0; i < TAILLE_ILE; i++) {
            for (int j = 0; j < TAILLE_ILE; j++) {
                this.tab[i][j] = new Zone(i, j, TypeZone.NORMAL, SituationZone.NORMALE);
            }
        }
        //Initialisation des zones spéciales
        this.tab[0][0].setTypeZone(TypeZone.EAU);
        this.tab[0][TAILLE_ILE-1].setTypeZone(TypeZone.FEU);
        this.tab[TAILLE_ILE-1][0].setTypeZone(TypeZone.TERRE);
        this.tab[TAILLE_ILE-1][TAILLE_ILE-1].setTypeZone(TypeZone.VENT);
        this.tab[TAILLE_ILE/2][TAILLE_ILE/2].setTypeZone(TypeZone.HELIPORT);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAILLE_ILE; i++) {
            for (int j = 0; j < TAILLE_ILE; j++) {
                sb.append(this.tab[i][j].toString() + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static int getTailleIle() {
        return TAILLE_ILE;
    }

    public Zone[][] getTab() {
        return tab;
    }
}