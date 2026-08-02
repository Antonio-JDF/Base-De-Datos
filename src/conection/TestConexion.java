package conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[] args) {
        // Datos exactos de tu servidor Atenea
        String url = "jdbc:oracle:thin:@diana.lcc.uma.es:1521:atenea";
        String user = "UBD2770"; // Sustituye por el tuyo
        String pass = "UniUMABbMm1504"; // Sustituye por la tuya

        System.out.println("--- Iniciando prueba de conexión ---");
        
        try {
            // 1. Intentar cargar el driver manualmente para ver si el JAR funciona
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("1. Driver cargado correctamente.");

            // 2. Intentar conectar
            Connection conn = DriverManager.getConnection(url, user, pass);
            
            if (conn != null) {
                System.out.println("2. ¡CONEXIÓN EXITOSA! Has conectado con Oracle.");
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encuentra el JAR de Oracle (ojdbc).");
        } catch (SQLException e) {
            System.err.println("ERROR DE SQL: " + e.getMessage());
            System.err.println("Código de error: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}