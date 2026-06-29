<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Gestione utenti - SoccorsoWeb</title>

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
            text-align: center;
            padding: 35px 20px;
        }

        header h1 {
            margin: 0;
            font-size: 38px;
        }

        header p {
            margin: 8px 0 0;
        }

        nav {
            background: #0d47a1;
            text-align: center;
            padding: 14px;
        }

        nav a {
            color: white;
            text-decoration: none;
            font-weight: bold;
            margin: 0 12px;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: 95%;
            max-width: 1550px;
            margin: 30px auto;
        }

        .messaggio {
            padding: 14px 16px;
            margin-bottom: 22px;
            border-radius: 7px;
            font-weight: bold;
        }

        .messaggio-successo {
            background: #dff2e1;
            border: 1px solid #81c784;
            color: #1b5e20;
        }

        .messaggio-errore {
            background: #fde0e0;
            border: 1px solid #ef9a9a;
            color: #b71c1c;
        }

        .sezione {
            background: white;
            border-radius: 9px;
            padding: 24px;
            margin-bottom: 32px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.10);
        }

        .sezione h2 {
            margin: 0 0 20px;
            color: #0d47a1;
            font-size: 28px;
        }

        .tabella-container {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th,
        td {
            padding: 13px;
            border-bottom: 1px solid #ddd;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #e3f2fd;
            color: #0d47a1;
            white-space: nowrap;
        }

        tbody tr:hover {
            background: #f8f9fa;
        }

        .azioni {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 8px;
            min-width: 300px;
        }

        .azioni form {
            margin: 0;
        }

        button {
            border: none;
            border-radius: 5px;
            padding: 9px 13px;
            color: white;
            font-weight: bold;
            cursor: pointer;
            white-space: nowrap;
        }

        .btn-modifica {
            background: #1565c0;
        }

        .btn-modifica:hover {
            background: #0d47a1;
        }

        .btn-disattiva {
            background: #c62828;
        }

        .btn-disattiva:hover {
            background: #8e0000;
        }

        .btn-riattiva {
            background: #2e7d32;
        }

        .btn-riattiva:hover {
            background: #1b5e20;
        }

        .btn-caposquadra {
            background: #2e7d32;
        }

        .btn-caposquadra:hover {
            background: #1b5e20;
        }

        .btn-rimuovi-caposquadra {
            background: #6a1b9a;
        }

        .btn-rimuovi-caposquadra:hover {
            background: #4a148c;
        }

        .btn-rimuovi-squadra {
            background: #ef6c00;
        }

        .btn-rimuovi-squadra:hover {
            background: #e65100;
        }

        .btn-disabilitato {
            background: #9e9e9e;
            cursor: not-allowed;
            opacity: 0.65;
        }

        .stato-attivo {
            color: #1b5e20;
            font-weight: bold;
        }

        .stato-disattivato {
            color: #b71c1c;
            font-weight: bold;
        }

        .squadra-assegnata {
            color: #1b5e20;
            font-weight: bold;
        }

        .non-assegnato {
            color: #666;
            font-style: italic;
        }

        .badge-caposquadra {
            display: inline-block;
            background: #dcedc8;
            border: 1px solid #81c784;
            color: #1b5e20;
            border-radius: 5px;
            padding: 7px 10px;
            font-weight: bold;
        }

        .nessun-utente {
            color: #666;
            font-style: italic;
        }

        @media (max-width: 900px) {
            main {
                width: 98%;
            }

            .sezione {
                padding: 15px;
            }

            th,
            td {
                padding: 10px;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Gestione utenti</h1>

    <#if nomeAdmin??>
        <p>
            Amministratore: ${nomeAdmin}
        </p>
    </#if>
</header>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

    <a href="${contextPath}/admin/utenti">
        Gestione utenti
    </a>

    <a href="${contextPath}/admin/nuovo-utente">
        Nuovo utente
    </a>

    <a href="${contextPath}/logout">
        Logout
    </a>
</nav>

<main>

    <#if successo??>

        <#if successo == "modificato">

            <div class="messaggio messaggio-successo">
                Utente modificato correttamente.
            </div>

        <#elseif successo == "disattivato">

            <div class="messaggio messaggio-successo">
                Utente disattivato correttamente.
            </div>

        <#elseif successo == "riattivato">

            <div class="messaggio messaggio-successo">
                Utente riattivato correttamente.
            </div>

        <#elseif successo == "caposquadra_assegnato">

            <div class="messaggio messaggio-successo">
                L'operatore è stato impostato come caposquadra.
            </div>

        <#elseif successo == "caposquadra_rimosso">

            <div class="messaggio messaggio-successo">
                Il ruolo di caposquadra è stato rimosso.
            </div>

        <#elseif successo == "rimosso_da_squadra">

            <div class="messaggio messaggio-successo">
                L'operatore è stato rimosso dalla squadra.
            </div>

        <#else>

            <div class="messaggio messaggio-successo">
                Operazione completata correttamente.
            </div>

        </#if>

    </#if>

    <#if errore??>

        <#if errore == "azione">

            <div class="messaggio messaggio-errore">
                L'azione richiesta non è valida.
            </div>

        <#elseif errore == "parametri">

            <div class="messaggio messaggio-errore">
                I parametri ricevuti non sono validi.
            </div>

        <#elseif errore == "utente_non_trovato">

            <div class="messaggio messaggio-errore">
                L'utente selezionato non è stato trovato.
            </div>

        <#elseif errore == "autodisattivazione">

            <div class="messaggio messaggio-errore">
                Non puoi disattivare il tuo account.
            </div>

        <#elseif errore == "ultimo_admin">

            <div class="messaggio messaggio-errore">
                Non puoi disattivare l'ultimo amministratore attivo.
            </div>

        <#elseif errore == "caposquadra_non_attivo">

            <div class="messaggio messaggio-errore">
                Un operatore disattivato non può diventare caposquadra.
            </div>

        <#elseif errore == "operatore_non_assegnato">

            <div class="messaggio messaggio-errore">
                L'operatore non appartiene a una squadra.
            </div>

        <#else>

            <div class="messaggio messaggio-errore">
                Si è verificato un errore durante l'operazione.
            </div>

        </#if>

    </#if>

    <section class="sezione">

        <h2>Amministratori</h2>

        <#assign listaAmministratori = amministratori![]>

        <#if listaAmministratori?size == 0>

            <p class="nessun-utente">
                Non sono presenti amministratori.
            </p>

        <#else>

            <div class="tabella-container">

                <table>

                    <thead>
                    <tr>
                        <th>Email</th>
                        <th>Nome</th>
                        <th>Cognome</th>
                        <th>Codice fiscale</th>
                        <th>Stato</th>
                        <th>Operazioni</th>
                    </tr>
                    </thead>

                    <tbody>

                    <#list listaAmministratori as amministratore>

                        <#assign adminAttivo =
                            amministratore.attivo!false>

                        <tr>

                            <td>${amministratore.email!""}</td>

                            <td>${amministratore.nome!""}</td>

                            <td>${amministratore.cognome!""}</td>

                            <td>${amministratore.CF!""}</td>

                            <td>

                                <#if adminAttivo>

                                    <span class="stato-attivo">
                                        Attivo
                                    </span>

                                <#else>

                                    <span class="stato-disattivato">
                                        Disattivato
                                    </span>

                                </#if>

                            </td>

                            <td>

                                <div class="azioni">

                                    <form action="${contextPath}/admin/utenti"
                                          method="get">

                                        <input type="hidden"
                                               name="azione"
                                               value="modifica">

                                        <input type="hidden"
                                               name="ruolo"
                                               value="ADMIN">

                                        <input type="hidden"
                                               name="email"
                                               value="${amministratore.email!""}">

                                        <button type="submit"
                                                class="btn-modifica">
                                            Modifica
                                        </button>

                                    </form>

                                    <#if adminAttivo>

                                        <form action="${contextPath}/admin/utenti"
                                              method="post"
                                              onsubmit="return confirm('Disattivare questo amministratore?');">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="disattiva">

                                            <input type="hidden"
                                                   name="ruolo"
                                                   value="ADMIN">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${amministratore.email!""}">

                                            <button type="submit"
                                                    class="btn-disattiva">
                                                Disattiva
                                            </button>

                                        </form>

                                    <#else>

                                        <form action="${contextPath}/admin/utenti"
                                              method="post"
                                              onsubmit="return confirm('Riattivare questo amministratore?');">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="riattiva">

                                            <input type="hidden"
                                                   name="ruolo"
                                                   value="ADMIN">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${amministratore.email!""}">

                                            <button type="submit"
                                                    class="btn-riattiva">
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

        </#if>

    </section>

    <section class="sezione">

        <h2>Operatori</h2>

        <#assign listaOperatori = operatori![]>

        <#if listaOperatori?size == 0>

            <p class="nessun-utente">
                Non sono presenti operatori.
            </p>

        <#else>

            <div class="tabella-container">

                <table>

                    <thead>
                    <tr>
                        <th>Email</th>
                        <th>Nome</th>
                        <th>Cognome</th>
                        <th>Codice fiscale</th>
                        <th>Stato</th>
                        <th>Squadra</th>
                        <th>Caposquadra</th>
                        <th>Operazioni</th>
                    </tr>
                    </thead>

                    <tbody>

                    <#list listaOperatori as operatore>

                        <#assign operatoreAttivo =
                            operatore.attivo!false>

                        <#assign operatoreCaposquadra =
                            operatore.caposquadra!false>

                        <#assign operatoreAssegnato =
                            operatore.squadra??>

                        <tr>

                            <td>${operatore.email!""}</td>

                            <td>${operatore.nome!""}</td>

                            <td>${operatore.cognome!""}</td>

                            <td>${operatore.CF!""}</td>

                            <td>

                                <#if operatoreAttivo>

                                    <span class="stato-attivo">
                                        Attivo
                                    </span>

                                <#else>

                                    <span class="stato-disattivato">
                                        Disattivato
                                    </span>

                                </#if>

                            </td>

                            <td>

                                <#if operatoreAssegnato>

                                    <span class="squadra-assegnata">
                                        Assegnato
                                    </span>

                                <#else>

                                    <span class="non-assegnato">
                                        Nessuna squadra
                                    </span>

                                </#if>

                            </td>

                            <td>

                                <#if operatoreCaposquadra>

                                    <span class="badge-caposquadra">
                                        Sì
                                    </span>

                                <#else>

                                    No

                                </#if>

                            </td>

                            <td>

                                <div class="azioni">

                                    <form action="${contextPath}/admin/utenti"
                                          method="get">

                                        <input type="hidden"
                                               name="azione"
                                               value="modifica">

                                        <input type="hidden"
                                               name="ruolo"
                                               value="OPERATORE">

                                        <input type="hidden"
                                               name="email"
                                               value="${operatore.email!""}">

                                        <button type="submit"
                                                class="btn-modifica">
                                            Modifica
                                        </button>

                                    </form>

                                    <#if operatoreAttivo>

                                        <form action="${contextPath}/admin/utenti"
                                              method="post"
                                              onsubmit="return confirm('Disattivare questo operatore?');">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="disattiva">

                                            <input type="hidden"
                                                   name="ruolo"
                                                   value="OPERATORE">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${operatore.email!""}">

                                            <button type="submit"
                                                    class="btn-disattiva">
                                                Disattiva
                                            </button>

                                        </form>

                                    <#else>

                                        <form action="${contextPath}/admin/utenti"
                                              method="post"
                                              onsubmit="return confirm('Riattivare questo operatore?');">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="riattiva">

                                            <input type="hidden"
                                                   name="ruolo"
                                                   value="OPERATORE">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${operatore.email!""}">

                                            <button type="submit"
                                                    class="btn-riattiva">
                                                Riattiva
                                            </button>

                                        </form>

                                    </#if>

                                    <!--
                                        CAPOSQUADRA:
                                        non richiede una squadra già assegnata.
                                    -->
                                    <#if operatoreCaposquadra>

                                        <form action="${contextPath}/admin/utenti"
                                              method="post"
                                              onsubmit="return confirm('Rimuovere il ruolo di caposquadra?');">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="rimuovi_caposquadra">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${operatore.email!""}">

                                            <button type="submit"
                                                    class="btn-rimuovi-caposquadra">
                                                Rimuovi caposquadra
                                            </button>

                                        </form>

                                    <#elseif operatoreAttivo>

                                        <form action="${contextPath}/admin/utenti"
                                              method="post"
                                              onsubmit="return confirm('Rendere questo operatore caposquadra?');">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="rendi_caposquadra">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${operatore.email!""}">

                                            <button type="submit"
                                                    class="btn-caposquadra">
                                                Rendi caposquadra
                                            </button>

                                        </form>

                                    <#else>

                                        <button type="button"
                                                class="btn-disabilitato"
                                                disabled
                                                title="Riattiva prima l'operatore">
                                            Rendi caposquadra
                                        </button>

                                    </#if>

                                    <!--
                                        La rimozione dalla squadra compare
                                        solo se una squadra è presente.
                                    -->
                                    <#if operatoreAssegnato>

                                        <form action="${contextPath}/admin/utenti"
                                              method="post"
                                              onsubmit="return confirm('Rimuovere questo operatore dalla squadra?');">

                                            <input type="hidden"
                                                   name="azione"
                                                   value="rimuovi_da_squadra">

                                            <input type="hidden"
                                                   name="email"
                                                   value="${operatore.email!""}">

                                            <button type="submit"
                                                    class="btn-rimuovi-squadra">
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

        </#if>

    </section>

</main>

</body>

</html>