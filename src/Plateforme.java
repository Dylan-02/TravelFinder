import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import fr.ulille.but.sae_s2_2024.Chemin;
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

    /** Permet d'ajouter les différentes villes et trajets (s'ils remplissent les critères de) de la liste passée en paramètre à la plateforme
     * @param tab Tableau de chaîne de caractère représentant l'ensemble des données, doit impérativement être au format "villeDépart;villeArrivée;modalitéTransport;prix(e);pollution(kgCO2 e);durée(min)"
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

    /** Permet d'ajouter les différentes villes et trajets de la plateforme dans le graphe
     * Cette fonction ne choisit que les trajets ayant la modalité de transport définie et n'ajoute que les couts prioritaires définis par l'utilisateur
     * @param graphe Graphe dans lequel les villes et les trajets vont être ajoutés
     * @param plateforme Plateforme regroupant l'ensemble des villes et des trajets
     * @param cout Coût prioritaire défini par l'utilisateur
     * @param transports Transports définis par l'utilisateur
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

    /** Permet d'afficher la liste des plus courts chemins passée en paramètre, en modifiant le résultat en fonction du type de coût du chemin.
     * @param result Liste des plus courts chemins dans le graphe
     * @param cout Type de cout du chemin
     * @return Retourne sous forme de chaine de caractère la liste des plus courts chemins du graphe en fonction du type de coût choisi.
     */
    public String afficherPCC(List<Chemin> result, TypeCout cout) throws NoTripException {
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

    /** Permet de vérifier si la chaîne de caractère passé en paramètre est numérique.
     * @param str Chaine de caractère à tester
     * @return Retourne vrai si la chaîne de caractère représente un nombre, false sinon
     */
    public boolean isNumeric(String str) { 
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
    public boolean verifiyData(String[] data) throws InvalidStructureException {
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

    /**
     * Permet de vérifier la validité d'un fichier passé en paramètre
     * @param file chemin du fichier CSV à lire
     * @return un booléen indiquant si le fichier est valide ou non
     * @throws FileNotFoundException si le chemin passé en paramètre ne correspond à aucun fichier
     * @throws InvalidStructureException si le fichier ne respecte pas la structuration attendue
     */
    public boolean verifyCSV(String file) throws FileNotFoundException, InvalidStructureException {
        boolean result = true;
        Scanner sc = new Scanner(new File(file));
        sc.useDelimiter(";|\n");
        while (sc.hasNextLine()) {
            for (int idx = 0; idx < 3; idx++) {
                if (!sc.hasNext()) {
                    sc.close();
                    throw new InvalidStructureException("Structure du fichier invalide");
                }
                sc.next();
            }
            for (int cpt = 0; cpt < 3; cpt++) {
                if (!sc.hasNext()) {
                    sc.close();
                    throw new InvalidStructureException("Structure du fichier invalide");
                }
                sc.next();
            }
        }
        sc.close();
        return result;
    }

    /**
     * Permet de recupérer les données d'un fichier CSV
     * @param file chemin du fichier CSV à lire
     * @return les données du fichier dans un tableau de chaines de caractères
     * @throws FileNotFoundException si le chemin passé en paramètre ne correspond à aucun fichier
     * @throws InvalidStructureException si le fichier ne respecte pas la structuration attendue
     */
    public String[] getDataFromCSV(String file) throws FileNotFoundException, InvalidStructureException {
        String[] data = new String[0];
        int nbLines = this.getNbLinesFile(file);
        data = new String[nbLines];
        if (verifyCSV(file)) {
            Scanner sc = new Scanner(new File(file));
            sc.useDelimiter("\n");
            for (int idx = 0; idx < nbLines; idx++) {
                data[idx] = sc.nextLine();
            }
            sc.close();
        }
        return data;
    }

    /**
     * Permet d'obtenir le nombre de lignes d'un fichier
     * @param path Le chemin du fichier à lire
     * @return le nombre de ligne du fichier sous forme d'entier
     * @throws FileNotFoundException si le chemin passé en paramètre ne correspond à aucun fichier
     */
    public int getNbLinesFile(String path) throws FileNotFoundException {
        int nbLines = 0;
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                nbLines++;
                sc.nextLine();
            }
            sc.close();
        }
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        return nbLines;
    }

    public void ajouterCorrespondances(String[] tab) throws InvalidStructureException, IllegalArgumentException {
        for (int idx = 0; idx < tab.length; idx++) {
            String[] corresp = tab[idx].split(";");
            String ville = corresp[0];
            ModaliteTransport modaliteArrivee = ModaliteTransport.valueOf(corresp[1].toUpperCase());
            ModaliteTransport modaliteDepart = ModaliteTransport.valueOf(corresp[2].toUpperCase());
            Ville depart = new Ville("");
            Ville arrivee = new Ville("");
            switch (modaliteArrivee) {
                case AVION : 
                    depart = new Ville(ville+"-Aeroport");
                    break;
                case TRAIN : 
                    depart = new Ville(ville+"-Gare");
                    break;
                case BUS : 
                    depart = new Ville(ville+"-Bus");
                    break;
                default : new Ville("");
            }
            switch (modaliteDepart) {
                case AVION : 
                    arrivee = new Ville(ville+"-Aeroport");
                    break;
                case TRAIN : 
                    arrivee = new Ville(ville+"-Gare");
                    break;
                case BUS : 
                    arrivee = new Ville(ville+"-Bus");
                    break;
                default : new Ville("");
            }
            double poids;
            HashMap<TypeCout, Double> couts = new HashMap<>();
            couts.put(TypeCout.PRIX, Double.parseDouble(corresp[3]));
            couts.put(TypeCout.CO2, Double.parseDouble(corresp[4]));
            couts.put(TypeCout.TEMPS, Double.parseDouble(corresp[5]));
            Trajet trajet = new Trajet(depart, arrivee, modaliteDepart, couts);
            this.trajets.add(trajet);
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
