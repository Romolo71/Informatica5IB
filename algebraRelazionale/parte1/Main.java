public class Main {

    public static void stampaTitolo(String titolo) {
        int width = Math.max(60, titolo.length() + 4);
        int spacing = width - titolo.length();
        int leftSpacing = spacing / 2;
        int rightSpacing = spacing - leftSpacing;
        String blank = "║" + " ".repeat(width) + "║";

        System.out.println();
        System.out.println("╔" + "═".repeat(width) + "╗");
        System.out.println(blank);
        System.out.println("║" + " ".repeat(leftSpacing) + titolo + " ".repeat(rightSpacing) + "║");
        System.out.println(blank);
        System.out.println("╚" + "═".repeat(width) + "╝");
        System.out.println();
    }

    public static void main(String[] args) {
        Relation persone = new CSVLoader("Persone.csv").loadCSVinRelation();
        Relation altrePersone = new CSVLoader("Persone2.csv").loadCSVinRelation();
        Relation ordini = new CSVLoader("Ordini.csv").loadCSVinRelation();
        Relation prodotti = new CSVLoader("Prodotti.csv").loadCSVinRelation();

        Relation city = new CSVLoader("Country_DBs/City.csv").loadCSVinRelation();
        Relation country = new CSVLoader("Country_DBs/Country.csv").loadCSVinRelation();
        Relation countryLanguage = new CSVLoader("Country_DBs/CountryLanguage.csv").loadCSVinRelation();

        int prezzoMax = 0;
        String idProdottoCostoso = "";

        System.out.println("=== Relazione persone === \n ");
        System.out.println(persone);

        System.out.println("=== Selection: persone con nome Mario === \n ");
        System.out.println(persone.selection("nome", "Mario"));

        System.out.println("=== Projection: solo nome e cognome === \n ");
        System.out.println(persone.project("nome", "cognome"));

        System.out.println("=== Union: persone + altre persone === \n ");
        System.out.println(persone.union(altrePersone));

        System.out.println("=== Difference: persone non presenti in altre persone === \n ");
        System.out.println(persone.difference(altrePersone));

        System.out.println("=== Prodotto: Persone e Prodotti === \n ");
        System.out.println(persone.prodotto(prodotti).toString());

        System.out.println("=== Joint: Unione di Ordini e Prodotti con \"id_prodotto\" come attributo in comune === \n ");
        String[] join1 = {"id_prodotto", "id_prodotto"};
        Relation joinOrdiniProdotti = ordini.join(prodotti, join1);
        System.out.println(joinOrdiniProdotti.toString());

        System.out.println("=== Visualizzare totale ordini === \n ");
        int totaleGenerale = 0;

        for (int i = 0; i < ordini.getRows().size(); i++) {
            Row ordine = ordini.getRows().get(i);

            int idProdotto = Integer.parseInt(ordine.getValue(2));
            int quantita = Integer.parseInt(ordine.getValue(3));

            for (int j = 0; j < prodotti.getRows().size(); j++) {
                Row prodotto = prodotti.getRows().get(j);
                if (Integer.parseInt(prodotto.getValue(0)) == idProdotto) {

                    int prezzo = Integer.parseInt(prodotto.getValue(2));
                    totaleGenerale += prezzo * quantita;
                }

            }
        }
        System.out.println("Ci sono un totale di " + totaleGenerale + " ordini \n");

        System.out.println("=== Visualizzare totale singolo === \n ");
        for (int i = 0; i < ordini.getRows().size(); i++) {
            Row ordine = ordini.getRows().get(i);
            int idOrdine = Integer.parseInt(ordine.getValue(0));
            int idProdotto = Integer.parseInt(ordine.getValue(2));
            int quantita = Integer.parseInt(ordine.getValue(3));

            for (int j = 0; j < prodotti.getRows().size(); j++) {
                Row prodotto = prodotti.getRows().get(j);
                if (Integer.parseInt(prodotto.getValue(0)) == idProdotto) {
                    int prezzo = Integer.parseInt(prodotto.getValue(2));
                    int totaleOrdine = prezzo * quantita;
                    System.out.println("Ordine " + idOrdine + " -> " + totaleOrdine + " euro");
                }
            }
        }

        System.out.println("\n=== Visualizzare acquirenti del piu costoso === \n ");
         
        for (int i = 0; i < prodotti.getRows().size(); i++) {

            Row prodotto = prodotti.getRows().get(i);
            int prezzo = Integer.parseInt(prodotto.getValue(2));

            if (prezzo > prezzoMax) {
                prezzoMax = prezzo;
                idProdottoCostoso = prodotto.getValue(0);
            }
        }

        System.out.println("Gli utenti che hanno acquisto il prodotto piu costoso sono: ");

        for (int i = 0; i < ordini.getRows().size(); i++) {

            Row ordine = ordini.getRows().get(i);

            if (ordine.getValue(2).equals(idProdottoCostoso)) {

                String idUtente = ordine.getValue(1);
                for (int j = 0; j < persone.getRows().size(); j++) {
                    Row persona = persone.getRows().get(j);

                    if (persona.getValue(0).equals(idUtente)) {
                        System.out.println("- " + persona.getValue(1) + " " + persona.getValue(2));
                    }
                }
            }
        }

        /*Utilizzando le funzioni realizzate nelle precedenti esercitazioni, leggi i seguenti files CSV e crea il codice per eseguire le seguenti interrogazioni:
            - trova tutte le nazioni Europee
            - trova tutte le città della Francia
            - trova il nome delle nazioni che hanno una popolazione compresa tra 100 milioni e 200 milioni di abitanti
            - trova, per tutte le nazioni del sud America, il nome della capitale, la popolazione e nome dello stato
            - trova le nazioni asiatiche con numero di abitanti maggiore di quello del Giappone.
            - trova per l’Italia, la  popolazione della città col maggior numero di abitanti e la popolazione della città col minor numero di abitanti
            - trova tutte le nazioni in cui si parla inglese e non si parla francese 
        */
        stampaTitolo("PARTE 3: VISUALIZZAZIONE IN BASE AL DB COUNTRY'S");

        System.out.println("=== Tutte le nazioni Europee === \n ");
        Relation europe = country.selection("Continent", "Europe");
        System.out.println(europe);

        System.out.println("=== Tutte le città della Francia === \n ");
        Relation francia = city.project("Name", "CountryCode").selection("CountryCode", "FRA");
        System.out.println(francia);
        
        System.out.println("=== Nazioni con popolazione tra 100 e 200 milioni === \n ");
        Relation pop100 = country.selection("Population", ">=", 100000000);
        Relation pop200 = country.selection("Population", "<=", 200000000);
        Relation pop100_200 = pop100.join(pop200, new String[]{"Name", "Name"});
        System.out.println(pop100_200.project("Name", "Population"));

        System.out.println("=== Nazioni del Sud America con nome capitale, popolazione e nome dello stato === \n ");
        Relation sudAmerica = country.selection("Continent", "South America");
        System.out.println(sudAmerica.project("Name", "Capital", "Population"));

        System.out.println("=== Nazioni asiatiche con numero di abitanti maggiore di quello del Giappone === \n ");
    }
}
