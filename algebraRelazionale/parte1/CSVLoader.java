import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVLoader {

    // * Percorso del file CSV che verrà caricato
    String file = "";

    /**
     * * Costruttore della classe
     *
     *   @param file percorso del file CSV da leggere
     */
    public CSVLoader(String file) {
        this.file = file;
    }

    /**
     * * Legge il file CSV e restituisce una Relation contenente i dati caricati
     *
     *   @return Relation costruita a partire dal contenuto del CSV
     */
    public Relation loadCSVinRelation() {

        // * Relation che verrà popolata con i dati letti dal CSV
         Relation loaded = new Relation();

        // TODO: Salvare qui le righe lette dal CSV
        // ? Ogni elemento esterno rappresenta una riga
        // ? Ogni lista interna contiene i campi di quella riga
        List<List<String>> records = new ArrayList<>();

        // * Contatore delle righe lette dal file
        int rowNumber = 0;

        try {
            // * Apre il file e crea un buffer per leggerlo riga per riga
            BufferedReader br =
                    new BufferedReader(new FileReader(this.file));

            String line;

            // * Continua a leggere finché readLine() non restituisce null,
            // * cioè finché non viene raggiunta la fine del file
            while ((line = br.readLine()) != null) {

                // * Divide la riga usando la virgola come separatore
                // ? Esempio:
                // ? "Mario,Rossi,22" -> ["Mario", "Rossi", "22"]
                String[] campi = line.split(",");
                
                
                // * Converto "campi" in una List<String>
                List<String> campiList = Arrays.asList(campi);
                // * e la aggiungo a "records"
                records.add(campiList);

                // * Incrementa il numero della riga corrente
                rowNumber++;

                //* Uso i dati letti
                //* per popolare l'oggetto "loaded"
                
                //? Fare for per aggiungere man mano che creo
                //? oggetti ROW
                loaded.setHeader(records.get(0));
                for (int i = 1; i < records.size(); i++) {
                    Row r = new Row(records.get(i));
                    loaded.addRow(r);
                }
            }
            br.close();

        } catch (Exception e) {

            // ! Viene eseguito se si verifica un errore durante
            // ! l'apertura o la lettura del file
            System.out.println("can't load " + this.file);
        }

        // * Restituisce la Relation caricata
        return loaded;
    }
}