import java.io.FileNotFoundException;

import fr.ulille.but.sae_s2_2024.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MonController {
    
    @FXML
    private StackPane stackPane;

    // Toolbar
    @FXML
    private Button boutonAccueil;
    @FXML
    private Button boutonVoyage;
    @FXML
    private Button boutonHistorique;
    @FXML
    private Button boutonProfil;

    // Accueil
    @FXML
    private Pane pageAccueil;
    @FXML 
    Button boutonChercherVoyage;

    // Chercher Voyage
    @FXML
    private Pane pageVoyage;
    @FXML
    private ComboBox<Ville> villeDepart;
    @FXML
    private ComboBox<Ville> villeArrivee;
    @FXML
    private DatePicker date;
    @FXML
    private CheckBox checkBoxTrain;
    @FXML
    private CheckBox checkBoxAvion;
    @FXML
    private CheckBox checkBoxBus;
    @FXML
    private ComboBox<TypeCout> criteres;

    // Historique
    @FXML
    private Pane pageHistorique;

    // Profil
    @FXML
    private Pane pageProfil;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private CheckBox profilCheckBoxTrain;
    @FXML
    private CheckBox profilCheckBoxAvion;
    @FXML
    private CheckBox profilCheckBoxBus;
    @FXML
    private CheckBox profilCheckBoxTemps;
    @FXML
    private CheckBox profilCheckBoxCout;
    @FXML
    private CheckBox profilCheckBoxPollution;
    @FXML
    private Button saveProfil;

    // App
    private Plateforme plateforme;
    private Voyageur voyageur;
    private MultiGrapheOrienteValue graphe;
    private String trajets = "./src/trajets.csv";
    private String couts = "./src/couts.csv";

    @FXML
    private void initialize() {
        plateforme = new Plateforme();
        graphe = new MultiGrapheOrienteValue();
        boutonAccueil.setOnAction(event -> switchPane(pageAccueil));
        boutonVoyage.setOnAction(event -> switchPane(pageVoyage));
        boutonHistorique.setOnAction(event -> switchPane(pageHistorique));
        boutonProfil.setOnAction(event -> switchPane(pageProfil));
        boutonChercherVoyage.setOnAction(event -> switchPane(pageVoyage));
        try {
            String data[] = plateforme.getDataFromCSV(trajets);
            plateforme.verifiyData(data);
            for (int idx=0; idx<data.length; idx++) {
                String[] tab = data[idx].split(";");
                plateforme.retrieveData(tab);
            }
            String[] correspondances = plateforme.getDataFromCSV(couts);
            plateforme.verifiyData(correspondances);
            plateforme.ajouterCorrespondances(correspondances);
        } 
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        catch (IllegalArgumentException e) {System.err.println("Argument invalide");}
        catch (InvalidStructureException e) {System.err.println(e.getMessage());}
    }

    private void switchPane(Pane pane) {
        stackPane.getChildren().forEach(child -> child.setVisible(false));
        pane.setVisible(true);
    }
}

