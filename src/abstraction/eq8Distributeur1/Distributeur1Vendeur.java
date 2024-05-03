package abstraction.eq8Distributeur1;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;



/**
 * @author wiam 
 */
public class Distributeur1Vendeur extends Distributeur1Acteur implements IDistributeurChocolatDeMarque {
	protected double capaciteDeVente;
	protected  HashMap<ChocolatDeMarque, Double> ListPrix;
	protected String[] marques;
	protected Journal journalVente;
	protected int nombreEmploye;

	
	/**
	 * @author wiam
	 */
	public Distributeur1Vendeur() {
		super();
		this.capaciteDeVente=120000.0;  //capacite de vente par step
		this.ListPrix = new HashMap<ChocolatDeMarque, Double>();
		this.marques = new String[chocolats.size()];
		this.journalVente= new Journal (this.getNom() + " journal des ventes", this);
	}
	
	
	/**
	 *@author wiam
	 */
	public void initialiser () {
		super.initialiser();
		for (ChocolatDeMarque choco : chocolats) {
			this.setPrix(choco);
		}
		this.setNombreEmploye();
	}

	/**
	 * @author wiam
	 */
	public void setPrix(ChocolatDeMarque choco) {
		if (Filiere.LA_FILIERE.getEtape()<1) {
			if ((choco.getChocolat()==Chocolat.C_BQ)) {
				ListPrix.put(choco, (double) 2900);
			}
			if (choco.getChocolat()==Chocolat.C_MQ) {
				ListPrix.put(choco, (double) 6000);
			}
			if (choco.getChocolat()==Chocolat.C_MQ_E) {
				ListPrix.put(choco, (double) 10000);
			}
			if (choco.getChocolat()==Chocolat.C_HQ) {
				ListPrix.put(choco, (double) 15000);
			}
			if (choco.getChocolat()==Chocolat.C_HQ_E) {
				ListPrix.put(choco, (double) 22000);
			}
			if (choco.getChocolat()==Chocolat.C_HQ_BE) {
				ListPrix.put(choco, (double) 30000);
			}
		}
		else {
			if ((choco.isEquitable()) && (ListPrix.get(choco)>Filiere.LA_FILIERE.prixMoyen(choco, Filiere.LA_FILIERE.getEtape()-1))) {
				ListPrix.replace(choco, 0.97*Filiere.LA_FILIERE.prixMoyen(choco, Filiere.LA_FILIERE.getEtape()-1));
			}
			else if (0.8*ListPrix.get(choco)>Filiere.LA_FILIERE.prixMoyen(choco, Filiere.LA_FILIERE.getEtape()-1)) {
				ListPrix.replace(choco, ListPrix.get(choco)*0.8);
				//System.out.println(choco);
				//System.out.println(ListPrix.get(choco));
				//System.out.println(Filiere.LA_FILIERE.prixMoyen(choco, Filiere.LA_FILIERE.getEtape()-1)+"PM");
			}
			else if ((Filiere.LA_FILIERE.getVentes(choco,Filiere.LA_FILIERE.getEtape()-1)-Filiere.LA_FILIERE.getVentes(choco,Filiere.LA_FILIERE.getEtape()-2))/Filiere.LA_FILIERE.getVentes(choco,Filiere.LA_FILIERE.getEtape()-2)>0.02) {
				ListPrix.replace(choco, ListPrix.get(choco)*1.02);
				//System.out.println(ListPrix.get(choco)+"prix chocolat augmente de 2%");
			}
			else if ((Filiere.LA_FILIERE.getVentes(choco,Filiere.LA_FILIERE.getEtape()-1)-Filiere.LA_FILIERE.getVentes(choco,Filiere.LA_FILIERE.getEtape()-2))/Filiere.LA_FILIERE.getVentes(choco,Filiere.LA_FILIERE.getEtape()-2)<0.02) {
				ListPrix.replace(choco, ListPrix.get(choco)*0.98);
				//System.out.println(ListPrix.get(choco)+"prix chocolat diminue de 2%");
			}
		}
	}
	
	
	/**
	 *@author wiam
	 */
	public double prix(ChocolatDeMarque choco) {
		if (ListPrix.containsKey(choco)) {
			return ListPrix.get(choco);
		} 
		else {
			return 0;
		}
	}
	
	/**
	 *@author wiam
	 */
	public List<Journal> getJournaux() {
		List<Journal> jour = super.getJournaux();
		jour.add(journalVente);
		return jour;
	}
	
