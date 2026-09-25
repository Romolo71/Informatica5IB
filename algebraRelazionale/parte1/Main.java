public class Main {

    public static void main(String[] args) {
        Relation persone = new CSVLoader("Persone.csv").loadCSVinRelation();
        Relation altrePersone = new CSVLoader("Persone2.csv").loadCSVinRelation();

        System.out.println("=== Relazione persone ===");
        System.out.println(persone);

        System.out.println("=== Selection: persone con nome Mario ===");
        System.out.println(persone.selection("nome", "Mario"));

        System.out.println("=== Projection: solo nome e cognome ===");
        System.out.println(persone.project("nome", "cognome"));

        System.out.println("=== Union: persone + altre persone ===");
        System.out.println(persone.union(altrePersone));

        System.out.println("=== Difference: persone non presenti in altre persone ===");
        System.out.println(persone.difference(altrePersone));
    }
}
