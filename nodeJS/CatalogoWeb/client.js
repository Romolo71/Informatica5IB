const url = "http://127.0.0.1:8000/";
const risultato = document.getElementById("risultato");


async function caricaProdotti() {

    try {

        const risposta = await fetch(url + "prodotti.json");

        if (!risposta.ok) {
            throw new Error("Errore HTTP: " + risposta.status);
        }

        const dati = await risposta.json();

        risultato.innerHTML = "";

        dati.prodotti.forEach((prodotto, index) => {

            const card = document.createElement("div");

            card.classList.add("prodotto");

            card.style.animationDelay =
                `${index * 0.08}s`;

            card.innerHTML = `
                <div class="categoria">
                    ${prodotto.categoria}
                </div>

                <h2>${prodotto.nome}</h2>

                <div class="separatore"></div>

                <div class="footer-prodotto">

                    <span class="disponibile">
                        ● Disponibile
                    </span>

                    <span class="prezzo">
                        ${prodotto.prezzo.toFixed(2)} €
                    </span>

                </div>
            `;

            risultato.appendChild(card);
        });

    } catch (errore) {

        console.error("Errore:", errore);

        risultato.innerHTML = `
            <div class="errore">
                <h2>Errore</h2>
                <p>
                    Impossibile comunicare con il server.
                </p>
            </div>
        `;

    }
}

caricaProdotti();