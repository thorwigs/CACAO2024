package abstraction.eq4Transformateur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur1VendeurCCadre extends Transformateur1VendeurBourse implements IVendeurContratCadre {

	private static int PRIX_DEFAUT = 4500;
	
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCC;
	
	public Transformateur1VendeurCCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>();
		this.journalCC = new Journal(this.getNom()+" journal CC vendeur", this);
	}
	
	@Override
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		
	}

	
	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalCC);
		return res;
	}

	@Override
	public void next() {
		super.next();
		// Choix de l'acheteur
		// Tête de gondole
		// Gestion du prix
		this.journalCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque f : stockChocoMarque.keySet()) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
			if (stockChocoMarque.get(f).getValeur() - restantDu(f)>1200) { // au moins 100 tonnes par step pendant 6 mois
				this.journalCC.ajouter("   "+f+" suffisamment en stock pour passer un CC");
				double parStep = Math.max(100, (stockChocoMarque.get(f).getValeur()-restantDu(f))/24); // au moins 100, et pas plus que la moitie de nos possibilites divisees par 2
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(f);
				
				if (acheteurs.size()>0) {
					IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
					journalCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
					ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, f, e, cryptogramme, false);
					if (contrat==null) {
						journalCC.ajouter(Color.RED, Color.white,"   echec des negociations");
					} else {
						this.contratsEnCours.add(contrat);
						journalCC.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe");
					}
				} else {
					journalCC.ajouter("   pas d'acheteur");
				}
			}
		}
		// On archive les contrats termines
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()==0.0) {
				this.contratsTermines.add(c);
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			this.contratsEnCours.remove(c);
		}
		
		this.demandeCC = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			this.demandeCC += c.getQuantiteALivrerAuStep();
		}
		this.journalCC.ajouter("La demande des contrats cadres est de "+this.demandeCC+"T");
		this.journalCC.ajouter("=================================");
	}
	
	public double restantDu(ChocolatDeMarque f) {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}


	@Override
	public boolean vend(IProduit produit) {
		return produit.getType().equals("ChocolatDeMarque") && stockChocoMarque.get((ChocolatDeMarque)produit) != null && stockChocoMarque.get((ChocolatDeMarque)produit).getValeur()-restantDu((ChocolatDeMarque)produit)>1200;
	}


	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		journalCC.ajouter("      contreProposition("+contrat.getProduit()+" avec echeancier "+contrat.getEcheancier());
		Echeancier ec = contrat.getEcheancier();
		IProduit produit = contrat.getProduit();
		Echeancier res = ec;
		boolean acceptable = produit.getType().equals("ChocolatDeMarque")
				&& ec.getQuantiteTotale()>=1200  // au moins 100 tonnes par step pendant 6 mois
				&& ec.getStepFin()-ec.getStepDebut()>=11   // duree totale d'au moins 12 etapes
				&& ec.getStepDebut()<Filiere.LA_FILIERE.getEtape()+8 // ca doit demarrer dans moins de 4 mois
				&& ec.getQuantiteTotale()<stockChocoMarque.get((ChocolatDeMarque)produit).getValeur()-restantDu((ChocolatDeMarque)produit);
				if (!acceptable) {
					if (!produit.getType().equals("ChocolatDeMarque") || stockChocoMarque.get((ChocolatDeMarque)produit).getValeur()-restantDu((ChocolatDeMarque)produit)<1200) {
						if (!produit.getType().equals("ChocolatDeMarque")) {
							journalCC.ajouter("      ce n'est pas une feve : je retourne null");
						} else {
							journalCC.ajouter("      je n'ai que "+(stockChocoMarque.get((ChocolatDeMarque)produit).getValeur()-restantDu((ChocolatDeMarque)produit))+" de disponible (moins de 1200) : je retourne null");
						}
						return null;
					}
					if (ec.getQuantiteTotale()<=stockChocoMarque.get((ChocolatDeMarque)produit).getValeur()-restantDu((ChocolatDeMarque)produit)) {
						journalCC.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12)));
						return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12));
					} else {
						journalCC.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stockChocoMarque.get((ChocolatDeMarque)produit).getValeur()-restantDu((ChocolatDeMarque)produit)/12))));
						return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stockChocoMarque.get((ChocolatDeMarque)produit).getValeur()-restantDu((ChocolatDeMarque)produit)/12)));
					}
				}
				journalCC.ajouter("      j'accepte l'echeancier");
				return res;
	}
	
	public double prix(ChocolatDeMarque cdm) {
		double res=0;
		List<Double> lesPrix = new LinkedList<Double>();
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(cdm)) {
				lesPrix.add(c.getPrix());
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			if (c.getProduit().equals(cdm)) {
				lesPrix.add(c.getPrix());
			}
		}
		if (lesPrix.size()>0) {
			double somme=0;
			for (Double d : lesPrix) {
				somme+=d;
			}
			res=somme/lesPrix.size();
		}
		return res;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return 0; // ne peut pas etre le cas normalement 
		}
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		double prixCC = prix((ChocolatDeMarque)contrat.getProduit());
		if (prixCC==0.0) {
			PRIX_DEFAUT=(int)(PRIX_DEFAUT*0.98); // on enleve 2% tant qu'on n'a pas passe un contrat
		}
		double prixBaseBourse =  bourse.getCours(((ChocolatDeMarque) contrat.getProduit()).getChocolat().equals(Chocolat.C_HQ_BE)? Feve.F_HQ : Feve.F_MQ).getValeur()*1.5;
		double res = Math.max(Math.max(prixCC*1.25, (double)PRIX_DEFAUT), prixBaseBourse);
		journalCC.ajouter("      propositionPrix retourne "+res);
		return res;
	}


	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return 0; // ne peut pas etre le cas normalement 
		}
		List<Double> prix = contrat.getListePrix();
		if (prix.get(prix.size()-1)>=0.995*prix.get(0)) {
			journalCC.ajouter("      contrePropose le prix demande : "+contrat.getPrix());
			return contrat.getPrix();
		} else {
			int percent = (int)(100* Math.pow((contrat.getPrix()/prix.get(0)), prix.size()));
			int alea = Filiere.random.nextInt(100);
			if (alea< percent) { // d'autant moins de chance d'accepter que le prix est loin de ce qu'on proposait
				if (Filiere.random.nextInt(100)<20) { // 1 fois sur 5 on accepte
					journalCC.ajouter("      contrePropose le prix demande : "+contrat.getPrix());
					return contrat.getPrix();
				} else {
					double res = (prix.get(prix.size()-2)+contrat.getPrix())/2.0; // la mmoyenne des deux derniers prix
					journalCC.ajouter("      contreproposition("+contrat.getPrix()+") retourne "+res);
					return res;
				}
			} else {
				journalCC.ajouter("      contreproposition("+contrat.getPrix()+") retourne "+prix.get(0)*1.05);
				return prix.get(0)*1.05;
			}
		}
	}
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.contratsEnCours.add(contrat);
	}

	@Override
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		double stockActuel = stockChocoMarque.get(produit).getValeur();
		double aLivre = Math.min(quantite, stockActuel);
		journalCC.ajouter("   Livraison de "+aLivre+" T de "+produit+" sur "+quantite+" exigees pour contrat "+contrat.getNumero());
		stockChocoMarque.get((ChocolatDeMarque)produit).setValeur(this, stockActuel-aLivre);
		return aLivre;
	}
	
	
	

}
