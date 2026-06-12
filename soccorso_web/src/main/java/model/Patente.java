package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patente")
public class Patente {

    @Id
    @Column(name = "tipo_patente", nullable = false, length = 50)
    private String tipoPatente;
     @ManyToMany
    @JoinTable(
            name = "amministratore_abilità",
            joinColumns = @JoinColumn(name = "tipo_patente"),
            inverseJoinColumns = @JoinColumn(name = "email_amministratore")
    )
    private List<Amministratore> amministratori = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "operatore_abilità",
            joinColumns = @JoinColumn(name = "tipo_patente"),
            inverseJoinColumns = @JoinColumn(name = "email_operatore")
    )
    private List<Operatore> operatori = new ArrayList<>();

    public Patente() {
    }

    public Patente(String tipoPatente) {
        this.tipoPatente = tipoPatente;
    }

    public String getTipoPatente() {
        return tipoPatente;
    }

    public void setTipoPatente(String tipoPatente) {
        this.tipoPatente = tipoPatente;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tipoPatente);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Patente)) {
            return false;
        }

        Patente other = (Patente) obj;
        return Objects.equals(this.tipoPatente, other.tipoPatente);
    }

    @Override
    public String toString() {
        return this.tipoPatente;
    }
}