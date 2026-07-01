<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Gestione missioni</title>

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
            background: #1565c0;
            color: white;
            padding: 30px;
            text-align: center;
        }

        header h1 {
            margin: 0;
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
            max-width: 1400px;
            margin: 30px auto;
        }

        .messaggio-successo {
            background: #d4edda;
            color: #155724;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }

        .messaggio-errore {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }

        .vuoto {
            background: white;
            padding: 25px;
            text-align: center;
            border-radius: 8px;
        }

        .tabella-container {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        }

        th,
        td {
            padding: 14px;
            border-bottom: 1px solid #ddd;
            text-align: left;
            vertical-align: top;
        }

        th {
            background: #1976d2;
            color: white;
            white-space: nowrap;
        }

        tbody tr:hover {
            background: #f5f5f5;
        }

        .lista {
            margin: 0;
            padding-left: 18px;
        }

        .lista li {
            margin-bottom: 5px;
        }

        .caposquadra {
            color: #1b5e20;
            font-weight: bold;
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
    <h1>Gestione missioni</h1>
</header>

<nav>
    <a href="${contextPath!""}/admin">
        Dashboard
    </a>

    <a href="${contextPath!""}/admin/richieste">
        Gestione richieste
    </a>

    <a href="${contextPath!""}/admin/missioni">
        Gestione missioni
    </a>
</nav>

<main>

    <#if successo??>

        <div class="messaggio-successo">

            <#if successo == "creata">
                Missione creata correttamente.
            <#else>
                Operazione completata correttamente.
            </#if>

        </div>

    </#if>

    <#if errore??>

        <div class="messaggio-errore">
            Si è verificato un errore:
            ${errore!"Errore non specificato"}
        </div>

    </#if>

    <#assign listaMissioni = missioni![]>

    <#if listaMissioni?has_content>

        <div class="tabella-container">

            <table>

                <thead>
                <tr>
                    <th>Descrizione</th>
                    <th>Richiesta</th>
                    <th>Squadra</th>
                    <th>Operatori</th>
                    <th>Mezzi</th>
                    <th>Materiali</th>
                    <th>Aggiornamenti</th>
                </tr>
                </thead>

                <tbody>

                <#list listaMissioni as m>

                    <#if m??>

                        <tr>

                            <td>
                                ${(m.descrizione)!"Descrizione non disponibile"}
                            </td>

                            <td>

                                <#if m.richiesta??>

                                    ${(m.richiesta.email_segnalante)!"Email non disponibile"}

                                <#else>

                                    Richiesta non disponibile

                                </#if>

                            </td>

                            <td>

                                <#if m.squadra??>

                                    ${(m.squadra.nome)!"Squadra senza nome"}

                                <#else>

                                    Nessuna squadra

                                </#if>

                            </td>

                            <td>

                                <#if m.squadra??
                                    && m.squadra.operatori??
                                    && m.squadra.operatori?has_content>

                                    <ul class="lista">

                                        <#list m.squadra.operatori as operatore>

                                            <#if operatore??>

                                                <li>

                                                    ${(operatore.nome)!""}
                                                    ${(operatore.cognome)!""}

                                                    <#if operatore.email??>
                                                        -
                                                        ${operatore.email}
                                                    </#if>

                                                    <#if operatore.caposquadra!false>

                                                        <span class="caposquadra">
                                                            (Caposquadra)
                                                        </span>

                                                    </#if>

                                                </li>

                                            </#if>

                                        </#list>

                                    </ul>

                                <#else>

                                    Nessun operatore

                                </#if>

                            </td>

                            <td>

                                <#if m.mezzi??
                                    && m.mezzi?has_content>

                                    <ul class="lista">

                                        <#list m.mezzi as mezzo>

                                            <#if mezzo??>

                                                <li>
                                                    ${(mezzo.targa)!"Targa non disponibile"}
                                                </li>

                                            </#if>

                                        </#list>

                                    </ul>

                                <#else>

                                    Nessun mezzo

                                </#if>

                            </td>

                            <td>

                                <#if m.materiali??
                                    && m.materiali?has_content>

                                    <ul class="lista">

                                        <#list m.materiali as materiale>

                                            <#if materiale??>

                                                <li>
                                                    ${(materiale.tipo)!"Materiale non disponibile"}
                                                </li>

                                            </#if>

                                        </#list>

                                    </ul>

                                <#else>

                                    Nessun materiale

                                </#if>

                            </td>
                            <td>

                                <#if m.id??>

                                    <a href="${contextPath!""}/admin/missioni/aggiornamenti?id=${m.id}">
                                        Vedi aggiornamenti
                                    </a>

                                <#else>

                                    Non disponibile

                                </#if>

                            </td>

                        </tr>

                    </#if>

                </#list>

                </tbody>

            </table>

        </div>

    <#else>

        <div class="vuoto">
            Non sono presenti missioni.
        </div>

    </#if>

    <a class="pulsante"
       href="${contextPath!""}/admin/richieste">
        Vai alle richieste
    </a>

</main>

</body>
</html>