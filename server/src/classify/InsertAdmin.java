package classify;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import database.BookleDB;
import server.BookleServer;
import table.Standby;

public class InsertAdmin {
	private Socket sock = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private String cmd = null;
	private BookleDB bookleDB = null;

	public InsertAdmin(Socket sock, DataInputStream dis, DataOutputStream dos, String cmd) {
		this.sock = sock;
		this.dis = dis;
		this.dos = dos;
		this.cmd = cmd;
	}

	public void reservation_standby_insert(HashMap userMap, int type) {
		// type 1 : ����
		// type 2 : ���
		bookleDB = new BookleDB();

		try {
			String reserved_date = null;

			if (type == 1) { // type�� 1�� ���
				reserved_date = dis.readUTF();
			}

			String book_id = dis.readUTF();
			String ip = this.sock.getInetAddress().toString();
			System.out.println(ip);
			String member_id = userMap.get(ip).toString();
			if (bookleDB.getBook(book_id) == null) { // �������� �ʴ� ���� ID : 1
				dos.writeInt(1);
				dos.flush();
			} else if (bookleDB.getMember(member_id) == null) { // �������� �ʴ� ȸ�� ID : 2
				dos.writeInt(2);
				dos.flush();
			} else if (bookleDB.getBookCount("standby", member_id) < 7) {
				int current_quantity = bookleDB.getCurrentQuantityOfBook(book_id);

				if (current_quantity > 0) { // ���� å�� ������ 1�� �̻��� ��
					System.out.println("��� 0 �̻��Դϴ�. ������ �����մϴ�.");
					int result1 = bookleDB.insertReservation(reserved_date, book_id, member_id);
					if (result1 > 0) {
						int result2 = bookleDB.updateBookQuantity(book_id, "dec");
						if (result2 > 0) { // ���� �Է�, ���� ��� ���� ���� : 0
							System.out.println("���� ����");
							dos.writeInt(0);
							dos.writeInt(bookleDB.getCurrentQuantityOfBook(book_id));
							dos.flush();
						} else {

						}
					} else { // �Է� ���� : 3
						dos.writeInt(3);
						dos.flush();
					}
				} else { // å�� ������ ���� ��
					System.out.println("��� 0�Դϴ�. ����� ��ȣ�� �̽��ϴ�.");
					int total = bookleDB.getBook(book_id).getTotal_quantity();
					int result[] = new int[2];
					result = bookleDB.insertStandby(book_id, member_id, total);
					if (result[0] > 0) {
						System.out.println("�ϴ� ����");
						dos.writeInt(0);
						dos.writeInt(result[1]);
					} else {
						System.out.println("���� �Ұ���");
						dos.writeInt(3); // ���� �Ұ��� : 3
						dos.writeInt(-1);
					}
				}
			} else {
				dos.writeInt(4);
				dos.writeInt(0);
				dos.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void return_insert() {
		bookleDB = new BookleDB();

		try {
			String return_date = dis.readUTF();
			String rental_date = dis.readUTF();
			String book_id = dis.readUTF();
			String member_id = dis.readUTF();
			if (bookleDB.getBook(book_id) == null) { // �������� �ʴ� ���� ID : 1
				dos.writeInt(1);
				dos.flush();
			} else if (bookleDB.getMember(member_id) == null) { // �������� �ʴ� ȸ�� ID : 2
				dos.writeInt(2);
				dos.flush();
			} else {
				if (bookleDB.getRentalInfo(rental_date, book_id, member_id) == null) { // �������� �ʴ� �뿩 ���� : 4
					dos.writeInt(4);
					dos.flush();
				} else {
					int result1 = bookleDB.updateInventoryManagement(rental_date, return_date, book_id, member_id);
					if (result1 > 0) {
						int result2 = bookleDB.updateBookQuantity(book_id, "inc");
						if (result2 > 0) {
							int result3 = bookleDB.updateMemberRental(member_id, "dec");
							if (result3 > 0) { // �ݳ� �Է�, ���� ��� ����, ȸ�� �뿩 ���� ���� ���� : 0
								// ���� ��� �����Կ� ���� �ش� ������ ���� ��Ⱑ �ִ��� Ȯ�� �� ó�� �ʿ�
								ArrayList<Standby> list = bookleDB.getStandbyList(book_id);
								if (list.size() > 0) {
									String first_member_id = list.get(0).getMember_id();
									long time = BookleServer.time;
									time += (86400000 * 3);
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
									String reserved_date = sdf.format(new Date(time));
									bookleDB.insertReservation(reserved_date, book_id, first_member_id);
									bookleDB.updateBookQuantity(book_id, "dec");
									bookleDB.deleteStandbyFirstMember(book_id, first_member_id);
									bookleDB.updateStandbyPriority(book_id);
								}
								dos.writeInt(0);
								dos.flush();
							} else {
								// �Է� �ʱ�ȭ �ؾ��� ��
							}
						} else {
							// �Է� �ʱ�ȭ �ؾ��� ��
						}
					} else { // �Է� ���� : 3
						dos.writeInt(3);
						dos.flush();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rental_insert() {
		bookleDB = new BookleDB();

		try {
			String rental_date = dis.readUTF();
			String book_id = dis.readUTF();
			String member_id = dis.readUTF();
			if (bookleDB.getBook(book_id) == null) { // �������� �ʴ� ���� ID : 1
				dos.writeInt(1);
				dos.flush();
			} else if (bookleDB.getMember(member_id) == null) { // �������� �ʴ� ȸ�� ID : 2
				dos.writeInt(2);
				dos.flush();
			} else if (bookleDB.getBookCount("inventory_management", member_id) < 7) { // ------------>���� �κ�
				int result1 = bookleDB.insertInventoryManagement(rental_date, "�뿩 ��", book_id, member_id);
				if (result1 > 0) {
					int result2 = bookleDB.updateBookQuantity(book_id, "dec");
					if (result2 > 0) {
						int result3 = bookleDB.updateMemberRental(member_id, "inc");
						if (result3 > 0) { // �뿩 �Է�, ���� ��� ����, ȸ�� �뿩 ���� ���� ���� : 0
							dos.writeInt(0);
							dos.flush();
						} else {
							// �Է� �ʱ�ȭ �ؾ��� ��
						}
					} else {
						// �Է� �ʱ�ȭ �ؾ��� ��
					}
				} else { // �Է� ���� : 3
					dos.writeInt(3);
					dos.flush();
				}
			} else {
				dos.writeInt(3);
				dos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rental_reserved_insert() {
		bookleDB = new BookleDB();

		try {
			String rental_date = dis.readUTF();
			String reserved_date = dis.readUTF();
			String book_id = dis.readUTF();
			String member_id = dis.readUTF();
			if (bookleDB.getBook(book_id) == null) { // �������� �ʴ� ���� ID : 1
				dos.writeInt(1);
				dos.flush();
			} else if (bookleDB.getMember(member_id) == null) { // �������� �ʴ� ȸ�� ID : 2
				dos.writeInt(2);
				dos.flush();
			} else if (bookleDB.getReservation(reserved_date, book_id, member_id) == null) { // �������� �ʴ� ���� ���� : 4
				dos.writeInt(4);
				dos.flush();
			} else {
				int result1 = bookleDB.insertInventoryManagement(rental_date, "�뿩 ��", book_id, member_id);
				if (result1 > 0) {
					int result3 = bookleDB.updateMemberRental(member_id, "inc");
					if (result3 > 0) {
						int result4 = bookleDB.deleteReservation(reserved_date, book_id, member_id);
						if (result4 > 0) { // �뿩 �Է�, ȸ�� �뿩 ���� ����, ���� ���� ���� ���� : 0
							dos.writeInt(0);
							dos.flush();
						} else {
							// �Է� �ʱ�ȭ �ؾ��� ��
						}
					} else {
						// �Է� �ʱ�ȭ �ؾ��� ��
					}
				} else { // �Է� ���� : 3
					dos.writeInt(3);
					dos.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void book_insert() {
		bookleDB = new BookleDB();

		try {
			String id = dis.readUTF();
			String title = dis.readUTF();
			String writer = dis.readUTF();
			String publisher = dis.readUTF();
			String category = dis.readUTF();
			int quantity = dis.readInt();

			if (bookleDB.getBook(id) != null) { // �ߺ��� ���� ID : 1
				dos.writeInt(1);
				dos.flush();
			} else {
				int result = bookleDB.insertBook(id, title, writer, publisher, category, quantity);
				if (result > 0) { // �Է� ���� : 0
					dos.writeInt(0);
					dos.flush();
				} else { // �Է� ���� : 2
					dos.writeInt(2);
					dos.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exe_command(HashMap userMap) {
		if (cmd.endsWith("rental")) {
			rental_insert();
		} else if (cmd.endsWith("rental_reserved")) {
			rental_reserved_insert();
		} else if (cmd.endsWith("return")) {
			return_insert();
		} else if (cmd.endsWith("reservation")) {
			reservation_standby_insert(userMap, 1);
		} else if (cmd.endsWith("standby")) {
			reservation_standby_insert(userMap, 2);
		} else if (cmd.endsWith("book")) {
			book_insert();
		} else {

		}
	}
}
