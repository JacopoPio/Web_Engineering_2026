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
@Table(name = "abilita")
public class Abilita {
    @Id
    private String nome;
    @ManyToMany
    @JoinTable(
            name = "amministratore_abilità",
            joinColumns = @JoinColumn(name = "email_amministratore"),
            inverseJoinColumns = @JoinColumn(name = "nome_abilita")
    )
    private List<Amministratore> amministratori = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "operatore_abilità",
            joinColumns = @JoinColumn(name = "email_operatore"),
            inverseJoinColumns = @JoinColumn(name = "nome_abilita")
    )
    private List<Operatore> operatori = new ArrayList<>();
    public Abilita(String nome) {
        this.nome = nome;
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

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.nome.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Abilita)) {
            return false;
        }
        Abilita a = (Abilita) obj;
        return this.nome.equalsIgnoreCase(a.nome);
    }
    @Override
    public String toString() {
        return "Abilita: " + nome + '\n';
    }
}
