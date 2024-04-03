package abstraction.eq9Distributeur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public class Distributeur2Stocks extends Distributeur2Acteur{
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	protected List<ChocolatDeMarque> chocolatsVillors;
	protected Variable totalStocksChocoMarque;  
	
	


	public HashMap<ChocolatDeMarque, Double> getStockChocoMarque() {
		return this.stockChocoMarque;
	}

}
