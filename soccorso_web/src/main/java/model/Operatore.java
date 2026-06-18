package model;

import jakarta.persistence.*;
import jakarta_configuration.resources.PasswordConverter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "operatore")
public class Operatore {

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

    @Convert(converter = PasswordConverter.class)
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    
    @Column(name = "attivo", nullable = false)
    private boolean attivo = true;

    @ManyToMany
    @JoinTable(
            name = "operatore_patente",
            joinColumns = @JoinColumn(name = "email_operatore"),
            inverseJoinColumns = @JoinColumn(name = "tipo_patente", referencedColumnName= "tipo")
    )
    private List<Patente> patenti = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "operatore_abilita",
            joinColumns = @JoinColumn(name = "email_operatore"),
            inverseJoinColumns = @JoinColumn(name = "nome_abilita")
    )
    private List<Abilita> abilita = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_squadra")
    private Squadra squadra;

    public Operatore() {
    }
    
    public boolean isAttivo() {
        return attivo;
    }

    public boolean getAttivo() {
        return attivo;
    }
    
    public void setAttivo(boolean attivo){
        this.attivo = attivo;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(this.email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Operatore)) {
            return false;
        }

        Operatore other = (Operatore) obj;
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