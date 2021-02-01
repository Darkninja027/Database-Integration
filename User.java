
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class User {

    private int userID;
    private String username;
    private String password;
    private String email;
    private String dateOfBirth;
    private String salt;

    /**
     * The constructor for the user class for the object that is stored inside the json file 
     * @param user The username of the user
     * @param pass The password (in clear text) for the user - the password is encrypted later 
     * @param em The email address for the user
     * @param dob
     * @param filename
    */
    public User(String user, String pass, String em, String dob, String filename) {
        this.username = user;
        this.password = pass;
        this.email = em;
        this.dateOfBirth = dob;
        this.userID = setID(filename);
        this.salt = generateSalt();
    }

    public JSONObject createJson() {
        JSONObject obj = new JSONObject();
        obj.put("ID", userID);
        obj.put("Username", username);
        obj.put("Password", password);
        obj.put("Email", email);
        obj.put("Date of Birth", dateOfBirth);
        obj.put("Salt Key", salt);
        return obj;

    }

    public String getUsername() {
        return this.username;
    }
    public String getPass(){return this.password;}

    public void hashPass(String pass) {
        MessageDigest md;
        String passToHash = pass+salt;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(passToHash.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for(byte b : digest){
                sb.append(String.format("%02x", b & 0xff));
            }

            this.password = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        

    }

    private int setID(String filename) {
        int id = 10000;
        File file = new File(filename);
        if (file.length() == 0) {
            return id;
        } 
        else {
            try (FileReader fr = new FileReader(filename)) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(fr);
                JSONArray array = (JSONArray)obj;
                id += array.size();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return id;
    }

    public void submit(String filename){
        JSONObject jObject = createJson();
        JSONObject u = new JSONObject();
        JSONArray array = new JSONArray();
        u.put("Users", jObject);

        File file = new File(filename);
        if(file.length()==0){
            try(FileWriter fw = new FileWriter(filename)) {
                array.add(u);
                fw.write(array.toJSONString());
                fw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try(FileReader fr = new FileReader(filename)){
                JSONParser parser = new JSONParser();
                Object object = parser.parse(fr);
                array = (JSONArray)object;
                array.add(u);
                try (FileWriter fw = new FileWriter(filename)){
                   fw.write(array.toJSONString());
                   fw.flush(); 
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            catch(IOException e){e.printStackTrace();}
            catch(org.json.simple.parser.ParseException e){e.printStackTrace();}
        }
    }

    private String generateSalt(){
        String charsToUse = "ABCDEFGHIJKLMONPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        int size = 16;
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for(int i =0; i < size; i++){
            builder.append(charsToUse.charAt(random.nextInt(charsToUse.length())));
        }
        return builder.toString();
    }
}
