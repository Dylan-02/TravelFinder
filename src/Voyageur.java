import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;

/** Classe permettant de représenter un Voyageur
 * @author <a href=mailto:dylan.lecocq.etu@univ-lille.fr>Dylan L</a>
 * @author <a href=mailto:amaury.vanhoutte.etu@univ-lille.fr>Amaury V</a>
 * @version 1.0
 */

public class Voyageur implements Serializable {
    /** Attribut permettant de caractériser un voyageur par un nom sous forme de chaîne de caractère */
    private String nom;
    /** Attribut permettant de caractériser un voyageur par un critère préféré */
    private TypeCout typeCoutPref;
    /** Attribut permettant de répresenter le cout maximal défini par le voyageur */
    private double coutMax;
    /** Attribut permettant de définir le moyen de transport favori du voyageur */
    private ArrayList<ModaliteTransport> transportFavori;

    /** Constructeur de la classe, permettant d'instancier un nouveau Voyageur
     * @param nom Nom du voyageur
     * @param criterePref Critère préféré du voyageur
     */
    public Voyageur(String nom, TypeCout criterePref, double coutMax, ArrayList<ModaliteTransport> transportFavori) {
        this.nom = nom;
        this.typeCoutPref = criterePref;
        this.coutMax = coutMax;
        this.transportFavori = transportFavori;
    }

    /** Retourne le nom du voyageur de l'instance courante
     * @return Retourne une chaîne de caractère représentant le nom du voyageur
     */
    public String getNom() {
        return this.nom;
    }

    /** Retourne le critère préféré du voyageur de l'instance courante
     * @return Retourne le critère préféré du voyageur
     */ 
    public TypeCout getTypeCoutPref() {
        return this.typeCoutPref;
    }

    /** Retourne le coût maximal accepté par le voyageur
     * @return Le cout maximal du trajet défini par le voyageur
     */
    public double getCoutMax() {
        return this.coutMax;
    }

    /** Retourne le moyen de transport favori du voyageur 
     * @return Le moyen de transport favori du voyageur
    */
    public ArrayList<ModaliteTransport> getTransportFavori() {
        return this.transportFavori;
    }
    /** Permet de modifier le critère préféré du voyageur de l'instance courante
     * @param count Nouvelle valeur du critère préféré
     */
    public void setTypeCoutPref(TypeCout cout) {
        this.typeCoutPref = cout;
    }

    /** Permet de vérifier si les trajets passés en paramètre ne dépassent pas le cout maximal
     * 
     * @param trajets Représente un ensemble de trajets
     * @return Une liste ne contenant que les trajets ayant un coût inférieur ou égal au coût maximal.
     */
    public List<Chemin> verifierBornes(List<Chemin> trajets) {
        List<Chemin> resultat = new ArrayList<>();
        if (trajets.size() > 0) {
            for (int idx = 0; idx<trajets.size(); idx++) {
                Route trajet = new Route(trajets.get(idx));
                if (trajet.poids()<=this.getCoutMax()) resultat.add(trajet);
            }
        }
        return resultat;
    }
}