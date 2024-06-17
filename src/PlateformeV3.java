import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

public class PlateformeV3 extends Plateforme {

    // TODO
    public void ajouterVillesEtTrajets(MultiGrapheOrienteValue graphe, TypeCout[] couts, ArrayList<ModaliteTransport> transports) {

    }

    //TODO
    public String afficherPCC(List<Chemin> result, TypeCout cout) throws NoTripException {
        if (result.isEmpty()) throw new NoTripException("Aucun voyage correspondant.");
        String resultat = "";
        if (result.size() == 1) resultat = "Le trajet optimal basé sur vos préférences est : \n";
        else if (result.size() > 1) resultat = "Les trajets optimaux basés sur vos préférences sont : \n";
        for (int idx=0; idx<result.size(); idx++) {
            RouteV3 r = new RouteV3(result.get(idx));
            resultat = resultat + r.toString() +"\n";
        }
        return resultat;
    }

    
}
