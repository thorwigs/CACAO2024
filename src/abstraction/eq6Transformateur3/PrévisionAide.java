package abstraction.eq6Transformateur3;
import java.util.*;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Feve;
//class pour les décisions d'achats
public class PrévisionAide extends Transformateur3AcheteurBourse{
	
	private ArrayList<HashMap<Feve, Double>> historiquesStockFeves;
	private ArrayList<HashMap<Chocolat, Double>> historiquesStockChoco;
	
	public PrévisionAide() {
		super();
		historiquesStockFeves = new ArrayList<>();
		historiquesStockFeves.add(stockFeves);
		historiquesStockChoco = new ArrayList<>();
		historiquesStockChoco.add(stockChoco);
	}
	
	
	public ArrayList<HashMap<Feve, Double>> gethistoriquesStockFeves() {
		return historiquesStockFeves;
	}
	
	
	
	public ArrayList<HashMap<Chocolat, Double>> historiquesStockChoco(){
		return historiquesStockChoco;
	}
	
	
	
	/**
	 * Permet de mettre à jour l'historique des stocks
	 * @param stockFeves
	 * @param stockChoco
	 */
	public void addhistorique(HashMap<Feve, Double> stockFeves ,HashMap<Chocolat, Double> stockChoco){
		historiquesStockFeves.add(stockFeves);
		historiquesStockChoco.add(stockChoco);
	}
	
	public HashMap<Feve,Integer> Decision(){
		HashMap<Feve, Integer> Decision = new HashMap<>();
		if(historiquesStockFeves.size()<=1) {
			Decision.put(Feve.F_BQ,20);
			Decision.put(Feve.F_MQ,20);
			Decision.put(Feve.F_MQ_E,20);
			Decision.put(Feve.F_HQ,20);
			Decision.put(Feve.F_HQ_E,20);
			Decision.put(Feve.F_HQ_BE,20);
		
		}
		else {
			for(Feve f :stockFeves.keySet()){
				HashMap<Feve,Double> steps_avantdernier = historiquesStockFeves.get(historiquesStockFeves.size()-2);
				HashMap<Feve,Double> steps_dernier = historiquesStockFeves.get(historiquesStockFeves.size()-1);
				double value = (steps_dernier.get(f)-stockFeves.get(f))/stockFeves.get(f);
				if (value <=0) {
					value = 0;
				}
				Decision.put(f, (int) (stockFeves.get(f)*value));
			}
		}
		return Decision;
	}
	
	
}
