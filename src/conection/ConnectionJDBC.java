package conection;

import java.sql.Connection;
import java . sql . DriverManager ;
import java . sql . SQLException ;
import java . sql . Statement ;
public class ConnectionJDBC {
	private static String db_ = "atenea" ;
	private static String login_ = "UBD2770" ;
	private static String password_ = "UniUMABbMm1504" ;
	private static String url_ = "jdbc:oracle:thin:@diana.lcc.uma.es:1521:atenea" ;
	private static Connection connection_ ;
	private static Statement st_ = null ;
	private static Connection conn = null;
	public ConnectionJDBC () {
		try {
			Class . forName ( " com . mysql . jdbc . Driver " ) ;
			connection_ = DriverManager . getConnection ( url_ ,login_ , password_ ) ;
			if ( connection_ != null ) {
				st_ = connection_ . createStatement () ;
				System . out . println ( " Conexion a base de datos " + db_ + " correcta . " ) ;
			}	
			else
			System . out . println ( " Conexion fallida . " ) ;
		} catch ( SQLException e ) { e . printStackTrace () ;}
		catch ( ClassNotFoundException e ) { e . printStackTrace () ;}
		catch ( Exception e ) { e . printStackTrace () ;}
	}
	
	public static Connection getConnection() {
	    try {
	        if (conn == null || conn.isClosed()) {
	            String url = "jdbc:oracle:thin:@diana.lcc.uma.es:1521:atenea";
	            String user = login_;
	            String pass = password_;
	            conn = DriverManager.getConnection(url, user, pass);
	        }
	    } catch (SQLException e) {
	        // Esto te dirá en la consola POR QUÉ conn es null
	        System.err.println("FALLO AL OBTENER CONEXIÓN: " + e.getMessage());
	        return null; 
	    }
	    return conn;
	}
}
