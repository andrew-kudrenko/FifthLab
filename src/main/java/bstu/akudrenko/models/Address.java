package bstu.akudrenko.models;

import bstu.akudrenko.output.Stringify;
import bstu.akudrenko.storage.xml.bindings.BindXMLEntity;
import bstu.akudrenko.storage.xml.bindings.BindXMLTag;

@Stringify()
@BindXMLEntity(alias = "Address")
public class Address extends Model {
    @BindXMLTag(alias = "Country")
    public String country;

    @BindXMLTag(alias = "Settlement")
    public String town;

    @BindXMLTag(alias = "Street")
    public String streetName;

    @BindXMLTag(alias = "Building")
    public String house;
}
