package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Content1Comp extends JPanel {
	// ȸ������
	private JPanel userinfo_t = new JPanel();
	private JLabel userinfo_title = new JLabel("ȸ�� ����");
	private JLabel userinfo_titlement = new JLabel("ȸ������ ������ Ȯ���ϰų� ������ �� �ֽ��ϴ�.");
	private JPanel userinfo_h = new JPanel(new GridLayout(10, 1));
	private JPanel userinfo_c = new JPanel(new GridLayout(10, 1));

	private JPanel emptyPanel1 = new JPanel();
	private JPanel emptyPanel2 = new JPanel();
	private JPanel emptyPanel3 = new JPanel();
	private JPanel emptyPanel4 = new JPanel();

	private JPanel userinfo_b = new JPanel();
	private JPanel content1_panel = new JPanel(new BorderLayout());
	private JLabel content_id_label = new JLabel("���̵�");
	private JLabel content_id = new JLabel();

	private JLabel content_name_label = new JLabel("�̸�");
	private JLabel content_name = new JLabel();

	private JLabel content_gend_label = new JLabel("����");
	private JLabel content_gend = new JLabel();

	private JLabel content_email_label = new JLabel("�̸���");
	private JLabel content_email = new JLabel("�̸��� ����");

	private JLabel content_birth_label = new JLabel("�������");
	private JLabel content_birth = new JLabel("�������");

	private JLabel content_phone_label = new JLabel("��ȭ��ȣ");
	private JLabel content_phone = new JLabel();

	private JLabel content_joindate_label = new JLabel("���� ������");
	private JLabel content_joindate = new JLabel();

	private JLabel content_cr_label = new JLabel("���� �뿩���� å");
	private JLabel content_cr = new JLabel();

	private JLabel content_pr_label = new JLabel("�뿩 ������ å");
	private JLabel content_pr = new JLabel();
	
	private JButton pwchange = new JButton("�н����� ����");
	
	
	public Content1Comp(UserInfoDialog uid_addr, Font f, DataInputStream sock_in, DataOutputStream sock_out) {
		uid_addr.contentPanel.setLayout(new BorderLayout());

		this.userinfo_h.setBackground(new Color(160, 186, 237));
		this.userinfo_c.setBackground(new Color(178, 204, 255));
		this.userinfo_t.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.userinfo_title.setFont(f.deriveFont(Font.BOLD, 26f));
		this.userinfo_titlement.setFont(f.deriveFont(Font.PLAIN, 14f));
		this.userinfo_titlement.setForeground(Color.gray);
		this.userinfo_titlement.setPreferredSize(new Dimension(300, 30));
		this.userinfo_titlement.setVerticalAlignment(JLabel.BOTTOM);
		this.userinfo_title.setPreferredSize(new Dimension(110, 40));
		this.userinfo_t.add(userinfo_title);
		this.userinfo_t.add(userinfo_titlement);
		this.content1_panel.add(userinfo_t, BorderLayout.NORTH);

		// ������
		this.content_joindate_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_joindate_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_joindate_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_joindate_label);
		
		this.content_joindate.setPreferredSize(new Dimension(400, 80));
		this.content_joindate.setText("  " + uid_addr.userjoindate.substring(0, 4) + "�� " + uid_addr.userjoindate.substring(5, 7) + "�� "
				+ uid_addr.userjoindate.substring(8, 10) + "��");
		this.content_joindate.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_joindate);
		
		// ���̵�
		this.content_id_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_id_label.setPreferredSize(new Dimension(180, 80));
		this.content_id_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.userinfo_h.add(content_id_label);
		
		this.content_id.setPreferredSize(new Dimension(400, 80));
		this.content_id.setText("  " + uid_addr.userid);
		this.content_id.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_id);
		
		// �̸�
		this.content_name_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_name_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_name_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_name_label);

		this.content_name.setPreferredSize(new Dimension(400, 80));
		this.content_name.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.content_name.setText("  " + uid_addr.username);
		this.userinfo_c.add(content_name);

		// ����
		this.content_gend_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_gend_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_gend_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_gend_label);
		
		this.content_gend.setPreferredSize(new Dimension(400, 80));
		this.content_gend.setText("  " + uid_addr.usergend + "��");
		this.content_gend.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_gend);
		
		// �̸���
		this.content_email_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_email_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_email_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_email_label);

		this.content_email.setPreferredSize(new Dimension(400, 80));
		if(uid_addr.useremail.equals("null")) {
			this.content_email.setText("  �̸��� ������ �����ϴ�");
		}else {
			this.content_email.setText("  " + uid_addr.useremail);
		}
		this.content_email.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_email);

		// �������
		this.content_birth_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_birth_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_birth_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_birth_label);
		
		this.content_birth.setPreferredSize(new Dimension(400, 80));
		this.content_birth.setText("  " + uid_addr.userbirth.substring(0, 4) + "�� " + uid_addr.userbirth.substring(5, 7) + "�� " + uid_addr.userbirth.substring(8, 10) + "��");
		this.content_birth.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_birth);
		
		// ��ȭ��ȣ
		this.content_phone_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_phone_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_phone_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_phone_label);
		
		this.content_phone.setPreferredSize(new Dimension(400, 80));
		this.content_phone.setText("  " + uid_addr.userphone);
		this.content_phone.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_phone);

		// ���� �뿩��
		this.content_cr_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_cr_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_cr_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_cr_label);

		this.content_cr.setPreferredSize(new Dimension(400, 80));
		this.content_cr.setText("  " + uid_addr.currentRental + " ��");
		this.content_cr.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_cr);
		
		// �뿩����
		this.content_pr_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.content_pr_label.setFont(f.deriveFont(Font.BOLD, 16f));
		this.content_pr_label.setPreferredSize(new Dimension(180, 80));
		this.userinfo_h.add(content_pr_label);
		
		this.content_pr.setPreferredSize(new Dimension(400, 80));
		this.content_pr.setText("  " + uid_addr.possibleRental + " ��");
		this.content_pr.setFont(f.deriveFont(Font.PLAIN, 16f));
		this.userinfo_c.add(content_pr);
		
		// �н����庯��
		this.userinfo_b.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.userinfo_b.add(pwchange);

		// ���̾ƿ� ����
		this.content1_panel.add(userinfo_h, BorderLayout.WEST);
		this.content1_panel.add(userinfo_c);
		this.content1_panel.add(userinfo_b, BorderLayout.SOUTH);
		uid_addr.contentPanel.add(emptyPanel1, BorderLayout.WEST);
		uid_addr.contentPanel.add(emptyPanel2, BorderLayout.EAST);
		uid_addr.contentPanel.add(emptyPanel3, BorderLayout.SOUTH);
		uid_addr.contentPanel.add(emptyPanel4, BorderLayout.NORTH);
		uid_addr.contentPanel.add(content1_panel, BorderLayout.CENTER);
		
		this.pwchange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sock_out.writeUTF("account_pwchange");
					new PwChangeDialog(uid_addr, f, sock_in, sock_out);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(uid_addr, "�н����� ���� ����");
				}
			}
		});
	}
}