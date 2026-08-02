package controller;

import conection.ConnectionJDBC;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import model.Tienda;

public class TiendaController {
	public void insertarTienda(int id, String dir, String ciudad, String telf) throws Exception {
	    String sql = "INSERT INTO TIENDA (ID_tienda, Direccion, Ciudad, Telefono) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = ConnectionJDBC.getConnection(); 
	            PreparedStatement ps = conn.prepareStatement(sql)) {
	           
	           ps.setInt(1, id);
	           ps.setString(2, dir);
	           ps.setString(3, ciudad);
	           ps.setString(4, telf);
	           
	           ps.executeUpdate();
	       }
	}
    
    public List<Tienda> obtenerTiendas() throws SQLException {
        List<Tienda> lista = new ArrayList<>();
        String sql = "SELECT * FROM TIENDA ORDER BY ID_tienda ASC";
        
        try (Connection conn = ConnectionJDBC.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Tienda(
                    rs.getInt("ID_tienda"),
                    rs.getString("Direccion"),
                    rs.getString("Ciudad"),
                    rs.getString("Telefono")
                ));
            }
        }
        return lista;
    }
    
    /**
     * Obtiene el ID más alto registrado en la tabla PROVEEDOR.
     */
    public int obtenerUltimoId() {
        String sql = "SELECT MAX(ID_Tienda) FROM TIENDA";
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
    
}