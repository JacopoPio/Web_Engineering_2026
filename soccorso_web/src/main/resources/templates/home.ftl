<!DOCTYPE html>
<html>
<head>
    <meta charset = "UTF-8"<
    <title>HOME - Soccorso Web</title>
    <style>
        body{
            font-family: Ariel;
            margin: 0;
            padding: 0;
            background: #f4f4f4;
        }
        header{
            background: #d32f2f2;
            color: white;
            padding: 20px;
            text-align: center;
        }
        nav{
            background: #b71c1c;
            padding: 10px;
            text-align: center;
        }
        nav a{
            color: white;
            margin: 15px;
            font-weight: bold;
        }
        main{
            padding: 30px;
            background: white;
            max-width: 900px;
            margin: 30px;
            border-radius: 10px;
        }
    </style>
</head>
<body>
    <header>
        <h1>${titolo}!</h1>
        <p>${messaggio}</p>
    </header>
    
    <nav>
        <a href="home">Home</a>
        <a href="servizi">Servizi</a>
        <a href="contatti">Contatti</a>
    </nav>
    <main>
        <h2>Benvenuto ${nome}</h2>
        <p>
            Questo è un portale per la richiesta e la gestione di domande di soccorso.
            Qui puoi trovare informazioni, servizi disponibili e contatti utili in caso di problemi.
        </p>
    </main>
</body>
</html>
