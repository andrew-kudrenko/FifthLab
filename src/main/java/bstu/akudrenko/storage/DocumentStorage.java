package bstu.akudrenko.storage;

import bstu.akudrenko.models.Model;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class DocumentStorage<M extends Model> {
    public abstract Optional<M> getById(int id);
    public abstract List<M> getAll();

    public abstract List<M> where(Predicate<M> predicate);

    public abstract void removeById(int id);
    public abstract void update(int id, Object updates);
    public abstract void add(M model);
}
