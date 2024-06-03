/**
 * Signale qu'aucun voyage répondant au critères n'a été trouvé.
 */
public class NoTripException extends Exception {
    /**
     * Construit une nouvelle instance de NoTripException sans message détaillé.
     */
    NoTripException() {}

    /**
     * Construit une nouvelle instance de NoTripException avec le message détaillé spécifié.
     *
     * @param msg le message détaillé.
     */
    NoTripException(String msg) {
        super(msg);
    }
}