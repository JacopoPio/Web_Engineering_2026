/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.*;
import jakarta.persistence.*;

/**
 *
 * @author Jacopo Antonio
 */
@Entity
@Table(name = "squadra")
public class Squadra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    
    @OneToMany(mappedBy = "squadra")
    private List<Operatore> operatori = new ArrayList<>();
    
    @OneToOne(mappedBy = "squadra")
    private Missione missione;
    
    public Squadra()
    {
        super();
    }

    public Squadra(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public Squadra()
    {
        super();
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Squadra)) {
            return false;
        }
        Squadra s = (Squadra) obj;
        return this.id == s.id;
    }
    @Override
    public String toString()
    {
        return "Id: " + this.id + "\nNome Squadra: " + this.nome;
    }
}
