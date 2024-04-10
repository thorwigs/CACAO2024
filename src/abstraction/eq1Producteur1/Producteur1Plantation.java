package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;

public class Producteur1Plantation extends Producteur1VendeurAuxEncheres implements IPlantation{
	protected double nombreHec = 3E6;
	protected double nombreHecMax = 5E6;
	protected double i;
	protected HashMap<Feve, Double> plantation;
	public void next() {
		super.next();
	}
	public Producteur1Plantation() {
		plantation = new HashMap<Feve, Double>();
		
	}

	@Override
	public HashMap<Feve, Double> plantation() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void adjustPlantationSize(HashMap<Feve, Double> adjustments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<Feve, Double> maindoeuvre() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recruitWorkers(HashMap<Feve, Double> demand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<Feve, Double> quantite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProdTemps(HashMap<Feve, Double> d0, HashMap<Feve, Double> d1) {
		// TODO Auto-generated method stub
		
	}

}
