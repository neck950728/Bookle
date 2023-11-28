package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class NewPwChangeDialog extends JDialog {
	
	private PwChangeDialog pcd_addr = null;
	private DataInputStream sock_in = null;
	private DataOutputStream sock_out = null;
	private JPasswordField pw = new JPasswordField("");
	private JPasswordField pwconfirm = new JPasswordField("");
	private JLabel label = new JLabel("��ҹ��� �Ǵ� Ư������ ���� 8~20�ڸ��� �Է��ϼ���.");
	private JLabel label_pw = new JLabel("���ο� ��й�ȣ");
	private JLabel label_pwconfirm = new JLabel("��й�ȣ Ȯ��");
	private JButton send = new JButton("����");
	private final String pw_regex = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,20}$";
	
	private KeyListener kl = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) {
				pwEvent();
			}
		}
	};
	
	@SuppressWarnings("deprecation")
	public void pwEvent() {
		String passwd = String.valueOf(pw.getPassword());
		String confirmPw = String.valueOf(pwconfirm.getPassword());
		
		if(passwd.trim().isEmpty() || confirmPw.trim().isEmpty()) {
			label.setText("������ ����� �Էµ��� �ʾҽ��ϴ�.");
		}else if(!passwd.equals(confirmPw)) {
			label.setText("�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
		}else if(!Pattern.matches(pw_regex, passwd)) {
			label.setText("����Ͻ� �� ���� ��й�ȣ�Դϴ�.");
		}else if(passwd.contains("|")){
			label.setText("����Ͻ� �� ���� ��й�ȣ�Դϴ�.");
		}else {
			try {
				sock_out.writeInt(2);
				sock_out.writeUTF(String.valueOf(pw.getPassword()));
				String s = sock_in.readUTF();
				System.out.println(s);
				if(s.equals("success")){
					JOptionPane.showMessageDialog(pcd_addr, "���� ����");
					dispose();
					pcd_addr.dispose();
				}else if(s.equals("change_error")) {
					JOptionPane.showMessageDialog(pcd_addr, "���� ����");
				}else if(s.equals("pwduplicate")) {
					label.setText("�ߺ��� ��й�ȣ�Դϴ�. �ٽ� �Է����ּ���.");
				}
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(pcd_addr, "���� �Ұ�");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void compInit(Font f) {
		
		this.setLayout(null);
				
		this.pw.setEchoChar('*');
		this.pwconfirm.setEchoChar('*');
		
		this.label.setBounds(10,10,300,40);
		this.label.setFont(f.deriveFont(Font.BOLD, 14f));
		this.label.setForeground(Color.BLUE);
		this.add(label);
		
		this.label_pw.setBounds(10,50,110,50);
		this.label_pw.setFont(f.deriveFont(Font.BOLD, 12f));
		this.label_pw.setForeground(Color.RED);
		this.add(label_pw);
		
		this.pw.setNextFocusableComponent(this.pwconfirm);
		
		this.label_pwconfirm.setBounds(10,110,110,50);
		this.label_pwconfirm.setFont(f.deriveFont(Font.BOLD, 12f));
		this.label_pwconfirm.setForeground(Color.RED);
		this.add(label_pwconfirm);
		
		
		this.pw.setFont(f.deriveFont(Font.PLAIN, 12f));
		this.pw.setBounds(120,50,180,50);
		this.add(pw);
		
		this.pwconfirm.setFont(f.deriveFont(Font.PLAIN, 12f));
		this.pwconfirm.setBounds(120,110,180,50);
		this.add(pwconfirm);
		
		this.send.setFont(f.deriveFont(Font.PLAIN, 14f));
		this.send.setBounds(310,50,80,100);
		this.add(send);
	}
	
	public NewPwChangeDialog(PwChangeDialog self, Font f, DataInputStream sock_in, DataOutputStream sock_out){
		this.sock_in = sock_in;
		this.sock_out = sock_out;
		this.pcd_addr = self;
		
		this.addWindowListener(new WindowAdapter() {	
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					sock_out.writeInt(0);
					System.out.println("���� �ߴ� �޼��� ����");
				}catch(Exception e1) {}
				self.dispose();
				dispose();
			}
		});
		
		this.setTitle("�� ��й�ȣ ����");
		this.setSize(400, 200);
		this.setLocationRelativeTo(self);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.compInit(f);
		
		
		this.pw.addKeyListener(kl);
		this.send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pwEvent();
				
			}
		});
		
		this.pwconfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send.doClick();
			}
		});
		
		this.send.setPreferredSize(new Dimension(100, 100));
		this.add(send);
		this.setResizable(false);
		this.setModal(true);
		
		this.setVisible(true);
	}
}