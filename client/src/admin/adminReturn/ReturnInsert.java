package admin.adminReturn;

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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import admin.adminSearch.RentalSearch;

public class ReturnInsert extends JDialog {

	private ReturnList parent = null;
	private ReturnInsert self = this;

	private JLabel labelReturnDate = new JLabel("�ݳ� ��¥ : ");
	private JTextField fieldReturnDate = new JTextField();
	private JButton buttonReturnDate = new JButton(search.Main.getScaledImageIcon("refresh.png"));
	private JLabel labelRentalDate = new JLabel("�뿩 ��¥ : ");
	private JTextField fieldRentalDate = new JTextField();
	private JLabel labelReturnBook = new JLabel("�ݳ� ���� ID : ");
	private JTextField fieldReturnBook = new JTextField();
	private JLabel labelReturnMember = new JLabel("�ݳ� ȸ�� ID : ");
	private JTextField fieldReturnMember = new JTextField();

	private JButton buttonInsert = new JButton("�Է�");
	private JButton buttonExit = new JButton("�ݱ�");
	private JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));

	private Socket sock = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;

	public void setFieldRentalDate(String rental_date) {
		fieldRentalDate.setText(rental_date);
	}

	public void setFieldReturnBook(String book_id) {
		fieldReturnBook.setText(book_id);
	}

	public void setFieldReturnMember(String member_id) {
		fieldReturnMember.setText(member_id);
	}

	private void socketInit() throws Exception {
		dos = new DataOutputStream(sock.getOutputStream());
		dis = new DataInputStream(sock.getInputStream());
	}

	private void compInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		this.fieldReturnDate.setPreferredSize(new Dimension(130, 27));
		this.buttonReturnDate.setPreferredSize(new Dimension(50, 32));
		this.fieldRentalDate.setPreferredSize(new Dimension(130, 27));
		this.fieldReturnBook.setPreferredSize(new Dimension(130, 27));
		this.fieldReturnMember.setPreferredSize(new Dimension(130, 27));

		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.gridy = 0;
		this.add(labelReturnDate, gbc);
		gbc.gridx = 1;
		this.add(fieldReturnDate, gbc);
		gbc.gridx = 2;
		this.add(buttonReturnDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(labelRentalDate, gbc);
		gbc.gridx = 1;
		this.add(fieldRentalDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(labelReturnBook, gbc);
		gbc.gridx = 1;
		this.add(fieldReturnBook, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(labelReturnMember, gbc);
		gbc.gridx = 1;
		this.add(fieldReturnMember, gbc);

		gbc.insets = new Insets(30, 0, 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 4;
		this.panelButtons.add(buttonInsert);
		this.panelButtons.add(buttonExit);
		this.add(panelButtons, gbc);
	}

	private void eventInit() {
		this.buttonReturnDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dos.writeUTF("get_date");
					dos.flush();
					long time = dis.readLong();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String currentDate = sdf.format(new Date(time));
					fieldReturnDate.setText(currentDate);
					
					RentalSearch rs = new RentalSearch(self, sock);
					rs.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		this.buttonInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fieldReturnDate.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�ݳ��� ��¥�� �Է��ϼ���.");
				} else if (fieldRentalDate.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�뿩�ߴ� ��¥�� �Է��ϼ���.");
				} else if (fieldReturnBook.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�ݳ��� ���� ID�� �Է��ϼ���.");
				} else if (fieldReturnMember.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�ݳ��� ȸ�� ID�� �Է��ϼ���.");
				} else {
					try {
						dos.writeUTF("insert_return");
						dos.writeUTF(fieldReturnDate.getText().trim());
						dos.writeUTF(fieldRentalDate.getText().trim());
						dos.writeUTF(fieldReturnBook.getText().trim());
						dos.writeUTF(fieldReturnMember.getText().trim());
						dos.flush();

						int result = dis.readInt();
						if (result == 1) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� �����Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 2) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� ȸ���Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 3) {
							JOptionPane.showMessageDialog(self, "�ݳ� ó���� �����߽��ϴ�. �ٽ� �õ����ּ���.");
						} else if (result == 4) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� �뿩 �����Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else {
							JOptionPane.showMessageDialog(self, "�ݳ� ó���Ǿ����ϴ�.");
							fieldReturnDate.setText("");
							fieldRentalDate.setText("");
							fieldReturnBook.setText("");
							fieldReturnMember.setText("");
							parent.listUpdate();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		this.buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public ReturnInsert(JPanel parent, Socket sock) {
		this.parent = (ReturnList) parent;
		this.sock = sock;
		this.setTitle("���� �ݳ� �Է�");
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
