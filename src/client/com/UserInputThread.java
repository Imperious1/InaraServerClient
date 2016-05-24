package client.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by blaze on 5/23/2016.
 */
class UserInputThread extends Thread {

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        try {
            while ((s = in.readLine()) != null) {
                if (s.equals("halt")) {
                    in.close();
                    break;
                }
                    else if (!s.isEmpty() && s.contains("Group: "))
                        Client.sendRequest(Client.toJson(new RequestModel().setRequestId(2).setSearchedName(s)));
                    else if (!s.isEmpty() && s.equals("imps")) {
                        Client.sendRequest(Client.toJson(new RequestModel().setRequestId(3)));
                    } else if (!s.isEmpty() && s.equals("feds"))
                        Client.sendRequest(Client.toJson(new RequestModel().setRequestId(4)));
                    else if (!s.isEmpty())
                        Client.sendRequest(Client.toJson(new RequestModel().setRequestId(1).setSearchedName(s)));
                }
                System.exit(0);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
