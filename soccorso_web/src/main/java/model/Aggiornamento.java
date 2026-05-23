/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.*;

/**
 *
 * @author Jacopo Antonio
 */
@Entity
@Table(name = "aggiornamento")
public class Aggiornamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String descrizione;
    private LocalDateTime data_update;
    
    
    @ManyToMany(mappedBy = "aggiornamenti")
    private List<Missione> missioni = new ArrayList<>();
    
    
    public Aggiornamento(int id, String descrizione, LocalDateTime data_update) {
        this.id = id;
        this.descrizione = descrizione;
        this.data_update = data_update;
    }

    public int getId() {
        return this.id;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public LocalDateTime getData_update() {
        return this.data_update;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setData_update(LocalDateTime data_update) {
        this.data_update = data_update;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Aggiornamento)) {
            return false;
        }
        Aggiornamento other = (Aggiornamento) obj;
        return this.id == other.id;
    }
    @Override
    public String toString()
    {
        return "Aggiornamento " + this.data_update + "\nDescrizione: " + this.descrizione;
    }
    
}
