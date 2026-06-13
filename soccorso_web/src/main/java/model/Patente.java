package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "patente")
public class Patente {

    @Id
    @Column(name = "tipo", nullable = false, length = 10)
    private String tipoPatente;

    public Patente() {
    }

    public Patente(String tipoPatente) {
        this.tipoPatente = tipoPatente;
    }

    public String getTipoPatente() {
        return this.tipoPatente;
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