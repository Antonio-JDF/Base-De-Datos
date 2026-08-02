package controller;

import conection.ConnectionJDBC;
import java.sql.*;

public class CajaController {

    public double obtenerBalanceCaja(int idCaja) {
        String sql = "SELECT Balance FROM CAJA WHERE ID_caja = ?";
        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCaja);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("Balance");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public void actualizarVentasDia(int idCaja) throws SQLException {
        // Suma el total de todos los tickets del día para esa caja
        String sqlUpdate = "UPDATE CAJA SET Total_ventas_dia = (SELECT SUM(Total_pagado) " +
                           "FROM TICKET WHERE CAJA_ID_caja = ?) WHERE ID_caja = ?";
        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setInt(1, idCaja);
            pstmt.setInt(2, idCaja);
            pstmt.executeUpdate();
        }
    }
}