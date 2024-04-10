package abstraction.eq9Distributeur2;

import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public abstract class Distributeur2AppelOffre extends Distributeur2ContratCadre implements IAcheteurAO {
	protected Journal journal_AO;
	public Distributeur2AppelOffre() {
		super();
		this.journal_AO= new Journal(this.getNom()+" journal Appel d'offre", this);
	}
	
	
	public void next() {
		super.next();
		this.FaireAppelDOffre();
		}
	
	
	
	public OffreVente choisirOV(List<OffreVente> propositions) {
		Double rapportmin=Double.MAX_VALUE;
		OffreVente propRetenue = null;
		HashMap<OffreVente, Double> PropositionQuantitePrix = new HashMap<OffreVente, Double>();
		for (OffreVente proposition : propositions) {
			Double QuantitePrix =proposition.getQuantiteT()/ proposition.getPrixT();
			PropositionQuantitePrix.put(proposition, QuantitePrix);
			
			if (QuantitePrix<rapportmin) {
				propRetenue = proposition;
			}
		}
		return propRetenue;
	}

	public void FaireAppelDOffre() {
		for (ChocolatDeMarque chocolat : this.stockChocoMarque.keySet()) {
			if (this.stockChocoMarque.get(chocolat)<=0) {
				
				Double quantite = Filiere.LA_FILIERE.getVentes(chocolat, -24);
				OffreVente propRetenue=((SuperviseurVentesAO) Filiere.LA_FILIERE.getActeur("Sup.AO")).acheterParAO(this,this.cryptogramme,chocolat,quantite);
				this.getJournaux().get(2).ajouter("On a plus de "+chocolat+" et on en cherche "+quantite);
				if (propRetenue != null) {
					//this.getJournaux().get(2).ajouter("On a réaliser un appel d'offre "+PropositionQuantitePrix.toString());
					this.getJournaux().get(2).ajouter("L'appel d'offre est réussie et l'option choisie est: "+propRetenue.getQuantiteT() +" tonnes de "+propRetenue.getProduit()+" chez "+propRetenue.getVendeur()+ " pour un prix de "+propRetenue.getPrixT());
					this.getStockChocoMarque().put((ChocolatDeMarque) propRetenue.getProduit(),propRetenue.getQuantiteT());
					
				}
			}
		}
	}
	
	public List<Journal> getJournaux() {
		List<Journal> res= super.getJournaux();
		res.add(this.journal_AO);
		return res;
	}
	
}
