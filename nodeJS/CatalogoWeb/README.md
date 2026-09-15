# Progetto Catalogo Web 🛒

Sviluppo di una semplice applicazione web **Client/Server** per la visualizzazione dinamica di un catalogo di prodotti.

Il progetto ha lo scopo di comprendere il funzionamento della comunicazione tra un **client web** e un **server HTTP**, utilizzando JavaScript e la funzione `fetch()` per richiedere e visualizzare dati memorizzati in formato **JSON**.

---

## 👤 Informazioni Progetto

* **Realizzatore:** Riccardo Tonini
* **Richiesto da:** Prof. Stefano Giacomello
* **Istituto:** ITIS "C. Zuccante"
* **Tecnologie:** HTML5, CSS3, JavaScript ES6, JSON, HTTP

---

## 📚 Indice

* [🖥️ Descrizione del Progetto](#️-descrizione-del-progetto)
* [🌐 Comunicazione Client / Server](#-comunicazione-client--server)
* [⚙️ Funzionamento](#️-funzionamento)
* [🛠️ Funzionalità Implementate](#️-funzionalità-implementate)
* [📁 Struttura dei File](#-struttura-dei-file)
* [📦 Struttura dei Dati](#-struttura-dei-dati)
* [🚀 Istruzioni per l'uso](#-istruzioni-per-luso)

---

## 🖥️ Descrizione del Progetto

Il progetto consiste in un **catalogo web di prodotti** i cui dati non sono inseriti direttamente all'interno della pagina HTML.

Le informazioni vengono invece memorizzate in un file `prodotti.json` e richieste dal client tramite una comunicazione HTTP con il server.

Una volta ricevuti i dati, JavaScript genera dinamicamente le card dei prodotti e le inserisce all'interno della pagina.

Ogni prodotto contiene le seguenti informazioni:

* Nome
* Categoria
* Prezzo

L'interfaccia è stata realizzata tramite CSS ed è completamente responsive. Le card presentano inoltre animazioni ed effetti al passaggio del mouse per rendere il catalogo più moderno e interattivo.

Durante il caricamento dei dati viene mostrato un **loader**, che scompare una volta terminata la comunicazione con il server.

---

## 🌐 Comunicazione Client / Server

Lo scopo principale del progetto è comprendere il funzionamento di una semplice architettura **Client/Server**.

Il **client** è rappresentato dal browser dell'utente e dai file:

* `index.html`
* `style.css`
* `client.js`

Il **server** rende disponibili le risorse necessarie tramite il protocollo HTTP.

Il client effettua una richiesta al server per ottenere il file:

```text
prodotti.json
```

La richiesta viene eseguita tramite la funzione JavaScript:

```javascript
fetch()
```

Nel progetto viene utilizzato il seguente indirizzo:

```javascript
const url = "http://127.0.0.1:8000/";
```

L'indirizzo `127.0.0.1` identifica il computer locale, mentre `8000` rappresenta la porta sulla quale è in ascolto il server HTTP.

Il client richiede quindi la risorsa:

```text
http://127.0.0.1:8000/prodotti.json
```

Il server restituisce il file JSON e JavaScript può successivamente elaborarne il contenuto.

Il flusso della comunicazione può essere riassunto in questo modo:

```text
Browser / Client
      │
      │  Richiesta HTTP
      ▼
Server localhost:8000
      │
      │  prodotti.json
      ▼
   client.js
      │
      │  Elaborazione dati
      ▼
      DOM
      │
      ▼
Catalogo visualizzato
```

---

## ⚙️ Funzionamento

La logica principale dell'applicazione si trova nel file `client.js`.

### 1. Richiesta dei dati

La funzione `caricaProdotti()` viene dichiarata come asincrona:

```javascript
async function caricaProdotti()
```

Al suo interno viene utilizzato `fetch()` per effettuare la richiesta al server:

```javascript
const risposta = await fetch(url + "prodotti.json");
```

La parola chiave `await` permette di attendere la risposta del server prima di continuare l'esecuzione della funzione.

### 2. Conversione del JSON

Una volta ricevuta la risposta, i dati vengono convertiti in un oggetto JavaScript:

```javascript
const dati = await risposta.json();
```

In questo modo è possibile accedere all'array dei prodotti contenuto nel file JSON.

### 3. Visualizzazione dei prodotti

L'array viene attraversato utilizzando:

```javascript
dati.prodotti.forEach(...)
```

Per ogni prodotto viene creato dinamicamente un elemento HTML contenente le relative informazioni.

Le card vengono successivamente aggiunte all'elemento:

```html
<div id="risultato"></div>
```

presente nella pagina principale.

Questo permette di mantenere separati i **dati** dalla struttura HTML della pagina.

### 4. Gestione degli errori

La comunicazione con il server viene gestita tramite una struttura:

```javascript
try {
    // richiesta e visualizzazione dati
} catch (errore) {
    // gestione errore
}
```

Se il server non è raggiungibile oppure si verifica un problema durante il caricamento dei dati, viene mostrato un messaggio di errore all'utente.

### 5. Loader

Durante il caricamento viene visualizzato un loader tramite l'immagine:

```text
loader.png
```

Una volta completata la richiesta dei prodotti, il loader viene nascosto tramite JavaScript applicando un'apposita classe CSS.

---

## 🛠️ Funzionalità Implementate

Il progetto implementa le seguenti funzionalità:

1. **Comunicazione HTTP:** il client comunica con un server locale tramite una richiesta HTTP.

2. **Caricamento asincrono:** utilizzo di `async`, `await` e `fetch()` per ottenere i dati senza bloccare l'esecuzione della pagina.

3. **Lettura di dati JSON:** i prodotti vengono memorizzati separatamente dalla pagina web all'interno del file `prodotti.json`.

4. **Manipolazione del DOM:** JavaScript genera dinamicamente gli elementi necessari alla visualizzazione dei prodotti.

5. **Gestione degli errori:** eventuali problemi nella comunicazione con il server vengono intercettati tramite `try...catch`.

6. **Loader di caricamento:** durante il recupero dei dati viene visualizzata un'animazione di caricamento.

7. **Interfaccia responsive:** il catalogo si adatta automaticamente alle diverse dimensioni dello schermo.

8. **Animazioni CSS:** le card utilizzano transizioni, effetti hover e animazioni di ingresso per migliorare l'interfaccia grafica.

---

## 📁 Struttura dei File

Il progetto è organizzato nel seguente modo:

```text
CatalogoWeb/
│
├── index.html
├── style.css
├── client.js
├── prodotti.json
├── loader.png
└── README.md
```

### `index.html`

Contiene la struttura principale della pagina web.

Definisce:

* il loader;
* il titolo del catalogo;
* il contenitore nel quale vengono inseriti i prodotti;
* il collegamento al foglio di stile;
* il collegamento allo script JavaScript.

### `style.css`

Contiene la parte grafica dell'applicazione.

Gestisce:

* layout del catalogo;
* colori;
* card dei prodotti;
* responsive design;
* animazioni;
* effetti hover;
* loader.

### `client.js`

Contiene la logica dell'applicazione.

Si occupa di:

* collegarsi al server;
* richiedere `prodotti.json`;
* convertire la risposta JSON;
* generare dinamicamente le card;
* mostrare eventuali errori;
* gestire il loader.

### `prodotti.json`

Contiene i dati utilizzati dal catalogo.

In questo modo i dati sono separati dalla struttura HTML e possono essere modificati senza dover intervenire direttamente sulla pagina.

### `loader.png`

Immagine utilizzata per l'animazione mostrata durante il caricamento dei dati.

---

## 📦 Struttura dei Dati

I prodotti sono memorizzati all'interno di un file JSON.

La struttura utilizzata è simile alla seguente:

```json
{
    "prodotti": [
        {
            "nome": "Prodotto 1",
            "categoria": "Elettronica",
            "prezzo": 99.99
        },
        {
            "nome": "Prodotto 2",
            "categoria": "Informatica",
            "prezzo": 149.90
        }
    ]
}
```

Ogni prodotto è rappresentato da un oggetto con tre proprietà:

| Proprietà   | Descrizione                          |
| ----------- | ------------------------------------ |
| `nome`      | Nome del prodotto                    |
| `categoria` | Categoria di appartenenza            |
| `prezzo`    | Prezzo del prodotto espresso in euro |

JavaScript utilizza queste proprietà per costruire automaticamente il contenuto visualizzato nella pagina.

---

## 🚀 Istruzioni per l'uso

A differenza di una pagina HTML completamente statica, il progetto deve essere eseguito tramite un **server HTTP locale**, poiché il client deve effettuare una richiesta per ottenere il file JSON.

Posizionarsi tramite terminale nella cartella contenente il progetto.

Se è installato Python, è possibile avviare rapidamente un server HTTP con:

```bash
python -m http.server 8000
```

Il server sarà disponibile all'indirizzo:

```text
http://127.0.0.1:8000/
```

Aprire quindi nel browser:

```text
http://127.0.0.1:8000/index.html
```

All'apertura della pagina:

1. viene mostrato il loader;
2. `client.js` esegue una richiesta HTTP al server;
3. il server restituisce `prodotti.json`;
4. JavaScript converte i dati ricevuti;
5. vengono generate dinamicamente le card dei prodotti;
6. il loader scompare;
7. il catalogo viene visualizzato all'utente.

---

## 🎯 Obiettivo Didattico

Il progetto è stato realizzato con finalità didattiche per approfondire i concetti fondamentali dello sviluppo web e, in particolare, la comunicazione tra **client e server**.

L'esercitazione permette di applicare concretamente diversi argomenti affrontati durante lo studio di JavaScript e delle applicazioni web:

* protocollo HTTP;
* architettura Client/Server;
* richieste asincrone;
* utilizzo di `fetch()`;
* sintassi `async/await`;
* formato JSON;
* gestione degli errori;
* manipolazione dinamica del DOM;
* separazione tra struttura, stile, logica e dati.
