package bstu.akudrenko.storage;

import bstu.akudrenko.utils.ClassReflectionUtils;
import bstu.akudrenko.xml.parsers.FromStringTypes;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModelSearch {
    private DocumentStorage storage;

    public ModelSearch(DocumentStorage storage) {
        this.storage = storage;
    }

    public <M> List<M> search(HashMap<Field, ?> values) {
        return storage.where(m -> {
            AtomicBoolean isAllMatch = new AtomicBoolean(true);

            values.forEach((f, v) -> {
                try {
                    if (!f.get(m).equals(v)) {
                        isAllMatch.set(false);
                    }
                } catch (Exception e) {
                    isAllMatch.set(false);
                }
            });

            return isAllMatch.get();
        });
    }

    private <M> boolean isModelMatch(M model, List<AbstractMap.SimpleEntry<String, String>> values) {
        return values.stream().allMatch(e -> isFieldValueMatch(model, e.getKey(), e.getValue()));
    }

    private <M> boolean isFieldValueMatch(M model, String fieldName, String rawValue) {
        var cls = model.getClass();

        if (ClassReflectionUtils.isPrimitive(cls)) {
            try {
                var field = cls.getField(fieldName);

                if (field != null) {
                    var parsedValue = FromStringTypes.resolve(field.getType(), rawValue);
                    return field.get(model).equals(parsedValue);
                }
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}
