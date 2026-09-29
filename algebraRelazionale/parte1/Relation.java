import java.util.*;

public class Relation {
    List<String> header;
    ArrayList<Row> rows;

    /**
     * * Crea una relazione vuota.
     * * L'header contiene i nomi degli attributi; rows contiene le righe.
     */
    public Relation(List<String> header, ArrayList<Row> rows) {
        this.header = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public Relation(){
        this.header = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    /**
     * * Sostituisce le righe della relazione.
     * * Crea una nuova lista, così eventuali modifiche alla lista ricevuta
     * * non cambiano direttamente la lista interna.
     */
    public void setRows(ArrayList<Row> rows) {
        this.rows = new ArrayList<>(rows);
    }

    /** * Aggiunge una riga alla relazione. */
    public void addRow(Row rowToAdd) {
        this.rows.add(rowToAdd);
    }

    /** * Imposta i nomi degli attributi creando una copia della lista. */
    public void setHeader(List<String> header) {
        this.header = new ArrayList<>(header);
    }

    /** * Metodo getter dell'header per averne accesso */
    public List<String> getHeader(){
        return header;
    }

    /**
     * * SELEZIONE: mantiene solo le righe in cui l'attributo ha il valore richiesto.
     * * Esempio: selection("città", "Venezia").
     */
    public Relation selection(String attribute, String value) {
        int attributeIndex = header.indexOf(attribute);

        // ! Non possiamo cercare un valore in una colonna che non esiste.
        if (attributeIndex == -1) {
            throw new IllegalArgumentException("Attributo non presente: " + attribute);
        }

        Relation result = new Relation();
        result.setHeader(header);

        // * Controlla la colonna scelta in ogni riga e copia quelle corrispondenti.
        for (Row row : rows) {
            if (attributeIndex < row.values.size()
                    && Objects.equals(row.values.get(attributeIndex), value)) {
                result.addRow(new Row(new ArrayList<>(row.values)));
            }
        }

        return result;
    }

    /**
     * * PROIEZIONE: crea una relazione con le sole colonne richieste.
     * * Le righe duplicate nel risultato vengono eliminate.
     */
    private Relation project(List<String> attributes) {
        ArrayList<Integer> attributeIndexes = new ArrayList<>();

        // * Trova una volta sola la posizione di ogni colonna richiesta.
        for (String attribute : attributes) {
            int index = header.indexOf(attribute);
            if (index == -1) {
                throw new IllegalArgumentException("Attributo non presente: " + attribute);
            }
            attributeIndexes.add(index);
        }

        Relation result = new Relation();
        result.setHeader(attributes);

        // * Per ogni riga prende soltanto i valori delle colonne selezionate.
        for (Row row : rows) {
            ArrayList<String> projectedValues = new ArrayList<>();

            for (int index : attributeIndexes) {
                projectedValues.add(row.values.get(index));
            }

            Row projectedRow = new Row(projectedValues);

            // ? Due righe diverse possono diventare uguali dopo la proiezione.
            // * In quel caso la riga compare una sola volta nel risultato.
            if (!containsRow(result.rows, projectedRow)) {
                result.addRow(projectedRow);
            }
        }

        return result;
    }

    /**
     * * Permette di chiamare project("nome", "città")
     * * oltre a project(List.of("nome", "città")).
     */
    public Relation project(String... attributes) { //! RICORDA: "..." indica che ammette un numero variabile di argomenti
        return project(Arrays.asList(attributes));
    }

    /**
     * * UNIONE: raccoglie le righe di entrambe le relazioni.
     * * Ogni riga compare una sola volta.
     */
    public Relation union(Relation other) {
        checkCompatible(other);

        Relation result = new Relation();
        result.setHeader(header);

        addDistinctRows(result, rows);
        addDistinctRows(result, other.rows);

        return result;
    }

    /**
     * * DIFFERENZA: mantiene le righe presenti in questa relazione
     * * ma assenti nell'altra.
     */
    public Relation difference(Relation other) {
        checkCompatible(other);

        Relation result = new Relation();
        result.setHeader(header);

        for (Row row : rows) {
            if (!containsRow(other.rows, row)) {
                result.addRow(new Row(new ArrayList<>(row.values)));
            }
        }

        return result;
    }

    /**
     * * Verifica che le due relazioni abbiano lo stesso header.
     * * Serve prima di eseguire unione e differenza.
     */
    private void checkCompatible(Relation other) {
        if (other == null || !header.equals(other.header)) {
            throw new IllegalArgumentException(
                    "Le relazioni devono avere lo stesso schema"
            );
        }
    }

    /**
     * * Copia le righe da source a destination, saltando quelle già presenti.
     * * È usato dall'operazione di unione.
     */
    private void addDistinctRows(Relation destination, List<Row> source) {
        for (Row row : source) {
            if (!containsRow(destination.rows, row)) {
                destination.addRow(new Row(new ArrayList<>(row.values)));
            }
        }
    }

    /**
     * * Cerca una riga confrontando le liste dei suoi valori.
     * * Restituisce true se ne trova una uguale.
     */
    private boolean containsRow(List<Row> rowsToSearch, Row rowToFind) {
        for (Row row : rowsToSearch) {
            if (row.values.equals(rowToFind.values)) {
                return true;
            }
        }
        return false;
    }

    /**
     * * Converte la relazione in testo:
     * * prima l'header, poi una riga per ogni elemento di rows.
     */
    @Override
    public String toString() {
        String out = "";

        for (int i = 0; i < header.size(); i++) {
            out += header.get(i) + " ";
        }
        out += "\n";

        for (int i = 0; i < rows.size(); i++) {
            out += rows.get(i) + " \n";
        }

        return out;
    }
    
    /**
     * * Prodotto Cartesiano:
     * * Nella relazione finale tutti gli attributi delle due
     * * relazioni + tutte le combinazioni degli attributi
     */
    public Relation prodotto(Relation other) {
        Relation ris = new Relation();
        ArrayList<String> newHeader = new ArrayList<>();

        for (int i = 0; i < header.size(); i++) {
            newHeader.add(header.get(i));
        }

        for (int i = 0; i < other.header.size(); i++) {
            newHeader.add(other.header.get(i));
        }

        ArrayList<Row> newRows = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {

            for (int j = 0; j < other.rows.size(); j++) {
                ArrayList<String> values = new ArrayList<>();

                for (int k = 0; k < rows.get(i).getValues().size(); k++) {
                    values.add(rows.get(i).getValues().get(k));
                }
                for (int k = 0; k < other.rows.get(j).getValues().size(); k++) {
                    values.add(other.rows.get(j).getValues().get(k));
                }
                newRows.add(new Row(values));
            }
        }

        ris.setHeader(newHeader);
        ris.setRows(newRows);

        return ris;
    }

    /**
     * * Joint:
     * * Nella relazione finale si ottiene una tabella data
     * * dall'unione della prima relazione e la seconda in
     * * base ad un valore comune
     */

    public Relation join(Relation other, String[] joinField) {
        Relation prodotto = this.prodotto(other);

        int pos1 = prodotto.header.indexOf(joinField[0]);
        int pos2 = prodotto.header.lastIndexOf(joinField[1]);

        ArrayList<Row> righe = new ArrayList<>();

        for (int i = 0; i < prodotto.rows.size(); i++) {
            Row r = prodotto.rows.get(i);
            if (r.getValue(pos1).equals(r.getValue(pos2))) {
                righe.add(r);
            }
        }

        return new Relation(prodotto.getHeader(), righe);
    }
}