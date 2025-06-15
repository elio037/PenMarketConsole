package app;

import java.util.ArrayList;
import java.util.List;

import pen.PenService;
import pen.PenVO;
import pen.JinPenService;
import pen.file.ObjFileHashMapPenDAO;
import cart.CartItemVO;
import cart.CartService;
import cart.CartServiceImpl;
import cart.HashMapCartDAO;
import member.JinMemberService;
import member.MemberService;
import member.MemberVO;
import member.ObjFileHashMapMemberDAO;
import order.ObjFileHashMapOrderDAO;
import order.OrderItemVO;
import order.OrderService;
import order.OrderServiceImpl;
import order.OrderVO;

public class PenMarketConsoleApp {

	String[] startMenuList = {"종료", "상품 목록", "로그인", "회원 가입"};
	String[] adminMenuList = {"로그아웃", "상품 목록", "상품 등록", "상품 정보 수정", "상품 삭제", "회원 목록", "주문 목록"};
	String[] memberMenuList = {"로그아웃", "상품 목록", "상품 주문", "주문 목록", "장바구니 상품 담기", "장바구니 보기", "내 정보"};
	String[] cartMenuList = {"돌아가기", "상품 주문", "상품 삭제", "장바구니 비우기"};
	String[] myInfoMenuList = {"돌아가기", "비밀번호 변경", "회원 탈퇴"};
	
	final String ADMIN_ID = "admin";
	final String ADMIN_PWD = "1234";
	final String ADMIN_NAME = "관리자";
	
	final String CONFIRM = "yes";
	
	PenService bs = new JinPenService(new ObjFileHashMapPenDAO());
	MemberService ms = new JinMemberService(new ObjFileHashMapMemberDAO());
	OrderService os = new OrderServiceImpl(new ObjFileHashMapOrderDAO(), bs);
	CartService cs = new CartServiceImpl(new HashMapCartDAO());
	MemberVO loggedMember;
	
	MyAppReader input = new MyAppReader();
	
	public static void main(String[] args) {
		PenMarketConsoleApp app = new PenMarketConsoleApp();
		app.displayWelcome();
		app.controlStartMenu();
	}

	private void displayWelcome() {
		System.out.println("***********************************");
		System.out.println("*  어서오세요 Jin PenMarket입니다  *");
		System.out.println("***********************************");
	}

	private void controlStartMenu() {
		int menu;
		do {
			menu = selectMenu(startMenuList);
			
			switch (menu) {
			case 1: menuPenList(); break;
			case 2: menuLogin(); break;
			case 3: menuSignUp(); break;
			case 0: menuExit(); break;
			default : menuWrongNumber();
			}
			
		} while (menu != 0);
		
	}

	private void menuWrongNumber() {
		System.out.println("없는 메뉴입니다.");
		
	}

	private void menuExit() {
		System.out.println("Jin PenMarket 서비스를 종료합니다.");
		
	}

	private void menuPenList() {
		System.out.println("*** 상품 목록 ***");
		displayPenList();
	}
	
	private void displaypenList() {
		List<penVO> penList = bs.listPens();
		System.out.println("---------------------------------------");
		if (penList.isEmpty()) {
			System.out.println("등록된 상품이 없습니다.");
		} else {
			for (penVO pen : penList) {
				System.out.println(pen);
			}
		}
		System.out.println("---------------------------------------");	
	}

	private void menuLogin() {
		System.out.println("*** 로그인 ***");
		String id = input.readString(">> id : ");
		String password = input.readString(">> password : ");
		
		// 관리자 -> 관리자 메뉴
		if (id.equals(ADMIN_ID) && password.equals(ADMIN_PWD)) {
			loggedMember = new MemberVO(ADMIN_ID, ADMIN_PWD, ADMIN_NAME);
			System.out.println("관리자 모드로 변경합니다.");
			controlAdminMenu();
		} else {
			// 회원 -> 회원 메뉴
			loggedMember = ms.login(id, password);
			
			if (loggedMember != null) {
				System.out.println("[로그인] " + loggedMember.getUsername() + "님 안녕하세요.");
				controlMemberMenu();
			} else {
				// 아니면
				System.out.println("로그인을 하지 못했습니다.");
			}
		}
		
	}

	private void controlMemberMenu() {
		int menu;
		do {
			menu = selectMenu(memberMenuList);
			// "로그아웃", "도서 목록", "도서 주문", "주문 목록", "장바바구니 도서 담기", "장바구니 보기", "내 정보"
			switch (menu) {
			case 1 : menuPenList(); break;
			case 2 : menuPenOrder(); break;
			case 3 : menuOrderList(); break;
			case 4 : menuAddPen2Cart(); break;
			case 5 : menuCartView(); break;
			case 6 : menuMyInfo(); break;
			case 0 : menuLogout(); break;
			default : menuWrongNumber();
			}
		} while (menu != 0);

	}

