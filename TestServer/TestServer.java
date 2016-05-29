/**
 * A program to test the web server
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class TestServer{
    public static void main(String args[]) throws Exception {

        if(args.length != 2){
            System.out.println("Usage: TestServer lolcahost portNumber");
            System.exit(1);
        }

        String re1 = "GET /tiny.html HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Close\r\n" + "\r\n";
        String re2 = "GET /hello.html HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Close\r\n" + "\r\n";
        String re3 = "CONNECT /tiny.html HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Close\r\n" +"\r\n";
        String re4 = "TRACE /tiny.html HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Close\r\n" +"\r\n";
        String re5 = "PUT /tiny.html HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Close\r\n" +"\r\n";
        String re6 = "DELETE /tiny.html HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Close\r\n" +"\r\n";
        String re7 = "GET tiny.html HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Close\r\n" +"\r\n";

        boolean[] results = {false, false, false,false, false, false,false};

        Socket sock = new Socket(args[0], Integer.parseInt(args[1]));
        OutputStream outs = sock.getOutputStream();
        outs = sock.getOutputStream();
        outs.write(re1.getBytes());
        Scanner scan = new Scanner(sock.getInputStream());
        String header = scan.nextLine();
        if(header.contains("200")) results[0] = true; else results[0] = false;
        sock.close();

        sock = new Socket(args[0], Integer.parseInt(args[1]));
        outs = sock.getOutputStream();
        outs = sock.getOutputStream();
        outs.write(re2.getBytes());
        scan = new Scanner(sock.getInputStream());
        header = scan.nextLine();
        if(header.contains("404")) results[1] = true; else results[1] = false;
        sock.close();

        sock = new Socket(args[0], Integer.parseInt(args[1]));
        outs = sock.getOutputStream();
        outs = sock.getOutputStream();
        outs.write(re3.getBytes());
        scan = new Scanner(sock.getInputStream());
        header = scan.nextLine();
        if(header.contains("501")) results[2] = true; else results[2] = false;
        sock.close();

        sock = new Socket(args[0], Integer.parseInt(args[1]));
        outs = sock.getOutputStream();
        outs = sock.getOutputStream();
        outs.write(re4.getBytes());
        scan = new Scanner(sock.getInputStream());
        header = scan.nextLine();
        if(header.contains("405")) results[3] = true; else results[3] = false;
        sock.close();

        sock = new Socket(args[0], Integer.parseInt(args[1]));
        outs = sock.getOutputStream();
        outs = sock.getOutputStream();
        outs.write(re5.getBytes());
        scan = new Scanner(sock.getInputStream());
        header = scan.nextLine();
        if(header.contains("405")) results[4] = true; else results[4] = false;
        sock.close();

        sock = new Socket(args[0], Integer.parseInt(args[1]));
        outs = sock.getOutputStream();
        outs = sock.getOutputStream();
        outs.write(re6.getBytes());
        scan = new Scanner(sock.getInputStream());
        header = scan.nextLine();
        if(header.contains("405")) results[5] = true; else results[5] = false;
        sock.close();

        sock = new Socket(args[0], Integer.parseInt(args[1]));
        outs = sock.getOutputStream();
        outs = sock.getOutputStream();
        outs.write(re7.getBytes());
        scan = new Scanner(sock.getInputStream());
        header = scan.nextLine();
        if(header.contains("400")) results[6] = true; else results[6] = false;
        sock.close();

        System.out.println("Test 1 - 200 OK, Pass: " + results[0]);
        System.out.println("Test 2 - 404 Not Found, Pass: " + results[1]);
        System.out.println("Test 3 - 501 Not Implemented, Pass: " + results[2]);
        System.out.println("Test 4 - 405 TRACE Not Allowed, Pass: " + results[3]);
        System.out.println("Test 5 - 405 PUT Not Allowed, Pass: " + results[4]);
        System.out.println("Test 6 - 405 DELETE Not Allowed, Pass: " + results[5]);
        System.out.println("Test 7 - 400 Bad Request, Pass: " + results[6]);
    }
}