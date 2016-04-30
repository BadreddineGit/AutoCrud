package com.ofppt.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtilXml {

	public static SessionFactory getSessionFactory() {

		SessionFactory sessionfactory = null;

		try {

			sessionfactory = new Configuration().configure().buildSessionFactory();

		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);

		}

		return sessionfactory;

	}

}
