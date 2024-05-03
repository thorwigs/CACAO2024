package abstraction.eq8Distributeur1;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.contratsCadres.ContratCadre;
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
import abstraction.eqXRomu.produits.IProduit;


/**
 * @author ianis
 */
public class Distributeur1AcheteurContratCadre extends Distributeur1Vendeur implements IAcheteurContratCadre{
	private SuperviseurVentesContratCadre supCC;
	protected List<ExemplaireContratCadre> contrat_en_cours;
	protected List<ExemplaireContratCadre> contrat_term;
	protected Journal journalCC;
	

	/**
	 * @author ianis
	 */
	public Distributeur1AcheteurContratCadre() {
		super();
		this.contrat_en_cours = new LinkedList<ExemplaireContratCadre>();
		this.contrat_term= new LinkedList<ExemplaireContratCadre>();
		this.journalCC= new Journal (this.getNom() + "journal CC", this);
	}
	

	/**
	 *@author ianis
	 */
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}

	/**
	 *@author ianis
	 */
	public String getNom() {
		return (super.getNom());
	} 

	/**
	 *@author ianis
	 */
	public Color getColor() {
		return(super.getColor());
	}

	/**
	 *@author ianis
	 */
	public String getDescription() {
		return(super.getDescription());
	}
	
	/**
	 *@author ianis
	 */
	public List<Variable> getIndicateurs() {
		return(super.getIndicateurs());
	}

	/**
	 *@author ianis
	 */
	public List<Variable> getParametres() {
		return(super.getParametres());
	}

	/**
	 *@author ianis
	 */
	public List<Journal> getJournaux() {
		List<Journal> jour = super.getJournaux();
		jour.add(journalCC);
		return jour;
	}

	/**
	 *@author ianis
	 */
	public void setCryptogramme(Integer crypto) {
		super.setCryptogramme(crypto);	
	}

	/**
	 *@author ianis
	 */
	public void notificationFaillite(IActeur acteur) {
		super.notificationFaillite(acteur);
		
	}


	/**
	 *@author ianis
	 */
	public void notificationOperationBancaire(double montant) {
		super.notificationOperationBancaire(montant);
	}


	/**
	 *@author ianis
	 */
	public List<String> getNomsFilieresProposees() {
		return(super.getNomsFilieresProposees());
	}

	/**
	 *@author ianis
	 */
	public Filiere getFiliere(String nom) {
		return(super.getFiliere(nom));
	}

	/**
	 *@author ianis
	 */
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		return(super.getQuantiteEnStock(p, cryptogramme));
	}

	/**
	 *@author ianis
	 */
	public boolean achete(IProduit produit) {
		double a = 0 ; 
		for (int i=0; i<contrat_en_cours.size(); i++) {
			if (contrat_en_cours.get(i).getProduit().equals(produit)) {
				a = a + contrat_en_cours.get(i).getQuantiteRestantALivrer();
			}
		}
		return (produit.getType().equals("ChocolatDeMarque")
				&& this.stock_Choco.containsKey(produit)
				&& !this.chocoBan.contains(produit)
				&& 1000 < this.prevision(produit, 24) - this.stock_Choco.get(produit) - a );   
	}

	/**
	 *@author ianis
	 */
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")
			|| !this.stock_Choco.containsKey(contrat.getProduit())
			|| !this.achete(contrat.getProduit())
			|| this.chocoBan.contains(produit)
			|| contrat.getListePrix().size()>10) {
			return null;
		}
		
		Echeancier x = contrat.getEcheancier();
		if (x.getNbEcheances()>=24 && x.getNbEcheances()<=72 
			&&	contrat.getQuantiteTotale()>= 30 ) {
			
			if (contrat.getProduit().toString().contains("C_BQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*40)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))) {
			} 
			if (contrat.getProduit().toString().contains("C_MQ_E")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))) {
				}
			if ( contrat.getProduit().toString().contains("C_MQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())))  {
				}
			if (contrat.getProduit().toString().contains("C_HQ_BE")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*5)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))) {
				}
			if (contrat.getProduit().toString().contains("C_HQ_E")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*10)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))) {
				}
			if (contrat.getProduit().toString().contains("C_HQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))) {
				} 
			
		} else {
			int a = Filiere.LA_FILIERE.getEtape()+1;
			int b = 24 ; 
			double c = this.prevision(contrat.getProduit(), b) ;	
			double d = 0 ; 
			for (int i=0; i<contrat_en_cours.size(); i++) {
				if (contrat_en_cours.get(i).getProduit().equals(contrat.getProduit())) {
					d = d + contrat_en_cours.get(i).getQuantiteRestantALivrer();
				}
			}
			double e = this.stock_Choco.get(contrat.getProduit()); 
			double f = (c-d-e)/(b*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()));
			
			if (contrat.getQuantiteTotale() > c-d-e+100
				&& f-100*(1+contrat.getListePrix().size())>0
				&& f+100*(1+contrat.getListePrix().size())>0) {
			    x = new Echeancier (a,b,f+100*(1+contrat.getListePrix().size()));
			} else {
			    x = new Echeancier (a,b,f-100*(1+contrat.getListePrix().size()));
			}
		}
		return x;
	}
	
	
	/**
	 *@author ianis
	 */
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")
			|| this.chocoBan.contains(produit)) {
			return 0.0; 
		}
		
		if (contrat.getPrix() <= this.prix_a_perte(contrat.getProduit(),contrat.getPrix())*0.80) {
				return contrat.getPrix();
		}
		
		else {
			return this.prix_a_perte(contrat.getProduit(),contrat.getPrix())*(0.80+(0.15*contrat.getListePrix().size())/supCC.MAX_PRIX_NEGO);
		}
	}

	
	/**
	 *@author ianis
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.contrat_en_cours.add(contrat);
		this.journalCC.ajouter("Nouveau contrat cadre :"+contrat);
	}

	/**
	 *@author ianis
	 */
	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		this.journalCC.ajouter("Reception de "+quantiteEnTonnes+" T de "+p+" du contrat "+contrat.getNumero());
		stock_Choco.put((ChocolatDeMarque)p, stock_Choco.get((ChocolatDeMarque)p)+quantiteEnTonnes);
		this.totalStockChoco.ajouter(this, quantiteEnTonnes,cryptogramme);
	}
	
	
	/**
	 * @author ianis
	 */
	public double prix_a_perte(IProduit p, Double prix) {
		return this.prix((ChocolatDeMarque)p) - ((0.05*prix + this.Cout_Fixe())/this.totalStockChoco.getValeur(cryptogramme));
	}
	
	/**
	 * @author ianis
	 */
	public double prevision (IProduit p,int b) {
		double d = 0.0;
		int a = Filiere.LA_FILIERE.getEtape();
		for (int i =a-b; i<a ; i++ ) {
			d = d + Filiere.LA_FILIERE.getVentes((ChocolatDeMarque)p,i);
		}
		d = d * (1+(Filiere.LA_FILIERE.getIndicateur("C.F. delta annuel max conso").getValeur() + Filiere.LA_FILIERE.getIndicateur("C.F. delta annuel min conso").getValeur())/2);
		return d ; 
	}
	
	/**
	 * @author ianis
	 */
	public void next() {
		super.next();
		this.journalCC.ajouter("");
		this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"==================== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");

		for (ExemplaireContratCadre contrat : contrat_en_cours) {
			if (contrat.getMontantRestantARegler()==0 && contrat.getQuantiteRestantALivrer()==0) {
				contrat_term.add(contrat);
			} else {
				stock_Choco.put((ChocolatDeMarque)contrat.getProduit(),contrat.getQuantiteALivrerAuStep() );
				totalStockChoco.ajouter(this, contrat.getQuantiteALivrerAuStep(), cryptogramme);
			}
		}
		
		for(ExemplaireContratCadre contrat : contrat_term) {
			contrat_en_cours.remove(contrat);
		}
		
		
		for (ChocolatDeMarque choc : chocolats) {
			if (this.achete(choc)) {
				this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"Recherche d'un vendeur aupres de qui acheter");
				List<IVendeurContratCadre> vendeurs = supCC.getVendeurs(choc);
				if (vendeurs.contains(this)) {
					vendeurs.remove(this);
				}
				
				IVendeurContratCadre vendeur = null;		
				if (vendeurs.size()==1) {
					vendeur=vendeurs.get(0);
				} else if (vendeurs.size()>1) {
					vendeur = vendeurs.get((int)( Filiere.random.nextDouble()*vendeurs.size())); // a améliorer dans la V2
				}
				
				if (vendeur!=null) {
					this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"Demande au superviseur de debuter les negociations pour un contrat cadre de "+choc+" avec le vendeur "+vendeur);
					int a = Filiere.LA_FILIERE.getEtape()+1;
					int b = 24 ; 
					double c = this.prevision(choc, b) ;	
					double d = 0 ; 
					for (int i=0; i<contrat_en_cours.size(); i++) {
						if (contrat_en_cours.get(i).getProduit().equals(choc)) {
							d = d + contrat_en_cours.get(i).getQuantiteRestantALivrer();
						}
					}
					double e = this.stock_Choco.get(choc); 
					double f = (c-d-e)/(b*this.nombreMarquesParType.get(choc.getChocolat()));
				    Echeancier x = new Echeancier (a,b,f+1000);
					ExemplaireContratCadre cc = supCC.demandeAcheteur((IAcheteurContratCadre)this, vendeur, choc, x, cryptogramme,false);
					this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"-->aboutit au contrat "+cc);
					if (cc != null ) {
						Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), cryptogramme, "Coût Livraison", 0.05*cc.getPrix());
					}
				}	
			}
		}
		this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"=================================");
		this.journalCC.ajouter("");
		
		for (ChocolatDeMarque choc : chocolats) {
			System.out.println();
		}

	}
}
