package multiplayer.client;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CustomInputValidator class that contains function to validate user input
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class CustomInputValidator {

    /**
     * CustomInputValidator constructor
     */
    public CustomInputValidator () {}

    /**
     * Validates whether the password string matches the regex
     *
     *@param password password to validate
     *@param regex regex the password should match
     *
     * @return boolean password matches regex?
     */
    public boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Validates whether the email is a valid email adress
     *
     *@param email email to validate
     *
     * @return boolean email is valid?
     */
    public boolean isValidEmailAddress(String email) {
        // create the EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();

        // check for valid email addresses using isValid method
        return validator.isValid(email);
    }

}
