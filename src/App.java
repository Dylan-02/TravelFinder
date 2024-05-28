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
        String[] data = new String[]{"villeA;villeB;Train;60;1.7;80",
                                     "villeB;villeD;Train;22;2.4;40",
                                     "villeA;villeC;Train;42;1.4;50",
                                     "villeB;villeC;Train;14;1.4;60",
                                     "villeC;villeD;Avion;110;150;22",
                                     "villeC;villeD;Train;65;1.2;90"};
        Plateforme plateforme = new Plateforme();
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        try {
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
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        catch (NoTripException e) {System.err.println(e.getMessage());}
    }
}