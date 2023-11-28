package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class PwChangeDialog extends JDialog{
	private JPanel panel = new JPanel(new GridLayout(2, 1));
	private JLabel label = new JLabel("���� �н����带 �� �� �� �Է��ϼ���.");
	private JPasswordField pw = new JPasswordField("");
	private JButton send = new JButton("Ȯ��");
	
	private PwChangeDialog thisadd = this;
	
	private DataInputStream sock_in;
	private DataOutputStream sock_out;
	private Font font;
	
	
	@SuppressWarnings("deprecation")
	public void pwEvent() {
		if(pw.getText().length()==0) {
			label.setText("�н����尡 �Էµ��� �ʾҽ��ϴ�.");
		}else {
			try {
				//�����й�ȣ �Է� (1: �������� �� Ŀ�ǵ带 ������ ��ȣ��)
				sock_out.writeInt(1);
				sock_out.writeUTF(pw.getText());
				String cmd = sock_in.readUTF();
				if(cmd.equals("inputnewpw")) {
					new NewPwChangeDialog(thisadd, font, sock_in, sock_out);
				}else if (cmd.equals("pw_fail")) {
					label.setText("��й�ȣ�� Ʋ�Ƚ��ϴ�.�ٽ� �Է����ּ���.");
				}
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(thisadd, "���� ���� �����Դϴ�.");
				dispose();
			}
		}
	}
	
	public PwChangeDialog(UserInfoDialog self, Font font, DataInputStream sock_in, DataOutputStream sock_out){
		this.font = font;
		this.sock_in = sock_in;
		this.sock_out = sock_out;
		
		this.addWindowListener(new WindowAdapter() {	
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					sock_out.writeInt(0);
					System.out.println("���� �ߴ� �޼��� ����");
				}catch(Exception e1) {}
				dispose();
				
			}
		});
		
		
		this.setLayout(new FlowLayout());
		
		this.setTitle("��й�ȣ ����");
		this.setLocationRelativeTo(self);
		this.setSize(350, 120);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.label.setFont(font.deriveFont(Font.PLAIN, 13f));
		this.label.setForeground(Color.RED);
		this.label.setPreferredSize(new Dimension(200, 20));
		this.panel.add(label);
		this.pw.setEchoChar('*');
		this.pw.setFont(font.deriveFont(Font.PLAIN, 20f));
		this.pw.setPreferredSize(new Dimension(240, 35));
		this.panel.add(pw);
		this.add(panel);
		
		this.send.setPreferredSize(new Dimension(70, 70));
		this.add(send);
		
		this.pw.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					pwEvent();
				}
			}
		});
		
		this.send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pwEvent();
			}
		});
		
		this.setResizable(false);
		this.setModal(true);
		this.setVisible(true);
	}
}