package controller;

import conection.ConnectionJDBC;
import model.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorController {
    
    /**
     * Obtiene todos los proveedores registrados en la base de datos.
     * Basado en la tabla PROVEEDOR.
     */
    public List<Proveedor> obtenerProveedores() throws SQLException {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT ID_proveedor, Nombre, Pais FROM PROVEEDOR ORDER BY ID_proveedor ASC";
        
        try (Connection conn = ConnectionJDBC.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new Proveedor(
                    rs.getInt("ID_proveedor"), 
                    rs.getString("Nombre"),    
                    rs.getString("Pais")       
                ));
            }
        }
        return lista;
    }
    
    /**
     * Obtiene el ID más alto registrado en la tabla PROVEEDOR.
     */
    public int obtenerUltimoId() {
        String sql = "SELECT MAX(ID_proveedor) FROM PROVEEDOR";
        try (Connection conn = ConnectionJDBC.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }
    
    /**
     * Inserta un nuevo proveedor en la base de datos.
     */
    public boolean insertarProveedor(int id, String nombre, String pais) {
        String sql = "INSERT INTO PROVEEDOR (ID_proveedor, Nombre, Pais) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, nombre);
            pstmt.setString(3, pais);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}