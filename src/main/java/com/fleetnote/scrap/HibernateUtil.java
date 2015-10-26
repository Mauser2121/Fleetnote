package com.fleetnote.scrap;



import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	public static final SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	static{
		try{
			System.out.println(new Date() + "  : Initialisation de la session factory hibernate");
			Configuration config = new Configuration().configure();
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			sessionFactory = config.buildSessionFactory(serviceRegistry);
		}catch(Throwable ex){
			throw new ExceptionInInitializerError(ex);
		}
	}
	public static Session currentSession() throws HibernateException{
		Session s = (Session) session.get();
		if(s == null){
			s = sessionFactory.openSession();
			session.set(s);
		}
		return s;
	}
	public static void closeSession() throws HibernateException{
		Session s  = (Session)session.get();
		if(s != null)
			s.close();
		session.set(null);
	
	}
}
