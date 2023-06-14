package com.masai.CarDao;

import java.util.List;

import com.masai.Carbooking.com.masai.car.LoggedInId;
import com.masai.Entity.User;
import com.masai.Exception.SomethingWentwrongException;
import com.masai.Utilities.DBUtilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

public class UserImplDao implements UserDaoInterface {

	@Override
	public void adduser(User u) throws SomethingWentwrongException {
		EntityManager em=null;
		EntityTransaction et=null;
		try {
			em=DBUtilities.createconnection();
			Query query = em.createQuery("SELECT count(c) FROM User c WHERE username = :username");
			query.setParameter("username", u.getUsername());
			if((Long)query.getSingleResult() > 0) {
				//you are here means company with given name exists so throw exceptions
				throw new SomethingWentwrongException("The username" + u.getUsername() + " is already occupied");
			}
			et=em.getTransaction();
			et.begin();
			em.persist(u);
			et.commit();
		}catch( PersistenceException ex) {
		 throw new SomethingWentwrongException("Unable to add data");
	}finally {
		em.close();
		}
	}

	@Override
	public void login(String username, String password) throws SomethingWentwrongException {
		
			EntityManager em = null;
			try {
				em = DBUtilities.createconnection();
				
				Query query = em.createQuery("SELECT c.id FROM User c WHERE username = :username AND passwrod = :password AND isDeleted = 0");
				query.setParameter("username", username);
				query.setParameter("password", password);
				List<Integer> listInt = (List<Integer>)query.getResultList();
				if(listInt.size() == 0) {
					//you are here means company with given name exists so throw exceptions
					throw new SomethingWentwrongException("The username or password is incorrect");
				}
				LoggedInId.loginId= listInt.get(0);
			}catch(PersistenceException ex) {
				throw new SomethingWentwrongException("Unable to process request, try again later");
			}finally{
				em.close();
			}
		}

	@Override
	public void changepassword(String oldpassword, String newPassword) throws SomethingWentwrongException {
		
			EntityManager em = null;
			try {
				em = DBUtilities.createconnection();
				Query query = em.createQuery("SELECT count(c) FROM User c WHERE passwrod = :oldPassword AND id = :id");
				query.setParameter("oldPassword", oldpassword);
				query.setParameter("id", LoggedInId.loginId);
				Long userCount = (Long)query.getSingleResult();
				if(userCount == 0) {
					//you are here old password is incorrect for logged in user
					throw new SomethingWentwrongException("Invalid old password");
				}
				//You are here means all checks done, We can proceed for changing the password
				User customer = em.find(User.class,  LoggedInId.loginId);
				EntityTransaction et = em.getTransaction();
				et.begin();
				customer.setPasswrod(newPassword);
				et.commit();
			}catch(PersistenceException ex) {
				throw new SomethingWentwrongException("Unable to process request, try again later");
			}finally{
				em.close();
			}
		
		
	}
		
	

}
