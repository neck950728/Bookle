package admin.adminRental;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import admin.adminSearch.BookSearch;
import admin.adminSearch.MemberSearch;
import admin.adminSearch.ReservationSearch;

public class RentalInsert extends JDialog {

	private RentalList parent = null;
	private RentalInsert self = this;
	
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel panelGeneral = new JPanel();
	private JPanel panelReservation = new JPanel();

	private JLabel labelDate = new JLabel("�뿩 ��¥ : ");
	private JTextField fieldDate = new JTextField();
	private JButton buttonDate = new JButton(search.Main.getScaledImageIcon("refresh.png"));
	private JLabel labelRentalBook = new JLabel("�뿩 ���� ID : ");
	private JTextField fieldRentalBook = new JTextField();
	private JButton buttonSearchBook = new JButton("�˻�");
	private JLabel labelRentalMember = new JLabel("�뿩 ȸ�� ID : ");
	private JTextField fieldRentalMember = new JTextField();
	private JButton buttonSearchMember = new JButton("�˻�");

	private JButton buttonGeneralInsert = new JButton("�Է�");
	private JButton buttonGeneralExit = new JButton("�ݱ�");
	private JPanel panelGeneralButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	private JLabel labelDate_reserved = new JLabel("�뿩 ��¥ : ");
	private JTextField fieldDate_reserved = new JTextField();
	private JButton buttonDate_reserved = new JButton(search.Main.getScaledImageIcon("refresh.png"));
	private JLabel labelRentalBook_reserved = new JLabel("�뿩 ���� ID : ");
	private JTextField fieldRentalBook_reserved = new JTextField();
	private JLabel labelRentalMember_reserved = new JLabel("�뿩 ȸ�� ID : ");
	private JTextField fieldRentalMember_reserved = new JTextField();
	
	private JButton buttonReservationInsert = new JButton("�Է�");
	private JButton buttonReservationExit = new JButton("�ݱ�");
	private JPanel panelReservationButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	private String reserved_date = null;

	private Socket sock = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;

	public void setFieldRentalBook(String book_id) {
		fieldRentalBook.setText(book_id);
	}

	public void setFieldRentalMember(String member_id) {
		fieldRentalMember.setText(member_id);
	}
	
	public void setFieldRentalBookReserved(String book_id) {
		fieldRentalBook_reserved.setText(book_id);
	}
	
	public void setFieldRentalMemberReserved(String member_id) {
		fieldRentalMember_reserved.setText(member_id);
	}
	
	public void setReservedDate(String reserved_date) {
		this.reserved_date = reserved_date;
	}

	private void socketInit() throws Exception {
		dos = new DataOutputStream(sock.getOutputStream());
		dis = new DataInputStream(sock.getInputStream());
	}

