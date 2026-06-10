<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Richieste Operatore - SoccorsoWeb</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, Helvetica, sans-serif;
            margin: 0;
            padding: 0;
            background: #f4f6f8;
            color: #222;
        }

        header {
            background: linear-gradient(135deg, #1565c0, #1976d2);
            color: white;
            padding: 35px 20px;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 38px;
        }

        nav {
            background: #0d47a1;
            padding: 14px;
            text-align: center;
        }

        nav a {
            color: white;
            margin: 0 10px;
            font-weight: bold;
            text-decoration: none;
            padding: 8px 12px;
            border-radius: 6px;
        }

        nav a:hover {
            background: rgba(255, 255, 255, 0.18);
        }

        main {
            max-width: 1200px;
            margin: 35px auto;
            padding: 0 20px;
        }

        .panel {
            background: white;
            padding: 30px;
            border-radius: 14px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.10);
        }

        .panel h2 {
            margin-top: 0;
            color: #1565c0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 25px;
        }

        th {
            background: #1565c0;
            color: white;
            padding: 12px;
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
            display: inline-block;
            font-weight: bold;
            padding: 7px 10px;
            border-radius: 6px;
        }

        .stato-attiva {
            background: #e3f2fd;
            color: #0d47a1;
        }

        .stato-corso {
            background: #fff8e1;
            color: #e65100;
        }

        .btn {
            display: inline-block;
            background-color: #1565c0;
            color: white;
            padding: 9px 14px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
        }

        .btn:hover {
            background-color: #0d47a1;
        }

        .vuoto {
            padding: 20px;
            background: #f7f7f7;
            border-radius: 8px;
            margin-top: 20px;
        }

        footer {
            text-align: center;
            padding: 25px;
            color: #666;
            font-size: 14px;
        }

        @media screen and (max-width: 850px) {
            table {
                font-size: 14px;
            }

            nav a {
                display: block;
                margin: 6px 0;
            }

            header h1 {
                font-size: 30px;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Richieste Operatore</h1>
    <p>Benvenuto ${nome!"Operatore"} - Ruolo: ${ruolo!"OPERATORE"}</p>
</header>

<nav>
    <a href="${contextPath}/operatore">Dashboard</a>
    <a href="${contextPath}/operatore/richieste">Richieste</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>
    <section class="panel">

        <h2>Richieste da gestire</h2>

        <p>
            In questa pagina puoi visualizzare le richieste attive o in corso.
            Le richieste chiuse non vengono mostrate.
        </p>

        <#if richieste?size == 0>
            <div class="vuoto">
                Non ci sono richieste attive o in corso.
            </div>
        <#else>

            <table>
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Nome</th>
                        <th>Descrizione</th>
                        <th>Indirizzo</th>
                        <th>Stato</th>
                        <th>Dettaglio</th>
                    </tr>
                </thead>

                <tbody>
                    <#list richieste as r>
                        <tr>
                            <td>${r.email_segnalante!"Non disponibile"}</td>
                            <td>${r.nome_segnalante!"Non disponibile"}</td>
                            <td>${r.descrizione!"Non disponibile"}</td>
                            <td>${r.indirizzo!"Non disponibile"}</td>

                            <td>
                                <#if (r.stato!"") == "attiva">
                                    <span class="stato stato-attiva">Attiva</span>
                                <#elseif (r.stato!"") == "in corso">
                                    <span class="stato stato-corso">In corso</span>
                                <#else>
                                    <span class="stato">${r.stato!"Non disponibile"}</span>
                                </#if>
                            </td>

                            <td>
                                <a class="btn"
                                   href="${contextPath}/operatore/dettaglio-richiesta?email=${r.email_segnalante?url('UTF-8')}">
                                    Apri
                                </a>
                            </td>
                        </tr>
                    </#list>
                </tbody>
            </table>

        </#if>

    </section>
</main>

<footer>
    <p>&copy; 2026 SoccorsoWeb - Progetto Web Engineering</p>
</footer>

</body>
</html>