package search;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.freixas.jcalendar.JCalendar;

public class ReservationCalendar extends JDialog{
	private JTextField reservation;
	private Calendar serverDate = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	// ----------------------------------------------------------------------------------------------
	private JCalendar cal = new JCalendar();
	private JButton confirmBtn = new JButton("�Ϸ�");
	private JPanel combinePanel = new JPanel(new FlowLayout()); // cal�� confirmBtn�� �����ִ� Panel
	

	private void compInit() {
		cal.setDate(new Date(GetServerTime.serverTime)); // ������ ���� ��¥�� ǥ�õǰ� �ϱ� ���� ��¥�� �ٽ� ����
		serverDate.setTime(new Date(GetServerTime.serverTime));
		
		confirmBtn.setPreferredSize(new Dimension(70, 40));
		combinePanel.add(cal);
		combinePanel.add(confirmBtn);
		
		this.add(combinePanel);
	}
	
	private void eventInit() {
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Date convertedServerDate = setToTimeZero(serverDate); // ��¥���� ���ϱ� ���� �ð����� 0���� ����
				
				Calendar selectedDate = cal.getCalendar();
				Date convertedSelectedDate = setToTimeZero(cal.getCalendar());

				if(convertedSelectedDate.compareTo(convertedServerDate) < 0) { // ����
					JOptionPane.showMessageDialog(ReservationCalendar.this, "�߸��� �����Դϴ�.\n(���� ��¥)", "", JOptionPane.ERROR_MESSAGE);
				}else if(convertedSelectedDate.compareTo(convertedServerDate) > 0) { // �̷�
					Calendar temp = Calendar.getInstance();
					temp.setTime(convertedServerDate);
					temp.add(Calendar.DATE, 3);
					Date reservationPossiblePeriod = new Date(temp.getTimeInMillis());
					if(convertedSelectedDate.compareTo(reservationPossiblePeriod) <= 0) { // ������ ���� ��¥�κ��� 3�� ���ı����� ���� ����
						reservation.setText(sdf.format(new Date(selectedDate.getTimeInMillis())));
						dispose();
					}else {
						int month = temp.get(Calendar.MONTH) + 1; // 0 : 1��
						int days = temp.get(Calendar.DATE);
						JOptionPane.showMessageDialog(ReservationCalendar.this,
																	   "������Ⱓ ������ �Ұ����մϴ�.\n(�ִ� " + month + "�� " + days + "�ϱ��� ������ �����մϴ�.)",
																	   "", JOptionPane.ERROR_MESSAGE);
					}
				}else if(convertedSelectedDate.compareTo(convertedServerDate) == 0){ // ����
					reservation.setText(sdf.format(new Date(selectedDate.getTimeInMillis())));
					dispose();
				}
			}
		});
	}
	
	/*
 		�ð����� 0���� �������ִ� �޼���
 		ex) 2018�� 1�� 20�� 11�� 30�� 30��  ��  2018�� 1�� 20�� 00�� 00�� 00��
	*/
	private Date setToTimeZero(Calendar cal) {
		Date date = null;
		
		try {
			date = sdf.parse(cal.get(Calendar.YEAR) + "/" +
								  (cal.get(Calendar.MONTH) + 1) + "/" +
								   cal.get(Calendar.DATE));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return date;
	}
	
	public ReservationCalendar(JTextField reservation) {
		this.reservation = reservation; 
		
		this.setSize(350, 340);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		compInit();
		eventInit();
		
		this.setVisible(true);
	}
}