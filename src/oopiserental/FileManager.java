/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

import java.io.*;
import java.util.*;

/**
 *
 * @author murat
 */
public class FileManager {

    private static final String VEHICLE_FILE = "vehicles.txt";
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String RESERVATION_FILE = "reservations.txt";

    public static void saveVehicles(ArrayList<Vehicle> vehicles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLE_FILE))) {
            for (Vehicle v : vehicles) {
                String type = v.getClass().getSimpleName();
                writer.println(type + "," + v.getPlate() + "," + v.getBrand() + "," + v.dailyRate);
            }
        } catch (IOException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static void saveCustomer(Customer c) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CUSTOMER_FILE, true))) {
            out.println(c.getId() + "," + c.getName() + "," + c.getSurname() + "," + c.getLoyaltyTier());
        } catch (IOException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static void saveReservation(Reservation res) {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESERVATION_FILE, true))) {
            out.println(res.toString());
        } catch (IOException e) {
            System.err.println("Rezervasyon kaydı başarısız: " + e.getMessage());
        }
    }

    public static ArrayList<Customer> loadCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Customer c = new Customer(data[0], data[1], data[2]);
                c.setLoyaltyTier(data[3]);
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println("Henüz kayıtlı müşteri yok veya dosya okunamadı.");
        }
        return list;
    }

    public static void saveInvoice(Invoice invoice) {
        String fileName = "invoices.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.println(invoice.getFormattedInvoice());
            out.println("=========================================\n");
            System.out.println("Fatura başarıyla kaydedildi: " + fileName);
        } catch (IOException e) {
            System.err.println("Fatura yazma hatası: " + e.getMessage());
        }
    }
}
