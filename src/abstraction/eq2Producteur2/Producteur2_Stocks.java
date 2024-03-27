package abstraction.eq2Producteur2;

public class Producteur2_Stocks extends Producteur2Acteur {
	
	private static final int SEUIL = 0; 
	//seuil max de la production stockee (voir ce qu'on fait du reste: vente, poubelle, produit moins, ...)
	//déterminer ce qu'on fait en fonction de comment on est proche du seuil max
	
	//private final int PRIX_STOCK_TONNE = 0; récupérable via la filière
	
	private static final float DELAI_HQ_MQ = 0;
	private static final float DELAI_MQ_BQ = 0;
	
	private int quantite_stockee_hq;
	private int quantite_stockee_mq;
	private int quantite_stockee_bq;
	
	//méthode pour déterminer passage de haute_qualite à moyenne et de moyenne à basse
	//méthode update pour mettre à jour à chaque tour le stock
	
	
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
	
	public static int getSeuil() {
		return SEUIL;
	}
	
	public static float getDelaiHqMq() {
		return DELAI_HQ_MQ;
	}
	
	public static float getDelaiMqBq() {
		return DELAI_MQ_BQ;
	}

}
