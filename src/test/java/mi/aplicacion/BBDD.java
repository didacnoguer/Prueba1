package es.gimbernat;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BBDD {
    private  Connection conn;

    public  boolean init() {
        try {
        	Properties p = loadPropertiesFile();
        	if (p==null) return false;
        	
        	String strConn = (String) p.get("db.string_connection");
        	System.out.println(strConn);
            conn = DriverManager.getConnection(strConn);
            return true;
        } catch (SQLException e) {
            showError(e);
            unLoad();
            return false;
        }
    }

    public void showError(SQLException e) {
        System.out.println("Mensaje de error: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }
    
    public void unLoad()
    {
    	try {
			if (conn!= null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public Properties loadPropertiesFile()
    {
    	Properties p = new Properties();
    	try {
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			p.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    	return p;
    }
}
