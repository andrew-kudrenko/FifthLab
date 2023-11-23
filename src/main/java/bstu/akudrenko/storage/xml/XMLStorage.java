package bstu.akudrenko.storage.xml;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.storage.DocumentStorage;
import bstu.akudrenko.xml.XMLReader;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class XMLStorage<M extends Model> extends DocumentStorage<M> {
    private final File source;
    private final XMLReader reader;
    private final Class cls;

    public XMLStorage(Class cls, String path) {
        this.cls = cls;
        source = new File(path);
        reader = new XMLReader(source);
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
