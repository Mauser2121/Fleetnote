import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import webdriver.MyWebDriver;

import com.fleetnote.business.bean.references.Make;
import com.fleetnote.business.dao.references.MakeDAO;




public class AutoplusScrapper extends Thread{

	private ArrayList<String> lMake ;

	private WebDriver mDriver;
	private WebDriver mDriver2;
	
	private ArrayList<String> Sortie;
	private String ligneSortie;
	
	
	public static void main(String[] args) 
	{
		AutoplusScrapper lnew = new AutoplusScrapper();		
	}	
	
	public AutoplusScrapper(int in)
	{
	}
	
	public void run()
	{
//		HtmlUnitDriver test = new HtmlUnitDriver();
//		
	}
	

	public AutoplusScrapper()
	{
		 lMake = new ArrayList<String>();
		 MakeDAO mdao = new MakeDAO();
		 List<Make> makes = mdao.getMakeByDescription("Peugeot");
		 
		 for(int i=0;i<makes.size();i++)
			 lMake.add(makes.get(i).getDescriptionTrMake().replace(" ", "-"));
		 
		 Sortie = new ArrayList<String>();
		 try {
			parcourirMarque();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	
	public void parcourirMarque() throws IOException
	{
		
		 mDriver = new MyWebDriver();
		 mDriver2 = new MyWebDriver(); 
		
		 Iterator<String> it = lMake.iterator();
		 //Marque
		 while(it.hasNext())
		 {
			 String marque = it.next();
			 mDriver.get( "http://voiture.autoplus.fr/list/make/"+"Citroen");
			 WebElement nbreResult = mDriver.findElement(By.xpath("//*[@id='pnf-result']/table/tfoot/tr/td[2]"));
			 Integer maxpage = Integer.decode(nbreResult.getText().substring(nbreResult.getText().lastIndexOf("/")+1));
			 for(int i =1;i<maxpage+1;i++)	 
			 {
				 mDriver.get("http://voiture.autoplus.fr/list/make/"+"Citroen"+"/?page="+i);
				 List<WebElement> lm =  mDriver.findElements(By.xpath("//*[@id='pnf-result']/table/tbody/tr"));
				 
				
				 //Modele
				 for (WebElement row : lm) 
				 {	 
					 List<WebElement> cells = row.findElements(By.tagName("td"));
					 List<WebElement> link = row.findElements(By.tagName("a"));
					 int ik =0;
					 String modele = "";
					 
					 ligneSortie = new String();
					 ligneSortie = marque+";";
					 for (WebElement cell : cells) 
					 {
						 
						 int portes = 0;
						 int sieges = 0;
						 
						 Pattern lpatternCategorie = Pattern.compile("\\(([a-zA-Z0-9 éèàöëä.-]+)\\)");
						 
						 if(ik<4)
						 {
							
							 if(ik==1) 
							 {
								 Matcher lmatcher = lpatternCategorie.matcher(cell.getText().replace(marque, "").trim());
								 if(lmatcher.find())
								 {
									 modele = lmatcher.group(1)+";";
									 ligneSortie += cell.getText().replace(lmatcher.group(1), "").replace("(", "").replace(")", "") +";"+ modele;
								 }
								 else
									 ligneSortie += ";;";
							 }
							 else
								 ligneSortie += cell.getText().replace(marque, "").trim() + ";";
						 }
						else
						 {
							 //Versions
							String linkVeh =  link.get(1).getAttribute("href");
							String[] lv = linkVeh.split("/");
							String idAP = lv[4].split("-")[0];
							mDriver2.get(linkVeh);
							
							 List<WebElement> linfteech = mDriver2.findElements(By.xpath("//*[@id='tech-data']/div[1]/ul/li"));
							 for(WebElement li : linfteech)
							 {
								 String tmp = "";
								 if(li.getText().toLowerCase().contains("moteur"))
								 {
									 String[] l = li.getText().substring(li.getText().indexOf(":")+1).toLowerCase().split(",");
									 for(int j=0;j<3;j++)
									 {
										 if(l[j].toLowerCase().contains("essence sans plomb"))
											 tmp += "Essence"+";"; 
										 else if(l[j].trim().equals("courant électrique"))
											 tmp += "Electrique"+";";
										 else if(l[j].trim().equals("diesel / courant électrique"))
											 tmp += "Hybride Diesel"+";";
										 else if(l[j].trim().equals("essence courant électrique"))
											 tmp += "Hybride essence"+";";
										 else if(l[j].trim().equals("essence ou gaz"))
											 tmp += "GPL"+";";
										 else
											 tmp += l[j].trim()+";";
									 }
								 }
								 else if(li.getText().toLowerCase().contains("dimensions"))
								 {
									 String[] l = li.getText().substring(li.getText().indexOf(":")+1).toLowerCase().split("x");
									 for(int j=0;j<l.length;j++)
									 {
										 tmp += l[j].replace("m","").replace(".", ",")+";"; 
									 }
								 }
								 else if(li.getText().toLowerCase().contains("boîte"))
								 {
									 String[] l = li.getText().substring(li.getText().indexOf(":")+1).toLowerCase().split(",");
									 for(int j=0;j<2;j++)
									 {
										 if(j==1)
											 tmp += l[j].trim()+";"; 
										 else
										 {
											 if(l[j].contains("auto") || l[j].contains("séquentielle") || l[j].contains("robot") || l[j].contains("cvt"))
												 tmp += "Automatique;";
											 else
												 tmp += "Manuelle;";
											 
											 tmp += l[j].trim()+";"; 
											 
										 }
										
									 }
								 }
								 else if(li.getText().toLowerCase().contains("puissance max"))
								 {
									 String[] l = li.getText().toLowerCase().substring(li.getText().indexOf(":")+1,li.getText().toLowerCase().indexOf("à")).toLowerCase().split("/");
									 for(int j=0;j<2;j++)
									 {
										 tmp += l[j].toLowerCase().replace("ch","").replace("kW", "").trim()+";"; 
									 }
								 }
								 else if(li.getText().toLowerCase().contains("coffre"))
								 {
									 String[] l = li.getText().substring(li.getText().indexOf(":")+1).split("/");
									 for(int j=0;j<2;j++)
									 {
										 tmp += l[j].toLowerCase().replace("dm3","")+";"; 
									 }
								 }
								 else  if(li.getText().toLowerCase().contains("consommation"))
								 {
									 String conso = li.getText().substring(li.getText().indexOf(":")+1);
									 tmp = conso.substring(1,conso.indexOf("/")).replace("L", "").replace(".", ",").trim() + ";";
								 }
								 else if(li.getText().toLowerCase().contains("emisssion"))
								 {
									 tmp = li.getText().substring(li.getText().indexOf(":")+1).replace("g/km", "").trim() + ";";
								 }
								 else if(li.getText().toLowerCase().contains("charge"))
								 {
									 String[] l = li.getText().substring(li.getText().indexOf(":")+1).trim().toLowerCase().split("/");
									 for(int j=0;j<2;j++)
									 {
										 tmp += l[j].toLowerCase().replace("ch","").replace("kg", "").trim()+";"; 
									 }
									 
								 }else if(li.getText().toLowerCase().contains("portes"))
								 {
									 tmp = li.getText().toLowerCase().substring(li.getText().indexOf(":")+1) + ";";
									 portes = Integer.decode(li.getText().substring(li.getText().indexOf(":")+1).trim());
								 }
								 else if(li.getText().toLowerCase().contains("sièges"))
								 {
									 tmp = li.getText().toLowerCase().substring(li.getText().indexOf(":")+1) + ";";
									 sieges = Integer.decode(li.getText().substring(li.getText().indexOf(":")+1).trim());
								 }
								 else 
								 {
									 tmp = li.getText().toLowerCase().substring(li.getText().indexOf(":")+1).replace(" m", "").replace(" L", "") + ";";
								 }
								
								 ligneSortie +=  tmp ;
							 }
							 
							 List<WebElement> linfomech = mDriver2.findElements(By.xpath("//*[@id='tech-data']/div[1]/ul[2]/li"));
							 for(WebElement li : linfomech)
							 {
								 String tmp = "";
								
								
									 tmp = li.getText().substring(li.getText().indexOf(":")+1).replace("km/h", "").replace(" s", "") + ";";
								
								
								 ligneSortie +=  tmp ;
							 }
							 
							 List<WebElement> linfocomp = mDriver2.findElements(By.xpath("//*[@id='tech-data']/div[1]/ul[3]/li"));
							 for(WebElement li : linfocomp)
							 {
								
								 String tmp = "";
								
								 
								
									 tmp = li.getText().substring(li.getText().indexOf(":")+1) + ";";
								
								
								 ligneSortie +=  tmp ;
							 }
							 
							 if((portes>2 && sieges<4) || modele.contains("Fourgon") || modele.contains("Cabine") || modele.contains("cabine") || modele.contains("Pick-Up"))
								 ligneSortie +=  "VU;";
							 else 
								 ligneSortie +=  "VP;";
							
							 //Equipements
							 mDriver2.get(link.get(1).getAttribute("href").replace("technique", "equipements")+"#price-infos");
							 //Init bool
							 boolean clim = false;
							 boolean regul = false;
							 boolean gps = false;
							 boolean bluetooth = false;
							 boolean lecteurcd = false;
							 boolean peinturemetal = false;
							 
							 boolean listeequipement = false;
							 
							 List<WebElement> lequi = mDriver2.findElements(By.xpath("//*[@id='tech-data']/div[2]/ul"));
							 if(lequi.size()!=0) listeequipement =true;
							 for(WebElement le : lequi)
							 {
								 List<WebElement> linfoequip = le.findElements(By.xpath("li"));
								 for(WebElement linf : linfoequip)
								 {
									 WebElement ullocal = linf.findElement(By.xpath("ul"));
									 String ullocalstr = ullocal.getText().toLowerCase();
									
									 if(ullocalstr.contains("gps") || ullocalstr.contains("media nav") || ullocalstr.contains("tom tom") || ullocalstr.contains("navigation"))
										 gps = true;
									 else if(ullocalstr.contains("bluetooth") || ullocalstr.contains("connecting box"))
										 bluetooth = true;
									 else if(ullocalstr.contains("regulateur de vitesse") || ullocalstr.contains("régulateur de vitesse") || linf.getText().toLowerCase().trim().contains("tempomat") || ullocalstr.contains("limiteur de vitesse") )
										 regul = true;
									 else if(ullocalstr.contains("peinture métal"))
										 peinturemetal = true;
									 else if(linf.getText().contains("climatisation") || ullocalstr.contains("climatisation") || ullocalstr.contains("air conditionn�"))
										 clim = true;
									 else if(ullocalstr.contains("lecteur cd") || ullocalstr.contains(" cd "))
										 lecteurcd = true;
								 }
							 }
							 
							 if(listeequipement)
							 {
								 if(bluetooth)
									 ligneSortie +=  "Oui" + ";";
								 else
									 ligneSortie +=  "Non" + ";";
								 if(lecteurcd)
									 ligneSortie +=  "Oui" + ";";
								 else
									 ligneSortie +=  "Non" + ";";
								 if(gps)
									 ligneSortie +=  "Oui" + ";";
								 else
									 ligneSortie +=  "Non" + ";";
								 if(clim)
									 ligneSortie +=  "Oui" + ";";
								 else
									 ligneSortie +=  "Non" + ";";
								 if(regul)
									 ligneSortie +=  "Oui" + ";";
								 else
									 ligneSortie +=  "Non" + ";";
								 if(peinturemetal)
									 ligneSortie +=  "Oui" + ";";
								 else
									 ligneSortie +=  "Non" + ";";
							 }
							 else
								 ligneSortie += "NC;NC;NC;NC;NC;NC;";
							 
						 }
						 ik++;
					 }
					 ligneSortie+= "\n";
					 ecrire(ligneSortie);
				 }
				
			 }
		 }
		 
		 
	}
	
	
	private static void ecrire(String texte) throws IOException
    {
       
        FileWriter writer = null;
        try{
             writer = new FileWriter("BddVehicules.csv", true);
             writer.write(texte,0,texte.length());
        }catch(IOException ex){
            ex.printStackTrace();
        }finally{
          if(writer != null){
             writer.close();
          }
        }
    }
}
