package search;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Revalidate {
	private JScrollPane scroll;
	private ArrayList<JButton> pageBtnList;
	private JButton beforeBtn;
	private JButton nextBtn;
	private JPanel southPanel;

	public static Paging paging;
	private JPanel tempPanel = new JPanel();
	
	
	public void searchedContentsRevalidate(int pageNum) {
		Main.sock.search(Main.keyword);
		paging = new Paging(Sock.searchedContents.size(), pageNum, 7);
		
		if(paging.hasResult()) { // �˻� ����� �ִ� ���
			scroll.remove(tempPanel); // ���� Panel ����
			
			ArrayList<BookDTO> currentPageContents = paging.getCurrentPageContents();
			tempPanel = new SearchedItemPanel(currentPageContents);
			scroll.setViewportView(tempPanel);
			// scroll.revalidate();
			// scroll.repaint();
		}else { // �˻� ����� ���� ���
			JPanel hasNoResultPanel = new JPanel(new GridBagLayout());
			JLabel img = new JLabel(Main.getScaledImageIcon("hasNoResult.png", 200, 180));
			JLabel msg = new JLabel("�˻� ����� �������� �ʽ��ϴ�.");
			
			msg.setFont(new Font("Serif", Font.BOLD, 40));
			msg.setForeground(Color.BLACK);
			msg.setHorizontalAlignment(JLabel.CENTER);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0; gbc.gridy = 0; hasNoResultPanel.add(img, gbc);
			gbc.gridx = 0; gbc.gridy = 1; hasNoResultPanel.add(msg, gbc);
			scroll.remove(tempPanel);
			scroll.setViewportView(hasNoResultPanel);
			// scroll.revalidate();
			// scroll.repaint();
		}
		
		// SwingUtilities.invokeLater �޼��带 ����� ���� : http://blog.naver.com/dngu_icdi/221272600077 ����
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMinimum()); // Scroll Bar �ֻ������ �̵���Ű��
			}
		});
	}
	
	public void buttonRevalidate() {
		if(paging.hasResult()) { // �˻� ����� �ִ� ���
			int startPage = paging.getStartPage();
			int endPage = paging.getEndPage();
			int pageNum = paging.getStartPage();
			
			// ------------ �� ������ ��ư ��ȣ �� ActionListener �缳�� �� ------------
			for(JButton btn : pageBtnList) {
				// ������ ����Ǿ� �ִ� ActionListener ����
				if(btn.getActionListeners().length != 0) {
					for(ActionListener temp : btn.getActionListeners()) {
						btn.removeActionListener(temp);
					}
				}
				
				btn.setText(String.valueOf(pageNum));
				pageNum++;
			}

			for(JButton btn : pageBtnList) {
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						searchedContentsRevalidate(Integer.parseInt(btn.getText()));
						currentPageButtonMark(btn.getText());
					}
				});
			}
			// -------------------------------------------------------------------

			// ----------- �� ������ ��ư ���� ȭ�� ���� �� -----------
			southPanel.removeAll();
			
			if(startPage > 5) {
				southPanel.add(beforeBtn);
			}
	
			for(JButton btn : pageBtnList) {
				if(Integer.parseInt(btn.getText()) <= endPage) {
					southPanel.add(btn);
				}else {
					break;
				}
			}
			
			if(endPage < paging.getTotalPages()) {
				southPanel.add(nextBtn);
			}

			currentPageButtonMark(pageBtnList.get(0).getText());
			southPanel.revalidate();
			southPanel.repaint();
			// ---------------------------------------------------
		}else { // �˻� ����� ���� ���
			southPanel.removeAll();
			southPanel.revalidate();
			southPanel.repaint();
		}
	}
	
	public void currentPageButtonMark(String pageNum) {
		for(JButton btn : pageBtnList) {
			if(pageNum.equals(btn.getText())){
				btn.setBackground(Color.CYAN);
			}else {
				btn.setBackground(Color.LIGHT_GRAY);
			}
		}
	}
	
	public Revalidate(JScrollPane scroll, ArrayList<JButton> pageBtnList, JButton beforeBtn, JButton nextBtn, JPanel southPanel) {
		this.scroll = scroll;
		this.pageBtnList = pageBtnList;
		this.beforeBtn = beforeBtn;
		this.nextBtn = nextBtn;
		this.southPanel = southPanel;
	}
}