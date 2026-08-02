package controller;

import conection.ConnectionJDBC;
import java.sql.*;
import java.io.*;

public class ClienteController {

    public byte[] obtenerFotoSocio(int idTarjeta) throws SQLException {
        Connection conn = ConnectionJDBC.getConnection();
        String sql = "SELECT Foto FROM TARJETA_DE_SOCIO WHERE ID_tarjeta = ?";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idTarjeta);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            Blob b = rs.getBlob(1);
            if (b != null) {
                return b.getBytes(1, (int) b.length());
            }
        }
        return null;
    }
    
    public int obtenerUltimoIdCliente() {
        String sql = "SELECT MAX(ID_tarjeta) FROM TARJETA_DE_SOCIO";
        try (Connection conn = ConnectionJDBC.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Si la tabla está vacía
    }
    
    public boolean validarCliente(int id) {
        String sql = "SELECT COUNT(*) FROM CLIENTE WHERE ID_cliente = ?";
        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    public void actualizarFotoSocio(int idCliente, String rutaFichero) throws SQLException, IOException {
        Connection conn = ConnectionJDBC.getConnection();
        conn.setAutoCommit(false);
        
        int idTarjeta = -1;
        String sqlId = "SELECT TARJETA_DE_SOCIO_ID_tarjeta FROM CLIENTE WHERE ID_cliente = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlId)) {
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) idTarjeta = rs.getInt(1);
        }
        
        if (idTarjeta != -1) {
            String sqlInit = "UPDATE TARJETA_DE_SOCIO SET Foto = EMPTY_BLOB() WHERE ID_tarjeta = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInit)) {
                pstmt.setInt(1, idTarjeta);
                pstmt.executeUpdate();
            }
            
            String sqlSelect = "SELECT Foto FROM TARJETA_DE_SOCIO WHERE ID_tarjeta = ? FOR UPDATE";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
                pstmt.setInt(1, idTarjeta);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    Blob blob = rs.getBlob(1);
                    File fin = new File(rutaFichero);
                    try (FileInputStream fis = new FileInputStream(fin)) {
                        byte[] bytes = new byte[(int) fin.length()];
                        fis.read(bytes);
                        blob.setBytes(1, bytes);
                    }
                }
            }
        }
        conn.commit();
    }
    
}
