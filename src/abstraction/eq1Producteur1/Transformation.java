package abstraction.eq1Producteur1;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
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
					ChocolatDeMarque ca= new ChocolatDeMarque(c, "AfriChoco", pourcentageCacao);
					this.MamaChoco.add(ca);
					this.stockChocMarque.put(ca, 0.0);

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
	public void next() {
		super.next();
		//this.checkStock();
		this.beginTran();
		//System.out.println(this.BeginBQ);
		//System.out.println(this.BeginMQ);
		//System.out.println(this.BeginHQ);
		for (Chocolat c : Chocolat.values()) {
			this.journalTransfo.ajouter("On a en stock chocolat"+ c+ this.getQuantiteEnStock(c, cryptogramme));
		}
	}
	@Override
	public List<ChocolatDeMarque> getChocolatsProduits() {
		// TODO Auto-generated method stub
		if (this.MamaChoco.size() == 0) {
			Chocolat cmc = Chocolat.C_MQ_E;
			int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+cmc.getGamme()).getValeur());
			this.MamaChoco.add(new ChocolatDeMarque(cmc, "AfriChoco", pourcentageCacao));
		}
		return this.MamaChoco;
	}
	@Override
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






}