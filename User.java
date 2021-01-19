
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    public User(String user, String pass, String em, String dob, String filename) {
        this.username = user;
        this.password = pass;
        this.email = em;
        this.dateOfBirth = dob;
        this.userID = setID(filename);
    }

    public JSONObject createJson() {
        JSONObject obj = new JSONObject();
        obj.put("ID", userID);
        obj.put("Username", username);
        obj.put("Password", password);
        obj.put("Email", email);
        obj.put("Date of Birth", dateOfBirth);
        return obj;

    }

    public String getUsername() {
        return this.username;
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
                System.out.println(array.size());
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
                System.out.println("USERS: File empty");
                array.add(u);
                fw.write(array.toJSONString());
                fw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("File was not empty");
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
}
