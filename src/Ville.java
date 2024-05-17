import java.util.Objects;


import fr.ulille.but.sae_s2_2024.Lieu;
/**
 * Représente une ville.
 */
public class Ville implements Lieu {
    private String nom;

    /**
     * Construit un objet Ville avec le nom spécifié.
     * @param nom le nom de la ville
     */
    public Ville(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le nom de la ville.
     * @return le nom de la ville
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la ville.
     * @return une représentation sous forme de chaîne de caractères de la ville
     */
    public String toString() {
        return this.nom;
    }

    /**
     * Vérifie si cette ville est égale à l'objet spécifié.
     * Deux villes sont considérées comme égales si elles ont le même nom.
     * @param obj l'objet à comparer
     * @return true si les villes sont égales, false sinon
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ville ville = (Ville) obj;
        return this.nom.equals(ville.getNom());
    }

    /**
     * Retourne la valeur du code de hachage pour la ville.
     * @return la valeur du code de hachage pour la ville
     */
    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}
