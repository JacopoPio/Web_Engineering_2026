<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Materiali - SoccorsoWeb</title>

    <style>
        * { box-sizing: border-box; }

        body {
            font-family: Arial, Helvetica, sans-serif;
            margin: 0;
            padding: 0;
            background: #f4f6f8;
            color: #222;
        }

        /* ── HEADER ── */
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

        /* ── NAV ── */
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
            background: rgba(255,255,255,0.18);
        }

        /* ── MAIN ── */
        main {
            max-width: 1100px;
            margin: 35px auto;
            padding: 0 20px;
            display: grid;
            gap: 28px;
        }

        /* ── PANEL ── */
        .panel {
            background: white;
            padding: 30px;
            border-radius: 14px;
            box-shadow: 0 8px 25px rgba(0,0,0,.10);
        }

        .panel h2 {
            margin-top: 0;
            color: #b71c1c;
        }

        .panel p {
            margin-top: 0;
            color: #555;
        }

        /* ── MESSAGGI ── */
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

        /* ── FORM INSERIMENTO ── */
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr auto;
            gap: 12px;
            align-items: end;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            font-size: 14px;
            color: #444;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #bbb;
            border-radius: 6px;
            font-size: 14px;
        }

        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #b71c1c;
            box-shadow: 0 0 0 2px rgba(183,28,28,.15);
        }

        /* ── BOTTONI ── */
        .btn {
            display: inline-block;
            padding: 10px 18px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }

        .btn-primary {
            background: #b71c1c;
            color: white;
        }

        .btn-primary:hover {
            background: #8e0000;
        }

        .btn-edit {
            background: #1565c0;
            color: white;
        }

        .btn-edit:hover {
            background: #0d47a1;
        }

        .btn-delete {
            background: #c62828;
            color: white;
        }

        .btn-delete:hover {
            background: #8e0000;
        }

        .btn-cancel {
            background: #546e7a;
            color: white;
        }

        .btn-cancel:hover {
            background: #263238;
        }

        .btn-sm {
            padding: 7px 12px;
            font-size: 13px;
        }

        /* ── TABELLA ── */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th {
            background: #b71c1c;
            color: white;
            padding: 12px 14px;
            text-align: left;
        }

        td {
            padding: 11px 14px;
            border-bottom: 1px solid #e0e0e0;
            vertical-align: middle;
        }

        tr:hover {
            background: #fafafa;
        }

        tr.editing {
            background: #fff8e1;
        }

        tr.editing td {
            border-bottom: 1px solid #ffe082;
        }

        .azioni {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }

        .inline-input {
            padding: 7px 10px;
            border: 1px solid #bbb;
            border-radius: 5px;
            font-size: 13px;
            width: 100%;
        }

        .inline-input:focus {
            outline: none;
            border-color: #1565c0;
        }

        /* ── VUOTO ── */
        .vuoto {
            padding: 22px;
            background: #f7f7f7;
            border-radius: 8px;
            margin-top: 20px;
            color: #666;
        }

        /* ── FOOTER ── */
        footer {
            text-align: center;
            padding: 25px;
            color: #666;
            font-size: 14px;
        }

        /* ── RESPONSIVE ── */
        @media (max-width: 700px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            nav a {
                display: block;
                margin: 6px 0;
            }

            table {
                font-size: 13px;
            }

            .azioni {
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Gestione Materiali</h1>
    <p>Benvenuto ${nome!"Amministratore"} - Ruolo: ${ruolo!"ADMIN"}</p>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/admin/nuovo-utente">Gestione Utenti</a>
    <a href="${contextPath}/mezzi">Mezzi</a>
    <a href="${contextPath}/materiali">Materiali</a>
    <a href="${contextPath}/operatori">Operatori</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>

    <#if ok??>
        <div class="msg-ok">Operazione eseguita correttamente.</div>
    </#if>

    <#if errore??>
        <div class="msg-errore">Errore: ${errore}</div>
    </#if>

    <section class="panel">
        <h2>Aggiungi nuovo materiale</h2>
        <p>Inserisci il tipo e la descrizione del materiale.</p>

        <form action="${contextPath}/materiali/inserisci" method="post">
            <div class="form-grid">

                <div class="form-group">
                    <label for="tipo">Tipo materiale</label>
                    <input type="text"
                           id="tipo"
                           name="tipo"
                           placeholder="es. Kit medico"
                           maxlength="100"
                           required>
                </div>

                <div class="form-group">
                    <label for="descrizione">Descrizione</label>
                    <input type="text"
                           id="descrizione"
                           name="descrizione"
                           placeholder="es. Kit primo soccorso completo"
                           maxlength="500"
                           required>
                </div>

                <div class="form-group">
                    <label>&nbsp;</label>
                    <button type="submit" class="btn btn-primary">Aggiungi</button>
                </div>

            </div>
        </form>
    </section>

    <section class="panel">
        <h2>Materiali registrati</h2>
        <p>Elenco di tutti i materiali presenti nel database. Puoi modificare o eliminare ciascun record.</p>

        <#if materiali?size == 0>
            <div class="vuoto">Nessun materiale presente nel database.</div>
        <#else>

            <table>
                <thead>
                    <tr>
                        <th>Tipo</th>
                        <th>Descrizione</th>
                        <th>Azioni</th>
                    </tr>
                </thead>

                <tbody>
                    <#list materiali as mat>

                        <#if (tipoModifica??) && (tipoModifica == mat.tipo)>

                            <tr class="editing">
                                <td><strong>${mat.tipo}</strong></td>

                                <td>
                                    <form action="${contextPath}/materiali/modifica" method="post"
                                          id="form-mod-${mat.tipo?replace(' ','-')}">

                                        <input type="hidden" name="tipo" value="${mat.tipo}">

                                        <input class="inline-input"
                                               type="text"
                                               name="descrizione"
                                               value="${mat.descrizione!''}"
                                               maxlength="500"
                                               required>
                                    </form>
                                </td>

                                <td>
                                    <div class="azioni">
                                        <button type="submit"
                                                form="form-mod-${mat.tipo?replace(' ','-')}"
                                                class="btn btn-edit btn-sm">Salva</button>

                                        <a href="${contextPath}/materiali"
                                           class="btn btn-cancel btn-sm">Annulla</a>
                                    </div>
                                </td>
                            </tr>

                        <#else>

                            <tr>
                                <td>${mat.tipo}</td>
                                <td>${mat.descrizione!""}</td>

                                <td>
                                    <div class="azioni">

                                        <a href="${contextPath}/materiali/modifica-form?tipo=${mat.tipo?url('UTF-8')}"
                                           class="btn btn-edit btn-sm">Modifica</a>

                                        <form action="${contextPath}/materiali/elimina" method="post"
                                              onsubmit="return confirm('Eliminare il materiale ${mat.tipo}?');"
                                              style="display:inline;">

                                            <input type="hidden" name="tipo" value="${mat.tipo}">

                                            <button type="submit"
                                                    class="btn btn-delete btn-sm">Elimina</button>
                                        </form>

                                    </div>
                                </td>
                            </tr>

                        </#if>

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