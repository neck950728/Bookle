package admin.adminBook;

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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import admin.adminSearch.BookSearch;

public class BookInsert extends JDialog {

	private BookList parent = null;
	private BookInsert self = this;

	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel panelNew = new JPanel();
	private JPanel panelExisting = new JPanel();

	private JLabel labelCategory = new JLabel("ī�װ� : ");
	private JComboBox<String> comboCategory = new JComboBox<>();
	private JLabel labelNewBook = new JLabel("�ű� ���� ID : ");
	private JTextField fieldNewBook = new JTextField();
	private JLabel labelTitle = new JLabel("���� : ");
	private JTextField fieldTitle = new JTextField();
	private JLabel labelWriter = new JLabel("���� : ");
	private JTextField fieldWriter = new JTextField();
	private JLabel labelPublisher = new JLabel("���ǻ� : ");
	private JTextField fieldPublisher = new JTextField();
	private JLabel labelNewQuantity = new JLabel("���� : ");
	private JTextField fieldNewQuantity = new JTextField();

	private JButton buttonNewInsert = new JButton("�Է�");
	private JButton buttonNewExit = new JButton("�ݱ�");
	private JPanel panelNewButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));

	private JLabel labelExistingBook = new JLabel("���� ���� ID : ");
	private JTextField fieldExistingBook = new JTextField();
	private JButton buttonSearchBook = new JButton("�˻�");
	private JLabel labelExistingQuantity = new JLabel("���� : ");
	private JTextField fieldExistingQuantity = new JTextField();

	private JButton buttonExistingInsert = new JButton("�Է�");
	private JButton buttonExistingExit = new JButton("�ݱ�");
	private JPanel panelExistingButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));

	private Socket sock = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;

	public void setFieldExistingBook(String book_id) {
		fieldExistingBook.setText(book_id);
	}

	public boolean isStringInt(String str) {
		try {
			int quantity = Integer.parseInt(str);
			if (quantity > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private void socketInit() throws Exception {
		dos = new DataOutputStream(sock.getOutputStream());
		dis = new DataInputStream(sock.getInputStream());
	}

	private void compInit() {
		this.tabbedPane.add("�ű� ���� �Է�", panelNew);
		this.tabbedPane.add("���� ���� �Է�", panelExisting);
		this.add(tabbedPane);

		this.panelNew.setLayout(new GridBagLayout());
		this.panelExisting.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		this.fieldNewBook.setPreferredSize(new Dimension(130, 27));
		this.fieldTitle.setPreferredSize(new Dimension(130, 27));
		this.fieldWriter.setPreferredSize(new Dimension(130, 27));
		this.fieldPublisher.setPreferredSize(new Dimension(130, 27));
		this.fieldNewQuantity.setPreferredSize(new Dimension(130, 27));
		this.fieldExistingBook.setPreferredSize(new Dimension(130, 27));
		this.fieldExistingQuantity.setPreferredSize(new Dimension(130, 27));

		comboCategory.addItem("����");
		comboCategory.addItem("��ȭ");
		comboCategory.addItem("�Ҽ�");
		comboCategory.addItem("��/������");
		comboCategory.addItem("����/��ȭ");
		comboCategory.addItem("�ι�");
		comboCategory.addItem("��ǻ��/IT");

		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.gridy = 0;
		this.panelNew.add(labelCategory, gbc);
		gbc.gridx = 1;
		this.panelNew.add(comboCategory, gbc);
		gbc.gridx = 2;

		gbc.gridx = 0;
		gbc.gridy = 1;
		this.panelNew.add(labelNewBook, gbc);
		gbc.gridx = 1;
		this.panelNew.add(fieldNewBook, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		this.panelNew.add(labelTitle, gbc);
		gbc.gridx = 1;
		this.panelNew.add(fieldTitle, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		this.panelNew.add(labelWriter, gbc);
		gbc.gridx = 1;
		this.panelNew.add(fieldWriter, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		this.panelNew.add(labelPublisher, gbc);
		gbc.gridx = 1;
		this.panelNew.add(fieldPublisher, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		this.panelNew.add(labelNewQuantity, gbc);
		gbc.gridx = 1;
		this.panelNew.add(fieldNewQuantity, gbc);

		gbc.insets = new Insets(30, 0, 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 6;
		this.panelNewButtons.add(buttonNewInsert);
		this.panelNewButtons.add(buttonNewExit);
		this.panelNew.add(panelNewButtons, gbc);

		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.panelExisting.add(labelExistingBook, gbc);
		gbc.gridx = 1;
		this.panelExisting.add(fieldExistingBook, gbc);
		gbc.gridx = 2;
		this.panelExisting.add(buttonSearchBook, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		this.panelExisting.add(labelExistingQuantity, gbc);
		gbc.gridx = 1;
		this.panelExisting.add(fieldExistingQuantity, gbc);

		gbc.insets = new Insets(30, 0, 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.panelExistingButtons.add(buttonExistingInsert);
		this.panelExistingButtons.add(buttonExistingExit);
		this.panelExisting.add(panelExistingButtons, gbc);
	}

	private void eventInit() {
		this.comboCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dos.writeUTF("get_lastId");
					dos.writeUTF((String) comboCategory.getSelectedItem());
					dos.flush();

					String lastId = dis.readUTF();
					fieldNewBook.setText(lastId);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		this.buttonNewInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fieldNewBook.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�ű� ���� ID�� �Է��ϼ���.");
				} else if (fieldTitle.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�ű� ���� ������ �Է��ϼ���.");
				} else if (fieldWriter.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�ű� ���� ���ڸ� �Է��ϼ���.");
				} else if (fieldPublisher.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "�ű� ���� ���ǻ縦 �Է��ϼ���.");
				} else if (fieldNewQuantity.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "������ �Է��ϼ���.");
				} else if (!isStringInt(fieldNewQuantity.getText().trim())) {
					JOptionPane.showMessageDialog(self, "�߸��� �����Դϴ�. �ٽ� Ȯ�����ּ���.");
				} else {
					try {
						dos.writeUTF("insert_book");
						dos.writeUTF(fieldNewBook.getText().trim());
						dos.writeUTF(fieldTitle.getText().trim());
						dos.writeUTF(fieldWriter.getText().trim());
						dos.writeUTF(fieldPublisher.getText());
						dos.writeUTF((String) comboCategory.getSelectedItem());
						dos.writeInt(Integer.parseInt(fieldNewQuantity.getText().trim()));
						dos.flush();

						int result = dis.readInt();
						if (result == 1) {
							JOptionPane.showMessageDialog(self, "�ߺ��� ���� ID�Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 2) {
							JOptionPane.showMessageDialog(self, "�Է� ó���� �����߽��ϴ�. �ٽ� �õ����ּ���.");
						} else {
							JOptionPane.showMessageDialog(self, "�Է� ó���Ǿ����ϴ�.");
							fieldNewBook.setText("");
							fieldTitle.setText("");
							fieldWriter.setText("");
							fieldPublisher.setText("");
							fieldNewQuantity.setText("");
							parent.listUpdate();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		this.buttonNewExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.buttonSearchBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String keyword = fieldExistingBook.getText().trim();
				BookSearch bs = new BookSearch(self, sock, keyword);
				bs.setVisible(true);
			}
		});

		this.buttonExistingInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fieldExistingBook.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "���� ���� ID�� �Է��ϼ���.");
				} else if (fieldExistingQuantity.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(self, "������ �Է��ϼ���.");
				} else if (!isStringInt(fieldExistingQuantity.getText().trim())) {
					JOptionPane.showMessageDialog(self, "�߸��� �����Դϴ�. �ٽ� Ȯ�����ּ���.");
				} else {
					try {
						dos.writeUTF("update_book");
						dos.writeUTF(fieldExistingBook.getText().trim());
						dos.writeInt(Integer.parseInt(fieldExistingQuantity.getText().trim()));
						dos.flush();

						int result = dis.readInt();
						if (result == 1) {
							JOptionPane.showMessageDialog(self, "�������� �ʴ� �����Դϴ�. �ٽ� Ȯ�����ּ���.");
						} else if (result == 2) {
							JOptionPane.showMessageDialog(self, "�Է� ó���� �����߽��ϴ�. �ٽ� �õ����ּ���.");
						} else {
							JOptionPane.showMessageDialog(self, "�Է� ó���Ǿ����ϴ�.");
							fieldExistingBook.setText("");
							fieldExistingQuantity.setText("");
							parent.listUpdate();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		this.buttonExistingExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public BookInsert(JPanel parent, Socket sock) {
		this.parent = (BookList) parent;
		this.sock = sock;
		this.setTitle("���� �Է�");
		this.setSize(400, 500);
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
