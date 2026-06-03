<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Reserved Area</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: #f4f4f4;
        }

        header {
            background: #222;
            color: white;
            padding: 20px;
            text-align: center;
        }

        nav {
            background: #444;
            padding: 10px;
            text-align: center;
        }

        nav a {
            color: white;
            margin: 15px;
            font-weight: bold;
            text-decoration: none;
        }

        main {
            max-width: 900px;
            margin: 30px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
        }

        .card {
            background: #eeeeee;
            padding: 15px;
            margin-top: 20px;
            border-radius: 8px;
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
        <a href="admin">Dashboard</a>
        <a href="gestione-utenti">Gestione Utenti</a>
        <a href="richieste">Richieste</a>
        <a href="mezzi">Mezzi</a>
        <a href="materiali">Materiali</a>
        <a href="operatori">Operatori</a>
        <a href="logout">Logout</a>
    </nav>

    <main>
        <h2>Area Riservata Amministratore</h2>
        <p>Da qui puoi controllare le funzioni amministrative del sistema.</p>

        <div class="card">
            <h3>Gestione Richieste</h3>
            <p>Controlla le richieste inviate e il loro stato.</p>
        </div>

        <div class="card">
            <h3>Gestione Utenti</h3>
            <p>Visualizza, modifica o elimina gli utenti.</p>
        </div>
         <div class="card">
        <h3>Missioni in corso</h3>
        <p>Monitora missioni aperte, aggiornamenti e risorse impegnate.</p>
         <div class="card">
        <h3>Anagrafiche e risorse</h3>
        <p>Gestisci operatori, mezzi e materiali censiti nel sistema.</p>
        <div class="card">
            <h3>Report sistema</h3>
            <p>Consulta statistiche e informazioni generali sulla piattaforma.</p>
        </div>
    </main>
</body>
</html>