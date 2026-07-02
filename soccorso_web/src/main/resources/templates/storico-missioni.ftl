<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${(titolo!"Storico missioni")?html} - SoccorsoWeb</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; color: #222; }
        header { padding: 30px 20px; background: linear-gradient(135deg,#1565c0,#1976d2); color: white; text-align: center; }
        main { width: min(1250px,96%); margin: 30px auto; }
        .panel { padding: 25px; background: white; border-radius: 12px; box-shadow: 0 5px 18px rgba(0,0,0,.1); }
        .tabella { overflow-x: auto; }
        table { width: 100%; min-width: 1050px; border-collapse: collapse; }
        th { padding: 12px; background: #1565c0; color: white; text-align: left; }
        td { padding: 12px; border-bottom: 1px solid #ddd; vertical-align: top; }
        .badge { display: inline-block; padding: 6px 10px; border-radius: 999px; font-weight: bold; background: #fff3e0; color: #e65100; }
        .chiusa { background: #eeeeee; color: #424242; }
        .vuoto { padding: 17px; background: #fff3cd; color: #664d03; border-radius: 7px; }
        .btn { display: inline-block; margin-top: 22px; padding: 10px 17px; background: #1565c0; color: white; border-radius: 6px; text-decoration: none; font-weight: bold; }
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
            <div class="tabella">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Obiettivo</th>
                        <th>Posizione</th>
                        <th>Stato</th>
                        <th>Inizio</th>
                        <th>Fine</th>
                        <th>Successo</th>
                        <th>Commento finale</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list missioni as missione>
                        <#assign stato = (missione.richiesta.stato!"")?lower_case>
                        <tr>
                            <td>${missione.id!"-"}</td>
                            <td>${(missione.descrizione!"-")?html}</td>
                            <td>${(missione.posizione!"-")?html}</td>
                            <td><span class="badge <#if stato == 'chiusa'>chiusa</#if>">${stato?upper_case?html}</span></td>
                            <td>${(missione.dataInizioFormattata!"-")?html}</td>
                            <td>${(missione.dataFineFormattata!"-")?html}</td>
                            <td><#if missione.successo??>${missione.successo}/5<#else>-</#if></td>
                            <td>${(missione.commentoFinale!"-")?html}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        <#else>
            <div class="vuoto">Questo elemento non è ancora stato coinvolto in alcuna missione.</div>
        </#if>
        <a class="btn" href="${ritorno}">Torna alla lista</a>
    </section>
</main>
</body>
</html>