	private void compInit() {
		this.tabbedPane.add("�Ϲ� �뿩 �Է�", panelGeneral);
		this.tabbedPane.add("���� �뿩 �Է�", panelReservation);
		this.add(tabbedPane);
		
		this.panelGeneral.setLayout(new GridBagLayout());
		this.panelReservation.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		this.buttonDate.setPreferredSize(new Dimension(50, 32));
		this.fieldDate.setPreferredSize(new Dimension(130, 27));
		this.fieldRentalBook.setPreferredSize(new Dimension(130, 27));
		this.fieldRentalMember.setPreferredSize(new Dimension(130, 27));
		this.buttonDate_reserved.setPreferredSize(new Dimension(50, 32));
		this.fieldDate_reserved.setPreferredSize(new Dimension(130, 27));
		this.fieldRentalBook_reserved.setPreferredSize(new Dimension(130, 27));
		this.fieldRentalMember_reserved.setPreferredSize(new Dimension(130, 27));

		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.gridy = 0;
		this.panelGeneral.add(labelDate, gbc);
		gbc.gridx = 1;
		this.panelGeneral.add(fieldDate, gbc);
		gbc.gridx = 2;
		this.panelGeneral.add(buttonDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		this.panelGeneral.add(labelRentalBook, gbc);
		gbc.gridx = 1;
		this.panelGeneral.add(fieldRentalBook, gbc);
		gbc.gridx = 2;
		this.panelGeneral.add(buttonSearchBook, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		this.panelGeneral.add(labelRentalMember, gbc);
		gbc.gridx = 1;
		this.panelGeneral.add(fieldRentalMember, gbc);
		gbc.gridx = 2;
		this.panelGeneral.add(buttonSearchMember, gbc);

		gbc.insets = new Insets(30, 0, 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.panelGeneralButtons.add(buttonGeneralInsert);
		this.panelGeneralButtons.add(buttonGeneralExit);
		this.panelGeneral.add(panelGeneralButtons, gbc);
		
		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.panelReservation.add(labelDate_reserved, gbc);
		gbc.gridx = 1;
		this.panelReservation.add(fieldDate_reserved, gbc);
		gbc.gridx = 2;
		this.panelReservation.add(buttonDate_reserved, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.panelReservation.add(labelRentalBook_reserved, gbc);
		gbc.gridx = 1;
		this.panelReservation.add(fieldRentalBook_reserved, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.panelReservation.add(labelRentalMember_reserved, gbc);
		gbc.gridx = 1;
		this.panelReservation.add(fieldRentalMember_reserved, gbc);
		
		gbc.insets = new Insets(30, 0, 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.panelReservationButtons.add(buttonReservationInsert);
		this.panelReservationButtons.add(buttonReservationExit);
		this.panelReservation.add(panelReservationButtons, gbc);
	}

	private void eventInit() {
		this.buttonDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dos.writeUTF("get_date");
					dos.flush();
					long time = dis.readLong();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String currentDate = sdf.format(new Date(time));
					fieldDate.setText(currentDate);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		this.buttonSearchBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String keyword = fieldRentalBook.getText().trim();
				BookSearch bs = new BookSearch(self, sock, keyword);
				bs.setVisible(true);
			}
		});

		this.buttonSearchMember.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String keyword = fieldRentalMember.getText().trim();
				MemberSearch ms = new MemberSearch(self, sock, keyword);
				ms.setVisible(true);
			}
		});

		this.buttonGeneralInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fieldDate.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�뿩�� ��¥�� �Է��ϼ���.");
				} else if (fieldRentalBook.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�뿩�� ���� ID�� �Է��ϼ���.");
				} else if (fieldRentalMember.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�뿩�� ȸ�� ID�� �Է��ϼ���.");
				} else {
					try {
						dos.writeUTF("insert_rental");
						dos.writeUTF(fieldDate.getText().trim());
						dos.writeUTF(fieldRentalBook.getText().trim());
						dos.writeUTF(fieldRentalMember.getText().trim());
						dos.flush();

						int result = dis.readInt();
						if (result == 1) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� �����Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 2) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� ȸ���Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 3) {
							JOptionPane.showMessageDialog(self, "�뿩 ó���� �����߽��ϴ�. �ٽ� �õ����ּ���.");
						} else {
							JOptionPane.showMessageDialog(self, "�뿩 ó���Ǿ����ϴ�.");
							fieldDate.setText("");
							fieldRentalBook.setText("");
							fieldRentalMember.setText("");
							parent.listUpdate();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		this.buttonGeneralExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.buttonDate_reserved.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dos.writeUTF("get_date");
					dos.flush();
					long time = dis.readLong();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String currentDate = sdf.format(new Date(time));
					fieldDate_reserved.setText(currentDate);
					
					ReservationSearch rs = new ReservationSearch(self, sock);
					rs.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		this.buttonReservationInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fieldDate_reserved.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�뿩 ��¥�� �Է��ϼ���.");
				} else if (fieldRentalBook_reserved.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�뿩 ���� ID�� �Է��ϼ���.");
				} else if (fieldRentalMember_reserved.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�뿩 ȸ�� ID�� �Է��ϼ���.");
				} else {
					try {
						dos.writeUTF("insert_rental_reserved");
						dos.writeUTF(fieldDate_reserved.getText().trim());
						dos.writeUTF(reserved_date);
						dos.writeUTF(fieldRentalBook_reserved.getText().trim());
						dos.writeUTF(fieldRentalMember_reserved.getText().trim());
						dos.flush();

						int result = dis.readInt();
						if (result == 1) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� �����Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 2) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� ȸ���Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 3) {
							JOptionPane.showMessageDialog(self, "�뿩 ó���� �����߽��ϴ�. �ٽ� �õ����ּ���.");
						} else if (result == 4) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� ���������Դϴ�. �ٽ� �õ����ּ���.");
						} else {
							JOptionPane.showMessageDialog(self, "�뿩 ó���Ǿ����ϴ�.");
							fieldDate_reserved.setText("");
							fieldRentalBook_reserved.setText("");
							fieldRentalMember_reserved.setText("");
							parent.listUpdate();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		
		this.buttonReservationExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public RentalInsert(JPanel parent, Socket sock) {
		this.parent = (RentalList) parent;
		this.sock = sock;
		this.setTitle("���� �뿩 �Է�");
		this.setSize(350, 350);
		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);

		try {
			this.socketInit();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		this.compInit();
		this.eventInit();
	}

}
