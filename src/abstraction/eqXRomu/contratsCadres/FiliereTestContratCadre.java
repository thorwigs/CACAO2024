package abstraction.eqXRomu.contratsCadres;

import java.util.HashMap;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.*;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Feve;


public class FiliereTestContratCadre extends Filiere {
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

	private SuperviseurVentesContratCadre superviseurCC;

	public FiliereTestContratCadre() {
		super();
		HashMap<Chocolat, Double> repartitionInitiale = new HashMap<Chocolat, Double>();
		repartitionInitiale.put(Chocolat.C_HQ_BE,  10.0); // Haute Qualite   Bio-Equitable   
		repartitionInitiale.put(Chocolat.C_HQ_E,   20.0); // Haute Qualite   Equitable   
		repartitionInitiale.put(Chocolat.C_MQ_E,    5.0); // Moyenne Qualite Equitable   
		repartitionInitiale.put(Chocolat.C_MQ,     25.0); // Moyenne Qualite ni Bio ni Equitable 
		repartitionInitiale.put(Chocolat.C_BQ,     40.0); // Basse Qualite   ni Bio ni Equitable 

		ClientFinal  cf = new ClientFinal(7200000.0 , repartitionInitiale, DISTRIBUTIONS_ANNUELLES);

		this.ajouterActeur(cf);
		this.ajouterActeur(new ExempleTransformateurContratCadreVendeurAcheteur(Feve.F_MQ));
		this.ajouterActeur(new ExempleTransformateurContratCadreVendeurAcheteur(Feve.F_MQ));
		this.ajouterActeur(new ExempleTransformateurContratCadreVendeurAcheteur(Feve.F_MQ));
		this.ajouterActeur(new Romu());
		this.superviseurCC=new SuperviseurVentesContratCadre();
		this.ajouterActeur(this.superviseurCC);

	}
}
