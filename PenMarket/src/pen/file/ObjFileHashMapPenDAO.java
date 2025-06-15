package pen.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pen.PenVO;
import pen.HashMapPenDAO;

public class ObjFileHashMapPenDAO extends HashMapPenDAO implements FilePenDB {

	private String dataFilename = DATA_FILE + ".obj";
	
	public ObjFileHashMapPenDAO() {
		loadPens();
	}
	
	@Override
	public void savePens() {
		
		try (
			FileOutputStream fos = new FileOutputStream(dataFilename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
				
		) {
			oos.writeObject(penDB);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void loadPens() {
		
		try (
			FileInputStream fis = new FileInputStream(dataFilename);
			ObjectInputStream ois = new ObjectInputStream(fis);
		) {
			
			penDB = (Map<Integer, PenVO>)ois.readObject();
			penSeq = Collections.max(penDB.keySet()) + 1;
			
		} catch (FileNotFoundException e) {
			System.out.println("[DB로딩] " + dataFilename + "가 없습니다.");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean insertPen(PenVO pen) {
		boolean result = super.insertPen(pen);
		if (result) savePens();
		return result;
	}

	@Override
	public boolean updatePen(PenVO newPen) {
		boolean result = super.updatePen(newPen);
		if (result) savePens();
		return result;
	}
	
	@Override
	public boolean deletePen(int penNo) {
		boolean result = super.deletePen(penNo);
		if (result) savePens();
		return result;
	}

}