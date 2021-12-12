package mk.ukim.finki.wbs.lab5;

import mk.ukim.finki.wbs.lab1.Utils;
import org.apache.jena.rdf.model.*;

import java.util.HashSet;
import java.util.Set;

class Foaf {
    static Model readMyFoafProfile() {
        Model myModel = ModelFactory.createDefaultModel();
        myModel.read("https://raw.githubusercontent.com/Aleksandar1932/WBS_2021/lab-05/foaf.ttl", "ttl");
        return myModel;
    }

    static void printMyFriends(Model model) {
        Selector selector = new SimpleSelector(null,
                model.getProperty(model.getNsPrefixURI("foaf") + "knows"),
                (RDFNode) null);
        Utils.iterateAndPrintStatementIterator(model.listStatements(selector));
    }

    static Model loadFriendsModel(Model model) {
        Selector friendsSelector = new SimpleSelector(
                null,
                model.getProperty(model.getNsPrefixURI("foaf") + "knows"),
                (RDFNode) null);

        StmtIterator iterator = model.listStatements(friendsSelector);
        Set<String> foafURLs = new HashSet<>();

        while (iterator.hasNext()) {
            Statement stmt = iterator.nextStatement();
            if (stmt.getObject().isResource() && stmt.getObject().asResource().hasProperty(model.getProperty(model.getNsPrefixURI("rdfs") + "seeAlso"))) {
                foafURLs.add(
                        stmt.getObject().asResource()
                                .getProperty(model.getProperty(model.getNsPrefixURI("rdfs") + "seeAlso"))
                                .getObject()
                                .toString());
            }
        }
        Model unionModel = ModelFactory.createDefaultModel();
        for (String foafURI : foafURLs) {
            unionModel = unionModel.union(ModelFactory.createDefaultModel()
                    .read(foafURI, "ttl"));
        }

        return unionModel;
    }

    static void getPropertyFromJoint(Model model, String prefix, String property) {
        Selector propertySelector = new SimpleSelector(null,
                model.getProperty(model.getNsPrefixURI(prefix) + property),
                (RDFNode) null);

        Utils.iterateAndPrintStatementIterator(model.listStatements(propertySelector));
    }
}

public class FoafTest {
    public static void main(String[] args) {
        Model myModel = Foaf.readMyFoafProfile();
        Foaf.printMyFriends(myModel);
        Model friendsModel = Foaf.loadFriendsModel(myModel);
        Model myAndMyFriendsModel = myModel.union(friendsModel);

        System.out.println("=== Union of my model and my friends models ===");
        Utils.iterateAndPrintStatementIterator(myAndMyFriendsModel.listStatements());

        System.out.println("=== Querying the joint model ===");
        Foaf.getPropertyFromJoint(myAndMyFriendsModel, "foaf", "name");
        Foaf.getPropertyFromJoint(myAndMyFriendsModel, "foaf", "mbox_sha1sum");
        Foaf.getPropertyFromJoint(myAndMyFriendsModel, "foaf", "account");
        Foaf.getPropertyFromJoint(myAndMyFriendsModel, "foaf", "logo");
    }
}
