<!DOCTYPE html>

<html lang="it">

<head>
    <meta charset="UTF-8">


<meta
    name="viewport"
    content="width=device-width, initial-scale=1.0"
>

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
        padding: 35px 20px;
        background: linear-gradient(
            135deg,
            #1565c0,
            #1976d2
        );
        color: white;
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
        padding: 14px;
        background: #0d47a1;
        text-align: center;
    }

    nav a {
        display: inline-block;
        margin: 4px 10px;
        padding: 8px 12px;
        border-radius: 6px;
        color: white;
        font-weight: bold;
        text-decoration: none;
    }

    nav a:hover {
        background: rgba(255, 255, 255, 0.18);
    }

    main {
        max-width: 1050px;
        margin: 35px auto;
        padding: 0 20px;
    }

    .panel {
        padding: 30px;
        border-radius: 14px;
        background: white;
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.10);
    }

    .panel h2 {
        margin-top: 0;
        color: #1565c0;
    }

    .panel p {
        line-height: 1.5;
    }

    .msg-ok {
        margin-bottom: 22px;
        padding: 14px;
        border-left: 6px solid #2e7d32;
        border-radius: 7px;
        background: #e8f5e9;
        color: #1b5e20;
    }

    .msg-errore {
        margin-bottom: 22px;
        padding: 14px;
        border-left: 6px solid #c62828;
        border-radius: 7px;
        background: #ffebee;
        color: #b71c1c;
    }

    .squadra-box {
        margin-top: 25px;
        padding: 22px;
        border: 1px solid #d7e5f5;
        border-left: 6px solid #1976d2;
        border-radius: 12px;
        background: #f5f9ff;
    }

    .squadra-box h3 {
        margin: 0 0 12px;
        color: #1565c0;
    }

    .squadra-info {
        margin: 7px 0;
    }

    .etichetta {
        font-weight: bold;
    }

    .stato-assegnato {
        color: #ef6c00;
        font-weight: bold;
    }

    .stato-disponibile {
        color: #2e7d32;
        font-weight: bold;
    }

    .form-lascia-squadra {
        margin-top: 18px;
    }

    .btn-lascia-squadra {
        padding: 10px 16px;
        border: none;
        border-radius: 6px;
        background: #c62828;
        color: white;
        font-weight: bold;
        cursor: pointer;
    }

    .btn-lascia-squadra:hover {
        background: #8e0000;
    }

    .grid {
        display: grid;
        grid-template-columns: repeat(
            auto-fit,
            minmax(230px, 1fr)
        );
        gap: 20px;
        margin-top: 28px;
    }

    .card {
        display: block;
        padding: 22px;
        border-left: 6px solid #1976d2;
        border-radius: 12px;
        background: #f7f7f7;
        color: #222;
        text-decoration: none;
        transition: 0.2s;
    }

    .card:hover {
        transform: translateY(-4px);
        background: white;
        box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
    }

    .card h3 {
        margin-top: 0;
        color: #1565c0;
    }

    .card p {
        margin-bottom: 0;
        line-height: 1.5;
    }

    .card-disabilitata {
        border-left-color: #999;
        background: #eeeeee;
        color: #777;
        cursor: not-allowed;
    }

    .card-disabilitata h3 {
        color: #666;
    }

    .card-disabilitata:hover {
        transform: none;
        background: #eeeeee;
        box-shadow: none;
    }

    footer {
        padding: 25px;
        color: #666;
        font-size: 14px;
        text-align: center;
    }

    @media screen and (max-width: 700px) {

        nav a {
            display: block;
            margin: 6px 0;
        }

        header h1 {
            font-size: 30px;
        }

        .panel {
            padding: 22px;
        }
    }
</style>


</head>

<body>

<header>

