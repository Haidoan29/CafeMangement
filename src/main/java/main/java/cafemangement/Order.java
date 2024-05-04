/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.cafemangement;

/**
 *
 * @author Admin
 */
public class Order {
    private String maHoaDon;
    private String tenKH;
    private double thanhTien;
    private double tienTra;
    private double tienThoi;
    private String ngayGiaoDich;
    private String nhanVienTao;

    // Constructors, getters, and setters

    public Order(String maHoaDon, String tenKH, double thanhTien, double tienTra, double tienThoi, String ngayGiaoDich, String nhanVienTao) {
        this.maHoaDon = maHoaDon;
        this.tenKH = tenKH;
        this.thanhTien = thanhTien;
        this.tienTra = tienTra;
        this.tienThoi = tienThoi;
        this.ngayGiaoDich = ngayGiaoDich;
        this.nhanVienTao = nhanVienTao;
    }

    // Add getters and setters for each field

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public double getTienTra() {
        return tienTra;
    }

    public void setTienTra(double tienTra) {
        this.tienTra = tienTra;
    }

    public double getTienThoi() {
        return tienThoi;
    }

    public void setTienThoi(double tienThoi) {
        this.tienThoi = tienThoi;
    }

    public String getNgayGiaoDich() {
        return ngayGiaoDich;
    }

    public void setNgayGiaoDich(String ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }

    public String getNhanVienTao() {
        return nhanVienTao;
    }

    public void setNhanVienTao(String nhanVienTao) {
        this.nhanVienTao = nhanVienTao;
    }
}

