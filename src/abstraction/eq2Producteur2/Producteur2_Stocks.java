package abstraction.eq2Producteur2;
import java.util.ArrayList;
import java.util.List;

public class Producteur2_Stocks extends Producteur2_Lot {
	
	private static final double SEUIL = 0; 
	//seuil max de la production stockee (voir ce qu'on fait du reste: vente, poubelle, produit moins, ...)
	//déterminer ce qu'on fait en fonction de comment on est proche du seuil max
	
	//private final int PRIX_STOCK_TONNE = 0; récupérable via la filière
	
	private static final double DELAI_HQ_MQ = 0;
	private static final double DELAI_MQ_BQ = 0;
	
	private int quantite_stockee_hq;
	private int quantite_stockee_mq;
	private int quantite_stockee_bq;
	
	private List<Producteur2_Lot> stock_total;
	
	//méthode pour déterminer passage de haute_qualite à moyenne et de moyenne à basse
	//méthode update pour mettre à jour à chaque tour le stock
	
	//FILIERE.getEtape() pour avoir le numéro d'étape
	
	public Producteur2_Stocks() {
		super();
		List<Producteur2_Lot> l = new ArrayList<Producteur2_Lot>();
	}
	
	public int getQuantite_stockee_hq() {
		return quantite_stockee_hq;
	}
	
	public void setQuantite_stockee_hq(int quantite_stockee_hq) {
		this.quantite_stockee_hq = quantite_stockee_hq;
	}
	
	public int getQuantite_stockee_mq() {
		return quantite_stockee_mq;
	}
	
	public void setQuantite_stockee_mq(int quantite_stockee_mq) {
		this.quantite_stockee_mq = quantite_stockee_mq;
	}
	
	public int getQuantite_stockee_bq() {
		return quantite_stockee_bq;
	}
	
	public void setQuantite_stockee_bq(int quantite_stockee_bq) {
		this.quantite_stockee_bq = quantite_stockee_bq;
	}
	
	public static double getSeuil() {
		return SEUIL;
	}
	
	public static double getDelaiHqMq() {
		return DELAI_HQ_MQ;
	}
	
	public static double getDelaiMqBq() {
		return DELAI_MQ_BQ;
	}
	
	/*public void action_seuil() {
		if(this.getQuantiteEnStock())
	}*/
	
	public void mise_a_jour(double quantite_rest_BQ, double quantite_rest_MQ, double quantite_rest_MQE, double quantite_rest_HQ, double quantite_rest_HQE, double quantite_rest_HQBE) {
		if(quantite_rest_BQ != 0) {
			
		}
		if(quantite_rest_MQ != 0) {
			
		}
		if(quantite_rest_MQE != 0) {
			
		}
		if(quantite_rest_HQ != 0) {
			
		}
		if(quantite_rest_HQE != 0) {
			
		}
		if(quantite_rest_HQBE != 0) {
			
		}
	}

}
