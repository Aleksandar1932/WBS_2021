package mk.ukim.finki.wbs.lab4.task1;

import mk.ukim.finki.wbs.lab1.Utils;
import org.apache.jena.rdf.model.*;

public class DBPediaLinkedDataTest {
    public static Resource getLinkedResourceObject(StmtIterator iterator) {
        RDFNode linkedObject = iterator.toList()
                .stream()
                .filter(obj -> !obj.getObject().toString().contains("Skopje") && obj.getObject().isResource())
                .skip(13)
                .findFirst()
                .orElseThrow()
                .getObject();

        if (!linkedObject.isResource()) {
            throw new RuntimeException("Linked Object is not a resource");
        }

        return linkedObject.asResource();
    }

    public static void main(String[] args) {
        // Genesis
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("dbo", "https://dbpedia.org/ontology/");
        model.read("https://dbpedia.org/data/Skopje", "ttl"); // TODO: Implement caching
        Selector selector = new SimpleSelector(null, model.getProperty(model.getNsPrefixURI("dbo") + "wikiPageWikiLink"), (Object) null);
        StmtIterator iterator = model.listStatements(selector);

        // First Hop
        Resource linkedObject = getLinkedResourceObject(iterator);
        Model linkedObjectModel = ModelFactory.createDefaultModel();
        linkedObjectModel.read(linkedObject.getURI(), "ttl");
        Selector firstHopSelector = new SimpleSelector(
                linkedObject,
                model.getProperty(model.getNsPrefixURI("dbo") + "wikiPageWikiLink"),
                (RDFNode) null);
        StmtIterator firstHopIterator = linkedObjectModel.listStatements(firstHopSelector);

        // Second Hop
        Resource secondHopObject = getLinkedResourceObject(firstHopIterator);
        Model secondHopModel = ModelFactory.createDefaultModel();
        secondHopModel.read(secondHopObject.getURI(), "ttl");
        Selector secondHopSelector = new SimpleSelector(
                secondHopObject,
                null,
                (RDFNode) null
        );
        Utils.iterateAndPrintStatementIterator(secondHopModel.listStatements(secondHopSelector));
    }
}
