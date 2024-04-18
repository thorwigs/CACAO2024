package abstraction.eq2Producteur2;
import abstraction.eqXRomu.produits.*;
import abstraction.eqXRomu.filiere.*;


/** Classe permettant de créer des lots
 * @author Quentin
 */

public class Producteur2_Lot {
	
	private int etape;
	private double quantite;
	private Feve type_feve;
	
	/** Constructeur de classe
	 * @author Quentin
	 */
	public Producteur2_Lot(double quantite, Feve type_feve) {
		this.etape = Filiere.LA_FILIERE.getEtape();
		this.quantite = quantite;
		this.type_feve = type_feve;
	}
	
	/** Constructeur de classe pour l'étape 0
	 * @author Quentin
	 */
	public Producteur2_Lot(double quantite, Feve type_feve, int etape) {
		this.etape = etape;
		this.quantite = quantite;
		this.type_feve = type_feve;
	}
	

	/** Getter
	 * @author Quentin
	 */
	public int getEtape() {
		return etape;
	}

	/** Setter
	 * @author Quentin
	 */
	public void setEtape(int etape) {
		this.etape = etape;
	}
	

	/** Getter
	 * @author Quentin
	 */
	public double getQuantite() {
		return quantite;
	}

	/** Setter
	 * @author Quentin
	 */
	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}


	/** Getter
	 * @author Quentin
	 */
	public Feve getType_feve() {
		return type_feve;
	}

	/** Setter
	 * @author Quentin
	 */
	public void setType_feve(Feve type_feve) {
		this.type_feve = type_feve;
	}	
}


