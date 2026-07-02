package jakarta_configuration.resources;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public final class MailUtil {

    private static final String SMTP_HOST = env("SOCCORSOWEB_SMTP_HOST", "sandbox.smtp.mailtrap.io");
    private static final String SMTP_PORT = env("SOCCORSOWEB_SMTP_PORT", "2525");
    private static final String SMTP_USER = env("SOCCORSOWEB_SMTP_USER", "");
    private static final String SMTP_PASSWORD = env("SOCCORSOWEB_SMTP_PASSWORD", "");
    private static final String SMTP_FROM = env("SOCCORSOWEB_SMTP_FROM", "noreply@soccorsoweb.local");

    private MailUtil() {
    }

    public static void inviaCredenziali(String destinatario,
                                        String nome,
                                        String username,
                                        String passwordTemporanea,
                                        String ruolo) {
        String testo = "Ciao " + nome + ",\n\n"
                + "Il tuo account SoccorsoWeb è stato creato.\n\n"
                + "Ruolo: " + ruolo + "\n"
                + "Username/Email: " + username + "\n"
                + "Password temporanea: " + passwordTemporanea + "\n\n"
                + "Accedi e modifica la password appena possibile.\n\n"
                + "SoccorsoWeb";

        invia(destinatario, "Credenziali di accesso - SoccorsoWeb", testo);
    }

    public static void inviaConfermaRichiesta(String destinatario,
                                              String nomeSegnalante,
                                              String linkConferma) {
        String testo = "Ciao " + nomeSegnalante + ",\n\n"
                + "abbiamo ricevuto la tua richiesta di soccorso.\n"
                + "Per renderla attiva apri il seguente collegamento:\n\n"
                + linkConferma + "\n\n"
                + "Il collegamento scade dopo 24 ore.\n\n"
                + "SoccorsoWeb";

        invia(destinatario, "Conferma la richiesta - SoccorsoWeb", testo);
    }

    public static void inviaNotificaRichiestaAccettata(String destinatario,
                                                       String nomeSegnalante,
                                                       String descrizioneMissione) {
        String testo = "Ciao " + nomeSegnalante + ",\n\n"
                + "la tua richiesta è stata accettata ed è stata creata una missione.\n\n"
                + "Obiettivo: " + descrizioneMissione + "\n\n"
                + "SoccorsoWeb";

        invia(destinatario, "Richiesta accettata - SoccorsoWeb", testo);
    }

    public static void inviaNotificaNuovaMissione(String destinatario,
                                                  String nomeOperatore,
                                                  String descrizioneMissione,
                                                  String posizione) {
        String testo = "Ciao " + nomeOperatore + ",\n\n"
                + "sei stato assegnato a una nuova missione.\n\n"
                + "Obiettivo: " + descrizioneMissione + "\n"
                + "Posizione: " + posizione + "\n\n"
                + "Accedi all'area operatore per consultare i dettagli.\n\n"
                + "SoccorsoWeb";

        invia(destinatario, "Nuova missione assegnata - SoccorsoWeb", testo);
    }

    public static void inviaNotificaAggiornamento(String destinatario,
                                                  String nomeOperatore,
                                                  String descrizioneMissione,
                                                  String testoAggiornamento) {
        String testo = "Ciao " + nomeOperatore + ",\n\n"
                + "è stato inserito un aggiornamento per la missione:\n"
                + descrizioneMissione + "\n\n"
                + "Aggiornamento:\n" + testoAggiornamento + "\n\n"
                + "SoccorsoWeb";

        invia(destinatario, "Nuovo aggiornamento missione - SoccorsoWeb", testo);
    }

    public static void inviaNotificaMissioneChiusa(String destinatario,
                                                   String nomeOperatore,
                                                   String descrizioneMissione,
                                                   int successo,
                                                   String commentoFinale) {
        String testo = "Ciao " + nomeOperatore + ",\n\n"
                + "la missione è stata conclusa.\n\n"
                + "Missione: " + descrizioneMissione + "\n"
                + "Successo: " + successo + "/5\n"
                + "Commento finale: "
                + (commentoFinale == null || commentoFinale.isBlank()
                        ? "Nessun commento"
                        : commentoFinale)
                + "\n\nSoccorsoWeb";

        invia(destinatario, "Missione conclusa - SoccorsoWeb", testo);
    }

    private static void invia(String destinatario, String oggetto, String testo) {
        if (destinatario == null || destinatario.isBlank()) {
            return;
        }
        if (SMTP_USER.isBlank() || SMTP_PASSWORD.isBlank()) {
            System.err.println("Email non inviata: configurare SOCCORSOWEB_SMTP_USER e SOCCORSOWEB_SMTP_PASSWORD");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(oggetto);
            message.setText(testo);
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Errore invio email a " + destinatario + ": " + e.getMessage());
        }
    }

    private static String env(String nome, String valoreDefault) {
        String valore = System.getenv(nome);
        return valore == null || valore.isBlank() ? valoreDefault : valore.trim();
    }
}
