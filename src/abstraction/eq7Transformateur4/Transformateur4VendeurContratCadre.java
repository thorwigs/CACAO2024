package abstraction.eq7Transformateur4;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur4VendeurContratCadre extends Transformateur4AcheteurContratCadre implements IVendeurContratCadre {
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalVCC;

	public Transformateur4VendeurContratCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>();
		this.journalVCC = new Journal(this.getNom()+" journal CC vente", this);
	}
	
	public boolean vend(IProduit produit) {
		return produit.getType().equals("Chocolat") 
				&& stockChoco.get(produit)>450000; 
		//à modifier selon ce qu'on veut vendre et dans quelles circonstances
	}

	//Négociations
	
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		///à modifier selon comment on veut nos échéanciers
		if (!contrat.getProduit().getType().equals("Chocolat")) {
			return null;
		}

		if (stockChoco.get((Chocolat)(contrat.getProduit()))+restantALivrer((Chocolat)(contrat.getProduit()))+contrat.getEcheancier().getQuantiteTotale()>450000) {
			if (contrat.getEcheancier().getStepFin()-contrat.getEcheancier().getStepDebut()<11
					|| contrat.getEcheancier().getStepDebut()-Filiere.LA_FILIERE.getEtape()>8) {
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, contrat.getEcheancier().getQuantiteTotale()/12 );
			} else { // les volumes sont corrects, la duree et le debut aussi
				return contrat.getEcheancier();
			}
		} else {
			double marge = - 450000 + stockChoco.get((Chocolat)(contrat.getProduit())) + restantALivrer((Chocolat)(contrat.getProduit()));
			if (marge<1200) {
				return null;
			} else {
				double quantite = 1200 + Filiere.random.nextDouble()*(marge-1200); // un nombre aleatoire entre 1200 et la marge
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, quantite/12 );
			}
		}
	}

	public double propositionPrix(ExemplaireContratCadre contrat) {
		return contrat.getQuantiteTotale()*5500;
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}//s'inspirer de AcheteurCC

	//Après finalisation contrat 
	
	public double livrer(IProduit p, double quantite, ExemplaireContratCadre contrat) {
		double stock = (double)stockChoco.get((Chocolat)(contrat.getProduit()));
		if (quantite>=stock) {
			journalVCC.ajouter("Livraison de "+quantite+" T de "+p+" du contrat "+contrat.getNumero());
			stockChoco.put((Chocolat)p, stockChoco.get((Chocolat)p)-quantite);
			totalStocksChoco.retirer(this, quantite, cryptogramme);
			return quantite;
		}
		else {
			journalVCC.ajouter("Livraison de "+stock+" T de "+p+" du contrat "+contrat.getNumero());
			stockChoco.put((Chocolat)p, stockChoco.get((Chocolat)p)-stock);
			totalStocksChoco.retirer(this, stock, cryptogramme);
			return stock;
		}
		
	}//s'inspirer de AcheteurCC
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalVCC.ajouter("Nouveau contrat :"+contrat);
		this.contratsEnCours.add(contrat);
	}
	
	//Honorer le contrat
	
	public double restantALivrer(Chocolat choco) {
		double res=0;			
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(choco)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}

	public double restantPayeARecevoir() {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			res+=c.getMontantRestantARegler();
		}
		return res;
	}
		
	//Next
		
	public void next() { //à modifier
		super.next();
		this.journalVCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
				for (Chocolat choco : stockChoco.keySet()) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
					if (stockChoco.get(choco)+restantALivrer(choco)>30000) { 
						this.journalVCC.ajouter("   "+choco+" suffisamment trop en stock/contrat pour passer un CC");
						double parStep = Math.max(100, (-20000+stockChoco.get(choco)+restantALivrer(choco))/12); // au moins 100
						Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
						List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(choco);
						if (acheteurs.size()>0) {
							IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
							journalVCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
							ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, choco, e, cryptogramme, true);
							if (contrat==null) {
								journalVCC.ajouter(Color.RED, Color.white,"   echec des negociations");
							} else {
								this.contratsEnCours.add(contrat);
								journalVCC.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe");
							}
						} else {
							journalVCC.ajouter("   pas d'acheteur");
						}
					}
				}
		// On archive les contrats terminés  (pas à modifier)
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()==0.0 && c.getMontantRestantARegler()<=0.0) {
				this.contratsTermines.add(c);
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			journalVCC.ajouter("Archivage du contrat "+c);
			this.contratsEnCours.remove(c);
		}
		this.journalVCC.ajouter("=================================");
	}
	
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalVCC);
		return jx;
	}

}
