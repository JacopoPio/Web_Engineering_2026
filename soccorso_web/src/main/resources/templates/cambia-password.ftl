<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Cambia password - SoccorsoWeb</title>

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
            padding: 32px 20px;
            background: linear-gradient(135deg, #1565c0, #1976d2);
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
            padding: 14px;
            background: #0d47a1;
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
            width: min(560px, 94%);
            margin: 35px auto;
        }

        .pannello {
            padding: 28px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 5px 18px rgba(0, 0, 0, .1);
        }

        .pannello h2 {
            margin-top: 0;
            color: #0d47a1;
        }

        label {
            display: block;
            margin-top: 17px;
            font-weight: bold;
        }

        input {
            width: 100%;
            margin-top: 6px;
            padding: 11px;
            border: 1px solid #aaa;
            border-radius: 6px;
            font: inherit;
        }

        input:focus {
            outline: 2px solid #90caf9;
            border-color: #1565c0;
        }

        button {
            width: 100%;
            margin-top: 24px;
            padding: 12px 18px;
            border: 0;
            border-radius: 6px;
            background: #1565c0;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #0d47a1;
        }

        .messaggio {
            margin-bottom: 18px;
            padding: 14px;
            border-radius: 7px;
        }

        .successo {
            background: #e8f5e9;
            color: #1b5e20;
            border-left: 6px solid #2e7d32;
        }

        .errore {
            background: #ffebee;
            color: #b71c1c;
            border-left: 6px solid #b71c1c;
        }

        .regole {
            margin-top: 18px;
            padding: 14px;
            border-radius: 7px;
            background: #e3f2fd;
            color: #0d47a1;
            line-height: 1.5;
        }

        .regole ul {
            margin-bottom: 0;
        }
    </style>
</head>

<body>

<header>
    <h1>Cambia password</h1>

    <p>
        Utente:
        ${(nome!"Utente")?html}
    </p>
</header>

<nav>

    <#if ruolo?? && ruolo == "ADMIN">

        <a href="${contextPath}/admin">
            Dashboard
        </a>

    <#else>

        <a href="${contextPath}/operatori">
            Area operatore
        </a>

    </#if>

    <a href="${contextPath}/cambia-password">
        Cambia password
    </a>

    <a href="${contextPath}/logout">
        Logout
    </a>
</nav>

<main>

    <#if successo??>

        <div class="messaggio successo">

            <#if successo == "password_modificata">
                Password modificata correttamente.
                Da questo momento dovrai utilizzare la nuova password.
            <#else>
                Operazione completata correttamente.
            </#if>

        </div>

    </#if>

    <#if errore??>

        <div class="messaggio errore">

            <#if errore == "campi_obbligatori">
                Compila tutti i campi.

            <#elseif errore == "password_attuale_errata">
                La password attuale non è corretta.

            <#elseif errore == "password_non_coincidenti">
                La nuova password e la conferma non coincidono.

            <#elseif errore == "password_debole">
                La nuova password non rispetta i requisiti di sicurezza.

            <#elseif errore == "password_uguale">
                La nuova password deve essere diversa da quella attuale.

            <#elseif errore == "aggiornamento_fallito">
                Non è stato possibile aggiornare la password.

            <#else>
                Non è stato possibile completare l'operazione.
            </#if>

        </div>

    </#if>

    <section class="pannello">

        <h2>Imposta una nuova password</h2>

        <form action="${contextPath}/cambia-password"
              method="post">

            <label for="passwordAttuale">
                Password attuale
            </label>

            <input id="passwordAttuale"
                   name="passwordAttuale"
                   type="password"
                   autocomplete="current-password"
                   required>

            <label for="nuovaPassword">
                Nuova password
            </label>

            <input id="nuovaPassword"
                   name="nuovaPassword"
                   type="password"
                   minlength="8"
                   maxlength="72"
                   autocomplete="new-password"
                   required>

            <label for="confermaPassword">
                Conferma nuova password
            </label>

            <input id="confermaPassword"
                   name="confermaPassword"
                   type="password"
                   minlength="8"
                   maxlength="72"
                   autocomplete="new-password"
                   required>

            <button type="submit">
                Modifica password
            </button>

        </form>

        <div class="regole">
            <strong>La password deve contenere:</strong>

            <ul>
                <li>almeno 8 caratteri;</li>
                <li>una lettera maiuscola;</li>
                <li>una lettera minuscola;</li>
                <li>un numero;</li>
                <li>un simbolo.</li>
            </ul>
        </div>

    </section>

</main>

</body>
</html>