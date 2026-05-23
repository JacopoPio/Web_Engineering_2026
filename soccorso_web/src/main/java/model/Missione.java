/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.*;

/**
 *
 * @author Jacopo Antonio
 */
@Entity
@Table(name = "missione")
public class Missione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String descrizione;
    private int id_squadra;
    @Column(name = "email_richiesta")
    private String email_segnalante;
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
    public Missione()
    {
        super();
    }

    public Missione(int id, String descrizione, int id_squadra, String email_segnalante) {
        this.id = id;
        this.descrizione = descrizione;
        this.id_squadra = id_squadra;
        this.email_segnalante = email_segnalante;
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getId_squadra() {
        return this.id_squadra;
    }

    public void setId_squadra(int id_squadra) {
        this.id_squadra = id_squadra;
    }

    public String getEmail_segnalante() {
        return this.email_segnalante;
    }

    public void setEmail_segnalante(String email_segnalante) {
        this.email_segnalante = email_segnalante;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Missione)) {
            return false;
        }
        Missione m = (Missione) obj;
        return this.id == m.id;
    }

    @Override
    public String toString() {
        return "Missione: " + " id= " + id + "\ndescrizione= " + descrizione + "\nid_squadra= " + id_squadra + 
                "\nemail_segnalante= " + email_segnalante + '\n';
    }
    
    
}
