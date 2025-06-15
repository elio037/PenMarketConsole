package test;

import pen.PenDAO;
import pen.PenService;
import pen.PenVO;
import pen.JinPenService;
import pen.HashMapPenDAO;
import pen.ListPenDAO;
import pen.file.FilePenDB;
import pen.file.ObjFileHashMapPenDAO;

public class PenServiceTest {

	static PenDAO penDAO = new ObjFileHashMapPenDAO();
	static PenService ps = new JinPenService(penDAO);
	
	public static void main(String[] args) {
		
		//testSavePens();
		testLoadPens();
	}
	
	public static void testLoadPens() {
		((FilePenDB)penDAO).loadPens();
		System.out.println(ps.listBooks());
		
		bs.registBook(new PenVO("test4", "hye", "kopo", 15000, 9));
		System.out.println(ps.listPens());
		((FilePenDB)penDAO).savePens();
	}
	
	public static void testSavePens() {
		// 상품 등록
		ps.registPen(new PenVO("test", "jongin", "kopo", 10000, 10));
		ps.registPen(new PenVO("test2", "curi", "home", 1000, 5));
		ps.registPen(new PenVO("test3", "hye", "home", 3000, 15));
		((FilePenDB)penDAO).savePens();
	}
	
	static void serviceTest() {
		// 책상세정보
				System.out.println(ps.detailPenInfo(112));
				
				// 상품정보 수정
				ps.updatePenInstock(111, 15);
				ps.updatePenPrice(112, 30000);
				
				System.out.println(ps.listPens());
				
				//상품 삭제
				ps.removePen(113);
				
				System.out.println(ps.listPens());
				
				System.out.println();
				
				ps = new JinPenService(new HashMapPenDAO());
				
				// 책등록
				ps.registPen(new PenVO("test", "hyejeong", "kopo", 10000, 10));
				ps.registPen(new PenVO("test2", "curi", "home", 1000, 5));
				ps.registPen(new PenVO("test3", "hye", "home", 3000, 15));
				
				// 책목록
				System.out.println(ps.listPens());
				
				// 책상세정보
				System.out.println(ps.detailPenInfo(112));
				
				// 책정보 수정
				ps.updatePenInstock(111, 15);
				ps.updatePenPrice(112, 30000);
				
				System.out.println(ps.listPens());
				
				// 책 삭제
				ps.removePen(113);
				
				System.out.println(ps.listPens());
	}
	
	static void DAOTest() {
		BookDAO bookDAO = new ListBookDAO();
		
		// 책등록
		bookDAO.insertBook(new BookVO("test", "hyejeong", "kopo", 10000, 10));
		bookDAO.insertBook(new BookVO("test2", "curi", "home", 1000, 5));
		bookDAO.insertBook(new BookVO("test3", "hye", "home", 3000, 15));
		
		// 책정보
		System.out.println(bookDAO.selectAllBooks());
		System.out.println(bookDAO.selectBook(112));
				
		// 책 정보 수정
		BookVO book = bookDAO.selectBook(111);
		book.setInstock(15);
		bookDAO.updateBook(book);
		
		book = bookDAO.selectBook(112);
		book.setPrice(30000);
		bookDAO.updateBook(book);
		
		System.out.println(bookDAO.selectAllBooks());
		
		// 책 삭제
		bookDAO.deleteBook(113);
		System.out.println(bookDAO.selectAllBooks());
	}
}
