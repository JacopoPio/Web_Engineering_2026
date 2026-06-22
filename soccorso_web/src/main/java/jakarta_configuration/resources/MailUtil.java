package jakarta_configuration.resources;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {

    // Configura qui i dati del tuo server SMTP (o caricali da un file di properties)
   // Sostituisci la parte alta della tua classe MailUtil con questi dati precisi:
private static final String SMTP_HOST = "sandbox.smtp.mailtrap.io"; 
private static final String SMTP_PORT = "2525"; // La porta 2525 è perfetta
private static final String SMTP_USER = "19b8e6b29128f7"; 
private static final String SMTP_PASSWORD = "4f2f02b562f35c"; // Clicca sull'occhio o copia il valore che finisce con 'f35c' 

    public static void inviaCredenziali(String destinatario,
                                        String nome,
                                        String username,
                                        String passwordTemporanea,
                                        String ruolo) {

        // 1. Configurazione delle proprietà di rete per la connessione SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Obbligatorio per canali cifrati sicuri
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        // 2. Creazione della Sessione con autenticazione protetta
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            // 3. Creazione e composizione del messaggio email
            Message message = new MimeMessage(session);
            
            // Imposta il mittente (l'email configurata sopra)
            message.setFrom(new InternetAddress(SMTP_USER));
            
            // Imposta il destinatario reale
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            
            // Oggetto dell'email
            message.setSubject("Credenziali di Accesso - SoccorsoWeb");

            // Corpo del testo (Formattato)
            String testoEmail = "Ciao " + nome + ",\n\n"
                    + "Il tuo account per SoccorsoWeb è stato creato con successo dall'amministratore.\n"
                    + "Di seguito trovi i tuoi dati di accesso temporanei:\n\n"
                    + "Ruolo operativo: " + ruolo + "\n"
                    + "Username/Email: " + username + "\n"
                    + "Password Temporanea: " + passwordTemporanea + "\n\n"
                    + "Nota: Ti consigliamo caldamente di modificare questa password al tuo primo login.\n\n"
                    + "Cordiali saluti,\nIl Team di SoccorsoWeb";

            message.setText(testoEmail);

            // 4. Invio fisico dell'email attraverso la rete
            Transport.send(message);

            System.out.println(" Email inviata con successo a: " + destinatario);

        } catch (MessagingException e) {
            System.err.println("Errore critico durante l'invio dell'email: " + e.getMessage());
            e.printStackTrace();
            // Opzionale: puoi rilanciare un'eccezione personalizzata per catturarla nel Servlet
        }
    }
}