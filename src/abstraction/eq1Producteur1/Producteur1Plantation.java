package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Producteur1Plantation extends Producteur1Acteur implements IPlantation{
	protected double nombreHec = 3E6;
	protected double nombreHecMax = 5E6;
	protected Journal journalPlantation;
	protected HashMap<Feve, Double> plantation;
	protected boolean pesticides = true;
	protected HashMap<Feve, Double> production;
	protected HashMap<Feve, Double> prodAnnee;
	protected HashMap<Feve, Variable> stock;
	protected HashMap<Feve, Double> Stocck;
	public void next() {
		super.next();
	}
	public Producteur1Plantation() {
		super();
		this.prodAnnee =  new HashMap<Feve, Double>();
		this.production = new HashMap<Feve, Double>();
		
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
	    for (Feve feve : adjustments.keySet()) {
	        
	        double adjustment = adjustments.get(feve);
	        double currentSize = plantation.getOrDefault(feve, 0.0);
	       
	        double newSize = currentSize + adjustment;
	        	
	        if (newSize > nombreHecMax) {
	            newSize = nombreHecMax;
	        } else if (newSize < 0) {
	            newSize = 0; 
	        }
	        
	        
	        plantation.put(feve, newSize);
	        journalPlantation.ajouter(String.format("Adjusting %s plantation size by %.2f hectares", feve.name(), adjustment));
	    }
		
	}
	

	@Override
	public HashMap<Feve, Double> maindoeuvre() {
		//return nombre d'ouvriers avec un rendement 1 necessaire
		HashMap<Feve,Double> ouvriers = new HashMap<Feve,Double>();
		for (Feve f : this.plantation().keySet()) {
			if (f.isBio()) {
				this.journalPlantation.ajouter("Nombre d'ouvrier avec un rendement 1 necessaire pour la plantation bio est :"+ 1.5*this.plantation().get(f));
				ouvriers.put(f, 1.5*this.plantation().get(f));
			} else {
				this.journalPlantation.ajouter("Nombre d'ouvrier avec un rendement 1 necessaire pour la plantation est :"+ this.plantation().get(f));
				ouvriers.put(f, this.plantation().get(f));
			}
		}
		return ouvriers;
		
	}	 
		
	public void setHec(double hec) {
		this.nombreHec = hec;
	}

	@Override
	public void recruitWorkers(HashMap<Feve, Double> demand) {
		// TODO Auto-generated method stub
		for (Feve feve : demand.keySet()) {
			double requiredRendement = demand.get(feve);
		}
		
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
		this.prodAnnee = new HashMap<Feve, Double>();
		this.prodAnnee.put(Feve.F_BQ,0.7*0.650*this.nombreHec);
		this.prodAnnee.put(Feve.F_MQ, 0.650*0.28*this.nombreHec);
		this.prodAnnee.put(Feve.F_HQ, 0.650*0.02*this.nombreHec);
		this.prodAnnee.put(Feve.F_HQ_E, 0.0);
		this.prodAnnee.put(Feve.F_HQ_BE, 0.0);
		this.prodAnnee.put(Feve.F_MQ_E, 0.0);
		return prodAnnee;
	}
	public HashMap<Feve, Double> quantite() {
		// TODO Auto-generated method stub
		this.prodAnnee = this.prodAnnuel();
		this.production = new HashMap<Feve, Double>();
		
		if (pesticides) {
			this.production.put(Feve.F_BQ,0.9*this.prodAnnee.get(Feve.F_BQ));
			this.production.put(Feve.F_MQ, 0.85*this.prodAnnee.get(Feve.F_MQ));
			this.production.put(Feve.F_HQ, 0.8*this.prodAnnee.get(Feve.F_HQ));
			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
		}
		else {
			this.production.put(Feve.F_BQ,0.82*this.prodAnnee.get(Feve.F_BQ));
			this.production.put(Feve.F_MQ, 0.77*this.prodAnnee.get(Feve.F_MQ));
			this.production.put(Feve.F_HQ, 0.72*this.prodAnnee.get(Feve.F_HQ));
			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
		}
		return production;
	}

	@Override
	public void setProdTemps(HashMap<Feve, Double> d0, HashMap<Feve, Double> d1) {
		// TODO Auto-generated method stub
		
	}
	public HashMap<Feve, Double> ProdParStep(){
		this.prodAnnee = this.prodAnnuel();
		this.production = new HashMap<Feve, Double>();
		if (pesticides) {
			this.production.put(Feve.F_BQ,0.9*this.prodAnnee.get(Feve.F_BQ)/24);
			this.production.put(Feve.F_MQ, 0.85*this.prodAnnee.get(Feve.F_MQ)/24);
			this.production.put(Feve.F_HQ, 0.8*this.prodAnnee.get(Feve.F_HQ)/25.5);
			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
		}
		
		else {
			this.production.put(Feve.F_BQ,0.82*this.prodAnnee.get(Feve.F_BQ)/25.5);
			this.production.put(Feve.F_MQ, 0.77*this.prodAnnee.get(Feve.F_MQ)/25.5);
			this.production.put(Feve.F_HQ, 0.72*this.prodAnnee.get(Feve.F_HQ)/25.5);
			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
		}
		
		return production;
}

	public HashMap<Feve, Variable> IniStock(){
		this.stock = new HashMap<Feve, Variable>();
		HashMap<Feve, Double> pro = this.ProdParStep();
		
		for (Feve f : Feve.values()) {
			Variable v =  new Variable(this.getNom()+"Stock"+f.toString().substring(2), "<html>Stock de feves "+f+"</html>",this, 0.0, pro.get(f)*24, pro.get(f)*6);
			this.stock.put(f, v);
		}
		return stock;
	}
	public HashMap<Feve, Double> InitiStock(){
		this.Stocck = new HashMap<Feve, Double>();
		HashMap<Feve, Double> pro = this.ProdParStep();
		for (Feve f : Feve.values()) {
			this.Stocck.put(f, pro.get(f)*6);
		}
		return this.Stocck;
	}

}
