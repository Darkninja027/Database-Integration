import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Validators {
    private File file;
    private JSONParser jParser = new JSONParser();

    public Validators(String fName) {
        this.file = new File(fName);
    }

    /**
     * a check to see if the username entered is able to be used
     * 
     * @param uName the username being tested against the file
     * @return whether or not the username will be valid to use
     */
    public boolean validateUsername(String uName) {
        boolean validUser = true;
        if (file.length() == 0) {
            return validUser;
        } else {
            try (FileReader fr = new FileReader(file.getName())) {
                Object object = jParser.parse(fr);
                JSONArray array = (JSONArray) object;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject jObt1 = (JSONObject) array.get(i);
                    JSONObject jObj2 = (JSONObject) jObt1.get("Users");
                    if (jObj2.get("Username").toString().equalsIgnoreCase(uName)) {
                        validUser = false;
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            return validUser;
        }
    }

    /**
     * Checks the date being passed in as a parameter to see if it is a valid date
     * of birth
     * 
     * @param day   The day of the date
     * @param month The month of the date
     * @param year  The year of the date
     * @return A Boolean stating whether the date is valid or not
     */
    public boolean validateDate(String day, String month, String year) {
        boolean validDate = true;
        try {
            int d = Integer.parseInt(day);
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);

            if (m >= 1 && m <= 12) {
                if (m == 1) {
                    if (d < 1 && d > 31) {
                        validDate = false;
                    }
                } // january
                else if (m == 2) {
                    if (d < 1 && d > 29) {
                        validDate = false;
                    }
                } // february
                else if (m == 3) {
                    if (d < 1 && d > 31) {
                        validDate = false;
                    }
                } // march
                else if (m == 4) {
                    if (d < 1 && d > 30) {
                        validDate = false;
                    }
                } // april
                else if (m == 5) {
                    if (d < 1 && d > 31) {
                        validDate = false;
                    }
                } // may
                else if (m == 6) {
                    if (d < 1 && d > 30) {
                        validDate = false;
                    }
                } // june
                else if (m == 7) {
                    if (d < 1 && d > 31) {
                        validDate = false;
                    }
                } // july
                else if (m == 8) {
                    if (d < 1 && d > 31) {
                        validDate = false;
                    }
                } // august
                else if (m == 9) {
                    if (d < 1 && d > 30) {
                        validDate = false;
                    }
                } // september
                else if (m == 10) {
                    if (d < 1 && d > 31) {
                        validDate = false;
                    }
                } // october
                else if (m == 11) {
                    if (d < 1 && d > 30) {
                        validDate = false;
                    }
                } // november
                else if (m == 12) {
                    if (d < 1 && d > 31) {
                        validDate = false;
                    }
                } // december
            } else {
                validDate = false;
            }

            if (y < 1 && y > 9999) {
                validDate = false;
            }
        } catch (NumberFormatException e) {
            validDate = false;

        }
        return validDate;
    }

    public boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        return true;
    }

    public boolean validateEmail(String email) {
        boolean validEmail = true;
        Pattern pattern = Pattern.compile(
                "[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+");
        Matcher match = pattern.matcher(email);
        if (!match.matches()) {
            validEmail = false;
        }
        return validEmail;
    }

    public boolean validateLogin(String filename, String username, String password) {
        boolean validCredentials = false;
        try (FileReader fr = new FileReader(filename)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(fr);
            JSONArray array = (JSONArray)obj;
            for(int i = 0; i < array.size(); i++){
                JSONObject usersOuter = (JSONObject)array.get(i);
                JSONObject userInner = (JSONObject)usersOuter.get("Users");
                System.out.println(password);
                boolean validHash = validateHash(password, userInner.get("Password").toString(), userInner.get("Salt Key").toString());
                if(userInner.get("Username").toString().equalsIgnoreCase(username) &&
                validHash){
                    validCredentials = true;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return validCredentials;
    }

    private boolean validateHash(String password, String hash, String salt){
        boolean valid = false;
        MessageDigest md;
        String pass = password + salt;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for(byte b : digest){
                sb.append(String.format("%02x", b & 0xff));
            }
            if(hash.equals(sb.toString())){
                valid = true;
            }
            

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return valid;
    }
}
