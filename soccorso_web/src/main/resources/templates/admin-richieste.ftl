<!DOCTYPE html>

<html lang="it">

<head>
    <meta charset="UTF-8">

```
<meta
    name="viewport"
    content="width=device-width, initial-scale=1.0"
>

<title>Gestione Richieste - SoccorsoWeb</title>

<style>
    * {
        box-sizing: border-box;
    }

    body {
        margin: 0;
        padding: 0;
        font-family: Arial, Helvetica, sans-serif;
        background: #f4f6f8;
        color: #222;
    }

    header {
        padding: 35px 20px;
        background: linear-gradient(
            135deg,
            #b71c1c,
            #d32f2f
        );
        color: white;
        text-align: center;
    }

    header h1 {
        margin: 0;
        font-size: 38px;
    }

    header p {
        margin: 10px 0 0;
    }

    nav {
        padding: 14px;
        background: #8e0000;
        text-align: center;
    }

    nav a {
        display: inline-block;
        margin: 4px 10px;
        padding: 8px 12px;
        border-radius: 6px;
        color: white;
        font-weight: bold;
        text-decoration: none;
    }

    nav a:hover {
        background: rgba(255, 255, 255, 0.18);
    }

    main {
        max-width: 1250px;
        margin: 35px auto;
        padding: 0 20px;
    }

    .panel {
        padding: 30px;
        border-radius: 14px;
        background: white;
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.10);
    }

    .panel h2 {
        margin-top: 0;
        color: #b71c1c;
    }

    .tabella-contenitore {
        width: 100%;
        overflow-x: auto;
    }

    table {
        width: 100%;
        min-width: 1050px;
        margin-top: 25px;
        border-collapse: collapse;
    }

    th {
        padding: 12px;
        background: #b71c1c;
        color: white;
        text-align: left;
    }

    td {
        padding: 12px;
        border-bottom: 1px solid #ddd;
        vertical-align: top;
    }

    tr:hover {
        background: #fafafa;
    }

    .stato {
        font-weight: bold;
    }

    .stato-attiva {
        color: #2e7d32;
    }

    .stato-in-corso {
        color: #ef6c00;
    }

    .stato-chiusa {
        color: #616161;
    }

    .stato-form {
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .stato-form select {
        min-width: 115px;
        padding: 8px;
        border: 1px solid #aaa;
        border-radius: 5px;
        background: white;
        font-size: 14px;
    }

    .stato-form button {
        padding: 9px 14px;
        border: none;
        border-radius: 5px;
        background: #b71c1c;
        color: white;
        font-weight: bold;
        cursor: pointer;
    }

    .stato-form button:hover {
        background: #8e0000;
    }

    .azioni {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
    }

    .btn {
        display: inline-block;
        min-width: 125px;
        padding: 9px 14px;
        border-radius: 5px;
        color: white;
        font-weight: bold;
        text-align: center;
        text-decoration: none;
    }

    .btn-dettaglio {
        background: #455a64;
    }

    .btn-dettaglio:hover {
        background: #263238;
    }

    .btn-missione {
        background: #1565c0;
    }

    .btn-missione:hover {
        background: #0d47a1;
    }

    .missione-non-disponibile {
        color: #777;
        font-size: 14px;
        font-style: italic;
    }

    .msg-ok {
        margin-bottom: 20px;
        padding: 14px;
        border-left: 6px solid #2e7d32;
        border-radius: 6px;
        background: #e8f5e9;
        color: #1b5e20;
    }

    .msg-errore {
        margin-bottom: 20px;
        padding: 14px;
        border-left: 6px solid #b71c1c;
        border-radius: 6px;
        background: #ffebee;
        color: #b71c1c;
    }

    .vuoto {
        margin-top: 20px;
        padding: 20px;
        border-radius: 8px;
        background: #f7f7f7;
    }

    footer {
        padding: 25px;
        color: #666;
        font-size: 14px;
        text-align: center;
    }

    @media screen and (max-width: 850px) {
        .stato-form {
            flex-direction: column;
            align-items: stretch;
        }

        nav a {
            display: block;
            margin: 6px 0;
        }
    }
</style>
```

</head>

<body>

<header>
    <h1>Gestione Richieste</h1>

```
<p>
    Benvenuto ${(nome!"Amministratore")?html}
    - Ruolo: ${(ruolo!"ADMIN")?html}
</p>
```

</header>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

```
<a href="${contextPath}/admin/richieste">
    Richieste
</a>

<a href="${contextPath}/admin/missioni">
    Missioni
</a>

<a href="${contextPath}/admin/nuovo-utente">
    Gestione Utenti
</a>

<a href="${contextPath}/mezzi">
    Mezzi
</a>

<a href="${contextPath}/materiali">
    Materiali
</a>

<a href="${contextPath}/operatori">
    Operatori
</a>

<a href="${contextPath}/logout">
    Logout
</a>
```

</nav>

<main>

