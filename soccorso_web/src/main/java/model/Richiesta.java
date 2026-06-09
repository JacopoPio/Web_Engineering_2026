/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.Arrays;

/**
 *
 * @author Jacopo Antonio
 */
@Entity
@Table(name = "richiesta")
public class Richiesta {
    
    @Id
    private String email_segnalante;
    private String descrizione;
    private String indirizzo;
    private String stato;
    private String pathFoto;
    private String nome_segnalante;
    private byte[] ip_origine;

    public String getNome_segnalante() {
        return this.nome_segnalante;
    }

    public void setNome_segnalante(String nome_segnalante) {
        this.nome_segnalante = nome_segnalante;
    }

    public byte[] getIp_origine() {
        return this.ip_origine;
    }

    public void setIp_origine(byte[] ip_origine) {
        this.ip_origine = ip_origine;
    }

    public Richiesta()
    {
        super();
    }
    public String getEmail_segnalante() {
        return this.email_segnalante;
    }
    
    public void setEmail_segnalante(String email_segnalante) {
        this.email_segnalante = email_segnalante;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getStato() {
        return this.stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getPathFoto() {
        return this.pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public Richiesta(String email_segnalante, String descrizione, String indirizzo, String stato, String pathFoto, String nome, byte[] ip) {
        this.email_segnalante = email_segnalante;
        this.descrizione = descrizione;
        this.indirizzo = indirizzo;
        this.stato = stato;
        this.pathFoto = pathFoto;
        this.nome_segnalante = nome;
        this.ip_origine = ip;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.email_segnalante.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Richiesta)) {
            return false;
        }
        Richiesta r = (Richiesta) obj;
        return this.email_segnalante.equals(r.email_segnalante);
    }
    @Override
    public String toString()
    {
        return "Email segnalante: " + this.email_segnalante + 
                "\nDescrizione: " + this.descrizione + 
                "\nIndirizzo: " + this.indirizzo +
                "\nStato: " + this.stato +
                "\nNome segnalante: " + this.nome_segnalante + 
                "Ip origine: " + Arrays.toString(this.ip_origine) + "\n";
    }
}
