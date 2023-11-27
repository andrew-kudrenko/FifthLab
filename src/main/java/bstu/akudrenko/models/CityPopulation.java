package bstu.akudrenko.models;

import bstu.akudrenko.storage.xml.bindings.BindXMLEntity;
import bstu.akudrenko.storage.xml.bindings.BindXMLTag;

@BindXMLEntity(alias = "Population")
public class CityPopulation {
    @BindXMLTag(alias = "Size")
    public int size;

    @BindXMLTag(alias = "CitizenName")
    public String citizenName;
}
