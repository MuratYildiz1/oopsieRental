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

    private static final String VEHICLE_FILE = "data/vehicles.txt";

    public static void saveVehicle(Vehicle v) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(VEHICLE_FILE, true))) {
            out.println(v.getPlate() + "," + v.getBrand() + "," + v.dailyRate);
        }
    }
}
