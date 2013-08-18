package designtime.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SkeletonCreator {
	public static void main(String args[]) throws Exception  {
		BufferedReader in = new BufferedReader(new FileReader("./src/designtime/utils/SkeletonService.txt"));
		
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
		System.out.print("Servcie Name: ");
		String svcName = con.readLine();
		System.out.println(svcName);
		
		System.out.print("Method Name: ");
		String methodName = con.readLine();
		System.out.println(methodName);
		String nameCan = methodName;
		if(methodName.startsWith("get")) {
			nameCan = methodName.substring(3);
		}
		else if(methodName.startsWith("addOrUpdate")) {
			nameCan = methodName.substring(11);
		}
		nameCan = nameCan.substring(0, 1).toUpperCase() + nameCan.substring(1);
		
		System.out.print("Params(Blank line for EOF): ");
		String params = "";
		while(true) {
			String aLine = con.readLine();
			if(aLine.trim().equals("")) {
				break;
			}
			params = params + aLine + ",";
		}
		StringTokenizer tkn = new StringTokenizer(params, ",");
		ArrayList<String> paramsArray = new ArrayList<String>();
		ArrayList<String> paramsTypeArray = new ArrayList<String>();
		ArrayList<String> paramsNameArray = new ArrayList<String>();
		String paramsPrint = "";
		String canonicalParams = "";
		boolean first = true;
		while(tkn.hasMoreTokens()) {
			String aParam = tkn.nextToken().trim();
			StringTokenizer tkn2 = new StringTokenizer(aParam);
			String type = tkn2.nextToken();
			String name = tkn2.nextToken();
			paramsArray.add(aParam);
			paramsTypeArray.add(type);
			paramsNameArray.add(name);
			
			if(first) {
				first = false;
			}
			else {
				paramsPrint = paramsPrint + " + \", ";
				canonicalParams = canonicalParams + ", ";
			}
			
			paramsPrint = paramsPrint + name + "=\" + " + name;
			canonicalParams = canonicalParams + type + " " + name;
		}
		String param1 = paramsNameArray.size() >= 1 ? paramsNameArray.get(0) : null;
		String param2 = paramsNameArray.size() >= 2 ? paramsNameArray.get(1) : param1;
		System.out.println(canonicalParams);
		
		String resultNameCan = nameCan + "Result";
		System.out.print("Result Name(" + resultNameCan + "): ");
		String resultName = con.readLine();
		if(resultName.trim().equals("")) {
			resultName = resultNameCan;
		}
		System.out.println(resultName);
		
		String recordNameCan = nameCan + "Record";
		System.out.print("Record Name(" + recordNameCan + "): ");
		String recordName = con.readLine();
		if(recordName.trim().equals("")) {
			recordName = recordNameCan;
		}
		System.out.println(recordName);
		
		source = source.replace("##SERVICE##", svcName);
		source = source.replace("##METHOD##", methodName);
		source = source.replace("##SQL##", methodName);
		source = source.replace("##PARAMS##", canonicalParams);
		source = source.replace("##PARAMS_PRINT##", paramsPrint);
		source = source.replace("##PARAM1##", param1);
		source = source.replace("##PARAM2##", param2);
		source = source.replace("##RESULT##", resultName);
		source = source.replace("##RECORD##", recordName);
		
		System.out.println("**************************************************************\n");
		System.out.println(source);
		System.out.println("**************************************************************\n");
		
		DBTableToClass.main(new String[]{resultName, recordName, methodName, param1});
	}
}
