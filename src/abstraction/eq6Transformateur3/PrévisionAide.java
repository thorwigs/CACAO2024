package abstraction.eq6Transformateur3;
import java.util.*;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

//class pour les décisions de quantité d'achats
/**
 * @author Mahel
 */
public class PrévisionAide extends Transformateur3Acteur{
	//protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	private ArrayList<HashMap<Feve, Double>> historiquesStockFeves;
	private ArrayList<HashMap<Chocolat, Double>> historiquesStockChoco;
	//private ArrayList<HashMap<ChocolatDeMarque, Double>> historiquesStockChocoMarque;
		
	public PrévisionAide() {
		super();
		historiquesStockFeves = new ArrayList<>();
		historiquesStockFeves.add(stockFeves);
		historiquesStockChoco = new ArrayList<>();
		historiquesStockChoco.add(stockChoco);
		//historiquesStockChocoMarque = new ArrayList<>();
	}
	/**
	 * @author Mahel
	 */
	
	public ArrayList<HashMap<Feve, Double>> gethistoriquesStockFeves() {
		return historiquesStockFeves;
	}
	
	/**
	 * @author Mahel
	 */
	
	public ArrayList<HashMap<Chocolat, Double>> historiquesStockChoco(){
		return historiquesStockChoco;
	}
	
	//public ArrayList<HashMap<ChocolatDeMarque, Double>> gethistoriquesStockChocoMarque(){
//		return historiquesStockChocoMarque;
//	}
	/**
	@author Mahel
	*/
	public Chocolat Correspond(Feve f) {
		switch( f ) {
			
			case F_HQ_BE : return Chocolat.C_HQ_BE;
			case F_HQ_E : return Chocolat.C_HQ_E ; 
			case F_HQ : return Chocolat.C_HQ ;
			case F_MQ_E : return Chocolat.C_MQ_E ;
			case F_MQ : return Chocolat.C_MQ ;
			case F_BQ : return Chocolat.C_BQ ;
			default : break;
		}
			return Chocolat.C_HQ_BE ;
	}
	
	public Feve Correspond(Chocolat c) {
		
		switch(  c ) {
		
		case C_HQ_BE : return Feve.F_HQ_BE;
		case C_HQ_E : return Feve.F_HQ_E ; 
		case C_HQ : return Feve.F_HQ ;
		case C_MQ_E : return Feve.F_MQ_E ;
		case C_MQ : return Feve.F_MQ ;
		case C_BQ : return Feve.F_BQ ;
		
		default : break;
	}
		return Feve.F_HQ_BE ;
		
	}
	
	
	
	/**
	 * Permet de mettre à jour l'historique des stocks
	 * @param stockFeves
	 * @param stockChoco
	 */
	public void addhistorique(HashMap<Feve, Double> stockFeves ,HashMap<Chocolat, Double> stockChoco, HashMap<ChocolatDeMarque, Double> stockChocoMarque){
		historiquesStockFeves.add(stockFeves);
		historiquesStockChoco.add(stockChoco);
		//historiquesStockChocoMarque.add(stockChocoMarque);
	}
	/**
	 * @author Mahel
	 */
	
	public HashMap<Feve,Integer> Decision(){
		HashMap<Feve, Integer> Decision = new HashMap<>();
		for(ChocolatDeMarque c :stockChocoMarque.keySet()) {
			if(stockChocoMarque.get(c)<=10000) {
				if(Decision.get(Correspond(c.getChocolat()))!=null) {
				Decision.put(Correspond(c.getChocolat()),(int) (Decision.get(Correspond(c.getChocolat()))+10000-stockChocoMarque.get(c)) );
				}
				else {
					Decision.put(Correspond(c.getChocolat()),(int) (10000-stockChocoMarque.get(c))) ;
				}
			}
		}
		
		
		
		return Decision;
	}
	
	
	/**
	public HashMap<Feve,Integer> Decision3(){
		HashMap<Feve, Integer> Decision = new HashMap<>();
		if(historiquesStockFeves.size()<=1 || historiquesStockFeves == null) {
			Decision.put(Feve.F_BQ,20000);
			Decision.put(Feve.F_MQ,20000);
			Decision.put(Feve.F_MQ_E,20000);
			Decision.put(Feve.F_HQ,20000);
			Decision.put(Feve.F_HQ_E,20000);
			Decision.put(Feve.F_HQ_BE,20000);
		
		}
		else {
			
			/**
			 * @author Mahel et Cédric
			 
			
			for(Feve f :stockFeves.keySet()){
				// HashMap<Feve,Double> steps_avantdernier = historiquesStockFeves.get(historiquesStockFeves.size()-2);
				HashMap<Feve,Double> steps_dernier = historiquesStockFeves.get(historiquesStockFeves.size()-1);
				System.out.println(steps_dernier.get(f)+ " historique feve");
				System.out.println(stockFeves.get(f)+ " stock feve");
				double value = (steps_dernier.get(f)-stockFeves.get(f));
				if (value <=0.00001) {
					value = 0;
				}
				Decision.put(f, (int) (value));
				
			}
		}
		System.out.println(Decision +" Dans la décision");
		return Decision;
	}
	*/
	/**
	 * @author Mahel
	 
	public HashMap<Feve,Integer> Decision2(){
		HashMap<Feve, Integer> Decision = new HashMap<>();
		if(historiquesStockFeves.size()<=1 || stockChocoMarque == null) {
			Decision.put(Feve.F_BQ,20000);
			Decision.put(Feve.F_MQ,20000);
			Decision.put(Feve.F_MQ_E,20000);
			Decision.put(Feve.F_HQ,20000);
			Decision.put(Feve.F_HQ_E,20000);
			Decision.put(Feve.F_HQ_BE,20000);
		
		}
		else {
			
			
			for(ChocolatDeMarque c :stockChocoMarque.keySet()){
				// HashMap<Feve,Double> steps_avantdernier = historiquesStockFeves.get(historiquesStockFeves.size()-2);
				HashMap<ChocolatDeMarque,Double> steps_dernier = historiquesStockChocoMarque.get(historiquesStockChoco.size()-1);
				double value = (steps_dernier.get(c)-stockChocoMarque.get(c));
				if (value <=0) {
					value = 0;
				}
				Decision.put(Correspond(c.getChocolat()), (int) (value));
			}
		}
		return Decision;
	}
	*/
	
}
