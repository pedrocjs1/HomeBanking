package com.MindHub.HomeBanking;

import com.MindHub.HomeBanking.models.*;
import com.MindHub.HomeBanking.repositories.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class HomeBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {
			// save a couple of customers
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			Client Pedro = new Client("Pedro", "Vega", "pedro@mindhub.com");

			clientRepository.save(melba);
			clientRepository.save(Pedro);

			Account account001 = new Account("VIN001", LocalDateTime.now(), 5000.00);
			Account account002 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00);

			melba.addAccount(account001);
			melba.addAccount(account002);

			accountRepository.save(account001);
			accountRepository.save(account002);


			Account account003 = new Account("VIN001", LocalDateTime.now(), 65000);
			Account account004 = new Account("VIN002", LocalDateTime.now().plusDays(1), 75400);

			Pedro.addAccount(account003);
			Pedro.addAccount(account004);

			accountRepository.save(account003);
			accountRepository.save(account004);


			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 550, "Iphone 17 used ", LocalDateTime.now(), account001);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 300.99, "Counter Strike GO Skin ak 47", LocalDateTime.now().plusDays(1), account001);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 200.50, "selection t-shirt ", LocalDateTime.now().plusDays(2), account001);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 100.30, "boca t-shirt", LocalDateTime.now().plusDays(3), account001);
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, 2, "coca cola", LocalDateTime.now().plusDays(4), account001);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, 3, "Burger King hamburger", LocalDateTime.now().plusDays(5), account001);
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, 500, "mac apple", LocalDateTime.now().plusDays(6), account001);


			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);


			List<Integer> mortgagePayments = List.of(12,24,36,48,60);
			Loan Mortgage = new Loan("Mortgage", 500000, mortgagePayments);

			List<Integer> personalPayments = List.of(6,12,24);
			Loan Personal = new Loan("Personal", 100000, personalPayments);

			List<Integer> carLoanPayments = List.of(6,12,24,36);
			Loan CarLoan = new Loan("CarLoan", 300000, carLoanPayments);

			loanRepository.save(Mortgage);
			loanRepository.save(Personal);
			loanRepository.save(CarLoan);


			ClientLoan clientloan1 = new ClientLoan(400000, 60, new Date() , melba, Mortgage);
			ClientLoan clientloan2 = new ClientLoan(200000, 12, new Date(), melba, Personal);
			clientLoanRepository.save(clientloan1);
			clientLoanRepository.save(clientloan2);

			ClientLoan clientloan3 = new ClientLoan(100000, 24, new Date() , Pedro, Personal);
			ClientLoan clientloan4 = new ClientLoan(200000, 36, new Date(), Pedro, CarLoan);
			clientLoanRepository.save(clientloan3);
			clientLoanRepository.save(clientloan4);




		};
	}


}
