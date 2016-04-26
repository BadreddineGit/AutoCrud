package com.ofppt.dao.crud;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ofppt.common.InstanceFactory;
import com.ofppt.dao.sessionfactory.HibernateUtilXml;
import com.ofppt.metier.ClassTrait;

public class CrudDaoImpl {

	private static CrudDaoImpl instance;
	private SessionFactory factory;

	private CrudDaoImpl() {
		factory = HibernateUtilXml.getSessionFactory();
	}

	public static synchronized CrudDaoImpl getInstance() {
		if (instance == null) {
			instance = new CrudDaoImpl();
		}

		return instance;
	}

	public void create(Object obj) {

		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			session.save(obj);
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null)
				tx.rollback();
			e.printStackTrace();

		} finally {
			session.close();
		}

	}

	public Object read(Integer personneID, Class<? extends Object> type) {

		Session session = factory.openSession();
		Transaction tx = null;
		Object obj = null;

		try {

			tx = session.beginTransaction();
			obj = session.get(type, personneID);
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null)
				tx.rollback();
			e.printStackTrace();

		} finally {
			session.close();
		}

		return obj;
	}

	public void update(Integer objectId, Object newObject) {

		ClassTrait classTrait = InstanceFactory.getClassTrait();

		Session session = factory.openSession();
		Transaction tx = null;
		Object object;

		try {

			tx = session.beginTransaction();
			object = session.get(newObject.getClass(), objectId);
			classTrait.update(object, newObject);
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null)
				tx.rollback();
			e.printStackTrace();

		} finally {
			session.close();
		}

	}

	public void delete(Integer id, Class<? extends Object> type) {

		Session session = factory.openSession();
		Transaction tx = null;
		Object obj = null;

		try {

			tx = session.beginTransaction();
			obj = session.get(type, id);
			session.delete(obj);

			tx.commit();

		} catch (HibernateException e) {

			if (tx != null)
				tx.rollback();
			e.printStackTrace();

		} finally {
			session.close();
		}

	}

	public List<Object> readAll(Class<? extends Object> type) {

		Session session = factory.openSession();
		Transaction tx = null;
		List<Object> objects = null;

		try {

			tx = session.beginTransaction();
			objects = session.createCriteria(type).list();
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null)
				tx.rollback();
			e.printStackTrace();

		} finally {
			session.close();
		}
		return objects;
	}

}
