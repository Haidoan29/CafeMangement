/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.cafemangement;

import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import java.time.LocalDate;
import javafx.beans.property.*;

/**
 *
 * @author Admin
 */
public class Users {

    private StringProperty id; // Mã NV
    private StringProperty name; // Tên NV
    private StringProperty phone; // SĐT
    private StringProperty Address; // Địa chỉ
    private ObjectProperty<LocalDate> Date_birth; // Ngày sinh
    private StringProperty type; // Chức vụ
    private StringProperty email; // Email

    // Constructor, getters, and setters for the properties
    public Users(String id, String name, String phone, String Address, LocalDate Date_birth, String type, String email) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.Address = new SimpleStringProperty(Address);
        this.Date_birth = new SimpleObjectProperty<>(Date_birth);
        this.type = new SimpleStringProperty(type);
        this.email = new SimpleStringProperty(email);
    }

    // Getter and Setter methods for id
    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }
    // Getter and Setter methods for username

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // Getter and Setter methods for phone
    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    // Getter and Setter methods for Address
    public String getAddress() {
        return Address.get();
    }

    public StringProperty AddressProperty() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address.set(Address);
    }

    // Getter and Setter methods for Address
    public LocalDate getDateBirth() {
        return Date_birth.get();
    }

    public ObjectProperty<LocalDate> Date_birthProperty() {
        return Date_birth;
    }

    public void setDateOfBirth(LocalDate Date_birth) {
        this.Date_birth.set(Date_birth);
    }

    // Getter and Setter methods for type
    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    // Getter and Setter methods for email
    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
