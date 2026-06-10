<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Richieste - SoccorsoWeb</title>

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
            background: linear-gradient(135deg, #b71c1c, #d32f2f);
            color: white;
            padding: 35px 20px;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 38px;
        }

        nav {
            background: #8e0000;
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
            color: #b71c1c;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 25px;
        }

        th {
            background: #b71c1c;
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
            font-weight: bold;
            color: #b71c1c;
        }

        .stato-form {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .stato-form select {
            padding: 8px;
            border: 1px solid #aaa;
            border-radius: 5px;
            font-size: 14px;
        }

        .stato-form button {
            background-color: #b71c1c;
            color: white;
            border: none;
            padding: 9px 14px;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
        }

        .stato-form button:hover {
            background-color: #8e0000;
        }

        .msg-ok {
            background: #e8f5e9;
            color: #1b5e20;
            border-left: 6px solid #2e7d32;
            padding: 14px;
            margin-bottom: 20px;
            border-radius: 6px;
        }

        .msg-errore {
            background: #ffebee;
            color: #b71c1c;
            border-left: 6px solid #b71c1c;
            padding: 14px;
            margin-bottom: 20px;
            border-radius: 6px;
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

            .stato-form {
                flex-direction: column;
                align-items: stretch;
            }

            nav a {
                display: block;
                margin: 6px 0;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Gestione Richieste</h1>
    <p>Benvenuto ${nome!"Amministratore"} - Ruolo: ${ruolo!"ADMIN"}</p>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/gestione-utenti">Gestione Utenti</a>
    <a href="${contextPath}/mezzi">Mezzi</a>
    <a href="${contextPath}/materiali">Materiali</a>
    <a href="${contextPath}/operatori">Operatori</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>
    <section class="panel">

        <h2>Richieste ricevute</h2>
        <p>Da questa pagina puoi controllare le richieste inviate dagli utenti e modificarne lo stato.</p>

        <#if ok??>
            <div class="msg-ok">Stato aggiornato correttamente.</div>
        </#if>

        <#if errore??>
            <div class="msg-errore">Errore durante l'aggiornamento dello stato.</div>
        </#if>

        <#if richieste?size == 0>
            <div class="vuoto">
                Nessuna richiesta presente nel database.
            </div>
        <#else>

            <table>
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Descrizione</th>
                        <th>Indirizzo</th>
                        <th>Stato attuale</th>
                        <th>Cambia stato</th>
                    </tr>
                </thead>

                <tbody>
                    <#list richieste as r>
                        <tr>
                            <td>${r.email_segnalante}</td>
                            <td>${r.descrizione}</td>
                            <td>${r.indirizzo}</td>
                            <td class="stato">${r.stato}</td>

                            <td>
                                <form action="${contextPath}/admin/cambia-stato" method="post" class="stato-form">

                                    <input type="hidden" name="email_segnalante" value="${r.email_segnalante}">

                                    <select name="stato">
                                        <option value="attiva" <#if (r.stato!"") == "attiva">selected</#if>>
                                            Attiva
                                        </option>

                                        <option value="in corso" <#if (r.stato!"") == "in corso">selected</#if>>
                                            In corso
                                        </option>

                                        <option value="chiusa" <#if (r.stato!"") == "chiusa">selected</#if>>
                                            Chiusa
                                        </option>
                                    </select>

                                    <button type="submit">Aggiorna</button>
                                </form>
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