/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author dipal
 */
@Path("main")
public class Cpl {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Cpl
     */
    public Cpl() {
    }

    /**
     * Retrieves representation of an instance of cpl.Cpl
     * @return an instance of java.lang.String
     */
    
    long timeStamp = System.currentTimeMillis() / 1000;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("userLogin&{userEmail}&{userPass}")
    public String userLogin(@PathParam("userEmail") String userEmail, @PathParam("userPass") String userPass) {

        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        int userId=-1;
        String dbEmail = null;
        String dbpass = null;
        String result = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            //DriverManager.registerDriver(new mysql.jdbc.OracleDriver());
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");

            sql = "Select * from User where email=?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, userEmail);
            rs = stm.executeQuery();

            while (rs.next()) {
                userId=rs.getInt(1);
                dbEmail = rs.getString(4);
                dbpass = rs.getString(5);
            }

            if (!rs.wasNull() && dbpass.equals(userPass)) {
                result = "Login Successfull";
            } else if (!rs.wasNull()) {
                result = "Invalid Password";
            }

        } catch (SQLException ex) {
            status = "Error";
            result = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", result);
            jsonObject.accumulate("User Id", userId);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("changePassword&{password}&{userName}&{dob}")
    public String InsertTeam(@PathParam("password") String password,
            @PathParam("userName") String userName,@PathParam("dob") String dob)
                      {
        //TODO return proper representation object
        String msg;
        Connection conn = null;
        JSONObject firstObject = new JSONObject();
        Date cd = new Date();
        long epoch = cd.getTime();
        int ts = (int) (epoch / 1000);
        System.out.println("Current Time Stamp: " + ts);
        PreparedStatement stmt = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");
            //System.out.println(conn)
            String sql;
            sql = "update User set password=? where userName=? && dob=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, password);
            stmt.setString(2, userName);
            stmt.setString(3,dob);
            int rs = stmt.executeUpdate();
            firstObject.accumulate("Status", "Ok");
            firstObject.accumulate("TimeStamp ", ts);
             
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date parsed = null;
            try {
                parsed = format.parse(dob);
            } catch (ParseException ex) {
                Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());


            if (rs > 0) {
                msg = rs + " Record have successfully been updated.";
                firstObject.accumulate("message : ", msg);
            } else {
                msg = rs + " No record updated.";
                firstObject.accumulate("message : ", msg);
            }

        } catch (Exception ex) {
            msg = ex.getMessage();
            firstObject.accumulate("Status", "Error");
            firstObject.accumulate("TimeStamp ", ts);
            firstObject.accumulate("Message :", msg);

        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return firstObject.toString();
    }

    @GET
    @Path("insertFeedback&{title}&{description}&{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String insertFeedback(@PathParam("title") String title, @PathParam("description") String description, 
                                 @PathParam("email") String email) {
       
        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        String result = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");
            
             sql = "insert into Feedback (title, description, email) values(?,?,?)";

            stm = con.prepareStatement(sql);
            stm.setString(1,title);
            stm.setString(2, description);
            stm.setString(3, email);
            int rs = stm.executeUpdate();
            
            if (rs > 0) {
                message = "Feedback inserted Succesfully";
            } else {
                message = "Failed to insert";
                status = "error";
            }  
             
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            status = "Error";
            result = ex.getMessage();
        }finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Message", message);
           
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        return jsonObject.toString();
    }
    }
}
