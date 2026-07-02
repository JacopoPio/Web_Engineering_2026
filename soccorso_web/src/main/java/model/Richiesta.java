package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "richiesta")
public class Richiesta implements Serializable {

    /*
     * Scelta progettuale mantenuta: l'email del segnalante
     * continua a essere la chiave primaria della richiesta.
     */
    @Id
    @Column(name = "email_segnalante", nullable = false, length = 50)
    private String email_segnalante;

    @Column(name = "nome_segnalante", length = 255)
    private String nome_segnalante;

    @Column(name = "descrizione", length = 1000)
    private String descrizione;

    @Column(name = "indirizzo", length = 255)
    private String indirizzo;

    @Column(name = "stato", length = 30)
    private String stato;

    @Column(name = "pathFoto", length = 500)
    private String pathFoto;

    @Column(name = "ip_origine", length = 16)
    private byte[] ip_origine;

    @Column(name = "token_conferma", unique = true, length = 64)
    private String tokenConferma;

    @Column(name = "data_creazione")
    private LocalDateTime dataCreazione;

    @Column(name = "data_conferma")
    private LocalDateTime dataConferma;

    public Richiesta() {
    }

    public Richiesta(String emailSegnalante,
                     String descrizione,
                     String indirizzo,
                     String stato,
                     String pathFoto,
                     String nomeSegnalante,
                     byte[] ipOrigine) {
        this.email_segnalante = emailSegnalante;
        this.descrizione = descrizione;
        this.indirizzo = indirizzo;
        this.stato = stato;
        this.pathFoto = pathFoto;
        this.nome_segnalante = nomeSegnalante;
        this.ip_origine = ipOrigine;
        this.dataCreazione = LocalDateTime.now();
    }

    public String getEmail_segnalante() {
        return email_segnalante;
    }

    public void setEmail_segnalante(String email_segnalante) {
        this.email_segnalante = email_segnalante;
    }

    public String getNome_segnalante() {
        return nome_segnalante;
    }

    public void setNome_segnalante(String nome_segnalante) {
        this.nome_segnalante = nome_segnalante;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public byte[] getIp_origine() {
        return ip_origine;
    }

    public void setIp_origine(byte[] ip_origine) {
        this.ip_origine = ip_origine;
    }

    public String getTokenConferma() {
        return tokenConferma;
    }

    public void setTokenConferma(String tokenConferma) {
        this.tokenConferma = tokenConferma;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getDataConferma() {
        return dataConferma;
    }

    public void setDataConferma(LocalDateTime dataConferma) {
        this.dataConferma = dataConferma;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email_segnalante);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Richiesta)) {
            return false;
        }
        Richiesta other = (Richiesta) obj;
        return Objects.equals(email_segnalante, other.email_segnalante)
                && Arrays.equals(ip_origine, other.ip_origine);
    }
}
