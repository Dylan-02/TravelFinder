import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class VoyageurV3 extends Voyageur {
    private TypeCout[] preferencesCouts;
    private ArrayList<Route> historique;

    public VoyageurV3(String nom, double coutMax, ArrayList<ModaliteTransport> transportFavori) {
        super(nom, null, coutMax, transportFavori);
        this.historique = new ArrayList<>();
        this.preferencesCouts = TypeCout.values();
    }

    public VoyageurV3(String nom, double coutMax, ArrayList<ModaliteTransport> transportFavori, ArrayList<Route> historique, TypeCout[] preferencesCouts) {
        super(nom, null, coutMax, transportFavori);
        this.historique = historique;
        if (preferencesCouts.length < TypeCout.values().length-1) this.preferencesCouts = preferencesCouts;
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
        if (index < this.preferencesCouts.length-1 && index >= 0) {
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
