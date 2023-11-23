package bstu.akudrenko;

import bstu.akudrenko.interact.AppMenu;
import bstu.akudrenko.models.PublicLocation;
import bstu.akudrenko.xml.XMLReader;
import bstu.akudrenko.xml.XMLWriter;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        var writer = new XMLWriter(
            new File("src/main/resources/bstu.akudrenko/handled_public_locations.xml"),
        "LocationGroup"
        );

        var reader = new XMLReader(new File("src/main/resources/bstu.akudrenko/public_locations.xml"));

        var models = reader.getAll(PublicLocation.class);
        System.out.println(models.size());
        writer.write(models);
    }
}