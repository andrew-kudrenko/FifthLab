package bstu.akudrenko.storage;

import bstu.akudrenko.utils.ClassReflectionUtils;
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
                    var deepField = ClassReflectionUtils.deepGetField(m, entry.getFirst());

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
}
