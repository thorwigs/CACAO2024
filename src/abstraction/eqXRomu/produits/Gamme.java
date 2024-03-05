package abstraction.eqXRomu.produits;

public enum Gamme {
	BQ, // BASSE QUALITE
	MQ, // MOYENNE QUALITE
	HQ; // HAUTE QUALITE
	
	public boolean greaterThan(Gamme g) {
		return this==HQ || (g==BQ) || this==g;
	}
}
