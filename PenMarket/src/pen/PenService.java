package pen;

import java.util.List;

public interface PenService {
	boolean registPen(PenVO pen);
	List<PenVO> listPens();
	PenVO detailPenInfo(int penNo);
	boolean updatePenPrice(int penNo, int price);
	boolean updatePenInstock(int penNo, int instock);
	boolean removePen(int pebNo);
}
