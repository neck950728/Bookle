package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Content3Comp extends JPanel {
	// �뿩����
	private JLabel present_borrow_label = new JLabel("���� �뿩��Ȳ");
	private JLabel present_borrow_label_ment = new JLabel("ȸ������ ���� �뿩���� å�� ������ �˷��ݴϴ�.");
	private JPanel present_borrow_label_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	// �뿩�� / �ݳ��� / åid / ���� / ���� / ���ǻ�
	private String[] headers1 = new String[] { "û����ȣ", "����", "����", "���ǻ�", "�뿩��" };
	private DefaultTableModel dtm1 = new DefaultTableModel(headers1, 0);
	private JTable table1 = new JTable(dtm1);
	private JScrollPane present_borrow_scroll = new JScrollPane(table1);

	private JLabel total_borrow_label = new JLabel("��ü �뿩 ���");
	private JLabel total_borrow_label_ment = new JLabel("ȸ������ ���ݱ��� �뿩�� ��� ������ �˷��ݴϴ�.");
	private JPanel total_borrow_label_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	private String[] headers2 = new String[] { "û����ȣ", "����", "����", "���ǻ�", "�뿩��", "�ݳ�" };
	private DefaultTableModel dtm2 = new DefaultTableModel(headers2, 0);
	private JTable table2 = new JTable(dtm2);
	private JScrollPane total_borrow_scroll = new JScrollPane(table2);
	private String[] rcv_msg = new String[6];

	public Content3Comp(UserInfoDialog uid_addr, Font f, DataInputStream sock_in, DataOutputStream sock_out, int flag) {
		try {
			sock_out.writeUTF("list_member_detail");
			sock_out.writeUTF(uid_addr.userid);
			sock_out.writeUTF("rental");
			int size = sock_in.readInt();
			System.out.println(size);
			// �뿩�� / �ݳ��� / åid / ���� / ���� / ���ǻ�
			for (int i = 0; i < size; i++) {
				rcv_msg[0] = sock_in.readUTF();
				rcv_msg[1] = sock_in.readUTF();
				rcv_msg[2] = sock_in.readUTF();
				rcv_msg[3] = sock_in.readUTF();
				rcv_msg[4] = sock_in.readUTF();
				rcv_msg[5] = sock_in.readUTF();
				if (rcv_msg[1].equals("�뿩 ��")) {
					dtm1.addRow(new Object[] { rcv_msg[2], rcv_msg[3], rcv_msg[4], rcv_msg[5], rcv_msg[0] });
					dtm2.addRow(new Object[] { rcv_msg[2], rcv_msg[3], rcv_msg[4], rcv_msg[5], rcv_msg[0], "�뿩��"});
				}else {
					dtm2.addRow(new Object[] { rcv_msg[2], rcv_msg[3], rcv_msg[4], rcv_msg[5], rcv_msg[0], rcv_msg[1] });
				}
				
			}
		} catch (Exception e) {
			System.out.println("���� �Է� ����");
		}
		uid_addr.contentPanel.setLayout(null);
		if (flag == 3) {
			
			// ���� ������Ȳ
			this.present_borrow_label_panel.setBackground(Color.WHITE);
			this.present_borrow_label.setPreferredSize(new Dimension(160, 50));
			this.present_borrow_label.setFont(f.deriveFont(Font.BOLD, 26f));
			this.present_borrow_label_panel.add(present_borrow_label);

			this.present_borrow_label_ment.setFont(f.deriveFont(Font.PLAIN, 14f));
			this.present_borrow_label_ment.setForeground(Color.GRAY);
			this.present_borrow_label_ment.setPreferredSize(new Dimension(300, 30));

			this.present_borrow_label_ment.setVerticalAlignment(JLabel.BOTTOM);
			this.present_borrow_label_panel.setBounds(0, 0, uid_addr.contentPanel.getWidth(), 50);
			this.present_borrow_label_panel.add(present_borrow_label_ment);

			uid_addr.contentPanel.add(present_borrow_label_panel);
			this.present_borrow_scroll.setBounds(0, 50, uid_addr.contentPanel.getWidth(),
					uid_addr.contentPanel.getHeight());
			uid_addr.contentPanel.add(present_borrow_scroll);

			// // ���ڻ��� ���Ѵ�.
			table1.setGridColor(Color.white);
			// // �� ������ �������� �׸��� ���Ѵ�.
			table1.setShowVerticalLines(false);
			table1.getColumn("û����ȣ").setPreferredWidth((int) (present_borrow_scroll.getWidth() * 0.10));
			table1.getColumn("����").setPreferredWidth((int) (present_borrow_scroll.getWidth() * 0.42));
			table1.getColumn("����").setPreferredWidth((int) (present_borrow_scroll.getWidth() * 0.20));
			table1.getColumn("���ǻ�").setPreferredWidth((int) (present_borrow_scroll.getWidth() * 0.15));
			table1.getColumn("�뿩��").setPreferredWidth((int) (present_borrow_scroll.getWidth() * 0.13));
			table1.setAutoCreateRowSorter(true);
			TableRowSorter ts1 = new TableRowSorter(table1.getModel());
			table1.setRowSorter(ts1);
		} else if (flag == 4) {
			// ���� ������Ȳ
			this.total_borrow_label_panel.setBackground(Color.WHITE);
			this.total_borrow_label.setPreferredSize(new Dimension(170, 50));
			this.total_borrow_label.setFont(f.deriveFont(Font.BOLD, 26f));
			this.total_borrow_label_panel.add(total_borrow_label);

			this.total_borrow_label_ment.setFont(f.deriveFont(Font.PLAIN, 14f));
			this.total_borrow_label_ment.setForeground(Color.GRAY);
			this.total_borrow_label_ment.setPreferredSize(new Dimension(300, 30));
			this.total_borrow_label_ment.setVerticalAlignment(JLabel.BOTTOM);
			this.total_borrow_label_panel.setBounds(0, 0, uid_addr.contentPanel.getWidth(), 50);
			this.total_borrow_label_panel.add(total_borrow_label_ment);

			uid_addr.contentPanel.add(total_borrow_label_panel);
			this.total_borrow_scroll.setBounds(0, 50, uid_addr.contentPanel.getWidth(),
					uid_addr.contentPanel.getHeight() - 50);
			uid_addr.contentPanel.add(total_borrow_scroll);
			// // �� ������ �������� �׸��� ���Ѵ�.
			table2.setShowVerticalLines(false);
			table2.getColumn("û����ȣ").setPreferredWidth((int) (total_borrow_scroll.getWidth() * 0.08));
			table2.getColumn("����").setPreferredWidth((int) (total_borrow_scroll.getWidth() * 0.27));
			table2.getColumn("����").setPreferredWidth((int) (total_borrow_scroll.getWidth() * 0.15));
			table2.getColumn("���ǻ�").setPreferredWidth((int) (total_borrow_scroll.getWidth() * 0.10));
			table2.getColumn("�뿩��").setPreferredWidth((int) (total_borrow_scroll.getWidth() * 0.20));
			table2.getColumn("�ݳ�").setPreferredWidth((int) (total_borrow_scroll.getWidth() * 0.20));
			table2.setAutoCreateRowSorter(true);
			TableRowSorter ts2 = new TableRowSorter(table2.getModel());
			table2.setRowSorter(ts2);
		}

	}

}