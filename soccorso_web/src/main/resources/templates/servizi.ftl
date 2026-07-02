<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Servizi - Soccorso Web</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background-color: #f2f2f2;
            color: #000;
        }

        header {
            background-color: #dc2626;
            color: white;
            text-align: center;
            padding: 40px 20px;
        }

        header h1 {
            margin: 0;
            font-size: 42px;
            font-weight: bold;
        }

        header p {
            margin-top: 25px;
            font-size: 20px;
        }

        nav {
            background-color: #bf1717;
            text-align: center;
            padding: 14px;
        }

        nav a {
            color: white;
            font-weight: bold;
            font-size: 20px;
            margin: 0 20px;
            text-decoration: underline;
        }

        nav a:hover {
            color: #ffd6d6;
        }

        .contenitore {
            width: 85%;
            margin: 40px auto;
            background-color: white;
            padding: 40px;
            border-radius: 12px;
            box-sizing: border-box;
        }

        .contenitore h2 {
            margin-top: 0;
            font-size: 30px;
            color: #bf1717;
        }

        .intro {
            font-size: 20px;
            line-height: 1.4;
            margin-bottom: 35px;
        }

        .servizi-container {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 25px;
        }

        .servizio-card {
            border: 1px solid #ddd;
            border-left: 6px solid #bf1717;
            padding: 22px;
            border-radius: 8px;
            background-color: #fafafa;
        }

        .servizio-card h3 {
            margin-top: 0;
            color: #bf1717;
            font-size: 23px;
        }

        .servizio-card p {
            font-size: 18px;
            line-height: 1.4;
        }

        .btn-area {
            margin-top: 35px;
        }

        .btn {
            display: inline-block;
            background-color: #bf1717;
            color: white;
            padding: 12px 22px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
            font-size: 17px;
        }

        .btn:hover {
            background-color: #991313;
        }

        footer {
            text-align: center;
            background-color: #222;
            color: white;
            padding: 20px;
            margin-top: 40px;
        }

        @media screen and (max-width: 800px) {
            .servizi-container {
                grid-template-columns: 1fr;
            }

            header h1 {
                font-size: 32px;
            }

            nav a {
                display: inline-block;
                margin: 8px 12px;
            }

            .contenitore {
                width: 92%;
                padding: 25px;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>SERVIZI - Soccorso Web</h1>
    <p>Scopri i servizi disponibili nel portale di soccorso</p>
</header>

<nav>
    <a href="home">Home</a>
    <a href="servizi">Servizi</a>
</nav>

<div class="contenitore">

    <h2>Servizi disponibili</h2>

    <p class="intro">
        Soccorso Web permette agli utenti di inviare richieste di soccorso
        e agli amministratori di gestirle tramite una dashboard dedicata.
    </p>

    <div class="servizi-container">

        <div class="servizio-card">
            <h3>Invio richiesta di soccorso</h3>
            <p>
                L'utente può compilare un modulo indicando la descrizione
                del problema, la posizione, la propria email e una foto opzionale.
            </p>
        </div>

        <div class="servizio-card">
            <h3>Gestione richieste</h3>
            <p>
                L'amministratore può visualizzare tutte le richieste ricevute
                attraverso la dashboard amministrativa.
            </p>
        </div>

        <div class="servizio-card">
            <h3>Aggiornamento stato</h3>
            <p>
                Ogni richiesta può essere aggiornata con uno stato, ad esempio:
                da confermare, in gestione, risolta o rifiutata.
            </p>
        </div>

        <div class="servizio-card">
            <h3>Contatto con il segnalante</h3>
            <p>
                L'email inserita nel modulo consente all'amministratore
                di ricontattare il segnalante in caso di necessità.
            </p>
        </div>

    </div>

    <div class="btn-area">
        <a class="btn" href="home">Torna al modulo di richiesta</a>
    </div>

</div>

<footer>
    <p>Soccorso Web - Portale per la gestione delle richieste di soccorso</p>
</footer>

</body>
</html>
