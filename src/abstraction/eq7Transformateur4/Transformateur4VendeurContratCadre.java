package abstraction.eq7Transformateur4;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur4VendeurContratCadre extends Transformateur4AcheteurContratCadre implements IVendeurContratCadre {
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCCvente;

	public Transformateur4VendeurContratCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>();
		this.journalCCvente = new Journal(this.getNom()+" journal CC vente", this);
	}
	
	public boolean vend(IProduit produit) {
		return produit.getType().equals("Chocolat") 
				&& stockChoco.get(produit)>450000; 
		//à modifier selon ce qu'on veut vendre et dans quelles circonstances
	}

	//Négociations
	
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
	}//s'inspirer de AcheteurCC

	public double propositionPrix(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}//s'inspirer de AcheteurCC

	//Après finalisation contrat 
	
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}//s'inspirer de AcheteurCC
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalCCvente.ajouter("Nouveau contrat :"+contrat);
		this.contratsEnCours.add(contrat);
	}
	
	//Honorer le contrat
	
	public double restantDu(Feve f) {
		double res=0;			
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}

	public double restantAPayer() {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			res+=c.getMontantRestantARegler();
		}
		return res;
	}
		
	//Next
		
	public void next() {
		super.next();
	}
	
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalCCvente);
		return jx;
	}
}
