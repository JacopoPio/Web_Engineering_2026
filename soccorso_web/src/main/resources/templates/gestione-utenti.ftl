<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">

    <meta
        name="viewport"
        content="width=device-width, initial-scale=1.0"
    >

    <title>Gestione utenti</title>

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
            padding: 30px 20px;
            background: linear-gradient(135deg, #1565c0, #1976d2);
            color: white;
            text-align: center;
        }

        header h1 {
            margin: 0;
        }

        nav {
            padding: 14px;
            background: #0d47a1;
            text-align: center;
        }

        nav a {
            display: inline-block;
            margin: 5px 12px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: min(1200px, 95%);
            margin: 30px auto;
        }

        .barra-superiore {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            margin-bottom: 20px;
        }

        section {
            margin-bottom: 30px;
            padding: 22px;
            overflow-x: auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, .08);
        }

        section h2 {
            margin-top: 0;
            color: #0d47a1;
        }

        table {
            width: 100%;
            min-width: 750px;
            border-collapse: collapse;
        }

        th,
        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #eef3f8;
        }

        tbody tr:hover {
            background: #f8fafc;
        }

        .attivo {
            color: #166534;
            font-weight: bold;
        }

        .disattivato {
            color: #991b1b;
            font-weight: bold;
        }

        .caposquadra {
            color: #0d47a1;
            font-weight: bold;
        }

        .ruolo-semplice {
            color: #666;
        }

        .azioni {
            display: flex;
            gap: 7px;
            flex-wrap: wrap;
            align-items: center;
        }

        .azioni form {
            margin: 0;
        }

        button,
        .pulsante {
            display: inline-block;
            padding: 9px 14px;
            border: none;
            border-radius: 5px;
            background: #1565c0;
            color: white;
            font: inherit;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
        }

        button:hover,
        .pulsante:hover {
            background: #0d47a1;
        }

        .pericolo {
            background: #c62828;
        }

        .pericolo:hover {
            background: #8e0000;
        }

        .riattiva {
            background: #2e7d32;
        }

        .riattiva:hover {
            background: #1b5e20;
        }

        .secondario {
            background: #607d8b;
        }

        .secondario:hover {
            background: #455a64;
        }

        .promuovi {
            background: #6a1b9a;
        }

        .promuovi:hover {
            background: #4a148c;
        }

        .messaggio {
            margin-bottom: 18px;
            padding: 13px 15px;
            border-radius: 5px;
            font-weight: bold;
        }

        .successo {
            border: 1px solid #86c99a;
            background: #dff3e5;
            color: #166534;
        }

        .errore {
            border: 1px solid #ef9a9a;
            background: #fde2e2;
            color: #991b1b;
        }

        .vuoto {
            padding: 25px;
            color: #666;
            text-align: center;
            font-style: italic;
        }

        footer {
            margin-top: 35px;
            padding: 20px;
            background: #263238;
            color: white;
            text-align: center;
        }

        @media (max-width: 700px) {
            .barra-superiore {
                flex-direction: column;
                align-items: stretch;
            }

            .barra-superiore .pulsante {
                text-align: center;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Gestione utenti</h1>
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

    <div class="barra-superiore">

        
            class="pulsante"
            href="${contextPath}/admin/nuovo-utente"
        >
            Crea nuovo utente
        </a>

        
            class="pulsante secondario"
            href="${contextPath}/admin"
        >
            Torna alla dashboard
        </a>

    </div>

    <#-- Messaggi di successo -->

    <#if successo??>
        <div class="messaggio successo">

            <#if successo == "modificato">
                Utente modificato correttamente.

            <#elseif successo == "disattivato">
                Utente disattivato correttamente.

            <#elseif successo == "riattivato">
                Utente riattivato correttamente.

            <#elseif successo == "caposquadra_assegnato">
                Operatore promosso a caposquadra correttamente.

            <#elseif successo == "caposquadra_rimosso">
                Ruolo di caposquadra rimosso correttamente.

            <#else>
                Operazione completata correttamente.
            </#if>

        </div>
    </#if>

    <#-- Messaggi di errore -->

    <#if errore??>
        <div class="messaggio errore">

            <#if errore == "autodisattivazione">
                Non puoi disattivare il tuo account.

            <#elseif errore == "ultimo_admin">
                Non puoi disattivare l'ultimo amministratore attivo.

            <#elseif errore == "utente_non_trovato">
                L'utente richiesto non esiste.

            <#elseif errore == "parametri">
                I parametri ricevuti non sono validi.

            <#elseif errore == "azione">
                L'operazione richiesta non è valida.

            <#else>
                Si è verificato un errore durante l'operazione.
            </#if>

        </div>
    </#if>

    <section>

        <h2>Amministratori</h2>

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

                <#if amministratori?? && amministratori?size gt 0>

                    <#list amministratori as utente>

                        <tr>
                            <td>
                                ${(utente.email!"")?html}
                            </td>

                            <td>
                                ${(utente.nome!"")?html}
                            </td>

                            <td>
                                ${(utente.cognome!"")?html}
                            </td>

                            <td>
                                <#if utente.attivo>
                                    <span class="attivo">
                                        Attivo
                                    </span>
                                <#else>
                                    <span class="disattivato">
                                        Disattivato
                                    </span>
                                </#if>
                            </td>

                            <td>
                                <div class="azioni">

                                    <#--
                                        Form GET per la modifica.

                                        Il browser codifica automaticamente
                                        l'email, quindi non serve ?url.
                                    -->
                                    <form
                                        method="get"
                                        action="${contextPath}/admin/utenti"
                                    >
                                        <input
                                            type="hidden"
                                            name="azione"
                                            value="modifica"
                                        >

                                        <input
                                            type="hidden"
                                            name="ruolo"
                                            value="ADMIN"
                                        >

                                        <input
                                            type="hidden"
                                            name="email"
                                            value="${(utente.email!"")?html}"
                                        >

                                        <button type="submit">
                                            Modifica
                                        </button>
                                    </form>

                                    <#if utente.attivo>

                                        <form
                                            method="post"
                                            action="${contextPath}/admin/utenti"
                                            onsubmit="return confirm('Disattivare questo amministratore?');"
                                        >
                                            <input
                                                type="hidden"
                                                name="azione"
                                                value="disattiva"
                                            >

                                            <input
                                                type="hidden"
                                                name="ruolo"
                                                value="ADMIN"
                                            >

                                            <input
                                                type="hidden"
                                                name="email"
                                                value="${(utente.email!"")?html}"
                                            >

                                            <button
                                                class="pericolo"
                                                type="submit"
                                            >
                                                Disattiva
                                            </button>
                                        </form>

                                    <#else>

                                        <form
                                            method="post"
                                            action="${contextPath}/admin/utenti"
                                        >
                                            <input
                                                type="hidden"
                                                name="azione"
                                                value="riattiva"
                                            >

                                            <input
                                                type="hidden"
                                                name="ruolo"
                                                value="ADMIN"
                                            >

                                            <input
                                                type="hidden"
                                                name="email"
                                                value="${(utente.email!"")?html}"
                                            >

                                            <button
                                                class="riattiva"
                                                type="submit"
                                            >
                                                Riattiva
                                            </button>
                                        </form>

                                    </#if>

                                </div>
                            </td>
                        </tr>

                    </#list>

                <#else>

                    <tr>
                        <td
                            class="vuoto"
                            colspan="5"
                        >
                            Nessun amministratore presente.
                        </td>
                    </tr>

                </#if>

            </tbody>
        </table>

    </section>

    <section>

        <h2>Operatori</h2>

        <table>
            <thead>
                <tr>
                    <th>Email</th>
                    <th>Nome</th>
                    <th>Cognome</th>
                    <th>Stato</th>
                    <th>Ruolo</th>
                    <th>Operazioni</th>
                </tr>
            </thead>

            <tbody>

                <#if operatori?? && operatori?size gt 0>

                    <#list operatori as utente>

                        <tr>
                            <td>
                                ${(utente.email!"")?html}
                            </td>

                            <td>
                                ${(utente.nome!"")?html}
                            </td>

                            <td>
                                ${(utente.cognome!"")?html}
                            </td>

                            <td>
                                <#if utente.attivo>
                                    <span class="attivo">
                                        Attivo
                                    </span>
                                <#else>
                                    <span class="disattivato">
                                        Disattivato
                                    </span>
                                </#if>
                            </td>

                            <td>
                                <#if utente.caposquadra>
                                    <span class="caposquadra">
                                        Caposquadra
                                    </span>
                                <#else>
                                    <span class="ruolo-semplice">
                                        Operatore semplice
                                    </span>
                                </#if>
                            </td>

                            <td>
                                <div class="azioni">

                                    <#--
                                        Anche per l'operatore usiamo
                                        un form GET invece di ?url.
                                    -->
                                    <form
                                        method="get"
                                        action="${contextPath}/admin/utenti"
                                    >
                                        <input
                                            type="hidden"
                                            name="azione"
                                            value="modifica"
                                        >

                                        <input
                                            type="hidden"
                                            name="ruolo"
                                            value="OPERATORE"
                                        >

                                        <input
                                            type="hidden"
                                            name="email"
                                            value="${(utente.email!"")?html}"
                                        >

                                        <button type="submit">
                                            Modifica
                                        </button>
                                    </form>

                                    <#--
                                        Toggle per il ruolo di caposquadra.
                                    -->
                                    <#if utente.caposquadra>

                                        <form
                                            method="post"
                                            action="${contextPath}/admin/utenti"
                                            onsubmit="return confirm('Rimuovere il ruolo di caposquadra a questo operatore?');"
                                        >
                                            <input
                                                type="hidden"
                                                name="azione"
                                                value="rimuovi_caposquadra"
                                            >

                                            <input
                                                type="hidden"
                                                name="ruolo"
                                                value="OPERATORE"
                                            >

                                            <input
                                                type="hidden"
                                                name="email"
                                                value="${(utente.email!"")?html}"
                                            >

                                            <button
                                                class="secondario"
                                                type="submit"
                                            >
                                                Rimuovi caposquadra
                                            </button>
                                        </form>

                                    <#else>

                                        <form
                                            method="post"
                                            action="${contextPath}/admin/utenti"
                                            onsubmit="return confirm('Rendere questo operatore caposquadra?');"
                                        >
                                            <input
                                                type="hidden"
                                                name="azione"
                                                value="rendi_caposquadra"
                                            >

                                            <input
                                                type="hidden"
                                                name="ruolo"
                                                value="OPERATORE"
                                            >

                                            <input
                                                type="hidden"
                                                name="email"
                                                value="${(utente.email!"")?html}"
                                            >

                                            <button
                                                class="promuovi"
                                                type="submit"
                                            >
                                                Rendi caposquadra
                                            </button>
                                        </form>

                                    </#if>

                                    <#if utente.attivo>

                                        <form
                                            method="post"
                                            action="${contextPath}/admin/utenti"
                                            onsubmit="return confirm('Disattivare questo operatore?');"
                                        >
                                            <input
                                                type="hidden"
                                                name="azione"
                                                value="disattiva"
                                            >

                                            <input
                                                type="hidden"
                                                name="ruolo"
                                                value="OPERATORE"
                                            >

                                            <input
                                                type="hidden"
                                                name="email"
                                                value="${(utente.email!"")?html}"
                                            >

                                            <button
                                                class="pericolo"
                                                type="submit"
                                            >
                                                Disattiva
                                            </button>
                                        </form>

                                    <#else>

                                        <form
                                            method="post"
                                            action="${contextPath}/admin/utenti"
                                        >
                                            <input
                                                type="hidden"
                                                name="azione"
                                                value="riattiva"
                                            >

                                            <input
                                                type="hidden"
                                                name="ruolo"
                                                value="OPERATORE"
                                            >

                                            <input
                                                type="hidden"
                                                name="email"
                                                value="${(utente.email!"")?html}"
                                            >

                                            <button
                                                class="riattiva"
                                                type="submit"
                                            >
                                                Riattiva
                                            </button>
                                        </form>

                                    </#if>

                                </div>
                            </td>
                        </tr>

                    </#list>

                <#else>

                    <tr>
                        <td
                            class="vuoto"
                            colspan="6"
                        >
                            Nessun operatore presente.
                        </td>
                    </tr>

                </#if>

            </tbody>
        </table>

    </section>

</main>

<footer>
    SoccorsoWeb – Area amministratore
</footer>

</body>
</html>