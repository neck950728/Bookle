package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class JoinDialog extends JDialog {
	private JoinDialog self = this;
	private SendAuthNum sendAuthNum = new SendAuthNum();
	private int authNum;
	private boolean isAuth;
	private boolean isReceive;
	
	private ImageIcon getScaledImageIcon(String fileName, int width, int height) {
		URL imageURL = this.getClass().getResource("/images/" + fileName);
		ImageIcon imageIcon = new ImageIcon(imageURL);
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(width, height, 4);
		imageIcon = new ImageIcon(newimg);
		return imageIcon;
	}
	
	
	private String idConfirm = null;
	// private int closeCmd = -1;
	private int width = 70;
	private int height = 18;
	private JLabel logoImg = new JLabel(getScaledImageIcon("bookle_logo.png", this.width, this.height));
	private JLabel joinTitle = new JLabel("ȸ �� �� ��");
	
	private JTextField rand = new JTextField();
	private JLabel mainAlarm = new JLabel("* ǥ�ô� �ʼ� �Է»����Դϴ�.");
	private JLabel starIcon1 = new JLabel("*");
	private JLabel starIcon2 = new JLabel("*");
	private JLabel starIcon3 = new JLabel("*");
	private JLabel starIcon4 = new JLabel("*");
	private JLabel starIcon5 = new JLabel("*");
	private JLabel starIcon6 = new JLabel("*");
	private JLabel starIcon7 = new JLabel("*");
	private JLabel starIcon8 = new JLabel("*");
	
	private String gend = null;
	
	private JLabel idTitle = new JLabel("ID");
	private JLabel pwTitle = new JLabel("��й�ȣ");
	private JLabel pwConfirmTitle = new JLabel("��й�ȣ");
	private JLabel pwConfirmTitle2 = new JLabel("Ȯ��");
	private JLabel genderTitle = new JLabel("����");
	private JLabel nameTitle = new JLabel("�̸�");
	private ButtonGroup gender = new ButtonGroup();
	private JRadioButton male = new JRadioButton("����");
	private JRadioButton female = new JRadioButton("����");
	private JLabel birthTitle = new JLabel("�������");
	private JLabel hintTitle = new JLabel("��й�ȣ Q&A");
	private JLabel idAlarm = new JLabel();
	private JLabel pwAlarm1 = new JLabel("��ҹ��� �Ǵ� Ư������ ���� 8~20�ڸ��� �Է��ϼ���.");
	private JLabel pwAlarm2 = new JLabel();
	private JLabel nameAlarm = new JLabel();
	private JLabel genderAlarm = new JLabel();
	private JLabel hintAlarm = new JLabel();
	private JLabel phoneAlarm = new JLabel();
	private JLabel emailAlarm = new JLabel();
	
	private final String id_regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
	private final String pw_regex = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,20}$";
	private final String email_regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
	
	private JTextField idInput = new HintTextField("���̵� �Է����ּ���.");
	private JComboBox<String> birthYear = new JComboBox<>();
	private JComboBox<String> birthMonth = new JComboBox<>();
	private JComboBox<String> birthDay = new JComboBox<>();
	private JPasswordField pwInput = new JPasswordField("");
	private JPasswordField pwConfirmInput = new JPasswordField("");
	private JTextField nameInput = new HintTextField("�̸��� �Է����ּ���.");
	private JComboBox<String> hintBox = new JComboBox<>();
	private JTextField ansInput = new HintTextField("�ش� ��Ʈ�� ���� �亯�� �����ּ���.");
	private JButton idCheckBtn = new JButton("�ߺ�Ȯ��");
	private JLabel emailTitle = new JLabel("�̸���");
	private JLabel phoneTitle = new JLabel("�޴���");
	private JTextField phoneNum1 = new JTextField();
	private JLabel phone_bar1 = new JLabel("-");
	private JTextField phoneNum2 = new JTextField();
	private JLabel phone_bar2 = new JLabel("-");
	private JTextField phoneNum3 = new JTextField();
	private JTextField emailInput = new HintTextField("ex) seo@naver.com");
	
	private JButton joinBtn = new JButton("�����ϱ�");
	
	private int phone_flag = 0; // 0 : ������ȣ �ޱ�, 1 : ��ȣ ���Է�
	private JButton receiveAuthNumBtn = new JButton("������ȣ �ޱ�");
	private JTextField authInput = new HintTextField("������ȣ �Է�");
	private JButton authBtn = new JButton("����");
	
	private Font f;

	private String hintDef;
	private String hint0;
	private String hint1;
	private String hint2;
	private String hint3;
	private String hint4;
	private String hint5;
	private String hint6;
	private String hint7;
	private String hint8;
	private String hint9;
	
	private DataInputStream sock_in = null;
	private DataOutputStream sock_out = null;
	
	// =============================================
	private boolean idCheck;
	private boolean pwCheck;
	private boolean nameCheck;
	private boolean genderCheck;
	private boolean pwHintCheck;
	
	private boolean isPossiblePw = false;
	private void pwCheck() {
		String inputPw = String.valueOf(pwInput.getPassword());
		pwAlarm1.setForeground(Color.RED);
		
		if(inputPw.contains(" ") || inputPw.contains("��")) {
			isPossiblePw = false;
			pwAlarm1.setText("������ ����Ͻ� �� �����ϴ�.");
		}else if(inputPw.isEmpty() || inputPw.length() < 8) {
			isPossiblePw = false;
			pwAlarm1.setText("��й�ȣ�� �ʹ� ª���ϴ�.");
		}else if(!Pattern.matches(pw_regex, inputPw)) {
			isPossiblePw = false;
			pwAlarm1.setText("��й�ȣ�� ����, ���� ���� 8 ~ 20���̾�� �մϴ�.");
		}else if(inputPw.contains("|")) {
			isPossiblePw = false;
			pwAlarm1.setText("����Ͻ� �� ���� ��й�ȣ�Դϴ�.");
		}else {
			isPossiblePw = true;
			pwAlarm1.setForeground(Color.BLUE);
			pwAlarm1.setText("��� ������ ��й�ȣ�Դϴ�!");
		}
	}
	
	private void confirmPwCheck() {
		pwAlarm2.setForeground(Color.RED);
		
		if(isPossiblePw) {
			String confirmPw = String.valueOf(pwConfirmInput.getPassword());
			
			if(confirmPw.equals(String.valueOf(pwInput.getPassword()))) {
				pwAlarm2.setForeground(Color.BLUE);
				pwAlarm2.setText("��й�ȣ�� ��ġ�մϴ�!");
				pwCheck = true;
			}else {
				pwAlarm2.setText("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			}
		}else {
			pwAlarm2.setText("��й�ȣ�� ���� �Է����ּ���.");
		}
	}
	
	private void pwCheckInit() {
		pwInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println(pwInput.getText());
				pwCheck();
			}
			
			/*
				���� ���, �ؽ�Ʈ�� '123'�� �ԷµǾ� �ִ� ���¿��� 4�� ������
				4�� �ؽ�Ʈ�� �ԷµǴ� �ð����� �̺�Ʈ�� �߻��Ͽ� �ؽ�Ʈ�� �ԷµǾ� �ִ� ���� �������� �ð��� �� ���� '1234'�� �ƴ� '123'�� �������� �ȴ�.
				�׷��� �ش� Ű�� �������� �� �� �� �� ��й�ȣ üũ�� �ϰԲ� keyReleased �޼��嵵 �������̵� ���� ���̴�.
			*/
			@Override
			public void keyReleased(KeyEvent e) {
				pwCheck();
			}
		});
		
		
		pwConfirmInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				confirmPwCheck();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				confirmPwCheck();
			}
		});
	}
	
	private void tryJoin() {
		try {
			String yearData = birthYear.getSelectedItem().toString().substring(0, 4);
			String monthData = birthMonth.getSelectedItem().toString().substring(0, 2);
			String dayData = birthDay.getSelectedItem().toString().substring(0, 2);
			
			sock_out.writeUTF("account_join");
			sock_out.writeUTF(idConfirm);
			sock_out.writeUTF(String.valueOf(pwConfirmInput.getPassword()));
			sock_out.writeUTF(nameInput.getText());
			sock_out.writeUTF(gend);
			sock_out.writeUTF(yearData + "/" + monthData + "/" + dayData);
			sock_out.writeUTF(phoneNum1.getText() + "-" + phoneNum2.getText() + "-" + phoneNum3.getText());
			sock_out.writeUTF(emailInput.getText());
			sock_out.writeUTF(hintBox.getSelectedItem().toString());
			sock_out.writeUTF(ansInput.getText());
			sock_out.flush();
			String cmd = sock_in.readUTF();
			if(cmd.equals("success")) {
				JOptionPane.showMessageDialog(self, nameInput.getText() + "��! ������ ���ϵ帳�ϴ�.", "ȸ������ ����!", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}else if(cmd.equals("join_fail")) {
				JOptionPane.showMessageDialog(self, "���Կ� �����Ͽ����ϴ�.\n�ٽ� �õ����ּ���.");
			}
		}catch(Exception e3) {
			JOptionPane.showMessageDialog(self,"ȸ�����Կ� ������ �߻��Ͽ����ϴ�.\n �ٽ� �õ����ּ���.");
		}
	}
	
	private void eventInit() {
		// ���� ��ư�� ���� ���
		this.joinBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idCheck = false;
				pwCheck = false;
				nameCheck = false;
				genderCheck = false;
				pwHintCheck = false;
				
				// ----------- ���̵� check(�ߺ� üũ �������� �����ϰ� �˻���) -----------
				String id = idInput.getText();
				if(id.isEmpty()) {
					idAlarm.setText("���̵� �Է����ּ���.");
				}
				// �ߺ�Ȯ�� �Ϸ�� ���̵��, ���� �Էµ� ���̵� �ٸ� ���
				else if(!id.equals(idConfirm)) {
					JOptionPane.showMessageDialog(self, "���̵� �ߺ�Ȯ���� �ʼ��Դϴ�.");
				}else {
					idAlarm.setText("");
					idCheck = true;
				}
				// -------------------------------------------------------------------
				
				// --- ��й�ȣ check ---
				pwCheck();
				confirmPwCheck();
				if(pwCheck) {
					pwAlarm1.setText("");
					pwAlarm2.setText("");
				}
				// ---------------------
				
				// ------------------- �̸� check -------------------
				String name = nameInput.getText();
				if(name.isEmpty()) {
					nameAlarm.setText("�̸��� �Է����ּ���.");
				}else if(name.contains(" ") || name.contains("��")){
					nameAlarm.setText("������ ����Ͻ� �� �����ϴ�.");
				}else if(name.length() > 10){
					nameAlarm.setText("�̸��� �ʹ� ��ϴ�.");
				}else {
					nameAlarm.setText("");
					nameCheck = true;
				}
				// -------------------------------------------------
				
				// ---------------- ���� check ----------------
				if(gender.isSelected(null)) {
					genderAlarm.setText("������ �������ּ���.");
				}else {
					if(male.isSelected()) gend = "��";
					else if(female.isSelected()) gend = "��";
					genderAlarm.setText("");
					genderCheck = true;
				}
				// -------------------------------------------
				
				// ------------------------------ ��й�ȣ ��Ʈ check ------------------------------
				if(hintBox.getSelectedItem().equals(hintDef)) {
					hintAlarm.setText("��й�ȣ ��Ʈ�� �������ּ���.");
				}else if(ansInput.getText().isEmpty()) {
					hintAlarm.setText("��й�ȣ ��Ʈ�� ���� ���� �����ּ���.");
				}else if(ansInput.getText().startsWith(" ") || ansInput.getText().startsWith("��")) {
					hintAlarm.setText("�������� ������ �� �����ϴ�.");
				}else if(ansInput.getText().length() > 17){
					hintAlarm.setText("���� �ʹ� ��ϴ�.");
				}else {
					hintAlarm.setText("");
					pwHintCheck = true;
				}
				// -------------------------------------------------------------------------------
				
				// ---------------- �޴��� check ----------------
				if(!isAuth) {
					phoneAlarm.setText("�޴��� ������ ���ּ���.");
				}else {
					phoneAlarm.setText("");
				}
				// ---------------------------------------------
				
				if(idCheck && pwCheck && nameCheck && genderCheck && pwHintCheck && isAuth) {
					if(emailInput.getText().isEmpty()){
						int select = JOptionPane.showConfirmDialog(self, "�̸����� �Էµ��� �ʾҽ��ϴ�.\n�׷��� �����Ͻðڽ��ϱ�?", "ȸ������", JOptionPane.YES_NO_OPTION);
						if(0 == select) {
							tryJoin();
						}
					}else if(emailInput.getText().length() > 28) {
						emailAlarm.setText("�̸����� �ʹ� ��ϴ�.");
					}else if(!Pattern.matches(email_regex, emailInput.getText())) {
						emailAlarm.setText("�߸��� �̸��� �����Դϴ�.");
					}else {
						tryJoin();
					}
				}
			}
		});
		
		this.idCheckBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idcheckInit();
			}
		});
	}
	
	private void authInit() {
		receiveAuthNumBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(0 == phone_flag) {
					try {
						String phone1 = phoneNum1.getText().trim();
						String phone2 = phoneNum2.getText().trim();
						String phone3 = phoneNum3.getText().trim();
						
						if(phone1.isEmpty() || phone2.isEmpty() || phone3.isEmpty()) {
							phoneAlarm.setText("�޴��� ��ȣ�� �Է����ּ���.");
						}else if((phone1.contains(" ") || phone1.contains("��")) || (phone2.contains(" ") || phone2.contains("��")) || (phone3.contains(" ") || phone3.contains("��"))) {
							phoneAlarm.setText("������ ����Ͻ� �� �����ϴ�.");
						}else if(!((phone1.length() == 3) && (phone2.length() == 4) && (phone3.length() == 4))) {
							phoneAlarm.setText("�߸��� �޴��� �����Դϴ�.");
						}else {
							Integer.parseInt(phone1); Integer.parseInt(phone2); Integer.parseInt(phone3);
							
							authNum = sendAuthNum.send(phone1 + phone2 + phone3);
							
							phoneAlarm.setText("");
							phoneNum1.setEnabled(false);
							phoneNum2.setEnabled(false);
							phoneNum3.setEnabled(false);
							receiveAuthNumBtn.setText("��ȣ ���Է�");
							isReceive = true;
							phone_flag = 1;
							JOptionPane.showMessageDialog(JoinDialog.this, "������ȣ�� �����Ͽ����ϴ�!", "", JOptionPane.INFORMATION_MESSAGE);
						}
					}catch(Exception e2) {
						phoneAlarm.setText("���ڸ� �Է����ּ���.");
					}
				}else if(1 == phone_flag) {
					phoneNum1.setEnabled(true); phoneNum1.setText("");
					phoneNum2.setEnabled(true); phoneNum2.setText("");
					phoneNum3.setEnabled(true); phoneNum3.setText("");
					authInput.setEnabled(true); authInput.setText("");
					receiveAuthNumBtn.setText("������ȣ �ޱ�");
					authBtn.setEnabled(true);
					phone_flag = 0;
					isAuth = false;
					isReceive = false;
					JOptionPane.showMessageDialog(JoinDialog.this, "�ٽ� ������ �޾��ּ���.", "", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		

		authBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isReceive) {
					if(authInput.getText().equals(String.valueOf(authNum))) {
						authInput.setEnabled(false);
						authBtn.setEnabled(false);
						isAuth = true;
						JOptionPane.showMessageDialog(JoinDialog.this, "������ �����Ͽ����ϴ�!", "���� ����!", JOptionPane.INFORMATION_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(JoinDialog.this, "������ȣ�� ��ġ���� �ʽ��ϴ�.", "���� ����", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(JoinDialog.this, "������ȣ�� ���� �޾��ּ���.", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void idcheckInit() {
		String id = idInput.getText();
		idAlarm.setForeground(Color.RED);
		
		if(id.trim().isEmpty()) {
			idAlarm.setText("���̵� �Է����ּ���.");
		}else if(id.contains(" ") || id.contains("��")) {
			idAlarm.setText("������ ����Ͻ� �� �����ϴ�.");
		}else if(!Pattern.matches(id_regex, id)){
			idAlarm.setText("���̵�� ����, ���� ���� 5 ~ 12���̾�� �մϴ�.");
		}else {
			try {
				// (���̵� �ߺ��˻�)
				// 1. ���̵� �ߺ��˻� ��ȣ ����
				sock_out.writeUTF("account_idcheck");
				// 2. ���̵� ����
				sock_out.writeUTF(idInput.getText());
				// 3. ���̵� �ߺ��˻� ��� ����
				String cmd = sock_in.readUTF();
				
				if(cmd.equals("id_duplicate")) {
					idAlarm.setText("�̹� �����ϴ� ���̵��Դϴ�.");
				}else if(cmd.equals("success")) {
					idAlarm.setForeground(Color.BLUE);
					idAlarm.setText("���� ���̵𱺿�!");
					idConfirm = id;
				}
			}catch(Exception e) {
				e.printStackTrace();
				idAlarm.setText("�ߺ�Ȯ�� �������� ������ �߻��Ͽ����ϴ�.");
			}
		}
	}
	
	private void compInit() {
		setLayout(null);
		setTitle("ȸ������");

		this.logoImg.setBounds(7, 7, this.width, this.height);
		add(this.logoImg);
		
		this.mainAlarm.setBounds(310, 65, 200, 19);
		this.mainAlarm.setForeground(new Color(255, 0, 0));
		this.mainAlarm.setFont(this.f.deriveFont(2, 12.0F));
		this.add(mainAlarm);

		this.starIcon1.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon1.setForeground(new Color(255, 0, 0));
		this.starIcon2.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon2.setForeground(new Color(255, 0, 0));
		this.starIcon3.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon3.setForeground(new Color(255, 0, 0));
		this.starIcon4.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon4.setForeground(new Color(255, 0, 0));
		this.starIcon5.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon5.setForeground(new Color(255, 0, 0));
		this.starIcon6.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon6.setForeground(new Color(255, 0, 0));
		this.starIcon7.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon7.setForeground(new Color(255, 0, 0));
		this.starIcon8.setFont(this.f.deriveFont(0, 25.0F));
		this.starIcon8.setForeground(new Color(255, 0, 0));
		
		for (int i = 2018; i >= 1900; i--) {
			this.birthYear.addItem(i + "��");
		}
		for (int i = 1; i <= 9; i++) {
			this.birthMonth.addItem("0" + i + "��");
		}
		for (int i = 10; i <= 12; i++) {
			this.birthMonth.addItem(i + "��");
		}
		for (int i = 1; i <= 9; i++) {
			this.birthDay.addItem("0" + i + "��");
		}
		for (int i = 10; i <= 31; i++) {
			this.birthDay.addItem(i + "��");
		}
		
		this.hintDef = "��Ʈ�� �������ּ���.";
		this.hint1 = "����� ���� �� 1ȣ��?";
		this.hint2 = "����� �ƹ��� ������?";
		this.hint3 = "����� ��Ӵ� ������?";
		this.hint4 = "����� ���� ģ�� ģ�� �̸���?";
		this.hint5 = "����� ù��� �̸���?";
		this.hint6 = "����� ���� �����ϴ� ������?";
		this.hint7 = "����� ���� ��￡ ���� ��������?";
		this.hint8 = "����� ������?";
		this.hint9 = "����� �¾ ����?";
		this.hint0 = "����� �����ϴ� ���� ������ ��ü������?";

		this.hintBox.addItem(this.hintDef);
		this.hintBox.addItem(this.hint1);
		this.hintBox.addItem(this.hint2);
		this.hintBox.addItem(this.hint3);
		this.hintBox.addItem(this.hint4);
		this.hintBox.addItem(this.hint5);
		this.hintBox.addItem(this.hint6);
		this.hintBox.addItem(this.hint7);
		this.hintBox.addItem(this.hint8);
		this.hintBox.addItem(this.hint9);
		this.hintBox.addItem(this.hint0);


		this.rand.setBounds(0, 0, 0, 0);
		add(this.rand);

		this.joinTitle.setBounds(0, 20, 500, 28);
		this.joinTitle.setFont(this.f.deriveFont(1, 28.0F));
		this.joinTitle.setHorizontalAlignment(0);
		add(this.joinTitle);

		this.idTitle.setBounds(15, 100, 100, 19);
		this.idTitle.setFont(this.f.deriveFont(1, 18.0F));
		add(this.idTitle);

		this.starIcon1.setBounds(45, 100, 100, 22);
		add(this.starIcon1);

		this.idInput.setBounds(115, 92, 250, 35);
		this.idInput.setFont(this.f.deriveFont(0, 16.0F));
		add(this.idInput);

		this.idCheckBtn.setBounds(375, 92, 90, 35);
		this.idCheckBtn.setFont(this.f.deriveFont(1, 13.0F));
		this.idCheckBtn.setBackground(new Color(178, 204, 255));
		
		this.idCheckBtn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					idcheckInit();
				}
			}
		});
		add(this.idCheckBtn);

		this.idAlarm.setBounds(115, 127, 350, 20);
		this.idAlarm.setForeground(new Color(255, 0, 0));
		this.idAlarm.setFont(this.f.deriveFont(2, 12.0F));
		add(this.idAlarm);

		this.pwTitle.setBounds(15, 160, 100, 19);
		this.pwTitle.setFont(this.f.deriveFont(1, 18.0F));
		add(this.pwTitle);

		this.starIcon2.setBounds(90, 160, 100, 18);
		add(this.starIcon2);

		this.pwInput.setBounds(115, 150, 350, 35);
		this.pwInput.setFont(this.f.deriveFont(0, 16.0F));
		add(this.pwInput);

		this.pwAlarm1.setBounds(115, 185, 350, 20);
		this.pwAlarm1.setForeground(new Color(255, 0, 0));
		this.pwAlarm1.setFont(this.f.deriveFont(2, 12.0F));
		add(this.pwAlarm1);

		this.pwConfirmTitle.setBounds(15, 215, 100, 18);
		this.pwConfirmTitle.setFont(this.f.deriveFont(1, 17.0F));
		add(this.pwConfirmTitle);

		this.pwConfirmTitle2.setBounds(15, 235, 100, 18);
		this.pwConfirmTitle2.setFont(this.f.deriveFont(1, 17.0F));
		add(this.pwConfirmTitle2);

		this.pwConfirmInput.setBounds(115, 215, 350, 35);
		this.pwConfirmInput.setFont(this.f.deriveFont(0, 16.0F));
		add(this.pwConfirmInput);

		this.starIcon3.setBounds(90, 225, 100, 18);
		add(this.starIcon3);

		this.pwAlarm2.setBounds(115, 250, 350, 20);
		this.pwAlarm2.setForeground(new Color(255, 0, 0));
		this.pwAlarm2.setFont(this.f.deriveFont(2, 12.0F));
		add(this.pwAlarm2);

		this.nameTitle.setBounds(15, 290, 100, 19);
		this.nameTitle.setFont(this.f.deriveFont(1, 18.0F));
		add(this.nameTitle);

		this.nameInput.setBounds(115, 280, 350, 35);
		this.nameInput.setFont(this.f.deriveFont(0, 16.0F));
		add(this.nameInput);

		this.starIcon4.setBounds(58, 290, 100, 19);
		add(this.starIcon4);

		this.nameAlarm.setBounds(115, 315, 350, 20);
		this.nameAlarm.setForeground(new Color(255, 0, 0));
		this.nameAlarm.setFont(this.f.deriveFont(2, 12.0F));
		add(this.nameAlarm);

		this.genderTitle.setBounds(15, 335, 100, 19);
		this.genderTitle.setFont(this.f.deriveFont(1, 18.0F));
		add(this.genderTitle);
		
		

		this.starIcon5.setBounds(58, 335, 100, 19);
		add(this.starIcon5);

		this.male.setBounds(115, 329, 100, 28);
		this.male.setFont(this.f.deriveFont(0, 16.0F));
		add(this.male);

		this.female.setBounds(220, 329, 100, 28);
		this.female.setFont(this.f.deriveFont(0, 16.0F));
		add(this.female);

		this.genderAlarm.setBounds(280, 333, 200, 20);
		this.genderAlarm.setForeground(new Color(255, 0, 0));
		this.genderAlarm.setFont(this.f.deriveFont(2, 12.0F));
		add(this.genderAlarm);

		this.gender.add(this.male);
		this.gender.add(this.female);

		this.birthTitle.setBounds(15, 380, 100, 19);
		this.birthTitle.setFont(this.f.deriveFont(1, 18.0F));
		this.add(birthTitle);

		this.starIcon6.setBounds(93, 380, 100, 19);
		this.add(starIcon6);
		
		this.birthYear.setBounds(115, 370, 120, 35);
		this.birthYear.setFont(this.f.deriveFont(1, 16.0F));
		this.add(birthYear);
		
		this.birthMonth.setBounds(260, 370, 90, 35);
		this.birthMonth.setFont(this.f.deriveFont(1, 16.0F));
		this.add(birthMonth);
		
		this.birthDay.setBounds(375, 370, 90, 35);
		this.birthDay.setFont(this.f.deriveFont(1, 16.0F));
		this.add(birthDay);
		
		this.hintTitle.setBounds(15, 425, 150, 19);
		this.hintTitle.setFont(this.f.deriveFont(1, 18.0F));
		this.add(hintTitle);
		
		this.starIcon7.setBounds(143, 425, 50, 19);
		this.add(starIcon7);
		
		this.hintAlarm.setBounds(160, 423, 200, 20);
		this.hintAlarm.setForeground(new Color(255, 0, 0));
		this.hintAlarm.setFont(this.f.deriveFont(2, 12.0F));
		this.add(hintAlarm);
		
		this.hintBox.setBounds(25, 450, 440, 35);
		this.hintBox.setFont(this.f.deriveFont(Font.PLAIN, 14.0F));
		this.add(hintBox);
		
		this.ansInput.setBounds(25, 490, 440, 35);
		this.ansInput.setFont(this.f.deriveFont(Font.PLAIN, 16.0F));
		this.add(ansInput);
		
		this.phoneTitle.setBounds(15, 550, 150, 19);
		this.phoneTitle.setFont(this.f.deriveFont(1, 18.0F));
		this.add(phoneTitle);
		
		this.starIcon8.setBounds(73, 550, 150, 19);
		this.add(starIcon8);
		
		this.phoneNum1.setBounds(95, 540, 70, 35);
		this.phoneNum1.setFont(this.f.deriveFont(Font.PLAIN, 16.0F));
		this.add(phoneNum1);
		
		this.phone_bar1.setBounds(170, 540, 40, 35);
		this.phone_bar1.setFont(this.f.deriveFont(Font.BOLD, 16.0F));
		this.add(phone_bar1);
		
		this.phoneNum2.setBounds(185, 540, 80, 35);
		this.phoneNum2.setFont(this.f.deriveFont(Font.PLAIN, 16.0F));
		this.add(phoneNum2);
		
		this.phone_bar2.setBounds(270, 540, 40, 35);
		this.phone_bar2.setFont(this.f.deriveFont(Font.BOLD, 16.0F));
		this.add(phone_bar2);
		
		this.phoneNum3.setBounds(285, 540, 80, 35);
		this.phoneNum3.setFont(this.f.deriveFont(Font.PLAIN, 16.0F));
		this.add(phoneNum3);
		
		// ------------------ �޴��� ���� ���� ------------------
		this.receiveAuthNumBtn.setBounds(370, 540, 103, 35);
		this.add(receiveAuthNumBtn);
		
		this.authInput.setBounds(265, 580, 100, 35);
		this.add(authInput);
		
		this.authBtn.setBounds(370, 580, 70, 35);
		this.add(authBtn);
		// ---------------------------------------------------
		
		this.phoneAlarm.setBounds(100, 575, 200, 20);
		this.phoneAlarm.setForeground(new Color(255, 0, 0));
		this.phoneAlarm.setFont(this.f.deriveFont(2, 12.0F));
		this.add(phoneAlarm);
		
		
		this.emailTitle.setBounds(15, 640, 150, 19);
		this.emailTitle.setFont(this.f.deriveFont(1, 18.0F));
		this.add(emailTitle);
		
		this.emailInput.setBounds(115, 630, 350, 35);
		this.emailInput.setFont(this.f.deriveFont(0, 16.0F));
		this.add(emailInput);
		
		this.emailAlarm.setBounds(115, 665, 350, 20);
		this.emailAlarm.setForeground(new Color(255, 0, 0));
		this.emailAlarm.setFont(this.f.deriveFont(2, 12.0F));
		add(this.emailAlarm);
		
		
		this.joinBtn.setBounds(365, 670, 100, 40);
		this.joinBtn.setBackground(new Color(178, 204, 255));
		this.joinBtn.setFont(this.f.deriveFont(0, 14.0F));
		
		this.add(joinBtn);
	}

	public JoinDialog(LoginDialog self, Font font, DataInputStream sock_in, DataOutputStream sock_out) {
		this.f = font;
		this.sock_in = sock_in;
		this.sock_out = sock_out;
		
		setSize(500, 750);
		setLocationRelativeTo(self);
		setDefaultCloseOperation(2);
		setResizable(false);
		setModal(true);

		compInit();
		eventInit();
		authInit();
		pwCheckInit();

		setVisible(true);
	}
}