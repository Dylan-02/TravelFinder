import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class VoyageurV3 extends Voyageur {
    private TypeCout[] preferencesCouts;
    private ArrayList<Route> historique;

    public VoyageurV3(String nom, double coutMax, ArrayList<ModaliteTransport> transportFavori) {
        this(nom, coutMax, transportFavori, new ArrayList<>(), TypeCout.values());
    }

    public VoyageurV3(String nom, double coutMax, ArrayList<ModaliteTransport> transportFavori, TypeCout[] preferencesCouts) {
        this(nom, coutMax, transportFavori, new ArrayList<>(), preferencesCouts);
    }

    public VoyageurV3(String nom, double coutMax, ArrayList<ModaliteTransport> transportFavori, ArrayList<Route> historique, TypeCout[] preferencesCouts) {
        super(nom, null, coutMax, transportFavori);
        this.historique = historique;
        if (preferencesCouts.length == TypeCout.values().length) this.preferencesCouts = preferencesCouts;
        else this.preferencesCouts = TypeCout.values();
    }

    public TypeCout[] getPreferencesCouts() {
        return this.preferencesCouts;
    }

    public ArrayList<Route> getHistorique() {
        return this.historique;
    }

    public void setHistorique(ArrayList<Route> newHistorique) {
        this.historique = newHistorique;
    }

    public void setPreferencesCouts(TypeCout[] newPreferences) {
        this.preferencesCouts = newPreferences;
    }

    public void changePreference(TypeCout cout, int index) {
        if (index < this.preferencesCouts.length && index >= 0) {
            int positionCout = this.getPreferencePosition(cout);
            TypeCout tmp = this.preferencesCouts[index];
            this.preferencesCouts[index] = cout;
            this.preferencesCouts[positionCout] = tmp;
        }
    }

    public int getPreferencePosition(TypeCout cout) {
        int result = -1;
        for (int index = 0; index < this.preferencesCouts.length; index++) {
            if (this.preferencesCouts[index].equals(cout)) result = index;
        }
        return result;
    }

    public TypeCout getTypeCoutPref() {
        return this.preferencesCouts[0];
    }

    public void addToHistorique(Route newRoute) {
        if (newRoute != null) this.historique.add(newRoute);
    }

    public double getTotalParCoutHistorique(TypeCout cout) {
        double total = 0;
        for (int idx=0; idx < this.historique.size(); idx++) {
            RouteV3 route = (RouteV3)this.historique.get(idx);
            total += route.getCoutTotal(cout);
        }
        return total;
    }

    public String getStatistiques() {
        String res = "Vous n'avez pas encore de voyage enregistré.";
        if (this.historique.size() > 0) {
            DecimalFormat df = new DecimalFormat("#.##");
            double totalTemps = this.getTotalParCoutHistorique(TypeCout.TEMPS);
            double moyenneTemps = totalTemps/this.historique.size();
            double totalPrix = this.getTotalParCoutHistorique(TypeCout.PRIX);
            double moyennePrix = totalPrix/this.historique.size();
            double totalPollution = this.getTotalParCoutHistorique(TypeCout.CO2);
            double moyennePollution = totalPollution/this.historique.size();
            res = "Au total, vous avez :\n"+
                "Voyagé "+this.historique.size()+" fois.\n"+
                "Passé "+totalTemps+" minutes dans les transports, avec en moyenne "+moyenneTemps+" minutes par voyage.\n"+
                "Dépensé "+totalPrix+"€ pour vos voyages, avec en moyenne "+moyennePrix+"€ par voyage\n"+
                "Emis "+df.format(totalPollution)+"kg CO2e, avec en moyenne "+df.format(moyennePollution)+"kg CO2 émis par voyage";
        }

        return res;
    }

    public void saveVoyageur() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("./src/saves/userSave")))) {
            oos.writeObject(this);
        } catch(Exception e) {e.getMessage();}
    }

    public static VoyageurV3 loadVoyageur(String file) {
        VoyageurV3 voyageur = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("./src/saves/userSave")))) {
            voyageur = (VoyageurV3) ois.readObject();
        } catch(Exception e) {e.getMessage();}
        return voyageur;
    }
}
