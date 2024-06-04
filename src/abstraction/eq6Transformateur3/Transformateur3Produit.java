package abstraction.eq6Transformateur3;

import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur3Produit extends Transformateur3AcheteurBourse {
	protected double quantiteParStep;
	/**
	 * @author Mahel 
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
	
	/**
	 * @author Mahel
	 */
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
	 * @author Mahel
	 */
	public void next() {
		super.next();
		this.quantiteParStep = 0;
		journal.ajouter("stock avant transformation : ");
		journal.ajouter("stock de feves : ");
		for(Feve p : stockFeves.keySet()) {
			journal.ajouter(p.getGamme() +" : " + stockFeves.get(p));
		}
		
		journal.ajouter("stock de chocolat : ");
		for(Chocolat c : stockChoco.keySet()) {
			journal.ajouter(c.getGamme() +" : " + stockChoco.get(c));
		
		}
		journal.ajouter("stock de chocolat de marques : ");
		for(ChocolatDeMarque c : stockChocoMarque.keySet()) {
			journal.ajouter(c.getGamme() +" : " + stockChocoMarque.get(c));
		}
		
		for(Feve p : stockFeves.keySet()) {
			TransformationFeve(p);
		}
		TransformationChocodeMarque();
		journal.ajouter("stock après transformation : ");
		journal.ajouter("stock de feves : ");
		for(Feve p : stockFeves.keySet()) {
			journal.ajouter(p.getGamme() +" : " + stockFeves.get(p));
		}
		journal.ajouter("stock de chocolat : ");
		for(Chocolat c : stockChoco.keySet()) {
			journal.ajouter(c.getGamme() +" : " + stockChoco.get(c));
		}
		journal.ajouter("stock de chocolat de marques : ");
		for(ChocolatDeMarque c : stockChocoMarque.keySet()) {
			journal.ajouter(c.getGamme() +" : " + stockChocoMarque.get(c));
		}
		
		journal.ajouter("Quantite transformee ce step : " + quantiteParStep);
	}
	
	/**
	 * 
	 * @author Mahel 
	 */
	public void TransformationFeve(Feve f) {
		double feve_en_stock = this.stockFeves.get(f);
		double choco_en_stock = this.stockChoco.get(this.Correspond(f));						
		if(feve_en_stock >0 ) {                                               
			Filiere.LA_FILIERE.getBanque().payerCout(this,this.cryptogramme,"Cout de production",feve_en_stock*(0.5*1200+8+2*1000*0.27));
			this.stockChoco.put(this.Correspond(f),choco_en_stock + feve_en_stock /*/ 4.2*/);
			quantiteParStep += feve_en_stock;
			this.stockFeves.put(f,0.0);
		}
		
	}
	/**
	 * @author Mahel
	 */
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		if(stockChocoMarque != null) {
			for(ChocolatDeMarque c : this.stockChocoMarque.keySet()) {
				Variable var = new Variable("Chocolat sharks," + c.getNom(),this,stockChocoMarque.get(c));
				res.add(var);
			}
			res.add(new Variable("Quantite_produite_par_step ",this,this.quantiteParStep));
			double q = 0;
			for (ExemplaireContratCadre c : this.contratsEnCours) {
				q += c.getQuantiteALivrerAuStep();
			}
			res.add(new Variable("Ventes ", this,q ));
		}
		else {
			
		}
		
		
		return res;
	}
	/**
	 * @author Mahel
	 */
	public void TransformationChocodeMarque() {
		for(ChocolatDeMarque c : this.chocosProduits) {
			if(stockChocoMarque.get(c) != null) {
				this.stockChocoMarque.put(c, stockChocoMarque.get(c)+stockChoco.get(c.getChocolat()));
				this.stockChoco.put(c.getChocolat(), (double) 0);
			}
			else {
				this.stockChocoMarque.put(c, stockChoco.get(c.getChocolat()));
			}
		}
	}
}
