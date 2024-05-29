package abstraction.eq2Producteur2;

import java.awt.Color;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;


/** classe Contrat Cadre
 * @author Maxime
 * @author Prof
 */

public abstract class Producteur2VendeurCCadre extends Producteur2VendeurBourse implements IVendeurContratCadre {

	private static int PRIX_DEFAUT = 4500;
	
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCC;
	protected double quantiteVendueCC;
	protected int etapeCC;
	protected double prix_contrat;

	/** Constructeur de classe
	 * @author Noemie
	 */
	public Producteur2VendeurCCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>();
		this.journalCC = new Journal(this.getNom()+" journal CC", this);
	}
	
	/** initialisation
	 * @author Noemie
	 */
	public void initialiser() {
		super.initialiser();
		this.etapeCC = 0;
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	
	/** next
	 * @author Maxime
	 */
	public void next() {
		super.next();
		this.journalCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (Feve f : stock.keySet()) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
			if (stock.get(f)-restantDu(f)>1200 && f != Feve.F_MQ_E) { // au moins 100 tonnes par step pendant 6 mois
				this.journalCC.ajouter("   "+f+" suffisamment en stock pour passer un CC");
				double parStep = this.par_step(f);
				if(parStep > 100) {
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
							this.prix_contrat = contrat.getPrix();
							journalCC.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe");
						}
					} else {
						journalCC.ajouter("pas d'acheteur");
					}
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
		this.journalCC.ajouter("=================================");
		
		for (Feve f : Feve.values()) {
			if (f != Feve.F_HQ_BE) {
				this.stock_variable.get(f).setValeur(this, this.stock.get(f));
			}
		}
		this.tonnes_venduesCC.setValeur(this, this.getNbTonnesVenduesCC());
	}
	
	public void setQuantiteVendueCC(double q) {
		this.quantiteVendueCC = q;
	}
	
	

	/** Retourne la quantité de fèves qui doivent encore être livrées
	 * @author Maxime
	 */
	public double restantDu(Feve f) {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}

	/** prix  
	 * @author Maxime
	 */
	public double prix(Feve f) {
		double res=0;
		List<Double> lesPrix = new LinkedList<Double>();
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				lesPrix.add(c.getPrix());
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			if (c.getProduit().equals(f)) {
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
	
	/** Ajoute le journalCC aux autres journaux
	 * @author Maxime
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalCC);
		return jx;
	}

	/** Indique par un booléen si on a assez de fèves pour passer un contrat cadre
	 * @author Maxime
	 */
	public boolean vend(IProduit produit) {
		if (produit instanceof Feve){
			Feve f = (Feve) produit;
			if (f == Feve.F_MQ_E) {
				return false;
			}
			else {
				return produit.getType().equals("Feve") && stock.get((Feve)produit)-restantDu((Feve)produit)>1200;
		
			}
		}
		return false;
	}
	
	/** Retourne la quantite de feve vendue par contrats cadres
	 * @author Noemie
	 */
	public double getNbTonnesVenduesCC() {
		return quantiteVendueCC;
	}

	/** Pour les contrats qui n'ont pas encore été signés, on évalue si on va produire assez
	 * pour pouvoir tenir ce nouveau contrat en plus des autres
	 * @author Noemie
	 */
	public double par_step(Feve f) {
		double doit_livrer = 0;
		double prod_par_step = this.getHectaresPlantes(f, this.cryptogramme)*0.5/24/2;
		
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			Feve feve_contrat = (Feve) c.getProduit();
			if (f.equals(feve_contrat)) {
				doit_livrer = doit_livrer + c.getQuantiteALivrerAuStep();
			}
		}
		if (doit_livrer < prod_par_step) {
			return prod_par_step - doit_livrer;
		}
		else {
			return 0;
		}
	}
	
	public boolean ajout_contrat_ok(ExemplaireContratCadre contrat) {
		Feve f = (Feve) contrat.getProduit();
		double doit_environ_livrer_par_step = contrat.getEcheancier().getQuantiteTotale()/24;		
		double prod_par_step = this.getHectaresPlantes(f, this.cryptogramme)*0.5/24;
		double doit_deja_livrer = 0;
	
		for (ExemplaireContratCadre  c : this.contratsEnCours) {
			Feve feve_contrat = (Feve) c.getProduit();
			
			if (f.equals(feve_contrat)) {
				doit_deja_livrer = doit_deja_livrer + c.getQuantiteALivrerAuStep();
			}
		}
		
		if (doit_deja_livrer + doit_environ_livrer_par_step < prod_par_step) {
			return true;
		}
		return false;
		
	}
	
	
	/** contre proposition du vendeur
	 * @author Maxime
	 */
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		journalCC.ajouter("      contreProposition("+contrat.getProduit()+" avec echeancier "+contrat.getEcheancier());
		Echeancier ec = contrat.getEcheancier();
		IProduit produit = contrat.getProduit();
		Echeancier res = ec;
		
		boolean acceptable = produit.getType().equals("Feve")
				&& contrat.getEcheancier() != null
				&& ec.getQuantiteTotale()>=1200  // au moins 100 tonnes par step pendant 6 mois
				&& ec.getStepFin()-ec.getStepDebut()>=11   // duree totale d'au moins 12 etapes
				&& ec.getStepDebut()<Filiere.LA_FILIERE.getEtape()+8 // ca doit demarrer dans moins de 4 mois
				&& ec.getQuantiteTotale()<stock.get((Feve)produit)-restantDu((Feve)produit)
				&& prod_step.get((Feve) produit).getValeur() >= ec.getQuantiteTotale()
				&& (Feve)produit != Feve.F_MQ_E
				&& this.ajout_contrat_ok(contrat);
				if (!acceptable) {
					if (!produit.getType().equals("Feve") || stock.get((Feve)produit)-restantDu((Feve)produit)<1200) {
						if (!produit.getType().equals("Feve")) {
							journalCC.ajouter("      ce n'est pas une feve : je retourne null");
						} else {
							journalCC.ajouter("      je n'ai que "+(stock.get((Feve)produit)-restantDu((Feve)produit))+" de disponible (moins de 1200) : je retourne null");
							
						}
						return null;
					}
					if (ec.getQuantiteTotale()<=stock.get((Feve)produit)-restantDu((Feve)produit)) {
						journalCC.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12)));
						return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12));
					} else {
						journalCC.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stock.get((Feve)produit)-restantDu((Feve)produit)/12))));
						return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stock.get((Feve)produit)-restantDu((Feve)produit)/12)));
					}
				}
			
				journalCC.ajouter("      j'accepte l'echeancier");
				return res;
	}


	/** Proposition de prix contrat cadre
	 * @author Maxime
	 */
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("Feve") || ( (Feve) contrat.getProduit()) == Feve.F_MQ_E) {
			return 0; // ne peut pas etre le cas normalement 
		}
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		double cours = ((Feve)(contrat.getProduit())).isEquitable() ? 0.0 : bourse.getCours((Feve)contrat.getProduit()).getValeur();
		double prixCC = prix((Feve)contrat.getProduit());
		if (prixCC==0.0) {
			PRIX_DEFAUT=(int)(PRIX_DEFAUT*0.98); // on enleve 2% tant qu'on n'a pas passe un contrat
		}
		double res = prixCC>cours ? prixCC*1.25 : (cours<PRIX_DEFAUT ? PRIX_DEFAUT : (int)(cours*1.5));
		journalCC.ajouter("      propositionPrix retourne "+res);
		return res;
	}

	/** Contre proposition de prix du vendeur pour un
	 * @author Maxime
	 */
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("Feve")) {
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
		Feve f = (Feve) contrat.getProduit();

		this.contratsEnCours.add(contrat);
	}

	/** Retire les fèves du stock pour les livrer au client
	 * @author Maxime
	 */
	
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		Feve f = (Feve) produit;
		double stockActuel = this.getQuantiteEnStock(f, this.cryptogramme);
		double a_livrer_par_step = contrat.getQuantiteALivrerAuStep();
		if (stockActuel < a_livrer_par_step) {
			int quantite_a_planter = (int) (4*a_livrer_par_step);
			this.planter(quantite_a_planter, f);
			
		}
		double aLivre = Math.min(quantite, stockActuel);
		
		this.stock_a_vendre(f, quantite);
		if (this.getQuantiteEnStock(f, this.cryptogramme) < 10000) {
			double hectares_a_planter = aLivre*2*1.3; // 0.5 tonnes par hectare et 1.3 pour avoir un peu plus
			this.planter(hectares_a_planter, f);
		}
		journalCC.ajouter("   Livraison de "+aLivre+" T de "+produit+" sur "+quantite+" exigees pour contrat "+contrat.getNumero());
		if (this.etapeCC == Filiere.LA_FILIERE.getEtape()) {
			this.quantiteVendueCC = this.quantiteVendueCC + aLivre;
		}
		else {
			this.etapeCC = Filiere.LA_FILIERE.getEtape();
			this.quantiteVendueCC = aLivre;
		}
		return Math.max(aLivre, 0);
	} 
}
