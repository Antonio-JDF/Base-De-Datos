package controller;

import conection.ConnectionJDBC;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoController {
    
    public List<String> obtenerDepartamentos() {
        List<String> lista = new ArrayList<>();
        // Consulta a la tabla DEPARTAMENTO 
        String sql = "SELECT ID_departamento FROM DEPARTAMENTO";
        try (Connection conn = ConnectionJDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add("Depto: " + rs.getInt("ID_departamento"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}