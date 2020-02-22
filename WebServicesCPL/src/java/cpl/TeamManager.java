/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpl;



import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 *
 * @author kushal
 */

@Path("teamManager")
public class TeamManager {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of B
     */
    public TeamManager() {
    }

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addTeamPlayer&{playerId}&{teamId}")
    public String addTeamPlayer(@PathParam("playerId") int playerId,@PathParam("teamId") int teamId) {

        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        int rs;
        String dbEmail = null;
        String dbpass = null;
        String result = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");

            sql = "insert into TeamPlayer (?,?)";
            stm = con.prepareStatement(sql);
            rs = stm.executeUpdate();

            System.out.println(rs + " rs ");
            if (rs > 0) {
                status = "inserted";
            } else {
                status = "not inserted";
            }

        } catch (SQLException ex) {
            status = "Error";
            result = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TeamManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("Result", result);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TeamManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(TeamManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

}

