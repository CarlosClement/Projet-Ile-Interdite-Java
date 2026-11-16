package vues;

import controleurs.IleControleur;
import controleurs.JoueurControleur;
import modeles.IleModele;
import modeles.JoueurModele;
import modeles.ObjetModele;
import modeles.enums.ActionSpeciale;
import modeles.enums.Direction;
import modeles.enums.SituationZone;
import modeles.enums.TypeObjets;
import affichage.ImageManager;
import vues.composants.JoueurInfoPanel;
import modeles.Zone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class IleVue extends JPanel {
    private JFrame frame;
    private IleModele modele;
    private JoueurModele[] joueurs;  // Tableau de joueurs
    private JoueurVue[] joueursVues; // Tableau de vues des joueurs
    private ObjetModele[] objets;
    private ObjetVue[] vueObjets;

    public static final int TAILLE_CASE = 80;
    private IleControleur ileControleur;
    private JoueurControleur[] joueursControleurs;

    private boolean Assecher = false;
    private boolean PlusActions = true;
    private boolean Fuir = false;
    private boolean aChercherCle = false;
    private boolean utiliserSacDeSable = false;
    private boolean utiliserHelicoptere = false;

    private JLabel labelJoueur;
    private JLabel labelActions;
    private JLabel labelMessage;

    private JoueurInfoPanel[] joueurInfoPanels;

    public IleVue(IleModele modele, int nombreJoueurs) {
        this.modele = modele;

        // Initialisation des joueurs
        this.joueurs = new JoueurModele[nombreJoueurs];
        this.joueursVues = new JoueurVue[nombreJoueurs];

        //Initialisation des objets
        this.objets = new ObjetModele[4];
        this.vueObjets = new ObjetVue[4];

        this.ileControleur = new IleControleur(modele, joueurs);
        this.joueursControleurs = new JoueurControleur[nombreJoueurs];

        // Positions de départ différentes pour chaque joueur
        int[][] positionsDepartJoueurs = {{IleModele.getTailleIle() / 2 - 1, IleModele.getTailleIle() / 2 - 1}, {IleModele.getTailleIle() / 2 - 1, IleModele.getTailleIle() / 2},
                {IleModele.getTailleIle() / 2, IleModele.getTailleIle() / 2 - 1}, {IleModele.getTailleIle() / 2, IleModele.getTailleIle() / 2}};

        int[][] positionsDepartArtefacts = {{0, 0}, {0, IleModele.getTailleIle() - 1}, {IleModele.getTailleIle() - 1, 0}, {IleModele.getTailleIle() - 1, IleModele.getTailleIle() - 1}};

        //Couleurs des joueurs
        Color[] couleurs = {new Color(102, 0, 161), Color.GREEN, Color.CYAN, Color.YELLOW};

        for (int i = 0; i < nombreJoueurs; i++) {
            this.joueurs[i] = new JoueurModele(modele, positionsDepartJoueurs[i][0], positionsDepartJoueurs[i][1], couleurs[i]);
            this.joueursVues[i] = new JoueurVue(modele, joueurs[i]);
            this.joueursControleurs[i] = new JoueurControleur(joueurs[i], modele);
        }
        this.joueurs[0].setEstActif(true);

        joueurInfoPanels = new JoueurInfoPanel[nombreJoueurs];
        for (int i = 0; i < nombreJoueurs; i++) {
            joueurInfoPanels[i] = new JoueurInfoPanel(joueurs[i], i);
        }

        this.objets[0] = new ObjetModele(
                positionsDepartArtefacts[0][0],
                positionsDepartArtefacts[0][1],
                true,
                true,
                TypeObjets.EAU
        );
        this.objets[1] = new ObjetModele(
                positionsDepartArtefacts[1][0],
                positionsDepartArtefacts[1][1],
                true,
                true,
                TypeObjets.FEU
        );
        this.objets[2] = new ObjetModele(
                positionsDepartArtefacts[2][0],
                positionsDepartArtefacts[2][1],
                true,
                true,
                TypeObjets.TERRE
        );
        this.objets[3] = new ObjetModele(
                positionsDepartArtefacts[3][0],
                positionsDepartArtefacts[3][1],
                true,
                true,
                TypeObjets.VENT
        );
        for (int i = 0; i < 4; i++) {
            this.vueObjets[i] = new ObjetVue(modele, this.objets[i]);
        }

        for (ObjetModele objet : objets) {
            this.modele.getTab()[objet.getX()][objet.getY()].setObjet(objet);
        }

        frame = new JFrame();
        frame.setTitle("Île interdite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(IleModele.getTailleIle() * TAILLE_CASE + 600, IleModele.getTailleIle() * TAILLE_CASE + 200);

        JLayeredPane CouchesEcran = new JLayeredPane();
        CouchesEcran.setPreferredSize(new Dimension(IleModele.getTailleIle() * TAILLE_CASE, IleModele.getTailleIle() * TAILLE_CASE));

        this.setBounds(0, 0, IleModele.getTailleIle() * TAILLE_CASE, IleModele.getTailleIle() * TAILLE_CASE);

        // Ajout des vues des joueurs
        for (JoueurVue joueurVue : joueursVues) {
            joueurVue.setBounds(0, 0, IleModele.getTailleIle() * TAILLE_CASE, IleModele.getTailleIle() * TAILLE_CASE);
            CouchesEcran.add(joueurVue, Integer.valueOf(2));
        }
        //Ajout des vues des objets
        for (ObjetVue obj : vueObjets) {
            obj.setBounds(0, 0, IleModele.getTailleIle() * TAILLE_CASE, IleModele.getTailleIle() * TAILLE_CASE);
            CouchesEcran.add(obj, Integer.valueOf(1));
        }

        CouchesEcran.add(this, Integer.valueOf(0));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(350, IleModele.getTailleIle() * TAILLE_CASE));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        Font policeInfo = new Font("Arial", Font.BOLD, 14);
        labelJoueur = new JLabel("Joueur actuel: 1");
        labelActions = new JLabel("Actions restantes: 3");
        labelMessage = new JLabel(" ");

        labelJoueur.setFont(policeInfo);
        labelActions.setFont(policeInfo);
        labelMessage.setFont(policeInfo);

        infoPanel.add(labelJoueur);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(labelActions);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(labelMessage);

        messagePanel.add(labelJoueur);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        messagePanel.add(labelActions);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        messagePanel.add(labelMessage);

        infoPanel.add(messagePanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel joueursInfoPanel = new JPanel();
        joueursInfoPanel.setLayout(new GridLayout(2, 2, 10, 10));

        for (JoueurInfoPanel jip : joueurInfoPanels) {
            joueursInfoPanel.add(jip);
        }

        infoPanel.add(joueursInfoPanel);

        updateInfoLabels("", Color.GREEN);

        JButton FDT = new JButton("Fin de tour");
        FDT.setPreferredSize(new Dimension(100, 50));

        FDT.addActionListener(e -> {
            if (Fuir) {
                updateInfoLabels("Il reste des joueurs pour fuir !", Color.RED);
            } else if (utiliserSacDeSable) {
                updateInfoLabels("Vous devez finir d'utiliser sac de sable !", Color.RED);
            } else if (utiliserHelicoptere) {
                updateInfoLabels("Vous devez choisir votre destination !", Color.RED);
            } else if (Assecher) {
                updateInfoLabels("Vous devez assécher une zone !", Color.RED);
            } else {
                // Inondation de zones
                int cpt = ileControleur.finDeTour();
                Assecher = false;
                aChercherCle = false;
                this.PlusActions = true;
                if (cpt == 0) {
                    updateInfoLabels("", Color.BLACK);
                } else if (cpt == 1) {
                    JOptionPane.showMessageDialog(frame,
                            "Félicitations ! Vous avez gagné !\n" +
                                    "Tous les joueurs sont sur l'héliport avec les 4 artefacts.",
                            "Victoire",
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    return;
                } else if (cpt == 2) {
                    JOptionPane.showMessageDialog(frame,
                            "Défaite ! Vous avez perdu !",
                            "Défaite",
                            JOptionPane.ERROR_MESSAGE);
                    frame.dispose();
                    return;
                } else if (cpt == 3) {
                    JOptionPane.showMessageDialog(frame,
                            "Défaite ! L'un des joueurs s'est noyé !",
                            "Défaite",
                            JOptionPane.ERROR_MESSAGE);
                    frame.dispose();
                    return;
                } else if (cpt == 4) {
                    updateInfoLabels("Cliquez sur une zone pour fuir !", Color.GREEN);
                    Fuir = true;
                }
            }
            repaint();
            this.requestFocusInWindow();
        });

        JButton AS = new JButton("Assécher");
        AS.setPreferredSize(new Dimension(150, 50));

        AS.addActionListener(e -> {
            JoueurControleur joueurCourant = joueursControleurs[ileControleur.getJoueurActuel()];
            if (Fuir) {
                updateInfoLabels("Il reste des joueurs pour fuir !", Color.RED);
            } else if (utiliserSacDeSable) {
                updateInfoLabels("Vous devez finir d'utiliser sac de sable !", Color.RED);
            } else if (utiliserHelicoptere) {
                updateInfoLabels("Vous devez choisir votre destination !", Color.RED);
            } else if (joueurCourant.getJoueur().getNbActions() <= 0) {
                PlusActions = false;
                updateInfoLabels("Vous n'avez plus d'actions !", Color.RED);
            } else {
                boolean peutAssecher = joueursControleurs[this.ileControleur.getJoueurActuel()].assecher();
                if (!peutAssecher) {
                    Assecher = false;
                    updateInfoLabels("Vous ne pouvez pas assécher", Color.RED);
                } else {
                    Assecher = true;
                    joueurCourant.getJoueur().SetNbActions(joueurCourant.getJoueur().getNbActions() - 1);
                    updateInfoLabels("Cliquez sur la zone à assécher", Color.yellow);
                }
            }
            repaint();
            this.requestFocusInWindow();
        });

        JButton ramasserBtn = new JButton("Ramasser Artefact");
        ramasserBtn.setPreferredSize(new Dimension(150, 50));

        ramasserBtn.addActionListener(e -> {
            JoueurControleur joueurCourant = joueursControleurs[ileControleur.getJoueurActuel()];
            if (Fuir) {
                updateInfoLabels("Il reste des joueurs pour fuir !", Color.RED);
            } else if (utiliserSacDeSable) {
                updateInfoLabels("Vous devez finir d'utiliser sac de sable !", Color.RED);
            } else if (utiliserHelicoptere) {
                updateInfoLabels("Vous devez choisir votre destination !", Color.RED);
            } else if (Assecher) {
                updateInfoLabels("Vous devez assécher !", Color.RED);
            } else if (joueurCourant.getJoueur().getNbActions() <= 0) {
                PlusActions = false;
                updateInfoLabels("Vous n'avez plus d'actions !", Color.RED);
            } else {
                boolean aRamasse = joueurCourant.ramasserArtefact();
                if (!aRamasse) {
                    updateInfoLabels("Erreur lors du ramassage !", Color.RED);
                } else {
                    labelMessage.setText("Artefact ramassé !");
                    labelMessage.setForeground(Color.GREEN);
                    joueurCourant.getJoueur().SetNbActions(joueurCourant.getJoueur().getNbActions() - 1);
                }
            }
            repaint();
            this.requestFocusInWindow();
        });
        JButton chercherCle = new JButton("Chercher clé");
        chercherCle.setPreferredSize(new Dimension(150, 50));

        chercherCle.addActionListener(e -> {
            if (Fuir) {
                updateInfoLabels("Il reste des joueurs pour fuir !", Color.RED);
            } else if (utiliserSacDeSable) {
                updateInfoLabels("Vous devez finir d'utiliser sac de sable !", Color.RED);
            } else if (utiliserHelicoptere) {
                updateInfoLabels("Vous devez choisir votre destination !", Color.RED);
            } else if (aChercherCle) {
                updateInfoLabels("Vous avez déjà cherché une clé", Color.RED);
            } else {
                int cpt = ileControleur.chercherCle();
                JoueurControleur joueurCourant = joueursControleurs[ileControleur.getJoueurActuel()];
                joueurCourant.getJoueur().SetNbActions(0);
                if (cpt == 0) {
                    updateInfoLabels("Il ne s'est rien passé !", Color.BLACK);
                } else if (cpt == 1) {
                    updateInfoLabels("Votre zone s'est inondée !", Color.RED);
                } else if (cpt == 2) {
                    updateInfoLabels("Vous avez reçu une clé !", Color.GREEN);
                } else if (cpt == 3) {
                    updateInfoLabels("Vous avez reçu l'action Sac de Sable !", Color.GREEN);
                } else if (cpt == 4) {
                    updateInfoLabels("Vous avez reçu l'action élicoptère !", Color.GREEN);
                } else if (cpt == 5) {
                    JOptionPane.showMessageDialog(frame,
                            "Défaite ! L'un des joueurs s'est noyé !",
                            "Défaite",
                            JOptionPane.ERROR_MESSAGE);
                    frame.dispose();
                    return;
                } else if (cpt == 6) {
                    updateInfoLabels("Cliquez sur une zone pour fuir !", Color.GREEN);
                    Fuir = true;
                }
                aChercherCle = true;
            }
            repaint();
            this.requestFocusInWindow();
        });

        JButton echangerCle = new JButton("Echanger");
        echangerCle.setPreferredSize(new Dimension(150, 50));

        echangerCle.addActionListener(e -> {
            JoueurControleur joueurCourant = joueursControleurs[ileControleur.getJoueurActuel()];
            if (Fuir) {
                updateInfoLabels("Il reste des joueurs pour fuir !", Color.RED);
            } else if (utiliserSacDeSable) {
                updateInfoLabels("Vous devez finir d'utiliser sac de sable !", Color.RED);
            } else if (utiliserHelicoptere) {
                updateInfoLabels("Vous devez choisir votre destination !", Color.RED);
            } else if (Assecher) {
                updateInfoLabels("Vous devez assécher !", Color.RED);
            } else if (joueurCourant.getJoueur().getNbActions() <= 0) {
                PlusActions = false;
                updateInfoLabels("Vous n'avez plus d'actions !", Color.RED);
            } else {

                Zone zoneActuelle = modele.getTab()[joueurCourant.getJoueur().getX()][joueurCourant.getJoueur().getY()];
                ArrayList<JoueurModele> autresJoueurs = new ArrayList<>(zoneActuelle.getJoueursPresent());
                autresJoueurs.remove(joueurCourant.getJoueur());

                if (autresJoueurs.isEmpty()) {
                    updateInfoLabels("Personne avec qui échanger !", Color.RED);
                    repaint();
                    this.requestFocusInWindow();
                    return;
                }

                if (joueurCourant.getJoueur().getCles().isEmpty()) {
                    updateInfoLabels("Vous n'avez pas de clés !", Color.RED);
                    repaint();
                    this.requestFocusInWindow();
                    return;
                }

                JDialog dialog = new JDialog(frame, "Échanger des clés", true);
                dialog.setLayout(new BorderLayout());

                // Panel pour la sélection
                JPanel selectionPanel = new JPanel(new GridLayout(2, 1, 10, 10));

                // Sélection du joueur
                JLabel joueurLabel = new JLabel("Joueur cible:");
                JComboBox<JoueurModele> joueurCombo = new JComboBox<>(autresJoueurs.toArray(new JoueurModele[0]));
                joueurCombo.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof JoueurModele) {
                            setText("Joueur " + (Arrays.asList(joueurs).indexOf((JoueurModele) value) + 1));
                        }
                        return this;
                    }
                });

                // Sélection de la clé à donner
                JLabel cleDonneeLabel = new JLabel("Clé à donner:");
                JComboBox<TypeObjets> cleDonneeCombo = new JComboBox<>(joueurCourant.getJoueur().getCles().toArray(new TypeObjets[0]));

                selectionPanel.add(joueurLabel);
                selectionPanel.add(joueurCombo);
                selectionPanel.add(cleDonneeLabel);
                selectionPanel.add(cleDonneeCombo);

                // Bouton de validation
                JButton validerButton = new JButton("Échanger");
                validerButton.addActionListener(ev -> {
                    JoueurModele cible = (JoueurModele) joueurCombo.getSelectedItem();
                    TypeObjets cleDonnee = (TypeObjets) cleDonneeCombo.getSelectedItem();

                    if (cible != null && cleDonnee != null) {
                        boolean succes = joueurCourant.echangerCle(cible, cleDonnee);

                        if (succes) {
                            joueurCourant.getJoueur().SetNbActions(joueurCourant.getJoueur().getNbActions() - 1);
                            updateInfoLabels("Échange réussi !", Color.GREEN);

                            for (JoueurInfoPanel jip : joueurInfoPanels) {
                                jip.updateInfo();
                            }

                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Échange impossible !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                dialog.add(selectionPanel, BorderLayout.CENTER);
                dialog.add(validerButton, BorderLayout.SOUTH);
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
            repaint();
            this.requestFocusInWindow();
        });
        JButton actionSpeciale = new JButton("Action Spéciale");
        actionSpeciale.setPreferredSize(new Dimension(150, 50));

        actionSpeciale.addActionListener(e -> {
            if (utiliserSacDeSable) {
                updateInfoLabels("Vous devez finir d'utiliser sac de sable !", Color.RED);
            } else if (utiliserHelicoptere) {
                updateInfoLabels("Vous devez choisir votre destination !", Color.RED);
            }
            JoueurModele joueurCourant = joueurs[ileControleur.getJoueurActuel()];
            ArrayList<ActionSpeciale> actionsDisponibles = joueurCourant.getActionsSpeciales();

            if (actionsDisponibles.isEmpty()) {
                updateInfoLabels("Aucune action spéciale disponible", Color.RED);
                return;
            }

            // Créer un menu déroulant pour choisir l'action
            JDialog dialog = new JDialog(frame, "Choisir une action spéciale", true);
            dialog.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridLayout(actionsDisponibles.size() + 1, 1));
            JLabel label = new JLabel("Choisissez une action spéciale:");
            panel.add(label);

            for (ActionSpeciale action : actionsDisponibles) {
                JButton actionBtn = new JButton(action.toString());
                actionBtn.addActionListener(ev -> {
                    dialog.dispose();
                    faireActionSpetiale(action);
                });
                panel.add(actionBtn);
            }

            dialog.add(panel, BorderLayout.CENTER);
            dialog.pack();
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        });

        //Gestion de la souris
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int X = e.getX() / TAILLE_CASE;
                int Y = e.getY() / TAILLE_CASE;
                if (Assecher) {

                    boolean aAsseche = false;

                    for (Zone z : joueurs[ileControleur.getJoueurActuel()].getAssechables(modele)) {
                        if (X == z.getX() && Y == z.getY()) {
                            modele.getTab()[X][Y].setSituationZone(SituationZone.NORMALE);
                            aAsseche = true;
                            break;
                        }
                    }

                    Assecher = false;
                    if (aAsseche) {
                        updateInfoLabels("Zone asséchée", Color.GREEN);
                    } else {
                        updateInfoLabels("Zone non valide pour assèchement !", Color.RED);
                    }
                    repaint();
                } else if (Fuir) {

                    // Trouver tous les joueurs submergés
                    ArrayList<JoueurModele> joueursSubmerges = new ArrayList<>();
                    for (JoueurModele j : joueurs) {
                        if (modele.getTab()[j.getX()][j.getY()].getSituationZone() == SituationZone.SUBMERGE) {
                            joueursSubmerges.add(j);
                        }
                    }

                    // Si aucun joueur submergé, erreur
                    if (joueursSubmerges.isEmpty()) {
                        Fuir = false;
                        updateInfoLabels("Erreur: aucun joueur à sauver !", Color.RED);
                        repaint();
                        return;
                    }

                    // Prendre le premier joueur submergé
                    JoueurModele joueurFuit = joueursSubmerges.get(0);

                    // Vérifier si la case cliquée est une case valide pour fuir
                    boolean caseValide = false;
                    for (Zone z : joueurFuit.getVoisinsNonSubmerges(modele)) {
                        if (z.getX() == X && z.getY() == Y) {
                            caseValide = true;
                            break;
                        }
                    }
                    if (caseValide) {
                        // Déplacer le joueur
                        joueurFuit.Bouger(X, Y, modele);

                        // Vérifier s'il reste d'autres joueurs submergés
                        boolean encoreSubmerge = false;
                        for (JoueurModele j : joueurs) {
                            if (modele.getTab()[j.getX()][j.getY()].getSituationZone() == SituationZone.SUBMERGE) {
                                encoreSubmerge = true;
                                break;
                            }
                        }
                        if (encoreSubmerge) {
                            updateInfoLabels("Un autre joueur doit fuir !", Color.RED);
                        } else {
                            Fuir = false;
                            // Passer au joueur suivant normalement
                            if (aChercherCle) {
                                joueurs[ileControleur.getJoueurActuel()].setEstActif(false);
                                ileControleur.setJoueurActuel((ileControleur.getJoueurActuel() + 1) % joueurs.length);
                                joueurs[ileControleur.getJoueurActuel()].setEstActif(true);
                                joueurs[ileControleur.getJoueurActuel()].SetNbActions(3);
                                updateInfoLabels("Tous les joueurs sont sauvés !", Color.GREEN);
                            } else {
                                updateInfoLabels("Vous êtes sauvé !", Color.RED);
                            }
                        }
                    } else {
                        updateInfoLabels("Zone non valide pour fuir !", Color.RED);
                    }
                    repaint();
                }
                if (utiliserSacDeSable) {
                    boolean reussi = joueursControleurs[ileControleur.getJoueurActuel()].utiliserSacDeSable(X, Y);
                    if (reussi) {
                        updateInfoLabels("Zone asséchée avec le sac de sable", Color.GREEN);
                        utiliserSacDeSable = false;
                        joueursControleurs[ileControleur.getJoueurActuel()].getJoueur().getActionsSpeciales().remove(ActionSpeciale.SAC_DE_SABLE);
                    } else {
                        updateInfoLabels("Zone non valide pour assèchement", Color.RED);
                    }
                    repaint();
                } else if (utiliserHelicoptere) {
                    // Récupérer les joueurs sur la case actuelle (sauf le joueur actuel)
                    Zone zoneActuelle = modele.getTab()[joueurs[ileControleur.getJoueurActuel()].getX()][joueurs[ileControleur.getJoueurActuel()].getY()];
                    ArrayList<JoueurModele> autresJoueurs = new ArrayList<>(zoneActuelle.getJoueursPresent());
                    autresJoueurs.remove(joueurs[ileControleur.getJoueurActuel()]);

                    if (!autresJoueurs.isEmpty()) {
                        // Demander quels joueurs transporter
                        JDialog transportDialog = new JDialog(frame, "Choisir les joueurs à transporter", true);
                        transportDialog.setLayout(new BorderLayout());

                        JPanel panel = new JPanel(new GridLayout(autresJoueurs.size() + 2, 1));
                        JLabel label = new JLabel("Sélectionnez les joueurs à transporter:");
                        panel.add(label);

                        ArrayList<JCheckBox> checkboxes = new ArrayList<>();
                        for (JoueurModele j : autresJoueurs) {
                            JCheckBox cb = new JCheckBox("Joueur " + (Arrays.asList(joueurs).indexOf(j) + 1));
                            cb.setForeground(j.getCouleur());
                            checkboxes.add(cb);
                            panel.add(cb);
                        }

                        JButton validerBtn = new JButton("Valider");
                        validerBtn.addActionListener(ev -> {
                            ArrayList<JoueurModele> joueursATransporter = new ArrayList<>();
                            for (int i = 0; i < checkboxes.size(); i++) {
                                if (checkboxes.get(i).isSelected()) {
                                    joueursATransporter.add(autresJoueurs.get(i));
                                }
                            }

                            boolean reussi = joueursControleurs[ileControleur.getJoueurActuel()].utiliserHelicoptere(X, Y, joueursATransporter);
                            if (reussi) {
                                updateInfoLabels("Déplacement en hélicoptère réussi", Color.GREEN);
                                utiliserHelicoptere = false;
                            } else {
                                updateInfoLabels("Destination non valide", Color.RED);
                            }
                            transportDialog.dispose();
                            repaint();
                        });

                        panel.add(validerBtn);
                        transportDialog.add(panel, BorderLayout.CENTER);
                        transportDialog.pack();
                        transportDialog.setLocationRelativeTo(frame);
                        transportDialog.setVisible(true);
                    } else {
                        // Pas d'autres joueurs à transporter
                        boolean reussi = joueursControleurs[ileControleur.getJoueurActuel()].utiliserHelicoptere(X, Y, new ArrayList<>());
                        if (reussi) {
                            updateInfoLabels("Déplacement en hélicoptère réussi", Color.GREEN);
                            utiliserHelicoptere = false;
                            joueursControleurs[ileControleur.getJoueurActuel()].getJoueur().getActionsSpeciales().remove(ActionSpeciale.HELICOPTERE);
                        } else {
                            updateInfoLabels("Destination non valide", Color.RED);
                        }
                        repaint();
                    }
                }
            }
        });

        //Gestion des touches
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean mouvementReussi = false;
                JoueurControleur controleur = joueursControleurs[ileControleur.getJoueurActuel()];

                if (controleur.getJoueur().getNbActions() <= 0) {
                    PlusActions = false;
                    updateInfoLabels("Vous n'avez plus d'actions !", Color.RED);
                    repaint();
                    requestFocusInWindow();
                    return;
                }
                if (Fuir) {
                    updateInfoLabels("Cliquez pour fuir !", Color.GREEN);
                    repaint();
                    requestFocusInWindow();
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        mouvementReussi = controleur.deplacer(Direction.HAUT);
                        break;
                    case KeyEvent.VK_DOWN:
                        mouvementReussi = controleur.deplacer(Direction.BAS);
                        break;
                    case KeyEvent.VK_LEFT:
                        mouvementReussi = controleur.deplacer(Direction.GAUCHE);
                        break;
                    case KeyEvent.VK_RIGHT:
                        mouvementReussi = controleur.deplacer(Direction.DROITE);
                        break;
                }
                if (!mouvementReussi) {
                    updateInfoLabels("Mouvement incorrect !", Color.RED);
                } else {
                    updateInfoLabels("", Color.GREEN);
                }

                repaint();
            }
        });
        actionSpeciale.setBackground(new Color(220, 220, 255));
        chercherCle.setBackground(new Color(220, 220, 255));
        AS.setBackground(new Color(220, 220, 255));
        FDT.setBackground(new Color(255, 220, 220));
        echangerCle.setBackground(new Color(220, 220, 255));
        ramasserBtn.setBackground(new Color(220, 220, 255));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttonPanel.add(AS);
        buttonPanel.add(ramasserBtn);
        buttonPanel.add(echangerCle);
        buttonPanel.add(chercherCle);
        buttonPanel.add(actionSpeciale);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(CouchesEcran, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.EAST);

        frame.add(mainPanel, BorderLayout.CENTER);

        frame.add(FDT, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.WEST);

        frame.setVisible(true);
        this.requestFocusInWindow();

        //Code pour tester le ramassage d'artefact
        /*joueurs[0].ajouterArtefact(TypeObjets.EAU);
        joueurs[0].ajouterCle(TypeObjets.EAU);
        joueurs[0].ajouterCle(TypeObjets.EAU);
        joueurs[0].ajouterCle(TypeObjets.EAU);
        joueurs[0].setX(0, modele);
        joueurs[0].setY(0, modele);
        joueurs[0].ajouterCle(TypeObjets.EAU);
        joueurs[0].ajouterCle(TypeObjets.FEU);
        joueurs[1].ajouterArtefact(TypeObjets.EAU);
        joueurs[2].ajouterArtefact(TypeObjets.VENT);
        updateInfoLabels("Test ramasser objet", Color.BLACK);*/

        //Code pour action spétiale hélicoptère
        /*joueurs[0].ajouterActionSpeciale(ActionSpeciale.HELICOPTERE);
        joueurs[0].setX(IleModele.getTailleIle()/2, modele);
        joueurs[0].setY(IleModele.getTailleIle()/2, modele);
        joueurs[1].setX(IleModele.getTailleIle()/2, modele);
        joueurs[1].setY(IleModele.getTailleIle()/2, modele);
        updateInfoLabels("Test hélicoptère", Color.BLACK);*/

        //Code pour action spétiale Sac de Sable
        /*joueurs[0].ajouterActionSpeciale(ActionSpeciale.SAC_DE_SABLE);
        modele.getTab()[0][1].setSituationZone(SituationZone.INONDEE);
        updateInfoLabels("Test Sac de Sable", Color.BLACK);*/

        //Code pour gagner
        /*joueurs[0].ajouterArtefact(TypeObjets.EAU);
        joueurs[1].ajouterArtefact(TypeObjets.FEU);
        joueurs[2].ajouterArtefact(TypeObjets.TERRE);
        joueurs[0].ajouterArtefact(TypeObjets.VENT);
        joueurs[0].setX(IleModele.getTailleIle()/2, modele);
        joueurs[0].setY(IleModele.getTailleIle()/2, modele);
        joueurs[1].setX(IleModele.getTailleIle()/2, modele);
        joueurs[1].setY(IleModele.getTailleIle()/2, modele);
        joueurs[2].setX(IleModele.getTailleIle()/2, modele);
        joueurs[2].setY(IleModele.getTailleIle()/2, modele);
        updateInfoLabels("Test situation gagner", Color.BLACK);*/

        //Code pour échange de clés
        /*joueurs[0].ajouterCle(TypeObjets.EAU);
        joueurs[0].ajouterCle(TypeObjets.FEU);
        joueurs[0].setX(IleModele.getTailleIle()/2, modele);
        joueurs[0].setY(IleModele.getTailleIle()/2, modele);
        joueurs[1].setX(IleModele.getTailleIle()/2, modele);
        joueurs[1].setY(IleModele.getTailleIle()/2, modele);
        updateInfoLabels("Test échange de clés", Color.BLACK);*/
    }

    private void faireActionSpetiale(ActionSpeciale action) {
        switch (action) {
            case SAC_DE_SABLE:
                utiliserSacDeSable = true;
                utiliserHelicoptere = false;
                Assecher = false;
                updateInfoLabels("Cliquez sur une zone à assécher", Color.YELLOW);
                break;
            case HELICOPTERE:
                utiliserHelicoptere = true;
                utiliserSacDeSable = false;
                Assecher = false;
                updateInfoLabels("Cliquez sur une destination", Color.YELLOW);
                break;
        }
        repaint();
        requestFocusInWindow();
    }

    private void updateInfoLabels(String S, Color C) {
        JoueurModele joueurCourant = joueurs[ileControleur.getJoueurActuel()];
        labelJoueur.setText("Joueur actuel: " + (ileControleur.getJoueurActuel() + 1));
        labelActions.setText("Actions restantes: " + joueurCourant.getNbActions());
        labelMessage.setText(S);
        labelMessage.setForeground(C);

        for (JoueurInfoPanel jip : joueurInfoPanels) {
            jip.updateInfo();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < IleModele.getTailleIle(); i++) {
            for (int j = 0; j < IleModele.getTailleIle(); j++) {
                Zone zone = modele.getTab()[i][j];
                Image img = getZoneImage(zone);
                g.drawImage(img, i * TAILLE_CASE, j * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, this);
            }
        }
        // Dessin des zones qu'on peut Assécher
        if (Assecher) {
            JoueurModele joueurCourant = joueurs[ileControleur.getJoueurActuel()];
            // Cases inondées
            for (Zone z : joueurCourant.getAssechables(modele)) {
                g.setColor(Color.yellow);
                g.fillRect(z.getX() * TAILLE_CASE, z.getY() * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
            }
            // Redessiner les bordures pour qu'elles restent visibles
            g.setColor(Color.black);
            for (Zone z : joueurCourant.getVoisins(modele)) {
                if (z != null) {
                    g.drawRect(z.getX() * TAILLE_CASE, z.getY() * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
                }
            }
            g.drawRect(joueurCourant.getX() * TAILLE_CASE,
                    joueurCourant.getY() * TAILLE_CASE,
                    TAILLE_CASE, TAILLE_CASE);
        }
        if (Fuir) {
            g.setColor(new Color(255, 200, 0));
            for (JoueurModele j : joueurs) {
                if (modele.getTab()[j.getX()][j.getY()].getSituationZone() == SituationZone.SUBMERGE) {
                    for (Zone z : j.getVoisinsNonSubmerges(modele)) {
                        g.fillRect(z.getX() * TAILLE_CASE, z.getY() * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
                    }
                }
            }
        }
        if (utiliserSacDeSable) {
            for (int x = 0; x < modele.getTab().length; x++) {
                for (int y = 0; y < modele.getTab()[x].length; y++) {
                    Zone z = modele.getTab()[x][y];
                    if (z == null) continue;

                    if (z.getSituationZone() == SituationZone.INONDEE) {
                        g.setColor(new Color(255, 255, 0)); // Jaune
                        g.fillRect(z.getX() * TAILLE_CASE, z.getY() * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
                        g.setColor(Color.black);
                        g.drawRect(z.getX() * TAILLE_CASE, z.getY() * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
                    }
                }
            }
        }
    }

    private Image getZoneImage(Zone zone) {
        switch (zone.getTypeZone()) {
            case HELIPORT:
                if (zone.getSituationZone() == SituationZone.SUBMERGE) { return ImageManager.getImage("heliportS"); }
                else if (zone.getSituationZone() == SituationZone.INONDEE) { return ImageManager.getImage("heliportI"); }
                else { return ImageManager.getImage("heliportNI"); }
            case EAU:
                if (zone.getSituationZone() == SituationZone.SUBMERGE) { return ImageManager.getImage("templeEauS"); }
                else if (zone.getSituationZone() == SituationZone.INONDEE) { return ImageManager.getImage("templeEauI"); }
                else { return ImageManager.getImage("templeEauNI"); }
            case FEU:
                if (zone.getSituationZone() == SituationZone.SUBMERGE) { return ImageManager.getImage("templeFeuS"); }
                else if (zone.getSituationZone() == SituationZone.INONDEE) { return ImageManager.getImage("templeFeuI"); }
                else { return ImageManager.getImage("templeFeuNI"); }
            case TERRE:
                if (zone.getSituationZone() == SituationZone.SUBMERGE) { return ImageManager.getImage("templeTerreS"); }
                else if (zone.getSituationZone() == SituationZone.INONDEE) { return ImageManager.getImage("templeTerreI"); }
                else { return ImageManager.getImage("templeTerreNI"); }
            case VENT:
                if (zone.getSituationZone() == SituationZone.SUBMERGE) { return ImageManager.getImage("templeAirS"); }
                else if (zone.getSituationZone() == SituationZone.INONDEE) { return ImageManager.getImage("templeAirI"); }
                else { return ImageManager.getImage("templeAirNI"); }
            default:
                if (zone.getSituationZone() == SituationZone.SUBMERGE) { return ImageManager.getImage("solS"); }
                else if (zone.getSituationZone() == SituationZone.INONDEE) { return ImageManager.getImage("solI"); }
                else { return ImageManager.getImage("solNI"); }
        }
    }
}