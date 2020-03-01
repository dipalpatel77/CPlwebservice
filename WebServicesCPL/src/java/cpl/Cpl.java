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

@Path("main")
public class Cpl extends DbConnection {

    private final long timeStamp = System.currentTimeMillis() / 1000;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("userLogin&{userType}&{userEmail}&{userPass}")
    public String userLogin(@PathParam("userType") String userType, @PathParam("userEmail") String userEmail, @PathParam("userPass") String userPass) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        int userId = -1;
        String message = "Login Failed";
        JSONObject jsonObject = null;
        String status = "OK";

        try {
            if (userType.equalsIgnoreCase("LeagueManager")) {
                sql = "Select lm.userId from LeagueManager lm join User u ON lm.userId = u.userId where u.email=? && u.password=?";
            } else {
                sql = "Select tm.userId from TeamManager tm join User u ON tm.userId = u.userId where u.email=? && u.password=?";
            }
            stm = con().prepareStatement(sql);
            stm.setString(1, userEmail);
            stm.setString(2, userPass);
            rs = stm.executeQuery();

            while (rs.next()) {
                userId = rs.getInt("userId");
                message = "Login Successfull";
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("UserId", userId);
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

     @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("verifyUser&{userEmail}&{dob}")
    public String verifyUser(@PathParam("userEmail") String userEmail, @PathParam("dob") String dob) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        int userId = -1;
        String message = "Invalid User";
        JSONObject jsonObject = null;
        String status = "OK";

        try {
            
          sql = "Select userId from User where email=? && dob=?";
            
            stm = con().prepareStatement(sql);
            stm.setString(1, userEmail);
            stm.setString(2, dob);
            rs = stm.executeQuery();

            while (rs.next()) {
                userId = rs.getInt("userId");
                message = "Valid User";
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("UserId", userId);
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

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("changePassword&{newPassword}&{userId}")
    public String changePassword(@PathParam("newPassword") String newPassword,
            @PathParam("userId") int userId) {

        PreparedStatement stm = null;
        String sql = null;
        int rs;
        String message = null;
        JSONObject jsonObject = null;
        String status = "OK";

        try {
            sql = "update User set password=? where userId=?";
            stm = con().prepareStatement(sql);
            stm.setString(1, newPassword);
            stm.setInt(2, userId);
            rs = stm.executeUpdate();

            if (rs > 0) {
                message ="Password updated successfully";

            } else {
                message = "No records updated.";
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
    @Path("sendFeedback&{title}&{description}&{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String sendFeedback(@PathParam("title") String title, @PathParam("description") String description,
            @PathParam("email") String email) {

        PreparedStatement stm = null;
        String sql = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;

        try {
            sql = "insert into Feedback (title, description, email) values(?,?,?)";
            stm = con().prepareStatement(sql);
            stm.setString(1, title);
            stm.setString(2, description);
            stm.setString(3, email);
            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = "Feedback Sent Successfully";
            } else {
                message = "Failed to Send Feedback";
                status = "Error";
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
    @Path("viewPlayerDetails&{playerId}")
    @Produces("application/json")
    public String viewPlayerDetails(@PathParam("playerId") int playerId) {

        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        PreparedStatement stm = null;
        String status = "OK";
        String message = null;
        String sql;
        ResultSet rs;

        try {
            sql = "Select * from Player where playerId=?";
            stm = con().prepareStatement(sql);
            stm.setInt(1, playerId);
            rs = stm.executeQuery(sql);

            singleObject = new JSONObject();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("playerId", rs.getInt("playerId"));
                singleObject.accumulate("PlayerName", rs.getString("playerName"));
                singleObject.accumulate("BirthDate", rs.getString("dob"));
                singleObject.accumulate("role", rs.getString("role"));
                singleObject.accumulate("birthPlace", rs.getString("birthPlace"));
                singleObject.accumulate("teamId", rs.getString("teamId"));
            }

        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Player Details", singleObject);
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("updateTeam&{teamId}&{teamName}&{teamColor}&{teamManagerId}")
    public String updateTeam(@PathParam("teamId") int teamId,
            @PathParam("teamName") String teamName,
            @PathParam("teamColor") String teamColor,
            @PathParam("teamManagerId") int teamManagerId) {

        String msg = null;
        String status = "OK";
        JSONObject jsonObject = null;
        PreparedStatement stmt = null;

        try {
            String sql;
            sql = "Update Team set teamName=?,teamColor=?,teamManagerId=? where teamId=?";
            stmt = con().prepareStatement(sql);
            stmt.setString(1, teamName);
            stmt.setString(2, teamColor);
            stmt.setInt(3, teamManagerId);
            stmt.setInt(4, teamId);
            int rs = stmt.executeUpdate();

            if (rs > 0) {
                msg = rs + " Record have successfully been updated.";
            } else {
                msg = rs + " No record updated.";
            }

        } catch (Exception ex) {
            status = "error";
            msg = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp ", timeStamp);
            jsonObject.accumulate("Message :", msg);
            try {
                con().close();
            } catch (SQLException ex) {
                Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("viewSchedule&{scheduleId}")
    public String viewSchedule(@PathParam("scheduleId") int scheduleId) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
        JSONObject mainObject = null;
        JSONArray list = null;

        try {
            sql = "Select * from Schedule where scheduleId=?";
            stm = con().prepareStatement(sql);
            stm.setInt(1, scheduleId);
            rs = stm.executeQuery();

            mainObject = new JSONObject();
            list = new JSONArray();
            while (rs.next()) {
                message = "Available";
                mainObject.accumulate("scheduleId", rs.getInt("scheduleId"));
                mainObject.accumulate("teamA", rs.getString("teamA"));
                mainObject.accumulate("teamB", rs.getString("teamB"));
                mainObject.accumulate("Date", rs.getString("Date"));
                mainObject.accumulate("venue", rs.getString("venue"));
                //Cannot determine value type from string 'null'
//                mainObject.accumulate("result", rs.getString("result"));
//                mainObject.accumulate("resultDescription", rs.getInt("resultDescription"));
                System.out.println(mainObject);
                list.add(mainObject);
                mainObject.clear();
            }

        } catch (Exception ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Schedule", list);

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("viewTeams")
    public String viewTeams() {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        String result = null;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonarry = null;
        String status = "OK";
        String message = null;
        try {
            sql = "Select * from Team";
            stm = con().prepareStatement(sql);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonarry = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("teamId", rs.getString("teamId"));
                singleObject.accumulate("teamName", rs.getString("teamName"));
                singleObject.accumulate("teamColor", rs.getString("teamColor"));
                singleObject.accumulate("teamManagerId", rs.getInt("teamManagerId"));
                jsonarry.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            result = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", result);
            jsonObject.accumulate("Teams", jsonarry);

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

    @GET
    @Path("viewPlayersByTeam&{teamId}")
    @Produces("application/json")
    public String viewPlayersByTeam(@PathParam("teamId") int teamId) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject jsonObject = null;
        JSONObject singleObject = null;
        JSONArray list = null;
        String status = "OK";
        String message = null;

        try {
            sql = "Select * from Player where teamId=?";
            stm = con().prepareStatement(sql);
            stm.setInt(1, teamId);
            rs = stm.executeQuery();
            singleObject = new JSONObject();
            list = new JSONArray();

            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("playerId", rs.getString("playerId"));
                singleObject.accumulate("name", rs.getString("playerName"));
                list.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Players", list);
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

    @GET
    @Path("viewSeason")
    @Produces("application/json")
    public String viewSeason() {
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String status = "OK";
        String message = null;

        try {
            sql = "Select * from Season";
            stm = con().prepareStatement(sql);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonArray = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("Season Title", rs.getString("seasonTitle"));
                singleObject.accumulate("Description", rs.getString("description"));
                singleObject.accumulate("Start Date", rs.getString("startDate"));
                singleObject.accumulate("End Date", rs.getString("endDate"));
                jsonArray.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("String", jsonArray);

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
}
