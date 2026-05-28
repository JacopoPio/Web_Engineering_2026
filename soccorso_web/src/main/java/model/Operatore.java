/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.util.*;
import jakarta.persistence.*;

/**
 *
 * @author Jacopo Antonio
 */
@Entity
@Table (name = "operatore")
public class Operatore {
    @Id
    private String email;
    
    @Column (name = "password")
    private String password;
    private String nome;
    private String cognome;
    private String CF;
    
    @Column(name = "data_nascita")
    private LocalDate data_nascita;
    
    @Column(name = "città_di_nascita")
    private String città_di_nascita;
    
    private String indirizzo;
    private boolean caposquadra;
    
    @ManyToOne
    @JoinColumn(name = "id_squadra")
    private Squadra squadra;
    
    @ManyToMany(mappedBy="operatori")
    private List<Abilita> abilita = new ArrayList<>();
    @ManyToMany(mappedBy="operatori")
    private List<Patente> patente = new ArrayList<>();
    public Operatore()
    {
        super();
    }
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Abilita> getAbilita() {
        return this.abilita;
    }

    public void setAbilita(List<Abilita> abilita) {
        this.abilita = abilita;
    }

    public List<Patente> getPatente() {
        return this.patente;
    }

    public void setPatente(List<Patente> patente) {
        this.patente = patente;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCF() {
        return this.CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public LocalDate getData_nascita() {
        return data_nascita;
    }

    public void setData_nascita(LocalDate data_nascita) {
        this.data_nascita = data_nascita;
    }

    public String getCittà_di_nascita() {
        return città_di_nascita;
    }

    public void setCittà_di_nascita(String città_di_nascita) {
        this.città_di_nascita = città_di_nascita;
    }

    public String getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public boolean isCaposquadra() {
        return this.caposquadra;
    }

    public void setCaposquadra(boolean caposquadra) {
        this.caposquadra = caposquadra;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.email.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Operatore)) {
            return false;
        }
        Operatore o = (Operatore) obj;
        return this.email.equals(o.email);
    }
    @Override
    public String toString()
    {
        return "Email: " + this.email + 
                "\nNome: " + this.nome +
                "\nCognome: " + this.cognome +
                "\nCF: " + this.CF +
                "\nData nascita: " + this.data_nascita +
                "\nCittà nascita: " + this.città_di_nascita +
                "\nIndirizzo: " + this.indirizzo +
                "\nCaposquadra: " + this.caposquadra +
                "\nid_squadra: " + this.squadra.getId()+ "\n";
    }
    
}
