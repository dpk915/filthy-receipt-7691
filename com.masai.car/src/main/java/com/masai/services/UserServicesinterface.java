package com.masai.services;

import com.masai.Entity.User;
import com.masai.Exception.SomethingWentwrongException;

public interface UserServicesinterface {
	public void adduserservices(User u) throws SomethingWentwrongException;
	public void loginuser(String username,String Password) throws SomethingWentwrongException;
    public void changeoldpassword(String oldpassword,String newpassword) throws SomethingWentwrongException;
}
