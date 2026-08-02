package controller;

import conection.ConnectionJDBC;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class EventoController {

    public DefaultTableModel obtenerEventosConCreadores() {
        String[] columnas = {"Evento", "Tipo", "Creador", "Nacionalidad", "Tienda ID"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        String sql = "SELECT E.Nombre AS Evento, E.Tipo_evento, C.Nombre AS Creador, " +
                     "C.Nacionalidad, E.TIENDA_ID_tienda " +
                     "FROM EVENTO E " +
                     "LEFT JOIN CREADOR C ON E.ID_evento = C.EVENTO_ID_evento";

        try (Connection conn = ConnectionJDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("Evento"), rs.getString("Tipo_evento"),
                    rs.getString("Creador"), rs.getString("Nacionalidad"),
                    rs.getInt("TIENDA_ID_tienda")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return modelo;
    }
    
    public void insertarEventoConCreadorExistente(int idEv, String nomEv, String tipoEv, int idTienda, int idCrExistente) throws Exception {
        String[] datos = buscarCreadorPorId(idCrExistente);
        if (datos == null) throw new Exception("El creador con ID " + idCrExistente + " no existe.");

        int nuevoIdRegistroCr = obtenerUltimoIdCreador() + 1;

        insertarEventoYCreador(idEv, nomEv, tipoEv, idTienda, nuevoIdRegistroCr, datos[0], datos[1], datos[2]);
    }
    
    public void insertarEventoYCreador(int idEv, String nomEv, String tipoEv, int idTienda,
                                      int idCr, String nomCr, String nacCr, String tipoCr) throws Exception {
        
        String sqlEvento = "INSERT INTO EVENTO (ID_evento, Nombre, Tipo_evento, TIENDA_ID_tienda) VALUES (?, ?, ?, ?)";
        String sqlCreador = "INSERT INTO CREADOR (ID_creador, Nombre, Nacionalidad, Tipo_creador, EVENTO_ID_evento) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionJDBC.getConnection()) {
            conn.setAutoCommit(false); // Iniciamos transacción

            try {
                try (PreparedStatement psEv = conn.prepareStatement(sqlEvento)) {
                    psEv.setInt(1, idEv);
                    psEv.setString(2, nomEv);
                    psEv.setString(3, tipoEv);
                    psEv.setInt(4, idTienda);
                    psEv.executeUpdate();
                }

                try (PreparedStatement psCr = conn.prepareStatement(sqlCreador)) {
                    psCr.setInt(1, idCr);
                    psCr.setString(2, nomCr);
                    psCr.setString(3, nacCr);
                    psCr.setString(4, tipoCr);
                    psCr.setInt(5, idEv);
                    psCr.executeUpdate();
                }

                conn.commit(); 
            } catch (Exception e) {
                conn.rollback(); 
                throw e;
            }
        }
    }
    
    /**
     * Obtiene el ID más alto registrado en la tabla EVENTO.
     */
    public int obtenerUltimoIdEvento() {
        String sql = "SELECT MAX(ID_evento) FROM EVENTO";
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

    /**
     * Obtiene el ID más alto registrado en la tabla CREADOR.
     */
    public int obtenerUltimoIdCreador() {
        String sql = "SELECT MAX(ID_creador) FROM CREADOR";
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
    
    public String[] buscarCreadorPorId(int idCr) {
        String sql = "SELECT Nombre, Nacionalidad, Tipo_creador FROM CREADOR WHERE ID_creador = ?";
        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                        rs.getString("Nombre"),
                        rs.getString("Nacionalidad"),
                        rs.getString("Tipo_creador")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}