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
			ListPrix.put(choco, (double) 10000);
		}
		if (choco.getChocolat()==Chocolat.C_MQ) {
			ListPrix.put(choco, (double) 20000);
		}
		if (choco.getChocolat()==Chocolat.C_MQ_E) {
			ListPrix.put(choco, (double) 25000);
		}
		if (choco.getChocolat()==Chocolat.C_HQ) {
			ListPrix.put(choco, (double) 60000);
		}
		if (choco.getChocolat()==Chocolat.C_HQ_E) {
			ListPrix.put(choco, (double) 80000);
		}
		if (choco.getChocolat()==Chocolat.C_HQ_BE) {
			ListPrix.put(choco, (double) 100000);
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
		if (crypto!=this.cryptogramme) {
			journalVente.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} else {
			int pos= (chocolats.indexOf(choco));
			if (pos<0) {
				return 0.0;
			} else {
				return Math.min(capaciteDeVente, this.getQuantiteEnStock(choco,crypto));
			}
		}
	}
	
	public double quantiteEnVenteTotal() {
		double x = 0.0;
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			x = x + this.quantiteEnVente(choc, cryptogramme);
		}
		return x;
	}

	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		double capaciteTG = 0.1 * this.quantiteEnVenteTotal();
		Map<Chocolat, Integer> nombreMarquesParType = new HashMap<>();
	    for (ChocolatDeMarque cm : chocolats) {
	        Chocolat typeChoco = cm.getChocolat();
	        nombreMarquesParType.put(typeChoco, nombreMarquesParType.getOrDefault(typeChoco, 0) + 1);
    }
	    
		if (choco.getMarque()== "Chocoflow") {
			return 0.6 * capaciteTG;
		}
		else {
			if(choco.getChocolat().isEquitable()) {
				if(choco.getChocolat().getGamme()==Gamme.MQ) {
					return 0.1 * capaciteTG/ nombreMarquesParType.getOrDefault(Chocolat.C_MQ_E, 1);
				}
				if(choco.getChocolat().getGamme()==Gamme.HQ) {
					return 0.3 * capaciteTG / nombreMarquesParType.getOrDefault(Chocolat.C_BQ, 1);
				}
			}
		}
		return 0;

	}
	
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		int pos= (chocolats.indexOf(choco));
		if (pos>=0) {
			stock_Choco.put(choco, this.getQuantiteEnStock(choco,crypto) - quantite) ;
			}
	}

	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		journalVente.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
	}
	
	public void next() {
		super.next();
		journalVente.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			for (int i=0; i<this.chocolats.size(); i++) {
			journalVente.ajouter("Le prix moyen du chocolat \""+chocolats.get(i).getNom()+"\" a l'etape precedente etait de "+Filiere.LA_FILIERE.prixMoyen(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-1));
			journalVente.ajouter("Les ventes de chocolat \""+chocolats.get(i)+" il y a un an etaient de "+Filiere.LA_FILIERE.getVentes(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-24));
			}
		}
		this.journalVente.ajouter("=================================");

	}

}
