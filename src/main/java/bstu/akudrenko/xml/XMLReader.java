package bstu.akudrenko.xml;

import bstu.akudrenko.xml.parsers.SAXEventHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class XMLReader {
    private File source;
    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final SAXParser parser;

    public XMLReader(File source) {
        this.source = source;

        try {
            parser = factory.newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException("Creating XML Reader failed");
        }
    }

    public <M> List<M> getAll(Class<M> cls) {
        var entities = new ArrayList<M>();

        var handler = new SAXEventHandler<>(cls);
        handler.setOnEntityParsed(entities::add);

        try {
            parser.parse(source, handler);
        } finally {
            return entities;
        }
    }

    public <M> Optional<M> find(Class<M> cls, Predicate<M> predicate) {
        AtomicReference<Optional<M>> found = new AtomicReference<>(Optional.empty());

        try {
            var handler = new SAXEventHandler<>(cls);
            handler.setOnEntityParsed(v -> {
                if (predicate.test(v)) {
                    found.set(Optional.of(v));
                    handler.stop();
                }
            });

            parser.parse(source, handler);
        } finally {
            return found.get();
        }
    }
}
