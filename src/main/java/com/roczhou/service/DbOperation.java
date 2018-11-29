package com.roczhou.service;

import com.roczhou.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by ZhouPeng on 2018/11/29.
 */
public class DbOperation {
    public void saveCardInfo2DB(HashMap cardInfo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        conn = ConnectionFactory.getConnection();
        conn.setAutoCommit(true);

        String sql = "insert into cards (id_no,name,sex,nation,birth,address,image_status,risk_type,direction,log_id) values('"+cardInfo.get("id_no")+"','"+cardInfo.get("name")+"','"+cardInfo.get("sex")+"','"+cardInfo.get("nation")+"','"+cardInfo.get("birth")+"','"+cardInfo.get("address")+"','"+cardInfo.get("image_status")+"','"+cardInfo.get("risk_type")+"','"+cardInfo.get("direction")+"','"+cardInfo.get("log_id")+"')";
        pstm = conn.prepareStatement(sql);
        pstm.execute();
        if (pstm != null) {
            try {
                pstm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
