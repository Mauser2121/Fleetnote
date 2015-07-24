import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;



public class Exporter {
	String fileName;
	List<String> colNames;
	List<List<String>> datas;

	public Exporter() {
		colNames = new ArrayList<String>();
		datas = new ArrayList<List<String>>();
		fileName = null;
	}

	public Exporter(String fileName) {
		this();
		this.fileName = fileName;
	}

	public void addColumn(String newName) throws Exception {
		if (datas.isEmpty() == true)
			this.colNames.add(newName);
		else
			throw new Exception(
					"You already put datas, you can't add a new column");
	}

	public void addData(List<String> data) throws Exception {
		if (colNames.size() != data.size()) {
			throw new Exception(
					"The size of the data sent isn't the same as the number of column you entered");
		}
		this.datas.add(data);
	}

	public void save() throws Exception {
		if (fileName == null) {
			throw new Exception("no filename has been set");
		}
		
		String texte = new String("");
		
		for (String curColumn : colNames) {
			texte += curColumn + ";" ;
		}
		texte += "\n";
		for (List<String> curRow : datas) {
			for (String curCell : curRow) {
				texte += curCell + ";";
			}
			texte += "\n";
		}
		
		BufferedWriter writer = null;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
		writer.write(texte, 0, texte.length());
		writer.close();
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public final List<String> getColNames() {
		return colNames;
	}

	public final List<List<String>> getDatas() {
		return datas;
	}

}
