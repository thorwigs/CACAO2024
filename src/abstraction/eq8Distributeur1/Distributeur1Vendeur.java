package abstraction.eq8Distributeur1;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class Distributeur1Vendeur extends Distributeur1Acteur implements IDistributeurChocolatDeMarque {
	protected double capaciteDeVente;
	protected  HashMap<ChocolatDeMarque, Double> ListPrix;
	protected String[] marques;
	protected Journal journalVente;
	
	public Distributeur1Vendeur() {
		super();
		this.capaciteDeVente=120000.0;  //capacite de vente par step
		this.ListPrix = new HashMap<ChocolatDeMarque, Double>();
		this.marques = new String[chocolats.size()];
		this.journalVente= new Journal (this.getNom() + " journal des ventes", this);
	}


	public void setPrix(ChocolatDeMarque choco) {
		if (choco.getChocolat()==Chocolat.C_BQ) {
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
	
	
	public double prix(ChocolatDeMarque choco) {
		if (ListPrix.containsKey(choco)) {
			return ListPrix.get(choco);
		} 
		else {
			return 0;
		}
	}
	
	public List<Journal> getJournaux() {
		List<Journal> jour = super.getJournaux();
		jour.add(journalVente);
		return jour;
	}
	
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if (crypto!=this.cryptogramme
			|| !chocolats.contains(choco)) {
			journalVente.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} 
		else {
			if (choco.getMarque()== "Chocoflow") {
				return Math.min((capaciteDeVente*0.20)/chocoProduits.size(), this.getQuantiteEnStock(choco,crypto));
			}
			if (choco.toString().contains("C_BQ")) {
				double x = (capaciteDeVente*0.32)/this.nombreMarquesParType.get(choco.getChocolat());
				return Math.min(x , this.getQuantiteEnStock(choco,crypto));
			}
			if (choco.toString().contains("C_MQ")) {
				double x = (capaciteDeVente*0.12)/this.nombreMarquesParType.get(choco.getChocolat());
				return Math.min(x , this.getQuantiteEnStock(choco,crypto));	
			}
			if (choco.toString().contains("C_MQ_E")) {
				double x = (capaciteDeVente*0.12)/this.nombreMarquesParType.get(choco.getChocolat());
				return Math.min(x , this.getQuantiteEnStock(choco,crypto));
			}
			if (choco.toString().contains("C_HQ")) {
				double x = (capaciteDeVente*0.12)/this.nombreMarquesParType.get(choco.getChocolat());
				return Math.min(x , this.getQuantiteEnStock(choco,crypto));
			}
			if (choco.toString().contains("C_HQ_E")) {
				double x = (capaciteDeVente*0.08)/this.nombreMarquesParType.get(choco.getChocolat());
				return Math.min(x , this.getQuantiteEnStock(choco,crypto));
			}
			if (choco.toString().contains("C_HQ_BE")) {
				double x = (capaciteDeVente*0.04)/this.nombreMarquesParType.get(choco.getChocolat());
				return Math.min(x , this.getQuantiteEnStock(choco,crypto));
			}
		}
		return 0.0;
	}
	
	public double quantiteEnVenteTotal() {
		double x = 0.0;
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			x = x + this.quantiteEnVente(choc, cryptogramme);
		}
		return x ; 
	}

	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {		
/*		double capaciteTG = 0.1 * this.quantiteEnVenteTotal();
		
		if (choco.getMarque()== "Chocoflow") {
			return Math.min((capaciteTG*0.60)/chocoProduits.size(), this.getQuantiteEnStock(choco,crypto));
		}
		
		else {
			if(choco.getChocolat().isEquitable()) {
				if(choco.getChocolat().getGamme()==Gamme.MQ) {
					double x =  0.1 * capaciteTG/ nombreMarquesParType.getOrDefault(Chocolat.C_MQ_E, 1);
					return Math.min(this.getQuantiteEnStock(choco,crypto), x);
				}
				if(choco.getChocolat().getGamme()==Gamme.HQ) {
					double x = 0.3 * capaciteTG / (nombreMarquesParType.getOrDefault(Chocolat.C_HQ_E, 1)+nombreMarquesParType.getOrDefault(Chocolat.C_HQ_BE, 1));
					return Math.min(this.getQuantiteEnStock(choco,crypto), x);
				}
			}
		}
		return 0;   */  
		
	return this.quantiteEnVente(choco, crypto)/10.0;
	}
	
	public double quantiteEnVenteTGTotal() {
		double x = 0.0;
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			x = x + this.quantiteEnVenteTG(choc, cryptogramme);
		}
		return x ; 
	}
	
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		int pos= (chocolats.indexOf(choco));
		if (pos>=0) {
			stock_Choco.put(choco, this.getQuantiteEnStock(choco,crypto) - quantite) ;
			totalStockChoco.retirer(this, quantite, cryptogramme);
			}
		journalVente.ajouter(client.getNom()+" a acheté "+quantite+" pour "+montant+" d'euros ");
		
	}

	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		journalVente.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
	}
	
	public void next() {
		super.next();
		journalVente.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		journalVente.ajouter("QuantitéEnVenteTGTotal : "+this.quantiteEnVenteTGTotal());
/*		System.out.println("Etape : "+Filiere.LA_FILIERE.getEtape());
		System.out.println("quantiteEnVenteTGTotal : "+this.quantiteEnVenteTGTotal());
		System.out.println("quantiteEnVenteTotal/10 : "+this.quantiteEnVenteTotal()*0.1);
		System.out.println("capaciteDeVente/10 : "+this.capaciteDeVente*0.1);
		for (ChocolatDeMarque choc : chocolats) {
			System.out.println(choc);
			System.out.println("Stock de "+choc.getNom()+" : "+stock_Choco.get(choc));
			System.out.println("Quantite en vente de "+choc.getNom()+" : "+this.quantiteEnVente(choc, cryptogramme));
			System.out.println("Quantite en vente TG de "+choc.getNom()+" : "+this.quantiteEnVenteTG(choc, cryptogramme));
		}
		System.out.println("");   */
		journalVente.ajouter("=================================");
		

	}

}
