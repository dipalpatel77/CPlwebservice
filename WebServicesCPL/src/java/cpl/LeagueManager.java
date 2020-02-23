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
 * @author hp
 */
@Path("leagueManager")
public class LeagueManager {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LeagueManager
     */
    public LeagueManager() {
    }

     long timeStamp = System.currentTimeMillis() / 1000;
    
    /**
     * Retrieves representation of an instance of cpl.LeagueManager
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

   // Create Season
    @GET
    @Path("createSeason&{seasonTitle}&{startDate}&{endDate}&{description}&{leagueManagerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createSeason(@PathParam("seasonTitle") String seasonTitle,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate, 
            @PathParam("description") String description,
            @PathParam("leagueManagerId") int leagueManagerId) {
        //TODO return proper representation object

        PreparedStatement stm = null;
        JSONObject jsonObj = null;
        String sql;
        Connection con = null;
        String status = "OK";
        String message = null;

        try {
            
             Class.forName("com.mysql.jdbc.Driver");
            //DriverManager.registerDriver(new mysql.jdbc.OracleDriver());
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");
            
            sql = "insert into Season (seasonTitle,startDate,endDate,description,leagueManagerId) values(?,?,?,?,?)";
            stm = con.prepareStatement(sql);

            stm.setString(1, seasonTitle);
            stm.setString(2, startDate);
            stm.setString(3, endDate);
            stm.setString(4, description);
            stm.setInt(5, leagueManagerId);

            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = rs + " Records have successfully been inserted.";
                status = "ok";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObj.toString();
    }
    
      // Insert team information
   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("insertTeam&{name}&{color}&{leagueManagerId}&{teamManagerId}")

    public String InsertTeam(@PathParam("name") String name,@PathParam("color") String color,
                            @PathParam("leagueManagerId") int leagueManagerId,
                            @PathParam("teamManagerId") int teamManagerId) {
        //TODO return proper representation object
        Connection conn = null;
        JSONObject firstObject = new JSONObject();
        PreparedStatement stmt = null;
        String sql;
        String status = "OK";
        String message = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");
            sql = "insert into Team(name,color,leagueManagerId,teamManagerId)values(?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            //stmt.setInt(1, 1);
            stmt.setString(1,name);
            stmt.setString(2,color);
            stmt.setInt(3,leagueManagerId);
            stmt.setInt(4,teamManagerId);
            
            int rs = stmt.executeUpdate();
            firstObject.accumulate("Status", "Ok");
            firstObject.accumulate("TimeStamp ", timeStamp);

            if (rs > 0) {
                message = rs + " Record(s) have been successfully inserted.";
                firstObject.accumulate("message : ", message);
            } else {
                message = rs + " No record Inserted.";
                firstObject.accumulate("message : ", message);
            }
        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();
        } finally {
            firstObject = new JSONObject();
            firstObject.accumulate("Status", status);
            firstObject.accumulate("TimeStamp", timeStamp);
            firstObject.accumulate("Message", message);
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return firstObject.toString();
    }
    
   //Delete Team Information
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteTeam&{teamId}")
    public String DeleteTeam(@PathParam("teamId") int teamId) {

        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs=null;
        String result = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
       

        try {
            Class.forName("com.mysql.jdbc.Driver");
            //DriverManager.registerDriver(new mysql.jdbc.OracleDriver());
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");

            sql = "Delete from Team where teamId=?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, teamId);
            stm.execute();        
        

        } catch (SQLException ex) {
            status = "Error";
            result = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", result);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return jsonObject.toString();
    }

    
}
