<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aggiornamenti missione</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f8;
            color: #222;
        }

        header {
            background: #1565c0;
            color: white;
            padding: 30px;
            text-align: center;
        }

        header p {
            margin: 8px 0 0 0;
            opacity: 0.9;
        }

        nav {
            background: #0d47a1;
            padding: 15px;
            text-align: center;
        }

        nav a {
            color: white;
            text-decoration: none;
            font-weight: bold;
            margin: 0 12px;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: 95%;
            max-width: 1000px;
            margin: 30px auto;
        }

        .vuoto {
            background: white;
            padding: 25px;
            text-align: center;
            border-radius: 8px;
        }

        .aggiornamento {
            background: white;
            padding: 18px;
            margin-bottom: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        }

        .aggiornamento .data {
            color: #666;
            font-size: 0.9em;
            margin-bottom: 8px;
        }

        .pulsante {
            display: inline-block;
            margin-top: 25px;
            padding: 12px 18px;
            background: #1565c0;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .pulsante:hover {
            background: #0d47a1;
        }
    </style>
</head>

<body>

<header>
    <h1>Aggiornamenti missione</h1>
    <p>${(missione.descrizione)!"Descrizione non disponibile"}</p>
</header>

<nav>
    <a href="${contextPath!""}/admin">Dashboard</a>
    <a href="${contextPath!""}/admin/richieste">Gestione richieste</a>
    <a href="${contextPath!""}/admin/missioni">Gestione missioni</a>
</nav>

<main>

    <#assign listaAggiornamenti = aggiornamenti![]>

    <#if listaAggiornamenti?has_content>

        <#list listaAggiornamenti as a>

            <#if a??>

                <div class="aggiornamento">

                    <div class="data">
                        <#if a.data_update??>
                            ${a.data_update?string("dd/MM/yyyy HH:mm")}
                        <#else>
                            Data non disponibile
                        </#if>
                    </div>

                    <div>
                        ${(a.descrizione)!"Nessuna descrizione"}
                    </div>

                </div>

            </#if>

        </#list>

    <#else>

        <div class="vuoto">
            Non sono presenti aggiornamenti per questa missione.
        </div>

    </#if>

    <a class="pulsante" href="${contextPath!""}/admin/missioni">
        Torna alle missioni
    </a>

</main>

</body>
</html>
