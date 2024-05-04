/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.cafemangement;

/**
 *
 * @author Admin
 */
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Objects;

import javafx.beans.property.*;

public class product {

    private final StringProperty maMon = new SimpleStringProperty();
    private final StringProperty tenMon = new SimpleStringProperty();
    private final StringProperty trangThaiBan = new SimpleStringProperty("Đang bán");
    private final DoubleProperty giaBan = new SimpleDoubleProperty();
    private final IntegerProperty soLuong = new SimpleIntegerProperty();
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final StringProperty moTa = new SimpleStringProperty();

    public product(String maMon, String tenMon, double giaBan, String imageUrl) {
        setMaMon(maMon);
        setTenMon(tenMon);
        setGiaBan(giaBan);
        setImageUrl(imageUrl);
        

    }
    public product(String maMon, String tenMon, double giaBan, String imageUrl , String moTa) {
        setMaMon(maMon);
        setTenMon(tenMon);
        setGiaBan(giaBan);
        setImageUrl(imageUrl);
        setMoTa(moTa);
        

    }

    public product(String maMon, String tenMon, double giaBan, int soLuong) {
        setMaMon(maMon);
        setTenMon(tenMon);
        setSoLuong(soLuong);
        setGiaBan(giaBan);
    }

    // Getter and Setter methods for each property
    public String getMoTa() {
        return moTa.get();
    }
 public void setMoTa(String value) {
        moTa.set(value);
    }
    
    public String getTrangThaiBan() {
        return trangThaiBan.get();
    }

    public void setTrangThaiBan(String value) {
        trangThaiBan.set(value);
    }

    public StringProperty trangThaiBanProperty() {
        return trangThaiBan;
    }

    public String getMaMon() {
        return maMon.get();
    }

    public void setMaMon(String value) {
        maMon.set(value);
    }

    public StringProperty maMonProperty() {
        return maMon;
    }

    public String getTenMon() {
        return tenMon.get();
    }

    public void setTenMon(String value) {
        tenMon.set(value);
    }

    public StringProperty tenMonProperty() {
        return tenMon;
    }

    public double getGiaBan() {
        return giaBan.get();
    }

    public void setGiaBan(double value) {
        giaBan.set(value);
    }

    public DoubleProperty giaBanProperty() {
        return giaBan;
    }

    public int getSoLuong() {
        return soLuong.get();
    }

    public void setSoLuong(int value) {
        soLuong.set(value);
    }

    public IntegerProperty soLuongProperty() {
        return soLuong;
    }

    public String getimageUrl() {
        return imageUrl.get();
    }

    public void setImageUrl(String value) {
        imageUrl.set(value);
    }

    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        product product = (product) o;
        return Objects.equals(maMon.get(), product.maMon.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(maMon.get());
    }
    

}
