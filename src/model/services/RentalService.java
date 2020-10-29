package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

public class RentalService {
	
	private Double pricePerDay;
	private Double pricePerHour;
	
	private BrazilTaxService taxService;

	public RentalService(Double pricePerDay, Double pricePerHour, BrazilTaxService taxService) {
		this.pricePerDay = pricePerDay;
		this.pricePerHour = pricePerHour;
		this.taxService = taxService;
	}
	
	public void processInvoice(CarRental carRental) {
		//Pega as datas em milesegundos
		long t1 = carRental.getStart().getTime();
		long t2 = carRental.getFinish().getTime();
		
		//recebe o valor em horas no formato double
		//converte os milesgundo em hour milesegundos > segundos > minutos > horas 		
		double hours = (double) (t2 - t1) / 1000 / 60 /60;
		double basicPayment;
		if (hours <= 12.0) {
			//função que arredonada para um numero inteiro pra cima
			basicPayment = Math.ceil(hours) * pricePerHour;
		} else {
			//pegar o valor em dias
			basicPayment = Math.ceil(hours / 24) * pricePerDay;
		}
		
		double tax = taxService.tax(basicPayment);
		
		carRental.setInvoice(new Invoice(basicPayment, tax));
	}	
}
