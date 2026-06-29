package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "squadra")
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /*
     * Il lato proprietario è Operatore.squadra,
     * perché contiene la @JoinColumn id_squadra.
     */
    @OneToMany(mappedBy = "squadra")
    private List<Operatore> operatori = new ArrayList<>();

    /*
     * Il lato proprietario è Missione.squadra,
     * perché contiene la @JoinColumn id_squadra.
     */
    @OneToOne(mappedBy = "squadra")
    private Missione missione;

    public Squadra() {
    }

    public Squadra(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    /*
     * Normalmente non devi chiamare questo setter
     * quando crei una nuova squadra, perché l'ID
     * viene generato da MySQL.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Operatore> getOperatori() {
        return operatori;
    }

    public void setOperatori(List<Operatore> operatori) {
        this.operatori = operatori != null
                ? operatori
                : new ArrayList<>();
    }

    public Missione getMissione() {
        return missione;
    }

    public void setMissione(Missione missione) {
        this.missione = missione;
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

        if (!(obj instanceof Squadra)) {
            return false;
        }

        Squadra other = (Squadra) obj;

        /*
         * Due entità nuove con ID null non devono
         * essere considerate automaticamente uguali.
         */
        return id != null
                && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Id: " + id
                + "\nNome squadra: " + nome;
    }
}