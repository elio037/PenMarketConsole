package pen;

import java.util.List;

public interface PenDAO {
	boolean insertPen(PenVO pen);
	PenVO selectPen(int penNo);
	List<PenVO> selectAllPens();
	boolean updatePen(PenVO newPen);
	boolean deletePen(int penNo);
}
