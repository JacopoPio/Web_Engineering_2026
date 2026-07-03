<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        ${(titolo!"SoccorsoWeb")?html}
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
            background: #d32f2f;
            color: white;
            text-align: center;
        }

        header h1 {
            margin: 0;
        }

        header p {
            margin: 8px 0 0;
        }

        nav {
            padding: 12px;
            background: #b71c1c;
            text-align: center;
        }

        nav a {
            display: inline-block;
            margin: 5px 12px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: min(900px, 94%);
            margin: 30px auto;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 5px 18px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }

        input,
        textarea {
            width: 100%;
            margin-top: 6px;
            padding: 11px;
            border: 1px solid #aaa;
            border-radius: 6px;
            font: inherit;
        }

        textarea {
            resize: vertical;
        }

        input:focus,
        textarea:focus {
            outline: 2px solid #ef9a9a;
            border-color: #d32f2f;
        }

        button {
            margin-top: 20px;
            padding: 12px 18px;
            border: 0;
            border-radius: 6px;
            background: #d32f2f;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #b71c1c;
        }

        .errore {
            margin-bottom: 18px;
            padding: 13px;
            border-left: 6px solid #b71c1c;
            border-radius: 6px;
            background: #ffebee;
            color: #b71c1c;
        }

        .nota {
            color: #555;
            line-height: 1.5;
        }

        .captcha-box {
            margin-top: 18px;
            padding: 15px;
            border: 1px solid #ffcdd2;
            border-radius: 8px;
            background: #fff5f5;
        }

        .captcha-box label {
            margin-top: 0;
        }

        .campo-obbligatorio {
            color: #b71c1c;
        }

        @media (max-width: 600px) {
            main {
                padding: 20px;
            }

            nav a {
                display: block;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>
        ${(titolo!"SoccorsoWeb")?html}
    </h1>

    <p>
        ${(messaggio!"Portale per la gestione delle richieste")?html}
    </p>
</header>

<nav>
    <a href="${contextPath}/home">
        Home
    </a>

    <a href="${contextPath}/servizi">
        Servizi
    </a>

    <a href="${contextPath}/login">
        Area riservata
    </a>
</nav>

<main>

    <h2>Invia una richiesta di soccorso</h2>

    <#if errore??>

        <div class="errore"
             role="alert"
             aria-live="assertive">

            <#if errore == "campi">

                Compila tutti i campi obbligatori.

            <#elseif errore == "email">

                Inserisci un indirizzo email valido.

            <#elseif errore == "captcha">

                Il risultato del controllo antispam non è corretto.
                Inserisci il risultato della nuova operazione mostrata.

            <#elseif errore == "email_usata">

                Questa email ha già una richiesta registrata nel sistema.

            <#elseif errore == "ip_recente">

                È già stata inviata una richiesta recente dallo stesso indirizzo IP.

            <#elseif errore == "foto">

                Il formato o la dimensione della foto non sono validi.

            <#else>

                Non è stato possibile inviare la richiesta.

            </#if>
        </div>

    </#if>

    <p class="nota">
        Dopo l'invio riceverai un'email contenente il collegamento
        necessario per rendere attiva la richiesta.
    </p>

    <form action="${contextPath}/richiesta-invio"
          method="post"
          enctype="multipart/form-data">

        <label for="descrizione">
            Descrizione
            <span class="campo-obbligatorio">*</span>
        </label>

        <textarea id="descrizione"
                  name="descrizione"
                  rows="5"
                  maxlength="1000"
                  placeholder="Descrivi il problema e il tipo di soccorso necessario"
                  required></textarea>

        <label for="posizione">
            Posizione
            <span class="campo-obbligatorio">*</span>
        </label>

        <input id="posizione"
               name="posizione"
               type="text"
               maxlength="255"
               placeholder="Indirizzo o luogo dell'emergenza"
               required>

        <label for="emailSegnalante">
            Email del segnalante
            <span class="campo-obbligatorio">*</span>
        </label>

        <input id="emailSegnalante"
               name="emailSegnalante"
               type="email"
               maxlength="50"
               autocomplete="email"
               placeholder="nome@example.com"
               required>

        <label for="nomeSegnalante">
            Nome del segnalante
            <span class="campo-obbligatorio">*</span>
        </label>

        <input id="nomeSegnalante"
               name="nomeSegnalante"
               type="text"
               maxlength="150"
               autocomplete="name"
               placeholder="Inserisci il tuo nome"
               required>

        <label for="foto">
            Foto opzionale
        </label>

        <input id="foto"
               name="foto"
               type="file"
               accept="image/jpeg,image/png,image/webp">

        <p class="nota">
            Formati consentiti: JPG, PNG e WEBP.
            Dimensione massima: 5 MB.
        </p>

        <div class="captcha-box">

            <label for="captcha">
                Controllo antispam:
                quanto fa ${captchaA} + ${captchaB}?
                <span class="campo-obbligatorio">*</span>
            </label>

            <!--
                Identifica esattamente l'operazione mostrata.
                Deve essere inviato insieme alla risposta.
            -->
            <input type="hidden"
                   name="captchaId"
                   value="${captchaId?html}">

            <input id="captcha"
                   name="captcha"
                   type="number"
                   min="0"
                   step="1"
                   inputmode="numeric"
                   autocomplete="off"
                   placeholder="Inserisci il risultato"
                   required>
        </div>

        <button type="submit">
            Invia richiesta
        </button>

    </form>

</main>

</body>
</html>