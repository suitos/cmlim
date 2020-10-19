package seminartest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DBConnection {
	private static String DB_URL = "jdbc:mariadb://13.209.153.11:3306/seminar_fleta?autoReconnect=true";
	private static String DB_ID = "root";
	private static String DB_PW = "qlxkalsB!5))";
	
	private PreparedStatement pstmt = null;   
	
	private String entrance_query = "select entrance_code from mt_seminar_entrance_code where seminar_id='%s' and email='%s'";
	private String inviteID_query = "select id from mt_seminar_invite where seminar_id='%s' and email='%s'";
	private String authcode_query = "select auth_code from mt_auth_mail where email='%s'";
	private String passwordreset_query = "select password_reset_key from mt_user where email='%s'";
	private String endDate_query = "update mt_channel_charge set end_date='%s' where channel_id=(select id from mt_channel where name='%s');";
	
	private String setpw_query = "update mt_user set password='{bcrypt}$2a$10$P8LSA3E700EBvJWT8VilmuwPvMZmk9JWCU6weu/foF/9CjpJFov.u' where email='%s'";
	
	private String delete_channelmember_query = "delete from mt_channel_member where channel_id='%s'";
	private String delete_channel_query = "delete from mt_channel where id='%s'";
			
	private Connection connect(){

		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getEntranceCode(String seminarID, String email) {
		String ret = "";
		ResultSet rs = null;
		String query = String.format(entrance_query, seminarID, email);
		System.out.println("query :" + query);
		Connection conn = connect();
		if(conn != null) {
			try {

				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();

				while(rs.next()) {
	               ret = rs.getString(1);
	            }

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					if(pstmt != null) {
	                    pstmt.close(); // 선택사항이지만 호출 추천
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 필수 사항
				
			}
		}
		return ret;
	}
	
	public String getInviteID(String seminarID, String email) {
		String ret = "";
		ResultSet rs = null;
		String query = String.format(inviteID_query, seminarID, email);
		System.out.println("query :" + query);
		Connection conn = connect();
		if(conn != null) {
			try {

				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();

				while(rs.next()) {
	               ret = rs.getString(1);
	            }

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					if(pstmt != null) {
	                    pstmt.close(); // 선택사항이지만 호출 추천
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 필수 사항
				
			}
		}
		return ret;
	}
	
	public String getPasswordResetKey(String email) {
		String ret = "";
		ResultSet rs = null;
		String query = String.format(passwordreset_query, email);
		System.out.println("query :" + query);
		Connection conn = connect();
		if(conn != null) {
			try {

				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();

				while(rs.next()) {
	               ret = rs.getString(1);
	            }

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					if(pstmt != null) {
	                    pstmt.close(); // 선택사항이지만 호출 추천
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 필수 사항
				
			}
		}
		return ret;
	}
	
	public ArrayList<String> getAuthCodeForJoin(String email) {
		ResultSet rs = null;
		ArrayList<String> ret = new ArrayList<String>();
		String query = String.format(authcode_query, email);
		System.out.println("query :" + query);
		Connection conn = connect();
		if(conn != null) {
			try {

				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();

				
				while(rs.next()) {
	               ret.add(rs.getString(1));
	            }

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					if(pstmt != null) {
	                    pstmt.close(); // 선택사항이지만 호출 추천
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 필수 사항
				
			}
		}
		return ret;
	}
	
	/*
	 * setDemoExpire
	 * boolean isNow : true : 2일전  날짜로 데모 만료처리, false : 오늘 날짜로 데모 만료처리
	 * channelName : 채널명
	 */
	public void setDemoExpire(boolean isNow, String channelName) {
	
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		if(isNow)
			cal.add(Calendar.DAY_OF_MONTH, -2);

		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		String date = format2.format(cal.getTime());
		
		ResultSet rs = null;
		String query = String.format(endDate_query, date, channelName);
		System.out.println("query :" + query);
		Connection conn = connect();
		if(conn != null) {
			try {

				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();


			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					if(pstmt != null) {
	                    pstmt.close(); // 선택사항이지만 호출 추천
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 필수 사항
				
			}
		}
	}
	
	public void setUserPW(String email) {
		ResultSet rs = null;
		String query = String.format(setpw_query, email);
		System.out.println("query :" + query);
		Connection conn = connect();
		if(conn != null) {
			try {

				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					if(pstmt != null) {
	                    pstmt.close(); // 선택사항이지만 호출 추천
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 필수 사항
				
			}
		}
	}
	
	public void deleteChannel(String channelID) {
		
		ResultSet rs = null;
		String query = String.format(delete_channelmember_query, channelID);
		String query2 = String.format(delete_channel_query, channelID);
		System.out.println("query :" + query);
		System.out.println("query :" + query2);
		Connection conn = connect();
		if(conn != null) {
			try {

				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				
				pstmt.clearParameters();
				pstmt = conn.prepareStatement(query2);
				rs = pstmt.executeQuery();
				System.out.println(rs.next());
					if(rs.next() == true) {
						Exception e = new Exception("query failed");
						throw e;
						}
					

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					if(pstmt != null) {
	                    pstmt.close(); // 선택사항이지만 호출 추천
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 필수 사항
				
			}
		}
		
	}
	
}
