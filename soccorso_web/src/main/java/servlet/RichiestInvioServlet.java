package servlet;

import dao.DaoInterfaceRichiesta;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta_configuration.resources.JPAUtil;
import jakarta_configuration.resources.MailUtil;
import model.Richiesta;
import model.StatoRichiesta;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@WebServlet(
        name = "RichiestaInvioServlet",
        urlPatterns = {"/richiesta-invio"}
)
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class RichiestInvioServlet extends HttpServlet {

    /*
     * Accetta solamente richieste POST provenienti
     * dal form della home.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        /*
         * Il CAPTCHA viene controllato prima di salvare
         * qualsiasi dato o file.
         */
        if (!captchaValido(request)) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/home?errore=captcha"
            );
            return;
        }

        String descrizione =
                normalizza(
                        request.getParameter("descrizione")
                );

        String indirizzo =
                normalizza(
                        request.getParameter("posizione")
                );

        String emailSegnalante =
                normalizza(
                        request.getParameter("emailSegnalante")
                ).toLowerCase();

        String nomeSegnalante =
                normalizza(
                        request.getParameter("nomeSegnalante")
                );

        /*
         * Controllo dei campi obbligatori.
         */
        if (descrizione.isBlank()
                || indirizzo.isBlank()
                || emailSegnalante.isBlank()
                || nomeSegnalante.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/home?errore=campi"
            );
            return;
        }

        /*
         * Controllo semplice del formato email.
         */
        if (!emailSegnalante.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
        )) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/home?errore=email"
            );
            return;
        }

        byte[] ipOrigine =
                ricavaIndirizzoIp(request);

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        try {
            DaoInterfaceRichiesta daoRichiesta =
                    new DaoInterfaceRichiestaImpl(
                            entityManager
                    );

            /*
             * Nel model attuale email_segnalante è la
             * chiave primaria, quindi la stessa email
             * non può avere più richieste.
             */
            Richiesta richiestaEsistente =
                    daoRichiesta.findByEmail(
                            emailSegnalante
                    );

            if (richiestaEsistente != null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/home?errore=email_usata"
                );
                return;
            }

            /*
             * Controllo facoltativo contro richieste troppo
             * ravvicinate provenienti dallo stesso IP.
             *
             * Attualmente impedisce una nuova richiesta dallo
             * stesso IP nei 10 minuti precedenti.
             */
            boolean ipRecente =
                    daoRichiesta.existsIpRecente(
                            ipOrigine,
                            LocalDateTime.now()
                                    .minusMinutes(10)
                    );

            if (ipRecente) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/home?errore=ip_recente"
                );
                return;
            }

            String pathFoto;

            try {
                pathFoto = salvaFoto(request);

            } catch (ServletException e) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/home?errore=foto"
                );
                return;
            }

            /*
             * Due UUID senza trattini producono
             * un token lungo 64 caratteri.
             */
            String tokenConferma =
                    generaTokenConferma();

            /*
             * La richiesta viene salvata inizialmente
             * nello stato "da confermare".
             *
             * Diventerà "attiva" soltanto dopo che
             * l'utente avrà aperto il link ricevuto.
             */
            Richiesta richiesta = new Richiesta(
                    emailSegnalante,
                    descrizione,
                    indirizzo,
                    StatoRichiesta.DA_CONFERMARE,
                    pathFoto,
                    nomeSegnalante,
                    ipOrigine
            );

            richiesta.setTokenConferma(
                    tokenConferma
            );

            /*
             * Il costruttore di Richiesta dovrebbe già
             * impostare la data di creazione.
             *
             * Questo controllo evita comunque che resti null.
             */
            if (richiesta.getDataCreazione() == null) {
                richiesta.setDataCreazione(
                        LocalDateTime.now()
                );
            }

            daoRichiesta.save(
                    richiesta
            );

            String linkConferma =
                    costruisciLinkConferma(
                            request,
                            tokenConferma
                    );

            /*
             * MailUtil invia il link necessario per
             * impostare la richiesta come attiva.
             */
            MailUtil.inviaConfermaRichiesta(
                    emailSegnalante,
                    nomeSegnalante,
                    linkConferma
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/richiesta-inviata"
            );

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore durante il salvataggio "
                    + "della richiesta",
                    e
            );

        } finally {

            if (entityManager != null
                    && entityManager.isOpen()) {

                entityManager.close();
            }
        }
    }

    /*
     * Impedisce di aprire direttamente la Servlet
     * tramite una richiesta GET.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/home"
        );
    }

    /*
     * Controlla il CAPTCHA generato da HomeServlet.
     *
     * HomeServlet conserva nella sessione una mappa:
     *
     * captchaId -> risultato corretto
     */
    private boolean captchaValido(
            HttpServletRequest request
    ) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        String captchaId =
                normalizza(
                        request.getParameter("captchaId")
                );

        String risposta =
                normalizza(
                        request.getParameter("captcha")
                );

        if (captchaId.isBlank()
                || risposta.isBlank()) {

            return false;
        }

        Integer risultatoCorretto;

        synchronized (session) {

            Object valore =
                    session.getAttribute(
                            "captchaSalvati"
                    );

            if (!(valore instanceof Map<?, ?>)) {
                return false;
            }

            @SuppressWarnings("unchecked")
            Map<String, Integer> captchaSalvati =
                    (Map<String, Integer>) valore;

            /*
             * Rimuove il CAPTCHA dalla sessione.
             * In questo modo può essere usato una sola volta.
             */
            risultatoCorretto =
                    captchaSalvati.remove(
                            captchaId
                    );
        }

        if (risultatoCorretto == null) {
            return false;
        }

        try {
            int risultatoInserito =
                    Integer.parseInt(
                            risposta
                    );

            return risultatoInserito
                    == risultatoCorretto;

        } catch (NumberFormatException e) {

            return false;
        }
    }

    private String generaTokenConferma() {

        String primaParte =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        String secondaParte =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        return primaParte + secondaParte;
    }

    private byte[] ricavaIndirizzoIp(
            HttpServletRequest request
    ) throws IOException {

        String indirizzoIp =
                request.getRemoteAddr();

        /*
         * Converte l'indirizzo localhost IPv6
         * nel corrispondente indirizzo IPv4.
         */
        if ("0:0:0:0:0:0:0:1"
                .equals(indirizzoIp)
                || "::1".equals(indirizzoIp)) {

            return new byte[]{
                127, 0, 0, 1
            };
        }

        return InetAddress
                .getByName(indirizzoIp)
                .getAddress();
    }

    private String costruisciLinkConferma(
            HttpServletRequest request,
            String token
    ) {

        String schema =
                request.getScheme();

        String host =
                request.getServerName();

        int porta =
                request.getServerPort();

        StringBuilder link =
                new StringBuilder();

        link.append(schema)
                .append("://")
                .append(host);

        boolean portaHttpStandard =
                "http".equalsIgnoreCase(schema)
                && porta == 80;

        boolean portaHttpsStandard =
                "https".equalsIgnoreCase(schema)
                && porta == 443;

        if (!portaHttpStandard
                && !portaHttpsStandard) {

            link.append(":")
                    .append(porta);
        }

        link.append(
                request.getContextPath()
        );

        link.append(
                "/conferma-richiesta?token="
        );

        link.append(
                URLEncoder.encode(
                        token,
                        StandardCharsets.UTF_8
                )
        );

        return link.toString();
    }

    private String salvaFoto(
            HttpServletRequest request
    ) throws IOException, ServletException {

        Part fotoPart =
                request.getPart("foto");

        /*
         * La foto è facoltativa.
         */
        if (fotoPart == null
                || fotoPart.getSize() == 0) {

            return null;
        }

        /*
         * Controllo aggiuntivo della dimensione.
         * @MultipartConfig impedisce comunque di
         * superare 5 MB.
         */
        if (fotoPart.getSize()
                > 5L * 1024L * 1024L) {

            throw new ServletException(
                    "Foto troppo grande"
            );
        }

        String contentType =
                fotoPart.getContentType();

        String estensione;

        if ("image/jpeg".equalsIgnoreCase(
                contentType
        )) {

            estensione = ".jpg";

        } else if ("image/png".equalsIgnoreCase(
                contentType
        )) {

            estensione = ".png";

        } else if ("image/webp".equalsIgnoreCase(
                contentType
        )) {

            estensione = ".webp";

        } else {

            throw new ServletException(
                    "Formato della foto non consentito"
            );
        }

        String nomeFile =
                UUID.randomUUID()
                        .toString()
                + estensione;

        String uploadPath =
                getServletContext()
                        .getRealPath("/uploads");

        if (uploadPath == null) {

            throw new ServletException(
                    "La cartella uploads "
                    + "non è disponibile"
            );
        }

        File uploadDirectory =
                new File(uploadPath);

        if (!uploadDirectory.exists()
                && !uploadDirectory.mkdirs()) {

            throw new ServletException(
                    "Impossibile creare "
                    + "la cartella uploads"
            );
        }

        File fileDestinazione =
                new File(
                        uploadDirectory,
                        nomeFile
                );

        fotoPart.write(
                fileDestinazione
                        .getAbsolutePath()
        );

        return "uploads/" + nomeFile;
    }

    private String normalizza(
            String valore
    ) {

        return valore == null
                ? ""
                : valore.trim();
    }
}