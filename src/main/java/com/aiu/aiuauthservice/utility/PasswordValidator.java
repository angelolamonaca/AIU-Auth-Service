package com.aiu.aiuauthservice.utility;

import org.passay.*;

import java.util.Arrays;
import java.util.List;

public class PasswordValidator {

    public List<String> validate(String password) {
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(

                // at least 8 characters
                new LengthRule(8, 30),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));
        return validator.getMessages(result);
    }
}
