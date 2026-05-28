/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import jakarta.persistence.*;
import java.util.*;

/**
 *
 * @author Jacopo Antonio
 */
@Entity
@Table(name = "amministratore")
public class Amministratore {
    
    @Id
    private String email;
    private String nome;
    private String cognome;
    private LocalDate data_nascita; 
    private String citta_nascita;
    private String CF;
    private String indirizzo;
    private String password;
    @ManyToMany(mappedBy = "amministratori")
    private List<Patente> patenti = new ArrayList<>();
    @ManyToMany(mappedBy = "amministratori")
    private List<Abilita> abilita = new ArrayList<>();
    public Amministratore()
    {
        super();
    }

    public List<Patente> getPatenti() {
        return this.patenti;
    }

    public void setPatenti(List<Patente> patenti) {
        this.patenti = patenti;
    }

    public List<Abilita> getAbilita() {
        return this.abilita;
    }

    public void setAbilita(List<Abilita> abilita) {
        this.abilita = abilita;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getData_nascita() {
        return this.data_nascita;
    }

    public void setData_nascita(LocalDate data_nascita) {
        this.data_nascita = data_nascita;
    }

    public String getCitta_nascita() {
        return citta_nascita;
    }

    public void setCitta_nascita(String citta_nascita) {
        this.citta_nascita = citta_nascita;
    }

    public String getCF() {
        return this.CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public String getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + this.email.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Amministratore)) {
            return false;
        }
        Amministratore f = (Amministratore) obj;
        return this.email.equals(f.email);
    }
    @Override
    public String toString()
    {
        return "Email: " + this.email + 
                "\nNome: " + this.nome +
                "\nCognome: " + this.cognome +
                "\nCF: " + this.CF +
                "\nData nascita: " + this.data_nascita +
                "\nCittà nascita: " + this.citta_nascita +
                "\nIndirizzo: " + this.indirizzo +
                "\n";
    }
    
}
