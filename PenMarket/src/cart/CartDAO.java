package cart;

import java.util.List;

public interface CartDAO {
	boolean insertCartItem(CartItemVO item);
	CartItemVO selectCartItem(int penNo);
	List<CartItemVO> selectAllCartItem();
	boolean deleteCartItem(int penNo);
	boolean clear();
}
