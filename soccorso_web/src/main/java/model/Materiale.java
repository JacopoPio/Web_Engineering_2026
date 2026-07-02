package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "materiale")
public class Materiale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    @Column(name = "descrizione", nullable = false, length = 500)
    private String descrizione;

    @ManyToMany(mappedBy = "materiali")
    private List<Missione> missioni = new ArrayList<>();

    public Materiale() {
    }

    public Materiale(String tipo, String descrizione) {
        this.tipo = tipo;
        this.descrizione = descrizione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<Missione> getMissioni() {
        return missioni;
    }

    public void setMissioni(List<Missione> missioni) {
        this.missioni = missioni;
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
        if (!(obj instanceof Materiale other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }
}
