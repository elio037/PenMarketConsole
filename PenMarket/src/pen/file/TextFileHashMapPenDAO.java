package pen.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pen.PenVO;
import pen.HashMapPenDAO;

public class TextFileHashMapPenDAO extends HashMapPenDAO implements FilePenDB {

	private String dataFilename = DATA_FILE + ".txt";
	private final String DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";
			
	@Override
	public void savePens() {
		
		try (
			FileWriter fw = new FileWriter(dataFilename);
			PrintWriter pw = new PrintWriter(fw);
		) {
			
			for (PenVO pen : penDB.values()) {
				pw.println(pen.getPenNo());
				pw.println(pen.getTitle());
				pw.println(pen.getOrigin());
				pw.println(pen.getBrand());
				pw.println(pen.getPrice());
				pw.println(pen.getInstock());
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				pw.println(sdf.format(pen.getRegdate()));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		
	}

	@Override
	public void loadPens() {

		try ( FileReader fr = new FileReader(dataFilename);
			  BufferedReader br = new BufferedReader(fr);
		) {
			
			while (br.ready()) {
				int penNo = Integer.parseInt(br.readLine());
				String title = br.readLine().strip();
				String origin = br.readLine().strip();
				String brand = br.readLine().strip();
				int price = Integer.parseInt(br.readLine());
				int instock = Integer.parseInt(br.readLine());
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				Date regdate = sdf.parse(br.readLine());
				
				penDB.put(penNo, new PenVO(penNo, title, origin, brand, price, instock, regdate));
				
				if (penSeq <= penNo) penSeq = penNo + 1;
			}
		} catch (FileNotFoundException e) {
			System.out.println("[로딩] " + dataFilename + "이 없습니다.");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
	}

}