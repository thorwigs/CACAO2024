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
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur4VendeurContratCadre extends Transformateur4AcheteurContratCadre implements IVendeurContratCadre {
	private SuperviseurVentesContratCadre supCC;
	protected List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalVCC;

	public Transformateur4VendeurContratCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>();
		this.journalVCC = new Journal(this.getNom()+" journal CC vente", this);
	}
	
	public boolean vend(IProduit produit) {
		return produit.getType().equals("ChocolatDeMarque") 
				&& stockChocoMarque.containsKey(produit)
				&& stockChocoMarque.get(produit)>25000 ; 
		//à modifier selon ce qu'on veut vendre et dans quelles circonstances
		
		//j'ai modifié un truc : on vérifie que produit est bien un de nos chocolat de marque
	}

	//Négociations
	
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		///à modifier selon comment on veut nos échéanciers
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return null;
		}

		if (stockChocoMarque.get((ChocolatDeMarque)(contrat.getProduit()))-restantALivrer((ChocolatDeMarque)(contrat.getProduit()))-contrat.getEcheancier().getQuantiteTotale()>100) {
			if (contrat.getEcheancier().getStepFin()-contrat.getEcheancier().getStepDebut()<11
					|| contrat.getEcheancier().getStepDebut()-Filiere.LA_FILIERE.getEtape()>8) {
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, contrat.getEcheancier().getQuantiteTotale()/12 );
			} else { // les volumes sont corrects, la duree et le debut aussi
				return contrat.getEcheancier();
			}
		} else {
			double marge = - 25000 + stockChocoMarque.get((ChocolatDeMarque)(contrat.getProduit())) - restantALivrer((ChocolatDeMarque)(contrat.getProduit()));
			if (marge<100) {
				return null;
			} else {
				double quantite = 25000 + Filiere.random.nextDouble()*(marge-100); // un nombre aleatoire entre 25000 et la marge
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, quantite/12 );
			}
		}
	}

	public double propositionPrix(ExemplaireContratCadre contrat) {
		return contrat.getQuantiteTotale()*5500;
	}//à modifier selon variation du nb d'employer

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		return contrat.getPrix();
	}//à modifier selon notre prix minimal accepté

	//Après finalisation contrat 
	
	public double livrer(IProduit p, double quantite, ExemplaireContratCadre contrat) {
		double stock = (double)stockChocoMarque.get((ChocolatDeMarque)(contrat.getProduit()));
		if (quantite>=stock) {
			journalVCC.ajouter("Livraison de "+quantite+" T de "+p+" du contrat "+contrat.getNumero());
			stockChocoMarque.put((ChocolatDeMarque)p, stockChocoMarque.get((ChocolatDeMarque)p)-quantite);
			totalStocksChocoMarque.retirer(this, quantite, cryptogramme);
			return quantite;
		}
		else {
			journalVCC.ajouter("Livraison de "+stock+" T de "+p+" du contrat "+contrat.getNumero());
			stockChocoMarque.put((ChocolatDeMarque)p, stockChocoMarque.get((ChocolatDeMarque)p)-stock);
			totalStocksChocoMarque.retirer(this, stock, cryptogramme);
			return stock;
		}
		
	}//s'inspirer de AcheteurCC
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if (contrat.getVendeur().equals(this)) {
			journalVCC.ajouter("Nouveau contrat :"+contrat);
			this.contratsEnCours.add(contrat);
		}
		else {
			super.notificationNouveauContratCadre(contrat);
		}
	}
	
	//Honorer le contrat
	
	public double restantALivrer(ChocolatDeMarque choco) {
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
		
	public void next() { 
		super.next();
		this.journalVCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
				for (ChocolatDeMarque choco : stockChocoMarque.keySet()) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
					if (stockChocoMarque.get(choco)+restantALivrer(choco)>30000) { 
						this.journalVCC.ajouter("   "+choco+" suffisamment trop en stock/contrat pour passer un CC");
						double parStep = Math.max(100, (-20000+stockChocoMarque.get(choco)+restantALivrer(choco))/12); // au moins 100
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
