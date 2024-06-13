import java.util.List;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.Trancon;

public class RouteV3 extends Route {

    public RouteV3(Chemin chemin) {
        super(chemin);
    }
    
    public RouteV3(List<Trancon> aretes, Lieu depart, Lieu arrivee, int poids) {
        super(aretes, depart, arrivee, poids);
    }
}
