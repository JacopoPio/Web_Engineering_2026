<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Login - SoccorsoWeb</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background-color: #f4f6f8;
            color: #222;
        }

        header {
            background: linear-gradient(135deg, #b71c1c, #d32f2f);
            color: white;
            text-align: center;
            padding: 40px 20px;
        }

        header h1 {
            margin: 0;
            font-size: 40px;
        }

        nav {
            background-color: #8e0000;
            text-align: center;
            padding: 14px;
        }

        nav a {
            color: white;
            font-weight: bold;
            margin: 0 15px;
            text-decoration: none;
            padding: 8px 12px;
            border-radius: 6px;
        }

        nav a:hover {
            background: rgba(255, 255, 255, 0.18);
        }

        .login-container {
            max-width: 480px;
            margin: 45px auto;
            background: white;
            padding: 35px;
            border-radius: 14px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.10);
        }

        .login-container h2 {
            margin-top: 0;
            color: #b71c1c;
            text-align: center;
        }

        label {
            display: block;
            margin-top: 18px;
            font-weight: bold;
        }

        input,
        select {
            width: 100%;
            padding: 12px;
            margin-top: 8px;
            border: 1px solid #aaa;
            border-radius: 6px;
            font-size: 16px;
        }

        button {
            width: 100%;
            margin-top: 25px;
            background-color: #b71c1c;
            color: white;
            border: none;
            padding: 13px;
            border-radius: 6px;
            font-size: 17px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background-color: #8e0000;
        }

        .errore {
            background: #ffebee;
            color: #b71c1c;
            border-left: 6px solid #b71c1c;
            padding: 12px;
            margin-bottom: 18px;
            border-radius: 6px;
        }

        footer {
            text-align: center;
            color: #666;
            padding: 25px;
            font-size: 14px;
        }
    </style>
</head>

<body>

<header>
    <h1>Area Riservata</h1>
    <p>Accesso per amministratori e operatori</p>
</header>

<nav>
    <a href="${contextPath}/home">Home</a>
    <a href="${contextPath}/servizi">Servizi</a>
    <a href="${contextPath}/contatti">Contatti</a>
</nav>

<div class="login-container">

    <h2>Login</h2>

    <#if errore??>
        <div class="errore">
            Username, password o ruolo non validi.
        </div>
    </#if>

    <form action="${contextPath}/login" method="post">

        <label for="ruolo">Tipo utente</label>
        <select id="ruolo" name="ruolo" required>
            <option value="ADMIN">Amministratore</option>
            <option value="OPERATORE">Operatore</option>
        </select>

        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Accedi</button>

    </form>

</div>

<footer>
    <p>&copy; 2026 SoccorsoWeb - Progetto Web Engineering</p>
</footer>

</body>
</html>