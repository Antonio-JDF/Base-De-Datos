package controller;

import conection.ConnectionJDBC;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

import model.Producto;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class ProductoController {

    public void insertarPortada(int idProd, int idTienda, String rutaFichero) throws SQLException, IOException {
        Connection conn = ConnectionJDBC.getConnection();
        conn.setAutoCommit(false);
        String sqlInit = "UPDATE PRODUCTO SET Portada = EMPTY_BLOB() WHERE ID_producto = ? AND TIENDA_ID_tienda = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInit)) {
            pstmt.setInt(1, idProd);
            pstmt.setInt(2, idTienda);
            pstmt.executeUpdate();
        }
        String sqlSelect = "SELECT Portada FROM PRODUCTO WHERE ID_producto = ? AND TIENDA_ID_tienda = ? FOR UPDATE";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, idProd);
            pstmt.setInt(2, idTienda);
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
        conn.commit();
    }

    public Map<String, String> obtenerDetallesEspecificos(int idProd, int idTienda, String tipo) {
        Map<String, String> detalles = new HashMap<>();
        String tabla = tipo.toUpperCase(); // Mapeo a CINE, LIBRO, MÚSICA, etc.
        if(tabla.equals("MÚSICA")) tabla = "MUSICA"; 
        if(tabla.equals("TECNOLOGÍA")) tabla = "TECNOLOGIA";

        String sql = "SELECT * FROM " + tabla + " WHERE PRODUCTO_ID_producto = ? AND PRODUCTO_TIENDA_ID_tienda = ?";
        try (Connection conn = ConnectionJDBC.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProd);
            pstmt.setInt(2, idTienda);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    String col = md.getColumnName(i);
                    if (!col.contains("PRODUCTO_ID") && !col.contains("TIENDA_ID")) {
                        detalles.put(col, rs.getString(i));
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return detalles;
    }
    
    public void actualizarPortadaProducto(int idProducto, String rutaImagen) throws SQLException, IOException {
        Connection conn = ConnectionJDBC.getConnection();
        String sqlInit = "UPDATE PRODUCTO SET Portada = EMPTY_BLOB() WHERE ID_producto = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInit)) {
            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();
        }

        // 2. Insertar los bytes de la imagen
        String sqlUpdate = "SELECT Portada FROM PRODUCTO WHERE ID_producto = ? FOR UPDATE";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob(1);
                File file = new File(rutaImagen);
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[(int) file.length()];
                    fis.read(buffer);
                    blob.setBytes(1, buffer);
                }
            }
        }
    }
    
	
    public void insertarProductoYSubtipo(int id, int tienda, int proveedor, String nom, double pre, int stk, 
            String cat, String gar, String tipo, String rutaImg, 
            Map<String, String> extras) throws SQLException, IOException {
	    Connection conn = ConnectionJDBC.getConnection();
	    try {
	        conn.setAutoCommit(false);

	        String sqlProd = "INSERT INTO PRODUCTO (ID_producto, TIENDA_ID_tienda, PROVEEDOR_ID_proveedor,Nombre, Precio, Stock, CATEGORIA, GARANTÍA, TIPO_PRODUCTO, Portada) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, EMPTY_BLOB())";
	        try (PreparedStatement ps = conn.prepareStatement(sqlProd)) {
	            ps.setInt(1, id);
	            ps.setInt(2, tienda);
	            ps.setInt(3, proveedor);
	            ps.setString(4, nom);
	            ps.setDouble(5, pre);
	            ps.setInt(6, stk);
	            ps.setString(7, cat);
	            try {
	                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	                java.util.Date d = sdf.parse(gar); 
	                ps.setDate(8, new java.sql.Date(d.getTime()));
	            } catch (ParseException e) {
	                throw new SQLException("Error de formato en la garantía: '" + gar + "'. Use DD/MM/AAAA.");
	            }
	            ps.setString(9, tipo);
	            ps.executeUpdate();
	        }

	        String tabla = tipo.toUpperCase().replace("Ú", "U").replace("Í", "I");
	        StringBuilder sqlSub = new StringBuilder("INSERT INTO " + tabla + " (PRODUCTO_ID_producto, PRODUCTO_TIENDA_ID_tienda");
	        StringBuilder values = new StringBuilder(") VALUES (?, ?");
	        
	        for (String col : extras.keySet()) {
	            sqlSub.append(", \"").append(col.replace(" ", "_").toUpperCase()).append("\"");
	            values.append(", ?");
	        }
	        sqlSub.append(values).append(")");

	        try (PreparedStatement ps = conn.prepareStatement(sqlSub.toString())) {
	            ps.setInt(1, id); ps.setInt(2, tienda);
	            int i = 3;
	            for (String val : extras.values()) {
	                ps.setString(i++, val);
	            }
	            ps.executeUpdate();
	        }

	        if (rutaImg != null) actualizarPortadaProducto(id, rutaImg);
	        conn.commit();
	    } catch (SQLException e) {
	        conn.rollback();
	        throw e;
	    } finally {
	        conn.setAutoCommit(true);
	    }
	}
    
    public List<Producto> filtrarPorTipos(List<String> tiposSeleccionados) throws SQLException {
        List<Producto> resultados = new ArrayList<>();
        
        if (tiposSeleccionados == null || tiposSeleccionados.isEmpty()) {
            return resultados;
        }

        String placeholders = String.join(",", Collections.nCopies(tiposSeleccionados.size(), "?"));
        
        String sql = "SELECT * FROM PRODUCTO WHERE Tipo_producto IN (" + placeholders + ") ORDER BY Nombre ASC";

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < tiposSeleccionados.size(); i++) {
                pstmt.setString(i + 1, tiposSeleccionados.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(new Producto(
                        rs.getInt("ID_producto"),
                        rs.getString("Nombre"),
                        rs.getString("Categoria"),
                        rs.getDouble("Precio"),
                        rs.getInt("Stock"),
                        rs.getDate("Garantía"),
                        rs.getInt("PROVEEDOR_ID_proveedor"),
                        rs.getString("Tipo_producto"),
                        rs.getBytes("Portada"),
                        rs.getInt("TIENDA_ID_tienda")
                    ));
                }
            }
        }
        return resultados;
    }
    
    /**
     * Obtiene el ID más alto registrado en la tabla PROVEEDOR.
     */
    public int obtenerUltimoId() {
        String sql = "SELECT MAX(ID_producto) FROM PRODUCTO";
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