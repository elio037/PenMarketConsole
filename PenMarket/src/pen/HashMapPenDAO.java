package pen;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapPenDAO implements PenDAO {

	protected Map<Integer, PenVO> penDB = new HashMap<>();
	protected int penSeq = 111;
	
	@Override
	public boolean insertPen(PenVO pen) {
		pen.setPenNo(penSeq++);
		pen.setRegdate(new Date());
		penDB.put(pen.getPenNo(), pen);
		return true;
	}

	@Override
	public PenVO selectPen(int penNo) {
		return penDB.get(penNo);
	}

	@Override
	public List<PenVO> selectAllPens() {
		return new ArrayList<>(penDB.values());
	}

	@Override
	public boolean updatePen(PenVO newPen) {
		penDB.put(newPen.getPenNo(), newPen);
		return true;
	}

	@Override
	public boolean deletePen(int penNo) {
		return penDB.remove(penNo) != null;
	}

}
