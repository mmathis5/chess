package service;
import dataAccess.*;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.InternalFailureException;
import dataAccess.Exceptions.UsernameExistsException;
import model.*;

import javax.xml.crypto.Data;
import java.util.Objects;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException, UsernameExistsException {
        //check if the username already exists in the database
        String username = user.getUsername();
        if (this.userDAO.userExists(username)){
            throw new UsernameExistsException("this user already exists");
        }
        //insert into the hashmap
        this.userDAO.addUser(username, user);
        //get the next authToken
        String authToken = this.authDAO.generateAuthToken();
        //generate a new AuthData object
        AuthData authData = new AuthData(authToken, username);
        //add the AuthData object into the appropriate HashMap
        this.authDAO.addAuthData(authToken, authData);
        //return the AuthData object
        return authData;
    }
        public AuthData login(String username, String password) throws DataAccessException, InternalFailureException {
            try {
                //check if the username already exists in the database
                if (!this.userDAO.userExists(username)) {
                    throw new DataAccessException("this user doesn't exists");
                }
                //validate that username and password match
                UserData userData = this.userDAO.getUser(username);
                String recordedPassword = userData.getPassword();
                if (!Objects.equals(recordedPassword, password)) {
                    throw new DataAccessException("password doesn't match user");
                }
                String authToken = this.authDAO.generateAuthToken();
                //generate a new AuthData object
                AuthData authData = new AuthData(authToken, username);
                //add the AuthData object into the appropriate HashMap
                this.authDAO.addAuthData(authToken, authData);
                //return the AuthData object
                return authData;
            }
            catch (DataAccessException e){
                throw e;
            }
            catch (Exception e){
                throw new InternalFailureException("Something went wrong on our end");
            }
        }

        public void logout(String authToken) throws DataAccessException, InternalFailureException{
            try {
                AuthData authData = this.authDAO.getAuth(authToken);
                if (authData == null) {
                    throw new DataAccessException("This is not a valid AuthToken");
                }
                this.authDAO.deleteAuth(authToken);
            }
            catch(DataAccessException e){
                throw e;
            }
            catch (Exception e){
                throw new InternalFailureException("Something went wrong internally");
            }
        }


}
