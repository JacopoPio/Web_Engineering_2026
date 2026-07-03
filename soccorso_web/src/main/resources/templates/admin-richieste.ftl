<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        <#if mostraArchivio!false>
            Archivio richieste
        <#else>
            Gestione richieste
        </#if>
        - SoccorsoWeb
    </title>

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
            padding: 32px 20px;
            background: linear-gradient(135deg, #b71c1c, #d32f2f);
            color: white;
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
            padding: 14px;
            background: #8e0000;
            text-align: center;
        }

        nav a {
            display: inline-block;
            margin: 5px 10px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: min(1450px, 97%);
            margin: 30px auto;
        }

        .azioni-pagina {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 18px;
        }

        .panel {
            padding: 25px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, .1);
        }

        .panel h2 {
            margin-top: 0;
            color: #8e0000;
        }

        .tabella {
            overflow-x: auto;
        }

        table {
            width: 100%;
            min-width: 1100px;
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
            vertical-align: top;
        }

        tbody tr:hover {
            background: #fafafa;
        }

        .stato {
            display: inline-block;
            padding: 6px 10px;
            border-radius: 999px;
            font-weight: bold;
            white-space: nowrap;
        }

        .attiva {
            background: #e8f5e9;
            color: #1b5e20;
        }

        .in-corso {
            background: #fff3e0;
            color: #e65100;
        }

        .chiusa {
            background: #eeeeee;
            color: #424242;
        }

        .ignorata {
            background: #ffebee;
            color: #b71c1c;
        }

        .azioni {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
            align-items: center;
        }

        .azioni form {
            margin: 0;
        }

        .btn,
        button {
            display: inline-block;
            padding: 9px 13px;
            border: 0;
            border-radius: 6px;
            color: white;
            font-family: inherit;
            font-size: 14px;
            font-weight: bold;
            text-align: center;
            text-decoration: none;
            cursor: pointer;
        }

        .btn-crea {
            background: #1565c0;
        }

        .btn-crea:hover {
            background: #0d47a1;
        }

        .btn-ignora {
            background: #c62828;
        }

        .btn-ignora:hover {
            background: #8e0000;
        }

        .btn-archivia {
            background: #546e7a;
        }

        .btn-archivia:hover {
            background: #37474f;
        }

        .btn-ripristina {
            background: #2e7d32;
        }

        .btn-ripristina:hover {
            background: #1b5e20;
        }

        .btn-vista {
            background: #455a64;
        }

        .btn-vista:hover {
            background: #263238;
        }

        .btn-indietro {
            background: #1565c0;
        }

        .btn-indietro:hover {
            background: #0d47a1;
        }

        .msg {
            margin-bottom: 18px;
            padding: 14px;
            border-radius: 7px;
        }

        .ok {
            background: #e8f5e9;
            color: #1b5e20;
            border-left: 6px solid #2e7d32;
        }

        .errore {
            background: #ffebee;
            color: #b71c1c;
            border-left: 6px solid #b71c1c;
        }

        .foto {
            max-width: 110px;
            max-height: 80px;
            border-radius: 5px;
            object-fit: cover;
        }

        .nessuna-azione {
            color: #666;
            font-style: italic;
        }

        @media (max-width: 700px) {
            header h1 {
                font-size: 28px;
            }

            nav a {
                display: block;
            }

            .azioni-pagina {
                justify-content: stretch;
            }

            .azioni-pagina .btn {
                width: 100%;
            }
        }
    </style>
</head>

<body>

<header>
    <#if mostraArchivio!false>
        <h1>Archivio richieste</h1>
        <p>Richieste chiuse rimosse dalla gestione ordinaria</p>
    <#else>
        <h1>Gestione richieste</h1>
        <p>
            Benvenuto ${(nome!"Amministratore")?html}
        </p>
    </#if>
</header>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

    <a href="${contextPath}/admin/richieste">
        Richieste
    </a>

    <a href="${contextPath}/admin/missioni">
        Missioni
    </a>

    <a href="${contextPath}/logout">
        Logout
    </a>
</nav>

