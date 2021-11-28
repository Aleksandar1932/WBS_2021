package mk.ukim.finki.wbs.lab4.task2;

import mk.ukim.finki.wbs.lab1.Utils;
import org.apache.jena.rdf.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

class ModelFetcher {
    public static Model withCustomRequest(String drugURI, String format) throws IOException {
        URL url = new URL(drugURI);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Accept", "text/turtle");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);

        in.close();

        Model model = ModelFactory.createDefaultModel();
        model.read(response.toString(), format);
        return model;
    }

    public static Model withJena(String drugURI, String format) {
        Model model = ModelFactory.createDefaultModel();
        model.read(drugURI, format);
        return model;
    }
}

class Hifm {
    public static String hifmOntPrefix;
    public static String lodFinkiPrefix;
    public static String hifmDataPrefix;
    public static String drugBankPrefix;
    public static String rdfsPrefix;

    public static void init(Model model) {
        hifmOntPrefix = model.getNsPrefixURI("hifm-ont");
        lodFinkiPrefix = "http://linkeddata.finki.ukim.mk/lod/data/hifm#";
        hifmDataPrefix = "http://purl.org/net/hifm/data#";
        drugBankPrefix = model.getNsPrefixURI("drugbank");
        rdfsPrefix = model.getNsPrefixURI("rdfs");
    }

    private static String removePrefix(String full, String prefix) {
        return full.replaceAll(prefix, "");
    }


    public static Boolean isInBio2Rdf(Model model, String drugId) {
        SimpleSelector selector = new SimpleSelector(model.getResource(hifmDataPrefix + drugId), model.getProperty(rdfsPrefix + "seeAlso"), (RDFNode) null);
        return model.listStatements(selector).toList().size() != 0;
    }

    public static Collection<Resource> similarFunctionInDrugBank(Model model, String drugId) {
        if (!isInBio2Rdf(model, drugId)) {
            throw new RuntimeException(String.format("Drug %s not in Bio2Rdf", hifmDataPrefix + drugId));
        }

        System.out.printf("Similar drugs to %s, also in Bio2Rdf\n", hifmDataPrefix + drugId);
        SimpleSelector selector = new SimpleSelector(
                model.getResource(hifmDataPrefix + drugId),
                model.getProperty(hifmOntPrefix + "similarTo"),
                (RDFNode) null);
        Set<Resource> similarResources = model.listStatements(selector).toList()
                .stream().filter(statement -> statement.getObject().isResource())
                .map(statement -> statement.getObject().asResource())
                .filter(resource -> Hifm.isInBio2Rdf(model, resource
                        .getURI()
                        .replaceAll(hifmDataPrefix, "")))
                .collect(Collectors.toSet());

        return similarResources;
    }

    public static void printSimilarFunctionInDrugBank(Model model, String drugId) {
        Collection<Resource> similarResources = similarFunctionInDrugBank(model, drugId);
        similarResources.forEach(resource -> System.out.printf("\t%s\n", resource.getURI()));
    }

    public static Collection<Model> modelsForSimilarDrugsInDrugBank(Model model, String drugId) {
        Collection<Resource> similarResources = similarFunctionInDrugBank(model, drugId);

        return similarResources.stream().map(resource -> ModelFetcher.withJena(
                lodFinkiPrefix + removePrefix(resource.getURI(), hifmDataPrefix),
                "TURTLE"
        )).collect(Collectors.toSet());
    }

}

public class LocalLinkedDataTest {
    public static void main(String[] args) throws IOException {
        Model model = Utils.readModel("data/hifm-dataset-bio2rdf.ttl", "TURTLE");
        Hifm.init(model);

        Hifm.printSimilarFunctionInDrugBank(model, "975397");
        Hifm.modelsForSimilarDrugsInDrugBank(model, "975397");
    }
}
