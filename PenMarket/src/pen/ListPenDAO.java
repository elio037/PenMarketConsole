package pen;

import java.util.LinkedList;
import java.util.List;

public class ListPenDAO implements PenDAO {

	private List<PenVO> penList = new LinkedList<PenVO>();
	private int penSeq = 111; // 책번호 1씩 증가
	
	@Override
	public boolean insertPen(PenVO pen) {
		pen.setPenNo(penSeq++);
		penList.add(pen);
		return true;
	}

	@Override
	public PenVO selectPen(int penNo) {
		for (PenVO pen : penList) {
			if (pen.getPenNo() == penNo)
				return pen;
		}
		return null;
	}

	@Override
	public List<PenVO> selectAllPens() {
		return penList;
	}

	@Override
	public boolean updatePen(PenVO newPen) {
		for (int i = 0; i < penList.size(); i++) {
			if (penList.get(i).getPenNo() == newPen.getPenNo()) {
				penList.set(i, newPen);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deletePen(int PenNo) {

		for (PenVO pen : penList) {
			if (pen.getPenNo() == PenNo) {
				penList.remove(pen);
				return true;
			}
		}
		return false;
	}

}
