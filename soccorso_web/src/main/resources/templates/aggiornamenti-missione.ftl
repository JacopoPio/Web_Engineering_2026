<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aggiornamenti missione</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; color: #222; }
        header { padding: 30px; background: #1565c0; color: white; text-align: center; }
        nav { padding: 14px; background: #0d47a1; text-align: center; }
        nav a { color: white; margin: 0 12px; text-decoration: none; font-weight: bold; }
        main { width: min(950px, 94%); margin: 30px auto; }
        .panel, .aggiornamento { margin-bottom: 18px; padding: 20px; background: white; border-radius: 9px; box-shadow: 0 3px 12px rgba(0,0,0,.08); }
        textarea { width: 100%; min-height: 100px; padding: 10px; }
        button, .btn { display: inline-block; margin-top: 12px; padding: 10px 17px; border: 0; border-radius: 5px; background: #1565c0; color: white; text-decoration: none; font-weight: bold; cursor: pointer; }
        .errore { margin-bottom: 18px; padding: 14px; background: #f8d7da; color: #721c24; border-radius: 6px; }
        .chiusa { margin-bottom: 18px; padding: 14px; background: #eeeeee; color: #424242; border-radius: 6px; }
        .data { color: #666; font-size: .9rem; margin-bottom: 6px; }
    </style>
</head>
<body>
<header>
    <h1>Aggiornamenti missione #${missione.id}</h1>
    <p>${(missione.descrizione!"-")?html}</p>
</header>
<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/missioni">Missioni</a>
</nav>
<main>
    <#if errore??><div class="errore">${errore?html}</div></#if>

    <#if missioneChiusa!false>
        <div class="chiusa">La missione è chiusa: gli aggiornamenti restano consultabili ma non possono essere modificati.</div>
    <#else>
        <section class="panel">
            <form method="post" action="${contextPath}/admin/missioni/aggiornamenti?id=${missione.id}">
                <label for="descrizione"><strong>Nuovo aggiornamento</strong></label>
                <textarea id="descrizione" name="descrizione" maxlength="1000" required></textarea>
                <button type="submit">Inserisci aggiornamento</button>
            </form>
        </section>
    </#if>

    <#if aggiornamenti?? && aggiornamenti?has_content>
        <#list aggiornamenti as a>
            <article class="aggiornamento">
                <div class="data">${(a.dataFormattata!"-")?html}</div>
                <div>${(a.descrizione!"-")?html}</div>
            </article>
        </#list>
    <#else>
        <section class="panel">Non sono presenti aggiornamenti.</section>
    </#if>

    <a class="btn" href="${contextPath}/admin/missioni">Torna alle missioni</a>
</main>
</body>
</html>
