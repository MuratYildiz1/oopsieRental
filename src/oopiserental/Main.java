/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

import java.util.ArrayList;

/**
 *
 * @author murat
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== OOPSIE RENTAL SİSTEM TESTİ BAŞLIYOR ===\n");

        try {
            Customer murat = new Customer("1", "Murat", "Yildiz");
            murat.setLoyaltyTier("Gold"); // %20 indirim bekliyoruz [cite: 67]

            Vehicle suv = new SUV("35ABC123", "Dacia Duster", 1000.0);
            Vehicle eco = new Economy("35XYZ789", "Renault Clio", 500.0);

            System.out.println("Müşteri: " + murat);
            System.out.println("SUV Günlük Ücret (Vergili): " + suv.calculateRent(1) + " TL");
            System.out.println("Economy Günlük Ücret: " + eco.calculateRent(1) + " TL\n");

            System.out.println("--- Rezervasyon Yapılıyor ---");
            Reservation res = new Reservation("RES-001", murat, suv, 5);
            System.out.println(res); // toString() ile özet bilgi [cite: 16]

            System.out.println("\n--- Hata Yönetimi Testi ---");
            try {
                res.checkAvailability(); // Araç zaten 'res' constructor'ında rented=true yapıldı
            } catch (RentalException e) {
                System.out.println("Yakalanan Hata: " + e.getMessage());
            }

            System.out.println("\n--- Dosya Sistemi Kayıt Testi ---");
            ArrayList<Vehicle> araclar = new ArrayList<>();
            araclar.add(suv);
            araclar.add(eco);

            FileManager.saveVehicles(araclar);
            FileManager.saveCustomer(murat);
            FileManager.saveReservation(res);

            System.out.println("\n--- Fatura Oluşturma Testi ---");
            Invoice inv = new Invoice("INV-2026-001", res);
            System.out.println(inv.getFormattedInvoice());
            FileManager.saveInvoice(inv);

        } catch (Exception e) {
            System.err.println("Beklenmedik bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
