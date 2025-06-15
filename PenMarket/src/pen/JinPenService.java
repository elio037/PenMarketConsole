package pen;

import java.util.List;

public class JinPenService implements PenService {

	private PenDAO penDAO;
	
	public JinPenService(PenDAO penDAO) {
		this.penDAO = penDAO;
	}
	
	@Override
	public boolean registPen(PenVO pen) {
		return penDAO.insertPen(pen);
	}

	@Override
	public List<PenVO> listPens() {
		return penDAO.selectAllPens();
	}

	@Override
	public PenVO detailPenInfo(int penNo) {
		
		return penDAO.selectPen(penNo);
	}

	@Override
	public boolean updatePenPrice(int penNo, int price) {
		PenVO pen = penDAO.selectPen(penNo);
		
		if (pen != null) {
			pen.setPrice(price);
			penDAO.updatePen(pen);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean updatePenInstock(int penNo, int instock) {
		PenVO pen = penDAO.selectPen(penNo);
		
		if (pen != null) {
			pen.setInstock(instock);
			penDAO.updatePen(pen);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean removePen(int penNo) {
		/*
		PenVO pen = penDAO.selectPen(penNo);
		if (pen != null) {
			penDAO.deletePen(penNo);
			return true;
		}
		return false;
		 */
		return penDAO.deletePen(penNo);		
	}

}
