import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import fr.ulille.but.sae_s2_2024.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

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
    private Button boutonChercherVoyage;

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
    private ListView<TypeCout> preferencesCouts;
    @FXML
    private Button boutonCalculerVoyage;
    @FXML
    private Button boutonReserverVoyage;
    @FXML
    private ListView<RouteV3> resultats;

    // Historique
    @FXML
    private Pane pageHistorique;
    @FXML
    private Label labelAucunVoyage;
    @FXML
    private ListView<Route> listeHistorique;
    @FXML
    private Label nbVoyages;
    @FXML
    private Label tempsTotal;
    @FXML
    private Label tempsMoyen;
    @FXML
    private Label coutTotal;
    @FXML
    private Label coutMoyen;
    @FXML
    private Label emissionsTotal;
    @FXML
    private Label emissionsMoyenne;

    // Profil
    @FXML
    private Pane pageProfil;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField coutMax;
    @FXML
    private CheckBox profilCheckBoxTrain;
    @FXML
    private CheckBox profilCheckBoxAvion;
    @FXML
    private CheckBox profilCheckBoxBus;
    @FXML
    private ListView<TypeCout> profilPreferencesCout;
    @FXML
    private Button saveProfil;

    // App
    private Plateforme plateforme;
    private VoyageurV3 voyageur;
    private MultiGrapheOrienteValue graphe;
    private String trajets = "./src/trajets.csv";
    private String couts = "./src/couts.csv";

    @FXML
    private void initialize() {
        plateforme = new Plateforme();
        graphe = new MultiGrapheOrienteValue();
        voyageur = VoyageurV3.loadVoyageur("./saves/userSave");
        boutonAccueil.setOnAction(event -> switchPane(pageAccueil));
        boutonVoyage.setOnAction(event -> openNouveauVoyage());
        boutonHistorique.setOnAction(event -> openHistory());
        boutonProfil.setOnAction(event -> openProfile());
        boutonChercherVoyage.setOnAction(event -> openNouveauVoyage());
        boutonCalculerVoyage.setOnAction(event -> calculerVoyage());
        boutonReserverVoyage.setOnAction(event -> reserverVoyage());
        saveProfil.setOnAction(event -> createUser());
        setupCell(profilPreferencesCout);
        setupCell(preferencesCouts);
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
        ajouterVilles();
        displayHistorique();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (voyageur != null) {
                    voyageur.saveVoyageur();
                    System.out.println("User saved !");
                }
            }
        });
    }

    private void switchPane(Pane pane) {
        stackPane.getChildren().forEach(child -> child.setVisible(false));
        pane.setVisible(true);
    }

    private void calculerVoyage() {
        resultats.getItems().clear();
        graphe = new MultiGrapheOrienteValue();
        plateforme.ajouterVillesEtTrajets(graphe, preferencesCouts.getItems().get(0), getSelectedTransports());
        try {
            List<Chemin> result = AlgorithmeKPCC.kpcc(graphe, villeDepart.getSelectionModel().getSelectedItem(), villeArrivee.getSelectionModel().getSelectedItem(), 3);
            if (result.size() == 0) throw new NoTripException();
            boutonReserverVoyage.setVisible(true);
            for (int idx=0; idx<result.size(); idx++) {
                RouteV3 r = new RouteV3(result.get(idx));
                resultats.getItems().add(r);
            }      
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucun voyage trouvé");
            alert.setHeaderText("Aucun voyage trouvé");
            alert.setContentText("Veuillez essayer avec des critères différents");
            alert.showAndWait();
        }
    }

    private ArrayList<ModaliteTransport> getSelectedTransports() {
        ArrayList<ModaliteTransport> transports = new ArrayList<>();
        if (checkBoxAvion.isSelected()) transports.add(ModaliteTransport.AVION);
        if (checkBoxBus.isSelected()) transports.add(ModaliteTransport.BUS);
        if (checkBoxTrain.isSelected()) transports.add(ModaliteTransport.TRAIN);
        return transports;
    }

    private void ajouterVilles() {
        villeArrivee.getItems().addAll(plateforme.getVilles());
        villeDepart.getItems().addAll(plateforme.getVilles());
        FXCollections.sort(villeDepart.getItems(), Comparator.comparing(v -> v.getNom()));
        FXCollections.sort(villeArrivee.getItems(), Comparator.comparing(v -> v.getNom()));
    }

    private void createUser() {
        String name = nom.getText() + " " + prenom.getText();
        ArrayList<ModaliteTransport> transports = new ArrayList<>();
        if (profilCheckBoxAvion.isSelected()) transports.add(ModaliteTransport.AVION);
        if (profilCheckBoxBus.isSelected()) transports.add(ModaliteTransport.BUS);
        if (profilCheckBoxTrain.isSelected()) transports.add(ModaliteTransport.TRAIN);
        try {
            double maxCost = Double.parseDouble(coutMax.getText());
            TypeCout[] prefCouts = profilPreferencesCout.getItems().toArray(new TypeCout[0]);
            voyageur = new VoyageurV3(name, maxCost, transports, prefCouts);
            System.out.println("Hello "+voyageur.getNom()+" !");
        }
        catch (NumberFormatException e) {System.err.println(e.getMessage());}
        catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    private void reserverVoyage() {
        RouteV3 route = resultats.getSelectionModel().getSelectedItem();
        if (voyageur != null && route != null) {
            voyageur.addToHistorique(route);
        } else {
            System.out.println("Veuillez vous connecter avant de réserver un voyage");
        }
    }

    private void openHistory() {
        switchPane(pageHistorique);
        displayHistorique();
        displayStatistiques();
    }

    private void openProfile() {
        switchPane(pageProfil);
        displayProfile();
    }

    public void openNouveauVoyage() {
        switchPane(pageVoyage);
        setupPageChercherVoyage();
    }

    private void displayHistorique() {
        listeHistorique.setVisible(false);
        if (voyageur == null) labelAucunVoyage.setText("Veuillez vous connecter afin de consulter vos derniers voyages");
        else if (voyageur.getHistorique().size() == 0) labelAucunVoyage.setText("Aucun voyage enregistré");
        else {
            labelAucunVoyage.setVisible(false);
            listeHistorique.setVisible(true);
            listeHistorique.getItems().clear();
            for (Route r : voyageur.getHistorique()) {
                listeHistorique.getItems().add(r);
            }
        }
    }

    private void displayStatistiques() {
        if (voyageur != null && voyageur.getHistorique().size() > 0) {
            DecimalFormat df = new DecimalFormat("#.##");
            nbVoyages.setText(""+voyageur.getHistorique().size());
            tempsTotal.setText(""+df.format(voyageur.getTotalParCoutHistorique(TypeCout.TEMPS))+" minutes");
            tempsMoyen.setText(""+df.format(voyageur.getTotalParCoutHistorique(TypeCout.TEMPS)/voyageur.getHistorique().size())+" minutes");
            coutTotal.setText(""+df.format(voyageur.getTotalParCoutHistorique(TypeCout.PRIX))+"€");
            coutMoyen.setText(""+df.format(voyageur.getTotalParCoutHistorique(TypeCout.PRIX)/voyageur.getHistorique().size())+"€");
            emissionsTotal.setText(""+df.format(voyageur.getTotalParCoutHistorique(TypeCout.CO2))+" kg CO2e");
            emissionsMoyenne.setText(""+df.format(voyageur.getTotalParCoutHistorique(TypeCout.CO2)/voyageur.getHistorique().size())+" kg CO2e");
        }
    }

    private void displayProfile() {
        if (voyageur != null) {
            try (Scanner sc = new Scanner(voyageur.getNom())) {
                sc.useDelimiter(" ");
                nom.setText(sc.next());
                prenom.setText(sc.next());
            }
            coutMax.setText(""+voyageur.getCoutMax());
            checkTransportsBoxes(profilCheckBoxAvion, profilCheckBoxBus, profilCheckBoxTrain);
            displayPrefCouts(profilPreferencesCout);
        }
    }

    public void displayPrefCouts(ListView<TypeCout> listView) {
        listView.getItems().clear();
        for (TypeCout cout : voyageur.getPreferencesCouts()) {
            listView.getItems().add(cout);
        }
    }

    private void checkTransportsBoxes(CheckBox avion, CheckBox bus, CheckBox train) {
        for (ModaliteTransport transport : voyageur.getTransportFavori()) {
            if (transport.equals(ModaliteTransport.AVION)) avion.setSelected(true);
            if (transport.equals(ModaliteTransport.BUS)) bus.setSelected(true);
            if (transport.equals(ModaliteTransport.TRAIN)) train.setSelected(true);
        }
    }

    public void setupPageChercherVoyage() {
        if (voyageur != null) {
            displayPrefCouts(preferencesCouts);
            checkTransportsBoxes(checkBoxAvion, checkBoxBus, checkBoxTrain);
        }
    }

    private void setupCell(ListView<TypeCout> listview) {
        if (voyageur == null) listview.getItems().addAll(TypeCout.values());
        else listview.getItems().addAll(voyageur.getPreferencesCouts());
        listview.setCellFactory(param -> {
            ListCell<TypeCout> cell = new ListCell<>() {
                @Override
                protected void updateItem(TypeCout item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.name());
                    }
                }
            };

            cell.setOnDragDetected(event -> {
                if (!cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);

                    Label dragLabel = new Label(cell.getItem().name());
                    dragLabel.setStyle("-fx-background-color: white; -fx-border-color: black;");
                    dragLabel.setPrefWidth(cell.getWidth());
                    dragLabel.setPrefHeight(cell.getHeight());

                    SnapshotParameters snapshotParameters = new SnapshotParameters();
                    snapshotParameters.setFill(Color.TRANSPARENT);
                    
                    WritableImage dragView = new WritableImage((int) dragLabel.getPrefWidth(), (int) dragLabel.getPrefHeight());
                    dragLabel.snapshot(snapshotParameters, dragView);
                    db.setDragView(dragView, dragView.getWidth() / 2, dragView.getHeight() / 2);
    
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(cell.getItem().name());
                    db.setContent(cc);
                    event.consume();
                }
            });
    
            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });
    
            cell.setOnDragEntered(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    cell.setOpacity(0.3);
                }
            });
    
            cell.setOnDragExited(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    cell.setOpacity(1);
                }
            });
    
            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    int draggedIdx = -1;
                    for (int i = 0; i < listview.getItems().size(); i++) {
                        if (listview.getItems().get(i).name().equals(db.getString())) {
                            draggedIdx = i;
                            break;
                        }
                    }
                    if (draggedIdx >= 0) {
                        TypeCout draggedItem = listview.getItems().remove(draggedIdx);
    
                        int dropIdx;
                        if (cell.isEmpty()) {
                            dropIdx = listview.getItems().size();
                        } else {
                            dropIdx = cell.getIndex();
                        }
    
                        listview.getItems().add(dropIdx, draggedItem);
                        event.setDropCompleted(true);
                        listview.getSelectionModel().select(dropIdx);
                    } else {
                        event.setDropCompleted(false);
                    }
                    event.consume();
                }
            });
    
            cell.setOnDragDone(DragEvent::consume);
    
            return cell;
        });
    }
}
