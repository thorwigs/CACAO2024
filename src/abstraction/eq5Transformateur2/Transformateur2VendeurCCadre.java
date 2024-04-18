package abstraction.eq5Transformateur2;

import java.awt.Color;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur2VendeurCCadre extends Transformateur2AcheteurCCadre implements IVendeurContratCadre {
	
	public Transformateur2VendeurCCadre () {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
	}
	
	public void next() {
		super.next();
		this.journalCC.ajouter("===VENDEUR=========STEP"+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque cm : chocosProduits) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
			if ((this.stockFeves.get(f)>0) & (f.getGamme()!=Gamme.HQ)) { // Modifier quantité minimale avant achat
				this.journalCC.ajouter("   "+f+" suffisamment peu en stock pour passer un CC");
				double parStep = 35000; // Changer quantité par Step
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 78, parStep);
				List<IVendeurContratCadre> vendeurs = supCC.getVendeurs(f);
				if (vendeurs.size()>0) {
					IVendeurContratCadre vendeur = vendeurs.get(Filiere.random.nextInt(vendeurs.size()));
					for (IVendeurContratCadre v : this.BlackListVendeur.keySet()) {
						if (this.BlackListVendeur.containsKey(vendeur)) {
							if (this.BlackListVendeur.get(vendeur)>this.BlackListVendeur.get(v)) { // Choisit le vendeur avec qui le moins de négociations a échoué
								vendeur = v;
							}
						}
					}
					journalCC.ajouter("   "+vendeur.getNom()+" retenu comme vendeur parmi "+vendeurs.size()+" vendeurs potentiels");
					ExemplaireContratCadre contrat = supCC.demandeAcheteur(this, vendeur, f, e, cryptogramme, false);
					if (contrat==null) {
						if (this.BlackListVendeur.containsKey(vendeur)) {
							this.BlackListVendeur.put(vendeur,this.BlackListVendeur.get(vendeur)+1);
						} else {
							this.BlackListVendeur.put(vendeur, 1);
						}
						journalCC.ajouter(Color.RED, Color.white,"   echec des negociations -- échec de "+this.BlackListVendeur.get(vendeur)+" contrats avec : "+vendeur);
						this.Etapenego=0;
					} else {
						this.contratsEnCours.add(contrat);
						this.Etapenego=0;
						journalCC.ajouter(Color.GREEN, vendeur.getColor(), "   contrat signe : #"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());
					}
				} else {
					journalCC.ajouter("   pas de vendeur");
					this.Etapenego=0;
			}
			} else {
				if (f.getGamme()!=Gamme.HQ) {
				journalCC.ajouter(f+" suffisament de stock pour ne pas passer de contrat cadre");
					}
				this.Etapenego=0;
			}
		}	
// On archive les contrats termines
for (ExemplaireContratCadre c : this.contratsEnCours) {
	if (c.getQuantiteRestantALivrer()==0.0 && c.getMontantRestantARegler()<=0.0) {
		journalCC.ajouter(Color.YELLOW, Color.BLACK,"Archivage du contrat : "+"#"+c.getNumero()+" | Acheteur : "+c.getAcheteur()+" | Vendeur : "+c.getVendeur()+" | Produit : "+c.getProduit()+" | Quantité totale : "+c.getQuantiteTotale()+" | Prix : "+c.getPrix());
		this.contratsTermines.add(c);
	}
}
for (ExemplaireContratCadre c : this.contratsTermines) {
	this.contratsEnCours.remove(c);
}
this.journalCC.ajouter("Nombre de contrats en cours : "+this.contratsEnCours.size());
this.journalCC.ajouter("Nombre de contrats termines : "+this.contratsTermines.size());
this.journalCC.ajouter("=================================");
}
	}
	
	/***
	 * Robin, Erwann
	 */
	public boolean vend(IProduit produit) {
		return (produit.getType().equals("Chocolat") && this.getQuantiteEnStock(produit, cryptogramme)>0)
				|| (produit.getType().equals("Feve") && this.getQuantiteEnStock(produit, cryptogramme)>10000)
				|| (this.chocosProduits.contains(produit) && this.getQuantiteEnStock(produit, cryptogramme)>0) ; //Valeur à changer
	}
	
	/***
	 * Robin, Vincent
	 */
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getEcheancier().getQuantiteTotale()< this.totalStocksChoco.getValeur()){
			return contrat.getEcheancier(); //peut être changé
		}else {
			return null;
		}
		}
	
	public double propositionPrix(ExemplaireContratCadre contrat) {
		return 1000.00;
	}
	/**
	 * Vincent
	 */
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (Filiere.random.nextDouble() < 0.2) { // 20% des cas
	        return contrat.getPrix(); // ne refait pas de contreproposition
	    } else {
	        return contrat.getPrix() * 1.07; // Contreproposition de 7% à la hausse
	    }
	}
	
	/***
	 * Robin
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if (contrat.getVendeur().equals(this)) {
			this.journalCC.ajouter("Nouveau contrat vendeur accepté : "+"#"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());	
			this.contratsEnCours.add(contrat);
		} else {
			super.notificationNouveauContratCadre(contrat);
		}
	}
	
	/***
	 * Robin
	 */
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		this.journalCC.ajouter("Livraison de : "+quantite+", tonnes de :"+produit.getType()+" provenant du contrat : "+contrat.getNumero());
		this.stockFeves.put((Feve)produit, stockFeves.get((Feve)produit)-quantite);
		this.totalStocksFeves.retirer(this, quantite, cryptogramme);
		return quantite;
		}

}
