package classify;
// pwchange ����
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import database.BookleDB;
import server.BookleServer;
import table.Member;

public class Account {
	
	private Socket sock = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private String cmd = null;
//	private HashMap<String, String> userMap;
	private BookleDB bookleDB = null;
	private Member mb = null;
	private String user_id = null;
	
	public Account(Socket sock, DataInputStream dis, DataOutputStream dos, String cmd) {
		this.sock= sock;
		this.dis = dis;
		this.dos = dos;
		this.cmd = cmd;
	}
	

	
	private void info(HashMap userMap, int type) {
		System.out.println("==== ���� �Ǵ� �뿩 ���� ����Ʈ ====");
		// type 1 : reserved data, type 2 : rental data
		bookleDB = new BookleDB();
		String ip = this.sock.getInetAddress().toString();
		String user_id = userMap.get(ip).toString();
		String sql = null;
		ArrayList<String[]> result = null;
		
		if(type == 1) {
			// ����� ������Ȳ
			System.out.println("---������Ȳ ����---");
			sql = "select b.id, b.title, b.writer, b.publisher, r.reserved_date from book b, reservation r where r.member_id='" + user_id + "' and b.id=r.book_id";
			result = bookleDB.select_info(sql);
			
		} else if (type == 2) {
			// ����� �뿩��Ȳ
			System.out.println("---�뿩��Ȳ ����---");
			sql = "select b.id, b.title, b.writer, b.publisher, i.io_date from book b, inventory_management i where i.member_id='" + user_id + "' and b.id=i.book_id";
			result = bookleDB.select_info(sql);
		}
		
		try {
			int size = 0;
			size = result.size();
			dos.writeInt(size);
			dos.flush();
			
			String msg = "";
			for(String[] tmp : result) {
				for(int i = 0; i < tmp.length; i++) {
					if(i != 4) {
						System.out.println(tmp[i]);
						msg += tmp[i] + "|";
					}
					else {
						msg += tmp[i];
					}
				}
				System.out.println("������ �޼��� : " + msg);
				dos.writeUTF(msg);
			}
		} catch (Exception e) {
			try {
				dos.writeUTF("error");
			} catch(Exception e1) {}
		}
		//bookleDB.closeConnection();
	}
	
