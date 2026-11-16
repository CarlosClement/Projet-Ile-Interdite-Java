import modeles.IleModele;
import vues.IleVue;


public class Main {
    public static void main(String[] args) {
        IleModele modele = new IleModele();
        IleVue vue = new IleVue(modele, 4);
    }
}
