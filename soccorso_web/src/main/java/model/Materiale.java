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
    private int id;

    @Column(name = "tipo", length = 255)
    private String tipo;

    @Column(name = "descrizione", length = 255)
    private String descrizione;

    @ManyToMany(mappedBy = "materiali")
    private List<Missione> missioni = new ArrayList<>();

    public Materiale() {
    }

    public Materiale(int id, String tipo, String descrizione) {
        this.id = id;
        this.tipo = tipo;
        this.descrizione = descrizione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        this.missioni = missioni == null ? new ArrayList<>() : missioni;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Materiale)) {
            return false;
        }
        Materiale other = (Materiale) obj;
        return id != 0 && id == other.id;
    }
}
