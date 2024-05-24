import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        ArrayList<ModaliteTransport> transports = new ArrayList<>();
        transports.add(ModaliteTransport.TRAIN);
        transports.add(ModaliteTransport.AVION);
        Voyageur voyageur = new Voyageur("Lisa", TypeCout.TEMPS, 100, transports);
        String[] data = new String[]{"villeA;villeB;Train;60;1.7;80",
                                     "villeB;villeD;Train;22;2.4;40",
                                     "villeA;villeC;Train;42;1.4;50",
                                     "villeB;villeC;Train;14;1.4;60",
                                     "villeC;villeD;Avion;110;150;22",
                                     "villeC;villeD;Train;65;1.2;90"};
        Plateforme plateforme = new Plateforme();
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        try {
            verifiyData(data);
            for (int idx=0; idx<data.length; idx++) {
                String[] tab = data[idx].split(";");
                plateforme.retrieveData(tab);
            }
            plateforme.ajouterVillesEtTrajets(graphe, voyageur.getTypeCoutPref(), voyageur.getTransportFavori());
            List<Chemin> result = AlgorithmeKPCC.kpcc(graphe, new Ville("villeA"), new Ville("villeD"), 3);
            result = verifierBornes(result, voyageur.getCoutMax());
            System.out.println(afficherPCC(result, voyageur.getTypeCoutPref()));
        }
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        catch (NoTripException e) {System.err.println(e.getMessage());}
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
    public static boolean verifiyData(String[] data) throws InvalidStructureException {
        if (data.length == 0 || data == null) throw new InvalidStructureException("Structure du fichier invalide.");
        boolean result = true;
        int idx=0;
        while(idx<data.length && result) {
            String[] res = data[idx].split(";");
            if(res.length != 6) throw new InvalidStructureException("Structure du fichier invalide.");
            for (int i = 3; i < res.length; i++) {
                if (!isNumeric(res[i])) throw new InvalidStructureException("Structure du fichier invalide.");
            }
            idx++;
        }
        return result;
    }

    public static boolean verifyCSV(String file) throws FileNotFoundException, InvalidStructureException {
        boolean result = true;
        Scanner sc = new Scanner(new File(file));
        sc.useDelimiter(";|\n");
        while (sc.hasNextLine()) {
            for (int idx = 0; idx < 3; idx++) {
                if (!sc.hasNext()) {
                    sc.close();
                    throw new InvalidStructureException("Structure du fichier invalide");
                }
            }
            for (int cpt = 0; cpt < 3; cpt++) {
                if (!sc.hasNextDouble()) {
                    sc.close();
                    throw new InvalidStructureException("Structure du fichier invalide");
                }
            }
        }
        sc.close();
        return result;
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
    public static String afficherPCC(List<Chemin> result, TypeCout cout) throws NoTripException {
        if (result.isEmpty()) throw new NoTripException("Aucun voyage correspondant.");
        String resultat = "";
        if (result.size() == 1) resultat = "Le trajet optimal basé sur le facteur "+cout.name()+" est : \n";
        else if (result.size() > 1) resultat = "Les trajets optimaux basés sur le facteur "+cout.name()+" sont : \n";
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