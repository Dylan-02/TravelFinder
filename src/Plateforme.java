import java.util.HashSet;

/**
 * La classe plateforme représente une plateforme contenant l'ensemble des villes et des trajets du réseau de transport
 */
public class Plateforme {
    private HashSet<Trajet> trajets;
    private HashSet<Ville> villes;

    /**
     * Construit un objet Plateforme avec l'ensemble des trajets et villes spécifiés.
     *
     * @param trajets l'ensemble des trajets
     * @param villes l'ensemble des villes
     */
    public Plateforme(HashSet<Trajet> trajets, HashSet<Ville> villes) {
        this.trajets = trajets;
        this.villes = villes;
    }

    /**
     * Construit un objet Plateforme vide.
     */
    public Plateforme() {
        this.trajets = new HashSet<>();
        this.villes = new HashSet<>();
    }

    /**
     * Renvoie l'ensemble des trajets de la Plateforme`.
     *
     * @return l'ensemble des Trajets.
     */
    public HashSet<Trajet> getTrajets() {
        return this.trajets;
    }

    /**
     * Renvoie l'ensemble des villes de la Plateforme..
     *
     * @return l'ensemble des villes.
     */
    public HashSet<Ville> getVilles() {
        return this.villes;
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de la Plateforme.
     *
     * @return une représentation sous forme de chaîne de caractères de la Plateforme
     */
    public String toString() {
        return "Villes : " + this.villes.toString() + "\n" +
               "Trajets : " + this.trajets.toString();
    }
}
