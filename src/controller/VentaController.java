package controller;

import conection.ConnectionJDBC;
import model.Ticket;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.*;
import java.util.ArrayList;
import java.util.List;
import model.ItemCarrito;

public class VentaController {
	
	
	public boolean procesarVentaMultiple(List<ItemCarrito> carrito, int idCliente, String formaPago, boolean esReserva, double pagoPrevio,double dineroEntregado) {
	    Connection conn = null;
	    try {
	        conn = ConnectionJDBC.getConnection();
	        conn.setAutoCommit(false);
	        
	        double totalVenta = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
	        
	        if (esReserva && pagoPrevio > totalVenta) {
	            JOptionPane.showMessageDialog(null, "Error: El pago previo (" + pagoPrevio + "€) no puede superar el total de la venta (" + totalVenta + "€).");
	            return false;
	        }
	        
	        int idTicket = obtenerSiguienteIdTicket(conn);
	        int idCajaAleatorio = obtenerIdCajaAleatorio(conn);
	        int proximoIdVenta = obtenerSiguienteIdVenta(conn);
	        
	        String nombreNivel = obtenerNombreNivel(conn, idCliente);
	        double porcentaje = calcularPorcentajeDescuento(nombreNivel);
	        double importeDescuento = totalVenta * porcentaje;
	        double totalConDescuento = totalVenta - importeDescuento;
	        
	        //double totalAPagar = totalConDescuento;
	        double importePendienteHoy = totalConDescuento - (esReserva ? pagoPrevio : 0);
	        double vuelta = dineroEntregado - importePendienteHoy;
	        double totalAportadoTicket = dineroEntregado + (esReserva ? pagoPrevio : 0);
	        /*if (esReserva) {
	            totalAPagar = totalConDescuento - pagoPrevio;
	            //dineroEntregado=dineroEntregado+pagoPrevio;
	        }
	        
	        double vuelta = dineroEntregado - totalAPagar;*/
	        String sqlTicket = "INSERT INTO TICKET (ID_ticket, Fecha_hora, Total_pagado, Total_venta,Vuelta_dinero , Forma_pago, Total_a_pagar, CAJA_ID_caja) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement psT = conn.prepareStatement(sqlTicket)) {
	            psT.setInt(1, idTicket);
	            psT.setDouble(2, totalAportadoTicket);
	            psT.setDouble(3, totalVenta);
	            psT.setDouble(4, vuelta);
	            psT.setString(5, formaPago);
	            psT.setDouble(6, totalConDescuento);
	            psT.setInt(7, idCajaAleatorio);
	            psT.executeUpdate();
	            actualizarCaja(conn, idCajaAleatorio, totalConDescuento);
	        }
	        
	        for (ItemCarrito item : carrito) {
	            int idVenta = proximoIdVenta;
	            proximoIdVenta = proximoIdVenta + 1;
	            int idTienda = buscarTiendaParaProducto(conn,item.getIdProducto());
	            String sqlVenta = "INSERT INTO VENTA (ID_venta, Cantidad, Precio_total, Descuento_aplicado, VENDEDOR_TRABAJADOR_ID, TICKET_ID_ticket, CLIENTE_ID_cliente) " +
                        "VALUES (?, ?, ?, 0, 1003, ?, ?)";
	            try (PreparedStatement psV = conn.prepareStatement(sqlVenta)) {
	                psV.setInt(1, idVenta);
	                psV.setInt(2, item.getCantidad());
	                psV.setDouble(3, item.getSubtotal());
	                psV.setInt(4, idTicket);
	                psV.setInt(5, idCliente);
	                psV.executeUpdate();
	            }

	            String sqlReservado = "INSERT INTO RESERVADO (VENTA_ID_venta, PRODUCTO_ID_producto, PRODUCTO_TIENDA_ID_tienda) VALUES (?, ?, ?)";
	            try (PreparedStatement psRd = conn.prepareStatement(sqlReservado)) {
	                psRd.setInt(1, idVenta);
	                psRd.setInt(2, item.getIdProducto());
	                psRd.setInt(3, idTienda);
	                psRd.executeUpdate();
	            }

	            // 4. Si es RESERVA, insertar en la tabla RESERVA
	            if (esReserva) {
	                String sqlRes = "INSERT INTO RESERVA (ID_venta, Fecha, Estado, Pago_previo, ID_ticket) VALUES (?, CURRENT_DATE, 'Pendiente', ?, ?)";
	                try (PreparedStatement psR = conn.prepareStatement(sqlRes)) {
	                    psR.setInt(1, idVenta);
	                    psR.setDouble(2, pagoPrevio / carrito.size()); // Distribuimos el pago previo si hay varios items
	                    psR.setInt(3, idTicket);
	                    psR.executeUpdate();
	                }
	            }
	            
	            String sqlUpdateStock = "UPDATE PRODUCTO SET STOCK = STOCK - ? WHERE ID_PRODUCTO = ? AND TIENDA_ID_TIENDA = ?";
	            try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
	                psStock.setInt(1, item.getCantidad());
	                psStock.setInt(2, item.getIdProducto());
	                psStock.setInt(3, idTienda);
	                
	                int filasAfectadas = psStock.executeUpdate();
	                
	                //Verificar si nos quedamos sin stock
	                if (filasAfectadas == 0) {
	                    throw new SQLException("No se pudo actualizar el stock del producto ID: " + item.getIdProducto());
	                }
	            }
	        }
	        
