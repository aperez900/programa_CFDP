package project;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class main {

	// Directorio base de datos
	private static final Path DATA_FOLDER = Paths.get("src/");

	// Archivos de entrada
	private static final Path FILE_PRODUCTS = DATA_FOLDER.resolve("products.txt");
	private static final Path FILE_SALESMEN = DATA_FOLDER.resolve("salesmen.txt");

	// Prefijo para archivos de ventas
	private static final String SALES_PREFIX = "sales_"; // solo prefijo de nombre

	// Archivos de salida (se generan en carpeta de ejecución)
	private static final String REPORT_SALESMEN = "reporte_vendedores.csv";
	private static final String REPORT_PRODUCTS = "reporte_productos.csv";

    public static void main(String[] args) {
        try {
        	System.out.println("Directorio actual: " + new File(".").getAbsolutePath());
        	
        	
        	
            // 1. Cargar productos y vendedores
        	Map<String, Producto> productos = cargarProductos(FILE_PRODUCTS.toString());
        	Map<String, String> vendedores = cargarVendedores(FILE_SALESMEN.toString());
            

            // 2. Procesar ventas
            Map<String, Double> ventasPorVendedor = new HashMap<>();
            Map<String, Integer> ventasPorProducto = new HashMap<>();

            Files.list(DATA_FOLDER)
            .filter(path -> path.getFileName().toString().startsWith(SALES_PREFIX))
            .forEach(path -> procesarArchivoVentas(
                path.toFile(), productos, ventasPorVendedor, ventasPorProducto));

            // 3. Generar reporte de vendedores
            List<Map.Entry<String, Double>> vendedoresOrdenados = ventasPorVendedor.entrySet()
                    .stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .collect(Collectors.toList());

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_SALESMEN))) {
                for (Map.Entry<String, Double> entry : vendedoresOrdenados) {
                    String vendedorId = entry.getKey();
                    String nombre = vendedores.getOrDefault(vendedorId, "Desconocido");
                    bw.write(nombre + ";" + entry.getValue());
                    bw.newLine();
                }
            }

            // 4. Generar reporte de productos
            List<Map.Entry<String, Integer>> productosOrdenados = ventasPorProducto.entrySet()
                    .stream()
                    .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                    .collect(Collectors.toList());

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_PRODUCTS))) {
                for (Map.Entry<String, Integer> entry : productosOrdenados) {
                    Producto p = productos.get(entry.getKey());
                    if (p != null) {
                        bw.write(p.nombre + ";" + p.precio + ";" + entry.getValue());
                        bw.newLine();
                    }
                }
            }

            System.out.println("✅ Proceso finalizado exitosamente. Reportes generados.");

        } catch (Exception e) {
            System.err.println("❌ Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------------- Métodos auxiliares ----------------

    private static void procesarArchivoVentas(File file, Map<String, Producto> productos,
                                              Map<String, Double> ventasPorVendedor,
                                              Map<String, Integer> ventasPorProducto) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String vendedorHeader = br.readLine(); // Ej: CC;1000000001
            if (vendedorHeader == null) return;

            String[] vendedorParts = vendedorHeader.split(";");
            if (vendedorParts.length < 2) return;

            String vendedorId = vendedorParts[0] + vendedorParts[1];
            double totalVendedor = 0.0;

            String linea = br.readLine(); // Segunda línea con productos
            if (linea != null) {
                String[] partes = linea.split(";");
                for (int i = 0; i < partes.length - 1; i += 2) {
                    String idProducto = partes[i];
                    int cantidad = Integer.parseInt(partes[i + 1]);

                    Producto p = productos.get(idProducto);
                    if (p != null) {
                        totalVendedor += p.precio * cantidad;

                        // acumular ventas por producto
                        ventasPorProducto.put(idProducto,
                                ventasPorProducto.getOrDefault(idProducto, 0) + cantidad);
                    }
                }
            }

            ventasPorVendedor.put(vendedorId,
                    ventasPorVendedor.getOrDefault(vendedorId, 0.0) + totalVendedor);

        } catch (Exception e) {
            System.err.println("⚠️ Error procesando archivo " + file.getName());
        }
    }

    private static Map<String, String> cargarVendedores(String filePath) throws IOException {
        Map<String, String> vendedores = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    String id = partes[0] + partes[1]; // CC+NumDoc
                    String nombre = partes[2] + " " + partes[3];
                    vendedores.put(id, nombre);
                }
            }
        }
        return vendedores;
    }

    private static Map<String, Producto> cargarProductos(String filePath) throws IOException {
        Map<String, Producto> productos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String id = partes[0];
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    productos.put(id, new Producto(id, nombre, precio));
                }
            }
        }
        return productos;
    }

    // Clase auxiliar Producto
    static class Producto {
        String id;
        String nombre;
        double precio;

        Producto(String id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }
    }
}

