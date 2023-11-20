package bstu.akudrenko.xml;

import bstu.akudrenko.models.PublicLocation;
import bstu.akudrenko.output.ModelPrinter;
import bstu.akudrenko.xml.parsers.XMLReader;

import java.io.File;

public class TestXML {
    private final File source = new File("src/main/resources/bstu.akudrenko/public_locations.xml");
    private final XMLReader reader = new XMLReader(source);

    public void findByKind(String kind) {
        try {
            var value = reader.find(PublicLocation.class, l -> l.type.equals(kind));
            var printer = new ModelPrinter();

            printer.println(value.get());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getAll() {
        try {
            var value = reader.getAll(PublicLocation.class);
            var printer = new ModelPrinter();

            var delimiter = "*-".repeat(20);
            System.out.println(delimiter);
            value.forEach(printer::println);
            System.out.println(delimiter);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
