package pen.file;

public interface FilePenDB {
	String DATA_FILE = "./data/penDB";
	void savePens();
	void loadPens();
}
