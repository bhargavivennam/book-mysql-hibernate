package com.example.bookmysql;

import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BookMysqlApplication {

	public static void main(String[] args) {
//		SpringApplication.run(BookMysqlApplication.class, args);

		var bookOne = new Book("Clean Code", "Robert Cecil Martin");
		var bookTwo = new Book("Spring in action", "Craig Walls");

		Transaction transaction = null;

		// insert the record boook information
		transaction = createBookDetails(transaction, bookOne, bookTwo);

		// Fetching the details
		 getBookDetails(transaction);

	}

	private static Transaction createBookDetails(Transaction transaction, Book bookOne, Book bookTwo) {
		try(var session = HibernateHelper.getSessionFactory().openSession()) {
			// start transaction
			transaction = session.beginTransaction();

			session.save(bookOne);
			session.save(bookTwo);

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return transaction;
	}

	private static void getBookDetails(Transaction transaction) {
		try (var session = HibernateHelper.getSessionFactory().openSession()) {
			List<Book> books = session.createQuery("from Book", Book.class).list();
			books.forEach(System.out::println);
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
}
