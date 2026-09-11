const url = "http://127.0.0.1:8000/";
const bottone = document.getElementById("btnInvia");
const risultato = document.getElementById("risultato");

bottone.addEventListener("click", async () => {

    try {

        // invia una richiesta al server
        const risposta = await fetch(url + "messaggio");

        // legge la risposta JSON
        const dati = await risposta.json();

        // visualizza il messaggio ricevuto
        risultato.innerHTML += dati.messaggio + "<br>";

    } catch (errore) {
        console.error("Errore: ", errore);
        risultato.textContent = "Errore nella comunicazione con il server";
    }
});