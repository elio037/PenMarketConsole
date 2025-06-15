package order;

import java.util.Date;
import java.util.List;

import pen.PenService;

public class OrderServiceImpl implements OrderService {
	
	private OrderDAO orderDAO;
	private PenService penService;
	
	private final int COMPLETE = 10;
	
	public OrderServiceImpl(OrderDAO orderDAO, PenService penService) {
		this.orderDAO = orderDAO;
		this.penService = penService;
	}

	@Override
	public boolean orderItems(OrderVO order) {

		// 1. 주문 정보 추가 (주문일, 배송상태, 배송완료일)
		order.setOrderDate(new Date());
		order.setStatus(COMPLETE);
		order.setDeliverDate(new Date());
				
		// 2. 도서 재고량 update
		for (OrderItemVO item : order.getOrderItemList()) {
			int penNo = item.getPenNo();
			int newInstock = penService.detailPenInfo(penNo).getInstock() - item.getQuantity();
			if (newInstock >= 0) {
				penService.updatePenInstock(penNo, newInstock);
			} else {
				return false;
			}
		}
		
		// 3. 주문 정보 DB에 추가
		orderDAO.insertOrder(order);		
		return true;
	}

	@Override
	public List<OrderVO> listMyOrders(String memberId) {
		return orderDAO.selectOrdersOfMember(memberId);
	}

	@Override
	public List<OrderVO> listAllOrder() {
		return orderDAO.selectAllOrder();
	}

}
