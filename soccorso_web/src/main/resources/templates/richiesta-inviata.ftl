<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle!"Richiesta inviata"} - SoccorsoWeb</title>

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

        .site-header {
            background: linear-gradient(135deg, #b71c1c, #d32f2f);
            color: white;
            padding: 35px 20px;
            text-align: center;
        }

        .site-header h1 {
            margin: 0;
            font-size: 42px;
        }

        .site-header p {
            margin-top: 10px;
            font-size: 18px;
        }

        .site-nav {
            background: #8e0000;
            padding: 14px 20px;
            text-align: center;
        }

        .site-nav a {
            color: white;
            text-decoration: none;
            margin: 0 12px;
            font-weight: bold;
            padding: 8px 12px;
            border-radius: 6px;
        }

        .site-nav a:hover {
            background: rgba(255, 255, 255, 0.18);
        }

        .container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .card {
            background: white;
            padding: 35px;
            border-radius: 14px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.10);
            border-left: 8px solid #d32f2f;
        }

        .card h2 {
            margin-top: 0;
            font-size: 32px;
            color: #b71c1c;
        }

        .card p {
            font-size: 17px;
            line-height: 1.6;
        }

        .success-box {
            background: #e8f5e9;
            border: 1px solid #81c784;
            color: #1b5e20;
            padding: 18px;
            border-radius: 10px;
            margin: 20px 0;
            font-weight: bold;
        }

        .status {
            display: inline-block;
            background: #fff3e0;
            color: #e65100;
            padding: 8px 12px;
            border-radius: 999px;
            font-weight: bold;
        }

        .btn {
            display: inline-block;
            margin-top: 20px;
            background: #d32f2f;
            color: white;
            text-decoration: none;
            padding: 12px 18px;
            border-radius: 8px;
            font-weight: bold;
        }

        .btn:hover {
            background: #b71c1c;
        }

        .site-footer {
            text-align: center;
            padding: 25px;
            color: #666;
            font-size: 14px;
        }

        @media screen and (max-width: 600px) {
            .site-header h1 {
                font-size: 32px;
            }

            .card {
                padding: 24px;
            }

            .site-nav a {
                display: block;
                margin: 6px 0;
            }
        }
    </style>
</head>

<body>

<header class="site-header">
    <h1>SoccorsoWeb</h1>
    <p>Portale per la gestione delle richieste di soccorso</p>
</header>

<nav class="site-nav">
    <a href="home">Home</a>
    <a href="login">Login</a>
</nav>

<main class="container">

    <section class="card">
        <h2>Richiesta inviata</h2>

        <div class="success-box">
            La tua richiesta è stata registrata correttamente.
        </div>

        <p>
            Stato iniziale della richiesta:
            <span class="status">DA_CONFERMARE</span>
        </p>

        <a class="btn" href="home">Torna alla home</a>
    </section>

</main>

<footer class="site-footer">
    <p>&copy; 2026 SoccorsoWeb - Progetto Web Engineering</p>
</footer>

</body>
</html>