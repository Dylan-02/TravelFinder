import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.Trancon;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class AppV3Test {
    VoyageurV3 v1, v2, v3; 
    String[] data;
    MultiGrapheOrienteValue graphe;
    Plateforme plateforme;
    Ville a, b, c, d;
    String[] corresp;
    String file;
    String[] trajets;

    @BeforeEach
    void initialization() {
        ArrayList<ModaliteTransport> train = new ArrayList<>();
        train.add(ModaliteTransport.TRAIN);
        ArrayList<ModaliteTransport> avion = new ArrayList<>();
        avion.add(ModaliteTransport.AVION);       
        v1 = new VoyageurV3("v1", 100, train);
        v2 =  new VoyageurV3("v2", 150, train);
        v3 = new VoyageurV3("v3", 200, avion);
        data  = new String[]{"villeA;villeB;Train;60;1.7;80",
                            "villeB;villeD;Train;22;2.4;40",
                            "villeA;villeC;Train;42;1.4;50",
                            "villeB;villeC;Train;14;1.4;60",
                            "villeC;villeD;Avion;110;150;22",
                            "villeC;villeD;Train;65;1.2;90"};
        graphe = new MultiGrapheOrienteValue();
        plateforme = new Plateforme();
        a = new Ville("villeA");
        b = new Ville("villeB");
        c = new Ville("villeC");
        d = new Ville("villeD");
        corresp = new String[]{"villeC;Train;Avion;60;0.1;20"};
        file = "./src/trajets.csv";
        trajets = new String[]{"villeA;villeA-Gare;Train;0;0;0",
                            "villeA-Gare;villeB-Gare;Train;60;1.7;80",
                            "villeB-Gare;villeD-Gare;Train;22;2.4;40",
                            "villeA-Gare;villeC-Gare;Train;42;1.4;50",
                            "villeB-Gare;villeC-Gare;Train;14;1.4;60",
                            "villeC-Aeroport;villeD-Aeroport;Avion;110;150;22",
                            "villeC-Gare;villeD-Gare;Train;65;1.2;90",
                            "villeD-Gare;villeD;Train;0;0;0",
                            "villeD-Aeroport;villeD;Avion;0;0;0"};
    }

    @Test
    void testGetNom() {
        assertEquals("v1", v1.getNom());
        assertEquals("v2", v2.getNom());
        assertEquals("v3", v3.getNom());
    }

    @Test
    void testChangePreference() {
        TypeCout[] couts = TypeCout.values();
        assertTrue(Arrays.equals(couts, v1.getPreferencesCouts()));
        assertTrue(Arrays.equals(couts, v2.getPreferencesCouts()));
        assertTrue(Arrays.equals(couts, v3.getPreferencesCouts()));
        v1.changePreference(TypeCout.PRIX, 0);
        System.out.println(v1.getPreferencesCouts());
        couts = new TypeCout[]{TypeCout.PRIX, TypeCout.TEMPS, TypeCout.CO2};
        assertTrue(Arrays.equals(couts, v1.getPreferencesCouts()));
        v2.changePreference(TypeCout.TEMPS, 0);
        couts = new TypeCout[]{TypeCout.TEMPS, TypeCout.CO2, TypeCout.PRIX};
        assertTrue(Arrays.equals(couts, v2.getPreferencesCouts()));
        v3.changePreference(TypeCout.TEMPS, 1);
        v3.changePreference(TypeCout.PRIX, 0);
        couts = new TypeCout[]{TypeCout.PRIX, TypeCout.TEMPS, TypeCout.CO2};
        assertTrue(Arrays.equals(couts, v3.getPreferencesCouts()));
        v3.changePreference(TypeCout.PRIX, 15);
        assertTrue(Arrays.equals(couts, v3.getPreferencesCouts()));
        v3.changePreference(TypeCout.PRIX, -15);
        assertTrue(Arrays.equals(couts, v3.getPreferencesCouts()));
    }

    @Test
    void testGetTypeCoutPref() {
        assertEquals(TypeCout.CO2, v1.getTypeCoutPref());
        v2.changePreference(TypeCout.PRIX, 0);
        assertEquals(TypeCout.PRIX, v2.getTypeCoutPref());
        v3.changePreference(TypeCout.TEMPS, 0);
        assertEquals(TypeCout.TEMPS, v3.getTypeCoutPref());
        v1.changePreference(TypeCout.PRIX, 0);
        assertEquals(TypeCout.PRIX, v1.getTypeCoutPref());
    }

    @Test
    void testgetPreferencePosition() {
        assertEquals(0, v1.getPreferencePosition(TypeCout.CO2));
        assertEquals(1, v1.getPreferencePosition(TypeCout.TEMPS));
        assertEquals(2, v1.getPreferencePosition(TypeCout.PRIX));
        v1.changePreference(TypeCout.PRIX, 0);
        assertEquals(0, v1.getPreferencePosition(TypeCout.PRIX));
        assertEquals(1, v1.getPreferencePosition(TypeCout.TEMPS));
        assertEquals(2, v1.getPreferencePosition(TypeCout.CO2));
        v1.changePreference(TypeCout.CO2, 1);
        assertEquals(0, v1.getPreferencePosition(TypeCout.PRIX));
        assertEquals(1, v1.getPreferencePosition(TypeCout.CO2));
        assertEquals(2, v1.getPreferencePosition(TypeCout.TEMPS));
        assertEquals(-1, v1.getPreferencePosition(null));
    }

    @Test
    void testAddToHistorique() {
        assertEquals(0, v1.getHistorique().size());
        Route r = new RouteV3(new ArrayList<Trancon>(), new Ville("A"), new Ville("B"), 10);
        assertEquals(0, v1.getHistorique().size());
        v1.addToHistorique(r);
        assertEquals(1, v1.getHistorique().size());
        assertEquals(r, v1.getHistorique().get(0));
    }

    @Test
    void testVerifiyData() {
        boolean result = false;
        try {
            plateforme.verifiyData(data);
            result = true;
        } catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertTrue(result);
        result = false;
        data = new String[]{};
        try {
            plateforme.verifiyData(data);
            result = true;
        } catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertFalse(result);
        data  = new String[]{"villeA;villeB;Train;60;1.7;80",
                            "villeB;villeD;Train;22;2.4;40",
                            "villeA;villeC;Train;42;1.4;50",
                            "villeB;villeC;Train;14;1.4",
                            "villeC;villeD;Avion;110;150;22",
                            "villeC;villeD;Train;65;1.2;90"};
        try {
            plateforme.verifiyData(data);
            result = true;
        } catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertFalse(result);
    }

    @Test
    void testRetrieveData() {
        data = new String[]{"villeA;villeB;Train;60;1.7;80"};
        data = data[0].split(";");
        plateforme.retrieveData(data);
        Trajet trajet = plateforme.getTrajets().iterator().next();
        assertEquals(2, plateforme.getVilles().size());
        assertEquals(2, plateforme.getTrajets().size());
        assertTrue(plateforme.getVilles().contains(new Ville("villeA")));
        assertTrue(plateforme.getVilles().contains(new Ville("villeB")));
        assertEquals(ModaliteTransport.TRAIN, trajet.getModalite());
        assertEquals(60.0, trajet.getCout(TypeCout.PRIX));
        assertEquals(1.7, trajet.getCout(TypeCout.CO2));
        assertEquals(80.0, trajet.getCout(TypeCout.TEMPS));
    }

    @Test
    void testAjouterVillesEtTrajets() {
        for (int idx=0; idx<data.length; idx++) {
            String[] tab = data[idx].split(";");
            plateforme.retrieveData(tab);
        }
        plateforme.ajouterVillesEtTrajets(graphe, v1.getTypeCoutPref(), v1.getTransportFavori());
        HashSet<Ville> villes = new HashSet<>();
        villes.add(a);
        villes.add(b);
        villes.add(c);
        villes.add(d);
        assertEquals(villes, plateforme.getVilles());
    }

    @Test
    void testIsNumeric() {
        assertTrue(plateforme.isNumeric("5"));
        assertTrue(plateforme.isNumeric("456"));
        assertTrue(plateforme.isNumeric("-5"));
        assertTrue(plateforme.isNumeric("67.5"));
        assertFalse(plateforme.isNumeric("Test"));
        assertFalse(plateforme.isNumeric(""));
    }

    @Test
    void testAjouterCorrespondances () throws IllegalArgumentException, InvalidStructureException{
        boolean result = false;
        for (int idx=0; idx<data.length; idx++) {
            String[] tab = data[idx].split(";");
            plateforme.retrieveData(tab);
        }
        plateforme.ajouterVillesEtTrajets(graphe, v1.getTypeCoutPref(), v1.getTransportFavori());
        try {
            plateforme.ajouterCorrespondances(data);
            result = true;
        } catch (IllegalArgumentException e) {System.err.println(e.getMessage());}
        assertFalse (result);
        try {
            plateforme.ajouterCorrespondances(new String[]{});
            result = true;
        } catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertTrue(result);
        try {
            plateforme.ajouterCorrespondances(corresp);
            result = true;
        } catch (IllegalArgumentException e) {System.err.println(e.getMessage());}
        assertTrue(result);
    }

    @Test
    void testGetNbLinesFile(){
        int result= 0;
        try{
            result= plateforme.getNbLinesFile(" ");
        } 
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        assertEquals(0, result);
        try{
            result= plateforme.getNbLinesFile(file);
        } 
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        assertEquals(9, result);
    }

    @Test
    void testVerifyCSV(){
        boolean result = false;
        try{
            result = plateforme.verifyCSV(" ");;
            
        } 
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertFalse(result);
        try{
            result = plateforme.verifyCSV(file);
        } 
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertTrue(result);
    }

    @Test
    void testGetDataFromCSV(){
        String[] result = new String[]{};
        try{
            result = plateforme.getDataFromCSV(" ");
        } 
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertNotEquals(result, trajets);
        try{
            result = plateforme.getDataFromCSV(file);
        } 
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
        assertTrue(Arrays.equals(result, trajets));
    }
}