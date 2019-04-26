package com.m1k.fyp

import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTest {
    @Test
    fun isLoggedCorrectly() {
        //check we start off with noone logged in
        assertEquals(null, GlobalApp.getLogged())

        //set the logged in user
        GlobalApp.setLogged("test")

        //check the user was correctly set
        assertEquals("test", GlobalApp.getLogged())

        //check the class detects that a user is logged in
        assertEquals(true, GlobalApp.isLogged())

        //logout
        GlobalApp.logOut()

        //check the logout was successful
        assertEquals(null, GlobalApp.getLogged())

    }
}
