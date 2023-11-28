package main;

import java.util.HashMap;

import org.json.simple.JSONObject;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public class SendAuthNum {
	private final String api_key = "NCS1YR5S5JOOAQAF";
	private final String api_secret = "I0CHQXOWPZMAQUIQO80AXPFRXNG8W5IN";
	private Message coolSms = new Message(api_key, api_secret);
	
	// ȸ�����Կ� ����
	public int send(String recipient_phoneNum) {
		int authNum = (int)(Math.random() * (999999 - 100000 + 1)) + 100000;
		String msg = "Bookle ���񽺸� �̿����ּż� �����մϴ� :)\n������ȣ�� [" + authNum + "]�Դϴ�.";
		
		HashMap<String, String> params = new HashMap<String, String>();
	    params.put("to", recipient_phoneNum);
	    params.put("from", "01057796682");
	    params.put("type", "SMS");
	    params.put("text", msg);
	    // params.put("app_version", "test app 1.2"); // application name and version

	    try {
	      JSONObject obj = (JSONObject)coolSms.send(params); // ������ȣ ����
	      System.out.println(obj.toString());
	    }catch(CoolsmsException e) { // ������ȣ ���� ����
	      System.out.println(e.getMessage());
	      System.out.println(e.getCode());
	    }
	    
	    return authNum;
	}
	
	
	// ��й�ȣ ã�⿡ ����
	public int send(String userName, String phoneNum) {
		int authNum = (int)(Math.random() * (999999 - 100000 + 1)) + 100000;
		String msg = userName + "��! Bookle ���񽺸� �̿����ּż� �����մϴ� :)\n������ȣ�� [" + authNum + "] �Դϴ�.";
		
		HashMap<String, String> params = new HashMap<String, String>();
	    params.put("to", phoneNum);
	    params.put("from", "01057796682");
	    params.put("type", "SMS");
	    params.put("text", msg);
	    // params.put("app_version", "test app 1.2"); // application name and version

	    try {
	      JSONObject obj = (JSONObject)coolSms.send(params); // ������ȣ ����
	      System.out.println(obj.toString());
	    }catch (CoolsmsException e) { // ������ȣ ���� ����
	      System.out.println(e.getMessage());
	      System.out.println(e.getCode());
	    }
	    
	    return authNum;
	}
}