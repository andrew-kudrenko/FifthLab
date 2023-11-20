package bstu.akudrenko.storage.xml;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.storage.DocumentStorage;
import bstu.akudrenko.xml.parsers.XMLReader;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class XMLStorage<M extends Model> extends DocumentStorage<M> {
    private final File source = new File("src/main/resources/bstu.akudrenko/public_locations.xml");
    private final XMLReader reader = new XMLReader(source);
    private final Class cls;

    public XMLStorage(Class cls) {
        this.cls = cls;
    }

    public List<M> getAll() {
        return reader.getAll(this.cls);
    }

    @Override
    public Optional<M> getById(int id) {
        return reader.<M>find(this.cls, m -> m.id == id);
    }

    @Override
    public List<M> where(Predicate<M> predicate) {
        return reader.getAll(cls).stream().filter(predicate).toList();
    }

    @Override
    public void removeById(int id) {

    }

    @Override
    public void update(int id, Object updates) {

    }

    @Override
    public void add(M model) {

    }
}