	        double puntosGanados = totalVenta / 10.0;
	        
	        String sqlPuntos = "UPDATE TARJETA_DE_SOCIO SET Puntos_acumulados = Puntos_acumulados + ? WHERE ID_tarjeta = (SELECT TARJETA_DE_SOCIO_ID_tarjeta FROM CLIENTE WHERE ID_cliente = ?)";
	        
	        try (PreparedStatement psPuntos = conn.prepareStatement(sqlPuntos)) {
	            psPuntos.setDouble(1, puntosGanados);
	            psPuntos.setInt(2, idCliente);
	            
	            int filas = psPuntos.executeUpdate();
	            
	            if (filas > 0) {
	                System.out.println("DEBUG: Se han sumado " + puntosGanados + " puntos a la tarjeta del cliente " + idCliente);
	            } else {
	                System.out.println("ERROR: No se pudo actualizar la tarjeta. Compruebe si el cliente existe.");
	            }
	        }
	        
	        double Total_a_pagar = totalConDescuento;
	        int ID_caja = idCajaAleatorio;
	        
	        String sqlBalance =
	        	    "UPDATE CAJA " +
	        	    "SET Balance = Balance + ? " +
	        	    "WHERE ID_caja = ?";

	        	try (PreparedStatement psBalance = conn.prepareStatement(sqlBalance)) {

	        	    psBalance.setDouble(1, Total_a_pagar);
	        	    psBalance.setInt(2, ID_caja);

	        	    int filas = psBalance.executeUpdate();

	        	    if (filas > 0) {
	        	        System.out.println(
	        	            "DEBUG: Balance actualizado en la caja " +
	        	            ID_caja + " (+" + Total_a_pagar + "€)"
	        	        );
	        	    } else {
	        	        System.out.println(
	        	            "ERROR: No se pudo actualizar el balance de la caja " + ID_caja
	        	        );
	        	    }
	        	}
	        	
	        	String sqlVentasDia =
	        		    "UPDATE CAJA " +
	        		    "SET Total_ventas_dia = Total_ventas_dia + ? " +
	        		    "WHERE ID_caja = ?";

	        		try (PreparedStatement psVentasDia = conn.prepareStatement(sqlVentasDia)) {

	        		    psVentasDia.setDouble(1, Total_a_pagar);
	        		    psVentasDia.setInt(2, ID_caja);

	        		    int filas = psVentasDia.executeUpdate();

	        		    if (filas > 0) {
	        		        System.out.println(
	        		            "DEBUG: Total_ventas_dia actualizado en la caja " +
	        		            ID_caja + " (+" + Total_a_pagar + "€)"
	        		        );
	        		    } else {
	        		        System.out.println(
	        		            "ERROR: No se pudo actualizar Total_ventas_dia de la caja " + ID_caja
	        		        );
	        		    }
	        		}