	private void menuBookOrder() {
		System.out.println("*** 상품 주문 ***");
		displayAvailablePenList();
		int penNo = input.readInt(">> 상품번호 : ");
		PenVO pen = bs.detailPenInfo(penNo);
		
		if (pen == null) {
			System.out.println("없는 상품 입니다.");
			return;
		}
		
		int quantity = input.readInt(">> 주문량 (" + pen.getInstock() + "개 이내) : ");
		if (quantity > pen.getInstock()) {
			System.out.println("주문량이 재고량보다 큽니다.");
			return;
		}
		
		// 주문 도서 목록
		List<OrderItemVO> orderItemList = new ArrayList<>();
		int price = pen.getPrice() * quantity;
		orderItemList.add(new OrderItemVO(penNo, quantity, price));
		
		// 주문 정보 생성
		OrderVO order = new OrderVO(loggedMember.getId(), orderItemList, price);
		// 배송 정보 추가
		setDeliveryInfo();
		order.setMobile(loggedMember.getMobile());
		order.setAddress(loggedMember.getAddress());
		
		if (os.orderItems(order)) {
			System.out.println("주문이 완료되었습니다.");
			System.out.println("배송이 완료되었습니다.");
		} else {
			System.out.println("주문을 하지 못했습니다.");
		}
	}
	
	private void setDeliveryInfo() {
		if (loggedMember.getMobile() == null) {
			System.out.println("*** 배송 정보 입력 ***");
			
			String mobile = input.readString(">> 모바일 번호 : ");
			String email = input.readString(">> 이메일 주소 : ");
			String address = input.readString(">> 주소 : ");
			
			loggedMember.setMobile(mobile);
			loggedMember.setEmail(email);
			loggedMember.setAddress(address);
			
			ms.addMemberInfo(loggedMember.getId(), mobile, email, address);
			//loggedMember = ms.detailMemberInfo(loggedMember.getId());
			
		}
	}

	private void displayAvailablePenList() {
		List<PenVO> penList = ps.listPens();
		System.out.println("---------------------------------------");
		if (penList.isEmpty()) {
			System.out.println("주문 가능한 상품이 없습니다.");
		} else {
			int count = 0;
			for (PenVO pen: penList) {
				if (pen.getInstock() > 0) {
					System.out.println(pen);
					count++;
				}
			}
			if (count == 0) 
				System.out.println("주문 가능한 상품이 없습니다.");
		}
		System.out.println("---------------------------------------");	
		
	}

	private void menuAddPen2Cart() {
		System.out.println("*** 장바구니에 도서 담기 ***");
		
		displayAvailablePenList();
		int penNo = input.readInt(">> 상품 번호 : ");
		PenVO pen = ps.detailPenInfo(penNo);
		
		if (pen == null) {
			System.out.println("없는 상품 입니다.");
			return;
		}
		
		int quantity = input.readInt(">> 주문량 (" + pen.getInstock() + "개 이내) : ");
		if (quantity > pen.getInstock()) {
			System.out.println("주문량이 재고량보다 큽니다.");
			return;
		}
		
		// 이미 장바구니에 있는지 확인
		// 없으면, 장바구니에 넣기
		if (cs.getCartItemInfo(penNo) == null) {
			cs.addItem2Cart(new CartItemVO(penNo, quantity));
			System.out.println("장바구니에 추가했습니다.");
		} else {
			System.out.println("이미 장바구니에 있는 상품입니다.");
		}
		
		
	}

	private void menuCartView() {
		System.out.println("*** 장바구니 보기 ***");
		displayCartItemList();
		
		if (!cs.isCartEmpty())controlCartMenu();
		
	}

	private void displayCartItemList() {	
		if (cs.isCartEmpty()) {
			System.out.println("장바구니가 비어 있습니다.");
		} else {
			System.out.println("---------------------------------------");	
			for (CartItemVO item : cs.listCartItems()) {
				System.out.println(item);
			}
			System.out.println("---------------------------------------");
		}
	}

