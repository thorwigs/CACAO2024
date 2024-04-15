package abstraction.eq1Producteur1;

import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1Production extends Producteur1Plantation{
	protected double prix_hq_F;
	protected Journal journalProduction;
	//protected HashMap<Feve, Variable> stockag;
	
	public Producteur1Production() {
		super();
		this.journalProduction = new Journal(this.getNom()+"   journal Production",this);
		this.prodParStep = this.ProdParStep();
		this.stock = this.IniStock();

	}
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme && this.stock.containsKey(p)) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			return this.stock.get(p).getValeur(cryptogramme); // A modifier
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
	public void feveToEqui() {
		for (Ouvrier ouvrier : this.liste_Ouvrier) {
			if (ouvrier.estEnfant) {
				this.journalProduction.ajouter("On ne peut pas faire de l'equitable car on employe des enfants");
			}
			if (ouvrier.isEquitable) {
				this.prodParStep.put(Feve.F_MQ_E, null);
				this.prodParStep.put(Feve.F_HQ_E, null);
				this.journalProduction.ajouter("On a transforme X% of MQ to MQ_E and Y% of HQ to HQ_E");
			}
		}
	}
	public void feveToBio() {
		if (this.pesticides) {
			this.journalProduction.ajouter("On ne peut pas faire du bio car on a utilise des pesticides");
		}
		else {	boolean bio = true;
			if (this.prodParStep.get(Feve.F_HQ_E) !=0) {
				this.prodParStep.put(Feve.F_HQ_BE, null);
			}
		}
		
	}
	public List<Variable> getIndicateurs() {
		List<Variable> res = super.getIndicateurs();
		res.addAll(this.stock.values());
		return res;
	}

	public void next() {
		super.next();
		
		double totalStock = 0;
		for (Feve f : Feve.values()) {
			this.stock.get(f).ajouter(this,this.getProd().get(f) );
			totalStock += this.stock.get(f).getValeur();
		}
		this.getJournaux().get(0).ajouter("Stock= "+ totalStock);
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", totalStock*this.getCoutStockage());

	}
	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(journalProduction);
		return res;
		
	}
	
	

}
