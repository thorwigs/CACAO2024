package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;

public class Producteur1Plantation extends Producteur1VendeurAuxEncheres implements IPlantation{
	protected double nombreHec = 3E6;
	protected double nombreHecMax = 5E6;
	protected Journal journalPlantation;
	protected HashMap<Feve, Double> plantation;
	protected boolean pesticides = true;
	protected HashMap<Feve, Double> production;
	protected HashMap<Feve, Double> prodAnnee;
	public void next() {
		super.next();
	}
	public Producteur1Plantation() {
		super();
		
		this.journalPlantation = new Journal(this.getNom()+"   journal Plantation",this);
		
	}


	@Override
	public HashMap<Feve, Double> plantation() {
		plantation = new HashMap<Feve, Double>();
		plantation.put(Feve.F_BQ,0.7*this.nombreHec );
		plantation.put(Feve.F_MQ, 0.28*this.nombreHec);
		plantation.put(Feve.F_HQ, 0.02*this.nombreHec);
		this.journalPlantation.ajouter("On a reussi planter");
		return plantation;
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
	public void setHec(double hec) {
		this.nombreHec = hec;
	}

	@Override
	public void recruitWorkers(HashMap<Feve, Double> demand) {
		// TODO Auto-generated method stub
		
	}
	public void achat(double hec) {
		double cout = hec * 500;
		if (this.nombreHec+hec > this.nombreHecMax && this.getSolde() > cout) {
			this.journalPlantation.ajouter("On peut acheter la quantite demande; la quantite on peut acheter est: "+ (this.nombreHecMax-this.nombreHec));
			this.achat(this.nombreHecMax-this.nombreHec);
		}
		else if(this.getSolde() < cout) {
			this.journalPlantation.ajouter("On peut pas acheter la quantite demande car on n'a pas l'argent");
		}
		else {
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Cout d'acheter des hectares",cout );
			this.setHec(this.nombreHec + hec);
		}
			
		
		}
		

	public HashMap<Feve, Double> prodAnnuel() {
		this.prodAnnee.put(Feve.F_BQ,0.7*650*this.nombreHec);
		this.prodAnnee.put(Feve.F_MQ, 650*0.28*this.nombreHec);
		this.prodAnnee.put(Feve.F_HQ, 650*0.02*this.nombreHec);
		return prodAnnee;
	}
	public HashMap<Feve, Double> quantite() {
		// TODO Auto-generated method stub
		if (pesticides) {
			this.production.put(Feve.F_BQ,0.9*this.prodAnnee.get(Feve.F_BQ));
			this.production.put(Feve.F_MQ, 0.85*this.prodAnnee.get(Feve.F_MQ));
			this.production.put(Feve.F_HQ, 0.8*this.prodAnnee.get(Feve.F_HQ));
		}
		else {
			this.production.put(Feve.F_BQ,0.82*this.prodAnnee.get(Feve.F_BQ));
			this.production.put(Feve.F_MQ, 0.77*this.prodAnnee.get(Feve.F_MQ));
			this.production.put(Feve.F_HQ, 0.72*this.prodAnnee.get(Feve.F_HQ));
		}
		return production;
	}

	@Override
	public void setProdTemps(HashMap<Feve, Double> d0, HashMap<Feve, Double> d1) {
		// TODO Auto-generated method stub
		
	}

}
