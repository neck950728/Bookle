package search;

import java.util.ArrayList;

public class Paging {
	private int searchedCount;
	private ArrayList<BookDTO> currentPageContents = new ArrayList<>();
	
	private int currentPage; // ���� ������ ��ȣ
	private int startPage; // ���� �������� �������� �� ���� �������� ��ȣ
	private int endPage; // ���� �������� �������� �� ������ �������� ��ȣ
	private int totalPages; // �� ������ ����
	
	
	public Paging(int searchedCount, int currentPage, int size) { // size : �������� ������ ����
		this.searchedCount = searchedCount;
		this.currentPage = currentPage;
		
		if(0 == searchedCount) {
			startPage = 0;
			endPage = 0;
			totalPages = 0;
		}else {
			totalPages = searchedCount / size;
			if(searchedCount % size > 0) totalPages++;
			
			int modVal = currentPage % 5;
			startPage = currentPage / 5 * 5 + 1;
			
			if(0 == modVal) startPage -= 5; // ���� �������� 5�� ���(������ ������)�� ���, ���� �������� �߸� �����Ǵ� ���� ����
			
			endPage = startPage + 4;
			if(endPage > totalPages) endPage = totalPages;
			
			// ���� �������� �ش��ϴ� ������ ���ϱ�
			if(currentPage == endPage) {
				for(int i = currentPage * size - (size - 1);   i <= currentPage * size;   i++) {
					if(Sock.searchedContents.size() < i) break; // ������ �������� �������� ������ size���� ���� �� �����Ƿ�.(IndexOutOfBounds ���� ����)
					currentPageContents.add(Sock.searchedContents.get(i - 1)); // ArrayList�� index���� 0���� �����ϹǷ� -1�� ���־�� �Ѵ�.
				}
			}else {
				for(int i = currentPage * size - (size - 1);   i <= currentPage * size;   i++) {
					currentPageContents.add(Sock.searchedContents.get(i - 1));
				}
			}
		}
	}
	
	public boolean hasNoResult() {
		return 0 == searchedCount;
	}

	public boolean hasResult() {
		return searchedCount > 0;
	}
	
	public ArrayList<BookDTO> getCurrentPageContents(){
		return currentPageContents;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getStartPage() {
		return startPage;
	}
	
	public int getEndPage() {
		return endPage;
	}

	public int getTotalPages() {
		return totalPages;
	}
}