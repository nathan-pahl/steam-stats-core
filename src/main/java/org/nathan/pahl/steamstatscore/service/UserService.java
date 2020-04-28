package org.nathan.pahl.steamstatscore.service;

import org.nathan.pahl.steamstatscore.regex.InputRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private transient final Logger logger = LoggerFactory.getLogger(this.getClass());

    private InputRegex inputRegex;

    @Autowired
    public UserService(InputRegex inputRegex) {
        this.inputRegex = inputRegex;
    }

    public Long parseInputForLong(String input) {
        String toParse = inputRegex.matchSteamId(input);
        try {
            return Long.parseLong(toParse);
        } catch (NumberFormatException e) {
            this.logger.info("User entered string");
        }
        return null;
    }

    public String parseInputForUsername(String input) {
        return inputRegex.matchSteamUsername(input);
    }

}