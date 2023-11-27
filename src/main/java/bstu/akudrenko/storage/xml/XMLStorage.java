package bstu.akudrenko.storage.xml;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.storage.DocumentStorage;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class XMLStorage<M extends Model> extends DocumentStorage<M> {
    private final File source;
    private final XMLReader reader;
    private final XMLWriter writer;
    private final Class<M> cls;
    private final Field idField;

    public XMLStorage(Class cls, String path, String rootElementName) {
        this.cls = cls;
        try {
            idField = cls.getDeclaredField("id");
        } catch (Exception e) {
            throw new RuntimeException();
        }
        source = new File(path);
        reader = new XMLReader(source);
        writer = new XMLWriter(source, rootElementName);
    }

    public List<M> getAll() {
        return reader.getAll(this.cls);
    }

    @Override
    public Optional<M> getById(int id) {
        return reader.find(this.cls, m -> {
            try {
                return idField.get(m).equals(id);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<M> where(Predicate<M> predicate) {
        return reader.getAll(cls).stream().filter(predicate).toList();
    }

    @Override
    public void remove(int id) {
        save(v -> v.stream().filter(m -> {
            try {
                return !idField.get(m).equals(id);
            } catch (IllegalAccessException e) {
                return false;
            }
        }).toList());
    }

    @Override
    public void update(M model) {
        save(v -> v.stream().map(m -> {
            try {
                return idField.get(m).equals(idField.get(model)) ? model : m;
            } catch (Exception e) {
                return m;
            }
        }).toList());
    }


    @Override
    public void add(M model) {
        getAll().stream().max((a, b) -> {
            try {
                return (int)idField.get(a) - (int)idField.get(b);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).ifPresent(entity -> {
            save(v -> {
                try {
                    var id = (int)idField.get(entity);
                    idField.set(model, id + 1);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                v.add(model);
                return v;
            });
        });
    }

    public void save(Function<List<M>, List<M>> handler) {
        var processed = handler.apply(reader.getAll(this.cls));
        writer.write(processed);
    }
}
