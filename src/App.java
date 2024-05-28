import java.io.FileNotFoundException;
import java.util.ArrayList;
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
        ArrayList<ModaliteTransport> transports = new ArrayList<>();
        transports.add(ModaliteTransport.TRAIN);
        transports.add(ModaliteTransport.AVION);
        Voyageur voyageur = new Voyageur("Lisa", TypeCout.PRIX, 200, transports);
        Plateforme plateforme = new Plateforme();
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        String file = "./src/data.csv";
        try {
            String[] data = plateforme.getDataFromCSV(file);
            plateforme.verifiyData(data);
            for (int idx=0; idx<data.length; idx++) {
                String[] tab = data[idx].split(";");
                plateforme.retrieveData(tab);
            }
            plateforme.ajouterVillesEtTrajets(graphe, voyageur.getTypeCoutPref(), voyageur.getTransportFavori());
            List<Chemin> result = AlgorithmeKPCC.kpcc(graphe, new Ville("villeA"), new Ville("villeD"), 3);
            result = voyageur.verifierBornes(result);
            System.out.println(plateforme.afficherPCC(result, voyageur.getTypeCoutPref()));
        }
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        catch (NoTripException e) {System.err.println(e.getMessage());}
    }
}