<main>

    <#if successo??>
        <div class="msg ok">

            <#if successo == "ignorata">
                Richiesta ignorata correttamente.

            <#elseif successo == "missione_creata"
                || successo == "creata">
                Missione creata correttamente.

            <#elseif successo == "archiviata">
                La richiesta chiusa è stata archiviata correttamente.
                Lo storico della missione è stato conservato.

            <#elseif successo == "ripristinata">
                La richiesta è stata ripristinata correttamente.

            <#else>
                Operazione completata correttamente.
            </#if>

        </div>
    </#if>

    <#if errore??>
        <div class="msg errore">

            <#if errore == "richiesta_non_trovata">
                Richiesta non trovata.

            <#elseif errore == "richiesta_non_attiva">
                L'operazione è consentita soltanto sulle richieste attive.

            <#elseif errore == "missione_esistente">
                Esiste già una missione per questa richiesta.

            <#elseif errore == "aggiornamento_fallito">
                Non è stato possibile aggiornare la richiesta.

            <#elseif errore == "richiesta_non_valida">
                La richiesta selezionata non è valida.

            <#elseif errore == "richiesta_non_chiusa">
                È possibile archiviare soltanto richieste chiuse.

            <#elseif errore == "richiesta_gia_archiviata">
                La richiesta è già presente nell'archivio.

            <#elseif errore == "richiesta_non_archiviata">
                La richiesta selezionata non risulta archiviata.

            <#elseif errore == "azione_non_valida">
                L'azione richiesta non è valida.

            <#elseif errore == "operazione_fallita">
                Non è stato possibile completare l'operazione.

            <#else>
                Non è stato possibile completare l'operazione.
            </#if>

        </div>
    </#if>

    <div class="azioni-pagina">

        <#if mostraArchivio!false>

            <a class="btn btn-indietro"
               href="${contextPath}/admin/richieste">
                Torna alle richieste
            </a>

        <#else>

            <a class="btn btn-vista"
               href="${contextPath}/admin/richieste?vista=archivio">
                Archivio richieste
            </a>

        </#if>

    </div>

    <section class="panel">

        <#if mostraArchivio!false>
            <h2>Richieste archiviate</h2>
        <#else>
            <h2>Richieste confermate</h2>
        </#if>

        <#if richieste?? && richieste?has_content>

            <div class="tabella">

                <table>
                    <thead>
                    <tr>
                        <th>Segnalante</th>
                        <th>Descrizione</th>
                        <th>Posizione</th>
                        <th>Foto</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>

                    <tbody>

                    <#list richieste as r>

                        <#assign stato =
                                (r.stato!"")?lower_case>

                        <tr>

                            <td>
                                <strong>
                                    ${(r.nome_segnalante!"-")?html}
                                </strong>

                                <br>

                                ${(r.email_segnalante!"-")?html}
                            </td>

                            <td>
                                ${(r.descrizione!"-")?html}
                            </td>

                            <td>
                                ${(r.indirizzo!"-")?html}
                            </td>

                            <td>
                                <#if r.pathFoto??
                                    && r.pathFoto?has_content>

                                    <a href="${contextPath}/${r.pathFoto?html}"
                                       target="_blank"
                                       rel="noopener">

                                        <img class="foto"
                                             src="${contextPath}/${r.pathFoto?html}"
                                             alt="Foto della richiesta">
                                    </a>

                                <#else>
                                    Nessuna
                                </#if>
                            </td>

                            <td>
                                <span class="stato ${stato?replace(' ', '-')}">
                                    ${stato?upper_case?html}
                                </span>
                            </td>

                            <td>
                                <div class="azioni">

                                    <#if mostraArchivio!false>

                                        <form action="${contextPath}/admin/richieste"
                                              method="post"
                                              onsubmit="return confirm(
                                                  'Ripristinare questa richiesta nella gestione ordinaria?'
                                              );">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="ripristina">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${(r.email_segnalante!"")?html}">

                                            <button class="btn-ripristina"
                                                    type="submit">
                                                Ripristina
                                            </button>

                                        </form>

                                    <#else>

                                        <#if stato == "attiva">

                                            <a class="btn btn-crea"
                                               href="${contextPath}/admin/missioni/nuova?richiestaId=${r.email_segnalante?url('UTF-8')}">
                                                Crea missione
                                            </a>

                                            <form action="${contextPath}/admin/richieste/ignora"
                                                  method="post"
                                                  onsubmit="return confirm(
                                                      'Ignorare questa richiesta?'
                                                  );">

                                                <input type="hidden"
                                                       name="email_segnalante"
                                                       value="${(r.email_segnalante!"")?html}">

                                                <button class="btn-ignora"
                                                        type="submit">
                                                    Ignora
                                                </button>

                                            </form>

                                        <#elseif stato == "chiusa">

                                            <form action="${contextPath}/admin/richieste"
                                                  method="post"
                                                  onsubmit="return confirm(
                                                      'Archiviare questa richiesta chiusa? Lo storico della missione verrà conservato.'
                                                  );">

                                                <input type="hidden"
                                                       name="azione"
                                                       value="archivia">

                                                <input type="hidden"
                                                       name="email"
                                                       value="${(r.email_segnalante!"")?html}">

                                                <button class="btn-archivia"
                                                        type="submit">
                                                    Archivia
                                                </button>

                                            </form>

                                        <#else>

                                            <span class="nessuna-azione">
                                                Nessuna azione disponibile
                                            </span>

                                        </#if>

                                    </#if>

                                </div>
                            </td>

                        </tr>

                    </#list>

                    </tbody>
                </table>

            </div>

        <#else>

            <#if mostraArchivio!false>
                <p>Non sono presenti richieste archiviate.</p>
            <#else>
                <p>Non sono presenti richieste confermate.</p>
            </#if>

        </#if>

    </section>

</main>

</body>
</html>