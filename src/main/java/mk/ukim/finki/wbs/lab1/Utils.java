package mk.ukim.finki.wbs.lab1;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.LiteralImpl;

public class Utils {
    public static Model readModel(String path, String format) {
        Model model = ModelFactory.createDefaultModel();
        model.read(path, format);
        return model;
    }

    public static void iterateAndPrintStatementIterator(StmtIterator iterator) {
        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();

            Resource subject = statement.getSubject();
            Property predicate = statement.getPredicate();
            RDFNode object = statement.getObject();

            System.out.print(subject);
            System.out.print(String.format(" %s ", predicate));

            if (object instanceof Resource) {
                System.out.print(object);
            } else {
                System.out.print(((LiteralImpl) object).getValue());
            }
            System.out.println(" .");
        }
    }
}
