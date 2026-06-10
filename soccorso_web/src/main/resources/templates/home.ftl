<!DOCTYPE html>
<html>
<head>
    <meta charset = "UTF-8">
    <title>HOME - Soccorso Web</title>
    <style>
        body{
            font-family: Arial;
            margin: 0;
            padding: 0;
            background: #f4f4f4;
        }
        header{
            background: #d32f2f;
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
         button {
            margin-top: 18px;
            padding: 12px 16px;
            background: #d32f2f;
            color: white;
            border: 0;
            cursor: pointer;
        }
         label {
            display: block;
            margin-top: 14px;
            font-weight: bold;
        }
        input, textarea {
            width: 100%;
            box-sizing: border-box;
            padding: 10px;
            margin-top: 6px;
        }
    </style>
</head>
<body>
    <header>
        <h1>${titolo! "Soccorso Web"}</h1>
        <p>${messaggio!"Portale per la gestione delle richieste"}</p>
    </header>
    
    <nav>
        <a href="home">Home</a>
        <a href="servizi">Servizi</a>
        <a href="contatti">Contatti</a>
        <a href= "login">Area Riservata</a>
   </nav>
    <main>
        <p>
            Questo è un portale per la richiesta e la gestione di domande di soccorso.
            Qui puoi trovare informazioni, servizi disponibili e contatti utili in caso di problemi.
            Compila il modulo della richiesta con attenzione
        </p>
        <form action="richiesta-invio" method="post" enctype = "multipart/form-data">
            <label for="descrizione">Descrizione</label>
            <textarea id="descrizione" name="descrizione" rows="5" required></textarea>
            <label for="posizione">Posizione</label>
            <input id="posizione" name="posizione" type="text" required>
            <label for="emailSegnalante">Email segnalante</label>
            <input id="emailSegnalante" name="emailSegnalante" type="text" required>
            <label for="foto">Foto Opzionale</label>
            <input id="foto" name="foto" type="file" accept="image/*">
            <label for="nomeSegnalante">Nome segnalante</label>
            <input id="nomeSegnalante" name="nomeSegnalante" type="text" required>
            
            <button type="submit">Invia Richiesta</button>
</form>
</main>
</body>
</html>