	private void controlCartMenu() {
		int menu;
		do {
			menu = selectMenu(cartMenuList);
			//"돌아가기", "상품 주문", "상품 삭제", "장바구니 비우기"}
			switch (menu) {
			case 1 : menuCartOrder(); break;
			case 2 : menuCartPenDelete(); break;
			case 3 : menuCartClear();
			case 0 : break;
			default : menuWrongNumber();
			}
		} while (menu != 0 && !cs.isCartEmpty());	
	}

	
	private void menuCartOrder() {
		System.out.println("*** 장바구니 상품 주문 ***");
		displayCartItemList();
		
		// 주문 상품  목록
		List<OrderItemVO> orderItemList = new ArrayList<>();
		int totalPrice = 0;
		for (CartItemVO item : cs.listCartItems()) {
			PenVO pen = ps.detailPenInfo(item.getBookNo());
			int price = pen.getPrice() * item.getQuantity();
			totalPrice += price;
			orderItemList.add(
				new OrderItemVO(item.getPenNo(), item.getQuantity(), price)
			);
		}
						
		// 주문 정보 생성
		OrderVO order = new OrderVO(loggedMember.getId(), orderItemList, totalPrice);
		
		// 배송 정보 추가
		setDeliveryInfo();
		order.setMobile(loggedMember.getMobile());
		order.setAddress(loggedMember.getAddress());
		
		displayOrderInfo(order);
		
		String confirm = input.readString(">> 위와 같은 내용을 주문 및 결제를 진행하시겠습니까? ('" 
									+ CONFIRM + "'이면 주문 실행) : " );
		if (confirm.equals(CONFIRM)) {
			if (os.orderItems(order)) {
				cs.clearCart();
				System.out.println("주문이 완료되었습니다.");
				System.out.println("배송이 완료되었습니다.");
				
			} else {
				System.out.println("주문을 하지 못했습니다.");
			}
		} else {
			System.out.println("주문이 취소되었습니다.");
		}
		
		
	}

	private void displayOrderInfo(OrderVO order) {
		System.out.println(order);
	}

	private void menuCartPenDelete() {
		System.out.println("*** 장바구니 상품 삭제 ***");
		displayCartItemList();
		int penNo = input.readInt(">> 상품 번호 :");
		CartItemVO item = cs.getCartItemInfo(penNo);
		if (item == null) {
			System.out.println("없는 상품 입니다.");
		} else {
			cs.removeCartItem(penNo);
			System.out.println("장바구니에서 상품을 삭제하였습니다.");
		}
		displayCartItemList();
	}

	private void menuCartClear() {
		System.out.println("*** 장바구니 비우기 ***");
		String confirm = input.readString(">> 장바구니의 모든 상품을 삭제하시겠습니까? ('" + CONFIRM + "'이면 삭제) : ");
		if (confirm.equals(CONFIRM)) {
			cs.clearCart();
			System.out.println("장바구니의 모든 상품을 삭제하였습니다.");
		} else {
			System.out.println("장바구니 비우기가 취소되었습니다.");
		}
		
	}

	private void menuMyInfo() {
		System.out.println("*** 내 정보 ***");
		System.out.println(loggedMember);
		
		controlMyInfoMenu();
	}

	private void controlMyInfoMenu() {
		int menu;
		do {
			menu = selectMenu(myInfoMenuList);
			// "돌아가기", "비밀번호 변경", "회원 탈퇴"
			switch (menu) {
			case 1 : menuUpatePassword(); break;
			case 2 : menuMemberExit(); break;
			case 0 : break;
			default : menuWrongNumber();
			}
		} while (menu != 0 && loggedMember != null);
		
	}

	private void menuUpatePassword() {
		System.out.println("*** 비밀번호 수정 ***");
		String oldPassword = input.readString(">> 기존 비밀번호 : ");
		String newPassword = input.readString(">> 새 비밀번호 : ");
		if (ms.updatePassword(loggedMember.getId(), oldPassword, newPassword)) {
			System.out.println("[비밀번호 수정] 비밀번호를 수정했습니다.");
		} else {
			System.out.println("[비밀번호 수정 실패] 비밀번호 수정에 실패했습니다.");
		}
	}

	private void menuMemberExit() {
		System.out.println("*** 회원 탈퇴 ***");
		String password = input.readString(">> 비밀번호 : ");
		if (ms.removeMember(loggedMember.getId(), password)) {
			System.out.println("[회원 탈퇴] 회원정보, 주문정보를 삭제하였습니다. 그동안 서비스를 이용해 주셔서 감사합니다.");
			loggedMember = null;
		} else {
			System.out.println("[회원 탈퇴 실패] 회원 탈퇴 처리에 실패했습니다.");
		}
		
	}

