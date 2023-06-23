package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Company extends User {
    String name;
    String location;
    String field;
    String contact;
    String kvk_num;
    byte[] logo;
    
    public Company() {

    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public void setKvk_num(String kvk_number) {
        this.kvk_num = kvk_number;
    }
    public String getKvk_num() {
        return kvk_num;
    }
    public byte[] getLogo() {
        return logo;
    }
    
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
