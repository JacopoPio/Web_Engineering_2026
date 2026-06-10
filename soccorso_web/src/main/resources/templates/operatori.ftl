<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Area Operatore - SoccorsoWeb</title>

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
            font-weight: bold;
            margin: 0 10px;
            text-decoration: none;
            padding: 8px 12px;
            border-radius: 6px;
        }

        nav a:hover {
            background: rgba(255, 255, 255, 0.18);
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
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.10);
        }

        .panel h2 {
            margin-top: 0;
            color: #1565c0;
        }

        .panel p {
            line-height: 1.5;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
            gap: 20px;
            margin-top: 25px;
        }

        .card {
            display: block;
            background: #f7f7f7;
            padding: 22px;
            border-radius: 12px;
            border-left: 6px solid #1976d2;
            text-decoration: none;
            color: #222;
            transition: 0.2s;
        }

        .card:hover {
            transform: translateY(-4px);
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
            background: white;
        }

        .card h3 {
            margin-top: 0;
            color: #1565c0;
        }

        .card p {
            margin-bottom: 0;
            line-height: 1.5;
        }

        footer {
            text-align: center;
            padding: 25px;
            color: #666;
            font-size: 14px;
        }

        @media screen and (max-width: 700px) {
            nav a {
                display: block;
                margin: 6px 0;
            }

            header h1 {
                font-size: 30px;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Area Operatore</h1>
    <p>Benvenuto ${nome!"Operatore"} - Ruolo: ${ruolo!"OPERATORE"}</p>
</header>

<nav>
    <a href="${contextPath}/operatore">Dashboard</a>
    <a href="${contextPath}/operatore/richieste">Richieste assegnate</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>
    <section class="panel">

        <h2>Pannello Operatore</h2>

        <p>
            Da questa area l'operatore può visualizzare le richieste attive,
            controllare gli interventi in corso e aggiornare lo stato delle attività operative.
        </p>

        <div class="grid">

            <a class="card" href="${contextPath}/operatore/richieste">
                <h3>Richieste assegnate</h3>
                <p>
                    Visualizza le richieste attive o in corso che devono essere gestite.
                </p>
            </a>

            <a class="card" href="${contextPath}/operatore/missioni">
                <h3>Missioni operative</h3>
                <p>
                    Controlla le missioni operative e le attività collegate agli interventi.
                </p>
            </a>

            <a class="card" href="${contextPath}/operatore/profilo">
                <h3>Profilo operatore</h3>
                <p>
                    Visualizza le informazioni dell'operatore attualmente loggato.
                </p>
            </a>

        </div>

    </section>
</main>

<footer>
    <p>&copy; 2026 SoccorsoWeb - Progetto Web Engineering</p>
</footer>

</body>
</html>