package model;

import jakarta.persistence.*;
import jakarta_configuration.resources.PasswordConverter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "amministratore")
public class Amministratore {

    @Id
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 50)
    private String cognome;

    @Column(name = "data_nascita")
    private LocalDate data_nascita;

    @Column(name = "citta_nascita", length = 100)
    private String citta_nascita;

    @Column(name = "CF", length = 16)
    private String CF;

    @Column(name = "indirizzo", length = 150)
    private String indirizzo;

    @Column(name = "password", nullable = false, length = 100)
    @Convert(converter = PasswordConverter.class)
    private String password;


    @ManyToMany(mappedBy = "amministratori")
    private List<Abilita> abilita = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "amministratore_patente",
        joinColumns = @JoinColumn(name = "email_amministratore"),
        inverseJoinColumns = @JoinColumn(name = "tipo_patente")
    )
    private List<Patente> patenti = new ArrayList<>();
    
    public Amministratore() {
        this.patenti = new ArrayList<>();
    }

    public Amministratore(String email, String nome, String cognome,
                          LocalDate data_nascita, String citta_nascita,
                          String CF, String indirizzo, String password) {
        this.patenti = new ArrayList<>();
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.data_nascita = data_nascita;
        this.citta_nascita = citta_nascita;
        this.CF = CF;
        this.indirizzo = indirizzo;
        this.password = password;
    }

    public List<Patente> getPatenti() {
        return patenti;
    }

    public void setPatenti(List<Patente> patenti) {
        this.patenti = patenti;
    }

    public List<Abilita> getAbilita() {
        return abilita;
    }

    public void setAbilita(List<Abilita> abilita) {
        this.abilita = abilita;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }   

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }   

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }   

    public LocalDate getData_nascita() {
        return data_nascita;
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
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * equals e hashCode basati sulla chiave primaria email.
     */
    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Amministratore)) {
            return false;
        }

        Amministratore other = (Amministratore) obj;

        return Objects.equals(this.email, other.email);
    }

    @Override
    public String toString() {
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