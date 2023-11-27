package bstu.akudrenko.models;

import bstu.akudrenko.storage.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.storage.xml.bindings.BindXMLEntity;
import bstu.akudrenko.storage.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.storage.xml.bindings.BindXMLTag;

@BindXMLEntity(alias = "City")
public class City {
    @BindXMLAttribute(alias = "Id")
    public int id;

    @BindXMLTag(alias = "Name")
    public String name;

    @BindXMLTag(alias = "Mayor")
    public String mayor;

    @BindXMLTag(alias = "Country")
    public String country;

    @BindXMLNestedEntity(alias = "Population")
    public CityPopulation population;
}
