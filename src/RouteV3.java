import java.text.DecimalFormat;
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

    public void setPoids(double newPoids) {
        this.poids = newPoids;
    }

    public double getCoutTotal(TypeCout cout) {
        double coutTotal = 0;
        for (int idx = 0; idx < this.aretes.size(); idx++) {
            Trajet trajet = (Trajet)this.aretes.get(idx);
            coutTotal += trajet.getCout(cout);
        }
        return coutTotal;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        String res = "";
        if (!aretes.isEmpty()) {
            if (this.memeModalite()) {
                res = getTrajet(aretes);
            } else {
                int idxChangement = idxChangement(aretes);
                res = getTrajet(aretes.subList(0, idxChangement)) + " puis "
                        + getTrajet(aretes.subList(idxChangement, aretes.size()));
            }
            res += " avec "+df.format(this.getCoutTotal(TypeCout.CO2))+" kg CO2e, "+df.format(this.getCoutTotal(TypeCout.PRIX))+" € et "+df.format(this.getCoutTotal(TypeCout.TEMPS))+" minutes.";
        }
        return res;
    }

    public double getMaximumParCout(TypeCout cout) {
        double maximum = 0;
        for (int idx = 0; idx < this.aretes.size(); idx++) {
            Trajet trajet = (Trajet)this.aretes.get(idx);
            if (trajet.getCout(cout) > maximum) maximum = trajet.getCout(cout);
        }
        return maximum;
    }
}
