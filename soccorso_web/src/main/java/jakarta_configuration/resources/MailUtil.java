package jakarta_configuration.resources;

public class MailUtil {

    public static void inviaCredenziali(String destinatario,
                                        String nome,
                                        String username,
                                        String passwordTemporanea,
                                        String ruolo) {

        System.out.println("====================================");
        System.out.println("INVIO CREDENZIALI");
        System.out.println("Destinatario: " + destinatario);
        System.out.println("Ciao " + nome + ",");
        System.out.println("Account creato per SoccorsoWeb.");
        System.out.println("Ruolo: " + ruolo);
        System.out.println("Username/email: " + username);
        System.out.println("Password temporanea: " + passwordTemporanea);
        System.out.println("====================================");
    }
}