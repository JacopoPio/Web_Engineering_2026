<!DOCTYPE html>
<html>
<head>
    <meta charset = "UTF-8">
    <title>Admin Reserved Area</title>
    <style>
        body{
            font-family: Ariel;
            margin: 0;
            padding: 0;
            background: #f4f4f4
        }
        header{
            background: #2222
            color: white;
            padding: 20px;
            text-align: center;
        }
        nav{
            background: #444;
            padding: 10px;
            text-align: center;
        }
        nav a{
            color: white;
            margin: 15px;
            font-weight: bold;
        }
        main{
            max-width: 100px;
            margin: 30px;
            background: white;
            padding: 30px;
            border-radius: 10px;
        }
        .card{
            background: #eeee
            padding: 15px;
            margin-top: 20px;
            border-radius: 8px;
        }
    </style>
</head>
<body>
    <header>
        <h1>Pannello Amministratore</h1>
        <p>Benvenuto ${nome}</p>
        <p>Ruolo: ${ruolo}</p>
     </header>
    
    <nav>
        <a href= "admin">Dashboard</a>
        <a href= "gestione-utenti">Gestione Utenti</a>
        <a href = "richieste">Richieste</a>
        <a hre = "logout">Logout</a>
    </nav> 

    <main>
         <h2>Area Risservata Amministratore</h2>
         <p>Da qui puoi controllare le funzioni amministraative del sistema.</p>
       
        <div class = "card">
            <h3>Gestione Richieste</h3>
            <p>Contolla le richieste inviate e il loro stato</p>
        </div>
        <div class = "card>
            <h3>Gestione Utenti</h3>
            <p>Visuaizza, modifica o elimina gli utenti</p>
        </div>
        <div class = "card">
            <h3>Report sistema</h3>
            <p>Consulta statistiche e informazioni generali sulla piattaforma</p>
        </div>
    </main>
</body>
</html>
