package com.raffelberg.cr_ticker.profiles;

import com.google.firebase.auth.FirebaseAuth;

public class TickerEditor {

    private boolean logged_in;
    private static TickerEditor instance;

    private TickerEditor(){
        logged_in = true;
    }

    public static TickerEditor getInstance() {
        if(instance == null){
            instance = new TickerEditor();
            instance.setLogged_in(false);
            return instance;
        }
        return instance;
    }

    /**
     * sets user's log in status, if true authenticates and grants permission to write to firebase
     * @param logged_in -logIn-status to be set
     */
    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();
    }

    public boolean getLogged_in(){
        return logged_in;
    }
}
