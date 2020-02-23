/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Max
 */
public class DbConnection {
    
    public Connection con() throws ClassNotFoundException, SQLException {
          
        Connection con1=null;
      
          Class.forName("com.mysql.jdbc.Driver");
       con1 = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");
         return con1;
    }
}
