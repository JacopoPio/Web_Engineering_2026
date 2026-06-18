```ftl
<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">

    <meta
        name="viewport"
        content="width=device-width, initial-scale=1.0"
    >

    <title>
        ${(titoloPagina!"Gestione utente")?html}
    </title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f8;
            color: #222;
        }

        header {
            padding: 30px 20px;
            background: linear-gradient(135deg, #1565c0, #1976d2);
            color: white;
            text-align: center;
        }

        header h1 {
            margin: 0 0 8px;
        }

        header p {
            margin: 0;
        }

        nav {
            padding: 13px;
            background: #0d47a1;
            text-align: center;
        }

        nav a {
            display: inline-block;
            margin: 4px 10px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: min(950px, 94%);
            margin: 30px auto;
        }

        .contenitore {
            padding: 25px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .messaggio {
            margin-bottom: 20px;
            padding: 14px 16px;
            border-radius: 6px;
            font-weight: bold;
        }

        .successo {
            border: 1px solid #86c99a;
            background: #dff3e5;
            color: #166534;
        }

        .errore {
            border: 1px solid #ef9a9a;
            background: #fde2e2;
            color: #991b1b;
        }

        .griglia {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 18px;
        }

        .campo {
            margin-bottom: 18px;
        }

        .campo-intero {
            grid-column: 1 / -1;
        }

        label {
            display: block;
            margin-bottom: 7px;
            font-weight: bold;
        }

        input,
        select {
            width: 100%;
            padding: 11px;
            border: 1px solid #b8c2cc;
            border-radius: 6px;
            font: inherit;
            background: white;
        }

        input:focus,
        select:focus {
            outline: 2px solid #90caf9;
            border-color: #1565c0;
        }

        input[readonly] {
            background: #edf1f5;
            color: #555;
        }

        fieldset {
            margin: 22px 0;
            padding: 18px;
            border: 1px solid #c7d1da;
            border-radius: 8px;
        }

        legend {
            padding: 0 8px;
            color: #0d47a1;
            font-weight: bold;
        }

        .lista-checkbox {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .checkbox-label {
            display: inline-flex;
            align-items: center;
            gap: 7px;
            margin: 0;
            padding: 9px 13px;
            border: 1px solid #d1d9e0;
            border-radius: 6px;
            background: #f8fafc;
            font-weight: normal;
            cursor: pointer;
        }

        .checkbox-label:hover {
            background: #eaf2fb;
        }

        .checkbox-label input {
            width: auto;
            margin: 0;
        }

        .nessun-elemento {
            margin: 0;
            color: #666;
            font-style: italic;
        }

        .azioni {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 25px;
        }

        button,
        .pulsante {
            display: inline-block;
            padding: 11px 20px;
            border: none;
            border-radius: 6px;
            background: #1565c0;
            color: white;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
        }

        button:hover,
        .pulsante:hover {
            background: #0d47a1;
        }

        .pulsante-secondario {
            background: #607d8b;
        }

        .pulsante-secondario:hover {
            background: #455a64;
        }

        .obbligatorio {
            color: #c62828;
        }

        small {
            display: block;
            margin-top: 5px;
            color: #666;
        }

        footer {
            margin-top: 35px;
            padding: 20px;
            background: #263238;
            color: white;
            text-align: center;
        }

        @media (max-width: 700px) {
            .griglia {
                grid-template-columns: 1fr;
            }

            .campo-intero {
                grid-column: auto;
            }

            .azioni {
                flex-direction: column;
            }

            button,
            .pulsante {
                width: 100%;
                text-align: center;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>
        ${(titoloPagina!"Gestione utente")?html}
    </h1>

    <#if nomeAdmin??>
        <p>
            Amministratore: ${nomeAdmin?html}
        </p>
    </#if>
</header>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

    <a href="${contextPath}/admin/utenti">
        Gestione utenti
    </a>

    <a href="${contextPath}/admin/nuovo-utente">
        Nuovo utente
    </a>

    <a href="${contextPath}/logout">
        Logout
    </a>
</nav>

<main>

    <#-- Messaggio di successo passato con data.put("successo", true) -->
    <#if successo?? && successo>
        <div class="messaggio successo">
            Utente creato correttamente.
            Le credenziali temporanee sono state generate.
        </div>
    </#if>

    <#-- Messaggi di errore -->
    <#if errore??>
        <div class="messaggio errore">

            <#if errore == "campi">
                Compila tutti i campi obbligatori.

            <#elseif errore == "cf_mancante">
                Il codice fiscale è obbligatorio.

            <#elseif errore == "cf_non_valido">
                Il codice fiscale inserito non rispetta il formato previsto.

            <#elseif errore == "ruolo">
                Il ruolo selezionato non è valido.

            <#elseif errore == "data">
                La data di nascita inserita non è valida.

            <#elseif errore == "email">
                Esiste già un amministratore o un operatore con questa email.

            <#elseif errore == "selezione_non_valida">
                È stata selezionata una patente o un'abilità non valida.

            <#elseif errore == "utente_non_trovato">
                L'utente richiesto non è stato trovato.

            <#else>
                Si è verificato un errore durante l'operazione.
            </#if>

        </div>
    </#if>

    <section class="contenitore">

        <h2>
            <#if (modalita!"creazione") == "modifica">
                Modifica i dati dell'utente
            <#else>
                Inserisci i dati del nuovo utente
            </#if>
        </h2>

        <form
            method="post"
            action="${formAction?html}"
        >

            <input
                type="hidden"
                name="azione"
                value="${azioneForm?html}"
            >

            <div class="griglia">

                <#-- Ruolo -->
                <div class="campo">

                    <#if (modalita!"creazione") == "modifica">

                        <label for="ruolo_visualizzato">
                            Ruolo
                        </label>

                        <input
                            type="text"
                            id="ruolo_visualizzato"
                            value="${(ruoloUtente!"")?html}"
                            readonly
                        >

                        <input
                            type="hidden"
                            name="ruolo"
                            value="${(ruoloUtente!"")?html}"
                        >

                    <#else>

                        <label for="ruolo">
                            Ruolo
                            <span class="obbligatorio">*</span>
                        </label>

                        <select
                            id="ruolo"
                            name="ruolo"
                            required
                        >
                            <option value="">
                                Seleziona un ruolo
                            </option>

                            <option
                                value="ADMIN"
                                <#if (ruoloUtente!"") == "ADMIN">
                                    selected
                                </#if>
                            >
                                Amministratore
                            </option>

                            <option
                                value="OPERATORE"
                                <#if (ruoloUtente!"") == "OPERATORE">
                                    selected
                                </#if>
                            >
                                Operatore
                            </option>
                        </select>

                    </#if>

                </div>

                <#-- Email -->
                <div class="campo">

                    <label for="email">
                        Email
                        <span class="obbligatorio">*</span>
                    </label>

                    <#if (modalita!"creazione") == "modifica">

                        <input
                            type="email"
                            id="email"
                            value="${(emailUtente!"")?html}"
                            readonly
                        >

                        <input
                            type="hidden"
                            name="email"
                            value="${(emailUtente!"")?html}"
                        >

                        <small>
                            L'email non può essere modificata.
                        </small>

                    <#else>

                        <input
                            type="email"
                            id="email"
                            name="email"
                            value="${(emailUtente!"")?html}"
                            autocomplete="email"
                            required
                        >

                    </#if>

                </div>

                <#-- Nome -->
                <div class="campo">
                    <label for="nome">
                        Nome
                        <span class="obbligatorio">*</span>
                    </label>

                    <input
                        type="text"
                        id="nome"
                        name="nome"
                        value="${(nomeUtente!"")?html}"
                        maxlength="100"
                        autocomplete="given-name"
                        required
                    >
                </div>

                <#-- Cognome -->
                <div class="campo">
                    <label for="cognome">
                        Cognome
                        <span class="obbligatorio">*</span>
                    </label>

                    <input
                        type="text"
                        id="cognome"
                        name="cognome"
                        value="${(cognomeUtente!"")?html}"
                        maxlength="100"
                        autocomplete="family-name"
                        required
                    >
                </div>

                <#-- Codice fiscale -->
                <div class="campo">
                    <label for="cf">
                        Codice fiscale
                        <span class="obbligatorio">*</span>
                    </label>

                    <input
                        type="text"
                        id="cf"
                        name="cf"
                        value="${(cfUtente!"")?html}"
                        minlength="16"
                        maxlength="16"
                        pattern="[A-Za-z]{6}[0-9]{2}[A-Za-z][0-9]{2}[A-Za-z][0-9]{3}[A-Za-z]"
                        title="Inserire un codice fiscale di 16 caratteri"
                        required
                    >
                </div>

                <#-- Data di nascita -->
                <div class="campo">
                    <label for="data_nascita">
                        Data di nascita
                    </label>

                    <input
                        type="date"
                        id="data_nascita"
                        name="data_nascita"
                        value="${(dataNascitaUtente!"")?html}"
                    >
                </div>

                <#-- Città di nascita -->
                <div class="campo">
                    <label for="citta_nascita">
                        Città di nascita
                    </label>

                    <input
                        type="text"
                        id="citta_nascita"
                        name="citta_nascita"
                        value="${(cittaNascitaUtente!"")?html}"
                        maxlength="100"
                    >
                </div>

                <#-- Indirizzo -->
                <div class="campo">
                    <label for="indirizzo">
                        Indirizzo
                    </label>

                    <input
                        type="text"
                        id="indirizzo"
                        name="indirizzo"
                        value="${(indirizzoUtente!"")?html}"
                        maxlength="255"
                        autocomplete="street-address"
                    >
                </div>

            </div>

            <#-- PATENTI: la servlet usa data.put("listaPatenti", listaPatenti) -->
            <fieldset>
                <legend>
                    Patenti possedute
                </legend>

                <#if listaPatenti?? && listaPatenti?size gt 0>

                    <div class="lista-checkbox">

                        <#list listaPatenti as patente>

                            <label class="checkbox-label">

                                <input
                                    type="checkbox"
                                    name="patenti"
                                    value="${patente.tipoPatente?html}"

                                    <#if patentiSelezionate??
                                        && patentiSelezionate?seq_contains(
                                            patente.tipoPatente
                                        )>
                                        checked
                                    </#if>
                                >

                                ${patente.tipoPatente?html}

                            </label>

                        </#list>

                    </div>

                <#else>

                    <p class="nessun-elemento">
                        Nessuna patente presente nel database.
                    </p>

                </#if>
            </fieldset>

            <#-- ABILITÀ: la servlet usa data.put("listaAbilita", abilita) -->
            <fieldset>
                <legend>
                    Abilità possedute
                </legend>

                <#if listaAbilita?? && listaAbilita?size gt 0>

                    <div class="lista-checkbox">

                        <#list listaAbilita as abilita>

                            <label class="checkbox-label">

                                <input
                                    type="checkbox"
                                    name="abilita"
                                    value="${abilita.nome?html}"

                                    <#if abilitaSelezionate??
                                        && abilitaSelezionate?seq_contains(
                                            abilita.nome
                                        )>
                                        checked
                                    </#if>
                                >

                                ${abilita.nome?html}

                            </label>

                        </#list>

                    </div>

                <#else>

                    <p class="nessun-elemento">
                        Nessuna abilità presente nel database.
                    </p>

                </#if>
            </fieldset>

            <div class="azioni">

                <button type="submit">

                    <#if (modalita!"creazione") == "modifica">
                        Salva modifiche
                    <#else>
                        Crea utente
                    </#if>

                </button>

                <#if (modalita!"creazione") == "modifica">

                    <a
                        class="pulsante pulsante-secondario"
                        href="${contextPath}/admin/utenti"
                    >
                        Annulla
                    </a>

                <#else>

                    <a
                        class="pulsante pulsante-secondario"
                        href="${contextPath}/admin"
                    >
                        Torna alla dashboard
                    </a>

                </#if>

            </div>

        </form>

    </section>

</main>

<footer>
    SoccorsoWeb – Area amministratore
</footer>

</body>
</html>
```
