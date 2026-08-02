package view;

import conection.ConnectionJDBC;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DialogTicket extends JDialog {
    public DialogTicket(Frame parent, int idVenta) {
        super(parent, "Detalle del Ticket", true);
        setSize(350, 450);
        setLocationRelativeTo(parent);
        
        JTextArea areaTicket = new JTextArea();
        areaTicket.setEditable(false);
        areaTicket.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Consulta para reconstruir el ticket físico desde la DB 
        String sql = "SELECT T.*, V.ID_venta,V.Descuento_aplicado FROM TICKET T " +
                     "JOIN VENTA V ON T.ID_ticket = V.TICKET_ID_ticket " +
                     "WHERE V.ID_venta = ?";

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idVenta);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("      TIENDA CCA \n");
                sb.append("---------------------------\n");
                sb.append("Ticket ID:   ").append(rs.getInt("ID_ticket")).append("\n");
                sb.append("Fecha:       ").append(rs.getTimestamp("Fecha_hora")).append("\n");
                sb.append("Caja:        ").append(rs.getInt("CAJA_ID_caja")).append("\n");
                sb.append("---------------------------\n");
                sb.append("Total Venta: ").append(rs.getDouble("Total_venta")).append(" €\n");
                //sb.append("Descuento:   ").append(Math.floor((rs.getDouble("Descuento_aplicado")/10) * (rs.getDouble("Total_venta"))*100)/100).append(" €\n");
                sb.append("A PAGAR:     ").append(rs.getDouble("Total_a_pagar")).append(" €\n");
                sb.append("---------------------------\n");
                sb.append("Metodo:      ").append(rs.getString("Forma_pago")).append("\n");
                sb.append("Pagado:      ").append(rs.getDouble("Total_pagado")).append(" €\n");
                sb.append("Vuelta:      ").append(rs.getDouble("Vuelta_dinero")).append(" €\n");
                sb.append("---------------------------\n");
                sb.append("   ¡Gracias por su compra!");
                areaTicket.setText(sb.toString());
            }
        } catch (SQLException e) {
            areaTicket.setText("Error al recuperar el ticket.");
        }

        add(new JScrollPane(areaTicket), BorderLayout.CENTER);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        add(btnCerrar, BorderLayout.SOUTH);
        setVisible(true);
    }
}