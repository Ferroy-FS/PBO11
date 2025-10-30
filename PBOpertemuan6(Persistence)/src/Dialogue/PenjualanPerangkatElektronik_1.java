/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dialogue;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author LEGION
 */
@Entity
@Table(name = "penjualan_perangkat_elektronik")
@NamedQueries({
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findAll", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByNomorSeri", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.nomorSeri = :nomorSeri"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByJenisPerangkat", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.jenisPerangkat = :jenisPerangkat"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByMerekPerangkat", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.merekPerangkat = :merekPerangkat"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByNamaPerangkat", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.namaPerangkat = :namaPerangkat"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByModelPerangkat", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.modelPerangkat = :modelPerangkat"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByWarna", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.warna = :warna"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByTahunRilis", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.tahunRilis = :tahunRilis"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByHarga", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.harga = :harga"),
    @NamedQuery(name = "PenjualanPerangkatElektronik_1.findByStok", query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE p.stok = :stok")})
public class PenjualanPerangkatElektronik_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nomor_seri")
    private String nomorSeri;
    @Basic(optional = false)
    @Column(name = "jenis_perangkat")
    private String jenisPerangkat;
    @Basic(optional = false)
    @Column(name = "merek_perangkat")
    private String merekPerangkat;
    @Basic(optional = false)
    @Column(name = "nama_perangkat")
    private String namaPerangkat;
    @Column(name = "model_perangkat")
    private String modelPerangkat;
    @Column(name = "warna")
    private String warna;
    @Column(name = "tahun_rilis")
    private Integer tahunRilis;
    @Column(name = "harga")
    private BigInteger harga;
    @Column(name = "stok")
    private Integer stok;

    public PenjualanPerangkatElektronik_1() {
    }

    public PenjualanPerangkatElektronik_1(String nomorSeri) {
        this.nomorSeri = nomorSeri;
    }

    public PenjualanPerangkatElektronik_1(String nomorSeri, String jenisPerangkat, String merekPerangkat, String namaPerangkat) {
        this.nomorSeri = nomorSeri;
        this.jenisPerangkat = jenisPerangkat;
        this.merekPerangkat = merekPerangkat;
        this.namaPerangkat = namaPerangkat;
    }

    public String getNomorSeri() {
        return nomorSeri;
    }

    public void setNomorSeri(String nomorSeri) {
        this.nomorSeri = nomorSeri;
    }

    public String getJenisPerangkat() {
        return jenisPerangkat;
    }

    public void setJenisPerangkat(String jenisPerangkat) {
        this.jenisPerangkat = jenisPerangkat;
    }

    public String getMerekPerangkat() {
        return merekPerangkat;
    }

    public void setMerekPerangkat(String merekPerangkat) {
        this.merekPerangkat = merekPerangkat;
    }

    public String getNamaPerangkat() {
        return namaPerangkat;
    }

    public void setNamaPerangkat(String namaPerangkat) {
        this.namaPerangkat = namaPerangkat;
    }

    public String getModelPerangkat() {
        return modelPerangkat;
    }

    public void setModelPerangkat(String modelPerangkat) {
        this.modelPerangkat = modelPerangkat;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public Integer getTahunRilis() {
        return tahunRilis;
    }

    public void setTahunRilis(Integer tahunRilis) {
        this.tahunRilis = tahunRilis;
    }

    public BigInteger getHarga() {
        return harga;
    }

    public void setHarga(BigInteger harga) {
        this.harga = harga;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nomorSeri != null ? nomorSeri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PenjualanPerangkatElektronik_1)) {
            return false;
        }
        PenjualanPerangkatElektronik_1 other = (PenjualanPerangkatElektronik_1) object;
        if ((this.nomorSeri == null && other.nomorSeri != null) || (this.nomorSeri != null && !this.nomorSeri.equals(other.nomorSeri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dialogue.PenjualanPerangkatElektronik_1[ nomorSeri=" + nomorSeri + " ]";
    }

    void setHarga(long l) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
