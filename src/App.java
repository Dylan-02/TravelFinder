import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

/** Classe principale du projet
 * @author <a href=mailto:dylan.lecocq.etu@univ-lille.fr>Dylan L</a>
 * @author <a href=mailto:amaury.vanhoutte.etu@univ-lille.fr>Amaury V</a>
 * @version 1.0
 */

public class App {
    public static void main(String[] args) {
        Voyageur voyageur = new Voyageur("Lisa", TypeCout.PRIX, 100, ModaliteTransport.TRAIN);
        String[] data = new String[]{"villeA;villeB;Train;60;1.7;80",
                                     "villeB;villeD;Train;22;2.4;40",
                                     "villeA;villeC;Train;42;1.4;50",
                                     "villeB;villeC;Train;14;1.4;60",
                                     "villeC;villeD;Avion;110;150;22",
                                     "villeC;villeD;Train;65;1.2;90"};
        Plateforme plateforme = new Plateforme();
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        if (verifiyData(data)) {
            for (int idx=0; idx<data.length; idx++) {
                String[] tab = data[idx].split(";");
                retrieveData(tab, plateforme);
            }
            ajouterVillesEtTrajets(graphe, plateforme, voyageur.getTypeCoutPref(), voyageur.getTransportFavori());
            List<Chemin> result = AlgorithmeKPCC.kpcc(graphe, new Ville("villeA"), new Ville("villeD"), 3);
            result = verifierBornes(result, voyageur.getCoutMax());
            System.out.println(afficherPCC(result, voyageur.getTypeCoutPref()));
        } else {
            System.out.println("Données invalides");
        }
    }

    /** Permet de vérifier si la chaîne de caractère passé en paramètre est numérique.
     * @param str Chaine de caractère à tester
     * @return Retourne vrai si la chaîne de caractère représente un nombre, false sinon
     */
    public static boolean isNumeric(String str) { 
        try {  
            Double.parseDouble(str);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }

    /** Permet de vérifier l'intégrité des données, c'est à dire si l'ensemble des champs attendus sont fournis et s'ils sont du bon format
     * @param data Tablleau de chaîne de caractère regroupant l'ensemble des données, chaque case du tableau doit être au format "villeDépart;villeArrivée;modalitéTransport;prix(e);pollution(kgCO2 e);durée(min)"
     * @return Retourne true si les données sont valides, false sinon
     */
    public static boolean verifiyData(String[] data) {
        boolean result = false;
        if (data.length > 0) {
            result = true;
            int idx=0;
            while(idx<data.length && result) {
                String[] res = data[idx].split(";");
                if(res.length != 6) result = false;
                for (int i = 3; i < res.length; i++) {
                    if (!isNumeric(res[i])) result = false;
                }
                idx++;
            }
        }
        return result;
    }

    /** Permet d'ajouter les différentes villes et trajets du tableau de chaine de caractère passé en paramètre à la plateforme
     * @param tab Tableau de chaîne de caractère représentant l'ensemble des données, doit impérativement être au format "villeDépart;villeArrivée;modalitéTransport;prix(e);pollution(kgCO2 e);durée(min)"
     * @param plateforme Plateforme à laquelle les différentes villes et trajets vont être ajoutés
     */
    public static void retrieveData(String[] tab, Plateforme plateforme) {
        HashMap<TypeCout, Double> couts = new HashMap<>();
        couts.put(TypeCout.PRIX, Double.parseDouble(tab[3]));
        couts.put(TypeCout.CO2, Double.parseDouble(tab[4]));
        couts.put(TypeCout.TEMPS, Double.parseDouble(tab[5]));
        plateforme.getTrajets().add(new Trajet(new Ville(tab[0]), new Ville(tab[1]), ModaliteTransport.valueOf(tab[2].toUpperCase()), couts));
        plateforme.getTrajets().add(new Trajet(new Ville(tab[1]), new Ville(tab[0]), ModaliteTransport.valueOf(tab[2].toUpperCase()), couts));
        if (!plateforme.getVilles().contains(new Ville(tab[0]))) plateforme.getVilles().add(new Ville(tab[0]));
        if (!plateforme.getVilles().contains(new Ville(tab[1]))) plateforme.getVilles().add(new Ville(tab[1]));
    }

    /** Permet d'ajouter les différentes villes et trajets de la plateforme dans le graphe
     * Cette fonction ne choisit que les trajets ayant la modalité de transport définie et n'ajoute que les couts prioritaires définis par l'utilisateur
     * @param graphe Graphe dans lequel les villes et les trajets vont être ajoutés
     * @param plateforme Plateforme regroupant l'ensemble des villes et des trajets
     * @param cout Coût prioritaire défini par l'utilisateur
     */
    public static void ajouterVillesEtTrajets(MultiGrapheOrienteValue graphe, Plateforme plateforme, TypeCout cout, ModaliteTransport transport) {
        for (Ville ville : plateforme.getVilles()) {
            graphe.ajouterSommet(ville);
        }
        for (Trajet trajet : plateforme.getTrajets()) {
            if (trajet.getModalite() == transport) {
                graphe.ajouterArete(trajet, trajet.getCouts().get(cout));
            }
        }
    }
    /** Permet de vérifier si les trajets passés en paramètre ne dépassent pas le cout maximal
     * 
     * @param trajets Représente un ensemble de trajets
     * @param coutMax Cout maximal que les trajets ne doivent pas dépasser
     * @return Une liste ne contenant que les trajets ayant un coût inférieur ou égal au coût maximal.
     */
    public static List<Chemin> verifierBornes(List<Chemin> trajets, double coutMax) {
        List<Chemin> resultat = new ArrayList<>();
        if (trajets.size() > 0) {
            for (int idx = 0; idx<trajets.size(); idx++) {
                Route trajet = new Route(trajets.get(idx));
                if (trajet.poids()<=coutMax) resultat.add(trajet);
            }
        }
        return resultat;
    }

    /** Permet d'afficher la liste des plus courts chemins passée en paramètre, en modifiant le résultat en fonction du type de coût du chemin.
     * @param result Liste des plus courts chemins dans le graphe
     * @param cout Type de cout du chemin
     * @return Retourne sous forme de chaine de caractère la liste des plus courts chemins du graphe en fonction du type de coût choisi.
     */
    public static String afficherPCC(List<Chemin> result, TypeCout cout) {
        String resultat = "Il n'y a aucun trajet correspondant.";
        if (result.size() == 1) resultat = "Le trajet optimal basé sur le facteur "+cout.name()+" est : \n";
        else resultat = "Les trajets optimaux basés sur le facteur "+cout.name()+" sont : \n";
        for (int idx=0; idx<result.size(); idx++) {
            Route r = new Route(result.get(idx));
            resultat = resultat + r.toString() + switch (cout) {
                case CO2 -> "kg CO2e.\n";
                case PRIX -> "€.\n";
                case TEMPS -> " minutes.\n";
                default -> ".\n";
            };
        }
        return resultat;
    }
}