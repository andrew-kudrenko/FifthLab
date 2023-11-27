package bstu.akudrenko.storage;

import bstu.akudrenko.models.Model;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class DocumentStorage<M extends Model> {
    public abstract Optional<M> getById(int id);
    public abstract List<M> getAll();

    public abstract List<M> where(Predicate<M> predicate);
    public abstract void remove(int id);
    public abstract void update(M model);
    public abstract void add(M model);

    public abstract void removeAll();
}
