/**
 * Signale que la structure du fichier parcouru est invalide.
 */
public class InvalidStructureException extends Exception {
    /**
     * Construit une nouvelle InvalidStructureException sans message détaillé.
     */
    InvalidStructureException() {}

    /**
     * Construit une nouvelle InvalidStructureException avec le message détaillé spécifié.
     * 
     * @param msg le message détaillé
     */
    InvalidStructureException(String msg) {
        super(msg);
    }
}
