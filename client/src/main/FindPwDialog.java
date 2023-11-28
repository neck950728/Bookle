package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FindPwDialog extends JDialog{
	private Font f;
	private SendAuthNum sendAuthNum = new SendAuthNum();
	private int authNum;
	private boolean isAuth;
	private boolean isReceive;
	
	private DataInputStream sock_in = null;
	private DataOutputStream sock_out = null;
	
	private JLabel findPwTitle = new JLabel("��й�ȣ ã��");
	
	private JLabel idTitle = new JLabel("ID");
	private JLabel birthTitle = new JLabel("�������");
	private JLabel nameTitle = new JLabel("�̸�");
	private JLabel hintTitle = new JLabel("��й�ȣ Q&A");
	private JLabel authTitle = new JLabel("�޴��� ��ȣ");
	private JTextField rand = new JTextField();
	
	private JTextField idInput = new HintTextField("���̵� �Է����ּ���.");
	private JComboBox<String> birthYear = new JComboBox<>();
	private JComboBox<String> birthMonth = new JComboBox<>();
	private JComboBox<String> birthDay = new JComboBox<>();
	private JTextField nameInput = new HintTextField("�̸��� �Է����ּ���.");
	private JComboBox<String> hintBox = new JComboBox<>();
	private JTextField ansInput = new HintTextField("�ش� ��Ʈ�� ���� �亯�� �����ּ���.");
	private JTextField phoneInput = new HintTextField("'-' ���� ����");
	private JTextField authInput = new HintTextField("������ȣ �Է�");
	
	private String hintDef,hint0,hint1,hint2,hint3,hint4,hint5,hint6,hint7,hint8,hint9;
	private JButton receiveAuthNumBtn = new JButton("������ȣ �ޱ�");
	private JButton findPwBtn = new JButton("ã��");
	private JButton authBtn = new JButton("����");
	
	private JLabel alarmMsg1 = new JLabel("");
	private JLabel alarmMsg2 = new JLabel("");
	private JLabel alarmMsg3 = new JLabel("");
	
	
	public void compInit() {
		this.setLayout(null);
		this.setTitle("��й�ȣ ã��");
		
		this.rand.setBounds(0,0,0,0);
		this.add(rand);
		this.findPwTitle.setBounds(20,20,380,30);
		this.findPwTitle.setFont(f.deriveFont(Font.BOLD,17f));
		this.add(findPwTitle);
		
		for(int i =2018;i>=1900;i--) {
			this.birthYear.addItem(i+"��");
		}
		for(int i = 1;i<=9;i++) {
			this.birthMonth.addItem("0"+i+"��");
		}
		for(int i = 10;i<=12;i++) {
			this.birthMonth.addItem(i+"��");
		}
		for(int i = 1;i<=9;i++) {
			this.birthDay.addItem("0"+i+"��");
		}
		for(int i = 10;i<=31;i++) {
			this.birthDay.addItem(i+"��");
		}
		
		this.idTitle.setBounds(15,70,80,40);
		this.idTitle.setFont(f.deriveFont(Font.BOLD,16f));
		// this.nameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(idTitle);
		
		
		this.idInput.setFont(f.deriveFont(Font.PLAIN,14f));
		this.idInput.setBounds(90,70,287,40);
		this.add(idInput);
		
		this.birthTitle.setBounds(15,130,80,40);
		this.birthTitle.setFont(f.deriveFont(Font.BOLD,14f));
		this.add(birthTitle);
		
		this.birthYear.setBounds(90,130,100,40);
		this.birthYear.setFont(f.deriveFont(Font.PLAIN,14f));
		this.add(birthYear);
		
		this.birthMonth.setBounds(200,130,85,40);
		this.birthMonth.setFont(f.deriveFont(Font.PLAIN,14f));
		this.add(birthMonth);
		
		this.birthDay.setBounds(295,130,83,40);
		this.birthDay.setFont(f.deriveFont(Font.PLAIN,14f));
		this.add(birthDay);
		
		this.nameTitle.setBounds(15,190,80,40);
		this.nameTitle.setFont(f.deriveFont(Font.BOLD,14f));
		this.add(nameTitle);
		
		this.nameInput.setBounds(90,190,287,40);
		this.nameInput.setFont(f.deriveFont(Font.PLAIN,14f));
		this.add(nameInput);
		
		this.alarmMsg1.setBounds(15,227,250,20);
		this.alarmMsg1.setForeground(new Color(255, 0, 0));
		this.alarmMsg1.setFont(f.deriveFont(Font.PLAIN|Font.ITALIC,12f));
		this.add(alarmMsg1);
		
		this.hintTitle.setBounds(15,260,120,30);
		this.hintTitle.setFont(f.deriveFont(Font.BOLD,14f));
		this.add(hintTitle);
		
		this.hintBox.setBounds(15,300,362,33);
		this.hintBox.setFont(f.deriveFont(Font.PLAIN,14f));
		this.hintDef = "��Ʈ�� �������ּ���.";
		this.hint1 = "����� ���� �� 1ȣ��?";this.hint2 = "����� �ƹ��� ������?";this.hint3 = "����� ��Ӵ� ������?";
		this.hint4 = "����� ���� ģ�� ģ�� �̸���?";this.hint5 = "����� ù��� �̸���?";this.hint6 = "����� ���� �����ϴ� ������?";
		this.hint7 = "����� ���� ��￡ ���� ��������?";this.hint8 = "����� ������?";this.hint9 = "����� �¾ ����?";
		this.hint0 = "����� �����ϴ� ���� ������ ��ü������?";
		
		this.hintBox.addItem(hintDef);
		this.hintBox.addItem(hint1);this.hintBox.addItem(hint2);this.hintBox.addItem(hint3);
		this.hintBox.addItem(hint4);this.hintBox.addItem(hint5);this.hintBox.addItem(hint6);
		this.hintBox.addItem(hint7);this.hintBox.addItem(hint8);this.hintBox.addItem(hint9);
		this.hintBox.addItem(hint0);
		this.add(hintBox);
		
		this.ansInput.setBounds(15,340,362,33);
		this.ansInput.setFont(f.deriveFont(Font.PLAIN,14f));
		this.add(ansInput);

		this.alarmMsg2.setBounds(15,370,250,20);
		this.alarmMsg2.setForeground(new Color(255, 0, 0));
		this.alarmMsg2.setFont(f.deriveFont(Font.PLAIN|Font.ITALIC,12f));
		this.add(alarmMsg2);
		
		// ------------------------- �޴��� ���� ���� -------------------------
		this.authTitle.setBounds(15, 410, 80, 40);
		this.authTitle.setFont(f.deriveFont(Font.BOLD,14f));
		this.add(authTitle);
		this.phoneInput.setBounds(90, 410, 150, 40);
		this.add(phoneInput);
		this.receiveAuthNumBtn.setBounds(240, 412, 103, 35);
		this.add(receiveAuthNumBtn);
		
		this.authInput.setBounds(90, 452, 150, 40);
		this.add(authInput);
		this.authBtn.setBounds(240, 454, 70, 35);
		this.add(authBtn);

		this.alarmMsg3.setBounds(15, 489, 250, 20);
		this.alarmMsg3.setForeground(new Color(255, 0, 0));
		this.alarmMsg3.setFont(f.deriveFont(Font.PLAIN|Font.ITALIC,12f));
		this.add(alarmMsg3);
		// -----------------------------------------------------------------
		
		this.findPwBtn.setBounds(277,525,100,35);
		this.findPwBtn.setBackground(new Color(178, 204, 255));
		this.ansInput.setFont(f.deriveFont(Font.PLAIN,14f));
		this.add(findPwBtn);
	}
	
	public void findPwInit(String id, String birth, String name, String hint, String ans) {
		try {
			sock_out.writeUTF("account_pwsearch");
			sock_out.writeUTF(id);
			sock_out.writeUTF(birth);
			sock_out.writeUTF(name);
			sock_out.writeUTF(hint);
			sock_out.writeUTF(ans);
			sock_out.flush();
			String s = sock_in.readUTF();
			if(s.equals("not_exist_id")) {
				JOptionPane.showMessageDialog(this, "������ �������� �ʽ��ϴ�.\n �Է��Ͻ� ������ �ٽ� �ѹ� Ȯ�����ּ���.");
			}else {
				String msg = sock_in.readUTF();
				if(msg.equals("success")) {
					JOptionPane.showMessageDialog(this, "��й�ȣ�� ������ ���� �����Ǿ����ϴ�.\n6�ڸ� �ӽú�й�ȣ : " + s);
					dispose();
				}else if(msg.equals("pw_change_fail")) {
					JOptionPane.showMessageDialog(this, "������ ������ �߻��Ͽ����ϴ�.\n�ٽ� �õ����ּ���.");
				}
			}
		}catch(Exception e) {
			JOptionPane.showMessageDialog(this, "�н����� ã�⿡ ������ �߻��Ͽ����ϴ�.");
		}
	}
	
	public void eventInit() {
		phoneInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if(!phoneInput.getText().isEmpty()) {
						Integer.parseInt(phoneInput.getText());
						alarmMsg3.setText("");
					}
				}catch(Exception e2) {
					phoneInput.setText("");
					alarmMsg3.setText("���ڸ� �Է����ּ���.");
				}
			}
		});
		
		// �� '������ȣ �ޱ�' ��ư ���� ��
		receiveAuthNumBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!idInput.getText().trim().isEmpty()) {
					String name = "";
					String registedPhoneNum = null;
					try {
						sock_out.writeUTF("account_phoneAuth");
						sock_out.writeUTF(idInput.getText());
						sock_out.flush();
						
						name = sock_in.readUTF();
						registedPhoneNum = sock_in.readUTF();
					}catch(Exception e2) {
						e2.printStackTrace();
					}
					
					if(!registedPhoneNum.isEmpty()) {
						if(!phoneInput.getText().isEmpty()) {
							String[] temp = registedPhoneNum.split("-");
							registedPhoneNum = "";
							for(String tmp : temp) {
								registedPhoneNum += tmp;
							}
							
							if(registedPhoneNum.equals(phoneInput.getText())) {
								authNum = sendAuthNum.send(name, registedPhoneNum);
								
								phoneInput.setEnabled(false);
								receiveAuthNumBtn.setEnabled(false);
								isReceive = true;
								JOptionPane.showMessageDialog(FindPwDialog.this, "������ȣ�� �����Ͽ����ϴ�!", "", JOptionPane.INFORMATION_MESSAGE);
							}else {
								JOptionPane.showMessageDialog(FindPwDialog.this, "�ش� ���̵� ��ϵ� ��ȣ�� ��ġ���� �ʽ��ϴ�.", "", JOptionPane.ERROR_MESSAGE);
							}
						}else {
							JOptionPane.showMessageDialog(FindPwDialog.this, "�޴��� ��ȣ�� �Է����ּ���.", "", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(FindPwDialog.this, "���̵� �ٽ� �ѹ� Ȯ�����ּ���.", "", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(FindPwDialog.this, "���̵� ���� �Է����ּ���.", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// �� '����' ��ư ���� ��
		authBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isReceive) {
					if(authInput.getText().equals(String.valueOf(authNum))) {
						authInput.setEnabled(false);
						authBtn.setEnabled(false);
						isAuth = true;
						JOptionPane.showMessageDialog(FindPwDialog.this, "������ �����Ͽ����ϴ�!", "���� ����!", JOptionPane.INFORMATION_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(FindPwDialog.this, "������ȣ�� ��ġ���� �ʽ��ϴ�.", "���� ����", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(FindPwDialog.this, "������ȣ�� ���� �޾��ּ���.", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		
		
		//1. idsearch �۽�
		//2. ok ����
		//3. name, birth
		//4. ok
		this.findPwBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(idInput.getText().isEmpty()&&!(nameInput.getText().isEmpty())) {
					alarmMsg1.setText("ID�� �Էµ��� �ʾҽ��ϴ�.");
				}else if(!(idInput.getText().isEmpty())&&nameInput.getText().isEmpty()) {
					alarmMsg1.setText("�̸��� �Էµ��� �ʾҽ��ϴ�.");
				}else if(idInput.getText().isEmpty()&&nameInput.getText().isEmpty()) {
					alarmMsg1.setText("ID �Ǵ� �̸��� �Էµ��� �ʾҽ��ϴ�.");
				}else {
					alarmMsg1.setText("");
				}
				
				if(ansInput.getText().isEmpty()) {
					alarmMsg2.setText("�亯�� �Էµ��� �ʾҽ��ϴ�.");
				}else if (hintBox.getSelectedItem()==hintDef){
					alarmMsg2.setText("��й�ȣ ��Ʈ�� ����ּ���.");
				}else {
					alarmMsg2.setText("");
				}
				
				if(!isAuth) {
					alarmMsg3.setText("�޴��� ������ ���ּ���.");
				}else {
					alarmMsg3.setText("");
				}
					
				if(!(idInput.getText().isEmpty()) && !(nameInput.getText().isEmpty())
				   && !(ansInput.getText().isEmpty()) && hintBox.getSelectedItem()!=hintDef && isAuth){
					String yearData = birthYear.getSelectedItem().toString().substring(0, 4);
					String monthData = birthMonth.getSelectedItem().toString().substring(0, 2);
					String dayData = birthDay.getSelectedItem().toString().substring(0, 2);
					System.out.println(yearData + monthData + dayData);
					findPwInit(idInput.getText(), yearData + "/" + monthData + "/"+ dayData, nameInput.getText(), hintBox.getSelectedItem().toString(), ansInput.getText());
				}
			}
		});
	}
	
	public FindPwDialog(FindIdPwDialog self, Font font,DataInputStream sock_in, DataOutputStream sock_out) {
		this.f = font;
		
		this.sock_in = sock_in;
		this.sock_out = sock_out;
		
		this.setSize(400, 605);
		this.setLocationRelativeTo(self);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setModal(true);
		
		this.eventInit();
		this.compInit();
		
		this.setVisible(true);
	}
}