`
<h1>Area Operatore</h1>

<p>
    Benvenuto ${(nome!"Operatore")?html}
    - Ruolo: ${(ruolo!"OPERATORE")?html}
</p>


</header>

<nav>


<a href="${contextPath}/operatore">
    Dashboard
</a>

<a href="${contextPath}/operatore/richieste">
    Richieste assegnate
</a>

<a href="${contextPath}/operatore/missioni">
    Missioni operative
</a>

<a href="${contextPath}/operatore/profilo">
    Profilo
</a>

<a href="${contextPath}/logout">
    Logout
</a>


</nav>

<main>


<section class="panel">

    <h2>Pannello Operatore</h2>

    <p>
        Da questa area puoi visualizzare le richieste
        assegnate, controllare le missioni operative e
        consultare il tuo profilo.
    </p>

    <#if successo?? && successo?has_content>

        <div class="msg-ok">

            <#if successo == "squadra_abbandonata">

                Sei stato rimosso correttamente dalla squadra.

            <#else>

                Operazione completata correttamente.

            </#if>

        </div>

    </#if>

    <#if errore?? && errore?has_content>

        <div class="msg-errore">

            <#if errore == "operatore_non_trovato">

                Non è stato possibile trovare il tuo profilo
                operatore.

            <#elseif errore == "nessuna_squadra">

                Non appartieni attualmente a nessuna squadra.

            <#elseif errore == "missione_in_corso">

                Non puoi lasciare la squadra mentre è collegata
                a una missione ancora in corso.

            <#elseif errore == "rimozione_fallita">

                Non è stato possibile rimuovere la tua
                appartenenza alla squadra.

            <#else>

                Si è verificato un errore durante l'operazione.

            </#if>

        </div>

    </#if>

    <div class="squadra-box">

        <h3>Situazione operativa</h3>

        <#if squadra??>

            <p class="squadra-info">

                <span class="etichetta">
                    Squadra attuale:
                </span>

                ${(squadra.nome!"Squadra senza nome")?html}

            </p>

            <p class="squadra-info">

                <span class="etichetta">
                    Stato:
                </span>

                <span class="stato-assegnato">
                    ASSEGNATO
                </span>

            </p>

            <#if squadra.missione??>

                <p class="squadra-info">

                    <span class="etichetta">
                        Missione:
                    </span>

                    ${(squadra.missione.descrizione!"Missione assegnata")?html}

                </p>

            </#if>

            <form
                action="${contextPath}/operatore/lascia-squadra"
                method="post"
                class="form-lascia-squadra"
                onsubmit="return confirm('Vuoi davvero lasciare la squadra?');"
            >

                <button
                    type="submit"
                    class="btn-lascia-squadra"
                >
                    Lascia la squadra
                </button>

            </form>

        <#else>

            <p class="squadra-info">

                <span class="etichetta">
                    Squadra attuale:
                </span>

                Nessuna squadra assegnata

            </p>

            <p class="squadra-info">

                <span class="etichetta">
                    Stato:
                </span>

                <span class="stato-disponibile">
                    DISPONIBILE
                </span>

            </p>

        </#if>

    </div>

    <div class="grid">

        <#if squadra??>

            <a
                class="card"
                href="${contextPath}/operatore/richieste"
            >

                <h3>Richieste assegnate</h3>

                <p>
                    Visualizza le richieste collegate alla
                    tua squadra e agli interventi operativi.
                </p>

            </a>

            <a
                class="card"
                href="${contextPath}/operatore/missioni"
            >

                <h3>Missioni operative</h3>

                <p>
                    Controlla le missioni e le attività
                    collegate alla tua squadra.
                </p>

            </a>

        <#else>

            <div class="card card-disabilitata">

                <h3>Richieste assegnate</h3>

                <p>
                    Non sono presenti richieste assegnate
                    perché non appartieni a una squadra.
                </p>

            </div>

            <div class="card card-disabilitata">

                <h3>Missioni operative</h3>

                <p>
                    Non sono presenti missioni operative
                    perché non appartieni a una squadra.
                </p>

            </div>

        </#if>

        <a
            class="card"
            href="${contextPath}/operatore/profilo"
        >

            <h3>Profilo operatore</h3>

            <p>
                Visualizza le informazioni del tuo profilo,
                le patenti e le abilità possedute.
            </p>

        </a>

    </div>

</section>


</main>

<footer>


<p>
    &copy; 2026 SoccorsoWeb - Progetto Web Engineering
</p>


</footer>

</body>

</html>