	/**
	 *@author wiam
	 */
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if (crypto!=this.cryptogramme
			|| !chocolats.contains(choco)) {
			journalVente.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} 
		else {
			if (choco.getMarque()== "Chocoflow") {
				return Math.abs(Math.min((capaciteDeVente*0.20)/chocoProduits.size(), this.getQuantiteEnStock(choco,crypto)));
			}
			if (choco.toString().contains("C_BQ")) {
				double x = (capaciteDeVente*0.32)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
				return Math.abs(Math.min(x , this.getQuantiteEnStock(choco,crypto)));
			}
			if (choco.toString().contains("C_MQ_E")) {
				double x = (capaciteDeVente*0.12)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
				return Math.abs(Math.min(x , this.getQuantiteEnStock(choco,crypto)));
			}
			if (choco.toString().contains("C_MQ")) {
				double x = (capaciteDeVente*0.12)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
				return Math.abs(Math.min(x , this.getQuantiteEnStock(choco,crypto)));	
			}
			if (choco.toString().contains("C_HQ_BE")) {
				double x = (capaciteDeVente*0.04)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
				return Math.abs(Math.min(x , this.getQuantiteEnStock(choco,crypto)));
			}
			if (choco.toString().contains("C_HQ_E")) {
				double x = (capaciteDeVente*0.08)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
				return Math.abs(Math.min(x , this.getQuantiteEnStock(choco,crypto)));
			}
			if (choco.toString().contains("C_HQ")) {
				double x = (capaciteDeVente*0.12)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
				return Math.abs(Math.min(x , this.getQuantiteEnStock(choco,crypto)));
			}
		}
		return 0.0;
	}
	
	/**
	 * @author wiam
	 */
	public double quantiteEnVenteTotal() {
		double x = 0.0;
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			x = x + this.quantiteEnVente(choc, cryptogramme);
		}
		return x ; 
	}

	/**
	 *@author wiam
	 */
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {		
		double capaciteTG = 0.1 * this.quantiteEnVenteTotal();
		
		if (choco.getMarque()== "Chocoflow") {
			return Math.abs(Math.min((capaciteTG*0.6)/chocoProduits.size(), this.getQuantiteEnStock(choco,crypto)));
		}
		 
		else {
			if(choco.getChocolat().isEquitable()) {
				if(choco.getChocolat().getGamme()==Gamme.MQ) {
					double x =  (0.1 * capaciteTG)/ (nombreMarquesParType.getOrDefault(Chocolat.C_MQ_E, 1)-1);
					return Math.abs(Math.min(this.getQuantiteEnStock(choco,crypto), x));
				}
				if(choco.getChocolat().getGamme()==Gamme.HQ) {
					double x = (0.3 * capaciteTG) / (nombreMarquesParType.getOrDefault(Chocolat.C_HQ_E, 1)+nombreMarquesParType.getOrDefault(Chocolat.C_HQ_BE, 1)-2);
					return Math.abs(Math.min(this.getQuantiteEnStock(choco,crypto), x));
				}
			}
		}
		return 0;     
	}
	
	/**
	 * @author wiam
	 */
	public double quantiteEnVenteTGTotal() {
		double x = 0.0;
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			x = x + this.quantiteEnVenteTG(choc, cryptogramme);
		}
		return x ; 
	}
	
	/**
	 *@author wiam
	 */
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		int pos= (chocolats.indexOf(choco));
		if (pos>=0) {
			stock_Choco.put(choco, this.getQuantiteEnStock(choco,crypto) - quantite) ;
			totalStockChoco.retirer(this, quantite, cryptogramme);
			}
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,client.getNom()+" a acheté "+quantite+" pour "+montant+" d'euros ");
		
	}

	/**
	 *@author wiam
	 */
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE," Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
	}
	
	/**
	 * @author wiam
	 */
	public int getNombreEmploye() {
		return this.nombreEmploye;
	} 
	
	/**
	 * @author wiam
	 */
	public void setNombreEmploye() {
		nombreEmploye=(int)(this.totalStockChoco.getValeur(cryptogramme)/10375);
	}
	
	/**
	 * @author wiam
	 */
	public double Cout_Fixe() {
		double stockage = 120 * this.totalStockChoco.getValeur(cryptogramme);
		double salaire = 1350 * this.getNombreEmploye();
		return stockage + salaire;
	}
	
	/**
	 * @author wiam 
	 */
	public void next() {
		super.next();
		journalVente.ajouter("");
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"==================== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"QuantitéEnVenteTotal à l'Etape "+Filiere.LA_FILIERE.getEtape()+" : " +this.quantiteEnVenteTotal());
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"QuantitéEnVenteTGTotal à l'Etape "+Filiere.LA_FILIERE.getEtape()+" : "+this.quantiteEnVenteTGTotal());
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"=================================");
		journalVente.ajouter("");
		this.setNombreEmploye();
		for (int i=0;i<this.ListPrix.size(); i++) {
			this.setPrix(chocolats.get(i));
		}
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), cryptogramme, "Coût Fixe", this.Cout_Fixe());

	}
	

}
