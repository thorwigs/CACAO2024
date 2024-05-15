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
	protected  int test;
	protected List<ExemplaireContratCadre> choix;
	

	/**
	 * @author ianis
	 */
	public Distributeur1AcheteurContratCadre() {
		super();
		this.contrat_en_cours = new LinkedList<ExemplaireContratCadre>();
		this.contrat_term= new LinkedList<ExemplaireContratCadre>();
		this.journalCC= new Journal (this.getNom() + "journal CC", this);
		this.test=0;
		this.choix=new LinkedList<ExemplaireContratCadre>();
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
	//			&& !this.chocoBan.contains(produit)
				&& 1000 < this.prevision(produit, 24) - this.stock_Choco.get(produit) - a );   
	}

	/**
	 *@author ianis
	 */
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")
			|| !this.stock_Choco.containsKey(contrat.getProduit())
			|| !this.achete(contrat.getProduit())
//			|| this.chocoBan.contains(produit)
			|| contrat.getListePrix().size()>10) {
			return null;
		}
		

		Echeancier x = contrat.getEcheancier();
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
		if (b>=24 && b<=72) {
			
			if (contrat.getProduit().toString().contains("C_BQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*40)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))
				&& contrat.getQuantiteTotale() > c-d-e+100) {
			} 
			if (contrat.getProduit().toString().contains("C_MQ_E")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))
				&& contrat.getQuantiteTotale() > c-d-e+100) {
				}
			if ( contrat.getProduit().toString().contains("C_MQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))
				&& contrat.getQuantiteTotale() > c-d-e+100)  {
				}
			if (contrat.getProduit().toString().contains("C_HQ_BE")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*5)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))
				&& contrat.getQuantiteTotale() > c-d-e+100) {
				}
			if (contrat.getProduit().toString().contains("C_HQ_E")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*10)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))
				&& contrat.getQuantiteTotale() > c-d-e+100) {
				}
			if (contrat.getProduit().toString().contains("C_HQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100*this.nombreMarquesParType.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()))
				&& contrat.getQuantiteTotale() > c-d-e+100) {
				} 			
		} else {
			b = 24;
			if (contrat.getQuantiteTotale() > c-d-e+100) {
			    x = new Echeancier (a,b,f+100*(1+contrat.getListePrix().size()));
			} else if (f-100*(1+contrat.getListePrix().size())>0){
			    x = new Echeancier (a,b,f-100*(1+contrat.getListePrix().size()));
			} else {
			    x = new Echeancier (a,b,f);
			}
		}
		return x;
	}
	
	
	/**
	 *@author ianis
	 */
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
//			|| this.chocoBan.contains(produit)) {
			return 0.0; 
		}

		if (this.test==0) {
			this.choix.add(contrat);
			return 0.0;
		}
		if (contrat.getPrix() <= this.prix_a_perte(contrat.getProduit(),contrat.getPrix())*0.80 && this.test == 1) {
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
	
	public ExemplaireContratCadre ChoisirCC(List<ExemplaireContratCadre> liste) {
		double prix = liste.get(0).getPrix();
		int choix = 0;
		for (int i=1; i<liste.size();i++) {
			if (liste.get(i).getPrix()<prix) {
				choix = i;
				prix = liste.get(i).getPrix();
			}
		}
		return liste.get(choix);
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
				this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"Recherche d'un vendeur aupres de qui acheter pour le chocolat : "+choc);
				List<IVendeurContratCadre> vendeurs = supCC.getVendeurs(choc);
				if (vendeurs.contains(this)) {
					vendeurs.remove(this);
				}
				
				IVendeurContratCadre vendeur = null;		
				if (vendeurs.size()==1) {
					vendeur=vendeurs.get(0);
					this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"Debut des négotatiations avec l'unique vendeur : "+ vendeur+ "pour le chocolat : "+choc);
				} else if (vendeurs.size()>1) {
					this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"Essaie de tous les contrats cadre possible");
					for (int i=0;i<vendeurs.size();i++) {
						this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"Demande au superviseur de debuter les negociations pour un contrat cadre de "+choc+" avec le vendeur "+vendeur);
						this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"C'est le "+i+"ème vendeur");
						int a = Filiere.LA_FILIERE.getEtape()+1;
						int b = 24 ; 
						double c = this.prevision(choc, b) ;	
						double d = 0 ; 
						for (int j=0; j<contrat_en_cours.size(); j++) {
							if (contrat_en_cours.get(j).getProduit().equals(choc)) {
								d = d + contrat_en_cours.get(j).getQuantiteRestantALivrer();
							}
						}
						double e = this.stock_Choco.get(choc); 
						double f = (c-d-e)/(b*this.nombreMarquesParType.get(choc.getChocolat()));
					    Echeancier x = new Echeancier (a,b,f+1000);
						supCC.demandeAcheteur((IAcheteurContratCadre)this, vendeur, choc, x, cryptogramme,false);
					}
					
					ExemplaireContratCadre cc = this.ChoisirCC(this.choix);
					vendeur = cc.getVendeur();				
				}
				
				if (vendeur!=null) {
					this.test=1;
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
						double quantiteFidele = super.Fidele.get(cc.getVendeur().getNom()) + cc.getQuantiteTotale();
						super.Fidele.replace(cc.getVendeur().getNom(), quantiteFidele);
						Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), cryptogramme, "Coût Livraison", 0.05*cc.getPrix());
						this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"l'accord du contrat cadre est : Quantitée totale de "+cc.getQuantiteTotale()+" pour un prix par Step de "+cc.getPrix() );
						this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"" );	
					} 
				}
				this.choix=new LinkedList<ExemplaireContratCadre>();
				this.test=0;
			}
		}
		this.journalCC.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LPURPLE,"=================================");
		this.journalCC.ajouter("");

	}
}
