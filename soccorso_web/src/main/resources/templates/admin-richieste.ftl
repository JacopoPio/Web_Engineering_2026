<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione richieste - SoccorsoWeb</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; color: #222; }
        header { padding: 32px 20px; background: linear-gradient(135deg,#b71c1c,#d32f2f); color: white; text-align: center; }
        nav { padding: 14px; background: #8e0000; text-align: center; }
        nav a { color: white; margin: 0 10px; font-weight: bold; text-decoration: none; }
        main { width: min(1450px, 97%); margin: 30px auto; }
        .panel { padding: 25px; background: white; border-radius: 12px; box-shadow: 0 6px 20px rgba(0,0,0,.1); }
        .tabella { overflow-x: auto; }
        table { width: 100%; min-width: 1100px; border-collapse: collapse; }
        th { padding: 12px; background: #b71c1c; color: white; text-align: left; }
        td { padding: 12px; border-bottom: 1px solid #ddd; vertical-align: top; }
        .stato { display: inline-block; padding: 6px 10px; border-radius: 999px; font-weight: bold; }
        .attiva { background: #e8f5e9; color: #1b5e20; }
        .in-corso { background: #fff3e0; color: #e65100; }
        .chiusa { background: #eeeeee; color: #424242; }
        .ignorata { background: #ffebee; color: #b71c1c; }
        .azioni { display: flex; gap: 8px; flex-wrap: wrap; }
        .btn, button { padding: 9px 13px; border: 0; border-radius: 6px; color: white; font-weight: bold; text-decoration: none; cursor: pointer; }
        .btn-crea { background: #1565c0; }
        .btn-ignora { background: #c62828; }
        .msg { margin-bottom: 18px; padding: 14px; border-radius: 7px; }
        .ok { background: #e8f5e9; color: #1b5e20; border-left: 6px solid #2e7d32; }
        .errore { background: #ffebee; color: #b71c1c; border-left: 6px solid #b71c1c; }
        .foto { max-width: 110px; max-height: 80px; border-radius: 5px; }
    </style>
</head>
<body>
<header>
    <h1>Gestione richieste</h1>
    <p>Benvenuto ${(nome!"Amministratore")?html}</p>
</header>
<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/admin/missioni">Missioni</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>
<main>
    <#if successo??>
        <div class="msg ok">
            <#if successo == "ignorata">Richiesta ignorata correttamente.
            <#elseif successo == "missione_creata" || successo == "creata">Missione creata correttamente.
            <#else>Operazione completata correttamente.</#if>
        </div>
    </#if>

    <#if errore??>
        <div class="msg errore">
            <#if errore == "richiesta_non_trovata">Richiesta non trovata.
            <#elseif errore == "richiesta_non_attiva">L'operazione è consentita soltanto su richieste attive.
            <#elseif errore == "missione_esistente">Esiste già una missione per questa richiesta.
            <#elseif errore == "aggiornamento_fallito">Non è stato possibile aggiornare la richiesta.
            <#else>Non è stato possibile completare l'operazione.</#if>
        </div>
    </#if>

    <section class="panel">
        <h2>Richieste confermate</h2>
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
                        <#assign stato = (r.stato!"")?lower_case>
                        <tr>
                            <td>
                                <strong>${(r.nome_segnalante!"-")?html}</strong><br>
                                ${(r.email_segnalante!"-")?html}
                            </td>
                            <td>${(r.descrizione!"-")?html}</td>
                            <td>${(r.indirizzo!"-")?html}</td>
                            <td>
                                <#if r.pathFoto?? && r.pathFoto?has_content>
                                    <a href="${contextPath}/${r.pathFoto?html}" target="_blank" rel="noopener">
                                        <img class="foto" src="${contextPath}/${r.pathFoto?html}" alt="Foto richiesta">
                                    </a>
                                <#else>Nessuna</#if>
                            </td>
                            <td>
                                <span class="stato ${stato?replace(' ','-')}">${stato?upper_case?html}</span>
                            </td>
                            <td>
                                <div class="azioni">
                                    <#if stato == "attiva">
                                        <a class="btn btn-crea"
                                           href="${contextPath}/admin/missioni/nuova?richiestaId=${r.email_segnalante?url('UTF-8')}">
                                            Crea missione
                                        </a>
                                        <form action="${contextPath}/admin/richieste/ignora" method="post"
                                              onsubmit="return confirm('Ignorare questa richiesta?');">
                                            <input type="hidden" name="email_segnalante"
                                                   value="${(r.email_segnalante!"")?html}">
                                            <button class="btn-ignora" type="submit">Ignora</button>
                                        </form>
                                    <#else>
                                        Nessuna azione disponibile
                                    </#if>
                                </div>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        <#else>
            <p>Non sono presenti richieste confermate.</p>
        </#if>
    </section>
</main>
</body>
</html>
