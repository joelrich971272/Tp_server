package btsciel.fr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            try {
                monServeur();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();


        }
    public static void monServeur() throws IOException {
        final int port = 5015;
        boolean deconnexionClientDemandee = false ;
        char [] bufferEntree = new char[65535] ;
        String reponse = null ;


        ServerSocket monServeurSocket = new ServerSocket(port) ;
        System.out.println("Serveur en fonctionnement");



        while (true) {
            //appelle de la méthode accept() qui renvoie une socket lors d'une nouvelle connexion
            Socket socketDuClient = monServeurSocket.accept();
            System.out.println("Connexion avec : " + socketDuClient.getInetAddress());

            //obtenir un flux en entrée et en sortie à partir de la socket
            BufferedReader fluxEntree = new BufferedReader(new InputStreamReader(socketDuClient.getInputStream()));
            PrintStream fluxSortie = new PrintStream(socketDuClient.getOutputStream());

            //écrire les traitements à réaliser
            while (!deconnexionClientDemandee && socketDuClient.isConnected()) {
                System.out.println("attente...");
                fluxSortie.println("Entrez une phase qui sera mise en majuscule par le serveur (exit pour finir)");
                //reception
                int NbLus = fluxEntree.read(bufferEntree);
                String messageRecu = new String(bufferEntree, 0, NbLus);
                if (messageRecu.length() != 0) {
                    System.out.println("\t\t Message reçu : " + messageRecu.toUpperCase());
                }


                if (messageRecu.equalsIgnoreCase("exit")||messageRecu.equalsIgnoreCase("fin")) {
                    reponse = "JE VOUS DECONNECTE !!!";
                    deconnexionClientDemandee = true;
                } else {
                    System.out.println("\t\t Traitement Requete ... ");
                    reponse = messageRecu;

                }
                if (messageRecu.equalsIgnoreCase("echo\r\n")) {
                    reponse = " "+ messageRecu.substring(4)+" ";
                    deconnexionClientDemandee = true;

                } else if (messageRecu.equalsIgnoreCase("hello\r\n")) {
                    reponse = " BONJOUR";

                } else if (messageRecu.equalsIgnoreCase("you\r\n") || messageRecu.equalsIgnoreCase("whoareyou?\r\n")) {

                    reponse = "Je suis " +InetAddress.getLocalHost() + " et vous etes sur le port " + monServeurSocket.getLocalPort();

                } else if (messageRecu.equalsIgnoreCase("me\r\n") ||messageRecu.equalsIgnoreCase("whoami?\r\n")){
                    reponse = " Vous etes " + socketDuClient.getInetAddress() +"et je travail sur le port: " + socketDuClient.getPort();

                } else if (messageRecu.equalsIgnoreCase("time\r\n")) {
                    reponse = " Voici la date et l'heure: " + LocalDateTime.now() ;

                }
                fluxSortie.println(reponse);
                System.out.println("\t\t Message emis : " + reponse.toUpperCase());
            }
            //client vient de se deconnecter !!!
            socketDuClient.close();
            //faut réarmé pour le client suivant
            deconnexionClientDemandee = false;



            socketDuClient.close();
        }
    }

    }


