package mk.ukim.finki.wbs.lab1.task1;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.VCARD;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static mk.ukim.finki.wbs.lab1.Utils.iterateAndPrintStatementIterator;

class PersonalRDF {
    public static Model initializeModel(){
        String aleksandarURI = "https://www.linkedin.com/in/ivanovskia/";
        Model model = ModelFactory.createDefaultModel();

        Resource faculty = model.createResource("https://finki.ukim.mk/")
                .addProperty(VCARD.N, "Faculty of Computer Science and Engineering");

        Resource aleksandar = model.createResource(aleksandarURI)
                .addProperty(VCARD.FN, "Aleksandar Ivanovski")
                .addProperty(VCARD.Given, "Aleksandar")
                .addProperty(VCARD.Family, "Ivanovski")
                .addProperty(VCARD.NICKNAME, "Ace")
                .addProperty(VCARD.NICKNAME, "Aleks")
                .addProperty(VCARD.ORG, faculty)
                .addProperty(VCARD.EMAIL, "aleksandar.ivanovski.1@students.finki.ukim.mk")
                .addProperty(VCARD.ROLE, "Student")
                .addProperty(FOAF.gender, "M")
                .addProperty(FOAF.birthday, "23-04-2000");

        return model;
    }

    public static void printModel(Model model, String format){
        if (format == null){
            iterativePrint(model);
        }
        else {
            System.out.printf("Printing with model.write() in %s format%n", format);
            model.write(System.out, format);
        }
    }

    public static void iterativePrint(Model model){
        StmtIterator iterator = model.listStatements();
        iterateAndPrintStatementIterator(iterator);
    }

    public static void writeModel(Model model, String fileName) throws FileNotFoundException {
        model.write(new FileOutputStream(String.format("%s.ttl", fileName)), "TURTLE");
    }
}

public class PersonalRDFTest {
    public static void main(String[] args) throws FileNotFoundException {
        Model model = PersonalRDF.initializeModel();
        PersonalRDF.printModel(model, null);

        PersonalRDF.printModel(model, "RDF/XML");
        PersonalRDF.printModel(model, "RDF/XML-ABBREV");
        PersonalRDF.printModel(model, "N-TRIPLES");
        PersonalRDF.printModel(model, "TURTLE");

        PersonalRDF.writeModel(model, "aleksandarIvanovskiGraph");
    }
}
