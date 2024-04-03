package abstraction.eq1Producteur1;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;

import java.util.ArrayList;
import java.util.HashMap;




//J'ai ajoute ca car pourqoui pas? Ca nous aidera a remplir les offres des de
public class Producteur1VendeurAppelIDOffre extends Producteur1VendeurCCadre implements IVendeurAO{

	//youssef ben abdeljelil//
	//prix minimal pour chaque type de fève 
	private double prix_seuil_BQ;
	private double prix_seuil_MQ;
	private double prix_seuil_MQ_E;
	private double prix_seuil_HQ;
	private double prix_seuil_HQ_BE;
	private double prix_seuil_HQ_E;
	
	private double seuil_minimAO;
	private double seuil_maximAO;
	public HashMap <Feve,Double> prix_defaut_feve;
	
	protected ArrayList<Feve> a_ne_pas_vendre=new ArrayList<Feve>();//feve a ne pas vendre pour les AO
	protected ArrayList<IAcheteurAO> black_list_Achteur_AO=new ArrayList<IAcheteurAO>();//black list pour les acheteurs
	protected HashMap <IAcheteurAO,Double> score_to_black_list;
	
	public static int score_seuil_blacklist=10;//score a partir duquel un achteur est ajouté au blacklist
	private Journal journalAO;
	private SuperviseurVentesAO supAO;
	
	public void initialiser() {
		super.initialiser();
		prix_defaut_feve=new HashMap<Feve, Double>();
		prix_defaut_feve.put(Feve.F_BQ,2217.0);
		prix_defaut_feve.put(Feve.F_MQ,2417.0 );
		prix_defaut_feve.put(Feve.F_HQ,3000.0);
		prix_defaut_feve.put(Feve.F_MQ_E,2600.0 );
		prix_defaut_feve.put(Feve.F_HQ_E,3200.0 );
		prix_defaut_feve.put(Feve.F_HQ_BE,3400.0 );
		this.supAO = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));


	}
	
	
	public Producteur1VendeurAppelIDOffre() {
	super();
	this.journalAO = new Journal(this.getNom()+" journal appel d'offre  ", this);

	}
	
	
	
	public OffreVente proposerVente(AppelDOffre offre) {

		
		// TODO Auto-generated method stub
	     IAcheteurAO acheteur_AO=offre.getAcheteur();
	     IProduit produit_AO=offre.getProduit();
	     double quantite_AO=offre.getQuantiteT();
	     double quantite_stock = this.getQuantiteEnStock( produit_AO , cryptogramme);
	     
	     //mettre a jour la black_list
	     if (score_to_black_list.containsKey(acheteur_AO)==false) {
				
	    	 score_to_black_list.put(acheteur_AO, 0.0);
		}
	     
	     
		if (black_list_Achteur_AO.contains(acheteur_AO)) {
			
			journalAO.ajouter(Filiere.LA_FILIERE.getEtape()+" Appel d'offre impossible, l'acheteur "+acheteur_AO.getNom()+" est dans la liste noire ");
			return null;
			
		}
		
		else if(quantite_stock<=quantite_AO) {
			journalAO.ajouter(Filiere.LA_FILIERE.getEtape()+" Appel d'offre impossible, quantite_stock<=quantite_AO");
			return null;
		}
		
		else if(a_ne_pas_vendre.contains(produit_AO)) {
			journalAO.ajouter(Filiere.LA_FILIERE.getEtape()+" Appel d'offre impossible, le produit "+produit_AO+" n'est pas à vendre");

			return null;
			
		}
		else if (quantite_AO<seuil_minimAO) {
			journalAO.ajouter(Filiere.LA_FILIERE.getEtape()+" Appel d'offre impossible, quantite_stock<=quantite_AO");

			return null;
		}
		else if(quantite_AO>seuil_maximAO) {
			journalAO.ajouter(Filiere.LA_FILIERE.getEtape()+" Appel d'offre impossible, quantite_stock<=quantite_AO");

			return null;
			
		} 
		else {
			OffreVente offre_finale=new OffreVente(offre, null, produit_AO, prix_defaut_feve.get(produit_AO));
			return offre_finale;
			
			
		}
		
		
	}

	@Override
	public void notifierVenteAO(OffreVente propositionRetenue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee) {
		// TODO Auto-generated method stub
		
	}
	

}
