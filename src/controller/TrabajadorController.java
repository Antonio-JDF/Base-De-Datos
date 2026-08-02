package controller;

import conection.ConnectionJDBC;
import model.ItemTrabajador;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class TrabajadorController {

    public ResultSet obtenerDatosBasicos(int id) throws SQLException {
        Connection conn = ConnectionJDBC.getConnection();

        String sql =
            "SELECT t.*, j.Nombre AS nombre_jefe, j.Apellido1 AS apellido_jefe " +
            "FROM TRABAJADOR t " +
            "LEFT JOIN TRABAJADOR j ON t.TRABAJADOR_ID = j.ID " +
            "WHERE t.ID = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();
    }

    public List<ItemTrabajador> obtenerSubordinadosCombo(int idGerente) throws SQLException {

        List<ItemTrabajador> lista = new ArrayList<>();

        String sql = """
            SELECT ID, Nombre, Apellido1
            FROM TRABAJADOR
            WHERE TRABAJADOR_ID = ?
            ORDER BY Nombre
        """;

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGerente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String nombreCompleto =
                        rs.getString("Nombre") + " " + rs.getString("Apellido1");
                lista.add(new ItemTrabajador(id, nombreCompleto));
            }
        }

        return lista;
    }

    public boolean validarTrabajador(int id) {
        String sql = "SELECT COUNT(*) FROM TRABAJADOR WHERE ID = ?";
        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existeTrabajador(int id) {
        String sql = "SELECT COUNT(*) FROM TRABAJADOR WHERE ID = ?";
        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertarTrabajadorCompleto(
        int id,
        String nombre,
        String ape1,
        String ape2,
        Date fechaNac,
        Date fechaContrato,
        double salario,
        int idTienda,
        int idGerente,
        String tipo,
        Map<String, String> extras
) throws SQLException {

    Connection conn = ConnectionJDBC.getConnection();
    try {
        conn.setAutoCommit(false);

        /* ========= INSERT TRABAJADOR ========= */
        String sqlTrabajador = """
            INSERT INTO TRABAJADOR
            (ID, Nombre, Apellido1, Apellido2, Fecha_Nacimiento, Fecha_contrato,
             Salario, TIENDA_ID_tienda, TRABAJADOR_ID, Tipo_trabajador)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sqlTrabajador)) {
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setString(3, ape1);
            ps.setString(4, ape2);
            ps.setDate(5, fechaNac);
            ps.setDate(6, fechaContrato);
            ps.setDouble(7, salario);
            ps.setInt(8, idTienda);
            ps.setInt(9, idGerente);
            ps.setString(10, tipo);
            ps.executeUpdate();
        }

        //SUBTIPOS 
        switch (tipo) {

            //ATENCIÓN AL CLIENTE
            case "Atención_al_cliente" -> {
                String sql = """
                    INSERT INTO AT_CLIENTE (Idiomas, Turno, TRABAJADOR_ID)
                    VALUES (?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, extras.get("Idiomas"));
                    ps.setString(2, extras.get("Turno"));
                    ps.setInt(3, id);
                    ps.executeUpdate();
                }
            }

            //CAJERO (CREA CAJA ANTES)
            case "Cajero" -> {

                // NUEVO ID_CAJA
                int nuevoIdCaja = 1;
                String sqlMaxCaja = "SELECT NVL(MAX(ID_caja), 0) + 1 FROM CAJA";
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(sqlMaxCaja)) {
                    if (rs.next()) {
                        nuevoIdCaja = rs.getInt(1);
                    }
                }

                //CAJA
                String sqlCaja = """
                    INSERT INTO CAJA (ID_caja, Total_ventas_dia, Balance)
                    VALUES (?, 0, 0)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sqlCaja)) {
                    ps.setInt(1, nuevoIdCaja);
                    ps.executeUpdate();
                }

                //CAJERO
                String sqlCajero = """
                    INSERT INTO CAJERO (Turno, TRABAJADOR_ID, CAJA_ID_caja)
                    VALUES (?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sqlCajero)) {
                    ps.setString(1, extras.get("Turno"));
                    ps.setInt(2, id);
                    ps.setInt(3, nuevoIdCaja);
                    ps.executeUpdate();
                }
            }

            // VENDEDOR (ACTUALIZA DEPARTAMENTO)
            case "Vendedor" -> {

                int idDepartamento = Integer.parseInt(extras.get("ID Departamento"));

                // 1️ INSERTAR VENDEDOR
                String sqlVend = """
                    INSERT INTO VENDEDOR
                    (Turno, Comisiones, TRABAJADOR_ID, DEPARTAMENTO_ID_departamento)
                    VALUES (?, ?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sqlVend)) {
                    ps.setString(1, extras.get("Turno"));
                    ps.setDouble(2, Double.parseDouble(extras.get("Comisiones")));
                    ps.setInt(3, id);
                    ps.setInt(4, idDepartamento);
                    ps.executeUpdate();
                }

                // 2️ RECALCULAR NUM_EMPLEADOS
                int total;
                String sqlCount = """
                    SELECT COUNT(*) FROM VENDEDOR
                    WHERE DEPARTAMENTO_ID_departamento = ?
                """;
                try (PreparedStatement ps = conn.prepareStatement(sqlCount)) {
                    ps.setInt(1, idDepartamento);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    total = rs.getInt(1);
                }

                // 3️ UPDATE DEPARTAMENTO
                String sqlUpdateDept = """
                    UPDATE DEPARTAMENTO
                    SET Num_empleados = ?
                    WHERE ID_departamento = ?
                """;
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateDept)) {
                    ps.setInt(1, total);
                    ps.setInt(2, idDepartamento);
                    ps.executeUpdate();
                }
            }
        }

        conn.commit();

    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
        conn.close();
    }
}

    

    public ResultSet obtenerInfoCajas() throws SQLException {

        String sql = """
            SELECT
                c.ID_caja,
                c.Balance,
                c.Total_ventas_dia,
                tr.ID AS ID_trabajador,
                tr.Nombre,
                tr.Apellido1,
                tr.Apellido2,
                cj.Turno,
                tr.TIENDA_ID_tienda
            FROM CAJA c
            LEFT JOIN CAJERO cj ON cj.CAJA_ID_caja = c.ID_caja
            LEFT JOIN TRABAJADOR tr ON tr.ID = cj.TRABAJADOR_ID
            ORDER BY c.ID_caja
        """;

        Connection con = ConnectionJDBC.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery();
    }

    
    public ResultSet obtenerInfoDepartamentos() throws Exception {

        String sql =
            "SELECT d.id_departamento, d.nombre, COUNT(v.TRABAJADOR_ID) AS num_empleados " +
            "FROM ( " +
            "   SELECT 1 id_departamento, 'Cine' nombre FROM dual UNION ALL " +
            "   SELECT 2, 'Libro' FROM dual UNION ALL " +
            "   SELECT 3, 'Música' FROM dual UNION ALL " +
            "   SELECT 4, 'Tecnología' FROM dual UNION ALL " +
            "   SELECT 5, 'Videojuego' FROM dual " +
            ") d " +
            "LEFT JOIN VENDEDOR v ON v.DEPARTAMENTO_ID_departamento = d.id_departamento " +
            "GROUP BY d.id_departamento, d.nombre " +
            "ORDER BY d.id_departamento";

        Connection con = ConnectionJDBC.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery();
    }

    
    public int obtenerUltimoIdTrabajador() {
        String sql = "SELECT MAX(ID) FROM TRABAJADOR";

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

    public Map<String, String> obtenerDetallesEspecializados(int id, String tipo)
            throws SQLException {

        Map<String, String> detalles = new HashMap<>();
        String tabla = "";

        switch (tipo) {
            case "Atención_al_cliente": tabla = "AT_CLIENTE"; break;
            case "Cajero":              tabla = "CAJERO"; break;
            case "Gerente":             tabla = "GERENTE"; break;
            case "Vendedor":            tabla = "VENDEDOR"; break;
            default: return detalles;
        }

        String sql = "SELECT * FROM " + tabla + " WHERE TRABAJADOR_ID = ?";

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    if (!md.getColumnName(i).equalsIgnoreCase("TRABAJADOR_ID")) {
                        detalles.put(md.getColumnName(i), rs.getString(i));
                    }
                }
            }
        }

        return detalles;
    }
}
