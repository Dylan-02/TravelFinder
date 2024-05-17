import java.util.List;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

public class GrapheTest {
    public static void main(String[] args) {
        Voyageur voyageur = new Voyageur("Lisa", TypeCout.PRIX, 100.0, ModaliteTransport.TRAIN);
        String[] data = new String[] {"villeA;villeB;Train;50;1.7;60",
                                    "villeA;villeC;Train;30;1.4;40",
                                    "villeB;villeC;Train;20;1.4;50",
                                    "villeB;villeD;Train;30;2.4;40",
                                    "villeC;villeE;Train;65;1.2;90",
                                    "villeD;villeE;Train;50;1.3;60",
                                    "villeA;villeE;Avion;170;300;30"};
        Plateforme plateforme = new Plateforme();
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        if (App.verifiyData(data)) {
            for (int idx=0; idx<data.length; idx++) {
                String[] tab = data[idx].split(";");
                App.retrieveData(tab, plateforme);
            }
            App.ajouterVillesEtTrajets(graphe, plateforme, voyageur.getTypeCoutPref(), voyageur.getTransportFavori());
            List<Chemin> result = AlgorithmeKPCC.kpcc(graphe, new Ville("villeA"), new Ville("villeE"), 3);
            result = App.verifierBornes(result, voyageur.getCoutMax());
            System.out.println(App.afficherPCC(result, voyageur.getTypeCoutPref()));
        } else {
            System.out.println("Données invalides");
        }
    }
}
