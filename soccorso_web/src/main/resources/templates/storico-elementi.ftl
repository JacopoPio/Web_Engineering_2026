<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${(titolo!"Storico missioni")?html} - SoccorsoWeb</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f8;
            color: #222;
        }

        header {
            padding: 32px 20px;
            background: linear-gradient(135deg, #b71c1c, #d32f2f);
            color: white;
            text-align: center;
        }

        header h1 { margin: 0; }

        nav {
            padding: 14px;
            background: #8e0000;
            text-align: center;
        }

        nav a {
            margin: 0 10px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        main {
            width: min(1100px, 94%);
            margin: 30px auto;
        }

        .panel {
            padding: 25px;
            border-radius: 12px;
            background: white;
            box-shadow: 0 5px 18px rgba(0, 0, 0, 0.10);
        }

        .errore {
            margin-bottom: 18px;
            padding: 13px;
            border-radius: 7px;
            background: #f8d7da;
            color: #842029;
        }

        table {
            width: 100%;
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
            vertical-align: middle;
        }

        .btn {
            display: inline-block;
            padding: 9px 14px;
            border-radius: 6px;
            background: #d32f2f;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        .btn:hover { background: #b71c1c; }

        .vuoto {
            padding: 15px;
            border-radius: 7px;
            background: #fff3cd;
            color: #664d03;
        }
    </style>
</head>
<body>

<header>
    <h1>${(titolo!"Storico missioni")?html}</h1>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>
    <section class="panel">
        <#if errore??>
            <div class="errore">${errore?html}</div>
        </#if>

        <#if tipo == "operatore">
            <#if operatori?? && operatori?has_content>
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Cognome</th>
                            <th>Email</th>
                            <th>Storico</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list operatori as operatore>
                            <tr>
                                <td>${(operatore.nome!"-")?html}</td>
                                <td>${(operatore.cognome!"-")?html}</td>
                                <td>${(operatore.email!"-")?html}</td>
                                <td>
                                    <a class="btn"
                                       href="${contextPath}/storico/operatore?email=${operatore.email?url('UTF-8')}">
                                        Vedi missioni
                                    </a>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <div class="vuoto">Non sono presenti operatori.</div>
            </#if>

        <#elseif tipo == "mezzo">
            <#if mezzi?? && mezzi?has_content>
                <table>
                    <thead>
                        <tr>
                            <th>Targa</th>
                            <th>Tipo</th>
                            <th>Storico</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list mezzi as mezzo>
                            <tr>
                                <td>${(mezzo.targa!"-")?html}</td>
                                <td>${(mezzo.tipo!"-")?html}</td>
                                <td>
                                    <a class="btn"
                                       href="${contextPath}/storico/mezzo?targa=${mezzo.targa?url('UTF-8')}">
                                        Vedi missioni
                                    </a>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <div class="vuoto">Non sono presenti mezzi.</div>
            </#if>

        <#elseif tipo == "materiale">
            <#if materiali?? && materiali?has_content>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tipo</th>
                            <th>Descrizione</th>
                            <th>Storico</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list materiali as materiale>
                            <tr>
                                <td>${materiale.id!"-"}</td>
                                <td>${(materiale.tipo!"-")?html}</td>
                                <td>${(materiale.descrizione!"-")?html}</td>
                                <td>
                                    <a class="btn"
                                       href="${contextPath}/storico/materiale?id=${materiale.id}">
                                        Vedi missioni
                                    </a>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <div class="vuoto">Non sono presenti materiali.</div>
            </#if>
        </#if>
    </section>
</main>

</body>
</html>

