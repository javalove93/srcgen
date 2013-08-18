package designtime.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Properties;

public class DBTableToClass {
	public static void main(String args[]) throws Exception {
		String resultName = args.length > 0 ? args[0] : "##RESULT##";
		String recordName = args.length > 1 ? args[1] : "##RECORD##";
		String methodName = args.length > 2 ? args[2] : "##METHOD##";
		String param1 = args.length > 3 ? args[3] : "##PARAM1##";
		
		BufferedReader in = new BufferedReader(new FileReader("./src/designtime/utils/DbTableToClass.txt"));

		char[] buf = new char[65536];
		String source = "";
		while(true) {
			int len = in.read(buf);
			if(len < 0) {
				break;
			}
			
			source = source + new String(buf, 0, len);
		}
		
		BufferedReader con = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Schema Name(rtest): ");
		String schemaName = con.readLine();
		if(schemaName.trim().equals("")) {
			schemaName = "rtest";
		}
		System.out.println(schemaName);

		System.out.print("Table Name: ");
		String tableName = con.readLine();
		System.out.println(tableName);
		
		System.out.println("\n");

		String jdbcUrl = "jdbc:mysql://centos:3306/rtest";
		Properties props = new Properties();
		props.setProperty("user", "rtest");
		props.setProperty("password", "welcome1");

		Connection conn = null;

		try {
			Driver drv = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = drv.connect(jdbcUrl, props);
			conn.setAutoCommit(false);
			
			DatabaseMetaData meta = conn.getMetaData();
//			ResultSet rs = meta.getTables(null, "rtest", null, null);
//			while(rs.next()) {
//				String name = rs.getString("TABLE_NAME");
//				String cat = rs.getString("TABLE_CAT");
//				System.out.println(cat + ": " + name);
//			}
			
			System.out.println("// Table: " + schemaName + "." + tableName);
			ResultSet rs2 = meta.getColumns(null, schemaName, tableName, null);
			ArrayList<String> colNames = new ArrayList<String>();
			ArrayList<String> colTypes = new ArrayList<String>();
			while(rs2.next()) {
				String colName = rs2.getString("COLUMN_NAME");
				int type = rs2.getInt("DATA_TYPE");
				String typeName = rs2.getString("TYPE_NAME");
				switch(type) {
				case Types.VARCHAR:
					typeName = "String";
					break;
				case Types.INTEGER:
					typeName = "Integer";
					break;
				case Types.DECIMAL:
//					typeName = "java.math.BigDecimal";
					typeName = "String";
					break;
				case Types.CHAR:
					typeName = "String";
					break;
				case Types.TIMESTAMP:
					typeName = "java.util.Date";
					break;
				}
				
				colNames.add(colName);
				colTypes.add(typeName);
				System.out.println(typeName + " " + colName + ";");
			}
			
			PreparedStatement pstmt = conn.prepareStatement("select * from " + schemaName + "." + tableName);
			ResultSet rs3 = pstmt.executeQuery();
			ArrayList<Boolean> nullCols = new ArrayList<Boolean>();
			for(int i = 0; i < colNames.size(); i++) {
				//	null로 가정
				nullCols.add(true);
			}
			while(rs3.next()) {
				for(int i = 0; i < colNames.size(); i++) {
					if(!nullCols.get(i)) {
						//	null이 아닌 것으로 판명나면 빨리 종료
						break;
					}
					
					String colName = colNames.get(i);
					Object obj = rs3.getObject(colName);
					if(obj != null) {
						nullCols.set(i, false);
					}
				}
			}
			
			for(int i = 0; i < colNames.size(); i++) {
				if(nullCols.get(i)) {
					System.out.println("// " + colNames.get(i) + " is null");
				}
			}

			String fields = "\t";
			String aLine = "";
			String hashFields = "\t";
			String updateFields = "";
			boolean first = true;
			for(int i = 0; i < colNames.size(); i++) {
				if(!first) {
					fields = fields + ", ";
					hashFields = hashFields + ", ";
//					updateFields = updateFields + ",\n";
				}
				aLine = aLine + colNames.get(i);
				fields = fields + colNames.get(i);
				hashFields = hashFields + "#" + colNames.get(i) + "#";
				if(aLine.length() > 80) {
					fields = fields + "\n\t";
					hashFields = hashFields + "\n\t";
					aLine = "";
				}
				
				if(!first) updateFields = updateFields + "\t<isNotNull prepend=\",\" property=\"" + colNames.get(i) + "\">\n\t\t" + colNames.get(i) + " = #" + colNames.get(i) + "#\n\t</isNotNull>\n";
				
				first = false;
			}

			String field1 = colNames.get(0);
					
			source = source.replace("##SCHEMA##", schemaName);
			source = source.replace("##TABLE##", tableName);
			source = source.replace("##RESULT##", resultName);
			source = source.replace("##RECORD##", recordName);
			source = source.replace("##METHOD##", methodName);
			source = source.replace("##PARAM1##", param1);
			source = source.replace("##FIELD1##", field1);
			source = source.replace("##FIELDS##", fields);
			source = source.replace("##HASH_FIELDS##", hashFields);
			source = source.replace("##UPDATE_FIELDS##", updateFields);
			
			System.out.println("**************************************************************\n");
			System.out.println(source);
			System.out.println("**************************************************************\n");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
