<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Area amministratore - SoccorsoWeb</title>

    <style>
        * {
            box-sizing: border-box;
        }

        html,
        body {
            margin: 0;
            min-height: 100%;
        }

        body {
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f8;
            color: #222;
        }

        /*
         * Barra di navigazione separata dall'header.
         */
        nav {
            width: 100%;
            padding: 14px 20px;
            background: #8e0000;
            text-align: center;
        }

        nav a {
            display: inline-block;
            margin: 5px 14px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        nav a:hover {
            text-decoration: underline;
        }

        header {
            padding: 42px 20px;
            background: linear-gradient(
                135deg,
                #8e0000,
                #b71c1c
            );
            color: white;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 38px;
        }

        header p {
            margin: 12px 0 0;
            font-size: 18px;
        }

        main {
            width: min(1350px, 94%);
            margin: 32px auto;
        }

        .messaggio {
            margin-bottom: 22px;
            padding: 15px;
            border-radius: 8px;
            font-weight: bold;
        }

        .successo {
            background: #e8f5e9;
            color: #1b5e20;
            border-left: 6px solid #2e7d32;
        }

        .errore {
            background: #ffebee;
            color: #b71c1c;
            border-left: 6px solid #b71c1c;
        }

        /*
         * La griglia si adatta automaticamente allo spazio.
         * Non ci sono larghezze o margini fissi sulle schede.
         */
        .dashboard-grid {
            display: grid;
            grid-template-columns:
                repeat(auto-fit, minmax(260px, 1fr));
            gap: 24px;
            align-items: stretch;
        }

        .card {
            display: flex;
            flex-direction: column;
            min-height: 210px;
            padding: 26px;
            border-left: 6px solid #d32f2f;
            border-radius: 12px;
            background: white;
            box-shadow: 0 5px 18px rgba(0, 0, 0, 0.10);
            text-decoration: none;
            color: #222;
            transition:
                transform 0.18s ease,
                box-shadow 0.18s ease;
        }

        .card:hover {
            transform: translateY(-4px);
            box-shadow: 0 9px 25px rgba(0, 0, 0, 0.16);
        }

        .card h2 {
            margin: 0 0 16px;
            color: #b71c1c;
            font-size: 23px;
            text-align: center;
        }

        .card p {
            margin: 0;
            line-height: 1.55;
            text-align: center;
            font-size: 17px;
        }

        .card-footer {
            margin-top: auto;
            padding-top: 22px;
            text-align: center;
        }

        .card-button {
            display: inline-block;
            padding: 10px 16px;
            border-radius: 6px;
            background: #b71c1c;
            color: white;
            font-weight: bold;
        }

        /*
         * Sezione storico separata e ordinata.
         */
        .storico-section {
            margin-top: 34px;
            padding: 30px;
            border-radius: 14px;
            background: white;
            box-shadow: 0 5px 18px rgba(0, 0, 0, 0.10);
        }

        .storico-section > h2 {
            margin: 0;
            color: #b71c1c;
            text-align: center;
            font-size: 30px;
        }

        .storico-section > p {
            margin: 12px 0 25px;
            text-align: center;
            font-size: 17px;
        }

        .storico-grid {
            display: grid;
            grid-template-columns:
                repeat(auto-fit, minmax(260px, 1fr));
            gap: 22px;
        }

        .storico-card {
            display: block;
            min-height: 170px;
            padding: 24px;
            border-left: 6px solid #d32f2f;
            border-radius: 10px;
            background: #f8f9fa;
            color: #222;
            text-decoration: none;
            transition:
                transform 0.18s ease,
                background 0.18s ease;
        }

        .storico-card:hover {
            transform: translateY(-3px);
            background: #fff3f3;
        }

        .storico-card h3 {
            margin: 0 0 14px;
            color: #b71c1c;
            text-align: center;
            font-size: 21px;
        }

        .storico-card p {
            margin: 0;
            line-height: 1.5;
            text-align: center;
            font-size: 16px;
        }

        footer {
            margin-top: 45px;
            padding: 20px;
            background: #263238;
            color: white;
            text-align: center;
        }

        @media (max-width: 700px) {
            nav a {
                display: block;
                margin: 9px 0;
            }

            header {
                padding: 32px 15px;
            }

            header h1 {
                font-size: 29px;
            }

            main {
                width: 94%;
            }

            .dashboard-grid,
            .storico-grid {
                grid-template-columns: 1fr;
            }

            .storico-section {
                padding: 22px;
            }
        }
    </style>
</head>

<body>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

    <a href="${contextPath}/admin/richieste">
        Richieste
    </a>

    <a href="${contextPath}/admin/utenti">
        Utenti
    </a>

    <a href="${contextPath}/admin/missioni">
        Missioni
    </a>

    <a href="${contextPath}/mezzi">
        Mezzi
    </a>

    <a href="${contextPath}/materiali">
        Materiali
    </a>

    <a href="${contextPath}/cambia-password">
        Cambia password
    </a>

    <a href="${contextPath}/logout">
        Logout
    </a>
</nav>

<header>
    <h1>Area riservata amministratore</h1>

    <p>
        Benvenuto ${(nome!"Amministratore")?html}.
        Da qui puoi controllare le funzioni amministrative
        del sistema.
    </p>
</header>

<main>

    <#if successo??>
        <div class="messaggio successo">
            ${successo?html}
        </div>
    </#if>

    <#if errore??>
        <div class="messaggio errore">
            ${errore?html}
        </div>
    </#if>

    <section class="dashboard-grid">

        <a class="card"
           href="${contextPath}/admin/richieste">

            <h2>Gestione richieste</h2>

            <p>
                Controlla le richieste inviate dagli utenti,
                crea le missioni e consulta il loro stato.
            </p>

            <div class="card-footer">
                <span class="card-button">
                    Apri richieste
                </span>
            </div>
        </a>

        <a class="card"
           href="${contextPath}/admin/utenti">

            <h2>Gestione utenti</h2>

            <p>
                Visualizza, crea e modifica amministratori
                e operatori registrati.
            </p>

            <div class="card-footer">
                <span class="card-button">
                    Apri utenti
                </span>
            </div>
        </a>

        <a class="card"
           href="${contextPath}/admin/missioni">

            <h2>Missioni in corso</h2>

            <p>
                Monitora le missioni aperte, inserisci
                aggiornamenti e gestisci le risorse impegnate.
            </p>

            <div class="card-footer">
                <span class="card-button">
                    Apri missioni
                </span>
            </div>
        </a>

        <a class="card"
           href="${contextPath}/mezzi">

            <h2>Mezzi</h2>

            <p>
                Gestisci ambulanze, veicoli e altri mezzi
                utilizzabili nelle operazioni di soccorso.
            </p>

            <div class="card-footer">
                <span class="card-button">
                    Gestisci mezzi
                </span>
            </div>
        </a>

        <a class="card"
           href="${contextPath}/materiali">

            <h2>Materiali</h2>

            <p>
                Gestisci materiali, attrezzature e risorse
                utilizzabili durante le missioni.
            </p>

            <div class="card-footer">
                <span class="card-button">
                    Gestisci materiali
                </span>
            </div>
        </a>

        <a class="card"
           href="${contextPath}/cambia-password">

            <h2>Cambia password</h2>

            <p>
                Modifica la password del tuo account
                amministratore in modo sicuro.
            </p>

            <div class="card-footer">
                <span class="card-button">
                    Modifica password
                </span>
            </div>
        </a>

    </section>

    <section class="storico-section">

        <h2>Storico missioni</h2>

        <p>
            Seleziona la categoria e scegli l'elemento
            di cui vuoi consultare le missioni.
        </p>

        <div class="storico-grid">

            <a class="storico-card"
               href="${contextPath}/storico/operatori">

                <h3>Storico operatori</h3>

                <p>
                    Visualizza gli operatori e consulta
                    le missioni alle quali hanno partecipato.
                </p>
            </a>

            <a class="storico-card"
               href="${contextPath}/storico/mezzi">

                <h3>Storico mezzi</h3>

                <p>
                    Consulta le missioni nelle quali
                    ciascun mezzo è stato utilizzato.
                </p>
            </a>

            <a class="storico-card"
               href="${contextPath}/storico/materiali">

                <h3>Storico materiali</h3>

                <p>
                    Consulta le missioni nelle quali
                    ciascun materiale è stato impiegato.
                </p>
            </a>

        </div>

    </section>

</main>

<footer>
    SoccorsoWeb — Area amministratore
</footer>

</body>
</html>