package btsciel.fr.Model;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Outils {




    public ArrayList<Ip_v4> nom () throws SocketException {
        ArrayList<Ip_v4> nomInetAdress = new ArrayList<>();
        Enumeration Interfaces = NetworkInterface.getNetworkInterfaces();
        while (Interfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) Interfaces.nextElement();
            System.out.println(ni.getDisplayName());
        }
        return nomInetAdress;
    }
}
