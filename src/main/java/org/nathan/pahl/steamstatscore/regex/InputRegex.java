package org.nathan.pahl.steamstatscore.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class InputRegex {

    private final String steamUrlIdRegex = ".+\\/(\\w{0}\\d+$)|(^\\w{0}\\d+$)";
    private final String steamUrlUsernameRegex = "(\\w+$|\\d+$)";

    public String matchSteamId(String input) {
        Pattern pattern = Pattern.compile(steamUrlIdRegex);
        Matcher matcher = pattern.matcher(input);
        String toParse = null;
        while(matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                toParse = matcher.group(i);
                if(toParse != null) {
                    return toParse;
                }
            }
        }
        return null;
    }

    public String matchSteamUsername(String input) {
        Pattern pattern = Pattern.compile(steamUrlUsernameRegex);
        Matcher matcher = pattern.matcher(input);
        String toParse = null;
        while(matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                toParse = matcher.group(i);
                if(toParse != null) {
                    return toParse;
                }
            }
        }
        return null;
    }

}