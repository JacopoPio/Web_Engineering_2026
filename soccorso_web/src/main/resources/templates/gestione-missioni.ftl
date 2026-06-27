<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">

    <title>Gestione missioni</title>

    <style>
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

        main {
            width: 90%;
            max-width: 1200px;
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
        }

        tr:hover {
            background: #f5f5f5;
        }

        .lista {
            margin: 0;
            padding-left: 18px;
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
    </style>
</head>

<body>

<header>
    <h1>Gestione missioni</h1>
</header>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

    <a href="${contextPath}/admin/richieste">
        Gestione richieste
    </a>

    <a href="${contextPath}/admin/missioni">
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
            Si è verificato un errore: ${errore}
        </div>

    </#if>

    <#if missioni?has_content>

        <table>

            <thead>
            <tr>
                <th>Descrizione</th>
                <th>Richiesta</th>
                <th>Squadra</th>
                <th>Operatori</th>
                <th>Mezzi</th>
                <th>Materiali</th>
            </tr>
            </thead>

            <tbody>

            <#list missioni as missione>

                <tr>

                    <td>
                        ${missione.descrizione!"Descrizione non disponibile"}
                    </td>

                    <td>

                        <#if missione.richiesta??>

                            ${missione.richiesta.emailSegnalante!"Email non disponibile"}

                        <#else>

                            Richiesta non disponibile

                        </#if>

                    </td>

                    <td>

                        <#if missione.squadra??>

                            ${missione.squadra.nome!"Squadra senza nome"}

                        <#else>

                            Nessuna squadra

                        </#if>

                    </td>

                    <td>

                        <#if missione.squadra??
                                && missione.squadra.operatori??>

                            <#if missione.squadra.operatori?has_content>

                                <ul class="lista">

                                    <#list missione.squadra.operatori as operatore>

                                        <li>
                                            ${operatore.email!"Email non disponibile"}
                                        </li>

                                    </#list>

                                </ul>

                            <#else>

                                Nessun operatore

                            </#if>

                        <#else>

                            Nessun operatore

                        </#if>

                    </td>

                    <td>

                        <#if missione.mezzi??>

                            <#if missione.mezzi?has_content>

                                <ul class="lista">

                                    <#list missione.mezzi as mezzo>

                                        <li>
                                            ${mezzo.targa!"Targa non disponibile"}
                                        </li>

                                    </#list>

                                </ul>

                            <#else>

                                Nessun mezzo

                            </#if>

                        <#else>

                            Nessun mezzo

                        </#if>

                    </td>

                    <td>

                        <#if missione.materiali??>

                            <#if missione.materiali?has_content>

                                <ul class="lista">

                                    <#list missione.materiali as materiale>

                                        <li>
                                            ${materiale.tipo!"Materiale"}
                                        </li>

                                    </#list>

                                </ul>

                            <#else>

                                Nessun materiale

                            </#if>

                        <#else>

                            Nessun materiale

                        </#if>

                    </td>

                </tr>

            </#list>

            </tbody>

        </table>

    <#else>

        <div class="vuoto">
            Non sono presenti missioni.
        </div>

    </#if>

    <a
        class="pulsante"
        href="${contextPath}/admin/richieste"
    >
        Vai alle richieste
    </a>

</main>

</body>
</html>
