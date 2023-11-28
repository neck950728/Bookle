package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Content2Comp extends JPanel {
	// ��������
	private UserInfoDialog uid_addr;
	private Font f;
	private JPanel present_rsv_label_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JLabel present_rsv_label = new JLabel("���� ������Ȳ");
	private JLabel present_rsv_label_ment = new JLabel("ȸ������ ���� �������� å�� ������ �˷��ݴϴ�.");

	private String[] headers1 = new String[] { "û����ȣ", "����", "����", "���ǻ�", "������" };
	private DefaultTableModel dtm1 = new DefaultTableModel(headers1, 0);
	private JTable table1 = new JTable(dtm1);
	private JScrollPane present_rsv_scroll = new JScrollPane(table1);

	private JPanel present_standby_label_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JLabel present_standby_label = new JLabel("���� ��������Ȳ");
	private JLabel present_standby_label_ment = new JLabel("��� ����� å�� ���� ȸ������ �ο����� ��� ��Ȳ�Դϴ�. (�ִ� 7��)");

	private String[] headers2 = new String[] { "û����ȣ", "����", "����", "���ǻ�", "����ȣ" };
	private DefaultTableModel dtm2 = new DefaultTableModel(headers2, 0);
	private JTable table2 = new JTable(dtm2);
	private JScrollPane present_standby_scroll = new JScrollPane(table2);
	private JButton rsvCancelBtn = new JButton("�������");
	private JButton standbyCancelBtn = new JButton("������");
	private JPanel panelBtn = new JPanel();
	private JPanel panel = new JPanel();
	private String[] command = new String[5];
	private int rsvSize;
	private int standbySize;

	public void sockInit(DataInputStream sock_in, DataOutputStream sock_out) {
		try {
			sock_out.writeUTF("list_member_detail");
			sock_out.writeUTF(uid_addr.userid);
			sock_out.writeUTF("reservation");
			rsvSize = sock_in.readInt();
			System.out.println(rsvSize);
			for (int i = 0; i < rsvSize; i++) {
				command[0] = sock_in.readUTF();
				command[1] = sock_in.readUTF();
				command[2] = sock_in.readUTF();
				command[3] = sock_in.readUTF();
				command[4] = sock_in.readUTF();

				dtm1.addRow(new Object[] { command[1], command[2], command[3], command[4], command[0] });
			}
			// standby cmd ����
			sock_out.writeUTF("list_standby_m");
			sock_out.writeUTF(uid_addr.userid);
			standbySize = sock_in.readInt();
			System.out.println(standbySize);
			for (int i = 0; i < standbySize; i++) {
				command[0] = sock_in.readUTF();
				command[1] = sock_in.readUTF();
				command[2] = sock_in.readUTF();
				command[3] = sock_in.readUTF();
				command[4] = sock_in.readUTF();

				dtm2.addRow(new Object[] { command[0], command[1], command[2], command[3], command[4]});
			}
		} catch (Exception e) {
			System.out.println("���� �Է� ����");
		}
	}

	public void eventInit(DataInputStream sock_in, DataOutputStream sock_out) {
		this.rsvCancelBtn.addActionListener(new ActionListener() {
			// ������� ��ư�� ���� ���
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (table1.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "����� ������ ���� �������ּ���.");
				} else {
					try {
						int a = JOptionPane.showConfirmDialog(null,
								"�ش� å�� ������ ����Ͻðڽ��ϱ�?\n[" + dtm1.getValueAt(table1.getSelectedRow(), 1).toString()+"]",
								"�������", JOptionPane.YES_NO_OPTION);
						if (a == 0) {
							sock_out.writeUTF("delete_reservation_cancel");
							sock_out.writeUTF(uid_addr.userid);
							sock_out.writeUTF(dtm1.getValueAt(table1.getSelectedRow(), 0).toString());
							sock_out.writeUTF(dtm1.getValueAt(table1.getSelectedRow(), 4).toString());
							int num = sock_in.readInt();
							System.out.println("��� : " + num);
							if (num == 0) {
								for (int i = rsvSize - 1; i >= 0; i--) {
									dtm1.removeRow(i);
								}
								for (int i = standbySize - 1; i >= 0; i--) {
									dtm2.removeRow(i);
								}
								sockInit(sock_in, sock_out);
								JOptionPane.showMessageDialog(null, "������ ����߽��ϴ�.");
							} else {
								JOptionPane.showMessageDialog(null, "������ ������� ���߽��ϴ�.");
							}
						}
					} catch (Exception e1) {
					}
				}
			}
		});
		this.standbyCancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(table2.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "����� ������ ���� �������ּ���.");
				} else {
					try {
						int a = JOptionPane.showConfirmDialog(null,
								"�ش� å�� ������ ����Ͻðڽ��ϱ�?\n[" + dtm2.getValueAt(table2.getSelectedRow(), 1).toString()+"]",
								"�������", JOptionPane.YES_NO_OPTION);
						if (a == 0) {
							sock_out.writeUTF("delete_standby_cancel");
							sock_out.writeUTF(uid_addr.userid);
							sock_out.writeUTF(dtm2.getValueAt(table2.getSelectedRow(), 0).toString());
							sock_out.writeUTF(dtm2.getValueAt(table2.getSelectedRow(), 4).toString());
							
							int num = sock_in.readInt();
							if (num == 0) {
								System.out.println("��� ����!");
								for (int i = rsvSize - 1; i >= 0; i--) {
									dtm1.removeRow(i);
								}
								for (int i = standbySize - 1; i >= 0; i--) {
									dtm2.removeRow(i);
								}
								sockInit(sock_in, sock_out);
								JOptionPane.showMessageDialog(null, "������ ����߽��ϴ�.");
							} else {
								JOptionPane.showMessageDialog(null, "������ ������� ���߽��ϴ�.");
							}
						}
					} catch (Exception e1) {
					}
				}
			}
		});
	}

	public void compInit() {
		uid_addr.contentPanel.setLayout(new BorderLayout());
		this.panel.setLayout(null);
		// ���� ������Ȳ
		this.present_rsv_label_panel.setBackground(Color.WHITE);
		this.present_rsv_label.setPreferredSize(new Dimension(160, 50));
		this.present_rsv_label.setFont(f.deriveFont(Font.BOLD, 26f));
		this.present_rsv_label_panel.add(present_rsv_label);

		this.present_rsv_label_ment.setFont(f.deriveFont(Font.PLAIN, 14f));
		this.present_rsv_label_ment.setForeground(Color.GRAY);
		this.present_rsv_label_ment.setPreferredSize(new Dimension(300, 30));
		this.present_rsv_label_ment.setVerticalAlignment(JLabel.BOTTOM);
		this.present_rsv_label_panel.setBounds(0, 0, uid_addr.contentPanel.getWidth(), 50);
		this.present_rsv_label_panel.add(present_rsv_label_ment);

		this.panel.add(present_rsv_label_panel);
		this.present_rsv_scroll.setBounds(0, 50, uid_addr.contentPanel.getWidth(), 280);
		this.panel.add(present_rsv_scroll);

		// ���� ��������Ȳ
		this.present_standby_label_panel.setBackground(Color.WHITE);
		this.present_standby_label.setPreferredSize(new Dimension(200, 50));
		this.present_standby_label.setFont(f.deriveFont(Font.BOLD, 26f));
		this.present_standby_label_panel.add(present_standby_label);

		this.present_standby_label_ment.setFont(f.deriveFont(Font.PLAIN, 14f));
		this.present_standby_label_ment.setForeground(Color.GRAY);
		this.present_standby_label_ment.setPreferredSize(new Dimension(390, 30));
		this.present_standby_label_ment.setVerticalAlignment(JLabel.BOTTOM);
		this.present_standby_label_panel.setBounds(0, 330, uid_addr.contentPanel.getWidth(), 50);
		this.present_standby_label_panel.add(present_standby_label_ment);

		this.panel.add(present_standby_label_panel);
		this.present_standby_scroll.setBounds(0, 380, uid_addr.contentPanel.getWidth(), 200);
		this.panel.add(present_standby_scroll);

		panelBtn.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panelBtn.add(rsvCancelBtn);
		panelBtn.add(standbyCancelBtn);
		uid_addr.contentPanel.add(panel);
		uid_addr.contentPanel.add(panelBtn, BorderLayout.SOUTH);

		// // �� ������ �������� �׸��� ���Ѵ�.
		table1.setShowVerticalLines(false);
		table1.getColumn("û����ȣ").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.10));
		table1.getColumn("����").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.42));
		table1.getColumn("����").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.20));
		table1.getColumn("���ǻ�").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.15));
		table1.getColumn("������").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.13));
		table1.setAutoCreateRowSorter(true);
		TableRowSorter ts = new TableRowSorter(table1.getModel());
		table1.setRowSorter(ts);

		table2.setShowVerticalLines(false);
		table2.getColumn("û����ȣ").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.10));
		table2.getColumn("����").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.45));
		table2.getColumn("����").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.20));
		table2.getColumn("���ǻ�").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.15));
		table2.getColumn("����ȣ").setPreferredWidth((int) (present_rsv_scroll.getWidth() * 0.10));
		table2.setAutoCreateRowSorter(true);
		TableRowSorter ts2 = new TableRowSorter(table2.getModel());
		table2.setRowSorter(ts2);
	}

	public Content2Comp(UserInfoDialog uid_addr, Font f, DataInputStream sock_in, DataOutputStream sock_out) {
		this.uid_addr = uid_addr;
		this.f = f;
		sockInit(sock_in, sock_out);
		compInit();
		eventInit(sock_in, sock_out);
	}
}