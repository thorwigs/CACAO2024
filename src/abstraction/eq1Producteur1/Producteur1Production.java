package abstraction.eq1Producteur1;

import java.util.ArrayList;
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
	protected HashMap<String, Integer> ageStock;

	//protected HashMap<Feve, Variable> stockag;
	
	public Producteur1Production() {
		super();
		this.journalProduction = new Journal(this.getNom()+"   journal Production",this);
		this.ageStock = new HashMap<String, Integer>();
		this.prodParStep = this.ProdParStep();
		this.stock = this.IniStock();
		for (Feve feve : this.stock.keySet()) {
			String s = feve.toString()+"/" +this.getQuantiteEnStock(feve, cryptogramme);
			this.ageStock.put(s, 0);
			this.journalProduction.ajouter(s+ " a l'age suivant"+ this.ageStock.get(s));
		}
		
	}
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme && this.stock.containsKey(p)) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			return this.stock.get(p).getValeur(cryptogramme); // A modifier
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
	public void updateAge() {
		for (String s : this.ageStock.keySet()) {
			this.ageStock.put(s, this.ageStock.get(s)+1);
			this.journalProduction.ajouter(s+ " a l'age suivant"+ this.ageStock.get(s));
		}
		for (Feve feve : this.stock.keySet()) {
			String s = feve.toString()+"/" +this.getQuantiteEnStock(feve, cryptogramme);
			this.ageStock.put(s, 0);
			this.journalProduction.ajouter(s+ " a l'age suivant"+ this.ageStock.get(s));
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
	public void degradStock() {
		List<String> keysToremove = new ArrayList<>(); 
		for (String s : this.ageStock.keySet()) {
			String[] mate = s.split("/");
			int age = this.ageStock.get(s);
			double stoc = Double.valueOf(mate[1]);
			HashMap<Feve, Double> what = new HashMap<Feve, Double>();
			Feve fe = null;
			for (Feve f : Feve.values()) {
				if (f.toString().equals(mate[0])) {
					what.put(f, stoc);
					fe = f;
				}
			}
			
			if (fe.equals(Feve.F_HQ)) {
				if (age > 4) {
					this.stock.get(fe).retirer(this, stoc);
					fe = Feve.F_MQ;
					Variable v =  new Variable(this.getNom()+"Stock"+fe.toString().substring(2), "<html>Stock de feves "+fe+"</html>",this, 0.0, this.prodParStep.get(fe)*24+stoc, this.prodParStep.get(fe)*6+stoc);
					this.stock.put(fe, v);
					this.journalProduction.ajouter("Le stock:"+ stoc +"a ete degrade de HQ a MQ");
					keysToremove.add(s);
				}
			}
			if (fe.equals(Feve.F_HQ_E) || fe.equals(Feve.F_HQ_BE) && this.getQuantiteEnStock(fe, cryptogramme) >= stoc) {
				if (age >4) {
					this.stock.get(fe).retirer(this, stoc);
					fe = Feve.F_MQ_E;
					Variable v =  new Variable(this.getNom()+"Stock"+fe.toString().substring(2), "<html>Stock de feves "+fe+"</html>",this, 0.0, this.prodParStep.get(fe)*24+stoc, this.prodParStep.get(fe)*6+stoc);
					this.stock.put(fe, v);
					this.journalProduction.ajouter("Le stock:"+ stoc +"a ete degrade de HQE a MQE");
					keysToremove.add(s);
				}
			}
			if (fe.equals(Feve.F_MQ) || fe.equals(Feve.F_MQ_E) && this.getQuantiteEnStock(fe, cryptogramme) >= stoc) {
				if (age > 8) {
					this.stock.get(fe).retirer(this, stoc);
					fe = Feve.F_BQ;
					Variable v =  new Variable(this.getNom()+"Stock"+fe.toString().substring(2), "<html>Stock de feves "+fe+"</html>",this, 0.0, this.prodParStep.get(fe)*24+stoc, this.prodParStep.get(fe)*6+stoc);
					this.stock.put(fe, v);
					this.journalProduction.ajouter("Le stock:"+ stoc +"a ete degrade de MQ a BQ");
					keysToremove.add(s);
				}
			}
			if (fe.equals(Feve.F_BQ) && this.getQuantiteEnStock(fe, cryptogramme) >= stoc ) {
				if ( age > 12) {
					this.stock.get(fe).retirer(this, stoc);
					this.journalProduction.ajouter("Le stock:"+ stoc+"doit etre jetee");
					keysToremove.add(s);
				}
			}
			this.ageStock.put(s, age);
			
			
			
				
			}
		for (String s : keysToremove) {
			this.ageStock.remove(s);
		}
	
	
		
		
	}
	public List<Variable> getIndicateurs() {
		List<Variable> res = super.getIndicateurs();
		res.addAll(this.stock.values());
		return res;
	}

	public void next() {
		super.next();
		this.updateAge();
		//this.degradStock();
		//this.journalProduction.ajouter("On a ces quantites:"+ this.prodParStep.get(Feve.F_BQ)+ "en stock pour la gamme BQ en  :"+ Filiere.LA_FILIERE.getEtape());
		//this.journalProduction.ajouter("On a ces quantites:"+ this.prodParStep.get(Feve.F_MQ)+ "en stock pour la gamme MQ en  :"+ Filiere.LA_FILIERE.getEtape());
		//this.journalProduction.ajouter("On a ces quantites:"+ this.prodParStep.get(Feve.F_HQ)+ "en stock pour la gamme HQ en  :"+ Filiere.LA_FILIERE.getEtape());

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
