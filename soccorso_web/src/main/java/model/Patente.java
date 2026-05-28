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
@Table(name = "patente")
public class Patente {
    @Id
    private String tipo;
    @ManyToMany
    @JoinTable(
            name = "amministratore_patente",
            joinColumns = @JoinColumn(name = "tipo_patente"),
            inverseJoinColumns = @JoinColumn(name = "email_amministratore")
    )
    private List<Amministratore> amministratori = new ArrayList<>();
    public Patente(String tipo) {
        this.tipo = tipo;
    }
    @ManyToMany
    @JoinTable(
            name = "operatore_patente",
            joinColumns = @JoinColumn(name = "email_operatore"),
            inverseJoinColumns = @JoinColumn(name = "tipo_patente")
    )
    private List<Operatore> operatori = new ArrayList<>();
    public Patente()
    {
        super();
    }

    public List<Amministratore> getAmministratori() {
        return this.amministratori;
    }

    public void setAmministratori(List<Amministratore> amministratori) {
        this.amministratori = amministratori;
    }

    public List<Operatore> getOperatori() {
        return this.operatori;
    }

    public void setOperatori(List<Operatore> operatori) {
        this.operatori = operatori;
    }
    
    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.tipo.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Patente)) {
            return false;
        }
        Patente p = (Patente)obj;
        return p.tipo.equalsIgnoreCase(p.tipo);
    }
    
}
