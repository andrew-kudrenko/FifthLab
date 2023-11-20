package bstu.akudrenko.storage.sql;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.storage.DocumentStorage;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MySqlStorage<M extends Model> extends DocumentStorage<M> {
    @Override
    public Optional<M> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<M> getAll() {
        return null;
    }

    @Override
    public List<M> where(Predicate<M> predicate) {
        return null;
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
