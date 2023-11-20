package bstu.akudrenko.models;

import bstu.akudrenko.output.Stringify;
import bstu.akudrenko.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.xml.bindings.BindXMLEntity;
import bstu.akudrenko.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.xml.bindings.BindXMLTag;

@Stringify()
@BindXMLEntity(alias = "Location")
public class PublicLocation extends Model {
    @BindXMLAttribute(alias = "Id")
    public int id;

    @BindXMLTag(alias = "Title")
    public String name;

    @BindXMLTag(alias = "Description")
    public String description;

    @BindXMLTag(alias = "Kind")
    public String type;

    @BindXMLNestedEntity(alias = "Address")
    public Address address;
}
