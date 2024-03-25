package com.gundemgaming.fukantin.tools;

import java.util.ArrayList;

public class BannedWordManager {

    //Activity variables
    private ArrayList<String> bannedArraylist;
    private boolean isUsernameBanned;

    //BannedWordList - Class
    private BannedWordList bannedWordList;

    public BannedWordManager(String username) {

        isUsernameBanned(username);

    }

    //Username Check
    public void isUsernameBanned(String username) {

        bannedWordList = new BannedWordList();
        bannedArraylist = bannedWordList.getBannedWordList();

        for(int i=0; i<bannedArraylist.size(); i++) {
            String bannedWord = bannedArraylist.get(i);
            if(username.contains(bannedWord)) {
                isUsernameBanned = true;
            }
            int lastElementOfBannedArrayList = bannedArraylist.size() - 1;
            if(i == lastElementOfBannedArrayList && !isUsernameBanned) {
                isUsernameBanned = false;
            }
        }
    }
    public boolean isUsernameBanned() {
        return isUsernameBanned;
    }
}
