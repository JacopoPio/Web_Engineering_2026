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
            background: linear-gradient(
                135deg,
                #1565c0,
                #1976d2
            );

            color: white;
            text-align: center;
            padding: 30px 20px;
        }

        header h1 {
            margin: 0;
            font-size: 36px;
        }

        nav {
            background: #0d47a1;
            padding: 14px;
            text-align: center;
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
            max-width: 1400px;
            margin: 30px auto;
        }

        .messaggio {
            padding: 14px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-weight: bold;
        }

        .successo {
            background: #dff2e1;
            color: #1b5e20;
            border: 1px solid #81c784;
        }

        .errore {
            background: #fde0e0;
            color: #b71c1c;
            border: 1px solid #ef9a9a;
        }

        .sezione {
            background: white;
            border-radius: 8px;
            padding: 22px;
            margin-bottom: 30px;
            box-shadow: 0 2px 8px
                rgba(0, 0, 0, 0.10);
        }

        .sezione h2 {
            margin-top: 0;
            color: #0d47a1;
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
            border-bottom: 1px solid #ddd;
            padding: 12px;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #e3f2fd;
            color: #0d47a1;
        }

        tr:hover {
            background: #f8f9fa;
        }

        .azioni {
            display: flex;
            flex-wrap: wrap;
            gap: 7px;
        }

        .azioni form {
            margin: 0;
        }

        button {
            border: none;
            border-radius: 5px;
            padding: 8px 12px;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        .btn-modifica {
            background: #1565c0;
        }

        .btn-modifica:hover {
            background: #0d47a1;
        }

        .btn-rimuovi-squadra {
            background: #ef6c00;
        }

        .btn-rimuovi-squadra:hover {
            background: #e65100;
        }

        .assegnato {
            color: #1b5e20;
            font-weight: bold;
        }

        .non-assegnato {
            color: #666;
            font-style: italic;
        }

        .attivo {
            color: #1b5e20;
            font-weight: bold;
        }

        .non-attivo {
            color: #b71c1c;
            font-weight: bold;
        }

        .vuoto {
            color: #666;
            padding: 16px 0;
        }
    </style>
</head>

<body>

<header>
    <h1>Gestione utenti</h1>
    <p>Amministratori e operatori di SoccorsoWeb</p>
</header>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

    <a href="${contextPath}/admin/utenti">
        Gestione utenti
    </a>

    <a href="${contextPath}/logout">
        Logout
    </a>
</nav>

<main>

    <#if successo??>

        <#if successo == "rimossoDaSquadra">
            <div class="messaggio successo">
                Operatore rimosso correttamente
                dalla squadra.
            </div>
        </#if>

    </#if>

    <#if errore??>

        <#if errore == "emailNonValida">
            <div class="messaggio errore">
                L'indirizzo email ricevuto
                non è valido.
            </div>

        <#elseif errore == "operatoreNonAssegnato">
            <div class="messaggio errore">
                L'operatore non esiste oppure
                non appartiene a una squadra.
            </div>

        <#else>
            <div class="messaggio errore">
                Si è verificato un errore.
            </div>
        </#if>

    </#if>

    <section class="sezione">

        <h2>Operatori</h2>

        <#assign listaOperatori = operatori![]>

        <#if listaOperatori?size == 0>

            <p class="vuoto">
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
                        <th>Stato</th>
                        <th>Squadra</th>
                        <th>Caposquadra</th>
                        <th>Operazioni</th>
                    </tr>
                    </thead>

                    <tbody>

                    <#list listaOperatori as operatore>

                        <tr>

                            <td>
                                ${operatore.email!""}
                            </td>

                            <td>
                                ${operatore.nome!""}
                            </td>

                            <td>
                                ${operatore.cognome!""}
                            </td>

                            <td>
                                <#if operatore.attivo!false>
                                    <span class="attivo">
                                        Attivo
                                    </span>
                                <#else>
                                    <span class="non-attivo">
                                        Non attivo
                                    </span>
                                </#if>
                            </td>

                            <td>
                                <#if operatore.squadra??>
                                    <span class="assegnato">
                                        Assegnato
                                    </span>
                                <#else>
                                    <span class="non-assegnato">
                                        Nessuna squadra
                                    </span>
                                </#if>
                            </td>

                            <td>
                                <#if operatore.caposquadra!false>
                                    Sì
                                <#else>
                                    No
                                </#if>
                            </td>

                            <td>

                                <div class="azioni">

                                    <!-- Modifica operatore -->
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

                                    <!-- Rimuovi dalla squadra -->
                                    <#if operatore.squadra??>

                                        <form action="${contextPath}/admin/utenti/rimuovi-squadra"
                                              method="post"
                                              onsubmit="return confirm(
                                                  'Rimuovere questo operatore dalla squadra?'
                                              );">

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

    <section class="sezione">

        <h2>Amministratori</h2>

        <#assign listaAmministratori =
            amministratori![]>

        <#if listaAmministratori?size == 0>

            <p class="vuoto">
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
                        <th>Stato</th>
                        <th>Operazioni</th>
                    </tr>
                    </thead>

                    <tbody>

                    <#list listaAmministratori
                           as amministratore>

                        <tr>

                            <td>
                                ${amministratore.email!""}
                            </td>

                            <td>
                                ${amministratore.nome!""}
                            </td>

                            <td>
                                ${amministratore.cognome!""}
                            </td>

                            <td>
                                <#if amministratore.attivo!false>
                                    <span class="attivo">
                                        Attivo
                                    </span>
                                <#else>
                                    <span class="non-attivo">
                                        Non attivo
                                    </span>
                                </#if>
                            </td>

                            <td>

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