package bstu.akudrenko.models;

import bstu.akudrenko.output.Stringify;
import bstu.akudrenko.storage.sql.BindSqlColumn;
import bstu.akudrenko.storage.sql.BindSqlTableName;
import bstu.akudrenko.storage.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.storage.xml.bindings.BindXMLEntity;
import bstu.akudrenko.storage.xml.bindings.BindXMLTag;

@Stringify()
@BindXMLEntity(alias = "Location")
@BindSqlTableName(alias = "locations")
public class PublicLocation extends Model {
    @BindXMLAttribute(alias = "Id")
    @BindSqlColumn(alias = "id")
    public int id;

    @BindXMLTag(alias = "Title")
    @BindSqlColumn(alias = "title")
    public String name;

    @BindXMLTag(alias = "Description")
    @BindSqlColumn(alias = "description")
    public String description;

    @BindXMLTag(alias = "Kind")
    @BindSqlColumn(alias = "kind")
    public String type;

    @BindXMLTag(alias = "Country")
    @BindSqlColumn(alias = "country")
    public String country;

    @BindXMLTag(alias = "Settlement")
    @BindSqlColumn(alias = "town")
    public String town;
}
