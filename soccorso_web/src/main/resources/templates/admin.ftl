<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Admin Reserved Area - SoccorsoWeb</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, Helvetica, sans-serif;
            margin: 0;
            padding: 0;
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

        header p {
            margin: 8px 0 0;
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
            background: rgba(255, 255, 255, 0.18);
        }

        main {
            max-width: 1100px;
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
            color: #b71c1c;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 20px;
            margin-top: 25px;
        }

        .card {
            display: block;
            background: #f7f7f7;
            padding: 22px;
            border-radius: 12px;
            border-left: 6px solid #d32f2f;
            text-decoration: none;
            color: #222;
            transition: 0.2s;
        }

        .card:hover {
            transform: translateY(-4px);
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
            background: #fff;
        }

        .card h3 {
            margin-top: 0;
            color: #b71c1c;
        }

        .card p {
            line-height: 1.5;
            margin-bottom: 0;
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
    <h1>Pannello Amministratore</h1>
    <p>Benvenuto ${nome!"Amministratore"}</p>
    <p>Ruolo: ${ruolo!"ADMIN"}</p>
</header>

<nav>
    <a href="${contextPath}/admin">Dashboard</a>
    <a href="${contextPath}/admin/richieste">Richieste</a>
    <a href="${contextPath}/admin/nuovo-utente">Gestione Utenti</a>
    <a href="${contextPath}/mezzi">Mezzi</a>
    <a href="${contextPath}/materiale">Materiali</a>
    <a href="${contextPath}/operatori">Gestione Operatori</a>
    <a href="${contextPath}/logout">Logout</a>
</nav>

<main>
    <section class="panel">
        <h2>Area Riservata Amministratore</h2>
        <p>Da qui puoi controllare le funzioni amministrative del sistema.</p>

        <div class="grid">

            <a class="card" href="${contextPath}/admin/richieste">
                <h3>Gestione Richieste</h3>
                <p>Controlla le richieste inviate dagli utenti e il loro stato.</p>
            </a>

            <a class="card" href="${contextPath}/admin/nuovo-utente">
                <h3>Gestione Utenti</h3>
                <p>Visualizza, modifica o elimina gli utenti registrati.</p>
            </a>

            <a class="card" href="${contextPath}/admin/missioni">
                <h3>Missioni in corso</h3>
                <p>Monitora missioni aperte, aggiornamenti e risorse impegnate.</p>
            </a>

            <a class="card" href="${contextPath}/operatori">
                <h3>Gestione Operatori</h3>
                <p>Gestisci gli operatori disponibili nel sistema.</p>
            </a>

            <a class="card" href="${contextPath}/mezzi">
                <h3>Mezzi</h3>
                <p>Gestisci ambulanze, veicoli e mezzi di soccorso.</p>
            </a>

            <a class="card" href="${contextPath}/materiali">
                <h3>Materiali</h3>
                <p>Gestisci materiali e risorse utilizzabili nelle missioni.</p>
            </a>

        </div>
    </section>
</main>

<footer>
    <p>&copy; 2026 SoccorsoWeb - Progetto Web Engineering</p>
</footer>

</body>
</html>