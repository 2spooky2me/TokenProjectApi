package com.jb.service;

import com.jb.client.ClientSession;

public interface LoginService {

    ClientSession companyCreateSession(String email, String password);

    ClientSession customerCreateSession(String email, String password);

    String generateToken();
}
