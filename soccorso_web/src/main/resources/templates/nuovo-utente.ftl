<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Nuovo Utente - SoccorsoWeb</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f8;
            color: #222;
        }

        header {
            background: linear-gradient(135deg, #b71c1c, #d32f2f);
            color: white;
            padding: 35px 20px;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 38px;
        }

        nav {
            background: #8e0000;
            padding: 14px;
            text-align: center;
        }

        nav a {
            color: white;
            margin: 0 10px;
            font-weight: bold;
            text-decoration: none;
            padding: 8px 12px;
            border-radius: 6px;
        }

        nav a:hover {
            background: rgba(255,255,255,0.18);
        }

        main {
            max-width: 1000px;
            margin: 35px auto;
            padding: 0 20px;
        }

        .panel {
            background: white;
            padding: 30px;
            border-radius: 14px;
            box-shadow: 0 8px 25px rgba(0,0,0,.10);
        }

        .panel h2 {
            margin-top: 0;
            color: #b71c1c;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 18px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group.full {
            grid-column: 1 / -1;
        }

        label {
            font-weight: bold;
            margin-bottom: 6px;
            color: #444;
        }

        input, select, textarea {
            padding: 10px;
            border: 1px solid #bbb;
            border-radius: 6px;
            font-size: 14px;
        }

        input:focus, select:focus, textarea:focus {
            outline: none;
            border-color: #b71c1c;
            box-shadow: 0 0 0 2px rgba(183,28,28,.15);
        }

        .checkbox-group {
            display: flex;
            gap: 14px;
            flex-wrap: wrap;
            margin-top: 5px;
        }

        .checkbox-group label {
            font-weight: normal;
        }

        .btn {
            display: inline-block;
            padding: 11px 18px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }

        .btn-primary {
            background: #b71c1c;
            color: white;
        }

        .btn-primary:hover {
            background: #8e0000;
        }

        .msg-ok {
            background: #e8f5e9;
            color: #1b5e20;
            border-left: 6px solid #2e7d32;
            padding: 14px;
            margin-bottom: 20px;
            border-radius: 6px;
        }

        .msg-errore {
            background: #ffebee;
            color: #b71c1c;
            border-left: 6px solid #b71c1c;
            padding: 14px;
            margin-bottom: 20px;
            border-radius: 6px;
        }

        footer {
            text-align: center;
            padding: 25px;
            color: #666;
            font-size: 14px;
        }

        @media (max-width: 700px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            nav a {
                display: block;
                margin: 6px 0;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Crea nuovo utente</h1>
    <p>Area Amministratore - SoccorsoWeb</p>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/admin/nuovo-utente">Nuovo Utente</a>
    <a href="${contextPath}/mezzi">Mezzi</a>
    <a href="${contextPath}/materiali">Materiali</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>

    <#if successo??>
        <div class="msg-ok">
            Utente creato correttamente. Le credenziali sono state generate e stampate/inviate.
        </div>
    </#if>

    <#if errore??>
        <div class="msg-errore">
            <#if errore == "campi">
                Compila almeno ruolo, email, nome e cognome.
            <#elseif errore == "email">
                Esiste già un utente con questa email.
            <#elseif errore == "ruolo">
                Ruolo non valido.
            <#elseif errore == "data">
                Data di nascita non valida.
            <#else>
                Errore durante la creazione dell'utente.
            </#if>
        </div>
    </#if>

    <section class="panel">
        <h2>Dati nuovo utente</h2>

        <form action="${contextPath}/admin/nuovo-utente" method="post">

            <div class="form-grid">

                <div class="form-group">
                    <label for="ruolo">Ruolo</label>
                    <select id="ruolo" name="ruolo" required>
                        <option value="">Seleziona ruolo</option>
                        <option value="ADMIN">Amministratore</option>
                        <option value="OPERATORE">Operatore</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="email">Email / Username</label>
                    <input type="email" id="email" name="email" required>
                </div>

                <div class="form-group">
                    <label for="nome">Nome</label>
                    <input type="text" id="nome" name="nome" required>
                </div>

                <div class="form-group">
                    <label for="cognome">Cognome</label>
                    <input type="text" id="cognome" name="cognome" required>
                </div>

                <div class="form-group">
                    <label for="data_nascita">Data di nascita</label>
                    <input type="date" id="data_nascita" name="data_nascita">
                </div>

                <div class="form-group">
                    <label for="citta_nascita">Città di nascita</label>
                    <input type="text" id="citta_nascita" name="citta_nascita">
                </div>

                <div class="form-group">
                    <label for="cf">Codice fiscale</label>
                    <input type="text" id="cf" name="cf" maxlength="16">
                </div>

                <div class="form-group">
                    <label for="indirizzo">Indirizzo</label>
                    <input type="text" id="indirizzo" name="indirizzo">
                </div>

                <div class="form-group full">
                    <label>Patenti</label>

                    <div class="checkbox-group">
                        <#-- Controllo se la lista esiste e non è vuota -->
                        <#if listaPatenti?? && (listaPatenti?size > 0)>
                            <#list listaPatenti as patente>
                                <label>
                                    <input type="checkbox" name="patenti" value="${patente.tipoPatente}"> 
                                    ${patente.tipoPatente}
                                </label>
                            </#list>
                        <#else>
                            <span style="color: #666; font-style: italic;">Nessuna patente presente nel database.</span>
                        </#if>
                    </div>
                </div>

                <div class="form-group full">
                    <label>Abilità (Caricate dal DB)</label>

                    <div class="checkbox-group">
                        <#if listaAbilita?? && (listaAbilita?size > 0)>
                            <#list listaAbilita as abilita>
                                <label>
                                    <input type="checkbox" name="abilita" value="${abilita.nome}"> 
                                    ${abilita.nome}
                                </label>
                            </#list>
                        <#else>
                            <span style="color: #666; font-style: italic;">Nessuna abilità presente nel database.</span>
                        </#if>
                    </div>
                </div>

                <div class="form-group full">
                    <button type="submit" class="btn btn-primary">
                        Crea utente
                    </button>
                </div>

            </div>

        </form>
    </section>

</main>

<footer>
    <p>&copy; 2026 SoccorsoWeb - Progetto Web Engineering</p>
</footer>

</body>
</html>