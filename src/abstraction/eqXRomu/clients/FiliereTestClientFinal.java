package abstraction.eqXRomu.clients;

import java.util.HashMap;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class FiliereTestClientFinal extends Filiere {
	private static final double DISTRIBUTIONS_ANNUELLES[][] = {
			//Jan1 Jan2 Fev1 Fev2 Mar1 Mar2 Avr1 Avr2 Mai1 Mai2 Jui1 Jui2 Jul1 Jul2 Aou1 Aou2 Sep1 Sep2 Oct1 Oct2 Nov1 Nov2 Dec1 Dec2
			{ 4.5, 4.5, 4.5, 4.5, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.5, 4.5, 4.5, 4.5, },			
			{ 5.5, 5.5, 5.0, 5.0, 4.5, 4.0, 4.0, 4.0, 4.0, 3.5, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.5, 4.0, 4.0, 4.5, 5.0, 5.0, 5.5, 5.5, },			
			{ 3.5, 3.5, 6.0, 3.5, 3.5, 3.5, 3.5, 3.5, 9.0, 3.5, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 9.0, 9.0, },			
			{ 3.0, 3.0, 6.0, 3.0, 3.0, 3.0, 3.0, 3.0, 9.0, 3.0, 3.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0,15.0,15.0, },			
			{ 3.0, 3.0, 7.0, 3.0, 3.0, 3.0, 3.0, 3.0,10.0, 3.0, 3.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0,10.0, 3.0, 3.0,11.0,10.0, },			
			{ 3.0, 3.0,10.0, 3.0, 3.0, 3.0, 3.0, 3.0,12.0, 3.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0,15.0,15.0, },			
			{ 3.0, 3.0,11.0, 3.0, 3.0, 3.0, 3.0, 3.0,13.0, 3.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 3.0,10.0, 3.0, 3.0,11.0,10.0, },			
	};

	public FiliereTestClientFinal() {
		super();

	HashMap<Chocolat, Double> repartitionInitiale = new HashMap<Chocolat, Double>();
	repartitionInitiale.put(Chocolat.C_HQ_BE,  10.0); // Haute Qualite  ,  Bio-Equitable 
	repartitionInitiale.put(Chocolat.C_HQ_E,   20.0); // Haute Qualite  ,  Equitable 
	repartitionInitiale.put(Chocolat.C_MQ_E,    5.0); // Moyenne Qualite,  Equitable 
	repartitionInitiale.put(Chocolat.C_MQ,     25.0); // Moyenne Qualite,ni Bio ni Equitable
	repartitionInitiale.put(Chocolat.C_BQ,     40.0); // Basse Qualite  ,ni Bio ni  Equitable

	ClientFinal  cf = new ClientFinal(7200000000.0 , repartitionInitiale, DISTRIBUTIONS_ANNUELLES);

		this.ajouterActeur(cf);
		ChocolatDeMarque[] chocos= {
				new ChocolatDeMarque(Chocolat.C_MQ, "lindt", 50),
				new ChocolatDeMarque(Chocolat.C_BQ, "ivoria", 30),
			    new ChocolatDeMarque(Chocolat.C_MQ_E, "lindt", 50),
				new ChocolatDeMarque(Chocolat.C_BQ, "ivoria", 30),
		};
		this.ajouterActeur(new ExempleTransformateurImproductif(chocos[0]));
		this.ajouterActeur(new ExempleTransformateurImproductif(chocos[1]));
		this.ajouterActeur(new ExempleTransformateurImproductif(chocos[2]));
		this.ajouterActeur(new ExempleTransformateurImproductif(chocos[3]));
		this.ajouterActeur(new Romu());
		ChocolatDeMarque[] chocos1= {
				chocos[0],chocos[1],chocos[2],
		};
		double[] stocks1= {1000000, 1000000, 1000000};
		double[] prix1= {1600, 1300, 2400};
		String[] marques1 = {"lindt"};
		this.ajouterActeur(new ExempleDistributeurChocolatMarque(chocos1, stocks1, 100000, prix1, marques1));

		ChocolatDeMarque[] chocos2= {
				chocos[1],chocos[2],chocos[3],
		};
		double[] stocks2= {1000000, 1000000, 1000000};
		double[] prix2= {1500, 1085, 2300};
		String[] marques2 = {"ivoria"};
		this.ajouterActeur(new ExempleDistributeurChocolatMarque(chocos2, stocks2, 50000, prix2, marques2));
	}
	
	
	public void initialiser() {
		super.initialiser();
		// il est possible de modifier l'attractivite initiale d'un chocolat (impossible d'appeler cette methode plus tard)
		//cf.initAttractiviteChoco(new ChocolatDeMarque(Chocolat.CHOCOLAT_BASSE,"D.Choco1CHOCOLAT_BASSE"), 4.5);
	}

	
	/**
	 * Redefinition afin d'interdire l'acces direct au client final
	 */
	public IActeur getActeur(String nom) {
		if (!nom.startsWith("C.F.")) {
			return super.getActeur(nom); 
		} else {
			return null;
		}
	}
}