```
<section class="panel">

    <h2>Richieste ricevute</h2>

    <p>
        Da questa pagina puoi controllare le richieste,
        modificarne lo stato e creare una missione per
        quelle attive.
    </p>

    <#if ok?? && ok?has_content>

        <div class="msg-ok">
            Stato aggiornato correttamente.
        </div>

    </#if>

    <#if successo?? && successo?has_content>

        <div class="msg-ok">

            <#if successo == "missione_creata"
                || successo == "creata">

                Missione creata correttamente.

            <#elseif successo == "stato_aggiornato">

                Stato aggiornato correttamente.

            <#else>

                Operazione completata correttamente.

            </#if>

        </div>

    </#if>

    <#if errore?? && errore?has_content>

        <div class="msg-errore">

            <#if errore == "stato_non_valido">

                Lo stato selezionato non è valido.

            <#elseif errore == "aggiornamento_fallito">

                Non è stato possibile aggiornare lo stato
                della richiesta.

            <#elseif errore == "richiesta_non_valida">

                La richiesta selezionata non è valida.

            <#elseif errore == "richiesta_non_trovata">

                La richiesta selezionata non esiste.

            <#elseif errore == "richiesta_non_attiva">

                La missione può essere creata solamente
                per una richiesta attiva.

            <#elseif errore == "missione_esistente">

                Per questa richiesta esiste già una missione.

            <#elseif errore == "campi">

                Compila tutti i campi obbligatori.

            <#elseif errore == "caposquadra">

                Seleziona un caposquadra.

            <#elseif errore == "caposquadra_non_trovato">

                Il caposquadra selezionato non esiste.

            <#elseif errore == "caposquadra_non_attivo">

                Il caposquadra selezionato non è attivo.

            <#elseif errore == "caposquadra_occupato">

                Il caposquadra selezionato è già assegnato
                a un'altra squadra.

            <#elseif errore == "operatore_non_caposquadra">

                L'operatore selezionato non può essere
                utilizzato come caposquadra.

            <#elseif errore == "operatore_non_disponibile">

                Uno degli operatori selezionati non è
                disponibile.

            <#elseif errore == "mezzo_non_disponibile">

                Uno dei mezzi selezionati non è disponibile.

            <#elseif errore == "materiale_non_disponibile">

                Uno dei materiali selezionati non è
                disponibile.

            <#else>

                Si è verificato un errore durante
                l'operazione.

            </#if>

        </div>

    </#if>

    <#if richieste?? && richieste?has_content>

        <div class="tabella-contenitore">

            <table>

                <thead>
                <tr>
                    <th>Email</th>
                    <th>Descrizione</th>
                    <th>Indirizzo</th>
                    <th>Stato attuale</th>
                    <th>Cambia stato</th>
                    <th>Azioni</th>
                </tr>
                </thead>

                <tbody>

                <#list richieste as r>

                    <#assign statoRichiesta =
                        (r.stato!"")
                        ?string
                        ?upper_case
                        ?replace(" ", "_")
                    >

                    <tr>

                        <td>
                            ${(r.email_segnalante!"Non disponibile")?html}
                        </td>

                        <td>
                            ${(r.descrizione!"Nessuna descrizione")?html}
                        </td>

                        <td>
                            ${(r.indirizzo!"Non indicato")?html}
                        </td>

                        <td>

                            <#if statoRichiesta == "ATTIVA">

                                <span class="stato stato-attiva">
                                    ATTIVA
                                </span>

                            <#elseif statoRichiesta == "IN_CORSO">

                                <span class="stato stato-in-corso">
                                    IN CORSO
                                </span>

                            <#elseif statoRichiesta == "CHIUSA">

                                <span class="stato stato-chiusa">
                                    CHIUSA
                                </span>

                            <#else>

                                <span class="stato">
                                    ${(statoRichiesta!"NON DEFINITO")?html}
                                </span>

                            </#if>

                        </td>

                        <td>

                            <form
                                action="${contextPath}/admin/cambia-stato"
                                method="post"
                                class="stato-form"
                            >

                                <input
                                    type="hidden"
                                    name="email_segnalante"
                                    value="${(r.email_segnalante!"")?html}"
                                >

                                <select
                                    name="stato"
                                    required
                                    aria-label="Nuovo stato richiesta"
                                >

                                    <option
                                        value="ATTIVA"
                                        <#if statoRichiesta == "ATTIVA">
                                            selected
                                        </#if>
                                    >
                                        Attiva
                                    </option>

                                    <option
                                        value="IN_CORSO"
                                        <#if statoRichiesta == "IN_CORSO">
                                            selected
                                        </#if>
                                    >
                                        In corso
                                    </option>

                                    <option
                                        value="CHIUSA"
                                        <#if statoRichiesta == "CHIUSA">
                                            selected
                                        </#if>
                                    >
                                        Chiusa
                                    </option>

                                </select>

                                <button type="submit">
                                    Aggiorna
                                </button>

                            </form>

                        </td>

                        <td>

                            <div class="azioni">

                                <#if statoRichiesta == "ATTIVA">

                                    <a
                                        class="btn btn-missione"
                                        href="${contextPath}/admin/missioni/nuova?richiestaId=${(r.email_segnalante!"")?url('UTF-8')}"
                                    >
                                        Crea missione
                                    </a>

                                <#else>

                                    <span class="missione-non-disponibile">
                                        Missione non creabile
                                    </span>

                                </#if>

                            </div>

                        </td>

                    </tr>

                </#list>

                </tbody>

            </table>

        </div>

    <#else>

        <div class="vuoto">
            Nessuna richiesta presente nel database.
        </div>

    </#if>

</section>


</main>

<footer>
    <p>
        &copy; 2026 SoccorsoWeb - Progetto Web Engineering
    </p>
</footer>

</body>
</html>
