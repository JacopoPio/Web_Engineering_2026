<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Creazione missione - SoccorsoWeb</title>

    <style>
        {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f8;
            color: #222;
        }

        header {
            padding: 32px 20px;
            background: linear-gradient(135deg, #1565c0, #1976d2);
            color: white;
            text-align: center;
        }

        header h1 {
            margin: 0 0 8px;
        }

        header p {
            margin: 0;
        }

        nav {
            padding: 14px;
            background: #0d47a1;
            text-align: center;
        }

        nav a {
            display: inline-block;
            margin: 5px 12px;
            color: white;
            font-weight: bold;
            text-decoration: none;
        }

        nav a:hover {
            text-decoration: underline;
        }

        main {
            width: min(1050px, 94%);
            margin: 30px auto;
        }

        section {
            margin-bottom: 25px;
            padding: 24px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
        }

        section h2 {
            margin-top: 0;
            color: #0d47a1;
        }

        .messaggio {
            margin-bottom: 20px;
            padding: 14px 16px;
            border-radius: 7px;
            font-weight: bold;
        }

        .errore {
            border: 1px solid #ef9a9a;
            background: #fde2e2;
            color: #991b1b;
        }

        .successo {
            border: 1px solid #86c99a;
            background: #dff3e5;
            color: #166534;
        }

        .dettagli-richiesta {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 18px;
        }

        .dettaglio {
            padding: 14px;
            border: 1px solid #d8e0e7;
            border-radius: 7px;
            background: #f8fafc;
        }

        .dettaglio-intero {
            grid-column: 1 / -1;
        }

        .etichetta {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-size: 0.9rem;
            font-weight: bold;
        }

        .valore {
            margin: 0;
            overflow-wrap: anywhere;
        }

        .campo {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 7px;
            font-weight: bold;
        }

        input[type="text"],
        textarea {
            width: 100%;
            padding: 11px;
            border: 1px solid #b8c2cc;
            border-radius: 6px;
            font: inherit;
        }

        input[type="text"]:focus,
        textarea:focus {
            outline: 2px solid #90caf9;
            border-color: #1565c0;
        }

        textarea {
            min-height: 120px;
            resize: vertical;
        }

        fieldset {
            margin: 25px 0;
            padding: 18px;
            border: 1px solid #c7d1da;
            border-radius: 8px;
        }

        legend {
            padding: 0 8px;
            color: #0d47a1;
            font-weight: bold;
        }

        .spiegazione {
            margin-top: 0;
            color: #555;
        }

        .lista-risorse {
            display: grid;
            grid-template-columns: repeat(
                auto-fit,
                minmax(230px, 1fr)
            );
            gap: 12px;
        }

        .risorsa {
            display: flex;
            align-items: flex-start;
            gap: 9px;
            padding: 12px;
            border: 1px solid #d6dee5;
            border-radius: 7px;
            background: #f8fafc;
            cursor: pointer;
        }

        .risorsa:hover {
            background: #eaf2fb;
        }

        .risorsa input {
            width: auto;
            margin-top: 3px;
        }

        .risorsa-contenuto {
            flex: 1;
        }

        .risorsa-nome {
            display: block;
            margin-bottom: 3px;
            font-weight: bold;
        }

        .risorsa-dettaglio {
            display: block;
            color: #666;
            font-size: 0.9rem;
        }

        .nessun-elemento {
            margin: 0;
            padding: 14px;
            border-radius: 6px;
            background: #fff3cd;
            color: #664d03;
        }

        .obbligatorio {
            color: #c62828;
        }

        .azioni {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 25px;
        }

        button,
        .pulsante {
            display: inline-block;
            padding: 11px 20px;
            border: none;
            border-radius: 6px;
            background: #1565c0;
            color: white;
            font: inherit;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
        }

        button:hover,
        .pulsante:hover {
            background: #0d47a1;
        }

        .pulsante-secondario {
            background: #607d8b;
        }

        .pulsante-secondario:hover {
            background: #455a64;
        }

        footer {
            margin-top: 35px;
            padding: 20px;
            background: #263238;
            color: white;
            text-align: center;
        }

        @media (max-width: 700px) {
            .dettagli-richiesta {
                grid-template-columns: 1fr;
            }

            .dettaglio-intero {
                grid-column: auto;
            }

            .azioni {
                flex-direction: column;
            }

            button,
            .pulsante {
                width: 100%;
                text-align: center;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Crea nuova missione</h1>

    <p>
        Assegna una richiesta attiva a una squadra di soccorso
    </p>
</header>

<nav>
    <a href="${contextPath}/admin">
        Dashboard
    </a>

    <a href="${contextPath}/admin/richieste">
        Richieste
    </a>

    <a href="${contextPath}/admin/missioni">
        Missioni
    </a>

    <a href="${contextPath}/logout">
        Logout
    </a>
</nav>

<main>

    <#if errore??>
        <div class="messaggio errore">

            <#if errore == "richiesta_non_trovata">
                La richiesta selezionata non esiste.

            <#elseif errore == "richiesta_non_attiva">
                La richiesta non è più attiva oppure è già stata assegnata.

            <#elseif errore == "campi">
                Inserisci l'obiettivo e la posizione della missione.

            <#elseif errore == "caposquadra">
                Devi selezionare un caposquadra.

            <#elseif errore == "operatore_non_disponibile">
                Uno degli operatori selezionati non è più disponibile.

            <#elseif errore == "mezzo_non_disponibile">
                Uno dei mezzi selezionati non è più disponibile.

            <#elseif errore == "materiale_non_disponibile">
                Uno dei materiali selezionati non è più disponibile.

            <#elseif errore == "risorsa_non_valida">
                È stata selezionata una risorsa non valida.

            <#else>
                Non è stato possibile creare la missione.
            </#if>

        </div>
    </#if>

    <#if successo??>
        <div class="messaggio successo">
            Missione creata correttamente.
        </div>
    </#if>

    <section>

        <h2>Richiesta di soccorso</h2>

        <div class="dettagli-richiesta">

            <div class="dettaglio">
                <span class="etichetta">
                    Identificativo richiesta
                </span>

                <p class="valore">
                    ${(richiestaId!"Non disponibile")?html}
                </p>
            </div>

            <div class="dettaglio">
                <span class="etichetta">
                    Stato
                </span>

                <p class="valore">
                    ${(richiestaStato!"ATTIVA")?html}
                </p>
            </div>

            <div class="dettaglio">
                <span class="etichetta">
                    Segnalante
                </span>

                <p class="valore">
                    ${(nomeSegnalante!"Non indicato")?html}
                </p>
            </div>

            <div class="dettaglio">
                <span class="etichetta">
                    Email segnalante
                </span>

                <p class="valore">
                    ${(emailSegnalante!"Non indicata")?html}
                </p>
            </div>

            <div class="dettaglio dettaglio-intero">
                <span class="etichetta">
                    Descrizione
                </span>

                <p class="valore">
                    ${(richiestaDescrizione!"Nessuna descrizione")?html}
                </p>
            </div>

            <div class="dettaglio dettaglio-intero">
                <span class="etichetta">
                    Posizione segnalata
                </span>

                <p class="valore">
                    ${(richiestaPosizione!"Non indicata")?html}
                </p>
            </div>

        </div>

    </section>

    <section>

        <h2>Dati della missione</h2>

        <form
            method="post"
            action="${contextPath}/admin/missioni/nuova"
        >

            <input
                type="hidden"
                name="richiestaId"
                value="${(richiestaId!"")?html}"
            >

            <div class="campo">

                <label for="obiettivo">
                    Obiettivo
                    <span class="obbligatorio">*</span>
                </label>

                <textarea
                    id="obiettivo"
                    name="obiettivo"
                    maxlength="500"
                    placeholder="Descrivi l'obiettivo operativo della missione"
                    required
                >${(obiettivoInserito!"")?html}</textarea>

            </div>

            <div class="campo">

                <label for="posizione">
                    Posizione della missione
                    <span class="obbligatorio">*</span>
                </label>

                <input
                    type="text"
                    id="posizione"
                    name="posizione"
                    maxlength="255"
                    value="${(posizioneInserita!richiestaPosizione!"")?html}"
                    required
                >

            </div>

            <fieldset>

                <legend>
                    Caposquadra
                    <span class="obbligatorio">*</span>
                </legend>

                <p class="spiegazione">
                    Seleziona esattamente un operatore come caposquadra.
                </p>

                <#if operatoriDisponibili??
                    && operatoriDisponibili?size gt 0>

                    <div class="lista-risorse">

                        <#list operatoriDisponibili as operatore>

                            <label class="risorsa">

                                <input
                                    type="radio"
                                    name="caposquadra"
                                    value="${(operatore.email!"")?html}"
                                    required

                                    <#if caposquadraSelezionato??
                                        && caposquadraSelezionato
                                            == operatore.email>
                                        checked
                                    </#if>
                                >

                                <span class="risorsa-contenuto">

                                    <span class="risorsa-nome">
                                        ${(operatore.nome!"")?html}
                                        ${(operatore.cognome!"")?html}
                                    </span>

                                    <span class="risorsa-dettaglio">
                                        ${(operatore.email!"")?html}
                                    </span>

                                </span>

                            </label>

                        </#list>

                    </div>

                <#else>

                    <p class="nessun-elemento">
                        Non sono disponibili operatori da assegnare
                        come caposquadra.
                    </p>

                </#if>

            </fieldset>

            <fieldset>

                <legend>
                    Altri componenti della squadra
                </legend>

                <p class="spiegazione">
                    Puoi selezionare zero o più operatori aggiuntivi.
                </p>

                <#if operatoriDisponibili??
                    && operatoriDisponibili?size gt 0>

                    <div class="lista-risorse">

                        <#list operatoriDisponibili as operatore>

                            <label class="risorsa">

                                <input
                                    type="checkbox"
                                    name="operatori"
                                    value="${(operatore.email!"")?html}"

                                    <#if operatoriSelezionati??
                                        && operatoriSelezionati
                                            ?seq_contains(
                                                operatore.email
                                            )>
                                        checked
                                    </#if>
                                >

                                <span class="risorsa-contenuto">

                                    <span class="risorsa-nome">
                                        ${(operatore.nome!"")?html}
                                        ${(operatore.cognome!"")?html}
                                    </span>

                                    <span class="risorsa-dettaglio">
                                        ${(operatore.email!"")?html}
                                    </span>

                                </span>

                            </label>

                        </#list>

                    </div>

                <#else>

                    <p class="nessun-elemento">
                        Non sono disponibili altri operatori.
                    </p>

                </#if>

            </fieldset>

            <fieldset>

                <legend>
                    Mezzi
                </legend>

                <p class="spiegazione">
                    Seleziona gli eventuali mezzi da assegnare.
                </p>

                <#if mezziDisponibili??
                    && mezziDisponibili?size gt 0>

                    <div class="lista-risorse">

                        <#list mezziDisponibili as mezzo>

                            <label class="risorsa">

                                <input
                                    type="checkbox"
                                    name="mezzi"
                                    value="${(mezzo.targa!"")?html}"

                                    <#if mezziSelezionati??
                                        && mezziSelezionati
                                            ?seq_contains(mezzo.targa)>
                                        checked
                                    </#if>
                                >

                                <span class="risorsa-contenuto">

                                    <span class="risorsa-nome">
                                        ${(mezzo.nome!"Mezzo")?html}
                                    </span>

                                    <span class="risorsa-dettaglio">
                                        Targa:
                                        ${(mezzo.targa!"Non indicata")?html}
                                    </span>

                                    <#if mezzo.descrizione??>
                                        <span class="risorsa-dettaglio">
                                            ${mezzo.descrizione?html}
                                        </span>
                                    </#if>

                                </span>

                            </label>

                        </#list>

                    </div>

                <#else>

                    <p class="nessun-elemento">
                        Nessun mezzo disponibile.
                    </p>

                </#if>

            </fieldset>

            <fieldset>

                <legend>
                    Materiali
                </legend>

                <p class="spiegazione">
                    Seleziona gli eventuali materiali necessari.
                </p>

                <#if materialiDisponibili??
                    && materialiDisponibili?size gt 0>

                    <div class="lista-risorse">

                        <#list materialiDisponibili as materiale>

                            <label class="risorsa">

                                <input
                                    type="checkbox"
                                    name="materiali"
                                    value="${(materiale.id!"")?html}"

                                    <#if materialiSelezionati??
                                        && materialiSelezionati
                                            ?seq_contains(
                                                materiale.id?string
                                            )>
                                        checked
                                    </#if>
                                >

                                <span class="risorsa-contenuto">

                                    <span class="risorsa-nome">
                                        ${(materiale.tipo!"Materiale")?html}
                                    </span>

                                    <#if materiale.descrizione??>
                                        <span class="risorsa-dettaglio">
                                            ${materiale.descrizione?html}
                                        </span>
                                    </#if>

                                </span>

                            </label>

                        </#list>

                    </div>

                <#else>

                    <p class="nessun-elemento">
                        Nessun materiale disponibile.
                    </p>

                </#if>

            </fieldset>

            <div class="azioni">

                <button
                    type="submit"
                    <#if !operatoriDisponibili??
                        || operatoriDisponibili?size == 0>
                        disabled
                    </#if>
                >
                    Crea missione
                </button>

                <a
                    class="pulsante pulsante-secondario"
                    href="${contextPath}/admin/richieste"
                >
                    Annulla
                </a>

            </div>

        </form>

    </section>

</main>

<footer>
    SoccorsoWeb – Gestione missioni
</footer>

</body>
</html>
