package cn.edu.cuit.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cuit.model.UserInfo;

public class DBOperation {
    private DBHelper dbHelper;
    private Connection connection;

    public DBOperation(){
        dbHelper=new DBHelper();
        connection=dbHelper.getConnection();

    }

    public int insert(UserInfo userInfo){
        int res = 0;
        try {
            String sql = "insert into UserInfo(UserName, Email, Phone, Password) values(?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,userInfo.getUserName());
            ps.setString(2,userInfo.getEmail());
            ps.setString(3,userInfo.getPhone());
            ps.setString(4,userInfo.getPassword());
            res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbHelper.closeConnection();
        }
        return res;
    }

    public int deleteByUserName(String userName) {
        int res= 0;
        try {
            String sql = "delete from UserInfo where UserName = '"+userName+"';";
            PreparedStatement ps = connection.prepareStatement(sql);
            res=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbHelper.closeConnection();
        }
        return res;
    }

    public int deleteByPhone(String phone) {
        int res= 0;
        try {
            String sql = "delete from UserInfo where UserName = '"+phone+"';";
            PreparedStatement ps = connection.prepareStatement(sql);
            res=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbHelper.closeConnection();
        }
        return res;
    }

    public int deleteByEmail(String email) {
        int res= 0;
        try {
            String sql = "delete from UserInfo where UserName = '"+email+"';";
            PreparedStatement ps = connection.prepareStatement(sql);
            res=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbHelper.closeConnection();
        }
        return res;
    }

    public List<UserInfo> selectAll(){
        List<UserInfo> userInfos = new ArrayList<>();

        try {
            String sql ="select * from UserInfo;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                UserInfo userInfo = new UserInfo();
                userInfo.setId(rs.getInt("Id"));
                userInfo.setUserName(rs.getString("UserName"));
                userInfo.setEmail(rs.getString("Email"));
                userInfo.setPhone(rs.getString("Phone"));
                userInfo.setPassword(rs.getString("Password"));
                userInfos.add(userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbHelper.closeConnection();
        }
        return userInfos;
    }

    private UserInfo selectByItem(String item) {
        UserInfo userInfo = new UserInfo();
        try {
            String sql = "select * from UserInfo where UserName = '" + item + "';";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userInfo.setId(rs.getInt("Id"));
                userInfo.setUserName(rs.getString("UserName"));
                userInfo.setEmail(rs.getString("Email"));
                userInfo.setPhone(rs.getString("Phone"));
                userInfo.setPassword(rs.getString("Password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnection();
        }
        return userInfo;
    }

    public UserInfo selectByUserName(String userName){
        return selectByItem(userName);
    }

    public UserInfo selectByPhone(String phone){
        return selectByItem(phone);
    }

    public UserInfo selectByEmail(String email){
        return selectByItem(email);
    }

    public int updateForPassword(String userName, String password){
        int res = 0;
        try {
            String sql = "update UserInfo set Password = ? where UserName = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,password);
            ps.setString(2,userName);
            res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbHelper.closeConnection();
        }
        return res;
    }
}
