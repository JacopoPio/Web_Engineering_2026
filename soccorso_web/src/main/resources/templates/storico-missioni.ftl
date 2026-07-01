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
            background: linear-gradient(135deg, #1565c0, #1976d2);
            color: white;
            text-align: center;
        }

        header h1 { margin: 0 0 8px; }
        header p { margin: 0; }

        main {
            width: min(1000px, 94%);
            margin: 30px auto;
        }

        .panel {
            padding: 25px;
            border-radius: 12px;
            background: white;
            box-shadow: 0 5px 18px rgba(0, 0, 0, 0.10);
        }

        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }

        th {
            padding: 12px;
            background: #1565c0;
            color: white;
            text-align: left;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            vertical-align: top;
        }

        .vuoto {
            margin-top: 20px;
            padding: 15px;
            border-radius: 7px;
            background: #fff3cd;
            color: #664d03;
        }

        .btn {
            display: inline-block;
            margin-top: 22px;
            padding: 10px 18px;
            border-radius: 6px;
            background: #1565c0;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        .btn:hover { background: #0d47a1; }
    </style>
</head>
<body>

<header>
    <h1>${(titolo!"Storico missioni")?html}</h1>
    <p>${(elemento!"")?html}</p>
</header>

<main>
    <section class="panel">
        <#if missioni?? && missioni?has_content>
            <table>
                <thead>
                    <tr>
                        <th>ID missione</th>
                        <th>Descrizione</th>
                    </tr>
                </thead>
                <tbody>
                    <#list missioni as missione>
                        <tr>
                            <td>${missione.id!"-"}</td>
                            <td>${(missione.descrizione!"Nessuna descrizione")?html}</td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        <#else>
            <div class="vuoto">
                Questo elemento non è ancora stato coinvolto in alcuna missione.
            </div>
        </#if>

        <a class="btn" href="${ritorno}">Torna alla lista</a>
    </section>
</main>

</body>
</html>
