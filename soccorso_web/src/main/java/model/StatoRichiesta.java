package model;

/**
 * Valori salvati nella colonna richiesta.stato.
 * Le costanti evitano differenze come ATTIVA, attiva e IN_CORSO.
 */
public final class StatoRichiesta {

    public static final String DA_CONFERMARE = "da confermare";
    public static final String ATTIVA = "attiva";
    public static final String IN_CORSO = "in corso";
    public static final String CHIUSA = "chiusa";
    public static final String IGNORATA = "ignorata";

    private StatoRichiesta() {
    }
}
