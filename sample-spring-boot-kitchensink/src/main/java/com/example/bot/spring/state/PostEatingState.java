package com.example.bot.spring;

//import java.io.IOException;
import com.rivescript.RiveScript;

public class PostEatingState extends State {

    boolean extractFood;

    /**
     * Default constructor for PostEatingState
     */
	public PostEatingState() {
	}

    /**
     * Reply a message for input text in this state
     * @param userId String data type
     * @param text String data type
     * @param bot RiveScript data type 
     * @return String data type as the reply
     */
	public String reply(String userId, String text, RiveScript bot) {
		String currentState = bot.getUservar(userId, "state");
        String topic = bot.getUservar(userId, "topic");

        String output = bot.reply(userId, text);
        String afterState = null;

        // when user is not typing leave
        if ( !output.equals("Okay. Tell me when you need help~~~") ) {

            // simple preprocessing by trimming new line character
            text = text.replace("\r", " ").replace("\n", " ").replace("  ", " ");

            // save the result in DB if the text is not empty
            if (text.length() > 1) {
                // save the date, userId, food in DB
                sql.addUserEatingHistory(userId, text);
            }
        }

		afterState = bot.getUservar(userId, "state");

        if (sql.exceedDailyCalorieQuota(userId)) {
             output += "\nWARNING!!! You have exceeded daily calorie quota.";
        }

		// write to DB
		syncSQLWithRiveScript(userId, bot);
		return output;
	}
}