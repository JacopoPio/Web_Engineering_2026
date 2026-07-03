<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Gestione materiali - SoccorsoWeb</title>

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
            background: linear-gradient(135deg, #1565c0, #1976d2);
            color: white;
            padding: 32px 20px;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 36px;
        }

        header p {
            margin: 8px 0 0;
        }

        nav {
            background: #0d47a1;
            padding: 14px;
            text-align: center;
        }

        nav a {
            display: inline-block;
            color: white;
            text-decoration: none;
            font-weight: bold;
            margin: 5px 10px;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: 92%;
            max-width: 1200px;
            margin: 30px auto;
        }

        .messaggio {
            padding: 14px 16px;
            margin-bottom: 20px;
            border-radius: 7px;
            font-weight: bold;
        }

        .successo {
            color: #155724;
            background: #d4edda;
            border: 1px solid #c3e6cb;
        }

        .errore {
            color: #721c24;
            background: #f8d7da;
            border: 1px solid #f5c6cb;
        }

        .pannello {
            background: white;
            padding: 24px;
            margin-bottom: 28px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .pannello h2 {
            margin-top: 0;
            color: #0d47a1;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 2fr auto;
            gap: 15px;
            align-items: end;
        }

        .campo {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        label {
            font-weight: bold;
        }

        input[type="text"],
        textarea {
            width: 100%;
            padding: 11px;
            border: 1px solid #b8c2cc;
            border-radius: 6px;
            font-family: inherit;
            font-size: 15px;
        }

        textarea {
            min-height: 90px;
            resize: vertical;
        }

        input:focus,
        textarea:focus {
            outline: 2px solid #90caf9;
            border-color: #1565c0;
        }

        button,
        .pulsante {
            display: inline-block;
            border: none;
            border-radius: 6px;
            padding: 10px 15px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }

        .primario {
            color: white;
            background: #1565c0;
        }

        .primario:hover {
            background: #0d47a1;
        }

        .modifica {
            color: white;
            background: #ef6c00;
        }

        .modifica:hover {
            background: #e65100;
        }

        .elimina {
            color: white;
            background: #c62828;
        }

        .elimina:hover {
            background: #8e0000;
        }

        .storico {
            color: white;
            background: #546e7a;
        }

        .storico:hover {
            background: #37474f;
        }

        .annulla {
            color: white;
            background: #757575;
        }

        .annulla:hover {
            background: #555;
        }

        .tabella-contenitore {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 750px;
        }

        th,
        td {
            padding: 13px;
            border-bottom: 1px solid #ddd;
            text-align: left;
            vertical-align: middle;
        }

        th {
            color: white;
            background: #1565c0;
        }

        tbody tr:hover {
            background: #f1f6fb;
        }

        .azioni {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
        }

        .azioni form {
            margin: 0;
        }

        .form-modifica {
            margin-top: 16px;
            padding: 18px;
            background: #fff8e1;
            border: 1px solid #ffe082;
            border-radius: 8px;
        }

        .form-modifica h3 {
            margin-top: 0;
            color: #e65100;
        }

        .form-modifica-grid {
            display: grid;
            grid-template-columns: 1fr auto auto;
            gap: 12px;
            align-items: end;
        }

        .vuoto {
            padding: 22px;
            color: #666;
            text-align: center;
            font-style: italic;
        }

        footer {
            margin-top: 40px;
            padding: 20px;
            background: #263238;
            color: white;
            text-align: center;
        }

        @media (max-width: 800px) {
            .form-grid,
            .form-modifica-grid {
                grid-template-columns: 1fr;
            }

            header h1 {
                font-size: 29px;
            }

            .azioni {
                flex-direction: column;
            }

            .azioni .pulsante,
            .azioni button {
                width: 100%;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Gestione materiali</h1>

    <#if nome??>
        <p>
            Benvenuto, ${nome?html}
            <#if ruolo??>
                — ${ruolo?html}
            </#if>
        </p>
    <#else>
        <p>Inserimento, modifica ed eliminazione dei materiali</p>
    </#if>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/admin/utenti">Utenti</a>
    <a href="${contextPath}/admin/missioni">Missioni</a>
    <a href="${contextPath}/mezzi">Mezzi</a>
    <a href="${contextPath}/materiali">Materiali</a>
    <a href="${contextPath}/storico/materiali">Storico materiali</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>

    <#if ok??>
        <div class="messaggio successo">
            Operazione completata correttamente.
        </div>
    </#if>

    <#if errore??>
        <div class="messaggio errore">
            ${errore?html}
        </div>
    </#if>

    <section class="pannello">
        <h2>Inserisci un nuovo materiale</h2>

        <form action="${contextPath}/materiali/inserisci"
              method="post"
              class="form-grid">

            <div class="campo">
                <label for="tipo">Nome o tipo del materiale</label>

                <input type="text"
                       id="tipo"
                       name="tipo"
                       maxlength="255"
                       placeholder="Esempio: Kit medico"
                       required>
            </div>

            <div class="campo">
                <label for="descrizione">Descrizione</label>

                <textarea id="descrizione"
                          name="descrizione"
                          maxlength="255"
                          placeholder="Inserisci una breve descrizione"
                          required></textarea>
            </div>

            <button type="submit" class="primario">
                Inserisci materiale
            </button>
        </form>
    </section>

    <section class="pannello">
        <h2>Materiali presenti</h2>

        <#if materiali?? && materiali?has_content>

            <div class="tabella-contenitore">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tipo</th>
                        <th>Descrizione</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list materiali as materiale>
                        <tr>
                            <td>${materiale.id}</td>

                            <td>
                                ${(materiale.tipo!"-")?html}
                            </td>

                            <td>
                                ${(materiale.descrizione!"-")?html}
                            </td>

                            <td>
                                <div class="azioni">
                                    <a class="pulsante modifica"
                                       href="${contextPath}/materiali/modifica-form?tipo=${materiale.tipo?url}">
                                        Modifica
                                    </a>

                                    <a class="pulsante storico"
                                       href="${contextPath}/storico/materiale?id=${materiale.id}">
                                        Storico
                                    </a>

                                    <form action="${contextPath}/materiali/elimina"
                                          method="post"
                                          onsubmit="return confirm('Eliminare il materiale ${materiale.tipo?js_string}?');">

                                        <input type="hidden"
                                               name="tipo"
                                               value="${materiale.tipo?html}">

                                        <button type="submit" class="elimina">
                                            Elimina
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>

                        <#if tipoModifica??
                            && materiale.tipo??
                            && tipoModifica == materiale.tipo>

                            <tr>
                                <td colspan="4">
                                    <div class="form-modifica">
                                        <h3>
                                            Modifica materiale:
                                            ${materiale.tipo?html}
                                        </h3>

                                        <form action="${contextPath}/materiali/modifica"
                                              method="post"
                                              class="form-modifica-grid">

                                            <input type="hidden"
                                                   name="tipo"
                                                   value="${materiale.tipo?html}">

                                            <div class="campo">
                                                <label for="descrizione-${materiale.id}">
                                                    Nuova descrizione
                                                </label>

                                                <textarea id="descrizione-${materiale.id}"
                                                          name="descrizione"
                                                          maxlength="255"
                                                          required>${(materiale.descrizione!"")?html}</textarea>
                                            </div>

                                            <button type="submit" class="modifica">
                                                Salva modifiche
                                            </button>

                                            <a href="${contextPath}/materiali"
                                               class="pulsante annulla">
                                                Annulla
                                            </a>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </#if>
                    </#list>
                    </tbody>
                </table>
            </div>

        <#else>
            <div class="vuoto">
                Non sono presenti materiali nel database.
            </div>
        </#if>
    </section>

</main>

<footer>
    SoccorsoWeb — Area amministratore
</footer>

</body>
</html>