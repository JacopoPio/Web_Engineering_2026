/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import jakarta.persistence.*;

/**
 *
 * @author Jacopo Antonio
 */
@Entity
@Table(name = "patente")
public class Patente {
    @Id
    private String tipo;

    public Patente(String tipo) {
        this.tipo = tipo;
    }
    public Patente()
    {
        super();
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
