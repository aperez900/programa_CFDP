package project.util;
import java.io.*;
import java.util.*;

/**
 * Métodos para generar archivos de prueba (productos, vendedores y ventas).
 */
public class FileGenerator {

    private static final String[] FIRST_NAMES = {"Andrés", "María", "Carlos", "Sofía", "Juan"};
    private static final String[] LAST_NAMES = {"Pérez", "Gómez", "Rodríguez", "Martínez", "López"};
    private static final String[] PRODUCT_NAMES = {"Computador", "Celular", "Impresora", "Televisor", "Tablet"};

    /**
     * Genera un archivo de productos con información pseudoaleatoria.
     */
    public static void createProductsFile(int productsCount, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            for (int i = 1; i <= productsCount; i++) {
                String id = "P" + String.format("%03d", i);
                String name = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
                double price = 50000 + (random.nextInt(50) * 10000); // precios entre 50k y 550k
                writer.write(id + ";" + name + ";" + price);
                writer.newLine();
            }
        }
    }

    /**
     * Genera un archivo de vendedores con información pseudoaleatoria.
     */
    public static void createSalesManInfoFile(int salesmanCount, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            for (int i = 1; i <= salesmanCount; i++) {
                String docType = "CC";
                long docNumber = 1000000000L + random.nextInt(900000000);
                String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                writer.write(docType + ";" + docNumber + ";" + firstName + ";" + lastName);
                writer.newLine();
            }
        }
    }

    /**
     * Genera un archivo de ventas pseudoaleatorias para un vendedor.
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            // Primera línea: identificación del vendedor
            writer.write("CC;" + id);
            writer.newLine();

            // Segunda línea: productos y cantidades
            StringBuilder salesLine = new StringBuilder();
            for (int i = 1; i <= randomSalesCount; i++) {
                String productId = "P" + String.format("%03d", random.nextInt(10) + 1); // P001 a P010
                int quantity = random.nextInt(5) + 1;
                salesLine.append(productId).append(";").append(quantity).append(";");
            }
            writer.write(salesLine.toString());
        }
    }
}