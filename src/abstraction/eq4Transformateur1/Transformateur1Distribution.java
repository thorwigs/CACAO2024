package abstraction.eq4Transformateur1;

import java.util.HashMap;
import java.util.List;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

/**
 * @author yannig_charonnat
 */

public class Transformateur1Distribution extends Transformateur1AcheteurCCadre implements IDistributeurChocolatDeMarque {
    
    private double pourcentageVenteDirecte;
    private Journal journalVD;
    private HashMap<ChocolatDeMarque, Double> quantiteEnStock;

    public Transformateur1Distribution() {
    	super();
        this.pourcentageVenteDirecte = 0.15;
        this.journalVD = new Journal("Ventes Directes", this);
    }
    
    public void initialiser() {
    	super.initialiser();
    	this.quantiteEnStock = new HashMap<ChocolatDeMarque, Double>();
    	for(ChocolatDeMarque cdm : this.stockChocoMarque.keySet()) {
    		this.quantiteEnStock.put(cdm, 0.);
    	}
    }

    public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalVD);
		return jx;
	}

	@Override
	public double prix(ChocolatDeMarque choco) {
		double prixMoyen = 0;
		int nbPrix = 0;
		
		for(ExemplaireContratCadre c :this.contratsEnCoursVente) {
			if(c.getProduit() == choco) {
				for(double p: c.getListePrix()) {
					prixMoyen += p;
					nbPrix++;
				}
			}
		}
		if (nbPrix != 0) {
			return prixMoyen/nbPrix * 1.1;
		} return PRIX_DEFAUT.get(choco.getGamme()) * 1.1;
	}

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		double aVendre = 0.0;
		if((choco.getGamme()==Gamme.HQ && this.stockChocoMarque.keySet().contains(choco)) 
				|| (Filiere.LA_FILIERE.getDistributeurs().size() == 1 && this.stockChocoMarque.keySet().contains(choco))) {
			if (this.stockChocoMarque.get(choco).getValeur() - this.demandeCC.get(choco.getGamme()) > this.stockChocoMarque.get(choco).getValeur() * this.pourcentageVenteDirecte) {
				aVendre = this.stockChocoMarque.get(choco).getValeur() * this.pourcentageVenteDirecte;
			} else {
				aVendre = Math.max(this.stockChocoMarque.get(choco).getValeur() - this.demandeCC.get(choco.getGamme()), 0);
			}
		this.journalVD.ajouter("mise en vente de " + aVendre + "T de chocolat "+ choco + ", déjà en stock : "+this.quantiteEnStock.get(choco));	
		}
		if(this.quantiteEnStock.get(choco) != null) {
			this.quantiteEnStock.put(choco, this.quantiteEnStock.get(choco) + aVendre);
		} else {
			this.quantiteEnStock.put(choco, aVendre);
		}
		return aVendre;
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		return this.quantiteEnVente(choco, crypto) * 0.099; //maximum
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		if (this.stockChocoMarque!=null && this.stockChocoMarque.keySet().contains(choco)) {
			this.stockChocoMarque.get(choco).retirer(this, quantite);
			this.totalStocksChocoMarque.retirer(this,  quantite, cryptogramme);
			this.quantiteEnStock.put(choco, this.quantiteEnStock.get(choco) - quantite);
			this.journalVD.ajouter("vente de "+quantite+" T de "+choco+" pour un prix de "+montant+" !");
		}
		
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		this.pourcentageVenteDirecte = Math.min(0.75, this.pourcentageVenteDirecte*1.05); // A modifier pour plus de réalisme mais là on est les seuls distributeurs...
		this.journalVD.ajouter("Plus de chocolat : "+choco+" en rayon");
	}
}