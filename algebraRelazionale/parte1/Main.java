public class Main {

    public static void main(String[] args) {
        Relation persone = new CSVLoader("Persone.csv").loadCSVinRelation();
        Relation altrePersone = new CSVLoader("Persone2.csv").loadCSVinRelation();
        Relation ordini = new CSVLoader("Ordini").loadCSVinRelation();
        Relation prodotti = new CSVLoader("Prodotti").loadCSVinRelation();

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

        System.out.println("=== Prodotto === \n ");
        System.out.println(persone.prodotto(prodotti).toString());

        System.out.println("=== Joint === \n ");
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
        System.out.println("tot: " + totaleGenerale + "\n");

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

        System.out.println("=== Visualizzare acquirenti del piu costoso === \n ");
         
        for (int i = 0; i < prodotti.getRows().size(); i++) {

            Row prodotto = prodotti.getRows().get(i);
            int prezzo = Integer.parseInt(prodotto.getValue(2));

            if (prezzo > prezzoMax) {
                prezzoMax = prezzo;
                idProdottoCostoso = prodotto.getValue(0);
            }
        }

        System.out.println("\n utenti che hanno acquisto il prodotto piu costoso");

        for (int i = 0; i < ordini.getRows().size(); i++) {

            Row ordine = ordini.getRows().get(i);

            if (ordine.getValue(2).equals(idProdottoCostoso)) {

                String idUtente = ordine.getValue(1);
                for (int j = 0; j < persone.getRows().size(); j++) {
                    Row persona = persone.getRows().get(j);

                    if (persona.getValue(0).equals(idUtente)) {
                        System.out.println(persona.getValue(1) + " " + persona.getValue(2));
                    }
                }
            }
        }
    }
}
