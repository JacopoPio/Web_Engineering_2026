<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle!"Richieste"} - SoccorsoWeb</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f8;
            color: #222;
        }

        header {
            background: #b71c1c;
            color: white;
            padding: 25px;
            text-align: center;
        }

        nav {
            background: #8e0000;
            padding: 12px;
            text-align: center;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin: 0 12px;
            font-weight: bold;
        }

        main {
            max-width: 1100px;
            margin: 40px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }

        h2 {
            color: #b71c1c;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 25px;
        }

        th {
            background: #d32f2f;
            color: white;
            padding: 12px;
            text-align: left;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background: #f9f9f9;
        }

        .status {
            padding: 6px 10px;
            border-radius: 999px;
            background: #fff3e0;
            color: #e65100;
            font-weight: bold;
            font-size: 13px;
        }

        .empty {
            padding: 20px;
            background: #f5f5f5;
            border-radius: 8px;
            margin-top: 20px;
        }
    </style>
</head>

<body>

<header>
    <h1>SoccorsoWeb</h1>
    <p>Area amministratore</p>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/home">Home</a>
</nav>

<main>
    <h2>Richieste ricevute</h2>

    <#if richieste?? && richieste?size gt 0>
        <table>
            <thead>
                <tr>
                    <th>Email</th>
                    <th>Nome</th>
                    <th>Descrizione</th>
                    <th>Indirizzo</th>
                    <th>Stato</th>
                    <th>Foto</th>
                </tr>
            </thead>

            <tbody>
                <#list richieste as r>
                    <tr>
                        <td>${r.email_segnalante!""}</td>
                        <td>${r.nomeSegnalante!""}</td>
                        <td>${r.descrizione!""}</td>
                        <td>${r.indirizzo!""}</td>
                        <td><span class="status">${r.stato!""}</span></td>
                        <td>
                            <#if r.pathFoto??>
                                <a href="${contextPath}/${r.pathFoto}" target="_blank">Apri foto</a>
                            <#else>
                                Nessuna foto
                            </#if>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    <#else>
        <div class="empty">
            Nessuna richiesta presente nel database.
        </div>
    </#if>
</main>

</body>
</html>