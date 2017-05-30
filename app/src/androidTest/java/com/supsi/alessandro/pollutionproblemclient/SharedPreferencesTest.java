package com.supsi.alessandro.pollutionproblemclient;

import android.support.test.runner.AndroidJUnit4;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.User;
import com.supsi.alessandro.pollutionproblemclient.storage.PollutionSharedPreferences;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by Alessandro on 30/05/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesTest {

    private static PollutionSharedPreferences mPollutionSharedPreferences;

    @BeforeClass
    public static void setUp(){
        mPollutionSharedPreferences = PollutionSharedPreferences.getInstance();
    }

    @Before
    public void beforeEachTest(){
        mPollutionSharedPreferences.cleanAll();
    }

    @Test
    public void savedUserIsCorrect() throws Exception {
        User user = new User("maske94","pass","Alessandro","Mascheroni","04-05-2017","Dongo");

        mPollutionSharedPreferences.storeUser(user.getUsername(),user.getPassword());

        String storedUsername = mPollutionSharedPreferences.getStoredUsername();
        String storedPassword = mPollutionSharedPreferences.getStoredPassword();

        // Assertions
        assertEquals(user.getUsername(),storedUsername);
        assertEquals(user.getPassword(),storedPassword);
    }

    @Test
    public void noUserIsStored(){
        String storedUsername = mPollutionSharedPreferences.getStoredUsername();
        String storedPassword = mPollutionSharedPreferences.getStoredPassword();
        ArrayList<Child> storedChildrenList = mPollutionSharedPreferences.getStoredChildrenList();

        // Assertions
        assertNull(storedUsername);
        assertNull(storedPassword);
        assertNull(storedChildrenList);
    }

    @Test
    public void childrenListIsCorrect(){
        User user = new User("maske94","pass","Alessandro","Mascheroni","04-05-2017","Dongo");

        Child child1 = new Child(user.getUsername(),"Pippo","2897348hf827");
        child1.setChildId("42543jh");
        Child child2 = new Child(user.getUsername(),"Pluto","skdlvjsn88745");
        child2.setChildId("3563453jh");

        mPollutionSharedPreferences.storeUser(user.getUsername(),user.getPassword());
        mPollutionSharedPreferences.storeChild(child1);
        mPollutionSharedPreferences.storeChild(child2);

        ArrayList<Child> storedChildrenList = mPollutionSharedPreferences.getStoredChildrenList();

        // Assertions
        assertEquals(2,storedChildrenList.size());
        assertEquals(child1,storedChildrenList.get(0));
        assertEquals(child2,storedChildrenList.get(1));
    }

    @Test
    public void removeChildIsCorrect(){
        User user = new User("maske94","pass","Alessandro","Mascheroni","04-05-2017","Dongo");

        Child child1 = new Child(user.getUsername(),"Pippo","2897348hf827");
        child1.setChildId("42543jh");
        Child child2 = new Child(user.getUsername(),"Pluto","skdlvjsn88745");
        child2.setChildId("3563453jh");

        mPollutionSharedPreferences.storeUser(user.getUsername(),user.getPassword());
        mPollutionSharedPreferences.storeChild(child1);
        mPollutionSharedPreferences.storeChild(child2);

        ArrayList<Child> storedChildrenList = mPollutionSharedPreferences.getStoredChildrenList();

        // Assertions
        assertEquals(2,storedChildrenList.size());
        assertEquals(child1,storedChildrenList.get(0));
        assertEquals(child2,storedChildrenList.get(1));

        ArrayList<Child> childrenListAfterRemoved = mPollutionSharedPreferences.removeChild(child1);

        // Assertions
        assertEquals(1,childrenListAfterRemoved.size());
        assertEquals(child2,childrenListAfterRemoved.get(0));

        ArrayList<Child> childrenListAfterSecondRemoved = mPollutionSharedPreferences.removeChild(child2);

        // Assertions
        assertEquals(0,childrenListAfterSecondRemoved.size());
    }

    @Test
    public void noChildToRemove(){
        ArrayList<Child> childrenList = mPollutionSharedPreferences.removeChild(new Child("pippo","gsfs","sgs"));
        assertNull(childrenList);
    }
}
