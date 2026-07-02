<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conferma richiesta - SoccorsoWeb</title>
    <style>
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; }
        main { width: min(720px, 92%); margin: 70px auto; background: white; padding: 32px; border-radius: 12px; text-align: center; box-shadow: 0 6px 20px rgba(0,0,0,.1); }
        .ok { color: #1b5e20; }
        .errore { color: #b71c1c; }
        a { display: inline-block; margin-top: 20px; padding: 10px 17px; background: #d32f2f; color: white; text-decoration: none; border-radius: 6px; }
    </style>
</head>
<body>
<main>
    <#if esito == "confermata">
        <h1 class="ok">Richiesta confermata</h1>
        <p>La richiesta è ora attiva e può essere presa in carico dagli amministratori.</p>
    <#elseif esito == "gia_confermata">
        <h1>Richiesta già confermata</h1>
        <p>Questo collegamento è già stato utilizzato.</p>
    <#elseif esito == "token_scaduto">
        <h1 class="errore">Collegamento scaduto</h1>
        <p>Il collegamento di conferma è scaduto. Invia una nuova richiesta.</p>
    <#else>
        <h1 class="errore">Collegamento non valido</h1>
        <p>Il collegamento non corrisponde a una richiesta confermabile.</p>
    </#if>
    <a href="${contextPath}/home">Torna alla home</a>
</main>
</body>
</html>
