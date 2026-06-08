<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle!"Richiesta inviata"} - SoccorsoWeb</title>

    <link rel="stylesheet" href="${contextPath!""}/static/css/style.css">
</head>

<body>

<header class="site-header">
    <h1>SoccorsoWeb</h1>
    <p>Portale per la gestione delle richieste di soccorso</p>
</header>

<nav class="site-nav">
    <a href="${contextPath!""}/home">Home</a>
    <a href="${contextPath!""}/login">Login</a>
</nav>

<main class="container">

    <section class="card">
        <h2>Richiesta inviata</h2>

        <p>
            La tua richiesta è stata registrata correttamente.
        </p>

        <p>
            In una versione completa del progetto, ora il sistema invierebbe una email
            di conferma al segnalante.
        </p>

        <p>
            Stato iniziale della richiesta:
            <strong>DA_CONFERMARE</strong>
        </p>

        <p>
            <a href="${contextPath!""}/home">Torna alla home</a>
        </p>
    </section>

</main>

<footer class="site-footer">
    <p>&copy; 2026 SoccorsoWeb - Progetto Web Engineering</p>
</footer>

<script src="${contextPath!""}/static/js/app.js"></script>

</body>
</html>
