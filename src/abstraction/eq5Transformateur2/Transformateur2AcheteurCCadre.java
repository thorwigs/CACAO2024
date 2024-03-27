package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur2AcheteurCCadre extends Transformateur2Acteur implements IAcheteurContratCadre {
	private SuperviseurVentesContratCadre supCC;
	protected Journal journalCC;
	
	
	
	
	
	public boolean achete(IProduit produit) {
		return produit.getType().equals("Feve") ;
	}

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		
		return contrat.getEcheancier();
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		return contrat.getPrix();
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalCC.ajouter("Nouveau contrat :"+contrat);		
	}

	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		journalCC.ajouter("Réception de : "+quantiteEnTonnes+" Tonnes de :"+p.getType()+" provenant du contrat : "+contrat.getNumero());
		stockFeves.put((Feve)p, stockFeves.get((Feve)p)+quantiteEnTonnes);
		totalStocksFeves.ajouter(this, quantiteEnTonnes, cryptogramme);
	}
	
}
