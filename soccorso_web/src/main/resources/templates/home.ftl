<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${(titolo!"SoccorsoWeb")?html}</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; color: #222; }
        header { background: #d32f2f; color: white; padding: 30px 20px; text-align: center; }
        nav { background: #b71c1c; padding: 12px; text-align: center; }
        nav a { color: white; margin: 0 12px; font-weight: bold; text-decoration: none; }
        main { width: min(900px, 94%); margin: 30px auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 5px 18px rgba(0,0,0,.1); }
        label { display: block; margin-top: 15px; font-weight: bold; }
        input, textarea { width: 100%; padding: 11px; margin-top: 6px; border: 1px solid #aaa; border-radius: 6px; }
        button { margin-top: 20px; padding: 12px 18px; background: #d32f2f; color: white; border: 0; border-radius: 6px; cursor: pointer; font-weight: bold; }
        .errore { margin-bottom: 18px; padding: 13px; border-left: 6px solid #b71c1c; background: #ffebee; color: #b71c1c; border-radius: 6px; }
        .nota { color: #555; line-height: 1.5; }
    </style>
</head>
<body>
<header>
    <h1>${(titolo!"SoccorsoWeb")?html}</h1>
    <p>${(messaggio!"Portale per la gestione delle richieste")?html}</p>
</header>
<nav>
    <a href="${contextPath}/home">Home</a>
    <a href="${contextPath}/servizi">Servizi</a>
    <a href="${contextPath}/login">Area riservata</a>
</nav>
<main>
    <h2>Invia una richiesta di soccorso</h2>

    <#if errore??>
        <div class="errore">
            <#if errore == "campi">Compila tutti i campi obbligatori.
            <#elseif errore == "email">Inserisci un indirizzo email valido.
            <#elseif errore == "captcha">Il risultato del controllo antispam non è corretto.
            <#elseif errore == "email_usata">Questa email ha già una richiesta registrata nel sistema.
            <#elseif errore == "ip_recente">È già stata inviata una richiesta recente dallo stesso indirizzo IP.
            <#else>Non è stato possibile inviare la richiesta.</#if>
        </div>
    </#if>

    <p class="nota">Dopo l'invio riceverai un'email contenente il collegamento necessario per rendere attiva la richiesta.</p>

    <form action="${contextPath}/richiesta-invio" method="post" enctype="multipart/form-data">
        <label for="descrizione">Descrizione</label>
        <textarea id="descrizione" name="descrizione" rows="5" maxlength="1000" required></textarea>

        <label for="posizione">Posizione</label>
        <input id="posizione" name="posizione" type="text" maxlength="255" required>

        <label for="emailSegnalante">Email segnalante</label>
        <input id="emailSegnalante" name="emailSegnalante" type="email" maxlength="50" required>

        <label for="nomeSegnalante">Nome segnalante</label>
        <input id="nomeSegnalante" name="nomeSegnalante" type="text" maxlength="150" required>

        <label for="foto">Foto opzionale (JPG, PNG o WEBP, massimo 5 MB)</label>
        <input id="foto" name="foto" type="file" accept="image/jpeg,image/png,image/webp">

        <label for="captcha">Quanto fa ${captchaA} + ${captchaB}?</label>
        <input id="captcha" name="captcha" type="number" min="0" required>

        <button type="submit">Invia richiesta</button>
    </form>
</main>
</body>
</html>
