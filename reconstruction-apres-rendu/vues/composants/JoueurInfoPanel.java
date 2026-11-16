/*
 * ============================================================================
 *  FICHIER ÉCRIT APRÈS LE RENDU (novembre 2026) — NE FAIT PAS PARTIE DU RENDU
 *  Écrit avec l'aide d'une IA (Claude), pour que le projet compile à nouveau.
 * ============================================================================
 *  La classe d'origine vues.composants.JoueurInfoPanel n'a jamais été poussée
 *  sur le dépôt GitLab du cours : IleVue.java l'importe, mais le fichier est
 *  absent, donc le projet tel que récupéré ne compile pas.
 *
 *  Ce remplacement minimal reprend seulement ce que le reste du code et le
 *  rapport permettent d'affirmer :
 *    - champs `joueur` (JoueurModele) et `numeroJoueur` (int), visibles sur le
 *      diagramme de classes (docs/diagramme_de_classes.pdf) ;
 *    - constructeur JoueurInfoPanel(JoueurModele, int) et méthode updateInfo(),
 *      appelés par IleVue ;
 *    - affichage des informations du joueur (clés, artefacts, actions
 *      spéciales), d'après la section 2 du rapport.
 *  L'apparence d'origine n'est pas connue ; celle-ci est volontairement sobre.
 * ============================================================================
 */
package vues.composants;

import modeles.JoueurModele;

import javax.swing.*;
import java.awt.*;

public class JoueurInfoPanel extends JPanel {
    private final JoueurModele joueur;
    private final int numeroJoueur;

    private final JLabel labelCles = new JLabel();
    private final JLabel labelArtefacts = new JLabel();
    private final JLabel labelActions = new JLabel();

    public JoueurInfoPanel(JoueurModele joueur, int numeroJoueur) {
        this.joueur = joueur;
        this.numeroJoueur = numeroJoueur;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(joueur.getCouleur(), 2),
                "Joueur " + (numeroJoueur + 1)));
        add(labelCles);
        add(labelArtefacts);
        add(labelActions);
        updateInfo();
    }

    public void updateInfo() {
        labelCles.setText("Clés : " + joueur.getCles());
        labelArtefacts.setText("Artefacts : " + joueur.getArtefacts());
        labelActions.setText("Actions spé. : " + joueur.getActionsSpeciales());
    }
}
