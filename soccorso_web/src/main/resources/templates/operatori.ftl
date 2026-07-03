<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area operatore - SoccorsoWeb</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; color: #222; }
        header { padding: 32px 20px; background: linear-gradient(135deg,#1565c0,#1976d2); color: white; text-align: center; }
        nav { padding: 14px; background: #0d47a1; text-align: center; }
        nav a { color: white; margin: 0 12px; font-weight: bold; text-decoration: none; }
        main { width: min(1150px, 95%); margin: 30px auto; }
        .panel { margin-bottom: 25px; padding: 25px; background: white; border-radius: 12px; box-shadow: 0 5px 18px rgba(0,0,0,.09); }
        h2 { color: #1565c0; }
        .profilo { display: grid; grid-template-columns: repeat(auto-fit,minmax(220px,1fr)); gap: 12px; }
        .dato { padding: 13px; border: 1px solid #d7e5f5; border-radius: 7px; background: #f7fbff; }
        .etichetta { display: block; color: #555; font-size: .9rem; font-weight: bold; margin-bottom: 4px; }
        .missione { margin-top: 18px; padding: 20px; border-left: 6px solid #1976d2; background: #f8fafc; border-radius: 9px; }
        .missione.chiusa { border-left-color: #616161; }
        .badge { display: inline-block; padding: 6px 10px; border-radius: 999px; font-weight: bold; background: #fff3e0; color: #e65100; }
        .badge.chiusa { background: #eeeeee; color: #424242; }
        ul { padding-left: 20px; }
        .vuoto { padding: 18px; background: #fff3cd; color: #664d03; border-radius: 7px; }
        .aggiornamenti { margin-top: 15px; padding-top: 12px; border-top: 1px solid #ddd; }
        .messaggio { padding: 14px; margin-bottom: 18px; border-radius: 7px; font-weight: bold; }
        .ok { background: #e8f5e9; color: #1b5e20; border-left: 5px solid #2e7d32; }
        .errore { background: #ffebee; color: #b71c1c; border-left: 5px solid #c62828; }
        .form-profilo { display: grid; grid-template-columns: repeat(auto-fit,minmax(220px,1fr)); gap: 14px; margin-top: 20px; }
        .campo label { display: block; margin-bottom: 6px; font-weight: bold; }
        .campo input { width: 100%; padding: 10px; border: 1px solid #bbb; border-radius: 6px; }
        fieldset { grid-column: 1 / -1; border: 1px solid #c7d1da; border-radius: 8px; padding: 15px; }
        legend { color: #0d47a1; font-weight: bold; padding: 0 7px; }
        .scelte { display: flex; flex-wrap: wrap; gap: 9px; }
        .scelta { display: inline-flex; align-items: center; gap: 6px; padding: 8px 11px; border: 1px solid #d1d9e0; border-radius: 6px; background: #f8fafc; }
        .scelta input { width: auto; }
        .azioni { grid-column: 1 / -1; }
        button { padding: 11px 18px; border: 0; border-radius: 6px; background: #1565c0; color: white; font-weight: bold; cursor: pointer; }
        button:hover { background: #0d47a1; }
    </style>
</head>
<body>
<header>
    <h1>Area operatore</h1>
    <p>Benvenuto ${(nome!"Operatore")?html}</p>
</header>
<nav>
    <a href="${contextPath}/operatori">Dashboard e missioni</a>
    <a href="${contextPath}/logout">Logout</a>
    <a href=${contextPath}/cambio-password>Cambia Password</a>
</nav>
<main>
    <#if successo??>
        <div class="messaggio ok">Profilo aggiornato correttamente.</div>
    </#if>
    <#if errore??>
        <div class="messaggio errore">
            <#if errore == "campi">Nome e cognome sono obbligatori.
            <#elseif errore == "data">La data di nascita non è valida.
            <#elseif errore == "selezione">Una patente o abilità selezionata non esiste.
            <#else>Non è stato possibile aggiornare il profilo.</#if>
        </div>
    </#if>

    <section class="panel">
        <h2>Profilo</h2>
        <div class="profilo">
            <div class="dato"><span class="etichetta">Email</span>${(operatore.email!"-")?html}</div>
            <div class="dato"><span class="etichetta">Nome</span>${(operatore.nome!"-")?html} ${(operatore.cognome!"")?html}</div>
            <div class="dato"><span class="etichetta">Codice fiscale</span>${(operatore.CF!"-")?html}</div>
            <div class="dato">
                <span class="etichetta">Squadra attuale</span>
                <#if operatore.squadra??>${(operatore.squadra.nome!"Squadra senza nome")?html}<#else>Nessuna squadra</#if>
            </div>
            <div class="dato">
                <span class="etichetta">Patenti</span>
                <#if operatore.patenti?? && operatore.patenti?has_content>
                    <#list operatore.patenti as p>${(p.tipoPatente!"-")?html}<#sep>, </#list>
                <#else>Nessuna</#if>
            </div>
            <div class="dato">
                <span class="etichetta">Abilità</span>
                <#if operatore.abilita?? && operatore.abilita?has_content>
                    <#list operatore.abilita as a>${(a.nome!"-")?html}<#sep>, </#list>
                <#else>Nessuna</#if>
            </div>
        </div>

        <h3>Modifica dati personali</h3>
        <form class="form-profilo" method="post" action="${contextPath}/operatori">
            <div class="campo">
                <label for="nome">Nome</label>
                <input id="nome" name="nome" type="text" maxlength="50"
                       value="${(operatore.nome!"")?html}" required>
            </div>
            <div class="campo">
                <label for="cognome">Cognome</label>
                <input id="cognome" name="cognome" type="text" maxlength="50"
                       value="${(operatore.cognome!"")?html}" required>
            </div>
            <div class="campo">
                <label for="data_nascita">Data di nascita</label>
                <input id="data_nascita" name="data_nascita" type="date"
                       value="${(dataNascita!"")?html}">
            </div>
            <div class="campo">
                <label for="citta_nascita">Città di nascita</label>
                <input id="citta_nascita" name="citta_nascita" type="text" maxlength="100"
                       value="${(operatore.citta_nascita!"")?html}">
            </div>
            <div class="campo">
                <label for="indirizzo">Indirizzo</label>
                <input id="indirizzo" name="indirizzo" type="text" maxlength="150"
                       value="${(operatore.indirizzo!"")?html}">
            </div>
            <fieldset>
                <legend>Patenti</legend>
                <div class="scelte">
                    <#if listaPatenti?? && listaPatenti?has_content>
                        <#list listaPatenti as patente>
                            <label class="scelta">
                                <input type="checkbox" name="patenti"
                                       value="${patente.tipoPatente?html}"
                                       <#if patentiSelezionate?? && patentiSelezionate?seq_contains(patente.tipoPatente)>checked</#if>>
                                ${patente.tipoPatente?html}
                            </label>
                        </#list>
                    <#else>Nessuna patente disponibile.</#if>
                </div>
            </fieldset>

            <fieldset>
                <legend>Abilità</legend>
                <div class="scelte">
                    <#if listaAbilita?? && listaAbilita?has_content>
                        <#list listaAbilita as voce>
                            <label class="scelta">
                                <input type="checkbox" name="abilita"
                                       value="${voce.nome?html}"
                                       <#if abilitaSelezionate?? && abilitaSelezionate?seq_contains(voce.nome)>checked</#if>>
                                ${voce.nome?html}
                            </label>
                        </#list>
                    <#else>Nessuna abilità disponibile.</#if>
                </div>
            </fieldset>

            <div class="azioni">
                <button type="submit">Salva profilo</button>
            </div>
        </form>
    </section>

    <section class="panel">
        <h2>Missioni associate</h2>
        <#if missioni?? && missioni?has_content>
            <#list missioni as m>
                <#assign chiusa = (m.stato!"")?lower_case == "chiusa">
                <article class="missione <#if chiusa>chiusa</#if>">
                    <h3>Missione #${m.id} — ${(m.descrizione!"Nessuna descrizione")?html}</h3>
                    <p><span class="badge <#if chiusa>chiusa</#if>">${(m.stato!"-")?upper_case?html}</span></p>
                    <p><strong>Posizione:</strong> ${(m.posizione!"-")?html}</p>
                    <p><strong>Inizio:</strong> ${(m.dataInizio!"-")?html}</p>
                    <p><strong>Fine:</strong> ${(m.dataFine!"-")?html}</p>
                    <#if m.successo??>
                        <p><strong>Successo:</strong> ${m.successo}/5</p>
                    </#if>
                    <#if m.commentoFinale?? && m.commentoFinale?has_content>
                        <p><strong>Commento finale:</strong> ${m.commentoFinale?html}</p>
                    </#if>

                    <div class="aggiornamenti">
                        <strong>Aggiornamenti</strong>
                        <#if m.aggiornamenti?? && m.aggiornamenti?has_content>
                            <ul>
                                <#list m.aggiornamenti as a>
                                    <li><strong>${(a.data!"-")?html}</strong> — ${(a.descrizione!"-")?html}</li>
                                </#list>
                            </ul>
                        <#else>
                            <p>Nessun aggiornamento presente.</p>
                        </#if>
                    </div>
                </article>
            </#list>
        <#else>
            <div class="vuoto">Non sei ancora stato coinvolto in alcuna missione.</div>
        </#if>
    </section>
</main>
</body>
</html>
