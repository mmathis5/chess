package dataAccessTests;

import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InternalFailureException;
import dataAccess.exceptions.UsernameExistsException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class DAOTests {
    @Test
    @Order(1)
    @DisplayName("Successful Registration")
    public void successRegister() throws DataAccessException, UsernameExistsException, InternalFailureException {

    }
}