	        conn.commit();
	        return true;
	    } catch (SQLException e) {
	        if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
	        e.printStackTrace();
	        return false;
	    }
	}
	
	private void actualizarCaja(Connection conn, int idCaja, double totalAPagar) throws SQLException {

	    String sql = """
	        UPDATE CAJA
	        SET
	            Balance = Balance + ?,
	            Total_ventas_dia = Total_ventas_dia + ?
	        WHERE ID_caja = ?
	    """;

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setDouble(1, totalAPagar);
	        ps.setDouble(2, totalAPagar);
	        ps.setInt(3, idCaja);
	        ps.executeUpdate();
	    }
	}


    public DefaultTableModel obtenerHistorial(int idCliente) {
        String[] columnas = {"ID Venta", "Fecha/Hora", "Total (€)", "Forma Pago", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        String sql = "SELECT V.ID_venta, T.Fecha_hora, V.Precio_total, T.Forma_pago, R.Estado " +
                     "FROM VENTA V " +
                     "JOIN TICKET T ON V.TICKET_ID_ticket = T.ID_ticket " +
                     "LEFT JOIN RESERVA R ON V.ID_venta = R.ID_venta " +
                     "WHERE V.CLIENTE_ID_cliente = ? " +
                     "ORDER BY T.Fecha_hora DESC";

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("ID_venta");
                fila[1] = rs.getTimestamp("Fecha_hora");
                fila[2] = rs.getDouble("Precio_total");
                fila[3] = rs.getString("Forma_pago");    // 'Efectivo', 'Tarjeta' o 'Financiación' 
                
                String estado = rs.getString("Estado");
                fila[4] = (estado != null) ? "Reserva: " + estado : "Venta Directa";
                
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }
    
    /**
     * Consulta el precio real de un producto en la base de datos.
     */
    public double obtenerPrecioProducto(int idProducto) {

        String sql = "SELECT Precio FROM PRODUCTO WHERE ID_producto = ?";
        try (Connection conn = conection.ConnectionJDBC.getConnection(); //
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Precio");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; 
    }
    
    private int buscarTiendaParaProducto(Connection conn, int idProducto) throws SQLException {
        String sql = "SELECT TIENDA_ID_TIENDA FROM PRODUCTO WHERE ID_PRODUCTO = ? FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("El producto no está en ninguna tienda.");
    }
    
    private int obtenerSiguienteIdTicket(Connection conn) throws SQLException {
        String sql = "SELECT MAX(ID_ticket) FROM TICKET";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1;
    }
    
    private int obtenerSiguienteIdVenta(Connection conn) throws SQLException {
        String sql = "SELECT MAX(ID_venta) FROM VENTA";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // vacía= 1
    }

    private int obtenerIdCajaAleatorio(Connection conn) throws SQLException {
    	List<Integer> idsCaja = new ArrayList<>();
        String sql = "SELECT ID_caja FROM CAJA";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                idsCaja.add(rs.getInt("ID_caja"));
            }
        }
        if (idsCaja.isEmpty()) return 1;
        java.util.Random rand = new java.util.Random();
        return idsCaja.get(rand.nextInt(idsCaja.size()));
    }
    
    private String obtenerNombreNivel(Connection conn, int idCliente) throws SQLException {
        String sql = "SELECT TARJETA_DE_SOCIO_Nivel FROM CLIENTE WHERE ID_cliente = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nivel = rs.getString("TARJETA_DE_SOCIO_Nivel");
                    return (nivel != null) ? nivel : "Básico";
                    /*
                    if (nivel != null) {
                        System.out.println("Cliente ID " + idCliente + " tiene nivel: [" + nivel + "]");
                        return nivel.trim(); // .trim() elimina espacios invisibles
                    } else {
                        System.out.println("El nivel en la BD es NULL para el cliente " + idCliente);
                        return "Básico";
                    }*/
                }
            }
        }catch (SQLException e) {
            System.out.println("ERROR SQL en obtenerNombreNivel: " + e.getMessage());
            throw e; 
        }
        return "Básico"; // Valor por defecto si no se encuentra
    }
    
    private double calcularPorcentajeDescuento(String nivel) {
        if (nivel == null) return 0.0;
        
        switch (nivel.trim().toUpperCase()) {
            case "BRONCE":return 0.10; // 10%
            case "PLATA":return 0.20; // 20%
            case "ORO":return 0.30; // 30%
            case "BÁSICO":return 0.0;
            default:       return 0.0;  // 0%
        }
    }
    
}