package com.sin.eugene.tripcomposer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CountryDAO {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void insert(Country person) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(person);
		session.getTransaction().commit();
	}
	
	public void insertAll(List<Country> country) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();
		country.forEach(session::save);
		transaction.commit();
	}

	public List<Country> selectAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Country.class);
		
		@SuppressWarnings("unchecked")
		List<Country> country = (List<Country>) criteria.list();
		session.getTransaction().commit();
		return country;
	}

}
