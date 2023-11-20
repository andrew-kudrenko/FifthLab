package bstu.akudrenko.models;

import bstu.akudrenko.xml.bindings.BindXMLEntity;
import bstu.akudrenko.xml.bindings.BindXMLTag;

@BindXMLEntity(alias = "Population")
public class CityPopulation {
    @BindXMLTag(alias = "Size")
    public int size;

    @BindXMLTag(alias = "CitizenName")
    public String citizenName;
}
