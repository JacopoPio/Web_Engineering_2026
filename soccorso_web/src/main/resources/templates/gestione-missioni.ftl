<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione missioni - SoccorsoWeb</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; color: #222; }
        header { padding: 30px; background: #1565c0; color: white; text-align: center; }
        nav { padding: 14px; background: #0d47a1; text-align: center; }
        nav a { color: white; margin: 0 12px; text-decoration: none; font-weight: bold; }
        main { width: min(1450px, 97%); margin: 30px auto; }
        .msg { margin-bottom: 18px; padding: 14px; border-radius: 7px; }
        .ok { background: #d4edda; color: #155724; }
        .errore { background: #f8d7da; color: #721c24; }
        .tabella { overflow-x: auto; }
        table { width: 100%; min-width: 1250px; border-collapse: collapse; background: white; box-shadow: 0 4px 15px rgba(0,0,0,.08); }
        th { padding: 12px; background: #1976d2; color: white; text-align: left; }
        td { padding: 12px; border-bottom: 1px solid #ddd; vertical-align: top; }
        ul { margin: 0; padding-left: 18px; }
        .btn { display: inline-block; padding: 8px 12px; border-radius: 5px; background: #1565c0; color: white; text-decoration: none; font-weight: bold; }
        .chiusura { min-width: 270px; }
        .chiusura select, .chiusura textarea { width: 100%; margin: 5px 0 8px; padding: 8px; }
        .chiusura button { width: 100%; padding: 9px; border: 0; border-radius: 5px; background: #c62828; color: white; font-weight: bold; cursor: pointer; }
        .vuoto { padding: 20px; background: white; border-radius: 8px; }
    </style>
</head>
<body>
<header><h1>Missioni in corso</h1></header>
<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/admin/missioni">Missioni</a>
</nav>
<main>
    <#if successo??>
        <div class="msg ok">
            <#if successo == "creata">Missione creata correttamente.
            <#elseif successo == "chiusa">Missione conclusa e risorse liberate correttamente.
            <#else>Operazione completata.</#if>
        </div>
    </#if>
    <#if errore??>
        <div class="msg errore">Non è stato possibile completare l'operazione: ${(errore!"errore")?html}</div>
    </#if>

    <#if missioni?? && missioni?has_content>
        <div class="tabella">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Obiettivo e posizione</th>
                    <th>Richiesta</th>
                    <th>Squadra</th>
                    <th>Operatori</th>
                    <th>Mezzi</th>
                    <th>Materiali</th>
                    <th>Aggiornamenti</th>
                    <th>Concludi</th>
                </tr>
                </thead>
                <tbody>
                <#list missioni as m>
                    <tr>
                        <td>${m.id}</td>
                        <td>
                            <strong>${(m.descrizione!"-")?html}</strong><br>
                            ${(m.posizione!"-")?html}
                        </td>
                        <td>
                            ${(m.richiesta.email_segnalante!"-")?html}
                        </td>
                        <td>${(m.squadra.nome!"-")?html}</td>
                        <td>
                            <#if m.operatori?? && m.operatori?has_content>
                                <ul><#list m.operatori as o><li>${(o.nome!"")?html} ${(o.cognome!"")?html}</li></#list></ul>
                            <#else>Nessuno</#if>
                        </td>
                        <td>
                            <#if m.mezzi?? && m.mezzi?has_content>
                                <ul><#list m.mezzi as me><li>${(me.targa!"-")?html}</li></#list></ul>
                            <#else>Nessuno</#if>
                        </td>
                        <td>
                            <#if m.materiali?? && m.materiali?has_content>
                                <ul><#list m.materiali as ma><li>${(ma.tipo!"-")?html}</li></#list></ul>
                            <#else>Nessuno</#if>
                        </td>
                        <td>
                            <a class="btn" href="${contextPath}/admin/missioni/aggiornamenti?id=${m.id}">Apri aggiornamenti</a>
                        </td>
                        <td>
                            <form class="chiusura" action="${contextPath}/admin/missioni/chiudi" method="post"
                                  onsubmit="return confirm('Concludere definitivamente la missione?');">
                                <input type="hidden" name="idMissione" value="${m.id}">
                                <label>Successo (0-5)</label>
                                <select name="successo" required>
                                    <option value="">Seleziona</option>
                                    <#list 0..5 as valore><option value="${valore}">${valore}</option></#list>
                                </select>
                                <label>Commento finale</label>
                                <textarea name="commentoFinale" rows="3" maxlength="1000"></textarea>
                                <button type="submit">Concludi missione</button>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <div class="vuoto">Non sono presenti missioni in corso.</div>
    </#if>
</main>
</body>
</html>
