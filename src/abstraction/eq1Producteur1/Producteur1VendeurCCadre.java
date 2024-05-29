package abstraction.eq1Producteur1;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1VendeurCCadre extends Producteur1VendeurBourse implements IVendeurContratCadre {

	protected SuperviseurVentesContratCadre supCC;
	private HashMap<IAcheteurContratCadre, Integer> Acheteurs;
	protected List<ExemplaireContratCadre> contratsEnCours;
	protected List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCoC;
	protected LinkedList<Double> echancesQua;
	protected double moy;

	public Producteur1VendeurCCadre() {
		super();
		this.contratsEnCours = new LinkedList<>();
		this.contratsTermines = new LinkedList<>();
		this.journalCoC = new Journal(this.getNom() + " journal CC", this);
		this.moy = 0;
		this.echancesQua = new LinkedList<Double>();
		this.Acheteurs = new HashMap<IAcheteurContratCadre, Integer>();
	}

	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre) Filiere.LA_FILIERE.getActeur("Sup.CCadre");
	}

	public void next() {
		super.next();
		this.journalCoC.ajouter("=== STEP " + Filiere.LA_FILIERE.getEtape() + " ====================");
		updateContracts();
		proposeNewContracts();
	}

	private void updateContracts() {
		Iterator<ExemplaireContratCadre> it = contratsEnCours.iterator();
		while (it.hasNext()) {
			ExemplaireContratCadre contrat = it.next();
			if (contrat.getQuantiteRestantALivrer() == 0) {
				contratsTermines.add(contrat);
				it.remove();
				journalCoC.ajouter(Color.MAGENTA, Color.WHITE, "Terminated contract: " + contrat.toString());
			}
		}
	}

	private void proposeNewContracts() {
		for (Feve f : stock.keySet()) {
			if (stock.get(f).getValeur() - restantDu(f) > 1200) {
				double parStep = Math.max(100, (stock.get(f).getValeur() - restantDu(f)) / 2);
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, parStep);
				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(f);

				if (acheteurs.size() > 0) {
					IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
					journalCoC.ajouter("Selected buyer: " + acheteur.getNom());
					ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, f, e, cryptogramme, false);
					if (contrat == null) {
						journalCoC.ajouter(Color.RED, Color.white, "Negotiation failed");
					} else {
						this.contratsEnCours.add(contrat);
						journalCoC.ajouter(Color.GREEN, acheteur.getColor(), "Contract signed");
					}
				} else {
					journalCoC.ajouter("No buyer available");
				}
			}
		}
	}

	protected double restantDu(Feve f) {
		double res = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res += c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}

	protected double prix(Feve f) {
		double res = 0;
		int count = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res += c.getPrix();
				count++;
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			if (c.getProduit().equals(f)) {
				res += c.getPrix();
				count++;
			}
		}
		if (count != 0) {
			return res / count;
		}

		Gamme gamme = f.getGamme();
		boolean bio = f.isBio();
		boolean equitable = f.isEquitable();
		double prime = 0;

		if (bio) {
			prime += 100;
		}
		if (equitable) {
			prime += 60;
		}
		if (gamme == Gamme.HQ) {
			prime += 300;
		}
		res += prime + 1472;
		return res;
	}

	@Override
	public boolean vend(IProduit produit) {
		if (produit instanceof Feve) {
			Feve f = (Feve) produit;
			return this.stock.get(f).getValeur() > 10;
		}
		return false;
	}

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		journalCoC.ajouter("Counter proposal for contract: " + contrat.getProduit() + " with schedule " + contrat.getEcheancier());
		Echeancier ec = contrat.getEcheancier();
		//System.out.println(contrat.getAcheteur());
		//System.out.println(Filiere.LA_FILIERE.getActeur("EQ6"));
		//System.out.println(contrat.getAcheteur().equals(Filiere.LA_FILIERE.getActeur("EQ6")));
		if ((contrat.getAcheteur().equals(Filiere.LA_FILIERE.getActeur("EQ6")))){
			return null;
			}
		IProduit produit = contrat.getProduit();
		if (!(produit instanceof Feve)) {
			journalCoC.ajouter("Not a cocoa bean");
			return null;
		}
		this.echancesQua.add(ec.getQuantiteTotale());

		Feve f = (Feve) produit;
		Double stockdispo = stock.get(f).getValeur() - restantDu(f);
		//System.out.println(stock.get(f).getValeur());

		for (int i =0; i < this.echancesQua.size() -1; i++) {
			moy += this.echancesQua.get(i);
		}
		moy/=this.echancesQua.size();
		if (stockdispo < 30000) {
			journalCoC.ajouter("Insufficient stock: " + stockdispo);
			return null;
		}
		if (Acheteurs.keySet().contains(contrat.getAcheteur())) {
			if (Acheteurs.get(contrat.getAcheteur())>=1) {
				return null;
			}
			
		}


		if (ec.getQuantiteTotale()>moy*1.2) {
			return null;
		}
		int duree = ec.getStepFin() - ec.getStepDebut();
		if (duree < 10) {
			journalCoC.ajouter("Duration less than 12 steps");
			return null;
		}
		if (Filiere.LA_FILIERE.getEtape() < 12) {
			journalCoC.ajouter("Contract not allowed in first 12th steps");
			return null;
		}
		if (this.contratsEnCours.size() >= 4) {
			journalCoC.ajouter("Maximum number of ongoing contracts reached");
			return null;
		}
		if (ec.getStepDebut() < Filiere.LA_FILIERE.getEtape() + 8) {
			return ec;
		}

		double totalQuantite = stock.get(f).getValeur() - restantDu(f);
		double perStepQuantite = Math.min(totalQuantite / 12, 100);
		Echeancier newEcheancier = new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, perStepQuantite);
		journalCoC.ajouter("Counter proposal schedule: " + newEcheancier);
		return newEcheancier;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if (!(contrat.getProduit() instanceof Feve)) {
			return 0;
		}
		return prix((Feve) contrat.getProduit());
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		List<Double> prix = contrat.getListePrix();
		if (prix.get(prix.size() - 1) >= 1.1* prix.get(0)) {
			journalCoC.ajouter("Accepting proposed price: " + contrat.getPrix());
			return contrat.getPrix();
		} else {
			double newPrice = prix.get(0) * 1.20;
			journalCoC.ajouter("Counter proposal price: " + newPrice);
			return newPrice;
		}
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {

		journalCoC.ajouter("New contract: " + contrat);
		if (Acheteurs.containsKey(contrat.getAcheteur())) {
			Acheteurs.put(contrat.getAcheteur(), this.Acheteurs.get(contrat.getAcheteur()));
		}
		else {
			Acheteurs.put(contrat.getAcheteur(),1);
		}


		this.contratsEnCours.add(contrat);

	}

	@Override
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		double stockActuel = stock.get(produit).getValeur((Integer) cryptogramme);
		double aLivre = Math.min(quantite, stockActuel);
		journalCoC.ajouter("Delivering " + aLivre + " T of " + produit + " for contract " + contrat.getNumero() + " to " + contrat.getAcheteur());
		stock.get(produit).setValeur(this, stockActuel - aLivre, (Integer) cryptogramme);
		return aLivre;
	}

	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(journalCoC);
		return res;
	}
	public double rest(Feve f) {


		return 0.0;
	}

	public void Meow() {
		System.out.println(this.contratsEnCours);
		System.out.println(this.contratsTermines);
	}
}
