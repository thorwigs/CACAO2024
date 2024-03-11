package abstraction.eqXRomu.acteurs;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class TransformateurXAcheteurCCadre extends TransformateurXAcheteurBourse implements IAcheteurContratCadre {
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCC;

	public TransformateurXAcheteurCCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>();
		this.journalCC = new Journal("TX Journal CC", this);
	}

	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}

	public void next() {
		super.next();
		this.journalCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		//		for (Feve f : stockFeves.keySet()) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
		//			if (stockFeves.get(f).getValeur()-restantDu(f)>1200) { // au moins 100 tonnes par step pendant 6 mois
		//				this.journalCC.ajouter("   "+f+" suffisamment en stock pour passer un CC");
		//				double parStep = Math.max(100, (stock.get(f).getValeur()-restantDu(f))/24); // au moins 100, et pas plus que la moitie de nos possibilites divisees par 2
		//				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
		//				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(f);
		//				if (acheteurs.size()>0) {
		//					IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
		//					journalCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
		//					ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, f, e, cryptogramme, false);
		//					if (contrat==null) {
		//						journalCC.ajouter(Color.RED, Color.white,"   echec des negociations");
		//					} else {
		//						this.contratsEnCours.add(contrat);
		//						journalCC.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe");
		//					}
		//				} else {
		//					journalCC.ajouter("   pas d'acheteur");
		//				}
		//			}
		//		}
		// On archive les contrats termines
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()==0.0 && c.getMontantRestantARegler()<=0.0) {
				this.contratsTermines.add(c);
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			journalCC.ajouter("Archivage du contrat "+c);
			this.contratsEnCours.remove(c);
		}
	}

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

	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalCC);
		return jx;
	}

	public boolean achete(IProduit produit) {
		return produit.getType().equals("Feve") 
				&& stockFeves.get(produit)+restantDu((Feve)produit)<150000;
	}

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("Feve")) {
			return null;
		}

		if (stockFeves.get((Feve)(contrat.getProduit()))+restantDu((Feve)(contrat.getProduit()))+contrat.getEcheancier().getQuantiteTotale()<150000) {
			if (contrat.getEcheancier().getStepFin()-contrat.getEcheancier().getStepDebut()<11
					|| contrat.getEcheancier().getStepDebut()-Filiere.LA_FILIERE.getEtape()>8) {
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, contrat.getEcheancier().getQuantiteTotale()/12 );
			} else { // les volumes sont corrects, la duree et le debut aussi
				return contrat.getEcheancier();
			}
		} else {
			double marge = 150000 - stockFeves.get((Feve)(contrat.getProduit())) - restantDu((Feve)(contrat.getProduit()));
			if (marge<1200) {
				return null;
			} else {
				double quantite = 1200 + Filiere.random.nextDouble()*(marge-1200); // un nombre aleatoire entre 1200 et la marge
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, quantite/12 );
			}
		}
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		// Il faudrait normalement tenir compte du volume du contrat (plus le volume est important 
		// plus les prix seront bas) et de l'urgence (si on n'en n'a pas en stock et pas de CC alors 
		// il devient plus urgent d'en disposer et donc on acceptera davantage un prix eleve)
		// mais dans cet acteur trivial on ne se base que sur le prix et via des tirages aleatoires.
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, cryptogramme);
		if (((Feve)contrat.getProduit()).isEquitable()) { // pas de cours en bourse
			double max = bourse.getCours(Feve.F_MQ).getMax()*1.25;
			double alea = Filiere.random.nextInt((int)max);
			if (contrat.getPrix()<alea) {
				return contrat.getPrix();
			} else {
				return bourse.getCours(Feve.F_MQ).getValeur()*(1+(Filiere.random.nextInt(25)/100.0)); // entre 1 et 1.25 le prix de F_MQ
			}
		} else {
			double cours = bourse.getCours((Feve)contrat.getProduit()).getValeur();
			double coursMax = bourse.getCours((Feve)contrat.getProduit()).getMax();
			int alea = Filiere.random.nextInt((int)(coursMax-cours));
			if (contrat.getPrix()<cours+alea) {
				return contrat.getPrix();
			} else {
				return cours*(1.1-(Filiere.random.nextDouble()/3.0));
			}
		}
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalCC.ajouter("Nouveau contrat :"+contrat);
		this.contratsEnCours.add(contrat);
	}

	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		journalCC.ajouter("Reception de "+quantiteEnTonnes+" T de "+p+" du contrat "+contrat.getNumero());
		stockFeves.put((Feve)p, stockFeves.get((Feve)p)+quantiteEnTonnes);
	}

}
