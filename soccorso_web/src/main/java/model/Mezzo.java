/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Objects;

/**
 *
 * @author Jacopo Antonio
 */
public class Mezzo {
    private String tipo;
    private String targa;

    public Mezzo(String tipo, String targa) {
        this.tipo = tipo;
        this.targa = targa;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getTarga() {
        return this.targa;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    @Override
    public String toString() {
        return "Mezzo: " + this.tipo + "Targa: " + this.targa;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj == null || !(obj instanceof Mezzo)) {
            return false;
        }
        Mezzo m = (Mezzo)obj;
        return m.getTarga().equals(this.targa);
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.targa);
        return hash;
    }
    
}
