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
import net.sf.json.JSONArray;
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
       @GET
    @Path("showPlayerDetail&{playername}")
    @Produces("application/json")
    public String showPlayerDetail(@PathParam("userId") int userId) {
        //TODO return proper representation object
        System.out.println("showPlayerDetail");
        String msg;
        Connection con = null;
        JSONObject singleObject = new JSONObject();
        JSONObject firstObject = new JSONObject();
        JSONArray mainArray = new JSONArray();
        PreparedStatement stmt = null;
        String status = "OK";
        String message = null;
        String sql;
        ResultSet rs;

        try {
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");
            // System.out.println("Succesfully connected");

            sql = "SELECT playerId,R.SOURCELOCATION,R.DESTINATION,R.RIDEDATE,R.EXTRADETAILS,U.FIRSTNAME,U.CONTACTNO,V.VEHICLETYPE,V.SEATSAVAILABLE FROM RIDE  R,USERDETAILS  U,VEHICLEdetail  V where U.USERID=R.USERID And V.VEHICLEID=R.VEHICLEID and U.userid!=" + userId;
            stmt = con.prepareStatement(sql);

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                message = "player Info";
                singleObject.accumulate("playerId", rs.getInt("playerId"));
                singleObject.accumulate("Player Name", rs.getString("name"));
                singleObject.accumulate("Destination", rs.getDate("dob"));
                singleObject.accumulate("role", rs.getString("role"));
                singleObject.accumulate("birthPlace", rs.getString("birthplace"));
            }

        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {
            firstObject = new JSONObject();
            firstObject.accumulate("Status", status);
            firstObject.accumulate("Check", userId);
            firstObject.accumulate("TimeStamp", timeStamp);
            firstObject.accumulate("Message", message);
            firstObject.accumulate("player info", mainArray);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return singleObject.toString();

    }
    
     //update team informtion
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("updateTeam&{teamId}&{name}&{color}&{leagueManagerId}&{teamManagerId}")
    public String InsertTeam(@PathParam("teamId") int teamId,@PathParam("name") String name,@PathParam("color") String color,
                            @PathParam("teamManagerId") int teamManagerId)
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
            sql = "update Team set name=?,color=?,teamManagerId=? where teamId=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, color);
            stmt.setInt(3, teamManagerId);
            stmt.setInt(4, teamId);
            int rs = stmt.executeUpdate();
            firstObject.accumulate("Status", "Ok");
            firstObject.accumulate("TimeStamp ", ts);
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("viewFeedback")
    public String ViewFeedback() {

        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        String result = null;
        JSONObject jsonObject = new JSONObject();
        String status = "OK";
        String message = null;
        JSONObject mainObject=new JSONObject();
        JSONArray list = new JSONArray();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");

            sql = "Select * from Feedback";
            stm= con.prepareStatement(sql);
            rs = stm.executeQuery();
            
          
            while (rs.next()) {

                int feedbackId = rs.getInt("feedbackId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String email = rs.getString("email");
              
              
                mainObject.accumulate("feedbackId", feedbackId);
                mainObject.accumulate("title", title);
                mainObject.accumulate("description", description);
                mainObject.accumulate("email", email);
                System.out.println(mainObject);
                list.add(mainObject);
                mainObject.clear(); 

            }

        } catch (SQLException ex) {
             Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println(ex);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
     
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("Message", result);
            jsonObject.accumulate("String", list);
           
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
    @Path("viewSchedule")
    public String ViewSchedule() {

        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        String result = null;
        JSONObject jsonObject = new JSONObject();
        String status = "OK";
        String message = null;
        JSONObject mainObject=new JSONObject();
        JSONArray list = new JSONArray();

        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");

            sql = "Select * from Schedule";
            stm= con.prepareStatement(sql);
            rs = stm.executeQuery();
            
          
            while (rs.next()) {

                int scheduleId = rs.getInt("scheduleId");
                String matchName = rs.getString("matchName");
                String teamA = rs.getString("teamA");
                String teamB = rs.getString("teamB");
                String date = rs.getString("Date");
                String venue = rs.getString("venue");
                int currentMatchId = rs.getInt("currentMatchId");
                int teamId = rs.getInt("teamId");
              
                mainObject.accumulate("scheduleId", scheduleId);
                mainObject.accumulate("matchName", matchName);
                mainObject.accumulate("teamA", teamA);
                mainObject.accumulate("teamB", teamB);
                mainObject.accumulate("Date", date);
                mainObject.accumulate("venue", venue);
                mainObject.accumulate("currentMatchId", currentMatchId);
                mainObject.accumulate("teamId", teamId);
                System.out.println(mainObject);
                list.add(mainObject);
                mainObject.clear(); 

            }

        } catch (Exception ex) {
            // Logger.getLogger(Feedback.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println(ex.getCause());
            
        } finally {
     
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("Message", result);
            jsonObject.accumulate("String", list);
           
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
    @Path("viewTeam")
    public String ViewTeam() {

        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        String result = null;
        JSONObject singleObject=new JSONObject();
        JSONObject jsonObject = new JSONObject();
         JSONArray jsonarry=new JSONArray();
        String status = "OK";
        String message = null;
        String name=null;
        String color=null;
        int teamManagerId;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //DriverManager.registerDriver(new mysql.jdbc.OracleDriver());
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");

            sql = "Select * from Team";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
              //  System.out.println(rs.toString());
                singleObject.accumulate("teamId", rs.getString("teamId"));
               singleObject.accumulate("name", rs.getString("name"));
               singleObject.accumulate("color", rs.getString("color"));
               singleObject.accumulate("leagueManagerId", rs.getString("leagueManagerId"));
               singleObject.accumulate("teamManagerId", rs.getInt("teamManagerId"));
                jsonarry.add(singleObject);
                singleObject.clear();

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
            jsonObject.accumulate("String", jsonarry);   
            
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
    @Path("showPlayersbyTeam&{teamname}")
    @Produces("application/json")
    public String showPlayerbyTeam(@PathParam("teamName") String teamname ) {
      
          Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        int userId=-1;
        String name = null;
        int player = 0;
        JSONObject jsonObject = new JSONObject();
        JSONObject singleObject = new JSONObject();
        Date  dob;
        String result = null;
        String status = "OK";
        String birthplace = null;
        String teamName = null; 
        
        

        try {
            //Class.forName("com.mysql.jdbc.Driver");
            //DriverManager.registerDriver(new mysql.jdbc.OracleDriver());
            con = DriverManager.getConnection("jdbc:mysql://198.71.227.97:3306/cpl", "mahesh", "eQa2j#78");

            sql = "Select name from players join teamplayer on teamplayer.playerid = player.playerid  where  team id = ";
            stm = con.prepareStatement(sql);
            stm.setString(1, name);
            rs = stm.executeQuery();
        
        
            
            while (rs.next()) {
                singleObject.accumulate("player", rs.getInt(1));
                name = rs.getString(2);
                dob = rs.getDate(3);
                birthplace = rs.getString(4);
                teamName = rs.getString(5);
                
                
            }

          
        } catch (SQLException ex) {
            status = "Error";
            result = ex.getMessage();
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
 }

