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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "missione")
public class Missione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descrizione", length = 255)
    private String descrizione;

    /*
     * Non deve esistere anche un campo int id_squadra.
     *
     * Questa relazione gestisce direttamente
     * la foreign key missione.id_squadra.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_squadra",
            referencedColumnName = "ID",
            nullable = false
    )
    private Squadra squadra;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "email_richiesta",
            referencedColumnName = "email_segnalante",
            nullable = false
    )
    private Richiesta richiesta;

    @ManyToMany
    @JoinTable(
            name = "missione_mezzo",
            joinColumns = @JoinColumn(
                    name = "id_missione"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "targa_mezzo"
            )
    )
    private List<Mezzo> mezzi = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "missione_aggiornamento",
            joinColumns = @JoinColumn(
                    name = "id_missione"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_aggiornamento"
            )
    )
    private List<Aggiornamento> aggiornamenti =
            new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "missione_materiale",
            joinColumns = @JoinColumn(
                    name = "id_missione"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_materiale"
            )
    )
    private List<Materiale> materiali =
            new ArrayList<>();

    public Missione() {
    }

    public Missione(
            String descrizione,
            Squadra squadra,
            Richiesta richiesta
    ) {
        this.descrizione = descrizione;
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

    public List<Mezzo> getMezzi() {
        return mezzi;
    }

    public void setMezzi(List<Mezzo> mezzi) {
        this.mezzi = mezzi != null
                ? mezzi
                : new ArrayList<>();
    }

    public List<Aggiornamento> getAggiornamenti() {
        return aggiornamenti;
    }

    public void setAggiornamenti(
            List<Aggiornamento> aggiornamenti
    ) {
        this.aggiornamenti =
                aggiornamenti != null
                        ? aggiornamenti
                        : new ArrayList<>();
    }

    public List<Materiale> getMateriali() {
        return materiali;
    }

    public void setMateriali(
            List<Materiale> materiali
    ) {
        this.materiali =
                materiali != null
                        ? materiali
                        : new ArrayList<>();
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

        return id != null
                && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {

        String emailRichiesta =
                richiesta == null
                        ? ""
                        : richiesta.getEmail_segnalante();

        Integer idSquadra =
                squadra == null
                        ? null
                        : squadra.getId();

        return "Missione: id=" + id
                + "\nDescrizione=" + descrizione
                + "\nId squadra=" + idSquadra
                + "\nEmail segnalante="
                + emailRichiesta;
    }
}