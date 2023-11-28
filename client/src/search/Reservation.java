package search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.LoginDialog;
import main.MainFrame;

public class Reservation extends JDialog {
	private BookDTO dto;
	private Detail detail;
	// -------------------------------------------------------------------------------------------------------------------
	private JLabel bookIdLabel = new JLabel("������ȣ ");
	private JLabel bookId = new JLabel();
	private JLabel titleLabel = new JLabel("���� ");
	private JLabel title = new JLabel();
	private JLabel reservationLabel = new JLabel("������ ");
	private JTextField reservation = new JTextField(10);
	private JButton calendarBtn = new JButton(Main.getScaledImageIcon("calendar.png", 24, 24));
	private JButton requestBtn = new JButton("��û");
	
	private JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel combinePanel = new JPanel(new GridLayout(0, 1)); // idPanel, titlePanel, reservationPanel�� �����ִ� Panel
	private JPanel requestPanel = new JPanel(new FlowLayout());
	// -------------------------------------------------------------------------------------------------------------------
	
	// ----------------- �� ���� ��� ���� �ʵ� �� -----------------
	private int orderNum; // ����ȣ
	
	private JLabel noInventoryLabel1 = new JLabel("��� ����");
	private JLabel noInventoryLabel2 = new JLabel();
	// ---------------------------------------------------------

	
	public void compInit() throws Exception {
		orderNum = Main.sock.getOrderNum(dto.getId());
		
		bookId.setText(dto.getId());
		title.setText(dto.getTitle());

		bookIdLabel.setFont(new Font("Serif", Font.BOLD, 15));
		titleLabel.setFont(new Font("Serif", Font.BOLD, 15));
		reservationLabel.setFont(new Font("Serif", Font.BOLD, 15));
		reservation.setEditable(false);
		reservation.setHorizontalAlignment(JTextField.CENTER);
		calendarBtn.setPreferredSize(new Dimension(34, 34));
		// --------------------------------------------------------------
		idPanel.add(bookIdLabel);
		idPanel.add(bookId);
		titlePanel.add(titleLabel);
		titlePanel.add(title);

		combinePanel.add(idPanel);
		combinePanel.add(titlePanel);
		if(dto.getCurrent_quantity() == 0) {
			noInventoryLabel1.setFont(new Font("Serif", Font.BOLD, 15));
			noInventoryLabel1.setForeground(Color.RED);
			noInventoryLabel2.setFont(new Font("Serif", Font.PLAIN, 15));
			noInventoryLabel2.setText("  |  ����� : " + orderNum + "��");
			
			reservationPanel.add(noInventoryLabel1);
			reservationPanel.add(noInventoryLabel2);
		}else {
			reservationPanel.add(reservationLabel);
			reservationPanel.add(reservation);
			reservationPanel.add(calendarBtn);
		}
		combinePanel.add(reservationPanel);
		requestPanel.add(requestBtn);

		this.setLayout(new BorderLayout());
		this.add(combinePanel, BorderLayout.CENTER);
		this.add(requestPanel, BorderLayout.SOUTH);
	}

	public void eventInit() {
		requestBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(!MainFrame.login_state) {
						JOptionPane.showMessageDialog(Reservation.this, "���� �α����� ���ּ���.");
						dispose();
						new LoginDialog(search.Main.mf_addr, main.Main.sock_in, main.Main.sock_out, main.Main.customFont);
					}else {
						if(dto.getCurrent_quantity() == 0) {
							int[] result = Main.sock.reservation(dto.getId());
							if(result[0] == 0) { // ���� ��� ����
								JOptionPane.showMessageDialog(Reservation.this, "ȸ������ ����ȣ�� " + result[1] + "���Դϴ�.", "", JOptionPane.INFORMATION_MESSAGE);
								noInventoryLabel2.setText("  |  ����� : " + result[1] + "��");
							}else if(result[0] == 3){
								JOptionPane.showMessageDialog(Reservation.this, "����ȣ�� ����á���ϴ�.\n������ �ٽ� �̿����ּ���.", "", JOptionPane.ERROR_MESSAGE);
							}else if(result[0] == 4) {
								JOptionPane.showMessageDialog(Reservation.this, "�뿩 ���� Ƚ���� ��� ����ϼ̽��ϴ�.", "", JOptionPane.ERROR_MESSAGE);
							}
						}else {
							String reservationDate = reservation.getText();

							if(reservationDate.isEmpty()) {
								JOptionPane.showMessageDialog(Reservation.this, "�������� �������ּ���.", "", JOptionPane.ERROR_MESSAGE);
							}else {
								int result = Main.sock.reservation(reservationDate, dto.getId());
								if (result == 0) {
									JOptionPane.showMessageDialog(Reservation.this, "���� ����!", "", JOptionPane.INFORMATION_MESSAGE);
									Main.sock.revalidateCurrentQuantity(dto.getId());
									if(detail != null) { // �󼼺��� ���� ���� �ڿ� ����ȭ�鵵 ���̹Ƿ� �� �� ����
										detail.inventoryRevalidate();
										SearchedItemPanel.searchedItemList.get(dto.getId()).inventoryRevalidate();
									}else {
										SearchedItemPanel.searchedItemList.get(dto.getId()).inventoryRevalidate();
									}
								}else {
									JOptionPane.showMessageDialog(Reservation.this, "���� ����...", "", JOptionPane.ERROR_MESSAGE);
								}
								dispose();
							}
						}
					}
				}catch(Exception exception) {
					exception.printStackTrace();
				}
			}
		});

		reservation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new ReservationCalendar(reservation);
			}
		});

		this.calendarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ReservationCalendar(reservation);
			}
		});
	}

	public Reservation(BookDTO dto, Detail detail) {
		try {
			this.dto = dto;
			this.detail = detail;

			this.setSize(280, 230);
			this.setResizable(false);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setLocationRelativeTo(null);
			
			this.compInit();
			this.eventInit();

			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Reservation(BookDTO dto) {
		this(dto, null);
	}
}