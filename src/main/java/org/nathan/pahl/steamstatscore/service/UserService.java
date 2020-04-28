package org.nathan.pahl.steamstatscore.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final String steamUrlIdRegex = "https://steamcommunity.com/profiles/(\\d+)|(\\d+)";
    private final String steamUrlUsernameRegex = "https:\\/\\/steamcommunity.com\\/profiles\\/(\\w+)|(\\w+)";


    private transient final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Long parseInputForLong(String input) {
        Pattern pattern = Pattern.compile(steamUrlIdRegex);
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        String group = matcher.group(1);
        try {
            return Long.parseLong(group);
        } catch (NumberFormatException e) {
            this.logger.info("User entered string");
        }
        return null;
    }

    public String parseInputForUsername(String input) {
        Pattern pattern = Pattern.compile(steamUrlUsernameRegex);
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        return matcher.group(1);
    }

}