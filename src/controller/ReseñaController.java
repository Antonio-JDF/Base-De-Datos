package controller;

import conection.ConnectionJDBC;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.Reseña;

public class ReseñaController {

    /**
     * Obtiene todas las reseñas escritas por un cliente específico.
     */
    public DefaultTableModel obtenerReseñasCliente(int idCliente) {
        String[] columnas = {"Producto", "Puntuación", "Comentario", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        String sql = "SELECT P.Nombre, R.Puntuación, R.Comentario, R.Fecha " +
                     "FROM RESEÑA R " +
                     "JOIN PRODUCTO P ON R.PRODUCTO_ID_producto = P.ID_producto " +
                     "AND R.PRODUCTO_TIENDA_ID_tienda = P.TIENDA_ID_tienda " +
                     "WHERE R.CLIENTE_ID_cliente = ? " +
                     "ORDER BY R.Fecha DESC";

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                    rs.getString("Nombre"),
                    rs.getDouble("Puntuación"),
                    rs.getString("Comentario"),
                    rs.getDate("Fecha")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }

    /**
     * Inserta una nueva reseña en la base de datos.
     */
    public boolean insertarReseña(Reseña r) {
        // Obtenemos el siguiente ID disponible para la reseña
        int nuevoId = obtenerSiguienteId();
        
        String sql = "INSERT INTO RESEÑA (ID_reseña, Puntuación, Comentario, Fecha, " +
                     "CLIENTE_ID_cliente, PRODUCTO_ID_producto, PRODUCTO_TIENDA_ID_tienda) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, nuevoId);
            pstmt.setDouble(2, r.getPuntuacion());
            pstmt.setString(3, r.getComentario());
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setInt(5, r.getIdCliente());
            pstmt.setInt(6, r.getIdProducto());
            pstmt.setInt(7, r.getIdTienda());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int obtenerSiguienteId() {
        try (Connection conn = ConnectionJDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(ID_reseña) FROM RESEÑA")) {
            if (rs.next()) return rs.getInt(1) + 1;
        } catch (SQLException e) { e.printStackTrace(); }
        return 1;
    }
}