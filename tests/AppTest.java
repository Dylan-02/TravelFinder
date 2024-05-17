import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import java.util.HashSet;

public class AppTest {
    Voyageur v1, v2, v3; 
    String[] data;
    MultiGrapheOrienteValue graphe;
    Plateforme plateforme;
    Ville a, b, c, d;

    @BeforeEach
    void initialization() {
        v1 = new Voyageur("v1", TypeCout.TEMPS, 100, ModaliteTransport.TRAIN);
        v2 =  new Voyageur("v2", TypeCout.PRIX, 150, ModaliteTransport.TRAIN);
        v3 = new Voyageur("v3", TypeCout.CO2, 200, ModaliteTransport.AVION);
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
    }

    @Test
    void testGetNom() {
        assertEquals("v1", v1.getNom());
        assertEquals("v2", v2.getNom());
        assertEquals("v3", v3.getNom());
    }

    @Test
    void testGetTypeCoutPref() {
        assertEquals(TypeCout.TEMPS, v1.getTypeCoutPref());
        assertEquals(TypeCout.PRIX, v2.getTypeCoutPref());
        assertEquals(TypeCout.CO2, v3.getTypeCoutPref());
        v3.setTypeCoutPref(TypeCout.PRIX);
        assertEquals(TypeCout.PRIX, v3.getTypeCoutPref());
    }

    @Test
    void testVerifiyData() {
        assertTrue(App.verifiyData(data));
        data = new String[]{};
        assertFalse(App.verifiyData(data));
        data  = new String[]{"villeA;villeB;Train;60;1.7;80",
                            "villeB;villeD;Train;22;2.4;40",
                            "villeA;villeC;Train;42;1.4;50",
                            "villeB;villeC;Train;14;1.4",
                            "villeC;villeD;Avion;110;150;22",
                            "villeC;villeD;Train;65;1.2;90"};
        assertFalse(App.verifiyData(data));                 
    }

    @Test
    void testRetrieveData() {
        data = new String[]{"villeA;villeB;Train;60;1.7;80"};
        data = data[0].split(";");
        App.retrieveData(data, plateforme);
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
            App.retrieveData(tab, plateforme);
        }
        App.ajouterVillesEtTrajets(graphe, plateforme, v1.getTypeCoutPref(), v1.getTransportFavori());
        HashSet<Ville> villes = new HashSet<>();
        villes.add(a);
        villes.add(b);
        villes.add(c);
        villes.add(d);
        assertEquals(villes, plateforme.getVilles());
    }

    @Test
    void testIsNumeric() {
        assertTrue(App.isNumeric("5"));
        assertTrue(App.isNumeric("456"));
        assertTrue(App.isNumeric("-5"));
        assertTrue(App.isNumeric("67.5"));
        assertFalse(App.isNumeric("Test"));
        assertFalse(App.isNumeric(""));
    }
}
