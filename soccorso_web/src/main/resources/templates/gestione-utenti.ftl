<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione utenti - SoccorsoWeb</title>

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

        header h1 { margin: 0 0 8px; font-size: 36px; }
        header p { margin: 0; }

        nav {
            padding: 14px;
            background: #0d47a1;
            text-align: center;
        }

        nav a {
            display: inline-block;
            margin: 5px 10px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        nav a:hover { text-decoration: underline; }

        main {
            width: min(1250px, 95%);
            margin: 30px auto;
        }

        .azioni-pagina {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            margin-bottom: 22px;
            padding: 18px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .azioni-pagina h2 { margin: 0 0 5px; color: #0d47a1; }
        .azioni-pagina p { margin: 0; color: #555; }

        .messaggio {
            margin-bottom: 20px;
            padding: 14px 16px;
            border-radius: 7px;
            font-weight: bold;
        }

        .messaggio-successo {
            border: 1px solid #86c99a;
            background: #dff3e5;
            color: #166534;
        }

        .messaggio-errore {
            border: 1px solid #ef9a9a;
            background: #fde2e2;
            color: #991b1b;
        }

        .riepilogo {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 16px;
            margin-bottom: 22px;
        }

        .scheda-riepilogo {
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .scheda-riepilogo .numero {
            display: block;
            margin-bottom: 6px;
            color: #1565c0;
            font-size: 30px;
            font-weight: bold;
        }

        .scheda-riepilogo .etichetta { color: #555; font-weight: bold; }

        .pannello {
            margin-bottom: 28px;
            padding: 24px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        .intestazione-pannello {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            margin-bottom: 18px;
        }

        .intestazione-pannello h2 { margin: 0; color: #0d47a1; }

        .ricerca {
            width: min(330px, 100%);
            padding: 10px 12px;
            border: 1px solid #b8c2cc;
            border-radius: 6px;
            font: inherit;
        }

        .ricerca:focus {
            outline: 2px solid #90caf9;
            border-color: #1565c0;
        }

        .tabella-contenitore { overflow-x: auto; }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 850px;
        }

        th, td {
            padding: 12px 14px;
            border-bottom: 1px solid #e0e0e0;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #eaf2fb;
            color: #0d47a1;
            font-size: 14px;
        }

        tbody tr:hover { background: #fafafa; }
        .nome-utente { font-weight: bold; }
        .email { color: #555; overflow-wrap: anywhere; }

        .badge {
            display: inline-block;
            padding: 5px 9px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: bold;
        }

        .badge-attivo { background: #dff3e5; color: #166534; }
        .badge-disattivo { background: #fde2e2; color: #991b1b; }
        .badge-caposquadra { background: #fff3cd; color: #664d03; }
        .badge-standard { background: #e5e7eb; color: #374151; }
        .badge-squadra { background: #dbeafe; color: #1e40af; }

        .azioni {
            display: flex;
            flex-wrap: wrap;
            gap: 7px;
        }

        .azioni form { margin: 0; }

        .pulsante, button {
            display: inline-block;
            padding: 8px 12px;
            border: none;
            border-radius: 5px;
            color: white;
            font: inherit;
            font-size: 13px;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
        }

        .pulsante-primario { background: #1565c0; }
        .pulsante-primario:hover { background: #0d47a1; }
        .pulsante-successo { background: #2e7d32; }
        .pulsante-successo:hover { background: #1b5e20; }
        .pulsante-avviso { background: #ef6c00; }
        .pulsante-avviso:hover { background: #e65100; }
        .pulsante-pericolo { background: #c62828; }
        .pulsante-pericolo:hover { background: #8e0000; }
        .pulsante-secondario { background: #607d8b; }
        .pulsante-secondario:hover { background: #455a64; }

        .vuoto {
            margin: 0;
            padding: 20px;
            border-radius: 7px;
            background: #f7f7f7;
            color: #666;
            text-align: center;
        }

        footer {
            margin-top: 35px;
            padding: 20px;
            background: #263238;
            color: white;
            text-align: center;
        }

        @media (max-width: 850px) {
            .riepilogo { grid-template-columns: 1fr; }

            .azioni-pagina,
            .intestazione-pannello {
                align-items: stretch;
                flex-direction: column;
            }

            .pulsante-nuovo {
                width: 100%;
                text-align: center;
            }

            .ricerca { width: 100%; }
        }
    </style>
</head>

<body>

<header>
    <h1>Gestione utenti</h1>

    <#if nomeAdmin??>
        <p>Amministratore: ${(nomeAdmin!"")?html}</p>
    <#else>
        <p>Amministrazione degli utenti di SoccorsoWeb</p>
    </#if>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/admin/missioni">Missioni</a>
    <a href="${contextPath}/admin/utenti">Gestione utenti</a>
    <a href="${contextPath}/admin/nuovo-utente">Nuovo utente</a>
    <a href="${contextPath}/mezzi">Mezzi</a>
    <a href="${contextPath}/materiali">Materiali</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>

    <#if successo??>
        <div class="messaggio messaggio-successo">
            <#if successo == "modificato">
                Utente modificato correttamente.
            <#elseif successo == "disattivato">
                Utente disattivato correttamente.
            <#elseif successo == "riattivato">
                Utente riattivato correttamente.
            <#elseif successo == "caposquadra_assegnato">
                L'operatore è stato abilitato come caposquadra.
            <#elseif successo == "caposquadra_rimosso">
                L'abilitazione di caposquadra è stata rimossa.
            <#elseif successo == "rimosso_da_squadra">
                L'operatore è stato rimosso dalla squadra.
            <#else>
                Operazione completata correttamente.
            </#if>
        </div>
    </#if>

    <#if errore??>
        <div class="messaggio messaggio-errore">
            <#if errore == "parametri">
                I dati dell'utente selezionato non sono validi.
            <#elseif errore == "azione">
                L'operazione richiesta non è valida.
            <#elseif errore == "utente_non_trovato">
                L'utente selezionato non è stato trovato.
            <#elseif errore == "autodisattivazione">
                Non puoi disattivare l'account con cui hai effettuato l'accesso.
            <#elseif errore == "ultimo_admin">
                Non puoi disattivare l'ultimo amministratore attivo.
            <#elseif errore == "caposquadra_non_attivo">
                Un operatore disattivato non può essere abilitato come caposquadra.
            <#elseif errore == "operatore_non_assegnato">
                L'operatore non appartiene a nessuna squadra.
            <#else>
                Si è verificato un errore durante l'operazione.
            </#if>
        </div>
    </#if>

    <section class="azioni-pagina">
        <div>
            <h2>Utenti registrati</h2>
            <p>Visualizza, modifica, attiva o disattiva amministratori e operatori.</p>
        </div>

        <a class="pulsante pulsante-primario pulsante-nuovo"
           href="${contextPath}/admin/nuovo-utente">
            Crea nuovo utente
        </a>
    </section>

    <#assign numeroAmministratori = (amministratori![])?size>
    <#assign numeroOperatori = (operatori![])?size>
    <#assign operatoriAttivi = 0>

    <#if operatori??>
        <#list operatori as operatore>
            <#if (operatore.attivo!false)>
                <#assign operatoriAttivi = operatoriAttivi + 1>
            </#if>
        </#list>
    </#if>

    <section class="riepilogo">
        <div class="scheda-riepilogo">
            <span class="numero">${numeroAmministratori}</span>
            <span class="etichetta">Amministratori</span>
        </div>

        <div class="scheda-riepilogo">
            <span class="numero">${numeroOperatori}</span>
            <span class="etichetta">Operatori</span>
        </div>

        <div class="scheda-riepilogo">
            <span class="numero">${operatoriAttivi}</span>
            <span class="etichetta">Operatori attivi</span>
        </div>
    </section>

    <section class="pannello">
        <div class="intestazione-pannello">
            <h2>Amministratori</h2>
        </div>

        <#if amministratori?? && amministratori?has_content>
            <div class="tabella-contenitore">
                <table id="tabella-amministratori">
                    <thead>
                    <tr>
                        <th>Utente</th>
                        <th>Email</th>
                        <th>Codice fiscale</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list amministratori as amministratore>
                        <tr>
                            <td class="nome-utente">
                                ${(amministratore.nome!"")?html}
                                ${(amministratore.cognome!"")?html}
                            </td>

                            <td class="email">${(amministratore.email!"")?html}</td>
                            <td>${(amministratore.CF!"Non indicato")?html}</td>

                            <td>
                                <#if (amministratore.attivo!false)>
                                    <span class="badge badge-attivo">Attivo</span>
                                <#else>
                                    <span class="badge badge-disattivo">Disattivato</span>
                                </#if>
                            </td>

                            <td>
                                <div class="azioni">
                                    <form method="get" action="${contextPath}/admin/utenti">
                                        <input type="hidden" name="azione" value="modifica">
                                        <input type="hidden" name="ruolo" value="ADMIN">
                                        <input type="hidden" name="email"
                                               value="${(amministratore.email!"")?html}">
                                        <button type="submit" class="pulsante-primario">
                                            Modifica
                                        </button>
                                    </form>

                                    <#if (amministratore.attivo!false)>
                                        <form method="post"
                                              action="${contextPath}/admin/utenti"
                                              onsubmit="return confirm('Disattivare questo amministratore?');">
                                            <input type="hidden" name="azione" value="disattiva">
                                            <input type="hidden" name="ruolo" value="ADMIN">
                                            <input type="hidden" name="email"
                                                   value="${(amministratore.email!"")?html}">
                                            <button type="submit" class="pulsante-pericolo">
                                                Disattiva
                                            </button>
                                        </form>
                                    <#else>
                                        <form method="post" action="${contextPath}/admin/utenti">
                                            <input type="hidden" name="azione" value="riattiva">
                                            <input type="hidden" name="ruolo" value="ADMIN">
                                            <input type="hidden" name="email"
                                                   value="${(amministratore.email!"")?html}">
                                            <button type="submit" class="pulsante-successo">
                                                Riattiva
                                            </button>
                                        </form>
                                    </#if>
                                </div>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        <#else>
            <p class="vuoto">Nessun amministratore presente.</p>
        </#if>
    </section>

    <section class="pannello">
        <div class="intestazione-pannello">
            <h2>Operatori</h2>
        </div>

        <#if operatori?? && operatori?has_content>
            <div class="tabella-contenitore">
                <table id="tabella-operatori">
                    <thead>
                    <tr>
                        <th>Operatore</th>
                        <th>Email</th>
                        <th>Stato</th>
                        <th>Ruolo operativo</th>
                        <th>Squadra</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list operatori as operatore>
                        <tr>
                            <td class="nome-utente">
                                ${(operatore.nome!"")?html}
                                ${(operatore.cognome!"")?html}
                            </td>

                            <td class="email">${(operatore.email!"")?html}</td>

                            <td>
                                <#if (operatore.attivo!false)>
                                    <span class="badge badge-attivo">Attivo</span>
                                <#else>
                                    <span class="badge badge-disattivo">Disattivato</span>
                                </#if>
                            </td>

                            <td>
                                <#if (operatore.caposquadra!false)>
                                    <span class="badge badge-caposquadra">Caposquadra</span>
                                <#else>
                                    <span class="badge badge-standard">Operatore</span>
                                </#if>
                            </td>

                            <td>
                                <#if operatore.squadra??>
                                    <span class="badge badge-squadra">
                                        ${(operatore.squadra.nome!"Squadra assegnata")?html}
                                    </span>
                                <#else>
                                    <span class="badge badge-standard">Non assegnato</span>
                                </#if>
                            </td>

                            <td>
                                <div class="azioni">
                                    <form method="get" action="${contextPath}/admin/utenti">
                                        <input type="hidden" name="azione" value="modifica">
                                        <input type="hidden" name="ruolo" value="OPERATORE">
                                        <input type="hidden" name="email"
                                               value="${(operatore.email!"")?html}">
                                        <button type="submit" class="pulsante-primario">
                                            Modifica
                                        </button>
                                    </form>

                                    <#if (operatore.attivo!false)>
                                        <form method="post"
                                              action="${contextPath}/admin/utenti"
                                              onsubmit="return confirm('Disattivare questo operatore?');">
                                            <input type="hidden" name="azione" value="disattiva">
                                            <input type="hidden" name="ruolo" value="OPERATORE">
                                            <input type="hidden" name="email"
                                                   value="${(operatore.email!"")?html}">
                                            <button type="submit" class="pulsante-pericolo">
                                                Disattiva
                                            </button>
                                        </form>
                                    <#else>
                                        <form method="post" action="${contextPath}/admin/utenti">
                                            <input type="hidden" name="azione" value="riattiva">
                                            <input type="hidden" name="ruolo" value="OPERATORE">
                                            <input type="hidden" name="email"
                                                   value="${(operatore.email!"")?html}">
                                            <button type="submit" class="pulsante-successo">
                                                Riattiva
                                            </button>
                                        </form>
                                    </#if>

                                    <#if (operatore.caposquadra!false)>
                                        <form method="post"
                                              action="${contextPath}/admin/utenti"
                                              onsubmit="return confirm('Rimuovere l’abilitazione di caposquadra?');">
                                            <input type="hidden" name="azione" value="rimuovi_caposquadra">
                                            <input type="hidden" name="email"
                                                   value="${(operatore.email!"")?html}">
                                            <button type="submit" class="pulsante-avviso">
                                                Rimuovi caposquadra
                                            </button>
                                        </form>
                                    <#else>
                                        <form method="post" action="${contextPath}/admin/utenti">
                                            <input type="hidden" name="azione" value="rendi_caposquadra">
                                            <input type="hidden" name="email"
                                                   value="${(operatore.email!"")?html}">
                                            <button type="submit" class="pulsante-successo">
                                                Rendi caposquadra
                                            </button>
                                        </form>
                                    </#if>

                                    <#if operatore.squadra??>
                                        <form method="post"
                                              action="${contextPath}/admin/utenti"
                                              onsubmit="return confirm('Rimuovere l’operatore dalla squadra?');">
                                            <input type="hidden" name="azione" value="rimuovi_da_squadra">
                                            <input type="hidden" name="email"
                                                   value="${(operatore.email!"")?html}">
                                            <button type="submit" class="pulsante-secondario">
                                                Rimuovi dalla squadra
                                            </button>
                                        </form>
                                    </#if>
                                </div>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        <#else>
            <p class="vuoto">Nessun operatore presente.</p>
        </#if>
    </section>

</main>

<footer>
    SoccorsoWeb – Gestione utenti
</footer>

</body>
</html>
