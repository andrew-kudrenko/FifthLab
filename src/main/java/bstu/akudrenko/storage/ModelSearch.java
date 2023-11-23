package bstu.akudrenko.storage;

import bstu.akudrenko.utils.Tuple;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModelSearch {
    private DocumentStorage storage;

    public ModelSearch(DocumentStorage storage) {
        this.storage = storage;
    }

    public <M> List<M> search(HashMap<Field, Tuple<String, Object>> values) {
        return storage.where(m -> {
            AtomicBoolean isAllMatch = new AtomicBoolean(true);

            values.forEach((f, entry) -> {
                var v = entry.getSecond();

                try {
                    var deepField = deepGetField(m, entry.getFirst());

                    if (deepField != null && !deepField.getSecond().equals(v)) {
                        isAllMatch.set(false);
                    }
                } catch (Exception e) {
                    isAllMatch.set(false);
                }
            });

            return isAllMatch.get();
        });
    }

    private <M> Tuple<Field, Object> deepGetField(M model, String path) {
        var parts = path.split("\\.");

        try {
            Field field = model.getClass().getField(parts[0]);
            Object value = field.get(model);

            for (var i = 1; i < parts.length; i++) {
                field = value.getClass().getField(parts[i]);
                value = field.get(value);
            }

            return Tuple.of(field, value);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
