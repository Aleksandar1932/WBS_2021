package mk.ukim.finki.wbs.lab1.task2;
import org.apache.jena.rdf.model.*;
import static mk.ukim.finki.wbs.lab1.Utils.iterateAndPrintStatementIterator;
import static mk.ukim.finki.wbs.lab1.Utils.readModel;


public class PersonalRDFReaderTest {
    public static void main(String[] args) {
        Model model = readModel("D:\\projects\\WBS_2021\\aleksandarIvanovskiGraph.ttl", "TURTLE");

        System.out.println("Printing model in TURTLE format");
        model.write(System.out, "TURTLE");

        Selector selector = new SimpleSelector(model.getResource("https://www.linkedin.com/in/ivanovskia/"), null, (Object) null);

        StmtIterator iterator = model.listStatements(selector);
        iterateAndPrintStatementIterator(iterator);
    }
}
