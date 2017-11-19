package com.example.bot.spring;

import com.example.bot.spring.DietbotController.DownloadedContent;
import com.rivescript.RiveScript;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;



public class RecommendFriendState extends State {
    // Constant values
    private static final String FRIEND_TRIGGER = "friend";
    private static final String DIGIT_REGEX = "[0-9]+";

    /**
     * Default constructor for RecommendFriendState
     */
    public RecommendFriendState() {
        
    }

    public String matchTrigger(String text) {
		if(text.equals(FRIEND_TRIGGER)) {
			return "FRIEND";
		}
        else{
            String[] splitText = text.split(" ");
            if(splitText.size == 2 && splitText[0].equals("code") && splitText[1].matches(DIGIT_REGEX)){
                return "CODE";
            }
            else{
                return "nothing";
            }
        }
    }

    public String decodeCodeMessage(String text) {
    	return text.split(" ")[1];
    }
 
    public Vector<String> replyForFriendCommand(String userId) {
        SQLDatabaseEngine sql = new SQLDatabaseEngine();
        int newCode = sql.generateAndStoreCode(userId);
        String newCodeString = "Thank you, your code is " + Integer.toString(newCode);
        Vector<String> vec = new Vector<String>(0);
        vec.add(newCodeString);
        return vec;
    }

    public Vector<String> actionForCodeCommand(String userId, String code) {
        SQLDatabaseEngine sql = new SQLDatabaseEngine();
        Vector<String> vec = new Vector<String>(0);

        if(!sql.searchUser(userId, "campaign_user")){
            // The user cannot claim
            vec.add("Sorry, you cannot claim coupon!");
        }
        else{
            ArrayList<String> ls = sql.getCodeInfo(Integer.valueOf(code));
            String requestUser = ls.get(0);
            String claimUser = ls.get(1);
            if(requestUser == null){
                // This code does not exist
                vec.add("Sorry, this code does not exist!");
            }
            else if(claimUser != null){
                // Someone claimed this coupon ady
                vec.add("Sorry, this code had been claimed!");
            }
            else{
                vec.add(requestUser);
                vec.add(claimUser);
            }  
        }
        return vec;
    }

    /**
     * Reply a message for input text
     * @param text A String data type
     * @return A String data type
     */
	public String reply(String userId, String text, RiveScript bot) {
		return "This function is not used";
	}
}