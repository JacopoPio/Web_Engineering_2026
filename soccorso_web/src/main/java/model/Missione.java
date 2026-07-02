package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "missione")
public class Missione implements Serializable {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "descrizione", length = 500)
    private String descrizione;

    @Column(name = "posizione", length = 255)
    private String posizione;

    @Column(name = "data_inizio")
    private LocalDateTime dataInizio;

    @Column(name = "data_fine")
    private LocalDateTime dataFine;

    @Column(name = "successo")
    private Integer successo;

    @Column(name = "commento_finale", length = 1000)
    private String commentoFinale;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_squadra",
            referencedColumnName = "ID",
            nullable = false
    )
    private Squadra squadra;

    /*
     * La richiesta continua a essere identificata dall'email.
     * Non viene introdotta alcuna colonna id_richiesta.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "email_richiesta",
            referencedColumnName = "email_segnalante",
            nullable = false
    )
    private Richiesta richiesta;

    @ManyToMany
    @JoinTable(
            name = "missione_operatore",
            joinColumns = @JoinColumn(name = "id_missione"),
            inverseJoinColumns = @JoinColumn(
                    name = "email_operatore",
                    referencedColumnName = "email"
            )
    )
    private List<Operatore> operatori = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "missione_mezzo",
            joinColumns = @JoinColumn(name = "id_missione"),
            inverseJoinColumns = @JoinColumn(name = "targa_mezzo")
    )
    private List<Mezzo> mezzi = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "missione_aggiornamento",
            joinColumns = @JoinColumn(name = "id_missione"),
            inverseJoinColumns = @JoinColumn(name = "id_aggiornamento")
    )
    private List<Aggiornamento> aggiornamenti = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "missione_materiale",
            joinColumns = @JoinColumn(name = "id_missione"),
            inverseJoinColumns = @JoinColumn(name = "id_materiale")
    )
    private List<Materiale> materiali = new ArrayList<>();

    public Missione() {
    }

    public Missione(String descrizione, Squadra squadra, Richiesta richiesta) {
        this.descrizione = descrizione;
        this.posizione = richiesta == null ? null : richiesta.getIndirizzo();
        this.dataInizio = LocalDateTime.now();
        this.squadra = squadra;
        this.richiesta = richiesta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    public String getDataInizioFormattata() {
        return dataInizio == null ? "-" : dataInizio.format(FORMATO_DATA);
    }

    public String getDataFineFormattata() {
        return dataFine == null ? "-" : dataFine.format(FORMATO_DATA);
    }

    public Integer getSuccesso() {
        return successo;
    }

    public void setSuccesso(Integer successo) {
        this.successo = successo;
    }

    public String getCommentoFinale() {
        return commentoFinale;
    }

    public void setCommentoFinale(String commentoFinale) {
        this.commentoFinale = commentoFinale;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    public Richiesta getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(Richiesta richiesta) {
        this.richiesta = richiesta;
    }

    public List<Operatore> getOperatori() {
        return operatori;
    }

    public void setOperatori(List<Operatore> operatori) {
        this.operatori = operatori == null ? new ArrayList<>() : operatori;
    }

    public List<Mezzo> getMezzi() {
        return mezzi;
    }

    public void setMezzi(List<Mezzo> mezzi) {
        this.mezzi = mezzi == null ? new ArrayList<>() : mezzi;
    }

    public List<Aggiornamento> getAggiornamenti() {
        return aggiornamenti;
    }

    public void setAggiornamenti(List<Aggiornamento> aggiornamenti) {
        this.aggiornamenti = aggiornamenti == null ? new ArrayList<>() : aggiornamenti;
    }

    public List<Materiale> getMateriali() {
        return materiali;
    }

    public void setMateriali(List<Materiale> materiali) {
        this.materiali = materiali == null ? new ArrayList<>() : materiali;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Missione)) {
            return false;
        }
        Missione other = (Missione) obj;
        return id != null && Objects.equals(id, other.id);
    }
}
