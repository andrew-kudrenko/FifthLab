package bstu.akudrenko.models;

import bstu.akudrenko.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.xml.bindings.BindXMLEntity;
import bstu.akudrenko.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.xml.bindings.BindXMLTag;

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
