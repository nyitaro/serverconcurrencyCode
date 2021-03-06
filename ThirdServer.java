import java.io.*;
import java.net.*;
import java.util.*;

import java.text.*;

class Concurrency extends Thread
{
    Socket connect; 

    public Concurrency(Socket connect)
    {
        this.connect = connect; 
    }

    public void run()
    {
        try
        {
            Scanner scan = new Scanner(connect.getInputStream()); 
            String line=null; 
            int nLines = 0; 
            String[]linesFromEnd = new String[32]; 

            while (true)
            {
                line = scan.nextLine();
                if(line.length()==0) 
                {
                    break;
                }
                linesFromEnd[nLines] = line;
                nLines = nLines + 1;
            }

            Scanner scans = new Scanner(linesFromEnd[0]); 

            String command = scans.next();
            String resources = scans.next();
            String fileName = "www" + resources; 
            String test = scans.next(); 

            Date date = new Date();
            SimpleDateFormat dateForm = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm z");
            String logFi= "Client Ip Address"+ connect.getInetAddress() + "resource" + resources; 

            String reply = ""; 
            String type = ""; 

            File inFile = new File (fileName); 

            if(inFile.getName().contains(".jpg") || inFile.getName().contains(".jpeg"))
            {
                type = "image/jpeg";
            }
            else
            if (inFile.getName().contains(".htm") ||inFile.getName().contains(".html"))
            {
                type = "text/html"; 
            }
            else 
            {
                type ="text/plain"; 
            }

            if(fileName.startsWith("/"))
            {
                fileName = fileName + "/"; 
            }

            if(inFile.isDirectory())
            {
                File files = new File(fileName + "index.htm");
                if(files.exists())
                {
                    inFile = files; 

                }
                else 

                    files = new File(fileName + "index.html");

                if(files.exists())
                {
                    inFile = files; 
                }
            }

            OutputStream a = connect.getOutputStream(); 

            if(command.equals("PUT") || command.equals("DELETE") || command.equals("TRACE"))
            {
                reply = "HTTP/1.0 405 Method Not Allowed\r\n"+
                "Connection: close" + "\r\n" +
                "Allow: GET, HEAD\r\n" + 
                "Content-Type:" + type + "\r\n"  
                + dateForm.format(date)+ "\r\n\r\n"+
                "<h1>405 Method Not Allowed<h1>";
                a.write(reply.getBytes()); 
                ThirdServer.logFile(logFi + "405 Method Not Allowed" );
            }
            else if (!command.equals("GET")&& !command.equals("HEAD"))
            {
                reply = "HTTP/1.0 501 Not Implemented\r\n" +
                "Connection: close" + "\r\n" + 
                "Content-Type:"+ type + "\r\n" +
                dateForm.format(date) + "\r\n\r\n"+
                "<h1>501 Not Implemented<h1>";
                a.write(reply.getBytes()); 
                ThirdServer.logFile(logFi + "501 Not Implemented" );
            }
            else if(!resources.startsWith("/"))
            {
                reply = "HTTP/1.0 400 Bad Request\r\n" + 
                "Connection: close" + "\r\n" + 
                "Content-Type:" + type + "\r\n" +
                dateForm.format(date) + "\r\n\r\n"+
                "<h1>400 Bad Resquest</h1>";
                a.write(reply.getBytes()); 
                ThirdServer.logFile(logFi + "400 Bad Request" );
            }
            else if (!inFile.exists())
            {
                reply = "HTTP/1.0 404 File Not Found\r\n" + 
                "Connection: close" +"\r\n" + 
                "Content-Type:" + type + "\r\n" +
                "Date : " + dateForm.format(date) + "\r\n" +
                "Content-Type: text/html "+ "\r\n\r\n"+
                "<h1>404 File Not Found </h1>";
                a.write(reply.getBytes()); 
                ThirdServer.logFile(logFi + "404 File Not Found" );
            }
            else 
            {
                reply = "HTTP/1.0 200 OK\r\n" + 
                "Conection: close" + "\r\n" + "Date: " + dateForm.format(date) + 
                "Content-Length: " + 
                inFile.length() + "r\n" + "Content-Type: " + type +
                "Content-Type: text/html " + "\r\n" +
                "\r\n\r\n" ;
                String sizestr = "bytes in file: " + inFile.length(); 
                a.write(reply.getBytes()); 
                ThirdServer.logFile(logFi + "200 OK" );

                if (!command.equals("HEAD"))
                {
                    InputStream fileInStream = new FileInputStream(inFile); 
                    byte[] buffer = new byte[256];

                    while (true)
                    {
                        int rc = fileInStream.read(buffer, 0, buffer.length); 
                        if (rc == -1)
                        {
                            break;
                        }
                        a.write(buffer, 0, rc); 
                    }

                }
                a.close(); 

            }
            connect.close();   
            } 
        catch (Exception e) {}
    }
}

public class ThirdServer
{ 
    private  static PrintWriter print = null; 

    public static void logFile(String log)
    {
        try {
            print = new PrintWriter(new FileOutputStream("logfile.txt", true), true);
        }
        catch (Exception e){}

        Date date = new Date();
        SimpleDateFormat dateForm = new SimpleDateFormat("EEE, DD MMM yyy HH:mm:ss Z");
        print.println("(" + dateForm.format(date) + ")" + log); 
    }

    public  static void main(String args[])
    { 
        try 
        {
            int port = Integer.parseInt(args[0]);
            ServerSocket serverSock = new ServerSocket(port); 

            while(true)
            {
                Socket connect = serverSock.accept(); 
                new Concurrency(connect).start(); 
            }
        } catch (Exception e){}
    }
}
