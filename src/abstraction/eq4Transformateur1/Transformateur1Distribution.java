package abstraction.eq4Transformateur1;

import java.util.HashMap;
import java.util.List;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class Transformateur1Distribution extends Transformateur1AcheteurCCadre implements IDistributeurChocolatDeMarque {
    
    private Transformateur1Acteur transformateur;
    private double pourcentageVenteDirecte;
    private HashMap<ChocolatDeMarque, Variable> ventesDirectes;
    private Journal journalVD;

    public Transformateur1Distribution() {
    	super();
        this.pourcentageVenteDirecte = 0.15;
        //this.ventesDirectes = new HashMap<>();
        this.journalVD = new Journal("Ventes Directes", this);
        
        /*
        List<ChocolatDeMarque> chocosProduits = this.getChocolatsProduits();
        for (ChocolatDeMarque choco : chocosProduits) {
            if (choco.getMarque().equals("LeaderKakao")) {
                this.ventesDirectes.put(choco, new Variable("Ventes directes de " + choco.getNom(), this));
            }
        }
        */
    }

    /*
    public void vendreDirectement(ChocolatDeMarque choco, double quantite) {
        if (this.ventesDirectes.containsKey(choco) && quantite > 0) {
            double stock = this.getQuantiteEnStock(choco, this.cryptogramme);
            double quantiteVendue = Math.min(stock * this.pourcentageVenteDirecte, quantite);
            this.ventesDirectes.get(choco).ajouter(this, quantiteVendue);
            this.stockChocoMarque.get(choco).retirer(this, quantiteVendue);
            this.journal.ajouter("Vente directe de " + quantiteVendue + " tonnes de " + choco.getNom());
        }
    }

    public HashMap<ChocolatDeMarque, Variable> getVentesDirectes() {
        return this.ventesDirectes;
    }
    */

    public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalVD);
		return jx;
	}

	@Override
	public double prix(ChocolatDeMarque choco) {
		double prixMoyen = 0;
		int nbPrix = 0;
		
		for(ExemplaireContratCadre c :this.contratsEnCours) {
			if(c.getProduit() == choco) {
				for(double p: c.getListePrix()) {
					prixMoyen += p;
					nbPrix++;
				}
			}
		}
		if (nbPrix != 0) {
			return prixMoyen/nbPrix * 1.1;
		} return PRIX_DEFAUT * 1.1;
	}

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if(choco.getGamme()==Gamme.HQ && this.stockChocoMarque.keySet().contains(choco)) {
			//System.out.println("qauntité en vente : "+this.stockChocoMarque.get(choco).getValeur()+" ; "+this.pourcentageVenteDirecte);
			return this.stockChocoMarque.get(choco).getValeur() * this.pourcentageVenteDirecte;
		}
		return 0;
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		return this.quantiteEnVente(choco, crypto) * 0.099; //maximum
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		if (this.stockChocoMarque!=null && this.stockChocoMarque.keySet().contains(choco)) {
			this.stockChocoMarque.get(choco).setValeur(this, this.stockChocoMarque.get(choco).getValeur()-quantite);
			this.totalStocksChocoMarque.retirer(this,  quantite, cryptogramme);
			this.journalVD.ajouter("vente de "+quantite+" T de "+choco+" pour un prix de "+montant+" !");
		}
		
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		this.pourcentageVenteDirecte = Math.min(0.75, this.pourcentageVenteDirecte*1.05); // A modifier pour plus de réalisme mais là on est les seuls distributeurs...
		this.journalVD.ajouter("Plus de chocolat : "+choco+" en rayon");
	}
}