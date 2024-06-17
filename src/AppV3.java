import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

public class AppV3 {
    public static void main(String[] args) {
        ArrayList<ModaliteTransport> transports = new ArrayList<>();
        transports.add(ModaliteTransport.TRAIN);
        transports.add(ModaliteTransport.AVION);
        TypeCout[] coutsPref = new TypeCout[]{TypeCout.PRIX, TypeCout.TEMPS, TypeCout.CO2};
        VoyageurV3 voyageur = VoyageurV3.loadVoyageur("./saves/userSave");
        if (voyageur == null) voyageur = new VoyageurV3("Dupont Lisa", 200, transports);
        voyageur.setHistorique(new ArrayList<>());
        voyageur.setPreferencesCouts(coutsPref);
        PlateformeV3 plateforme = new PlateformeV3();
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        String trajets = "./src/trajets.csv";
        String couts = "./src/couts.csv";
        try {
            String[] data = plateforme.getDataFromCSV(trajets);
            plateforme.verifiyData(data);
            for (int idx=0; idx<data.length; idx++) {
                String[] tab = data[idx].split(";");
                plateforme.retrieveData(tab);
            }
            String[] correspondances = plateforme.getDataFromCSV(couts);
            plateforme.verifiyData(correspondances);
            plateforme.ajouterCorrespondances(correspondances);
            plateforme.ajouterVillesEtTrajets(graphe, voyageur.getTypeCoutPref(), voyageur.getTransportFavori());
            List<Chemin> result = AlgorithmeKPCC.kpcc(graphe, new Ville("villeA"), new Ville("villeD"), 3);
            result = voyageur.verifierBornes(result);
            System.out.println(plateforme.afficherPCC(result, voyageur.getTypeCoutPref()));
            voyageur.addToHistorique(new RouteV3(result.get(0)));
            System.out.println("Historique de "+voyageur.getNom()+":\n"+voyageur.getHistorique());
            System.out.println(voyageur.getStatistiques());
            voyageur.saveVoyageur();
        }
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (IllegalArgumentException e) {System.err.println("Argument invalide");}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        catch (NoTripException e) {System.err.println(e.getMessage());}
    }
}
