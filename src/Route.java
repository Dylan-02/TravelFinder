import fr.ulille.but.sae_s2_2024.*;

import java.io.Serializable;
import java.util.List;

/**
 * La classe Route représente un chemin composé de plusieurs trançons.
 * Elle implémente l'interface Chemin.
 */
public class Route implements Chemin, Serializable {
    private double poids;
    private List<Trancon> aretes;
    private Lieu depart;
    private Lieu arrivee;

    /**
     * Constructeur de la classe Route.
     * @param aretes  la liste des trançons composant la route
     * @param depart  le lieu de départ de la route
     * @param arrivee le lieu d'arrivée de la route
     * @param poids   le poids de la route
     */
    public Route(List<Trancon> aretes, Lieu depart, Lieu arrivee, int poids) {
        this.poids = poids;
        this.aretes = aretes;
        this.depart = depart;
        this.arrivee = arrivee;
    }

    /**
     * Constructeur de la classe Route prenant en paramètre un objet Chemin.
     * @param chemin l'objet Chemin à partir duquel construire la route
     */
    public Route(Chemin chemin) {
        this.poids = chemin.poids();
        this.aretes = chemin.aretes();
        this.depart = chemin.aretes().get(0).getDepart();
        this.arrivee = chemin.aretes().get(chemin.aretes().size() - 1).getArrivee();
    }

    /**
     * Retourne le poids de la route. 
     * @return le poids de la route
     */
    public double poids() {
        return this.poids;
    }

    /**
     * Retourne la liste des trançons composant la route.
     * @return la liste des trançons composant la route
     */
    public List<Trancon> aretes() {
        return this.aretes;
    }

    /**
     * Retourne le lieu de départ de la route.
     * @return le lieu de départ de la route
     */
    public Lieu getDepart() {
        return this.depart;
    }

    /**
     * Retourne le lieu d'arrivée de la route.
     * @return le lieu d'arrivée de la route
     */
    public Lieu getArrivee() {
        return this.arrivee;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la route. 
     * @return une représentation sous forme de chaîne de caractères de la route
     */
    @Override
    public String toString() {
        String res = "";
        if (!aretes.isEmpty()) {
            if (this.memeModalite()) {
                res = getTrajet(aretes);
            } else {
                int idxChangement = idxChangement(aretes);
                res = getTrajet(aretes.subList(0, idxChangement)) + " puis "
                        + getTrajet(aretes.subList(idxChangement, aretes.size()));
            }
            res = res + " avec " + (int) this.poids();
        }
        return res;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du trajet.
     * @param trajet le trajet à représenter
     * @return une représentation sous forme de chaîne de caractères du trajet
     */
    public String getTrajet(List<Trancon> trajet) {
        String res = "";
        res = res + trajet.get(0).getModalite() + " de " + trajet.get(0).getDepart() + " à "
                + trajet.get(trajet.size() - 1).getArrivee();
        return res;
    }

    /**
     * Vérifie si tous les trançons de la route ont la même modalité.
     * @return true si tous les trançons ont la même modalité, false sinon
     */
    public boolean memeModalite() {
        boolean memeModalite = true;
        int idx = 1;
        if (this.aretes.size() > 1) {
            while (idx < this.aretes().size() && memeModalite) {
                Trancon precedente = this.aretes().get(idx-1);
                Trancon actuelle = this.aretes().get(idx);
                if (!precedente.getModalite().equals(actuelle.getModalite())) {
                    memeModalite = false;
                }
                idx++;
            }
        }
        return memeModalite;
    }
    
    /**
     * Retourne l'indice du premier trançon où il y a un changement de modalité.
     * @param trajet le trajet à analyser
     * @return l'indice du premier trançon où il y a un changement de modalité, -1 si aucun changement
     */
    public int idxChangement(List<Trancon> trajet) {
        int idxChangement = -1;
        for (int idx=1; idx < trajet.size(); idx++) {
                if (!trajet.get(idx).getModalite().equals(trajet.get(idx-1).getModalite())) {
                    idxChangement = idx;
                }
            }
        return idxChangement;
    }
}
