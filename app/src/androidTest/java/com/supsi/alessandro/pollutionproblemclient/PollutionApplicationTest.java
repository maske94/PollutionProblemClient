package com.supsi.alessandro.pollutionproblemclient;

import android.support.test.runner.AndroidJUnit4;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.storage.PollutionSharedPreferences;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Alessandro on 21/06/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PollutionApplicationTest {
    private static PollutionSharedPreferences mSharedPreferences;

    @BeforeClass
    public static void setUp(){
        mSharedPreferences = PollutionSharedPreferences.getInstance();
    }

    @Before
    public void beforeEachTest(){
        mSharedPreferences.cleanAll();
    }

    @Test
    public void deviceIdMatchIsCorrect(){
        String username = "maske94";
        mSharedPreferences.storeUser(username,"pippo");

        Child child1 = new Child(username,"Leonardo","id1215ds");
        Child child2 = new Child(username,"Michelangelo","askdfj89");
        Child child3 = new Child(username,"Valentino","asdfa9834on");
        child1.setChildId("child1");
        child2.setChildId("child2");
        child3.setChildId("child3");
        mSharedPreferences.storeChild(child1);
        mSharedPreferences.storeChild(child2);
        mSharedPreferences.storeChild(child3);

        String child1Id = PollutionApplication.getChildId(child1.getDeviceId());
        String child2Id = PollutionApplication.getChildId(child2.getDeviceId());
        String child3Id = PollutionApplication.getChildId(child3.getDeviceId());

        /**
         * Assertions
         */

        assertNotNull(child1Id);
        assertNotNull(child2Id);
        assertNotNull(child3Id);
        assertEquals(child1.getChildId(),child1Id);
        assertEquals(child2.getChildId(),child2Id);
        assertEquals(child3.getChildId(),child3Id);

    }

    @Test
    public void noUsernameIsLoggedIn(){
        String childId = PollutionApplication.getChildId("4skvldjnds9");
        assertNull(childId);
    }

    @Test
    public void noChildIsStored(){
        String username = "maske94";
        mSharedPreferences.storeUser(username,"pippo");

        String childId = PollutionApplication.getChildId("4skvldjnds9");
        assertNull(childId);
    }

    @Test
    public void matchedNotFound(){
        String username = "maske94";
        mSharedPreferences.storeUser(username,"pippo");

        Child child1 = new Child(username,"Leonardo","id1215ds");
        child1.setChildId("child1");
        mSharedPreferences.storeChild(child1);
        String child1Id = PollutionApplication.getChildId("notExist");


        /**
         * Assertions
         */
        assertNull(child1Id);

    }
}
