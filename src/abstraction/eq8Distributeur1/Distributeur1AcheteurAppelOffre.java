package abstraction.eq8Distributeur1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

public class Distributeur1AcheteurAppelOffre extends Distributeur1AcheteurContratCadre implements IAcheteurAO{
	private HashMap<ChocolatDeMarque, List<Double>> prixRetenus;
	private SuperviseurVentesAO supAO;
	protected Journal journalAO;
	
	public Distributeur1AcheteurAppelOffre() {
		super();
		this.journalAO= new Journal (this.getNom() +"Journal AO", this);
	}
	public void initialiser() {
		super.initialiser();
		this.supAO = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
		this.prixRetenus = new HashMap<ChocolatDeMarque, List<Double>>();
		for (ChocolatDeMarque cm : this.stock_Choco.keySet()) {
			this.prixRetenus.put(cm, new LinkedList<Double>());
		}
	}
	
	
	public void next() {
		super.next();
		this.journalAO.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (this.achete(choc)) {
				double x = 1000 ;
				OffreVente ov = supAO.acheterParAO(this,  cryptogramme, choc, x);
				journalAO.ajouter("   Je lance un appel d'offre de "+x+" T de "+choc);
				if (ov!=null) {
					journalAO.ajouter("   AO finalise : on ajoute "+x+" T de "+produit+" au stock");
					stock_Choco.put(choc, x);
					totalStockChoco.ajouter(this, x, cryptogramme);
				}
			
			}
			
		}

		// On archive les contrats termines
		this.journalAO.ajouter("=================================");
	}
	
	
	public OffreVente choisirOV(List<OffreVente> propositions) {
		
		return null;
	}
	
	public List<Journal> getJournaux(){
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}
	
	public boolean achete(IProduit produit) {
				double a = 0 ; 
				for (int i=0; i<contrat_en_cours.size(); i++) {
					if (contrat_en_cours.get(i).getProduit().equals(produit)) {
						a = a + contrat_en_cours.get(i).getQuantiteRestantALivrer();
					}
				}
				return (produit.getType().equals("ChocolatDeMarque")
						&& 0 < this.prevision(produit, 24) - this.stock_Choco.get(produit) - a 
						&& this.prevision(produit, 24) - this.stock_Choco.get(produit) - a <= 1000); 
//				return true ; 
			}
	

}