	private void controlAdminMenu() {
		int menu;
		do {
			menu = selectMenu(adminMenuList);
			// "로그아웃", "상품 목록", "상품 등록", "상품 정보 수정", "상품 삭제", "회원 목록", "주문 목록"
			switch (menu) {
			case 1: menuPenList(); break;
			case 2: menuPenRegist(); break;
			case 3: menuPenUpdate(); break;
			case 4: menuPenRemove(); break;
			case 5: menuMemberList(); break;
			case 6: menuOrderList(); break;
			case 0: menuLogout(); break;
			default : menuWrongNumber();
			}
			
		} while (menu != 0 && loggedMember != null);
		
	}

	private void menuBookRegist() {
		
		System.out.println("*** 상품  등록 ***");
		String title = input.readString(">> 상품 이름 : ");
		String origin = input.readString(">> 원산지 : ");
		String brand = input.readString(">> 브랜드: ");
		int price = input.readInt(">> 가격 : ");
		int instock = input.readInt(">> 재고량 : ");
		
		if (bs.registPen(new PenVO(title, origin, brand, price, instock))) {
			System.out.println("상품을 등록했습니다.");
			displayPenList();
		} else {
			System.out.println("상품 등록에 실패했습니다.");
		}
		
	}

	private void menuPenUpdate() {
		System.out.println("*** 상품 정보 수정 ***");
		displayPenList();
		int penNo = input.readInt(">> 상품 번호 :");
		
		int select = input.readInt(">> 수정할 정보 선택 (1. 가격, 2. 재고량) : ");
		if (select == 1) { // 가격
			int price = input.readInt(">> 새 가격 : ");
			if (bs.updatePenPrice(penNo, price)) {
				System.out.println("[상품 정보 수정] 가격을 수정하였습니다.");
			} else {
				System.out.println("[상품 정보 수정 오류] 없는 상품입니다.");
			}
			
		} else if (select == 2) {// 재고량
			int instock = input.readInt(">> 새 재고량 :");
			if (bs.updatePenInstock(penNo, instock)) {
				System.out.println("[상품 정보 수정] 재고량을 수정하였습니다.");
			} else {
				System.out.println("[상품 정보 수정 오류] 없는 상품입니다.");
			}
		} else {
			System.out.println("[상품 정보 수정 취소] 지원하지 않는 기능입니다.");
		}
		
	}

	private void menuPenRemove() {
		System.out.println("*** 상품 삭제 ***");
		displayPenList();
		int penNo = input.readInt(">> 상품 번호 :");
		String confirm = input.readString("선택한 상품을 삭제하시겠습니까? ('" + CONFIRM + "'를 입력하면 실행) : ");
		if (confirm.equals(CONFIRM)) {
			if (bs.removePen(penNo)) {
				System.out.println("[상품 삭제] 상품를 삭제했습니다.");
			} else {
				System.out.println("[상품 삭제 오류] 없는 상품입니다.");
			}
		} else {
			System.out.println("[상품 삭제 취소] 상품 삭제를 취소했습니다.");
		}
	}

	private void menuMemberList() {
		System.out.println("*** 회원 목록 ***");
		System.out.println("---------------------------------------");
		List<MemberVO> memberList = ms.listMembers();
		if (memberList.isEmpty()) {
			System.out.println("회원이 없습니다.");
		} else {
			for (MemberVO member : memberList) {
				System.out.println(member);
			}
		}
		System.out.println("---------------------------------------");
		
	}

	private void menuOrderList() {
		if (loggedMember.getId().equals(ADMIN_ID)) {
			System.out.println(os.listAllOrder());
		} else {
			System.out.println(os.listMyOrders(loggedMember.getId()));
		}
		
	}

	private void menuLogout() {
		
		System.out.println("[로그아웃] " + loggedMember.getUsername() + "님, 안녕히 가십시오.");
		loggedMember = null;
		
	}

	private void menuSignUp() {
		System.out.println("*** 회원 가입 ***");
		String id = input.readString(">> id : ");
		String password = input.readString(">> password : ");
		String username = input.readString(">> username : ");
		
		if (ms.registMember(new MemberVO(id, password, username))) {
			System.out.println("회원 가입이 완료되었습니다. 서비스 이용을 위한 로그인 해주세요.");
		} else {
			System.out.println("회원 가입에 실패하였습니다.");
		}
		
	}

	private int selectMenu(String[] menuList) {

		System.out.println("-------------------------------");
		for (int i = 1; i < menuList.length; i++) {
			System.out.println(i + ". " + menuList[i]);
		}
		System.out.println("0. " + menuList[0]);
		System.out.println("-------------------------------");
		return input.readInt(">> 메뉴 선택 : ");
	}
}
