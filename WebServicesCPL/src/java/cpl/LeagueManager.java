/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("leagueManager")
public class LeagueManager extends DbConnection {

    private final long timeStamp = System.currentTimeMillis() / 1000;

    @GET
    @Path("createSeason&{seasonTitle}&{startDate}&{endDate}&{description}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createSeason(@PathParam("seasonTitle") String seasonTitle,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("description") String description) {

        PreparedStatement stm = null;
        JSONObject jsonObj = null;
        String sql;
        String status = "OK";
        String message = null;

        try {
            sql = "insert into Season (seasonTitle,startDate,endDate,description) values(?,?,?,?)";
            stm = con().prepareStatement(sql);
            stm.setString(1, seasonTitle);
            stm.setString(2, startDate);
            stm.setString(3, endDate);
            stm.setString(4, description);

            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = rs + " Records have successfully been inserted.";
                status = "ok";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

            if (con() != null) {
                try {
                    con().close();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("createTeam&{teamName}&{teamColor}&{teamManagerId}")
    public String createTeam(@PathParam("teamName") String teamName,
            @PathParam("teamColor") String teamColor,
            @PathParam("teamManagerId") int teamManagerId) {

        JSONObject jsonObject = null;
        PreparedStatement stmt = null;
        String sql;
        String status = "OK";
        String message = null;

        try {
            sql = "insert into Team (teamName,teamColor,teamManagerId) values(?,?,?)";
            stmt = con().prepareStatement(sql);
            stmt.setString(1, teamName);
            stmt.setString(2, teamColor);
            stmt.setInt(3, teamManagerId);

            int rs = stmt.executeUpdate();

            if (rs > 0) {
                message = rs + " Record(s) have been successfully inserted.";
            } else {
                message = rs + " No record Inserted.";
            }
        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            if (con() != null) {
                try {
                    con().close();
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
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteTeam&{teamId}")
    public String deleteTeam(@PathParam("teamId") int teamId) {

        PreparedStatement stm = null;
        String sql = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;

        try {
            sql = "Delete from Team where teamId=?";
            stm = con().prepareStatement(sql);
            stm.setInt(1, teamId);
            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = rs + " Record(s) have been successfully deleted.";
            } else {
                message = rs + " No record Deleted.";
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            if (con() != null) {
                try {
                    con().close();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("viewFeedback")
    public String viewFeedback() {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
        JSONObject singleObject = null;
        JSONArray list = null;

        try {
            sql = "Select * from Feedback";
            stm = con().prepareStatement(sql);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            list = new JSONArray();
            while (rs.next()) {
                singleObject.accumulate("feedbackId", rs.getInt("feedbackId"));
                singleObject.accumulate("title", rs.getString("title"));
                singleObject.accumulate("description", rs.getString("description"));
                singleObject.accumulate("email", rs.getString("email"));
                System.out.println(singleObject);
                list.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
            status = "Error";
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Feedbacks", list);

            if (con() != null) {
                try {
                    con().close();
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
    
    // Create Schedule
    @GET
    @Path("createSchedule&{scheduleId}&{teamA}&{teamB}&{date}&{venue}&{result}&{resultDescription}&{seasonId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createSeason(@PathParam("scheduleId") int scheduleId,
            @PathParam("teamA") String teamA,
            @PathParam("teamB") String teamB,
            @PathParam("date") String date, 
            @PathParam("venue") String venue,
            @PathParam("result") String result,
            @PathParam("resultDescription") String resultDescription,
            @PathParam("seasonId") int seasonId)
          {

        PreparedStatement stm = null;
        JSONObject jsonObj = null;
        String sql;
        String status = "OK";
        String message = null;

        try {
            
            sql = "insert into Schedule (scheduleId,teamA,teamB,date,venue,result,resultDescription,seasonId) values(?,?,?,?,?,?,?,?)";
            stm = con().prepareStatement(sql);

            stm.setInt(1,scheduleId);
            stm.setString(2,teamA);
            stm.setString(3,teamB);
            stm.setString(4,date);
            stm.setString(5,venue);
            stm.setString(6,result);
            stm.setString(7,resultDescription);
            stm.setInt(8,seasonId);

            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = rs + " Records have successfully been inserted.";
                status = "ok";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

            if (con() != null) {
                try {
                    con().close();
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
    
}
