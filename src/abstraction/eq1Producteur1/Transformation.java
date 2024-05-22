package abstraction.eq1Producteur1;
import abstraction.eqXRomu.general.Variable;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;

import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;

public class Transformation extends Producteur1VendeurAuxEncheres implements IFabricantChocolatDeMarque, IMarqueChocolat{
	protected Journal journalTransfo;
	public HashMap<Chocolat, Variable > stockChoc;
	public HashMap<ChocolatDeMarque, Double> stockChocMarque;
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo;
	protected List<ChocolatDeMarque> MamaChoco;
	protected boolean BeginBQ; 
	protected boolean BeginMQ;
	protected boolean BeginHQ;

	protected HashMap<Gamme, Boolean> beginning;


	public Transformation() {
		super();
		//this.BeginBQ = this.BeginHQ = this.BeginMQ = true;

		this.journalTransfo = new Journal(getNom()+"Transfo", this);
		this.beginning = new HashMap<Gamme, Boolean>();
		for (Gamme g : Gamme.values()) {
			beginning.put(g, true);
		}
		this.stockChoc = new HashMap<Chocolat,Variable>();
		for (Chocolat choc : Chocolat.values()) {
			this.stockChoc.put(choc, new Variable("EQ1"+choc,this, 0));
		}
		this.stockChocMarque = new HashMap<ChocolatDeMarque, Double>();
		//this.stockChocMarque.put(null, null);
		this.MamaChoco = new LinkedList<ChocolatDeMarque>();


	}
	public void initialiser() {
		super.initialiser();
		this.pourcentageTransfo = new HashMap<Feve, HashMap<Chocolat, Double>>();
		this.pourcentageTransfo.put(Feve.F_HQ_BE, new HashMap<Chocolat, Double>());
		double conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_HQ_BE).put(Chocolat.C_HQ_BE, conversion);// la masse de chocolat obtenue est plus importante que la masse de feve vue l'ajout d'autres ingredients
		this.pourcentageTransfo.put(Feve.F_MQ_E, new HashMap<Chocolat, Double>());
		conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_MQ_E).put(Chocolat.C_MQ_E, conversion);
		this.pourcentageTransfo.put(Feve.F_MQ, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_MQ).put(Chocolat.C_MQ, conversion);
		this.pourcentageTransfo.put(Feve.F_BQ, new HashMap<Chocolat, Double>());
		conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_BQ).put(Chocolat.C_BQ, conversion);
		this.MamaChoco = new LinkedList<ChocolatDeMarque>();
		for (Feve f : Feve.values()) {
			if (this.pourcentageTransfo.keySet().contains(f)) {
				Filiere.LA_FILIERE.getMarquesChocolat();
				for (Chocolat c : this.pourcentageTransfo.get(f).keySet()) {
					int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+c.getGamme()).getValeur());
					ChocolatDeMarque cm= new ChocolatDeMarque(c, "AfriChoco", pourcentageCacao);
					this.MamaChoco.add(cm);
					this.stockChocMarque.put(cm, 0.0);

				}
			}
		}
	}


	/**
	 * This checks our current stock to see we have an excess and then decide to start making choclate
	 * 
	 */
	public void checkStock() {
		if (this.Stock_BQ.size() >= this.degrBQ) {
			this.BeginBQ = true;
			int j = this.Stock_MQ.size() - this.degrBQ;
			for (int i = j; i < j + this.degrBQ-1; i++) {
				if (this.Stock_BQ.get(i+1) <= this.Stock_BQ.get(i)) {
					this.BeginBQ = false;
					break; // Exit the loop if any decrease in stock is found
				}
			}
			this.beginning.put(Gamme.BQ, this.BeginBQ);


		}
		if (this.Stock_MQ.size() >= this.degrMQ) {
			this.BeginMQ = true;
			int j = this.Stock_MQ.size()-this.degrMQ;
			for (int i = j; i<this.Stock_MQ.size()-2 ;i++) {
				if (this.Stock_MQ.get(i+1) <= this.Stock_MQ.get(i)) {
					this.BeginMQ = false;
					break; // Exit the loop if any decrease in stock is found
				}
			}
			this.beginning.put(Gamme.MQ, this.BeginMQ);

		}
		if (this.Stock_HQ.size() >= this.degrHQ) {
			this.BeginHQ = true;
			for (int i  =this.Stock_HQ.size()-this.degrHQ; i<this.Stock_HQ.size()-1 ;i++) {
				if (this.Stock_HQ.get(i+1) <= this.Stock_HQ.get(i)) {
					this.BeginHQ = false;
					break; // Exit the loop if any decrease in stock is found
				}
			}
			this.beginning.put(Gamme.HQ,this.BeginHQ );



		}

	}
	public void beginTran() {
		this.checkStock();
		for (Feve f : this.pourcentageTransfo.keySet()) {
			if (this.beginning.get(f.getGamme())) {
				//System.out.println("Begin tran started");

				for (Chocolat c : this.pourcentageTransfo.get(f).keySet()) {
					int transfo =  (int) (0.5*this.stock.get(f).getValeur());//V1 capacite de tranformation illimitee ... (int) (Math.min(this.stockFeves.get(f), Filiere.random.nextDouble()*30));
					if (transfo>0) {
						this.stock.get(f).setValeur(this, this.stock.get(f).getValeur()-transfo);
						//	this.stock.get(f).retirer(this, transfo, this.cryptogramme);
						this.journalTransfo.ajouter("Transfo de "+Journal.entierSur6(transfo)+" T de "+f+" en :"+Journal.doubleSur(transfo*this.pourcentageTransfo.get(f).get(c),3,2)+" T de "+c);

						// La moitie (newChoco) sera stockee sous forme de chocolat, l'autre moitie directement etiquetee "Villors"
						boolean tropDeChoco = this.stock.get(f).getValeur((Integer)cryptogramme)>100000;
						double newChoco = tropDeChoco ? 0.0 : ((transfo*0.5)*this.pourcentageTransfo.get(f).get(c)); // la moitie en chocolat tant qu'on n'en n'a pas trop
						double newChocoMarque = ((transfo*0.5)*this.pourcentageTransfo.get(f).get(c))-newChoco;

						this.stockChoc.get(c).setValeur(this, this.stockChoc.get(c).getValeur()+newChoco);

						//this.journalTransfo.ajouter("En chocolat de marque on a "+ this.stockChocMarque.get(f));

					}
				}
			}
		}	
	}
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			if (p instanceof Feve) {
				return Math.max(stock.get((Feve) p).getValeur(), 0.0);
			} else if (p instanceof Chocolat) {
				return Math.max(stockChoc.get((Chocolat) p).getValeur(), 0.0);
			} else if (p instanceof ChocolatDeMarque) {
				return Math.max(stockChocMarque.get((ChocolatDeMarque) p), 0.0);
			}
		}
		return 0.0;
	}
	public Echeancier contrePropositionDuVendeurtr(ExemplaireContratCadre contrat) {
		Echeancier ec = contrat.getEcheancier();
		IProduit produit = contrat.getProduit();

		Echeancier res = ec;
		String type = produit.getType();
		if (type == "Feve") {
			return super.contrePropositionDuVendeur(contrat);
		}

		boolean acceptable = produit.getType().equals("ChocolatDeMarque")
				&& ec.getQuantiteTotale()>=20  // au moins 100 tonnes par step pendant 6 mois
				&& ec.getStepFin()-ec.getStepDebut()>=11   // duree totale d'au moins 12 etapes
				&& ec.getStepDebut()<Filiere.LA_FILIERE.getEtape()+8 // ca doit demarrer dans moins de 4 mois
				&& ec.getQuantiteTotale()<stockChocMarque.get((ChocolatDeMarque)produit)-restantDutr((ChocolatDeMarque)produit);
				if (!acceptable) {
					if (!produit.getType().equals("ChocolatDeMarque") || stockChocMarque.get((ChocolatDeMarque)produit)-restantDutr((ChocolatDeMarque)produit)<20) {
						if (!produit.getType().equals("ChocolatDeMarque")) {
							journalTransfo.ajouter("      ce n'est pas une feve : je retourne null");
						} else {
							journalTransfo.ajouter("      je n'ai que "+(stockChocMarque.get((ChocolatDeMarque)produit)-restantDutr((ChocolatDeMarque)produit))+" de disponible (moins de 1200) : je retourne null");
						}
						return null;
					}
					if (ec.getQuantiteTotale()<=stockChocMarque.get((ChocolatDeMarque)produit)-restantDutr((ChocolatDeMarque)produit)) {
						journalTransfo.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12)));
						return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12));
					} else {
						journalTransfo.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stockChocMarque.get((ChocolatDeMarque)produit)-restantDutr((ChocolatDeMarque)produit)/12))));
						return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stockChocMarque.get((ChocolatDeMarque)produit)-restantDutr((ChocolatDeMarque)produit)/12)));
					}
				}
				journalTransfo.ajouter("      j'accepte l'echeancier");
				return res;
	}
	private Double restantDutr(IProduit produit) {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(produit)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;

	}
	public void next() {
		super.next();
		this.checkStock();
		this.beginTran();
		this.MoneyBaby();
		//System.out.println(this.BeginBQ);
		//System.out.println(this.BeginMQ);
		//System.out.println(this.BeginHQ);
		for (Chocolat c : Chocolat.values()) {
			this.journalTransfo.ajouter("On a en stock chocolat"+ c+ this.getQuantiteEnStock(c, cryptogramme));
		}
	}

	public List<ChocolatDeMarque> getChocolatsProduits() {
		// TODO Auto-generated method stub
		if (this.MamaChoco.size() == 0) {
			Chocolat cmc = Chocolat.C_MQ_E;
			int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+cmc.getGamme()).getValeur());
			this.MamaChoco.add(new ChocolatDeMarque(cmc, "AfriChoco", pourcentageCacao));
		}
		return this.MamaChoco;
	}

	public List<String> getMarquesChocolat() {
		// TODO Auto-generated method stub
		LinkedList<String> marques = new LinkedList<String>();
		marques.add("AfriChoco");
		return marques;
	}
	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(journalTransfo);
		return res;
	}
	public List<Variable> getIndicateurs(){
		List<Variable> res = super.getIndicateurs();
		res.addAll(this.stockChoc.values());
		//res.addAll(this.stockChocMarque.values());
		return res;
	}


	public double prixtr(ChocolatDeMarque cdm) {
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
	public double livrertr(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		if (!(produit instanceof Feve)) {
			return super.livrer(produit, quantite, contrat);
		}
		else if (produit instanceof ChocolatDeMarque) {
			ChocolatDeMarque cdm = (ChocolatDeMarque) produit;
			if (!cdm.getMarque().equals("AfriChoco")) {
				journalTransfo.ajouter("Tentative de livraison d'un produit non possédé: " + cdm);
				return 0;
			}


			double stockActuel = stockChocMarque.get(produit);
			double aLivre = Math.min(quantite, stockActuel);
			journalTransfo.ajouter("   Livraison de "+aLivre+" T de "+produit+" sur "+quantite+" exigees pour contrat "+contrat.getNumero());
			//stockChocMarque.get((ChocolatDeMarque)produit).setValeur(this, stockActuel-aLivre);
			stockChocMarque.put((ChocolatDeMarque)produit, stockActuel-aLivre);
			//this.totalStocksChocoMarque.setValeur(this, this.totalStocksChocoMarque.getValeur(this.cryptogramme)-aLivre, this.cryptogramme);
			return aLivre;
		}
		else {
			return 0;
		}
	}
	public void notificationNouveauContratCadretr(ExemplaireContratCadre contrat) {
		this.contratsEnCours.add(contrat);
	}
	public void MoneyBaby() {
		for (ChocolatDeMarque c : stockChocMarque.keySet()) {
			if (c.getMarque().equals("AfriChoco")) {


				this.journalTransfo.ajouter("   " + c + " en stock : " + stockChocMarque.get(c) + " ; restanDu : " + restantDutr(c));
				if (stockChocMarque.get(c) - restantDutr(c) > 20) {
					this.journalTransfo.ajouter("   " + c + " suffisamment en stock pour passer un CC");
					double parStep = Math.max(100, (stockChocMarque.get(c) - restantDutr(c)) / 24);
					Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, parStep);
					List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(c);
					if (acheteurs.size() > 0) {
						IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
						journalTransfo.ajouter("   " + acheteur.getNom() + " retenu comme acheteur parmi " + acheteurs.size() + " acheteurs potentiels");
						ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, c, e, cryptogramme, false);
						if (contrat == null) {
							journalTransfo.ajouter(Color.RED, Color.white, "   echec des negociations");
						} else {
							this.contratsEnCours.add(contrat);
							journalTransfo.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe");
						}
					} else {
						journalTransfo.ajouter("   pas d'acheteur");
					}
				}
			}
			for (ExemplaireContratCadre con : this.contratsEnCours) {
				if (con.getQuantiteRestantALivrer() == 0.0) {
					this.contratsTermines.add(con);
				}
			}
			for (ExemplaireContratCadre con : this.contratsTermines) {
				this.contratsEnCours.remove(con);
			}
		}
		
	}
	






}