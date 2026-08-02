package controller;

import conection.ConnectionJDBC;
import java.sql.*;
import model.TarjetaSocio;

public class TarjetaSocioController {

    /**
     * Obtiene la tarjeta de socio a partir del ID del cliente.
     * (CLIENTE tiene FK a TARJETA_DE_SOCIO)
     */
    public TarjetaSocio obtenerTarjetaPorCliente(int idCliente) {

        String sql = """
            SELECT T.ID_tarjeta, T.Puntos_acumulados, T.Nivel,
                   T.Fecha_alta, T.Fecha_caducidad, T.Foto
            FROM TARJETA_DE_SOCIO T
            JOIN CLIENTE C
              ON C.TARJETA_DE_SOCIO_ID_tarjeta = T.ID_tarjeta
             AND C.TARJETA_DE_SOCIO_Nivel = T.Nivel
            WHERE C.ID_cliente = ?
        """;

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                TarjetaSocio t = new TarjetaSocio();
                t.setIdTarjeta(rs.getInt("ID_tarjeta"));
                t.setPuntosAcumulados(rs.getInt("Puntos_acumulados"));
                t.setNivel(rs.getString("Nivel"));
                t.setFechaAlta(rs.getDate("Fecha_alta"));
                t.setFechaCaducidad(rs.getDate("Fecha_caducidad"));
                t.setFoto(rs.getBytes("Foto"));
                return t;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza los puntos acumulados de una tarjeta
     */
    public boolean actualizarPuntos(int idTarjeta, String nivel, int nuevosPuntos) {

        String sql = """
            UPDATE TARJETA_DE_SOCIO
            SET Puntos_acumulados = ?
            WHERE ID_tarjeta = ? AND Nivel = ?
        """;

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nuevosPuntos);
            pstmt.setInt(2, idTarjeta);
            pstmt.setString(3, nivel);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}