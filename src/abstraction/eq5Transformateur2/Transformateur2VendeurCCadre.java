package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.produits.Feve;

import abstraction.eqXRomu.produits.IProduit;

public class Transformateur2VendeurCCadre extends Transformateur2AcheteurCCadre implements IVendeurContratCadre {

	public boolean vend(IProduit produit) {
		return produit.getType().equals("Chocolat") && this.getQuantiteEnStock(produit, cryptogramme)>0;
	}

	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		return contrat.getEcheancier();
	}

	public double propositionPrix(ExemplaireContratCadre contrat) {
		return contrat.getPrix();
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		return contrat.getPrix();
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalCC.ajouter("Nouveau contrat :"+contrat);		
	}

	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		journalCC.ajouter("Livraison de : "+quantite+", tonnes de :"+produit.getType()+" provenant du contrat : "+contrat.getNumero());
		stockFeves.put((Feve)produit, stockFeves.get((Feve)produit)-quantite);
		totalStocksFeves.retirer(this, quantite, cryptogramme);
		return quantite;
		}

}
