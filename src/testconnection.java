import java.util.ArrayList;
import java.util.List;

import com.fleetnote.business.bean.references.TrVehicle;
import com.fleetnote.business.dao.references.TrVehicleDAO;


public class testconnection {

	
	public static void main(String[] args) 
	{
		TrVehicleDAO tdao = new TrVehicleDAO();
		List<TrVehicle> list =  tdao.readAll();
	
		for(int i=0;i<list.size();i++)
			System.out.println(list.get(i).getVersion().getDescriptionTrVersion());
	}
}