	private void pw_change(HashMap userMap) {
		bookleDB = new BookleDB();

		try {
			String present_pw = null;
			String ip = this.sock.getInetAddress().toString();
			String id_ = userMap.get(ip).toString();
			System.out.println("������ id : " + id_);
			System.out.println("=== pw �����ϱ� ===");
			while (true) {
				// flag 1 : ���� ��й�ȣ �Է�â
				// flag 2 : ���ο� ��й�ȣ �Է�â
				// flag 0 : cancel
				int flag = dis.readInt();
				System.out.println("flag : " + flag);
				
				if(flag == 1) {
					System.out.println("===���� ��й�ȣ ��ġ Ȯ���� ���� â===");
					String pw_ = dis.readUTF(); // ���� ���
					
						System.out.println("���� ����ڰ� �Է��� pw : " + pw_); // ���� pw �Է� ����

						String sql = "select * from member where id='" + id_ + "'";
						ArrayList<Member> result = bookleDB.selectMemberData(sql);
						present_pw = result.get(0).getPw(); // ����� pw db�κ��� �޾ƿ�
						
						if (pw_.equals(result.get(0).getPw())) { // �Էµ� pw�� ������� pw�� ��
							System.out.println("�Է��� ��й�ȣ�� ����� ��й�ȣ�� ��ġ�մϴ�.");
							dos.writeUTF("inputnewpw"); // ������ ���ο� pw�Է��� ���� msg ����
							dos.flush();
						} else {
							dos.writeUTF("pw_fail");
							dos.flush();
						}
					
				} else if(flag == 2) {
					System.out.println("===���ο� ��й�ȣ�� �Է��ϴ� â===");
					String new_pw_ = dis.readUTF(); // ���ο� pw�� �Է� ����
					
					if (present_pw.equals(new_pw_)) { // ������� ���� pw�� ������ pw�� ���� ��� �ߺ� ó��
						System.out.println("����� pw�� ���� �Է��� pw�� �ߺ�");
						dos.writeUTF("pwduplicate"); // ���� �߻�
						dos.flush();
					} else {
						System.out.println("������ pw : " + new_pw_);
						
						String sql = "update member set pw='" + new_pw_ + "' where id='" + id_ + "'";
						if (bookleDB.update(sql) == 1) {
							dos.writeUTF("success");
							dos.flush();
							break;
						} else {
							System.out.println("db�� ���� ����");
							dos.writeUTF("change_error");
							dos.flush();
							break;
						}
						
					}
				} else if(flag == 0) {
					System.out.println("â ����");
					break;
				}
			}
			
		} catch (Exception e) {
			try {
				dos.writeUTF("change_fail");
				dos.flush();
			} catch (Exception e1) {
				System.out.println("���� ����");
			}
		}
		//bookleDB.closeConnection();
	}

	
	private void mypage(HashMap userMap) {
		//����� id, �̸�, �뿩��Ȳ, ���� ��Ȳ
		bookleDB = new BookleDB();
		
		try {
			String ip = this.sock.getInetAddress().toString();
			user_id = userMap.get(ip).toString();
			System.out.println("(account)user id : " + user_id);
			// ������� id�� �̸�
			String sql = "select * from member where id='" + user_id + "'";
			ArrayList<Member> mem = bookleDB.selectMemberData(sql);
			String id_ = mem.get(0).getId(); String name_ = mem.get(0).getName();
			String birth_ = mem.get(0).getBirth(); String phone_ = mem.get(0).getPhone();
			String email_ = mem.get(0).getE_mail(); String date_ = mem.get(0).getJoin_date();
			int current_ = mem.get(0).getCurrent_rental(); int possible_ = mem.get(0).getPossible_rental();
			String user_info_msg = id_ + "|" + name_ + "|" + birth_ + "|" + phone_ + "|" + email_ + "|" + date_ + "|" + current_ + "|" + possible_;
			
			dos.writeUTF(user_info_msg); // id|name
			dos.flush();
			//-------------------------------------------------------------------------
//			// ����� �뿩��Ȳ
//			sql = null;
//			sql = "select b.title, i.io_date from book b, inventory_management i where i.member_id='"+ id_ +"' and b.id=i.book_id";
//			ArrayList<String> inventory_info = new ArrayList<>();
//			inventory_info = bookleDB.selectJoinTitleDate(sql);
//			
//			int size = inventory_info.size();
//			dos.writeInt(size);
//			dos.flush();			
//			for(String tmp : inventory_info) {
//				dos.writeUTF(tmp);
//				dos.flush();
//			}
			
//			// ����� ������Ȳ
//			sql = null;
//			sql = "select title from book b, reservation_management r where r.member_id='"+ user_id +"' and b.id=r.book_id";
//			ArrayList<String> reservation_info = new ArrayList<>();
//			reservation_info = bookleDB.selectJoinTitle(sql);
//			
//			size = reservation_info.size();
//			dos.writeInt(size);
//			dos.flush();
//			for(String tmp : reservation_info) {
//				dos.writeUTF(tmp);
//				dos.flush();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//bookleDB.closeConnection();
	}
	
	private void user_pw_search(){
		bookleDB = new BookleDB();
		try {

			String id_ = dis.readUTF();
			String birth_ = dis.readUTF();
			String name_ = dis.readUTF();
			String question_ = dis.readUTF();
			String answer_ = dis.readUTF();

			String sql = "select * from member where id='" + id_ + "' and birth='" + birth_ + "' and name='" + name_ + "'and question='" + question_ + "' and answer='" + answer_ + "'";
			ArrayList<Member> mem = bookleDB.selectMemberData(sql);

			Random rnd =new Random();
			StringBuffer buf =new StringBuffer();
			
			if (mem.isEmpty()) {
				dos.writeUTF("not_exist_id");
				dos.flush();
			} else {
				for(int i=0;i<6;i++){
				    // rnd.nextBoolean() �� �������� true, false �� ����. true�� �� ���� �� �ҹ��ڸ�, false �� �� ���� �� ���ڸ� StringBuffer �� append �Ѵ�.
				    if(rnd.nextBoolean()){
				        buf.append((char)((int)(rnd.nextInt(26))+97));
				    }else{
				        buf.append((rnd.nextInt(10)));
				    }
				}

				String new_pw = buf.toString();
				
				dos.writeUTF(new_pw);
				dos.flush();
				//update member set pw='new_pw' where id='id_'
				sql = "update member set pw='" + new_pw + "' where id='" + id_ + "'";
				
				int result = bookleDB.update(sql);
				if (result == 1) {
					dos.writeUTF("success");
					dos.flush();
				} else if (result == 0) {
					dos.writeUTF("pw_change_fail");
					dos.flush();
				}

			}
		} catch (Exception e) {
			try {
				dos.writeUTF("pw_search_error");
				dos.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//bookleDB.closeConnection();
	}
	
	private void user_id_search() {
		bookleDB = new BookleDB();
		try {
			String name_ = dis.readUTF();
			String birth_ = dis.readUTF();
			
			System.out.println(name_ + " " + birth_);
			// Member ���̺��� name�� birth�� ���� ����� id select
			// sql = select id from Member where name=name, birth=birth
			String sql = "select * from member where name='" + name_ + "'and birth='" + birth_ + "'";
			ArrayList<Member> mem = bookleDB.selectMemberData(sql);
			if (mem.isEmpty()) { // ã�� id�� ����
				dos.writeUTF("not_found");
				dos.flush();
			} else {
				String user_id = mem.get(0).getId();
				dos.writeUTF(user_id);
				dos.flush();
			}
		} catch (Exception e) {
			try {
				dos.writeUTF("id_search_error");
				dos.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//bookleDB.closeConnection();
	}
	
	private void id_duplicate_check() {
		/*
		 * id �ߺ� Ȯ�� -> id_duplicate
		 */

		bookleDB = new BookleDB();
		try {
			String id_ = dis.readUTF();
			ArrayList<Member> mem = bookleDB.selectMemberData("select * from member where id='" + id_ + "'");
			
			if(mem.isEmpty()) {
				dos.writeUTF("success");
				dos.flush();
			} else {
				System.out.println("id �ߺ�");
				dos.writeUTF("id_duplicate");
				dos.flush();
			}
		} catch (Exception e) {
			try {
				dos.writeUTF("duplicate_error");
				dos.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//bookleDB.closeConnection();
	}
	
	private void login(HashMap userMap){
		BookleDB bookleDB = new BookleDB();
		try {
			String id_ = dis.readUTF();
			String pw_ = dis.readUTF();

			String sql = "select * from member where id='" + id_ + "'";
			ArrayList<Member> result = bookleDB.selectMemberData(sql);

			if (result.isEmpty()) {
				dos.writeUTF("id_error");
				dos.flush();
			} else {
				// �н����� Ȯ�� �ϴ� ��� ã�� ***
				if (pw_.equals(result.get(0).getPw())) { // �α��� ����
					if(id_.equals("admin")) {
						dos.writeUTF("admin");
						dos.flush();
					} else {
						dos.writeUTF("general");
						
						String name_ = result.get(0).getName();
						dos.writeUTF(name_);
						
						String email_ = result.get(0).getE_mail();
						if (email_ == null) {
							email_ = "";
						}
						dos.writeUTF(email_);
						
						String gender_ = result.get(0).getGender();
						dos.writeUTF(gender_);
						dos.flush();
					}
					
					String ip = sock.getInetAddress().toString();
					System.out.println("==== �α��� ====");
					System.out.println("ip : " + ip + ", id : " + id_ + "�� �α��� ����");
					userMap.put(ip, id_);
					
				} else { // �α��� ����
					dos.writeUTF("pw_error");
					dos.flush();
				}
			}
		} catch (Exception e) {
			try {
				dos.writeUTF("login_error");
				dos.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//bookleDB.closeConnection();
	}
	
	private void logout(HashMap userMap) {
		userMap.remove(this.sock.getInetAddress().toString());
		try {
			System.out.println(this.sock.getInetAddress() + " ����");
			this.sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//bookleDB.closeConnection();
	}
	
	private void memberJoin() {
		bookleDB = new BookleDB();
		
		long time = BookleServer.time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String today = sdf.format(new Date(time));
		
		try {
			// client�κ��� ȸ�� ������ receive
			// split ����ϴ� ��� �����ϱ�
			String id_ = dis.readUTF();
			String psw_ = dis.readUTF();
			String name_ = dis.readUTF();
			String gender_ = dis.readUTF();
			String birth_ = dis.readUTF();
			String phone_ = dis.readUTF();
			String e_mail_ = dis.readUTF();
			if(e_mail_.length() == 0) {
				e_mail_ = "";
			}
			String question_ = dis.readUTF();
			String answer_ = dis.readUTF();
			String join_date_ = today;
			
			int current_rental = 0; int possible_rental = 7; 
			mb = new Member(id_, psw_, name_, gender_, birth_, phone_, e_mail_, question_, answer_, today, current_rental, possible_rental);
			
			int result = bookleDB.insertMemberData(mb);

			if (result == 1) {
				System.out.println("���� �Ϸ�");
				dos.writeUTF("success");
				dos.flush();
			} else {
				System.out.println("���� ����");
				dos.writeUTF("join_fail");
				dos.flush();
			}
		} catch (Exception e) {
			try {
				dos.writeUTF("join_error");
				dos.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//bookleDB.closeConnection();
	}
	
	private void phoneAuth() {
		try {
			bookleDB = new BookleDB();
			String userId = dis.readUTF();
			
			String[] result = bookleDB.phoneAuth(userId);	
			dos.writeUTF(result[0]);
			dos.writeUTF(result[1]);
			dos.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
		//bookleDB.closeConnection();
	}
	
	public void exe_command(HashMap userMap) {
		if(cmd.endsWith("join")) {
			memberJoin();
		} else if(cmd.endsWith("logout")) {
			logout(userMap);
		} else if(cmd.endsWith("login")) {
			login(userMap);
		} else if(cmd.endsWith("idcheck")) {
			id_duplicate_check();
		} else if(cmd.endsWith("idsearch")) {
			user_id_search();
		} else if(cmd.endsWith("pwsearch")) {
			user_pw_search();
		} else if(cmd.endsWith("mypage")) {
			mypage(userMap);
		} else if(cmd.endsWith("pwchange")) {
			pw_change(userMap);
		} else if(cmd.endsWith("reservedinfo")) {
			info(userMap, 1);
		} else if(cmd.endsWith("rentalinfo")) {
			info(userMap, 2);
		} else if(cmd.endsWith("phoneAuth")){
			phoneAuth();
		}else {
			
		}
		
		
	}
	
}