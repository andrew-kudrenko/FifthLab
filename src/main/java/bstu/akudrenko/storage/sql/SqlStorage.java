package bstu.akudrenko.storage.sql;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.storage.DocumentStorage;
import bstu.akudrenko.utils.Triple;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SqlStorage<M extends Model> extends DocumentStorage<M> {
    private String tableName;
    private Connection connection;
    private final Triple<List<String>, Field[], String> columns;
    private final Class<M> cls;
    private final Field idField;

    public SqlStorage(Class<M> cls) {
        this.cls = cls;

        var fields = cls.getDeclaredFields();
        var aliases = Arrays.stream(fields).map(SqlQueries::getColumnName).toList();
        columns = new Triple<>(aliases, fields, String.join(", ", aliases));
        tableName = getTableName();

        try {
            idField = cls.getDeclaredField("id");
            connection = createConnection();
            ensureCreated();
        } catch (Exception e) {
            throw new RuntimeException("Connection failed. " + e);
        }
    }

    private void ensureCreated() throws SQLException {
        var query = "create table if not exists %s (%s)".formatted(
            tableName,
            String.join(", ", Arrays.stream(columns.getSecond()).map(SqlQueries::getColumnDeclaration).toList())
        );
        System.out.println(columns + "\n\n" + query);
        var statement = connection.prepareStatement(query);
        statement.execute();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/fifth_lab", "root", "root");
    }

    private String getTableName() {
        var binding = cls.getDeclaredAnnotation(BindSqlTableName.class);
        return binding != null ? binding.alias() : "";
    }

    @Override
    public Optional<M> getById(int id) {
        try {
            var result = connection
                .prepareStatement("select * from %s where id = %d".formatted(tableName, id))
                .executeQuery();

            if (result.next()) {
                var model = (M) cls.getDeclaredConstructors()[0].newInstance();

                for (var field : cls.getDeclaredFields()) {
                    var value = SqlTypes.get(result, field);
                    field.set(model, value);
                }

                return Optional.of(model);
            }

            return Optional.empty();
        } catch (Exception e) {
            System.out.println(e);
            return Optional.empty();
        }
    }

    @Override
    public List<M> getAll() {
        var models = new ArrayList<M>();

        try {
            var result = connection.prepareStatement("select * from %s".formatted(tableName)).executeQuery();

            while (result.next()) {
                var model = (M) cls.getDeclaredConstructors()[0].newInstance();

                for (var field : cls.getDeclaredFields()) {
                    var value = SqlTypes.get(result, field);
                    field.set(model, value);
                }

                models.add(model);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            return models;
        }
    }

    @Override
    public List<M> where(Predicate<M> predicate) {
        return getAll().stream().filter(m -> predicate.test(m)).toList();
    }

    @Override
    public void remove(int id) {
        var query = "delete from %s where id = %d".formatted(tableName, id);

        try {
            connection.prepareStatement(query).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(M model) {
        try {
            var id = (int) idField.get(model);
            var query = createUpdateQuery(id, model);

            connection.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createUpdateQuery(int id, M model) {
        var values = String.join(", ",
            Arrays.stream(columns.getSecond())
                .map(v -> {
                    try {
                        return "%s = %s".formatted(SqlQueries.getColumnName(v), SqlQueries.escapeValue(v.get(model)));
                    } catch (IllegalAccessException e) {
                        return "";
                    }
                })
                .toList()
        );

        return "update %s set %s where id = %d".formatted(
            tableName,
            values,
            id
        );
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
            try {
                var id = (int)idField.get(entity);
                idField.set(model, id + 1);
                connection
                    .prepareStatement(createAddQuery(model))
                    .execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String createAddQuery(M model) {
        var values = String.join(", ", Arrays.stream(columns.getSecond()).map(f -> {
            try {
                return SqlQueries.escapeValue(f.get(model));
            } catch (Exception e) {
                return "null";
            }
        }).toList());

        var query =  "insert into %s (%s) values (%s)".formatted(tableName, columns.getThird(), values);
        System.out.println(query);

        return query;
    }
}
