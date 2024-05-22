package abstraction.eq1Producteur1;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Producteur1VendeurAppelIDOffre extends Producteur1VendeurCCadre implements IVendeurAO{




	/**@author Youssef Ben Abdeljelil */
	//prix minimal pour chaque type de fève 
	
	private double seuil_minimAO=100;	// quantité minimale à vendre pour un appel d'offre, cette 
	//quantité est à négliger si les revenus dépassent 
	//celle de la bourse
	private double seuil_maximAO=100000;	//quantité maximale à vendre 0par les appels d'offre , cette quantité 
	//peut etre depassée si les revenus dépassent 
	//ceux de la bourse
	public HashMap <Feve,Double> prix_defaut_feve;//dictionnaire pour chaque feve et son prix

	protected ArrayList<Feve> a_ne_pas_vendre=new ArrayList<Feve>();//feve a ne pas vendre pour les AO
	protected ArrayList<IAcheteurAO> black_list_Acheteur_AO=new ArrayList<IAcheteurAO>();//black list pour les acheteurs
	protected HashMap <IAcheteurAO,Double> score_to_black_list; //dictionnaire pour chaque transfo et son score dans la liste noire

	public static int score_seuil_blacklist=1000;//score a partir duquel un achteur est ajouté au blacklist
	private Journal journalAO;
	private SuperviseurVentesAO supAO;


	public Producteur1VendeurAppelIDOffre() {
		super();
		this.journalAO = new Journal(this.getNom()+" journal appel d'offre  ", this);


	}

	public void initialiser() {
		super.initialiser();
		prix_defaut_feve=new HashMap<Feve, Double>();
		prix_defaut_feve.put(Feve.F_BQ,2217.0);
		prix_defaut_feve.put(Feve.F_MQ,2417.0 );
		prix_defaut_feve.put(Feve.F_HQ,3000.0);
		prix_defaut_feve.put(Feve.F_MQ_E,2600.0 );
		prix_defaut_feve.put(Feve.F_HQ_E,3200.0 );
		prix_defaut_feve.put(Feve.F_HQ_BE,3400.0 );
		//initilaiser les prix, ceci peut varier
		this.supAO = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
		score_to_black_list=new HashMap<IAcheteurAO, Double>();	// initialiser un dictionnaire vide pour 
		//les acteurs(transfo) 
		//et leurs scores dans la liste noire
	}


	/**
	 *@author youssef ben abdeljelil*/
	public double revenus_bourse_seuil_AO(AppelDOffre offre,BourseCacao bourse) {


		return bourse.getCours((Feve)(offre.getProduit())).getMax()*offre.getQuantiteT();
		//évaluer les revenus si on vendait ces quantités en bourse
	}

	public double revenues_AO_prix_defaut(AppelDOffre offre) 
	{ 
		double prix=prix_defaut_feve.get(offre.getProduit());
		return offre.getQuantiteT()*prix;
		//evaluer les revenus si on vendait par appel d'offre
		//le choix szra fait entre la vente en bourse ou la vente 
		//par appel d'offre selon les revenus de chaque méthode
	}

	/**
	 * Méthode pour proposer une vente en réponse à un appel d'offre.
	 * @param offre L'appel d'offre auquel répondre.
	 * @return L'offre de vente proposée.
	 */
	public OffreVente proposerVente(AppelDOffre offre) {
		

		// TODO Auto-generated method stub
		IAcheteurAO acheteur_AO=offre.getAcheteur();
		IProduit produit_AO=offre.getProduit();


		if (!(produit_AO instanceof Feve)) 
		{
			return null;



		}



		Feve feve_AO = (Feve)produit_AO;


		if (!(prix_defaut_feve.keySet().contains(feve_AO))) {
			journalAO.ajouter(" test 2");


			return null;
		}

		double quantite_AO=offre.getQuantiteT();
		double quantite_stock = this.getQuantiteEnStock( feve_AO , cryptogramme);

		//mettre a jour la black_list


		//revenus AO pour n'importe quelles quantités et qualités >revenues
		// bourse pour les memes quanites qualites
		if(quantite_stock<=quantite_AO) {

			journalAO.ajouter(" Appel d'offre impossible, le produit "+feve_AO+" est en rupture de stock");
			return null;
		}

		// else if (revenus_bourse_seuil_AO(offre,bourse )< revenues_AO_prix_defaut(offre)){
		//	System.out.println("helloworld");
		// 	return new OffreVente(offre, this, offre.getProduit(), prix_defaut_feve.get(feve_AO));	
		//}

		else if (black_list_Acheteur_AO.contains(acheteur_AO)) {

			journalAO.ajouter(" Appel d'offre impossible, l'acheteur "+acheteur_AO.getNom()+" est dans la liste noire ");
			return null;
		}

		//si feve figure dans la liste a ne pas vendre en par AOs
		else if(a_ne_pas_vendre.contains(feve_AO)) {
			journalAO.ajouter(" Appel d'offre impossible, le produit "+feve_AO+" n'est pas à vendre en appel d'offre");

			return null;

		}





		else  if (quantite_AO<seuil_minimAO || quantite_AO>seuil_maximAO) {
			if(quantite_AO<seuil_minimAO ) {
				journalAO.ajouter(" Appel d'offre impossible,  Quantités demandés du produit( "+feve_AO+") <" +seuil_minimAO);
			}
			else if(quantite_AO>seuil_maximAO ) 
			{
				journalAO.ajouter(" Appel d'offre impossible,  Quantités demandés du produit( "+feve_AO+") >" +seuil_maximAO);
			}
			return null;
		}



		/**
		 *@author youssef ben abdeljelil*/

		//on va ajouter une strategie pour savoir si on doit porcéder à une vente par appel d'offre 
		//ou non qui dépend des prix en bourse ,les seuils sur les quanites min et max peuvent 
		//changer si le revenu est égal ou supérieur à celui qu'on a pu génerer en bourse pour les quanites 
		//seuil min et max





		else {



			return new OffreVente(offre, this, offre.getProduit(),this.prix_defaut_feve.get(offre.getProduit()));
		}






	}
	public void next() {
		super.next();
		this.journalAO.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");

	}
	/**
	 * Méthode appelée lorsqu'une vente par appel d'offre est acceptée.
	 * @param propositionRetenue L'offre de vente retenue.
	 */
	@Override
	public void notifierVenteAO(OffreVente propositionRetenue) {
		// TODO Auto-generated method stub
		Feve feve_AO = (Feve) propositionRetenue.getOffre().getProduit();
		double à_retirer =Math.min(this.stock.get(feve_AO).getValeur(), propositionRetenue.getOffre().getQuantiteT());

		this.stock.get(feve_AO).retirer(this, à_retirer, cryptogramme);
		journalAO.ajouter(Color.GREEN,propositionRetenue.getOffre().getAcheteur().getColor()," On vend "+à_retirer+" T de "+feve_AO +" à "+propositionRetenue.getOffre().getAcheteur());
		double prix= prix_defaut_feve.get(feve_AO);
		super.notificationOperationBancaire(à_retirer*prix);
		super.getSolde();

		IAcheteurAO acheteur_AO=propositionRetenue.getOffre().getAcheteur();
		if (!(score_to_black_list.containsKey(acheteur_AO))) {

			score_to_black_list.put(acheteur_AO, 0.0);
		}
		IProduit produit_AO=propositionRetenue.getOffre().getProduit();

		//ajouter un -1 au score de la blacklist pour cet acheteur pour avoir refusé l'offre
		mise_a_jour_black_list(acheteur_AO, produit_AO,"notifierVenteAO");
		/**
		 *@author youssef ben abdeljelil*/




	}
	/**
	 * Méthode appelée lorsqu'une proposition de vente par appel d'offre est refusée.
	 * @param propositionRefusee L'offre de vente refusée.
	 */
	@Override
	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee) {
		// TODO Auto-generated method stub


		Feve feve_AO = (Feve) propositionRefusee.getOffre().getProduit();
		double prix_AO  = propositionRefusee.getPrixT();
		double quantite_AO = propositionRefusee.getQuantiteT();
		IAcheteurAO acheteur_AO=propositionRefusee.getOffre().getAcheteur();
		IProduit produit_AO=propositionRefusee.getOffre().getProduit();

		journalAO.ajouter(Color.RED,Color.white,"   Echec de Vente"+" par AO de "+quantite_AO+" T de "+feve_AO+" au prix de  "+prix_AO+" " );
		if (!(score_to_black_list.containsKey(acheteur_AO))) {

			score_to_black_list.put(acheteur_AO, 0.0);
		}
		//ajouter un +1 au score de la blacklist pour cet acheteur pour avoir refusé l'offre
		mise_a_jour_black_list(acheteur_AO, produit_AO, "notifierPropositionNonRetenueAO");



		/**
		 *@author youssef ben abdeljelil*/

	}
	/**
	 * Met à jour la liste noire des acheteurs en fonction des réponses aux appels d'offre.
	 * @param acheteur_AO L'acheteur concerné.
	 * @param produit_AO Le produit de l'appel d'offre.
	 * @param context Le contexte (acceptation ou refus) de la proposition.
	 */
	public void mise_a_jour_black_list(IAcheteurAO acheteur_AO,IProduit produit_AO ,String context) {// mettre a jour la lise norie selon que l'offre est acceptée ou refusée:context

		if (context.equals("notifierPropositionNonRetenueAO")) //si l'offre est refusée
		{
			if (score_to_black_list.containsKey(acheteur_AO)
					&&(produit_AO instanceof Feve)) {


				double score_actuel = score_to_black_list.get(acheteur_AO);
				score_to_black_list.put(acheteur_AO, score_actuel + 1);

			}


		}
		else if (context.equals("notifierVenteAO")) { // sinon si l'offre est acceptée
			if (score_to_black_list.containsKey(acheteur_AO)
					&&(produit_AO instanceof Feve)) {
				double score_actuel = score_to_black_list.get(acheteur_AO);
				score_to_black_list.put(acheteur_AO, score_actuel - 1);

			}
		}





		if ((score_to_black_list.get(acheteur_AO)<score_seuil_blacklist)&& (black_list_Acheteur_AO.contains(acheteur_AO))) {

			black_list_Acheteur_AO.remove(acheteur_AO);


		}
		if ((score_to_black_list.get(acheteur_AO)>score_seuil_blacklist)
				&& !(black_list_Acheteur_AO.contains(acheteur_AO))) {

			black_list_Acheteur_AO.add(acheteur_AO);

			/**
			 *@author youssef ben abdeljelil*/
		}
	}





	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}


}
