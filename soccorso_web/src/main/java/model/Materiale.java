/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Jacopo Antonio
 */
public class Materiale {
    private int id;
    private String tipo;
    private String descrizione;

    public Materiale(int id, String tipo, String descrizione) {
        this.id = id;
        this.tipo = tipo;
        this.descrizione = descrizione;
    }

    public int getId() {
        return this.id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Materiale)) {
            return false;
        }
        Materiale other = (Materiale) obj;
        return this.id == other.id;
    }
    @Override
    public String toString()
    {
        return "Tipo: " + this.tipo + " Descrizione: " + this.descrizione;
    }
}
