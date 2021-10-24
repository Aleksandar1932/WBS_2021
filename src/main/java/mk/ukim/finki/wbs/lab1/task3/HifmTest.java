package mk.ukim.finki.wbs.lab1.task3;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.SelectorImpl;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static mk.ukim.finki.wbs.lab1.Utils.iterateAndPrintStatementIterator;
import static mk.ukim.finki.wbs.lab1.Utils.readModel;

class Hifm {
    public static String hifmOntPrefix;
    public static String hifmDataPrefix;
    public static String rdfsPrefix;


    public static void init(Model model) {
        hifmOntPrefix = model.getNsPrefixURI("hifm-ont");
        hifmDataPrefix = "http://purl.org/net/hifm/data#";
        rdfsPrefix = model.getNsPrefixURI("rdfs");
    }

    public static void printAllDrugs(Model model) {
        SimpleSelector selector = new SelectorImpl(null, model.getProperty(Hifm.rdfsPrefix + "label"), (Object) null);
        StmtIterator iterator = model.listStatements(selector);

        TreeSet<String> drugs = iterator.toList()
                .stream()
                .map(statement -> statement.getObject().toString())
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.printf("drugs: %s%n", drugs);
    }

    public static void printAllRelations(Model model, String drugId) {
        System.out.printf("Printing relations for %s%n", drugId);
        SimpleSelector selector = new SimpleSelector(model.getResource(drugId), null, (RDFNode) null);
        StmtIterator iterator = model.listStatements(selector);
        iterateAndPrintStatementIterator(iterator);
    }

    public static void printSimilarDrugs(Model model, String drugId) {
        System.out.printf("Printing similar drugs to %s%n", drugId);
        SimpleSelector selector = new SimpleSelector(null, model.getProperty(Hifm.hifmOntPrefix + "similarTo"), model.getResource(drugId));
        StmtIterator iterator = model.listStatements(selector);

        Set<String> similarDrugs = iterator.toList().stream()
                .map(statement -> statement.getSubject()
                        .getProperty(new PropertyImpl(Hifm.rdfsPrefix + "label"))
                        .getObject().toString()
                )
                .collect(Collectors.toSet());

        System.out.println(similarDrugs);
    }

    private static Double getPrice(Model model, String drugId) {
        SimpleSelector selector = new SimpleSelector(model.getResource(drugId), model.getProperty(Hifm.hifmOntPrefix + "refPriceWithVAT"), (Object) null);
        StmtIterator iterator = model.listStatements(selector);

        return iterator.toList().stream().map(statement -> (int) statement.getObject().asLiteral().getValue() * 1.0).findFirst().orElse(0.0);
    }

    public static void printPriceAndPriceOfSimilar(Model model, String drugId) {
        System.out.printf("Price of %s is %.2f\n\tPrice of similar:\n", drugId, getPrice(model, drugId));

        SimpleSelector similarSelector = new SimpleSelector(null, model.getProperty(Hifm.hifmOntPrefix + "similarTo"), model.getResource(drugId));
        StmtIterator similarIterator = model.listStatements(similarSelector);

        similarIterator.toList().forEach(statement -> {
            String URI = statement.getSubject().getURI();
            System.out.printf("\tPrice of %s is %.2f\n", URI, getPrice(model, URI));
        });
    }
}

public class HifmTest {
    public static void main(String[] args) {
        Model model = readModel("hifm-dataset.ttl", "TURTLE");
        Hifm.init(model);

        Hifm.printAllDrugs(model);
        Hifm.printAllRelations(model, Hifm.hifmDataPrefix + "975036");
        Hifm.printSimilarDrugs(model, Hifm.hifmDataPrefix + "98167");
        Hifm.printPriceAndPriceOfSimilar(model, Hifm.hifmDataPrefix + "98167");
    }

}
