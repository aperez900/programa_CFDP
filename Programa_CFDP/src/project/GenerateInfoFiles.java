package project;
import project.util.FileGenerator;

/**
 * Clase principal para generar archivos de prueba (entrada del proyecto).
 */

public class GenerateInfoFiles {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
            // Generar archivo de productos
            FileGenerator.createProductsFile(10, "products.txt");

            // Generar archivo de vendedores
            FileGenerator.createSalesManInfoFile(5, "salesmen.txt");

            // Generar archivos de ventas (uno por vendedor)
            for (int i = 1; i <= 5; i++) {
                long docNumber = 1000000000L + i;
                FileGenerator.createSalesMenFile(5, "Vendedor" + i, docNumber, "sales_" + docNumber + ".txt");
            }

            System.out.println("✅ Archivos generados exitosamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al generar archivos: " + e.getMessage());
        }
    

	}

}
