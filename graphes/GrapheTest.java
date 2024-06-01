import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

public class GrapheTest {
    public static void main(String[] args) {
        ArrayList<ModaliteTransport> transports = new ArrayList<>();
        transports.add(ModaliteTransport.TRAIN);
        Voyageur voyageur = new Voyageur("Lisa", TypeCout.TEMPS, 150.0, transports);
        String trajets = "./graphes/trajets.csv";
        Plateforme plateforme = new Plateforme();
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        try {
            String[] data = plateforme.getDataFromCSV(trajets);
            plateforme.verifiyData(data);
            for (int idx=0; idx<data.length; idx++) {
                String[] tab = data[idx].split(";");
                plateforme.retrieveData(tab);
            }
            plateforme.ajouterVillesEtTrajets(graphe, voyageur.getTypeCoutPref(), voyageur.getTransportFavori());
            List<Chemin> result = AlgorithmeKPCC.kpcc(graphe, new Ville("villeA"), new Ville("villeE"), 3);
            result = voyageur.verifierBornes(result);
            System.out.println(plateforme.afficherPCC(result, voyageur.getTypeCoutPref()));
                
        }
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        catch (NoTripException e) {System.err.println(e.getMessage());}
    }
}
