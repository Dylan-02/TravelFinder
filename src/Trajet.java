import java.util.HashMap;

import fr.ulille.but.sae_s2_2024.*;

/**
 * La classe Trajet représente un trajet entre deux villes.
 * Elle implémente l'interface Trancon.
 */
public class Trajet implements Trancon {
    private Ville depart;
    private Ville arrivee;
    private ModaliteTransport modalite;
    private HashMap<TypeCout, Double> couts = new HashMap<TypeCout, Double>();

    /**
     * Constructeur de la classe Trajet.
     * @param depart    la ville de départ du trajet
     * @param arrivee   la ville d'arrivée du trajet
     * @param modalite  la modalité de transport du trajet
     * @param couts     les coûts associés au trajet
     */
    public Trajet(Ville depart, Ville arrivee, ModaliteTransport modalite, HashMap<TypeCout, Double> couts) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.modalite = modalite;
        this.couts = couts;
    }

    /**
     * Retourne la modalité de transport du trajet.
     * @return la modalité de transport
     */
    public ModaliteTransport getModalite() {
        return this.modalite;
    }

    /**
     * Retourne la ville de départ du trajet. 
     * @return la ville de départ
     */
    public Ville getDepart() {
        return this.depart;
    }

    /**
     * Retourne la ville d'arrivée du trajet.
     * @return la ville d'arrivée
     */
    public Ville getArrivee() {
        return this.arrivee;
    }

    public HashMap<TypeCout, Double> getCouts() {
        return this.couts;
    }

    /**
     * Retourne le coût associé au type spécifié.
     * @param type  le type de coût
     * @return le coût associé au type spécifié
     */
    public double getCout(TypeCout type) {
        return couts.get(type);
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du trajet.
     * @return une représentation du trajet
     */
    public String toString() {
        return this.depart + " vers " + this.arrivee + " en " + this.getModalite() + couts.toString();
    }
}
