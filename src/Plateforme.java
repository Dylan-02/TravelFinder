import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

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

    /** Permet d'ajouter les différentes villes et trajets de la plateforme dans le graphe
     * Cette fonction ne choisit que les trajets ayant la modalité de transport définie et n'ajoute que les couts prioritaires définis par l'utilisateur
     * @param graphe Graphe dans lequel les villes et les trajets vont être ajoutés
     * @param plateforme Plateforme regroupant l'ensemble des villes et des trajets
     * @param cout Coût prioritaire défini par l'utilisateur
     */
    public void retrieveData(String[] tab) {
        HashMap<TypeCout, Double> couts = new HashMap<>();
        couts.put(TypeCout.PRIX, Double.parseDouble(tab[3]));
        couts.put(TypeCout.CO2, Double.parseDouble(tab[4]));
        couts.put(TypeCout.TEMPS, Double.parseDouble(tab[5]));
        this.getTrajets().add(new Trajet(new Ville(tab[0]), new Ville(tab[1]), ModaliteTransport.valueOf(tab[2].toUpperCase()), couts));
        this.getTrajets().add(new Trajet(new Ville(tab[1]), new Ville(tab[0]), ModaliteTransport.valueOf(tab[2].toUpperCase()), couts));
        if (!this.getVilles().contains(new Ville(tab[0]))) this.getVilles().add(new Ville(tab[0]));
        if (!this.getVilles().contains(new Ville(tab[1]))) this.getVilles().add(new Ville(tab[1]));
    }

    /** Permet d'ajouter les différentes villes et trajets du tableau de chaine de caractère passé en paramètre à la plateforme
     * @param tab Tableau de chaîne de caractère représentant l'ensemble des données, doit impérativement être au format "villeDépart;villeArrivée;modalitéTransport;prix(e);pollution(kgCO2 e);durée(min)"
     * @param plateforme Plateforme à laquelle les différentes villes et trajets vont être ajoutés
     */
    public void ajouterVillesEtTrajets(MultiGrapheOrienteValue graphe, TypeCout cout, ArrayList<ModaliteTransport> transports) {
        for (Ville ville : this.getVilles()) {
            graphe.ajouterSommet(ville);
        }
        for (Trajet trajet : this.getTrajets()) {
            if (transports.contains(trajet.getModalite())) {
                graphe.ajouterArete(trajet, trajet.getCouts().get(cout));
            }
        }
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
